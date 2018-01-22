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

package com.libra.expr.compiler.syntax.operator;

import com.libra.TextUtils;
import com.libra.Log;

import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.syntax.Expr;
import com.libra.expr.compiler.syntax.FloatExpr;
import com.libra.expr.compiler.syntax.IntegerExpr;
import com.libra.expr.compiler.syntax.RegisterExpr;
import com.libra.expr.compiler.syntax.StringExpr;
import com.libra.expr.compiler.syntax.VarExpr;
import com.libra.virtualview.common.ExprCommon;

import java.util.Stack;

/**
 * Created by gujicheng on 16/9/14.
 */
public class TerOperator extends Operator {
    private static final String TAG = "TerOperator_TMTEST";

    private Stack<Character> mOperators;

    public void setOperatorStack(Stack<Character> operators) {
        mOperators = operators;
    }

    @Override
    public boolean deal(char op) {
        boolean ret = false;

        if (SymbolToken.COLON == op) {
            if (mOperands.size() >= 3 && mOperators.size() > 1) {
                char pre = mOperators.peek();
                if (SymbolToken.QUESTION == pre) {
                    mOperators.pop();
                    Expr exprParam3 = mOperands.pop();
                    Expr exprParam2 = mOperands.pop();
                    Expr exprParam1 = mOperands.pop();

                    Expr expr = operate(exprParam1, exprParam2, exprParam3);
                    if (null != expr) {
                        mOperands.push(expr);

                        ret = true;
                    } else {
                        Log.e(TAG, "operate failed");
                    }
                } else {
                    Log.e(TAG, "no ? matched");
                }
            } else {
                Log.e(TAG, "operand or operators too few operands:" + mOperands.size() + "  operators:" + mOperators.size());
            }
        }

        return ret;
    }

    private Expr operate(Expr expr1, Expr expr2, Expr expr3) {
        Expr ret = null;

        switch (expr1.mType) {
            case Expr.TYPE_INT:
                if (0 != ((IntegerExpr) expr1).mValue) {
                    ret = expr2;
                } else {
                    ret = expr3;
                }
                break;

            case Expr.TYPE_FLOAT:
                float v = ((FloatExpr) expr1).mValue;
                if (v < -0.0000001 || v > 0.0000001) {
                    ret = expr2;
                } else {
                    ret = expr3;
                }
                break;

            case Expr.TYPE_STR:
                String str = mStringManager.getString(((StringExpr) expr1).mValue);
                if (!TextUtils.isEmpty(str)) {
                    ret = expr2;
                } else {
                    ret = expr3;
                }
                break;

            case Expr.TYPE_VAR:
                writeCode(expr1, expr2, expr3);
                if (Expr.TYPE_REG == expr2.mType) {
                    int regId2 = ((RegisterExpr)expr2).mRegId;
                    if (Expr.TYPE_REG == expr3.mType) {
                        int regId3 = ((RegisterExpr)expr3).mRegId;
                        if (regId2 < regId3) {
                            ret = expr2;
                        } else {
                            ret = expr3;
                        }
                        mRegisterManager.free();
                    } else {
                        ret = expr2;
                    }
                } else {
                    if (Expr.TYPE_REG == expr3.mType) {
                        ret = expr3;
                    } else {
                        int regId = mRegisterManager.malloc();
                        ret = new RegisterExpr(regId);
                        mCodeGenerator.writeByte((byte)regId);
                    }
                }
                break;

            case Expr.TYPE_REG:
                writeCode(expr1, expr2, expr3);
                int regId1 = ((RegisterExpr)expr1).mRegId;
                if (Expr.TYPE_REG == expr2.mType) {
                    int regId2 = ((RegisterExpr)expr2).mRegId;
                    if (Expr.TYPE_REG == expr3.mType) {
                        int regId3 = ((RegisterExpr)expr3).mRegId;
                        if (regId1 < regId2) {
                            if ( regId1 < regId3) {
                                ret = expr1;
                            } else {
                                ret = expr3;
                            }
                        } else {
                            if (regId2 < regId3) {
                                ret = expr2;
                            } else {
                                ret = expr3;
                            }
                        }
                        mRegisterManager.free();
                        mRegisterManager.free();
                    } else {
                        if (regId1 < regId2) {
                            ret = expr1;
                        } else {
                            ret = expr2;
                        }
                        mRegisterManager.free();
                    }
                } else {
                    if (Expr.TYPE_REG == expr3.mType) {
                        int regId3 = ((RegisterExpr)expr3).mRegId;
                        if (regId1 < regId3) {
                            ret = expr1;
                        } else {
                            ret = expr3;
                        }
                        mRegisterManager.free();
                    } else {
                        ret = expr1;
                    }
                }
                break;
        }

        return ret;
    }

    private void writeCode(Expr expr1, Expr expr2, Expr expr3) {
        mCodeGenerator.writeByte(ExprCommon.OPCODE_TERNARY);

        int type1 = expr1.mType;
        int type2 = expr2.mType;
        int type3 = expr3.mType;

        short fc = (short)(type1 | (type2 << 3) | (type3 << 6));
        mCodeGenerator.writeShort(fc);

        writeExpr(expr1);
        writeExpr(expr2);
        writeExpr(expr3);
    }

    private void writeExpr(Expr expr) {
        switch (expr.mType) {
            case Expr.TYPE_VAR:
                writeVar((VarExpr)expr);
                break;
            case Expr.TYPE_INT:
                writeInt((IntegerExpr)expr);
                break;
            case Expr.TYPE_FLOAT:
                writeFloat((FloatExpr)expr);
                break;
            case Expr.TYPE_STR:
                writeString((StringExpr)expr);
                break;
            case Expr.TYPE_REG:
                writeRegister((RegisterExpr)expr);
                break;
        }
    }
}
