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
import com.libra.virtualview.common.VH2LayoutCommon;

/**
 * Created by gujicheng on 16/10/11.
 */
public class VH2LayoutParser extends VHLayoutParser {
    final static private String TAG = "VH2LayoutParser";

    public static class Builder implements Parser.IBuilder {
        @Override
        public Parser build(String name) {
            if (TextUtils.equals(name, "VH2Layout")) {
                return new VH2LayoutParser();
            }
            return null;
        }
    }

    @Override
    public int getId() {
    	
        return Common.VIEW_ID_VH2Layout;
    }

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);

        if (CONVERT_RESULT_FAILED == ret) {
            ret = CONVERT_RESULT_OK;
            switch (key) {
                case StringBase.STR_ID_layoutDirection:
                    if (!parseDirection(value)) {
                        Log.e(TAG, "parseAlign error");
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

    protected static boolean parseDirection(AttrItem item) {
        boolean ret = false;

        int value = -1;
        String strValue = item.mStrValue;
        if (!TextUtils.isEmpty(strValue)) {
            ret = true;
            strValue = strValue.trim();
            if (TextUtils.equals("left", strValue)) {
                value = VH2LayoutCommon.DIRECTION_LEFT;
            } else if (TextUtils.equals("right", strValue)) {
                value = VH2LayoutCommon.DIRECTION_RIGHT;
            } else if (TextUtils.equals("top", strValue)) {
                value = VH2LayoutCommon.DIRECTION_TOP;
            } else if (TextUtils.equals("bottom", strValue)) {
                value = VH2LayoutCommon.DIRECTION_BOTTOM;
            } else {
                Log.e(TAG, "parseDirection invalidate value:" + strValue);
                ret = false;
            }
        } else {
            Log.e(TAG, "parseDirection str is empty");
        }

        if (ret) {
            item.setIntValue(value);
        }

        return ret;
    }
}
