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

import com.libra.TextUtils;
import com.libra.expr.compiler.lex.SymbolToken;
import com.libra.expr.compiler.syntax.Expr;
import com.libra.expr.compiler.syntax.FloatExpr;
import com.libra.expr.compiler.syntax.IntegerExpr;
import com.libra.expr.compiler.syntax.RegisterExpr;
import com.libra.expr.compiler.syntax.StringExpr;
import com.libra.expr.compiler.syntax.VarExpr;
import com.libra.virtualview.common.ExprCommon;

/**
 * Created by gujicheng on 16/9/9.
 */
public class BinOperator extends Operator {
    private static final String TAG = "BinOperator_TMTEST";

    private static final int FUNCODE_LEN = 5;
    private static final byte[] sfunCode = {
            ExprCommon.VAR_VAR, ExprCommon.VAR_INT, ExprCommon.VAR_FLT, ExprCommon.VAR_STR, ExprCommon.VAR_REG,
            ExprCommon.INT_VAR, ExprCommon.NON_NON, ExprCommon.NON_NON, ExprCommon.NON_NON, ExprCommon.INT_REG,
            ExprCommon.FLT_VAR, ExprCommon.NON_NON, ExprCommon.NON_NON, ExprCommon.NON_NON, ExprCommon.FLT_REG,
            ExprCommon.STR_VAR, ExprCommon.NON_NON, ExprCommon.NON_NON, ExprCommon.NON_NON, ExprCommon.STR_REG,
            ExprCommon.REG_VAR, ExprCommon.REG_INT, ExprCommon.REG_FLT, ExprCommon.REG_STR, ExprCommon.REG_REG };

