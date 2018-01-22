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
public class SymbolParser extends Parser {
//    private final static String sSymbols[] = {
//            "#", "@", ".", "(", ")", "?",":" ,";","{","}",
//            "+", "-", "*", "/", "%"，"!","=", ">","<","&", "|",
//            "+=", "-=", "*=", "/=", "%="，"!=","==",">=","<=","&&","||"
//            "minus", "end"};

    private final static String sSingleSymbols = "#@.()?:;,{}[]";
    private final static String sSingleOrDoubleSymbols = "+-*/%!=><";

    private char mSymbol;
    private char mPreSymbol;

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public int addChar(char ch) {
        int ret = Parser.RESULT_NEED_MORE;

        switch (mState) {
            case STATE_Start:
                mPreSymbol = ch;
                int index = sSingleSymbols.indexOf(ch);
                if (index >= 0) {
                    mSymbol = (char)index;

                    ret = RESULT_FINISHED;
                } else {
                    index = sSingleOrDoubleSymbols.indexOf(ch);
                    if (index >= 0) {
                        mSymbol = (char) (index + sSingleSymbols.length());
                        mState = STATE_Body;
                    } else if ('&' == ch || '|' == ch) {
                        mState = STATE_Body;
                    } else {
                        ret = RESULT_FAILED;
                    }
                }
                break;

            case STATE_Body:
                if ('=' == ch) {
                    mSymbol += sSingleOrDoubleSymbols.length();
                    ret = RESULT_FINISHED;
                } else if (mPreSymbol == ch ) {
                    if ('&' == ch) {
//                        mSymbol += sSingleOrDoubleSymbols.length();
                        mSymbol = SymbolToken.AND;
                        ret = RESULT_FINISHED;
                    } else if ('|' == ch) {
                        mSymbol = SymbolToken.OR;
                        ret = RESULT_FINISHED;
                    } else {
                        ret = RESULT_FINISHED_REMAIN;
                    }
                } else {
                    ret = RESULT_FINISHED_REMAIN;
                }
                break;
        }
//        mChar = ch;
//        return (sSymbols.indexOf(ch) < 0) ? Parser.RESULT_FAILED : Parser.RESULT_FINISHED;

        return ret;
    }

    @Override
    public Token getToken() {
        return new SymbolToken(mSymbol);
    }
}
