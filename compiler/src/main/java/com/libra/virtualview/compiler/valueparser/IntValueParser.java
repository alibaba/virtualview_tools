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

import com.libra.Log;
import com.libra.TextUtils;
import com.libra.Utils;
import com.libra.virtualview.compiler.parser.Parser.AttrItem;

public class IntValueParser extends BaseValueParser {

	@Override
	public boolean parser(AttrItem value) {
		boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            String str = value.mStrValue.trim();
            try {
                // int
                if (str.equals("true")) {
                    value.setIntValue(1);
                } else if (str.equals("false")) {
                    value.setIntValue(0);
                } else {
                    if (str.endsWith(RP)) {
                        str = str.substring(0, str.length() - 2);
                        value.mExtra = AttrItem.EXTRA_RP;
                    }

                    value.setIntValue(Integer.parseInt(str));
                }
                ret = true;
            } catch (NumberFormatException e) {
                if (Utils.isEL(str)) {
                    value.setStr(str);
                    ret = true;
                } else {
                    Log.e(TAG, "parseInteger error:" + e);
                }
            }
        } else {
            Log.e(TAG, "parseInteger value invalidate:" + value);
        }
        return ret;
	}

}
