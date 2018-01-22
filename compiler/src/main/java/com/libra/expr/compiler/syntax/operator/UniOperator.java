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

import com.libra.Log;

import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.syntax.Expr;
import com.libra.expr.compiler.syntax.FloatExpr;
import com.libra.expr.compiler.syntax.IntegerExpr;
import com.libra.expr.compiler.syntax.RegisterExpr;
import com.libra.expr.compiler.syntax.VarExpr;
import com.libra.virtualview.common.ExprCommon;

/**
 * Created by gujicheng on 16/9/9.
 */
public class UniOperator extends Operator {
    private static final String TAG = "UniOperator_TMTEST";

//    private static final String sUniOperator = "`";

    private boolean isUni(char op) {
        if (SymbolToken.MINUS == op || SymbolToken.NOT == op) {
            return true;
        }

         return false;
    }

    @Override
    public boolean deal(char op) {
        boolean ret = false;

        if (isUni(op)) {
            if (mOperands.size() >= 1) {
                Expr exprParam = mOperands.pop();
                Expr expr = uniOperate(exprParam, op);
                if (null != expr) {
                    mOperands.push(expr);

                    ret = true;
                }
            } else {
                Log.e(TAG, "operand too few:" + mOperands.size());
            }
        }

        return ret;
    }

    private Expr uniOperate(Expr expr, char op) {
        Expr retExpr = null;
        switch (expr.mType) {
            case Expr.TYPE_INT:
                switch (op) {
                    case SymbolToken.MINUS:
                        retExpr = new IntegerExpr((-((IntegerExpr) expr).mValue));
                        break;

                    case SymbolToken.NOT:
                        retExpr = new IntegerExpr((0 == ((IntegerExpr) expr).mValue) ? 1 : 0);
                        break;
                }
                break;

            case Expr.TYPE_FLOAT:
                switch (op) {
                    case SymbolToken.MINUS:
                        retExpr = new FloatExpr((-((FloatExpr) expr).mValue));
                        break;

                    case SymbolToken.NOT:
                        retExpr = new IntegerExpr((0 == ((FloatExpr) expr).mValue) ? 1 : 0);
                        break;
                }
                break;

            case Expr.TYPE_VAR:
                writeUniCode(expr, op);
                int regId = mRegisterManager.malloc();
                retExpr = new RegisterExpr(regId);
                mCodeGenerator.writeByte((byte)regId);
                break;

            case Expr.TYPE_REG:
                writeUniCode(expr, op);
                retExpr = expr;
                break;
        }

        return retExpr;
    }


    private void writeUniCode(Expr expr, char op) {
        if (SymbolToken.MINUS == op) {
            mCodeGenerator.writeByte(ExprCommon.OPCODE_MINUS);
        } else if (SymbolToken.NOT == op) {
            mCodeGenerator.writeByte(ExprCommon.OPCODE_NOT);
        } else {
            Log.e(TAG, "invalidate operator:" + op);
        }

        int type = expr.mType;
        switch (type) {
            case Expr.TYPE_VAR:
                mCodeGenerator.writeByte((byte) Expr.TYPE_VAR);
                writeVar((VarExpr) expr);
                break;

            case Expr.TYPE_REG:
                mCodeGenerator.writeByte((byte) Expr.TYPE_REG);
                writeRegister((RegisterExpr) expr);
                break;

            default:
                Log.e(TAG, "invalidate type:" + type);
                break;
        }
    }

}