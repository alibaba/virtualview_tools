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

package com.libra.virtualview.compiler.parser;

import com.libra.TextUtils;
import com.libra.Log;

import com.libra.Utils;
import com.libra.virtualview.common.StringBase;
import com.libra.virtualview.common.TextBaseCommon;

/**
 * Created by gujicheng on 16/8/16.
 */
public abstract class TextBaseParser extends ViewBaseParser {
    private final static String TAG = "TextBaseBuilder";

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);

        if (ViewBaseParser.CONVERT_RESULT_FAILED == ret) {
            ret = ViewBaseParser.CONVERT_RESULT_OK;
            switch (key) {
                case StringBase.STR_ID_text:
                    Log.i(TAG, "text value " + value.mStrValue);
                    ret = CONVERT_RESULT_FAILED;
                    break;
                case StringBase.STR_ID_textColor:
                    if (!parseColor(value)) {
                        ret = CONVERT_RESULT_FAILED;
                    }
                    break;

                case StringBase.STR_ID_textSize:
                    if (!parseNumber(value)) {
                        ret = CONVERT_RESULT_FAILED;
                    }
                    break;

                case StringBase.STR_ID_textStyle:
                    if (!parseTextStyle(value)) {
                        Log.e(TAG, "parseTextStyle error");
                        ret = CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_lines:
                    if (!parseInteger(value)) {
                        Log.e(TAG, "parse lines error");
                        ret = CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_ellipsize:
                    if (value.mStrValue.equals("start")) {
                        value.setIntValue(TextBaseCommon.START);
                    } else if (value.mStrValue.equals("marquee")) {
                        value.setIntValue(TextBaseCommon.MARQUEE);
                    } else if (value.mStrValue.equals("middle")) {
                        value.setIntValue(TextBaseCommon.MIDDLE);
                    } else if (value.mStrValue.equals("end")) {
                        value.setIntValue(TextBaseCommon.END);
                    } else {
                        Log.e(TAG, "parse ellipsize error");
                        ret = CONVERT_RESULT_ERROR;
                    }
                    break;

                default:
                    ret = CONVERT_RESULT_FAILED;
                    break;
            }
        }

        return ret;
    }

    protected static boolean parseTextStyle(AttrItem item) {
        boolean ret = true;

        if (Utils.isEL(item.mStrValue)) {
            item.setStr(item.mStrValue);
        } else {
            int value = 0;
            String[] strs = item.mStrValue.split("\\|");
            for (String str : strs) {
                str = str.trim();
                if (TextUtils.equals("bold", str)) {
                    value |= TextBaseCommon.BOLD;
                } else if (TextUtils.equals("italic", str)) {
                    value |= TextBaseCommon.ITALIC;
                } else if (TextUtils.equals("strike", str)) {
                    value |= TextBaseCommon.STRIKE;
                } else {
                    Log.e(TAG, "invalidate value:" + str);
                    ret = false;
                    break;
                }
            }

            if (ret) {
                item.setIntValue(value);
            }
        }

        return ret;
    }
}