    private boolean isBin(char op) {
        if ((op >= SymbolToken.ADD && op <= SymbolToken.MOD) ||(op >= SymbolToken.EQ && op <= SymbolToken.OR) ) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deal(char op) {
        boolean ret = false;

        if (isBin(op)) {
//            Log.d(TAG, "deal operands size:" + mOperands.size());
            if (mOperands.size() >= 2) {
                Expr exprParam2 = mOperands.pop();
                Expr exprParam1 = mOperands.pop();
//                Log.d(TAG, "param1:" + exprParam1 + "  param2:" + exprParam2);
                Expr expr = binOperate(exprParam1, exprParam2, op);
                if (null != expr) {
                    mOperands.push(expr);
                    ret = true;
                } else {
                    Log.e(TAG, "expr is null");
                }
            } else {
                Log.e(TAG, "mOperands too few:" + mOperands.size());
            }
        } else {
//            Log.e(TAG, "op:" + SymbolToken.show(op) + "  is not operator");
        }

        return ret;
    }

    private static boolean isEq(char op) {
        return (SymbolToken.EQ == op || SymbolToken.ADD_EQ == op || SymbolToken.SUB_EQ == op || SymbolToken.MUL_EQ == op || SymbolToken.DIV_EQ == op || SymbolToken.MOD_EQ == op) ? true : false;
    }

    private Expr binOperate(Expr expr1, Expr expr2, char op) {
        Expr ret = null;

        switch (expr1.mType) {
            case Expr.TYPE_INT: {
                if (isEq(op)) {
                    Log.e(TAG, "left operand is not var:" + SymbolToken.show(op));
                    break;
                }

                IntegerExpr e1 = (IntegerExpr) expr1;
                switch (expr2.mType) {
                    case Expr.TYPE_INT: {
                        IntegerExpr e2 = (IntegerExpr) expr2;
                        ret = new IntegerExpr(intCalc(e1.mValue, e2.mValue, op));
                        break;
                    }

                    case Expr.TYPE_FLOAT: {
                        FloatExpr e2 = (FloatExpr) expr2;
                        ret = new FloatExpr((float) floatCalc(e1.mValue, e2.mValue, op));
                        break;
                    }

                    case Expr.TYPE_STR: {
                        if (SymbolToken.ADD == op) {
//                            StringManager sm = StringManager.getInstance();
                            StringExpr e2 = (StringExpr) expr2;
                            String v = mStringManager.getString(e2.mValue);
                            v = String.format("%d%s", e1.mValue, v);
                            int id = mStringManager.getStringId(v, true);

                            ret = new StringExpr(id);
                        } else {
                            Log.e(TAG, "invalidate string operator:" + SymbolToken.show(op));
                        }
                        break;
                    }

                    case Expr.TYPE_VAR: {
                        writeCode(expr1, expr2, op);
                        int regId = mRegisterManager.malloc();
                        ret = new RegisterExpr(regId);
                        mCodeGenerator.writeByte((byte)regId);
                        break;
                    }

                    case Expr.TYPE_REG: {
                        writeCode(expr1, expr2, op);
                        ret = expr2;
                        break;
                    }
                }
                break;
            }

            case Expr.TYPE_FLOAT: {
                if (isEq(op)) {
                    Log.e(TAG, "left operand is not var:" + SymbolToken.show(op));
                    break;
                }

                FloatExpr e1 = (FloatExpr) expr1;
                switch (expr2.mType) {
                    case Expr.TYPE_INT: {
                        IntegerExpr e2 = (IntegerExpr) expr2;
                        ret = new FloatExpr((float)floatCalc(e1.mValue, e2.mValue, op));
                        break;
                    }

                    case Expr.TYPE_FLOAT: {
                        FloatExpr e2 = (FloatExpr) expr2;
                        ret = new FloatExpr((float) floatCalc(e1.mValue, e2.mValue, op));
                        break;
                    }

                    case Expr.TYPE_STR: {
                        if (SymbolToken.ADD == op) {
//                            StringManager sm = StringManager.getInstance();
                            StringExpr e2 = (StringExpr) expr2;
                            String v = mStringManager.getString(e2.mValue);
                            v = String.format("%f%s", e1.mValue, v);
                            int id = mStringManager.getStringId(v, true);

                            ret = new StringExpr(id);
                        } else {
                            Log.e(TAG, "invalidate string operator:" + SymbolToken.show(op));
                        }
                        break;
                    }

                    case Expr.TYPE_VAR: {
                        writeCode(expr1, expr2, op);
                        int regId = mRegisterManager.malloc();
                        ret = new RegisterExpr(regId);
                        mCodeGenerator.writeByte((byte)regId);
                        break;
                    }

                    case Expr.TYPE_REG:
                        writeCode(expr1, expr2, op);
                        ret = expr2;
                        break;
                }
                break;
            }

            case Expr.TYPE_STR: {
                if (isEq(op)) {
                    Log.e(TAG, "left operand is not var:" + SymbolToken.show(op));
                    break;
                }

                StringExpr e1 = (StringExpr) expr1;
                String v1 = mStringManager.getString(e1.mValue);
                if (SymbolToken.ADD == op) {
                    switch (expr2.mType) {
                        case Expr.TYPE_INT: {
                            IntegerExpr e2 = (IntegerExpr) expr2;
                            v1 = String.format("%s%d", v1, e2.mValue);
                            int id = mStringManager.getStringId(v1, true);
                            ret = new StringExpr(id);
                            break;
                        }

                        case Expr.TYPE_FLOAT: {
                            FloatExpr e2 = (FloatExpr) expr2;
                            v1 = String.format("%s%f", v1, e2.mValue);
                            int id = mStringManager.getStringId(v1, true);
                            ret = new StringExpr(id);
                            break;
                        }

                        case Expr.TYPE_STR: {
                            StringExpr e2 = (StringExpr) expr2;
                            String v2 = mStringManager.getString(e2.mValue);
                            v2 = String.format("%s%s", v1, v2);
                            int id = mStringManager.getStringId(v2, true);

                            ret = new StringExpr(id);
                            break;
                        }

                        case Expr.TYPE_VAR: {
                            writeCode(expr1, expr2, op);
                            int regId = mRegisterManager.malloc();
                            ret = new RegisterExpr(regId);
                            mCodeGenerator.writeByte((byte)regId);
                            break;
                        }

                        case Expr.TYPE_REG:
                            writeCode(expr1, expr2, op);
                            ret = expr2;
                            break;
                    }
                } else if (SymbolToken.EQ_EQ == op) {
                    switch (expr2.mType) {
                        case Expr.TYPE_STR: {
                            StringExpr e2 = (StringExpr) expr2;
                            String v2 = mStringManager.getString(e2.mValue);
                            int v = 0;
                            if (TextUtils.equals(v1, v2)) {
                                v = 1;
                            }
                            ret = new IntegerExpr(v);
                            break;
                        }

                        case Expr.TYPE_VAR: {
                            writeCode(expr1, expr2, op);
                            int regId = mRegisterManager.malloc();
                            ret = new RegisterExpr(regId);
                            mCodeGenerator.writeByte((byte) regId);
                            break;
                        }

                        case Expr.TYPE_REG:
                            writeCode(expr1, expr2, op);
                            ret = expr2;
                            break;
                    }

                } else {
                    Log.e(TAG, "invalidate string operator:" + SymbolToken.show(op));
                }
                break;
            }

            case Expr.TYPE_VAR: {
                switch (expr2.mType) {
                    case Expr.TYPE_STR:
                        if (SymbolToken.ADD != op && SymbolToken.EQ != op && SymbolToken.ADD_EQ != op && SymbolToken.EQ_EQ != op) {
                            Log.e(TAG, "invalidate string operator:" + SymbolToken.show(op));
                            break;
                        }
                    case Expr.TYPE_INT:
                    case Expr.TYPE_FLOAT:
                    case Expr.TYPE_VAR:
                        writeCode(expr1, expr2, op);
                        int regId = mRegisterManager.malloc();
                        ret = new RegisterExpr(regId);
//                        Log.d(TAG, "regId:" + regId);
                        mCodeGenerator.writeByte((byte)regId);
                        break;

                    case Expr.TYPE_REG:
                        writeCode(expr1, expr2, op);
                        ret = expr2;
                        break;
                }
                break;
            }

            case Expr.TYPE_REG: {
                if (SymbolToken.EQ == op) {
                    Log.e(TAG, "left operand is not var:" + SymbolToken.show(op));
                    break;
                }

                switch (expr2.mType) {
                    case Expr.TYPE_STR:
                        if (SymbolToken.ADD != op && SymbolToken.EQ_EQ != op) {
                            Log.e(TAG, "invalidate string operator:" + SymbolToken.show(op));
                            break;
                        }
                    case Expr.TYPE_INT:
                    case Expr.TYPE_FLOAT:
                    case Expr.TYPE_VAR:
                        writeCode(expr1, expr2, op);
                        ret = expr1;
                        break;

                    case Expr.TYPE_REG: {
                        writeCode(expr1, expr2, op);
                        int id1 = ((RegisterExpr)expr1).mRegId;
                        int id2 = ((RegisterExpr)expr2).mRegId;
                        if (id1 < id2) {
                            ret = expr1;
                        } else {
                            ret = expr2;
                        }
                        mRegisterManager.free();
                        break;
                    }
                }
                break;
            }
        }

        return ret;
    }

    private int intCalc(int v1, int v2, char op) {
        int ret = 0;

//        Log.d(TAG, "intCalc op:" + op + " v1:" + v1 + "  v2:" + v2);
        switch (op) {
            case SymbolToken.ADD:
                ret = v1 + v2;
                break;

            case SymbolToken.SUB:
                ret = v1 - v2;
                break;

            case SymbolToken.MUL:
                ret = v1 * v2;
                break;

            case SymbolToken.DIV:
                ret = v1 / v2;
                break;

            case SymbolToken.MOD:
                ret = v1 % v2;
                break;

            case SymbolToken.GT:
                ret = (v1 > v2) ? 1 : 0;
                break;

            case SymbolToken.LT:
                ret = (v1 < v2) ? 1 : 0;
                break;

            case SymbolToken.GE:
                ret = (v1 >= v2) ? 1 : 0;
                break;

            case SymbolToken.LE:
                ret = (v1 <= v2) ? 1 : 0;
                break;

            case SymbolToken.NOT_EQ:
                ret = (v1 != v2) ? 1 : 0;
                break;

            case SymbolToken.EQ_EQ:
                ret = (v1 == v2) ? 1 : 0;
                break;

            default:
                Log.e(TAG, "invalidate op:" + SymbolToken.show(op));
                break;
        }

        return ret;
    }

    private double floatCalc(double v1, double v2, char op) {
        double ret = 0;

        switch (op) {
            case SymbolToken.ADD:
                ret = v1 + v2;
                break;

            case SymbolToken.SUB:
                ret = v1 - v2;
                break;

            case SymbolToken.MUL:
                ret = v1 * v2;
                break;

            case SymbolToken.DIV:
                ret = v1 / v2;
                break;

            case SymbolToken.GT:
                ret = (v1 > v2) ? 1 : 0;
                break;

            case SymbolToken.LT:
                ret = (v1 < v2) ? 1 : 0;
                break;

            case SymbolToken.GE:
                ret = (v1 >= v2) ? 1 : 0;
                break;

            case SymbolToken.LE:
                ret = (v1 <= v2) ? 1 : 0;
                break;

            case SymbolToken.NOT_EQ:
                ret = (v1 != v2) ? 1 : 0;
                break;

            case SymbolToken.EQ_EQ:
                ret = (v1 == v2) ? 1 : 0;
                break;

            default:
                Log.e(TAG, "invalidate op:" + SymbolToken.show(op));
                break;
        }

        return ret;
    }

    private void writeCode(Expr expr1, Expr expr2, char op) {
        byte code = -1;
        switch (op) {
            case SymbolToken.ADD:
                code = ExprCommon.OPCODE_ADD;
                break;
            case SymbolToken.SUB:
                code = ExprCommon.OPCODE_SUB;
                break;
            case SymbolToken.MUL:
                code = ExprCommon.OPCODE_MUL;
                break;
            case SymbolToken.DIV:
                code = ExprCommon.OPCODE_DIV;
                break;
            case SymbolToken.MOD:
                code = ExprCommon.OPCODE_MOD;
                break;
            case SymbolToken.EQ:
                code = ExprCommon.OPCODE_EQUAL;
                break;
            case SymbolToken.GT:
                code = ExprCommon.OPCODE_GT;
                break;
            case SymbolToken.LT:
                code = ExprCommon.OPCODE_LT;
                break;
            case SymbolToken.ADD_EQ:
                code = ExprCommon.OPCODE_ADD_EQ;
                break;
            case SymbolToken.SUB_EQ:
                code = ExprCommon.OPCODE_SUB_EQ;
                break;
            case SymbolToken.MUL_EQ:
                code = ExprCommon.OPCODE_MUL_EQ;
                break;
            case SymbolToken.DIV_EQ:
                code = ExprCommon.OPCODE_DIV_EQ;
                break;
            case SymbolToken.MOD_EQ:
                code = ExprCommon.OPCODE_MOD_EQ;
                break;
            case SymbolToken.NOT_EQ:
                code = ExprCommon.OPCODE_NOT_EQ;
                break;
            case SymbolToken.EQ_EQ:
                code = ExprCommon.OPCODE_EQ_EQ;
                break;
            case SymbolToken.GE:
                code = ExprCommon.OPCODE_GE;
                break;
            case SymbolToken.LE:
                code = ExprCommon.OPCODE_LE;
                break;
            case SymbolToken.AND:
                code = ExprCommon.OPCODE_AND;
                break;
            case SymbolToken.OR:
                code = ExprCommon.OPCODE_OR;
                break;
            default:
                Log.e(TAG, "wirteBinCode op is invalidate:" + SymbolToken.show(op));
                break;
        }
//        Log.d(TAG, "write opCode:" + code);
        mCodeGenerator.writeByte(code);

        int type1 = expr1.mType;
        int type2 = expr2.mType;
        if (type1 < FUNCODE_LEN && type2 < FUNCODE_LEN) {
            byte funCode = sfunCode[type1 * FUNCODE_LEN + type2];
            if (-1 == funCode) {
                Log.e(TAG, "fun code error type1:" + type1 + "  type2:"
                        + type2);
            } else {
                mCodeGenerator.writeByte(funCode);
                switch (funCode) {
                    case ExprCommon.VAR_VAR:
                        writeVar((VarExpr)expr1);
                        writeVar((VarExpr)expr2);
                        break;
                    case ExprCommon.VAR_INT:
                        writeVar((VarExpr)expr1);
                        writeInt((IntegerExpr)expr2);
                        break;
                    case ExprCommon.VAR_FLT:
                        writeVar((VarExpr)expr1);
                        writeFloat((FloatExpr)expr2);
                        break;
                    case ExprCommon.VAR_STR:
                        writeVar((VarExpr)expr1);
                        writeString((StringExpr)expr2);
                        break;
                    case ExprCommon.VAR_REG:
                        writeVar((VarExpr)expr1);
                        writeRegister((RegisterExpr)expr2);
                        break;

                    case ExprCommon.INT_VAR:
                        writeInt((IntegerExpr)expr1);
                        writeVar((VarExpr)expr2);
                        break;
                    case ExprCommon.INT_REG:
                        writeInt((IntegerExpr)expr1);
                        writeRegister((RegisterExpr)expr2);
                        break;

                    case ExprCommon.FLT_VAR:
                        writeFloat((FloatExpr)expr1);
                        writeVar((VarExpr)expr2);
                        break;
                    case ExprCommon.FLT_REG:
                        writeFloat((FloatExpr)expr1);
                        writeRegister((RegisterExpr)expr2);
                        break;

                    case ExprCommon.STR_VAR:
                        writeString((StringExpr)expr1);
                        writeVar((VarExpr)expr2);
                        break;
                    case ExprCommon.STR_REG:
                        writeString((StringExpr)expr1);
                        writeRegister((RegisterExpr)expr2);
                        break;

                    case ExprCommon.REG_VAR:
                        writeRegister((RegisterExpr)expr1);
                        writeVar((VarExpr)expr2);
                        break;
                    case ExprCommon.REG_INT:
                        writeRegister((RegisterExpr)expr1);
                        writeInt((IntegerExpr)expr2);
                        break;
                    case ExprCommon.REG_FLT:
                        writeRegister((RegisterExpr)expr1);
                        writeFloat((FloatExpr)expr2);
                        break;
                    case ExprCommon.REG_STR:
                        writeRegister((RegisterExpr)expr1);
                        writeString((StringExpr)expr2);
                        break;
                    case ExprCommon.REG_REG:
                        writeRegister((RegisterExpr)expr1);
                        writeRegister((RegisterExpr)expr2);
                        break;
                }
            }
        }
    }

}
