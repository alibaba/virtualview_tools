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

import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_CENTER;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_CENTER_CROP;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_CENTER_INSIDE;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_FIT_CENTER;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_FIT_END;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_FIT_START;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_FIT_XY;
import static com.libra.virtualview.common.ImageCommon.SCALE_TYPE_MATRIX;

/**
 * Created by gujicheng on 16/8/19.
 */
public abstract class ImageBaseParser extends ViewBaseParser {
    private final static String TAG = "ImageBaseBuilder";

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);

        if (ViewBaseParser.CONVERT_RESULT_FAILED == ret) {
            ret = ViewBaseParser.CONVERT_RESULT_OK;
            switch (key) {
                case StringBase.STR_ID_src:
                    Log.i(TAG, "image src value " + value.mStrValue);
                    ret = ViewBaseParser.CONVERT_RESULT_FAILED;
                    break;
                case StringBase.STR_ID_scaleType:
                    int v = parseScaleType(value.mStrValue);
                    if (v > -1) {
                        value.setIntValue(v);
                    } else {
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                    }
                    break;

                default:
                    ret = ViewBaseParser.CONVERT_RESULT_FAILED;
                    break;

            }
        }

        return ret;
    }

    private int parseScaleType(String str) {
        int ret = -1;

        if (TextUtils.equals(str, "center")) {
            ret = SCALE_TYPE_CENTER;
        } else if (TextUtils.equals(str, "center_crop")) {
            ret = SCALE_TYPE_CENTER_CROP;
        } else if (TextUtils.equals(str, "center_inside")) {
            ret = SCALE_TYPE_CENTER_INSIDE;
        } else if (TextUtils.equals(str, "fit_center")) {
            ret = SCALE_TYPE_FIT_CENTER;
        } else if (TextUtils.equals(str, "fit_end")) {
            ret = SCALE_TYPE_FIT_END;
        } else if (TextUtils.equals(str, "fit_start")) {
            ret = SCALE_TYPE_FIT_START;
        } else if (TextUtils.equals(str, "fit_xy")) {
            ret = SCALE_TYPE_FIT_XY;
        } else if (TextUtils.equals(str, "matrix")) {
            ret = SCALE_TYPE_MATRIX;
        } else {
            Log.e(TAG, "parseScaleType error:" + str);
        }

        return ret;
    }
}
