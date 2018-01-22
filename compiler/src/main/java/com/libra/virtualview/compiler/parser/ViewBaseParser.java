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

import com.libra.Color;
import com.libra.TextUtils;
import com.libra.Log;

import com.libra.Utils;
import com.libra.expr.common.ExprCode;
import com.libra.virtualview.common.IDataLoaderCommon;
import com.libra.virtualview.common.StringBase;
import com.libra.virtualview.common.ViewBaseCommon;

/**
 * Created by gujicheng on 16/8/16.
 */
public abstract class ViewBaseParser extends Parser {
    final static private String TAG = "Parser_TMTEST";

    // if convertAttribute return true
    public int convertAttribute(int key, AttrItem value) {
        int ret = CONVERT_RESULT_OK;

        switch (key) {
            case StringBase.STR_ID_minWidth:
            case StringBase.STR_ID_minHeight:
            case StringBase.STR_ID_id:
            case StringBase.STR_ID_uuid:
                if (!parseInteger(value)) {
                    ret = CONVERT_RESULT_ERROR;
                }
                break;

            case StringBase.STR_ID_paddingLeft:
            case StringBase.STR_ID_paddingRight:
            case StringBase.STR_ID_paddingTop:
            case StringBase.STR_ID_paddingBottom:
            case StringBase.STR_ID_autoDimX:
            case StringBase.STR_ID_autoDimY:
            case StringBase.STR_ID_alpha:
                if (!parseNumber(value)) {
                    ret = CONVERT_RESULT_FAILED;
                }
                break;

            case StringBase.STR_ID_gravity:
                if (!parseAlign(value)) {
                    Log.e(TAG, "parseAlign error:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
                break;

            case StringBase.STR_ID_background:
                if (!parseColor(value)) {
                    ret = CONVERT_RESULT_FAILED;
                }
                break;

            case StringBase.STR_ID_flag:
                if (!parseFlag(value)) {
                    ret = CONVERT_RESULT_ERROR;
                }
                break;

            case StringBase.STR_ID_onClick:
            case StringBase.STR_ID_onLongClick:
            case StringBase.STR_ID_onBeforeDataLoad:
            case StringBase.STR_ID_onAfterDataLoad:
            case StringBase.STR_ID_onSetData:
                ret = exprParse(value);
                break;

            case StringBase.STR_ID_autoDimDirection:
                if (!parseDimDirection(value)) {
                    ret = CONVERT_RESULT_ERROR;
                }
                break;

            case StringBase.STR_ID_visibility:
                if (!parseVisibility(value)) {
                    ret = CONVERT_RESULT_FAILED;
                }
                break;

            case StringBase.STR_ID_dataMode:
                if (!parseDataMode(value)) {
                    Log.e(TAG, "parseDataMode error:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
                break;

            default:
                ret = CONVERT_RESULT_FAILED;
        }

        return ret;
    }

    protected boolean parseDataMode(AttrItem value) {
        boolean ret = true;

        if (TextUtils.equals(value.mStrValue, "set")) {
            value.setIntValue(IDataLoaderCommon.MODE_SET);
        } else if (TextUtils.equals(value.mStrValue, "append")) {
            value.setIntValue(IDataLoaderCommon.MODE_APPEND);
        } else {
            ret = parseNumber(value);
            if (ret && (value.getmIntValue() != IDataLoaderCommon.MODE_SET && value.getmIntValue() != IDataLoaderCommon.MODE_APPEND)) {
                ret = false;
            }
        }

        return ret;
    }

    protected boolean parseVisibility(AttrItem value) {
        boolean ret = true;

        if (TextUtils.equals(value.mStrValue, "invisible")) {
            value.setIntValue(ViewBaseCommon.INVISIBLE);
        } else if (TextUtils.equals(value.mStrValue, "visible")) {
            value.setIntValue(ViewBaseCommon.VISIBLE);
        } else if (TextUtils.equals(value.mStrValue, "gone")) {
            value.setIntValue(ViewBaseCommon.GONE);
        } else {
            ret = parseNumber(value);
            if (ret && (value.getmIntValue() < 0 || value.getmIntValue() > 2)) {
                ret = false;
            }
        }

        return ret;
    }

    protected int exprParse(AttrItem value) {
        int ret = CONVERT_RESULT_OK;

        if (value.mStrValue.startsWith("${") && value.mStrValue.endsWith("}")) {
            if (mExprCompiler.compile(value.mStrValue)) {
                ExprCode code = mExprCompiler.getCode();
                if (null != code) {
                    int codeIndex = value.mStrValue.hashCode();
                    mExprCodeStore.addCode(code, codeIndex);
                    value.setCode(codeIndex);
                } else {
                    ret = CONVERT_RESULT_ERROR;
                    Log.e(TAG, "compile result code is null:" + value.mStrValue);
                }
            } else {
                ret = CONVERT_RESULT_ERROR;
                Log.e(TAG, "compile expr failed:" + value.mStrValue);
            }
        } else {
            int codeIndex = mExprCodeStore.getIndex(value.mStrValue.trim());
            if (codeIndex != 0) {
                value.setCode(codeIndex);
            } else {
                ret = CONVERT_RESULT_ERROR;
                Log.e(TAG, "onClick is not expr:" + value.mStrValue);
            }
        }

        return ret;
    }

    public static boolean parseColor(AttrItem item) {
        boolean ret = true;

        int value = 0;
        String str = item.mStrValue;
        if (Utils.isEL(str)) {
            item.setStr(str);
        } else {
            try {
                if (TextUtils.equals("black", str)) {
                    value = Color.BLACK;
                } else if (TextUtils.equals("blue", str)) {
                    value = Color.BLUE;
                } else if (TextUtils.equals("cyan", str)) {
                    value = Color.CYAN;
                } else if (TextUtils.equals("dkgray", str)) {
                    value = Color.DKGRAY;
                } else if (TextUtils.equals("gray", str)) {
                    value = Color.GRAY;
                } else if (TextUtils.equals("green", str)) {
                    value = Color.GREEN;
                } else if (TextUtils.equals("ltgray", str)) {
                    value = Color.LTGRAY;
                } else if (TextUtils.equals("magenta", str)) {
                    value = Color.MAGENTA;
                } else if (TextUtils.equals("magenta", str)) {
                    value = Color.MAGENTA;
                } else if (TextUtils.equals("red", str)) {
                    value = Color.RED;
                } else if (TextUtils.equals("transparent", str)) {
                    value = Color.TRANSPARENT;
                } else if (TextUtils.equals("yellow", str)) {
                    value = Color.YELLOW;
                } else {
                    value = Color.parseColor(str);
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "parseColor error:" + e + " string:" + str);
                ret = false;
            }
            if (ret) {
                item.setIntValue(value);
            }
        }
        return ret;
    }

    protected static boolean parseFlag(AttrItem item) {
        boolean ret = true;

        int value = 0;
        String[] strArr = item.mStrValue.split("\\|");
        for (String str : strArr) {
            str = str.trim();
            if (TextUtils.equals("flag_draw", str)) {
                value |= ViewBaseCommon.FLAG_DRAW;
            } else if (TextUtils.equals("flag_event", str)) {
                value |= ViewBaseCommon.FLAG_EVENT;
            } else if (TextUtils.equals("flag_dynamic", str)) {
                value |= ViewBaseCommon.FLAG_DYNAMIC;
            } else if (TextUtils.equals("flag_software", str)) {
                value |= ViewBaseCommon.FLAG_SOFTWARE;
            } else if (TextUtils.equals("flag_exposure", str)) {
                value |= ViewBaseCommon.FLAG_EXPOSURE;
            } else if (TextUtils.equals("flag_clickable", str)) {
                value |= ViewBaseCommon.FLAG_CLICKABLE;
            } else if (TextUtils.equals("flag_longclickable", str)) {
                value |= ViewBaseCommon.FLAG_LONG_CLICKABLE;
            } else if (TextUtils.equals("flag_touchable", str)) {
                value |= ViewBaseCommon.FLAG_TOUCHABLE;
            } else {
                Log.e(TAG, "invalidate value:" + str);
                ret = false;
                break;
            }
        }

        if (ret) {
            item.setIntValue(value);
        }

        return ret;
    }

    protected static boolean parseDimDirection(AttrItem item) {
        boolean ret = true;

        if (TextUtils.equals(item.mStrValue, "X")) {
            item.setIntValue(ViewBaseCommon.AUTO_DIM_DIR_X);
        } else if (TextUtils.equals(item.mStrValue, "Y")) {
            item.setIntValue(ViewBaseCommon.AUTO_DIM_DIR_Y);
        } else {
            Log.e(TAG, "parseDimDirection failed:" + item);
            ret = false;
        }

        return ret;
    }

    protected static boolean parseAlign(AttrItem item) {
        boolean ret = true;
        if (Utils.isEL(item.mStrValue)) {
            item.setStr(item.mStrValue);
        } else {
            int value = 0;
            String[] strArr = item.mStrValue.split("\\|");
            for (String str : strArr) {
                str = str.trim();
                if (TextUtils.equals("left", str)) {
                    value |= ViewBaseCommon.LEFT;
                } else if (TextUtils.equals("right", str)) {
                    value |= ViewBaseCommon.RIGHT;
                } else if (TextUtils.equals("h_center", str)) {
                    value |= ViewBaseCommon.H_CENTER;
                } else if (TextUtils.equals("top", str)) {
                    value |= ViewBaseCommon.TOP;
                } else if (TextUtils.equals("bottom", str)) {
                    value |= ViewBaseCommon.BOTTOM;
                } else if (TextUtils.equals("v_center", str)) {
                    value |= ViewBaseCommon.V_CENTER;
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
