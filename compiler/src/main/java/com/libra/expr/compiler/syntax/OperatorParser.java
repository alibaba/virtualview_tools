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

import com.libra.expr.common.StringSupport;
import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.lex.Token;
import com.libra.expr.compiler.syntax.operator.BinOperator;
import com.libra.expr.compiler.syntax.operator.Operator;
import com.libra.expr.compiler.syntax.operator.TerOperator;
import com.libra.expr.compiler.syntax.operator.UniOperator;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Created by gujicheng on 16/9/9.
 */
public class OperatorParser extends Parser {
    private final static String TAG = "OperatorParser_TMTEST";

    // ` means minus,$ means end
//    private static final String sOperatorString = "+-*/%()`=?:$";
    private static final char sOperatorString[] =
            {
            SymbolToken.ADD, SymbolToken.SUB, SymbolToken.MUL, SymbolToken.DIV, SymbolToken.MOD,
            SymbolToken.LEFT_BRACKET, SymbolToken.RIGHT_BRACKET, SymbolToken.MINUS, SymbolToken.EQ, SymbolToken.QUESTION, SymbolToken.COLON,
            SymbolToken.NOT, SymbolToken.GT, SymbolToken.LT, SymbolToken.GE, SymbolToken.LE, SymbolToken.EQ_EQ, SymbolToken.NOT_EQ,
            SymbolToken.ADD_EQ, SymbolToken.SUB_EQ, SymbolToken.MUL_EQ, SymbolToken.DIV_EQ, SymbolToken.MOD_EQ, SymbolToken.AND, SymbolToken.OR, SymbolToken.END};
    private static final int sOperatorCount = sOperatorString.length;

    // 0 means eq, 1 means greater(left greater than right), 2 means less, 3 means illegal
    private static final byte PRI_EQ = 0;
    private static final byte PRI_GR = 1;
    private static final byte PRI_LE = 2;
    private static final byte PRI_IL = 3;
    private static final byte[] sOpPriors = {
//             +       -       *       /       %      (       )       `       =       ?        :      not     >       <       >=      <=      ==      !=       +=     -=      *=       /=      %=     &&      ||      $
/* + */     PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* - */     PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* * */     PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* / */     PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* % */     PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* ( */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_EQ, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_IL,
/* ) */     PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_IL, PRI_GR, PRI_GR, PRI_IL, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* ` */     PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* = */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_GR,
/* ? */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_IL, PRI_LE, PRI_IL, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_IL,
/* : */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_GR,
/*not*/     PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* > */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* < */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/*>= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/*<= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/*== */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/*!= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/*+= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR,
/*-= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR,
/**= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR,
/*/= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR,
/*%= */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR,
/* && */    PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR,
/* || */    PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_GR, PRI_LE, PRI_GR, PRI_GR,
/* $ */     PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_IL, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_LE, PRI_EQ
    };

    private Stack<Character> mOperators;
    private List<Operator> mOperatorProcessors = new ArrayList<Operator>();
    private char mPreOperator;

    private TerOperator mTerOperator;

    private Stack<Expr> mOperands = new Stack<Expr>();

    public OperatorParser() {
        mOperatorProcessors.add(new UniOperator());
        mOperatorProcessors.add(new BinOperator());
        mTerOperator = new TerOperator();
        mOperatorProcessors.add(mTerOperator);
    }

    public void setOperatorStack(Stack<Character> operators) {
        mOperators = operators;
        mTerOperator.setOperatorStack(operators);
    }

    public void setOperandStack(Stack<Expr> operands) {
        mOperands = operands;
        for (Operator operator : mOperatorProcessors) {
            operator.setOperandStack(mOperands);
        }
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void init() {
        mPreOperator = SymbolToken.END;
    }

    @Override
    public void setCodeGenerator(CodeGenerator codeGenerator) {
        super.setCodeGenerator(codeGenerator);

        for (Operator operator : mOperatorProcessors) {
            operator.setCodeGenerator(mCodeGenerator);
        }
    }

    @Override
    public void setRegisterManager(RegisterManager registerManager) {
        super.setRegisterManager(registerManager);

        for (Operator operator : mOperatorProcessors) {
            operator.setRegisterManager(mRegisterManager);
        }
    }

    @Override
    public void setStringManager(StringSupport sm) {
        super.setStringManager(sm);

        for (Operator operator : mOperatorProcessors) {
            operator.setStringManager(sm);
        }
    }

    private void OperatorDeal(char op) {
        for (Operator operator : mOperatorProcessors) {
            if (operator.deal(op)) {
                break;
            }
        }
    }

    @Override
    public int parse(Token token) {
        int ret = RESULT_FAILED;

        if (Token.TYPE_SYMBOL == token.mType) {
            SymbolToken st = (SymbolToken) token;
            char op = st.mValue;
//            Log.d(TAG, "parse token*********************** operator:" + SymbolToken.show(op));
            if (isOperator(op)) {
                do {
                    int pri = prior(mPreOperator, op);

//                    Log.d(TAG, "prior left:" + SymbolToken.show(mPreOperator) + "  right:" + SymbolToken.show(op) + "  pri:" + pri + "  opesize:" + mOperators.size());
                    if (PRI_EQ == pri) {
                        try {
                            mOperators.pop();
                        } catch (EmptyStackException e) {
                            Log.e(TAG, "mOperators is empty");
                        }

                        if (SymbolToken.RIGHT_BRACKET == op) {
                            mPreOperator = mOperators.peek();
                        }
                        ret = RESULT_FINISH;
                        break;
                    } else if (PRI_GR == pri) {
                        try {
                            mOperators.pop();
                        } catch (EmptyStackException e) {
                            Log.e(TAG, "mOperators is empty");
                        }

//                        Log.d(TAG, "deal");
                        OperatorDeal(mPreOperator);
                        mPreOperator = mOperators.peek();
//                        Log.d(TAG, "deal end");
                    } else if (PRI_LE == pri) {
                        mOperators.push(op);
                        mPreOperator = op;
                        ret = RESULT_FINISH;
//                        Log.d(TAG, "break with le");
                        break;
                    } else {
                        // error
//                        Log.e(TAG, "operator deal error:" + SymbolToken.show(op));
                        ret = RESULT_FAILED;
                        break;
                    }
                } while (true);
            }
        }

        return ret;
    }

    @Override
    protected Expr buildExpr() {
        return null;
    }

    private static int prior(char ch1, char ch2) {
//        Log.d(TAG, "ch1:" + SymbolToken.show(ch1) + "  ch2:" + SymbolToken.show(ch2));
        int index1 = getIndex(ch1);
        int index2 = getIndex(ch2);

        if (index1 > -1 && index2 > -1) {
//            Log.d(TAG, "index1:" + index1 + "  index2:" + index2);
            return sOpPriors[index1 * sOperatorCount + index2];
        }

        return PRI_IL;
    }

    private static int getIndex(char ch) {
        int ret = -1;

        for (int i = 0; i < sOperatorString.length; ++i) {
            if (ch == sOperatorString[i]) {
                ret = i;
                break;
            }
        }

        return ret;
    }

    private static boolean isOperator(char ch) {
        return (getIndex(ch) > -1) ? true : false;
    }
}
