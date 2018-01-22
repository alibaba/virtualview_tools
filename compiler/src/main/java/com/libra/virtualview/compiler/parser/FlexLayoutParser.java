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

import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_CONTENT_CENTER;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_CONTENT_FLEX_END;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_CONTENT_FLEX_START;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_CONTENT_SPACE_AROUND;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_CONTENT_SPACE_BETWEEN;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_CONTENT_STRETCH;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_ITEMS_BASELINE;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_ITEMS_CENTER;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_ITEMS_FLEX_END;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_ITEMS_FLEX_START;
import static com.libra.virtualview.common.FlexLayoutCommon.ALIGN_ITEMS_STRETCH;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_DIRECTION_COLUMN;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_DIRECTION_COLUMN_REVERSE;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_DIRECTION_ROW;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_DIRECTION_ROW_REVERSE;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_WRAP_NOWRAP;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_WRAP_WRAP;
import static com.libra.virtualview.common.FlexLayoutCommon.FLEX_WRAP_WRAP_REVERSE;
import static com.libra.virtualview.common.FlexLayoutCommon.JUSTIFY_CONTENT_CENTER;
import static com.libra.virtualview.common.FlexLayoutCommon.JUSTIFY_CONTENT_FLEX_END;
import static com.libra.virtualview.common.FlexLayoutCommon.JUSTIFY_CONTENT_FLEX_START;
import static com.libra.virtualview.common.FlexLayoutCommon.JUSTIFY_CONTENT_SPACE_AROUND;
import static com.libra.virtualview.common.FlexLayoutCommon.JUSTIFY_CONTENT_SPACE_BETWEEN;

/**
 * Created by gujicheng on 16/8/22.
 */
public class FlexLayoutParser extends LayoutParser {
    private final static String TAG = "FlexLayoutParser_TMTEST";

    public static class Builder implements Parser.IBuilder {
        @Override
        public Parser build(String name) {
            if (TextUtils.equals(name, "FlexLayout")) {
                return new FlexLayoutParser();
            }
            return null;
        }
    }

    @Override
    public int getId() {
        return Common.VIEW_ID_FlexLayout;
    }

    @Override
    public int convertAttribute(int key, Parser.AttrItem value) {
        int ret = super.convertAttribute(key, value);

        int v;
        if (ViewBaseParser.CONVERT_RESULT_FAILED == ret) {
            ret = ViewBaseParser.CONVERT_RESULT_OK;
            switch (key) {
                case StringBase.STR_ID_flexDirection:
                    v = parseDirection(value.mStrValue);
                    if (v > -1) {
                        value.setIntValue(v);
                    } else {
                        Log.e(TAG, "parseDirection error");
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_flexWrap:
                    v = parseWrap(value.mStrValue);
                    if (v > -1) {
                        value.setIntValue(v);
                    } else {
                        Log.e(TAG, "parseDirection error");
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_justifyContent:
                    v = parseJustifyContent(value.mStrValue);
                    if (v > -1) {
                        value.setIntValue(v);
                    } else {
                        Log.e(TAG, "parseDirection error");
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_alignItems:
                    v = parseAlignItems(value.mStrValue);
                    if (v > -1) {
                        value.setIntValue(v);
                    } else {
                        Log.e(TAG, "parseDirection error");
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_alignContent:
                    v = parseAlignContent(value.mStrValue);
                    if (v > -1) {
                        value.setIntValue(v);
                    } else {
                        Log.e(TAG, "parseDirection error");
                        ret = ViewBaseParser.CONVERT_RESULT_ERROR;
                    }
                    break;

                case StringBase.STR_ID_flexGrow:
                    if (!parseNumber(value)) {
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

    private static int parseAlignItems(String str) {
        int ret = -1;

        if (TextUtils.equals(str, "flex-start")) {
            ret = ALIGN_ITEMS_FLEX_START;
        } else if (TextUtils.equals(str, "flex-end")) {
            ret = ALIGN_ITEMS_FLEX_END;
        } else if (TextUtils.equals(str, "center")) {
            ret = ALIGN_ITEMS_CENTER;
        } else if (TextUtils.equals(str, "baseline")) {
            ret = ALIGN_ITEMS_BASELINE;
        } else if (TextUtils.equals(str, "stretch")) {
            ret = ALIGN_ITEMS_STRETCH;
        } else {
            Log.e(TAG, "parseFlexAlign error, invalidate str:" + str);
        }

        return ret;
    }

    private static int parseJustifyContent(String str) {
        int ret = -1;

        if (TextUtils.equals(str, "flex-start")) {
            ret = JUSTIFY_CONTENT_FLEX_START;
        } else if (TextUtils.equals(str, "flex-end")) {
            ret = JUSTIFY_CONTENT_FLEX_END;
        } else if (TextUtils.equals(str, "center")) {
            ret = JUSTIFY_CONTENT_CENTER;
        } else if (TextUtils.equals(str, "space-between")) {
            ret = JUSTIFY_CONTENT_SPACE_BETWEEN;
        } else if (TextUtils.equals(str, "space-around")) {
            ret = JUSTIFY_CONTENT_SPACE_AROUND;
        } else {
            Log.e(TAG, "parseFlexAlign error, invalidate str:" + str);
        }

        return ret;
    }

    private static int parseAlignContent(String str) {
        int ret = -1;

        if (TextUtils.equals(str, "flex-start")) {
            ret = ALIGN_CONTENT_FLEX_START;
        } else if (TextUtils.equals(str, "flex-end")) {
            ret = ALIGN_CONTENT_FLEX_END;
        } else if (TextUtils.equals(str, "center")) {
            ret = ALIGN_CONTENT_CENTER;
        } else if (TextUtils.equals(str, "space-between")) {
            ret = ALIGN_CONTENT_SPACE_BETWEEN;
        } else if (TextUtils.equals(str, "space-around")) {
            ret = ALIGN_CONTENT_SPACE_AROUND;
        } else if (TextUtils.equals(str, "stretch")) {
            ret = ALIGN_CONTENT_STRETCH;
        } else {
            Log.e(TAG, "parseFlexAlign error, invalidate str:" + str);
        }

        return ret;
    }

    private static int parseWrap(String str) {
        int ret = -1;

        if (TextUtils.equals(str, "wrap")) {
            ret = FLEX_WRAP_WRAP;
        } else if (TextUtils.equals(str, "nowrap")) {
            ret = FLEX_WRAP_NOWRAP;
        } else if (TextUtils.equals(str, "wrap-reverse")) {
            ret = FLEX_WRAP_WRAP_REVERSE;
        } else {
            Log.e(TAG, "parseWrap error, invalidate str:" + str);
        }

        return ret;
    }

    private static int parseDirection(String str) {
        int ret = -1;

        if (TextUtils.equals(str, "row")) {
            ret = FLEX_DIRECTION_ROW;
        } else if (TextUtils.equals(str, "row-reverse")) {
            ret = FLEX_DIRECTION_ROW_REVERSE;
        } else if (TextUtils.equals(str, "column")) {
            ret = FLEX_DIRECTION_COLUMN;
        } else if (TextUtils.equals(str, "column-reverse")) {
            ret = FLEX_DIRECTION_COLUMN_REVERSE;
        } else {
            Log.e(TAG, "parseDirection error, invalidate str:" + str);
        }

        return ret;
    }
}
