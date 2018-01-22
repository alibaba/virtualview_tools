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


import com.libra.Log;
import com.libra.TextUtils;

/**
 * Created by gujicheng on 17/4/25.
 */

public class StringSlice implements CharSequence {
    private final static String TAG = "StringSlice_TMTEST";

    private String mStr;
    private int mStartPos;
    private int mStrLen;

    public StringSlice() {
    }

    public StringSlice(String str, int start, int len) {
        setStr(str, start, len);
    }

    public void setPos(int start, int len) {
        if (start >= 0 && len > 0) {
            mStartPos = start;
            mStrLen = len;
        }
    }

    public void setStr(String str, int start, int len) {
        if (!TextUtils.isEmpty(str) && start >= 0 && len > 0) {
            Log.d(TAG, "start:" + start + "  len:" + len);
            mStr = str;
            mStartPos = start;
            mStrLen = len;
        }
    }

    @Override
    public int length() {
        return mStrLen;
    }

    @Override
    public char charAt(int i) {
        return mStr.charAt(mStartPos + i);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringSlice(mStr, mStartPos + start, end - start);
    }

    @Override
    public String toString() {
        return String.format("StringSlice:%s", mStr.substring(mStartPos, mStartPos + mStrLen));
    }
}
