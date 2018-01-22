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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 16/8/3.
 */
public class CodeWriter {
    private List<Byte> mCode = new ArrayList<>();

    public void init() {
        mCode.clear();
    }

    public void release() {
        init();

        mCode = null;
    }

    public void writeByte(byte code) {
        mCode.add(code);
    }

    public void writeShort(short code) {
        mCode.add((byte) ((code & 0xff00) >> 8));
        mCode.add((byte) (code & 0xff));
    }

    public void writeInt(int code) {
        mCode.add((byte) ((code & 0xff000000) >> 24));
        mCode.add((byte) ((code & 0xff0000) >> 16));
        mCode.add((byte) ((code & 0xff00) >> 8));
        mCode.add((byte) (code & 0xff));
    }

    public List<Byte> getCode() {
        return mCode;
    }
}
