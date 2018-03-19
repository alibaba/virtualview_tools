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

package com.libra.virtualview.common;

import com.libra.Color;
import com.libra.Log;
import com.libra.TextUtils;

/**
 * Created by gujicheng on 16/8/3.
 */
public class Common {
    public final static String TAG = "ALIVV";

    public final static byte CODE_START_TAG = 0;
    public final static byte CODE_END_TAG = 1;

    // versions
    public final static int MAJOR_VERSION = 1;
    public final static int MINOR_VERSION = 0;
    public final static int PATCH_VERSION = 0;

    public final static int TYPE_INT = 1;
    public final static int TYPE_FLOAT = 2;
    public final static int TYPE_STRING = 3;

    public final static int MAX_TAB_SIZE = 20;
    public final static int MAX_PAGE_ITEM_COUNT = (1<<10);
    public final static int MAX_PAGE_ITEM_MASK = (MAX_PAGE_ITEM_COUNT - 1);

    public final static int USER_VIEW_ID_START = 1000;

    public final static int VIEW_ID_FrameLayout = 1;
    public final static int VIEW_ID_VHLayout = 2;
    public final static int VIEW_ID_VH2Layout = 3;
    public final static int VIEW_ID_GridLayout = 4;
    public final static int VIEW_ID_FlexLayout = 5;
    public final static int VIEW_ID_RatioLayout = 6;

    public final static int VIEW_ID_NativeText = 7;
    public final static int VIEW_ID_VirtualText = 8;
    public final static int VIEW_ID_NativeImage = 9;
    public final static int VIEW_ID_VirtualImage = 10;
    public final static int VIEW_ID_TMVirtualImage = 11;
    public final static int VIEW_ID_TMNativeImage = 12;
    public final static int VIEW_ID_NativeLine = 13;
    public final static int VIEW_ID_VirtualLine = 14;

    public final static int VIEW_ID_Scroller = 15;
    public final static int VIEW_ID_Page = 16;
    public final static int VIEW_ID_Grid = 17;
    public final static int VIEW_ID_VH = 18;
    public final static int VIEW_ID_Slider = 19;

    public final static int VIEW_ID_VirtualTime = 20;
    public final static int VIEW_ID_VirtualGraph = 21;
    public final static int VIEW_ID_VirtualProgress = 22;
    public final static int VIEW_ID_VirtualContainer = 23;

    public final static int VIEW_ID_Var = 24;

    public final static int VIEW_ID_NFrameLayout = 25;
    public final static int VIEW_ID_NGridLayout = 26;
    public final static int VIEW_ID_NRatioLayout = 27;
    public final static int VIEW_ID_NVH2Layout = 28;
    public final static int VIEW_ID_NVHLayout = 29;

    // only parse integer
    public static boolean parseInteger(DataItem value) {
        boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            try {
                // int
                    value.setIntValue(Integer.parseInt(value.mStrValue));
                ret = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "parseInteger value invalidate:" + value);
        }
        return ret;
    }

    public static boolean parseColor(DataItem value) {
        boolean ret = false;
        if (!TextUtils.isEmpty(value.mStrValue)) {
            try {
                ret = true;
                int v = 0;
                if (TextUtils.equals("black", value.mStrValue)) {
                    v = Color.BLACK;
                } else if (TextUtils.equals("blue", value.mStrValue)) {
                    v = Color.BLUE;
                } else if (TextUtils.equals("cyan", value.mStrValue)) {
                    v = Color.CYAN;
                } else if (TextUtils.equals("dkgray", value.mStrValue)) {
                    v = Color.DKGRAY;
                } else if (TextUtils.equals("gray", value.mStrValue)) {
                    v = Color.GRAY;
                } else if (TextUtils.equals("green", value.mStrValue)) {
                    v = Color.GREEN;
                } else if (TextUtils.equals("ltgray", value.mStrValue)) {
                    v = Color.LTGRAY;
                } else if (TextUtils.equals("magenta", value.mStrValue)) {
                    v = Color.MAGENTA;
                } else if (TextUtils.equals("magenta", value.mStrValue)) {
                    v = Color.MAGENTA;
                } else if (TextUtils.equals("red", value.mStrValue)) {
                    v = Color.RED;
                } else if (TextUtils.equals("transparent", value.mStrValue)) {
                    v = Color.TRANSPARENT;
                } else if (TextUtils.equals("yellow", value.mStrValue)) {
                    v = Color.YELLOW;
                } else {
                    v = Color.parseColor(value.mStrValue);
                }
                value.setIntValue(v);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                ret = false;
            }
        }

        return ret;
    }

}
