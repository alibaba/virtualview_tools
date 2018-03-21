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
import com.libra.TextUtils;
import com.libra.Utils;

/**
 * Created by gujicheng on 16/9/8.
 */
public class NumberParser extends Parser {
    private final static String TAG = "NumberParser_TMTEST";

    private final static int STATE_COLOR = 2;

//    private boolean mIsHex;
//    private boolean mIsFloat;

    private int mFlag = 0;

    private final static int FLAG_HEX = 1;
    private final static int FLAG_FLOAT = 2;
    private final static int FLAG_COLOR = 4;

    @Override
    public void reset() {
        super.reset();

        mFlag = 0;
//        mIsHex = false;
//        mIsFloat = false;
    }

    @Override
    public int addChar(char ch) {
        int ret = RESULT_NEED_MORE;

        switch (mState) {
            case STATE_Start:
                if (Utils.isDigit(ch)) {
                    mState = STATE_Body;
                    mStrBuilder.append(ch);
                } else if ('#' == ch) {
                    mState = STATE_COLOR;
                    mFlag |= FLAG_COLOR;
                } else {
                    ret = RESULT_FAILED;
                }
                break;

            case STATE_Body:
                if ('x' == ch || 'X' == ch) {
                    if (mStrBuilder.length() == 1 && TextUtils.equals("0", mStrBuilder.substring(0))) {
                        mFlag |= FLAG_HEX;
//                        mIsHex = true;
                        mStrBuilder.append(ch);
                    } else {
                        ret = RESULT_FINISHED_REMAIN;
                    }
                } else if ((ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
                    if (0 != (mFlag & FLAG_HEX)) {
                        mStrBuilder.append(ch);
                    } else {
                        ret = RESULT_FINISHED_REMAIN;
                    }
                } else if (Utils.isDigit(ch)) {
                    mStrBuilder.append(ch);
                } else if ('.' == ch) {
                    mFlag |= FLAG_FLOAT;

//                    mIsFloat = true;
                    mStrBuilder.append(ch);
                } else {
                    ret = RESULT_FINISHED_REMAIN;
                }
                break;

            case STATE_COLOR:
                if (Utils.isHex(ch)) {
                    mStrBuilder.append(ch);
                } else {
                    ret = RESULT_FINISHED_REMAIN;
                }
                break;
        }

        return ret;
    }

    @Override
    public Token getToken() {
        Token ret = null;
        String str = mStrBuilder.toString();
        try {
            if (0 != (mFlag & FLAG_FLOAT)) {
//                Log.d(TAG, "float:" + str);
                ret = new FloatToken(Float.parseFloat(str));
            } else {
                int v = 0;
                if (0 != (mFlag & FLAG_HEX)) {
                    str = str.substring(2);
//                    Log.d(TAG, "hex integer:" + str);
                    v = Integer.parseInt(str, 16);
                } else if (0 != (mFlag & FLAG_COLOR)) {
                    v = parseColor(str);
//                    Log.d(TAG, "parseColor str:" + str + "  value:" + v);
                } else {
//                    Log.d(TAG, "integer:" + str);
                    v = Integer.parseInt(str);
                }
                ret = new IntegerToken(v);
            }
        } catch(NumberFormatException e) {
            Log.e(TAG, "parse number error:" + e);
        }

        return ret;
    }

    private int parseColor(String str) {
        int ret = 0;

        if (!TextUtils.isEmpty(str)) {
            int len = str.length();
            String strColor = null;
            long alpha = 0;
            if (6 == len) {
                strColor = str;
                alpha = 0x00000000ff000000;
            } else if (8 == len) {
                strColor = str.substring(0, 6);
                String strAlpah = str.substring(6);
//                Log.d(TAG, "strAlpah:" + strAlpah + "  strColor:" + strColor);

                alpha = (Long.parseLong(strAlpah, 16)) << 24;
            } else {
                Log.e(TAG, "parseColor format error:" + str);
            }

            if (null != strColor) {
                long v = Long.parseLong(strColor, 16);
                v |= alpha;
                ret = (int)v;
            }
        } else {
            Log.e(TAG, "str is empty");
        }

        return ret;
    }
}
