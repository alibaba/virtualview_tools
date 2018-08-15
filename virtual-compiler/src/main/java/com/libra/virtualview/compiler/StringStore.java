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
import com.libra.TextUtils;
import com.libra.expr.common.StringSupport;
import com.libra.virtualview.common.StringBase;
import com.libra.virtualview.compiler.alert.Assert;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.libra.virtualview.common.Common.MAX_PAGE_ITEM_COUNT;

/**
 * Created by gujicheng on 16/8/3.
 */
public class StringStore extends StringBase implements StringSupport {
    final private static String TAG = "StringStore_TMTEST";

    private Map<String, Integer> mSysString2Index = new LinkedHashMap<>();

    private Map<Integer, String> mSysIndex2Sys = new LinkedHashMap<>();

    private Map<String, Integer> mString2Index = new LinkedHashMap<>();

    private Map<Integer, String> mIndex2String = new LinkedHashMap<>();

    private Map<String, Integer> mSingleOutputString2Index = new LinkedHashMap();

    private Map<Integer, String> mSingleOutputIndex2String = new LinkedHashMap();

    private int mOffset;

    public StringStore() {
        for (int i = 0; i < SYS_KEYS.length; i++) {
            mSysString2Index.put(SYS_KEYS[i], SYS_KEYS[i].hashCode());
            mSysIndex2Sys.put(SYS_KEYS[i].hashCode(), SYS_KEYS[i]);
        }
        Assert.check(mSysString2Index.size() == SYS_KEYS.length,
                "1 system string index's length != system string's length");
        Assert.check(mSysIndex2Sys.size() == SYS_KEYS.length,
                "2 system string index's length != system string's length");
        reset();
    }

    public void resetForFile() {
        mSingleOutputString2Index.clear();
        mSingleOutputIndex2String.clear();
    }

    public void reset() {
        mString2Index.clear();
        mIndex2String.clear();
        mSingleOutputString2Index.clear();
        mSingleOutputIndex2String.clear();
    }

    public void setPageId(int pageId) {
        mOffset = pageId * MAX_PAGE_ITEM_COUNT;
    }

    // offset must be 10000, 20000.....
    public int storeToFile(RandomAccessMemByte file) {
        int totalSize = 0;

        if (null != file) {
            int size = mSingleOutputString2Index.size();
            file.writeInt(size);
            Iterator<String> iterator = mSingleOutputString2Index.keySet().iterator();
            while (iterator.hasNext()) {
                String string = iterator.next();
                int index = mSingleOutputString2Index.get(string);
                byte[] bs = new byte[0];
                try {
                    bs = string.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                file.writeInt(index);
                file.writeShort(bs.length);
                file.write(bs);
                totalSize += 4 + bs.length + 2;
            }
        }

        return totalSize;
    }

    @Override
    public String getString(int id) {
        if (mSysIndex2Sys.containsKey(id)) {
            return mSysIndex2Sys.get(id);
        }
        if (mIndex2String.containsKey(id)) {
            return mIndex2String.get(id);
        }
        Log.e(TAG, "getString null:" + id);
        return null;
    }


    private int find(String str, boolean create) {
        int ret = 0;

        if (!TextUtils.isEmpty(str)) {
            if (mSysString2Index.containsKey(str)) {
                ret = mSysString2Index.get(str);
            }
            if (0 == ret) {
                if (mSingleOutputString2Index.containsKey(str)) {
                    ret = mSingleOutputString2Index.get(str);
                }
            }
            if (0 == ret && create) {
                ret = str.hashCode();
                
                Assert.check(!(mIndex2String.containsKey(ret) && !str.equals(mIndex2String.get(ret))),
                        "hash code conflicts, see string " + str);

                mString2Index.put(str, ret);
                mIndex2String.put(ret, str);
                mSingleOutputString2Index.put(str, ret);
                mSingleOutputIndex2String.put(ret, str);

                Assert.check(!mSysIndex2Sys.containsKey(ret),
                        "string's hash code conflicts with system key");
            }
        }

        return ret;
    }

    public int getStringId(String str) {
        return getStringId(str, true);
    }

    @Override
    public int getStringId(String str, boolean create) {
        return find(str, create);
    }

    @Override
    public boolean isSysString(int id) {
        return mSysIndex2Sys.containsKey(id);
    }

    @Override
    public boolean isSysString(String string) {
        return mSysString2Index.containsKey(string);
    }
}
