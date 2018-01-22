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

import com.libra.Log;

import com.libra.virtualview.common.StringBase;

/**
 * Created by gujicheng on 16/8/16.
 */
public abstract class LayoutParser extends ViewBaseParser {
    private final static String TAG = "LayoutParser_TMTEST";

    LayoutBaseImpParser mLayoutParserImp = new LayoutBaseImpParser();

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);
        if (CONVERT_RESULT_FAILED == ret) {
            ret = mLayoutParserImp.convertAttribute(key, value);

            if (CONVERT_RESULT_FAILED == ret) {
                ret = CONVERT_RESULT_OK;
                switch (key) {
                    case StringBase.STR_ID_borderWidth:
                        if (!parseFloat(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderWidth value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderWidth:" + value);
                        }
                        break;
                    case StringBase.STR_ID_borderRadius:
                        if (!parseFloat(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderRadius value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderRadius:" + value);
                        }
                        break;
                    case StringBase.STR_ID_borderTopLeftRadius:
                        if (!parseFloat(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderTopLeftRadius value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderTopLeftRadius:" + value);
                        }
                        break;
                    case StringBase.STR_ID_borderTopRightRadius:
                        if (!parseFloat(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderTopRightRadius value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderTopRightRadius:" + value);
                        }
                        break;
                    case StringBase.STR_ID_borderBottomLeftRadius:
                        if (!parseFloat(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderBottomLeftRadius value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderBottomLeftRadius:" + value);
                        }
                        break;
                    case StringBase.STR_ID_borderBottomRightRadius:
                        if (!parseFloat(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderBottomRightRadius value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderBottomRightRadius:" + value);
                        }
                        break;
                    case StringBase.STR_ID_borderColor:
                        if (!parseColor(value)) {
                            ret = CONVERT_RESULT_FAILED;
                            Log.e(TAG, "borderColor value error:" + value);
                        } else {
                            Log.d(TAG, "parse borderColor:" + value);
                        }
                        break;

                    default:
                        ret = CONVERT_RESULT_FAILED;
                        break;
                }
            }
        }

        return ret;
    }
}
