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

package com.libra.virtualview.compiler.valueparser;

import com.libra.Color;
import com.libra.Log;
import com.libra.TextUtils;
import com.libra.Utils;
import com.libra.virtualview.compiler.parser.Parser;

public class ColorValueParser extends BaseValueParser {
	public boolean parser(Parser.AttrItem item){
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
                } else if (TextUtils.equals("white", str)) {
                    value = Color.WHITE;
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
}
