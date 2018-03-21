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

package com.libra.virtualview.compiler;

import com.libra.Log;

import com.libra.expr.common.ExprCode;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

import static com.libra.virtualview.common.Common.MAX_PAGE_ITEM_COUNT;

/**
 * Created by gujicheng on 16/9/12.
 */
public class ExprCodeStore {
    private final static String TAG = "CodeStore_TMTEST";

    private Map<ExprCode, Integer> mCodes = new HashMap<>();
    private Map<String, Integer> mBlockMap = new HashMap<>();

    private int mOffset;

    public void setPageId(int pageId) {
        mOffset = pageId * MAX_PAGE_ITEM_COUNT;
    }

    public void reset() {
        mCodes.clear();
    }

    public int addCode(ExprCode code, String blockName) {
        int ret = blockName.hashCode();
        addCode(code, ret);
        mBlockMap.put(blockName, ret);
        return ret;
    }

    public int addCode(ExprCode code, int index) {
        int ret = index;
        if (null != code) {
            mCodes.put(code, index);
        }
        return ret;
    }

    public int getIndex(String blockName) {
        Integer ret = mBlockMap.get(blockName);
        if (null != ret) {
            return ret;
        } else {
            Log.e(TAG, "getIndex get failed blockName:" + blockName);
            return 0;
        }
    }

    public int storeToFile(RandomAccessMemByte file) {
        int totalSize = 0;

        if (null != file) {
            file.writeInt(mCodes.size());
            Iterator<ExprCode> iterator = mCodes.keySet().iterator();
            while (iterator.hasNext()) {
                ExprCode code = iterator.next();
                Integer integer = mCodes.get(code);
                file.writeInt(integer.intValue());
                file.writeShort(code.size());
                for (byte b : code.mCodeBase) {
                    file.writeByte(b);
                }
                totalSize += 4 + code.size() + 2;
            }
        }

        return totalSize;
    }
}
