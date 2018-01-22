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

package com.libra.expr.common;

import java.util.List;

/**
 * Created by gujicheng on 16/11/8.
 */

public class ExprCode {
    private final static String TAG = "ExprCode_TMTEST";

    public byte[] mCodeBase;
    public int mStartPos;
    public int mEndPos;

    public ExprCode() {
        mCodeBase = null;
        mStartPos = 0;
        mEndPos = 0;
    }

    public ExprCode(byte[] base, int startPos, int len) {
        mCodeBase = base;
        mStartPos = startPos;
        mEndPos = mStartPos + len;
    }

    public ExprCode(List<Byte> code) {
//        Log.d(TAG, "code:" + code);
        if (null != code) {
            int size = code.size();
            mCodeBase = new byte[size];
            for (int i = 0; i < size; ++i) {
                mCodeBase[i] = code.get(i);
            }

            mStartPos = 0;
            mEndPos = size;
        }
    }

    public ExprCode clone() {
        ExprCode ret = null;
        if (null != mCodeBase) {
            int size = this.size();
            ret = new ExprCode();
            ret.mCodeBase = new byte[size];
            ret.mStartPos = 0;
            ret.mEndPos = size;

            for (int i = 0; i < size; ++i) {
                ret.mCodeBase[i] = mCodeBase[i];
            }
        }

        return ret;
    }

    public int size() {
        return mEndPos - mStartPos;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("start pos:" + mStartPos + "  endPos:" + mEndPos + "  [");
        for (int i = mStartPos; i < mEndPos; ++i) {
            sb.append(mCodeBase[i] + ",");
        }
        sb.append("]");
        return sb.toString();
    }
}
