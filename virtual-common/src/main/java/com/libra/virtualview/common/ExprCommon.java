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

package com.libra.virtualview.common;

/**
 * Created by gujicheng on 16/9/12.
 */
public class ExprCommon {
    public static final byte OPCODE_ADD = 0;
    public static final byte OPCODE_SUB = 1;
    public static final byte OPCODE_MUL = 2;
    public static final byte OPCODE_DIV = 3;
    public static final byte OPCODE_MOD = 4;
    public static final byte OPCODE_EQUAL = 5;
    public static final byte OPCODE_TERNARY = 6;
    public static final byte OPCODE_MINUS = 7;
    public static final byte OPCODE_NOT = 8;
    public static final byte OPCODE_GT = 9;
    public static final byte OPCODE_LT = 10;
    public static final byte OPCODE_NOT_EQ = 11;
    public static final byte OPCODE_EQ_EQ = 12;
    public static final byte OPCODE_GE = 13;
    public static final byte OPCODE_LE = 14;
    public static final byte OPCODE_FUN = 15;
    public static final byte OPCODE_ADD_EQ = 16;
    public static final byte OPCODE_SUB_EQ = 17;
    public static final byte OPCODE_MUL_EQ = 18;
    public static final byte OPCODE_DIV_EQ = 19;
    public static final byte OPCODE_MOD_EQ = 20;
    public static final byte OPCODE_JMP = 21;
    public static final byte OPCODE_JMP_C = 22;
    public static final byte OPCODE_AND = 23;
    public static final byte OPCODE_OR = 24;
    public static final byte OPCODE_ARRAY = 25;

    public static final int NON_NON = -1;

    public static final int VAR_VAR = 0;
    public static final int VAR_INT = 1;
    public static final int VAR_FLT = 2;
    public static final int VAR_STR = 3;
    public static final int VAR_REG = 4;

    public static final int INT_VAR = 5;
    public static final int INT_REG = 6;

    public static final int FLT_VAR = 7;
    public static final int FLT_REG = 8;

    public static final int STR_VAR = 9;
    public static final int STR_REG = 10;

    public static final int REG_VAR = 11;
    public static final int REG_INT = 12;
    public static final int REG_FLT = 13;
    public static final int REG_STR = 14;
    public static final int REG_REG = 15;

    public static final int VAR_VAR_VAR = 0;
    public static final int VAR_VAR_INT = 1;
    public static final int VAR_VAR_FLT = 2;
    public static final int VAR_VAR_STR = 3;
    public static final int VAR_VAR_REG = 4;

    public static final int VAR_INT_VAR = 5;
    public static final int VAR_INT_INT = 6;
    public static final int VAR_INT_FLT = 7;
    public static final int VAR_INT_STR = 8;
    public static final int VAR_INT_REG = 9;

    public static final int VAR_FLT_VAR = 10;
    public static final int VAR_FLT_INT = 11;
    public static final int VAR_FLT_FLT = 12;
    public static final int VAR_FLT_STR = 13;
    public static final int VAR_FLT_REG = 14;

    public static final int VAR_STR_VAR = 15;
    public static final int VAR_STR_INT = 16;
    public static final int VAR_STR_FLT = 17;
    public static final int VAR_STR_STR = 18;
    public static final int VAR_STR_REG = 19;

    public static final int VAR_REG_VAR = 20;
    public static final int VAR_REG_INT = 21;
    public static final int VAR_REG_FLT = 22;
    public static final int VAR_REG_STR = 23;
    public static final int VAR_REG_REG = 24;

    public static final int REG_VAR_VAR = 25;
    public static final int REG_VAR_INT = 26;
    public static final int REG_VAR_FLT = 27;
    public static final int REG_VAR_STR = 28;
    public static final int REG_VAR_REG = 29;

    public static final int REG_INT_VAR = 30;
    public static final int REG_INT_INT = 31;
    public static final int REG_INT_FLT = 32;
    public static final int REG_INT_STR = 33;
    public static final int REG_INT_REG = 34;

    public static final int REG_FLT_VAR = 35;
    public static final int REG_FLT_INT = 36;
    public static final int REG_FLT_FLT = 37;
    public static final int REG_FLT_STR = 38;
    public static final int REG_FLT_REG = 39;

    public static final int REG_STR_VAR = 40;
    public static final int REG_STR_INT = 41;
    public static final int REG_STR_FLT = 42;
    public static final int REG_STR_STR = 43;
    public static final int REG_STR_REG = 44;

    public static final int REG_REG_VAR = 45;
    public static final int REG_REG_INT = 46;
    public static final int REG_REG_FLT = 47;
    public static final int REG_REG_STR = 48;
    public static final int REG_REG_REG = 49;
}
