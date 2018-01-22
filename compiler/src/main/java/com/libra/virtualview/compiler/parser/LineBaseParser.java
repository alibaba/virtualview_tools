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

import com.libra.virtualview.common.StringBase;
import com.libra.virtualview.common.LineBaseCommon;

/**
 * Created by gujicheng on 16/11/2.
 */

public abstract class LineBaseParser extends ViewBaseParser {
    private final static String TAG = "LineBaseParser_TMTEST";

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);

        if (ViewBaseParser.CONVERT_RESULT_FAILED == ret) {
            ret = ViewBaseParser.CONVERT_RESULT_OK;
            switch (key) {
                case StringBase.STR_ID_color:
                    if (!parseColor(value)) {
                        ret = CONVERT_RESULT_FAILED;
                    }
                    break;

                case StringBase.STR_ID_orientation:
                    if (TextUtils.equals(value.mStrValue, "H")) {
                        value.setIntValue(1);
                    } else if (TextUtils.equals(value.mStrValue, "V")) {
                        value.setIntValue(0);
                    } else {
                        ret = CONVERT_RESULT_ERROR;
                        Log.e(TAG, "orientation value error:" + value);
                    }
                    break;

                case StringBase.STR_ID_style:
                    if (value.mStrValue.equals("solid")) {
                        value.setIntValue(LineBaseCommon.STYLE_SOLID);
                    } else if (value.mStrValue.equals("dash")) {
                        Log.d(TAG, "set dash");
                        value.setIntValue(LineBaseCommon.STYLE_DASH);
                    } else {
                        ret = CONVERT_RESULT_ERROR;
                        Log.e(TAG, "style value error:" + value);
                    }
                    break;

                case StringBase.STR_ID_paintWidth:
                    if (!parseNumber(value)) {
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
}
