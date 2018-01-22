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

import com.libra.expr.common.ExprCode;
import com.libra.expr.common.StringSupport;
import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.lex.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by gujicheng on 16/9/8.
 */
public class SyntaxParser {
    private final static String TAG = "SyntaxParser_TMTEST";

    private List<Parser> mParsers = new ArrayList<Parser>();
    private Parser mCurParser;

    private Stack<Expr> mOperands = new Stack<Expr>();
    private Stack<Character> mOperators = new Stack<Character>();

    private CodeGenerator mCodeGenerator;
    private RegisterManager mRegisterManager;

    private OperatorParser mOperatorParser;
    private Token mPreToken;

    public SyntaxParser() {
        // function parser must before var parser
        mParsers.add(new StringParser());
        mParsers.add(new IntegerParser());
        mParsers.add(new FloatParser());
        mParsers.add(new IfParser());

        mOperatorParser = new OperatorParser();
        mOperatorParser.setOperandStack(mOperands);
        mOperatorParser.setOperatorStack(mOperators);
        mParsers.add(mOperatorParser);

        VarParser vp = new VarParser();
        vp.setOperands(mOperands);
        mParsers.add(vp);

        reset();
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        mCodeGenerator = codeGenerator;
        for (Parser parser : mParsers) {
            parser.setCodeGenerator(mCodeGenerator);
        }
    }

    public void setRegisterManager(RegisterManager reg) {
        mRegisterManager = reg;
        for (Parser parser : mParsers) {
            parser.setRegisterManager(reg);
        }
    }

    public void setStringStore(StringSupport sm) {
        for (Parser parser : mParsers) {
            parser.setStringManager(sm);
        }
    }

    public void reset() {
//        Log.d(TAG, "reset------------------");
        mOperands.clear();
        mOperators.clear();
        mOperators.push(SymbolToken.END);

        mCurParser = null;
        mOperands.removeAllElements();

        for (Parser parser : mParsers) {
            parser.reset();
        }

        mOperatorParser.init();

        mPreToken = null;
    }

    public Expr getExpr() {
        Expr ret = null;

        if ((1 == mOperands.size()) && (0 == mOperators.size() || (1 == mOperators.size() && SymbolToken.END == mOperators.get(0)))) {
            ret = mOperands.get(0);
        } else {
            Log.e(TAG, "getExpr failed operand size:" + mOperands.size() + "  operators size:" + mOperators.size());
        }

        return ret;
    }

    public void forceFinish() {
        if (parse(SymbolToken.TOKEN_END) ) {
//            Log.d(TAG, "forceFinish ok");
        } else {
            Log.e(TAG, "forceFinish failed");
        }
    }

    public ExprCode getExprCode() {
        ExprCode ret = mCodeGenerator.getCode();

//        if (1 == mOperands.size() && 0 == mOperators.size()) {
//            Expr expr = mOperands.get(0);
//            if (Expr.TYPE_REG == expr.mType) {
//                ret = mCodeGenerator.getCode();
//            } else {
//                Log.d(TAG, "no code");
//            }
//        } else {
//            Log.e(TAG, "getExprCode failed operand size:" + mOperands.size() + "  operators size:" + mOperators.size());
//        }

        return ret;
    }

    private void preTranslate(Token token) {
        // convert sub to minus
        if (Token.TYPE_SYMBOL == token.mType) {
            if (SymbolToken.SUB == ((SymbolToken) token).mValue) {
                if ((null == mPreToken)
                        || ((Token.TYPE_SYMBOL == mPreToken.mType)
                        && (((SymbolToken) mPreToken).mValue != SymbolToken.RIGHT_BRACKET) && (((SymbolToken) mPreToken).mValue != SymbolToken.WELL))) {
                    ((SymbolToken) token).mValue = SymbolToken.MINUS;
//                ((SymbolToken) token).mValue = '`';
                }

            } else if (SymbolToken.SENTENCE_END == ((SymbolToken) token).mValue) {
                ((SymbolToken) token).mValue = SymbolToken.END;
            }
        }

        mPreToken = token;
    }

    public boolean parse(Token token) {
        boolean ret = false;
//        Log.d(TAG, "parse token--------------------:" + token);

        if (null != token) {
            int result = Parser.RESULT_FAILED;

            ret = true;
            boolean isSentenceEnd = (Token.TYPE_SYMBOL == token.mType && SymbolToken.SENTENCE_END == ((SymbolToken) token).mValue);

            if (null == mCurParser) {
                preTranslate(token);

                for (Parser parser : mParsers) {
                    result = parser.addToken(token);
                    if (Parser.RESULT_FAILED != result) {
                        mCurParser = parser;
                        break;
                    }
                }

                if (null == mCurParser) {
//                    Log.e(TAG, "can not find parser:" + token);
                    ret = false;
                }
            } else {
                result = mCurParser.addToken(token);
                if (Parser.RESULT_FAILED == result) {
                    Log.e(TAG, "parse failed:" + token);
                    ret = false;
                }
            }

            if (ret) {
                switch (result) {
                    case Parser.RESULT_FINISH:
                    case Parser.RESULT_FINISH_BACK_1:
//                        Log.d(TAG, "parse finish");
                        Expr expr = mCurParser.getExpr();
                        if (null != expr) {
                            mOperands.push(expr);
                        }
//                        Log.d(TAG, "mOperands size:" + mOperands.size());
                        mCurParser.reset();
                        mCurParser = null;

                        if (Parser.RESULT_FINISH_BACK_1 == result) {
                            ret = parse(token);
                        }
                        break;
                }

                if (isSentenceEnd) {
//                    Log.d(TAG, "sentence end opesize:" + mOperators.size());
                    if (0 == mOperators.size()) {
                        mOperators.push(SymbolToken.END);
                    } else {
                        Log.w(TAG, "sentence end, but operator is not empty size:" + mOperators.size() + " :" + (int)mOperators.peek());
                    }
                    mOperands.clear();
                }
            }
        } else {
            Log.e(TAG, "token is null");
        }

        return ret;
    }
}
