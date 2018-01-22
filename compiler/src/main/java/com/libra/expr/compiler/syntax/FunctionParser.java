/*
 * MIT License
 *
 * Copyright (c) 2018 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.libra.expr.compiler.syntax;


import com.libra.Log;
import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.lex.Token;
import com.libra.expr.compiler.syntax.operator.Operator;
import com.libra.virtualview.common.ExprCommon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 16/10/17.
 */
public class FunctionParser extends Parser {
    private final static String TAG = "FunctionParser_TMTEST";

    private final static int STATE_ParamStart = 1;

    private int mState;
    private SyntaxParser mParamParser;
    private List<Expr> mParams = new ArrayList<Expr>();
    private RegisterExpr mResultExpr;

    private List<Integer> mFunNameIds;

    private int mObjectRegId;

    public void setNameIds(List<Integer> ids) {
        mFunNameIds = ids;
    }

    public FunctionParser() {
        reset();
    }

    @Override
    public void reset() {
        super.reset();

        mState = STATE_ParamStart;
        if (null != mParamParser) {
            mParamParser.reset();
            mParamParser = null;
        }

        mParams.clear();

        mFunNameIds = null;
        mResultExpr = null;
    }

    public void setObjectRegisterId(int regId) {
        mObjectRegId = regId;
    }

    @Override
    protected int parse(Token token) {
        int ret = RESULT_NEED_MORE;

//        Log.d(TAG, "parse token:" + token);

        switch (mState) {
            case STATE_ParamStart:
                if (null == mParamParser) {
                    if (Token.TYPE_SYMBOL == token.mType) {
                        SymbolToken st = (SymbolToken) token;
                        if (SymbolToken.RIGHT_BRACKET == st.mValue) {
                            // no params, and finished
                            ret = RESULT_FINISH;
//                            Log.d(TAG, "finished");
                            writeFunCode();
                            break;
                        } else {
                            // error
                            Log.e(TAG, "invalidate symbol:" + token);
                            ret = RESULT_FAILED;
                            break;
                        }
                    } else {
                        mParamParser = new SyntaxParser();
                        mParamParser.setCodeGenerator(mCodeGenerator);
                        mParamParser.setRegisterManager(mRegisterManager);
                        mParamParser.setStringStore(mStringManager);
                    }
                }

                if (null != mParamParser) {
                    if (mParamParser.parse(token)) {
//                        if ()
//                        Log.d(TAG, "........");
                    } else {
                        if (Token.TYPE_SYMBOL == token.mType) {
                            mParamParser.forceFinish();
                            Expr expr = mParamParser.getExpr();
                            if (SymbolToken.COMMA == ((SymbolToken) token).mValue) {
                                if (null != expr) {
                                    mParams.add(expr);
                                    // param ok, continue parse param
//                                    Log.d(TAG, "add param continue expr:" + expr);
                                    mParamParser.reset();
                                } else {
                                    Log.e(TAG, "failed:" + token);
                                    ret = RESULT_FAILED;
                                }
                            } else if (SymbolToken.RIGHT_BRACKET == ((SymbolToken) token).mValue) {
                                // end
//                                Log.d(TAG, "parse param finished");
                                if (null != expr) {
//                                    Log.d(TAG, "add param finish expr:" + expr);
                                    mParams.add(expr);
                                    // param ok, continue
                                    ret = RESULT_FINISH;
                                    writeFunCode();
                                } else {
                                    Log.e(TAG, "failed:" + token);
                                    ret = RESULT_FAILED;
                                }
                            } else {
                                Log.e(TAG, "invalidate token1:" + token);
                                ret = RESULT_FAILED;
                            }
                        } else {
                            Log.e(TAG, "invalidate token2:" + token);
                            ret = RESULT_FAILED;
                        }
                    }
                } else {
                    Log.e(TAG, "param parser is null");
                }
                break;

            default:
                Log.e(TAG, "can not reach here:" + mState);
                break;
        }

        return ret;
    }

    private void writeFunCode() {
//        Log.d(TAG, "writeFunCode");

        mCodeGenerator.writeByte(ExprCommon.OPCODE_FUN);

        mCodeGenerator.writeInt(mObjectRegId);

//        Log.d(TAG, "writeFunCode mFunNameIds Size:" + mFunNameIds.size());
        mCodeGenerator.writeByte((byte) mFunNameIds.size());
        for (int id : mFunNameIds) {
            mCodeGenerator.writeInt(id);
        }

//        Log.d(TAG, "writeFunCode mParams Size:" + mParams.size());
        mCodeGenerator.writeByte((byte)mParams.size());
        for (Expr param : mParams) {
            Operator.writeExpr(mCodeGenerator, param);
        }

        // result reg id
        int regId = mRegisterManager.malloc();
//        Log.d(TAG, "result register id:" + regId);
        mResultExpr = new RegisterExpr(regId);
        mCodeGenerator.writeByte((byte)regId);
    }

    @Override
    protected Expr buildExpr() {
        return mResultExpr;
    }
}
