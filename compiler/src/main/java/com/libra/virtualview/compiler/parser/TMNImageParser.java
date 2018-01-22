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

import com.libra.virtualview.common.Common;
import com.libra.virtualview.common.StringBase;

/**
 * Created by gujicheng on 16/8/16.
 */
public class TMNImageParser extends ImageBaseParser {

    private final static String TAG = "TMNImageParser_TMTEST";

    private int downgradeImgUrlId;

    @Override
    public int getId() {
        return Common.VIEW_ID_TMNativeImage;
    }

    public static class Builder implements IBuilder {
        @Override
        public Parser build(String name) {
            if (TextUtils.equals(name, "TMNImage")) {
                return new TMNImageParser();
            }
            return null;
        }
    }

    @Override
    public void init() {
        super.init();
        downgradeImgUrlId = mStringSupport.getStringId("downgradeImgUrl", true);
    }

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);
        if (ViewBaseParser.CONVERT_RESULT_FAILED == ret) {
            ret = ViewBaseParser.CONVERT_RESULT_OK;
            if (key == StringBase.STR_ID_maskColor) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    if (!parseColor(value)) {
                        ret = CONVERT_RESULT_ERROR;
                        Log.e(TAG, "maskColor value error:" + value);
//                    } else {
//                        Log.d(TAG, "parse maskColor:" + value);
                    }
                } else {
                    Log.e(TAG, "parse maskColor value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == StringBase.STR_ID_blurRadius) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    if (!parseInteger(value)) {
                        ret = CONVERT_RESULT_ERROR;
                        Log.e(TAG, "blurRadius value error:" + value);
//                    } else {
//                        Log.d(TAG, "parse blurRadius:" + value);
                    }
                } else {
                    Log.e(TAG, "parse blurRadius value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == StringBase.STR_ID_filterWhiteBg) {
                if ("TRUE".equalsIgnoreCase(value.mStrValue)) {
                    value.setIntValue(1);
                } else {
                    value.setIntValue(0);
                }
            } else if (key == StringBase.STR_ID_ratio) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    if (!parseFloat(value)) {
                        ret = CONVERT_RESULT_ERROR;
                        Log.e(TAG, "ratio value error:" + value);
//                    } else {
//                        Log.d(TAG, "parse ratio:" + value);
                    }
                } else {
                    Log.e(TAG, "parse ratio value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == StringBase.STR_ID_disablePlaceHolder) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    parseNumber(value);
                } else {
                    Log.e(TAG, "parse disablePlaceHolder value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == StringBase.STR_ID_disableCache) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    parseNumber(value);
                } else {
                    Log.e(TAG, "parse disableCache value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == StringBase.STR_ID_fixBy){
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    parseNumber(value);
                } else {
                    Log.e(TAG, "parse fixBy value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == StringBase.STR_ID_ck) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    Log.e(TAG, "store ck " + value);
                } else {
                    Log.e(TAG, "parse ck value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else if (key == downgradeImgUrlId) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    Log.e(TAG, "store downgradeImgUrlId " + value);
                } else {
                    Log.e(TAG, "parse downgradeImgUrlId value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else {
                ret = CONVERT_RESULT_FAILED;
            }
        }
        return ret;
    }
}
