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

import com.libra.Log;

/**
 * Created by gujicheng on 16/9/8.
 */
public class WordParser extends Parser {
    private final static String TAG = "WordParser_TMTEST";

    @Override
    public int addChar(char ch) {
        int ret = Parser.RESULT_NEED_MORE;

        switch (mState) {
            case STATE_Start:
                if (isFirstSymbol(ch)) {
                    mState = STATE_Body;
                    mStrBuilder.append(ch);
                } else {
                    ret = Parser.RESULT_FAILED;
                }
                break;

            case STATE_Body:
                if ((!isFirstSymbol(ch)) && (ch < '0' || ch > '9')) {
                    ret = Parser.RESULT_FINISHED_REMAIN;
                } else {
                    mStrBuilder.append(ch);
                }
                break;
        }

        return ret;
    }

    @Override
    public Token getToken() {
        Token ret = null;
        String str = mStrBuilder.toString();
        int value = mStringManager.getStringId(str, true);
        if (value == 0) {
            Log.e(TAG, str + " is not string:" + str);
        } else {
            ret = new WordToken(value);
        }
        return ret;
    }

    private static boolean isFirstSymbol(char ch) {
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_') ? true
                : false;
    }
}
