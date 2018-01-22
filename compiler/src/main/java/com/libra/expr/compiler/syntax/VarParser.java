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
import com.libra.expr.compiler.lex.WordToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by gujicheng on 16/9/9.
 */
public class VarParser extends Parser {
    private final static String TAG = "VarParse_TMTEST";

    private final static int STATE_Start = 0;
    private final static int STATE_Dot = 1;
    private final static int STATE_Word = 2;
    public final static char STATE_Function = 3;
    public final static char STATE_Array = 4;

    private Stack<Expr> mOperands;

    private int mState;
    private List<Integer> mWords = new ArrayList<Integer>();
    private FunctionParser mFunParser;
	private ArrayParser mArrayParser;
    private int mObjectRegId;

    public void setOperands(Stack<Expr> operands) {
        mOperands = operands;
    }

    @Override
    public void reset() {
        super.reset();

        mState = STATE_Start;
        mWords.clear();

        mFunParser = null;
        mArrayParser = null;
        mObjectRegId = -1;
    }

    @Override
    public int parse(Token token) {
        int ret = RESULT_NEED_MORE;

        switch (mState) {
            case STATE_Start:
                if (Token.TYPE_WORD == token.mType) {
                    mState = STATE_Dot;
                    WordToken wt = (WordToken) token;
                    mWords.add(wt.mValue);
//                    Log.d(TAG, "finish start with ");
                } else if ((Token.TYPE_SYMBOL == token.mType) && (SymbolToken.DOT == ((SymbolToken)token).mValue)) {
                    mState = STATE_Word;

                    mObjectRegId = mRegisterManager.getLastRegId();
                    if (mObjectRegId < 0) {
                        Log.e(TAG, "register is invalidate:" + mObjectRegId);
                        ret = RESULT_FAILED;
                    } else {
//                        Log.d(TAG, "fefegeeeeeeeeeeeeeeee:" + mOperands.size());
                        mOperands.pop();
                    }
                } else if ((Token.TYPE_SYMBOL == token.mType) && (SymbolToken.LEFT_MID_BRACKET == ((SymbolToken)token).mValue)) {
                    mObjectRegId = mRegisterManager.getLastRegId();
                    if (mObjectRegId < 0) {
                        Log.e(TAG, "register is invalidate:" + mObjectRegId);
                        ret = RESULT_FAILED;
                    } else {
//                        Log.d(TAG, "fefegeeeeeeeeeeeeeeee:" + mOperands.size());
                        mOperands.pop();
                    }

                    if (null == mArrayParser) {
                        mArrayParser = new ArrayParser();
                        mArrayParser.setRegisterManager(mRegisterManager);
                        mArrayParser.setCodeGenerator(mCodeGenerator);
                        mArrayParser.setStringManager(mStringManager);
                    }
                    mArrayParser.setObjectRegisterId(mObjectRegId);
                    mArrayParser.setNameIds(mWords);
                    mState = STATE_Array;
                } else {
                    ret = RESULT_FAILED;
                }
                break;

            case STATE_Dot:
                if (Token.TYPE_SYMBOL == token.mType) {
                    SymbolToken st = (SymbolToken)token;
                    if (SymbolToken.DOT == st.mValue) {
                        mState = STATE_Word;
//                        Log.d(TAG, "finish start with 1");
                    } else if (SymbolToken.LEFT_BRACKET == st.mValue) {
                        // function call
                        if (null == mFunParser) {
                            mFunParser = new FunctionParser();
                            mFunParser.setRegisterManager(mRegisterManager);
                            mFunParser.setCodeGenerator(mCodeGenerator);
                            mFunParser.setStringManager(mStringManager);
                        }
                        mFunParser.setObjectRegisterId(mObjectRegId);
                        mFunParser.setNameIds(mWords);
                        mState = STATE_Function;
                    } else if (SymbolToken.LEFT_MID_BRACKET == st.mValue) {
                        // array
                        if (null == mArrayParser) {
                            mArrayParser = new ArrayParser();
                            mArrayParser.setRegisterManager(mRegisterManager);
                            mArrayParser.setCodeGenerator(mCodeGenerator);
                            mArrayParser.setStringManager(mStringManager);
                        }
                        mArrayParser.setObjectRegisterId(mObjectRegId);
                        mArrayParser.setNameIds(mWords);
                        mState = STATE_Array;
                    } else {
//                        Log.d(TAG, "finish 1");
                        ret = RESULT_FINISH_BACK_1;
                    }
                } else {
//                    Log.d(TAG, "finish 2");
                    ret = RESULT_FINISH_BACK_1;
                }
                break;

            case STATE_Word:
                if (Token.TYPE_WORD == token.mType) {
                    WordToken wt = (WordToken)token;
                    mWords.add(wt.mValue);
//                    Log.d(TAG, "finish start with 2");
                    mState = STATE_Dot;
                } else {
                    Log.e(TAG, "need word:" + token);
                    ret = RESULT_FAILED;
                }
                break;

            case STATE_Function:
                ret = mFunParser.parse(token);
                break;

            case STATE_Array:
                ret = mArrayParser.parse(token);
                break;
        }

        return ret;
    }

    @Override
    protected Expr buildExpr() {
        if (null != mFunParser) {
            return mFunParser.buildExpr();
        } else if (null != mArrayParser) {
            return mArrayParser.buildExpr();
        } else {
            List<Integer> values = new ArrayList<Integer>(mWords);
//            Log.d(TAG, "build Expr objRegId:" + mObjectRegId);
            return new VarExpr(values, mObjectRegId);
        }
    }
}
