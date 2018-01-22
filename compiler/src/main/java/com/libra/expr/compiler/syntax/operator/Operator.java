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

import com.libra.expr.common.StringSupport;
import com.libra.expr.compiler.syntax.CodeGenerator;
import com.libra.expr.compiler.syntax.Expr;
import com.libra.expr.compiler.syntax.FloatExpr;
import com.libra.expr.compiler.syntax.IntegerExpr;
import com.libra.expr.compiler.syntax.RegisterExpr;
import com.libra.expr.compiler.syntax.RegisterManager;
import com.libra.expr.compiler.syntax.StringExpr;
import com.libra.expr.compiler.syntax.VarExpr;

import java.util.Stack;

/**
 * Created by gujicheng on 16/9/9.
 */
public abstract class Operator {
    private final static String TAG = "Operator_TMTEST";

    protected Stack<Expr> mOperands;
    protected CodeGenerator mCodeGenerator;
    protected RegisterManager mRegisterManager;
    protected StringSupport mStringManager;

    abstract public boolean deal(char op);

    public void setStringManager(StringSupport sm) {
        mStringManager = sm;
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        mCodeGenerator = codeGenerator;
    }

    public void setRegisterManager(RegisterManager registerManager) {
        mRegisterManager = registerManager;
    }

    public void setOperandStack(Stack<Expr> operands) {
        mOperands = operands;
    }

    public static void writeInt(CodeGenerator cg, IntegerExpr expr) {
        cg.writeInt(expr.mValue);
    }
    protected void writeInt(IntegerExpr expr) {
        writeInt(mCodeGenerator, expr);
//        mCodeGenerator.writeInt(expr.mValue);
    }

    public static void writeFloat(CodeGenerator cg, FloatExpr expr) {
        cg.writeInt(Float.floatToIntBits(expr.mValue));
    }
    protected void writeFloat(FloatExpr expr) {
        writeFloat(mCodeGenerator, expr);
//        mCodeGenerator.writeInt(Float.floatToIntBits(expr.mValue));
    }

    public static void writeString(CodeGenerator cg, StringExpr expr) {
        cg.writeInt(expr.mValue);
    }
    protected void writeString(StringExpr expr) {
        writeString(mCodeGenerator, expr);
//        mCodeGenerator.writeInt(expr.mValue);
    }

    public static void writeRegister(CodeGenerator cg, RegisterExpr expr) {
        cg.writeInt(expr.mRegId);
    }
    protected void writeRegister(RegisterExpr expr) {
        writeRegister(mCodeGenerator, expr);
//        mCodeGenerator.writeInt(expr.mRegId);
    }

    public static void writeVar(CodeGenerator cg, VarExpr expr) {
        cg.writeInt(expr.mObjRegisterId);
        int size = expr.mValue.size();
        cg.writeByte((byte)size);
        for (int i = 0; i < size; ++i) {
            cg.writeInt(expr.mValue.get(i));
        }
    }
    protected void writeVar(VarExpr expr) {
        writeVar(mCodeGenerator, expr);
//        int size = expr.mValue.size();
//        mCodeGenerator.writeByte((byte)size);
//        for (int i = 0; i < size; ++i) {
//            mCodeGenerator.writeInt(expr.mValue.get(i));
//        }
    }


    public static void writeExpr(CodeGenerator cg, Expr expr) {
        if (null != expr) {
            cg.writeByte((byte)expr.mType);
            switch (expr.mType) {
                case Expr.TYPE_VAR:
                    writeVar(cg, (VarExpr) expr);
                    break;

                case Expr.TYPE_INT:
                    writeInt(cg, (IntegerExpr) expr);
                    break;

                case Expr.TYPE_FLOAT:
                    writeFloat(cg, (FloatExpr)expr);
                    break;

                case Expr.TYPE_STR:
                    writeString(cg, (StringExpr)expr);
                    break;

                case Expr.TYPE_REG:
                    writeRegister(cg, (RegisterExpr)expr);
                    break;

                default:
                    Log.e(TAG, "writeExpr type invalidate:" + expr.mType);
                    break;
            }
        } else {
            Log.e(TAG, "expr is null");
        }
    }
}
