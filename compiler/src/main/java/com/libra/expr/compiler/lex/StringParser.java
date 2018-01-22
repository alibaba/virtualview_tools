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

import com.libra.TextUtils;
import com.libra.Log;

/**
 * Created by gujicheng on 16/9/8.
 */
public class StringParser extends Parser {
    private final static String TAG = "StringParser_TMTEST";

    private static final int STATE_TRANSFER = 2;

    @Override
    public int addChar(char ch) {
        int ret = Parser.RESULT_NEED_MORE;

        switch (mState) {
            case STATE_Start:
                if ('\'' == ch) {
                    mState = STATE_Body;
                } else {
                    ret = Parser.RESULT_FAILED;
                }
                break;

            case STATE_Body:
                if ('\'' == ch) {
                    ret = Parser.RESULT_FINISHED;
                } else if ('\\' == ch) {
                    mState = STATE_TRANSFER;
                } else {
                    mStrBuilder.append(ch);
                }
                break;

            case STATE_TRANSFER:
                if ('\\' == ch || '\'' == ch) {
                    mState = STATE_Body;
                    mStrBuilder.append(ch);
                } else {
                    Log.e(TAG, "invalidate transfer:" + ch);
                    ret = Parser.RESULT_FAILED;
                }
                break;
        }

        return ret;
    }

    @Override
    public Token getToken() {
        Token ret = null;

        String str = mStrBuilder.toString();
        if (!TextUtils.isEmpty(str)) {
            int value = mStringManager.getStringId(str, true);
            if (value == 0) {
                Log.e(TAG, str + " is not string:");
            } else {
                ret = new StringToken(value);
            }
        } else {
            // empty string
            ret = new StringToken(-1);
        }

        return ret;
    }
}
