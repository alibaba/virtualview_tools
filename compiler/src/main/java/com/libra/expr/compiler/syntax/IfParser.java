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

import com.libra.TextUtils;
import com.libra.Log;

import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.lex.Token;
import com.libra.expr.compiler.lex.WordToken;
import com.libra.expr.compiler.syntax.operator.Operator;
import com.libra.virtualview.common.ExprCommon;
import com.libra.virtualview.common.StringBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 16/12/12.
 */

public class IfParser extends Parser {
    private final static String TAG = "IfParser_TMTEST";

    private final static int STATE_start = 1;
    private final static int STATE_left_bracket = 2;
    private final static int STATE_condition = 3;
    private final static int STATE_left_big_bracket = 4;
    private final static int STATE_body = 5;
    private final static int STATE_elseif = 6;

    private int mState = STATE_start;
    private SyntaxParser mSentenceParser;
    private boolean mIsElse = false;

    private int mWriteJmpPos;
    private List<Integer> mJmpEndPos = new ArrayList<Integer>();

    @Override
    public void reset() {
        super.reset();

        mState = STATE_start;
        mIsElse = false;
        mJmpEndPos.clear();

        if (null != mSentenceParser) {
            mSentenceParser.reset();
            mSentenceParser = null;
        }
    }

    @Override
    protected int parse(Token token) {
        int ret = RESULT_NEED_MORE;

//        Log.d(TAG, "parse token:" + token + "  state:" + mState);
        switch (mState) {
            case STATE_start:
                if (Token.TYPE_WORD == token.mType && ((WordToken) token).mValue == StringBase.STR_ID_if) {
                    mState = STATE_left_bracket;
                } else {
                    ret = RESULT_FAILED;
                }
                break;

            case STATE_left_bracket:
                if (Token.TYPE_SYMBOL == token.mType && SymbolToken.LEFT_BRACKET == ((SymbolToken) token).mValue) {
                    mState = STATE_condition;
                    mWriteJmpPos = -1;
                    if (null == mSentenceParser) {
                        mSentenceParser = new SyntaxParser();
                        mSentenceParser.setCodeGenerator(mCodeGenerator);
                        mSentenceParser.setRegisterManager(mRegisterManager);
                        mSentenceParser.setStringStore(mStringManager);
                    }
                } else {
                    ret = RESULT_FAILED;
                }
                break;

            case STATE_condition:
//                Log.d(TAG, "STATE_condition:");
                if (mSentenceParser.parse(token)) {

                } else {
//                    Log.d(TAG, "condition finished");
                    if (Token.TYPE_SYMBOL == token.mType) {
                        mSentenceParser.forceFinish();
                        if (SymbolToken.RIGHT_BRACKET == ((SymbolToken) token).mValue) {
                            Expr expr = mSentenceParser.getExpr();
                            if (null != expr) {
//                                expr.mType
                                // param ok, continue parse param
                                if (Expr.TYPE_REG == expr.mType || Expr.TYPE_VAR == expr.mType) {
                                    mCodeGenerator.writeByte(ExprCommon.OPCODE_JMP_C);
                                    // condition jump offset
                                    mWriteJmpPos = mCodeGenerator.size();
                                    mCodeGenerator.writeInt(0);

                                    // write expr
                                    Operator.writeExpr(mCodeGenerator, expr);
                                } else if (Expr.TYPE_INT == expr.mType) {
                                    if (((IntegerExpr) expr).mValue <= 0) {
                                        // jmp
                                    }
                                } else if (Expr.TYPE_FLOAT == expr.mType) {
                                    if (((FloatExpr) expr).mValue <= 0) {
                                        // jmp
                                    }
                                } else if (Expr.TYPE_STR == expr.mType) {
                                    if (TextUtils.isEmpty(this.mStringManager.getString(((StringExpr) expr).mValue))) {
                                        // jmp
                                    }
                                }
//                                Log.d(TAG, "add param continue expr:" + expr);
                                mSentenceParser.reset();

                                mState = STATE_left_big_bracket;
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
                break;

            case STATE_left_big_bracket:
//                Log.d(TAG, "STATE_left_big_bracket");
                if (Token.TYPE_SYMBOL == token.mType && SymbolToken.LEFT_BIG_BRACKET == ((SymbolToken) token).mValue) {
                    mState = STATE_body;
                } else {
                    ret = RESULT_FAILED;
                }
                break;

            case STATE_body:
                if (mSentenceParser.parse(token)) {

                } else {
//                    Log.d(TAG, "STATE_body finished");
                    if (Token.TYPE_SYMBOL == token.mType) {
                        mSentenceParser.forceFinish();
                        if (SymbolToken.RIGHT_BIG_BRACKET == ((SymbolToken) token).mValue) {

                            mState = STATE_elseif;

                            mSentenceParser.reset();
                        } else {
                            Log.e(TAG, "invalidate token1:" + token);
                            ret = RESULT_FAILED;
                        }
                    } else {
                        Log.e(TAG, "invalidate token2:" + token);
                        ret = RESULT_FAILED;
                    }
                }
                break;

            case STATE_elseif:
                if ((Token.TYPE_WORD == token.mType) && ((((WordToken)token).mValue == StringBase.STR_ID_elseif) || (((WordToken)token).mValue == StringBase.STR_ID_else)) ) {
                    WordToken wt = (WordToken) token;

                    // jump to end
                    mCodeGenerator.writeByte(ExprCommon.OPCODE_JMP);
                    // jump offset
                    mJmpEndPos.add(mCodeGenerator.size());
                    mCodeGenerator.writeInt(0);

                    if (wt.mValue == StringBase.STR_ID_elseif) {
                        mState = STATE_left_bracket;
                    } else if (wt.mValue == StringBase.STR_ID_else) {
                        mState = STATE_left_big_bracket;
                        mIsElse = true;
//                        Log.d(TAG, "else coming");
                    } else {
                        ret = RESULT_FAILED;
                        Log.e(TAG, "failed");
                    }
                } else {
//                    Log.d(TAG, "if parser finished--------:" + token);
                    ret = RESULT_FINISH_BACK_1;

                    int size = mCodeGenerator.size();
                    for (int pos : mJmpEndPos) {
                        mCodeGenerator.rewriteInt(size, pos);
                    }
                }

                if (mWriteJmpPos > -1) {
                    int size = mCodeGenerator.size();
//                    Log.d(TAG, "rewrite code:" + mCodeGenerator);
//                    Log.d(TAG, "rewrite jmpc pos:" + size);
                    mCodeGenerator.rewriteInt(size, mWriteJmpPos);

                    mWriteJmpPos = -1;
                }

                break;
        }

        return ret;
    }

    @Override
    protected Expr buildExpr() {
        return null;
    }
}
