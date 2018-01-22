
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

package com.libra.expr.compiler.syntax;

import com.libra.expr.common.ExprCode;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {
    private List<Byte> mCode = new ArrayList<Byte>();

    public void reset() {
        mCode.clear();
    }

//    public void release() {
//        init();
//
//        mCode = null;
//    }

    public int size() {
        return mCode.size();
    }

    public void rewriteByte(byte code, int startPos) {
        mCode.set(startPos, code);
    }

    public void rewriteShort(short code, int startPos) {
        for (int i = 0; i < 2; ++i) {
            mCode.set(startPos++, (byte) (code & 0xff));
            code >>= 8;
        }
    }

    public void rewriteInt(int code, int startPos) {
        for (int i = 0; i < 4; ++i) {
            mCode.set(startPos++, (byte) (code & 0xff));
            code >>= 8;
        }
    }

    public void writeByte(byte code) {
        mCode.add(code);
    }

    public void writeShort(short code) {
        for (int i = 0; i < 2; ++i) {
            mCode.add((byte) (code & 0xff));
            code >>= 8;
        }
    }

    public void writeInt(int code) {
        for (int i = 0; i < 4; ++i) {
            mCode.add((byte) (code & 0xff));
            code >>= 8;
        }
    }

    public void writeLong(long code) {
        for (int i = 0; i < 8; ++i) {
            mCode.add((byte) (code & 0xff));
            code >>= 8;
        }
    }

    public ExprCode getCode() {
        return new ExprCode(mCode);
    }

    @Override
    public String toString() {
        return "code:" + mCode;
    }
}
