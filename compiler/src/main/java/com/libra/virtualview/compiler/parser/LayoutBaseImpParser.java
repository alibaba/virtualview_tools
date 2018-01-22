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

import com.libra.virtualview.common.LayoutCommon;
import com.libra.virtualview.common.StringBase;

/**
 * Created by gujicheng on 16/11/7.
 */

public class LayoutBaseImpParser extends Parser {
    private final static String TAG = "LayoutBIParser_TMTEST";

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = CONVERT_RESULT_OK;

        switch (key) {
            case StringBase.STR_ID_layoutWidth:
            case StringBase.STR_ID_layoutHeight:
                if (TextUtils.equals("wrap_content", value.mStrValue)) {
                    value.setIntValue(LayoutCommon.WRAP_CONTENT);
                } else if (TextUtils.equals("match_parent", value.mStrValue)) {
                    value.setIntValue(LayoutCommon.MATCH_PARENT);
                } else {
                    if (!parseNumber(value)) {
                        Log.e(TAG, "parse width or height error:" + value);
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
//                    } else {
//                        Log.d(TAG, "intValue:" + value.mIntValue);
                    }
                }
                break;

            case StringBase.STR_ID_layoutMarginLeft:
            case StringBase.STR_ID_layoutMarginRight:
            case StringBase.STR_ID_layoutMarginTop:
            case StringBase.STR_ID_layoutMarginBottom:
                if (!parseNumber(value)) {
                    Log.e(TAG, "parse margin or padding error:" + value);
                    ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                }
                break;

            default:
                ret = ViewBaseParser.CONVERT_RESULT_FAILED;
        }

        return ret;
    }
}
