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

/**
 * Created by gujicheng on 17/5/18.
 */

public class ViewBaseCommon {
    final public static int VERTICAL = 0;
    final public static int HORIZONTAL = 1;

    final public static int NORMAL = 0;
    final public static int REVERSE = 1;

    final public static int LEFT = 1;
    final public static int RIGHT = 2;
    final public static int H_CENTER = 4;
    final public static int TOP = 8;
    final public static int BOTTOM = 16;
    final public static int V_CENTER = 32;

    final public static int FLAG_DRAW = 0x00000001;
    final public static int FLAG_EVENT = 0x00000002;
    final public static int FLAG_DYNAMIC = 0x00000004;
    final public static int FLAG_SOFTWARE = 0x00000008;
    final public static int FLAG_EXPOSURE = 0x00000010;
    final public static int FLAG_CLICKABLE = 0x00000020;
    final public static int FLAG_LONG_CLICKABLE = 0x00000040;
    final public static int FLAG_TOUCHABLE = 0x00000080;

    final public static int AUTO_DIM_DIR_NONE = 0;
    final public static int AUTO_DIM_DIR_X = 1;
    final public static int AUTO_DIM_DIR_Y = 2;


    final public static int INVISIBLE = 0;
    final public static int VISIBLE = 1;
    final public static int GONE = 2;

    final public static int ANIMATION_LINEAR = 0;
    final public static int ANIMATION_DECELERATE = 1;
    final public static int ANIMATION_ACCELERATE = 2;
    final public static int ANIMATION_ACCELERATEDECELERATE = 3;
    final public static int ANIMATION_SPRING = 4;

}
