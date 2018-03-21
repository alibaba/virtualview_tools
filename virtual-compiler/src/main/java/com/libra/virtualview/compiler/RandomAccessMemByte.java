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

import java.util.Arrays;

/**
 * Created by longerian on 2018/1/22.
 */
public class RandomAccessMemByte {

    private byte[] mCode = new byte[65536]; //64KB

    private int index;

    private int size;

    public void seek(int pos) {
        index = pos;
    }

    public int length() {
        return size;
    }

    public void write(byte b) {
        if (index >= mCode.length) {
            byte[] current = mCode;
            mCode = new byte[2 * mCode.length];
            System.arraycopy(current, 0, mCode, 0, current.length);

        }
        mCode[index] = b;
        index++;
        if (index > size) {
            size++;
        }
    }

    public void write(byte b[]) {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) {
        for (int i = off; i < len; i++) {
            write(b[i]);
        }
    }

    public final void writeBoolean(boolean v) {
        write((byte) (v ? 1 : 0));
    }

    public final void writeByte(int v) {
        write((byte) v);
    }

    public final void writeShort(int v) {
        write((byte) ((v >>> 8) & 0xFF));
        write((byte) ((v >>> 0) & 0xFF));
    }

    public final void writeChar(int v) {
        write((byte) ((v >>> 8) & 0xFF));
        write((byte) ((v >>> 0) & 0xFF));
    }

    public final void writeInt(int v) {
        write((byte) ((v >>> 24) & 0xFF));
        write((byte) ((v >>> 16) & 0xFF));
        write((byte) ((v >>>  8) & 0xFF));
        write((byte) ((v >>>  0) & 0xFF));
    }

    public final void writeLong(long v) {
        write((byte) ((int)(v >>> 56) & 0xFF));
        write((byte) ((int)(v >>> 48) & 0xFF));
        write((byte) ((int)(v >>> 40) & 0xFF));
        write((byte) ((int)(v >>> 32) & 0xFF));
        write((byte) ((int)(v >>> 24) & 0xFF));
        write((byte) ((int)(v >>> 16) & 0xFF));
        write((byte) ((int)(v >>>  8) & 0xFF));
        write((byte) ((int)(v >>>  0) & 0xFF));
    }

    public final void writeFloat(float v) {
        writeInt(Float.floatToIntBits(v));
    }

    public final void writeDouble(double v) {
        writeLong(Double.doubleToLongBits(v));
    }

    public byte[] getByte() {
        byte[] content = new byte[size];
        System.arraycopy(mCode, 0, content, 0, size);
        return content;
    }

    public void close() {
        Arrays.fill(mCode, (byte) 0);
        index = 0;
    }

}
