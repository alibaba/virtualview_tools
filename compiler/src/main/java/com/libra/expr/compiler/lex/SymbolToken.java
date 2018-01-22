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

package com.libra.expr.compiler.lex;

/**
 * Created by gujicheng on 16/9/8.
 */
public class SymbolToken extends Token {
    private final static String sSymbols[] = {
            "#", "@", ".", "(", ")", "?",":" ,";", ",","{","}","[","]",
            "+", "-", "*", "/", "%", "!","=", ">","<",
            "+=", "-=", "*=", "/=", "%=", "!=", "==", ">=", "<=", "&&", "||",
            "minus", "end"};

    public final static char WELL = 0;
    public final static char AT = 1;
    public final static char DOT = 2;
    public final static char LEFT_BRACKET = 3;
    public final static char RIGHT_BRACKET = 4;
    public final static char QUESTION = 5;
    public final static char COLON = 6;
    public final static char SENTENCE_END = 7;
    public final static char COMMA = 8;
    public final static char LEFT_BIG_BRACKET = 9;
    public final static char RIGHT_BIG_BRACKET = 10;
    public final static char LEFT_MID_BRACKET = 11;
    public final static char RIGHT_MID_BRACKET = 12;

    public final static char ADD = 13;
    public final static char SUB = 14;
    public final static char MUL = 15;
    public final static char DIV = 16;
    public final static char MOD = 17;
    public final static char NOT = 18;
    public final static char EQ = 19;
    public final static char GT = 20;
    public final static char LT = 21;

    public final static char ADD_EQ = 22;
    public final static char SUB_EQ = 23;
    public final static char MUL_EQ = 24;
    public final static char DIV_EQ = 25;
    public final static char MOD_EQ = 26;
    public final static char NOT_EQ = 27;
    public final static char EQ_EQ = 28;
    public final static char GE = 29;
    public final static char LE = 30;
    public final static char AND = 31;
    public final static char OR = 32;

    public final static char MINUS = 33;

    public final static char END = 34;

    public char mValue;

    public final static SymbolToken TOKEN_END = new SymbolToken(END);

    public SymbolToken(char ch) {
        mType = TYPE_SYMBOL;

        mValue = ch;
    }

    @Override
    public String toString() {
        return String.format("Type:symbol value:%s, v:%d", show(mValue), (int)mValue);
    }

    public static String show(int value) {
        return sSymbols[value];
    }

}
