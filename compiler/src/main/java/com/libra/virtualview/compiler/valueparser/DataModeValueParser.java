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

import com.libra.TextUtils;
import com.libra.virtualview.common.IDataLoaderCommon;
import com.libra.virtualview.compiler.parser.Parser.AttrItem;

public class DataModeValueParser extends NumberValueParser {
	public boolean parser(AttrItem value) {
		boolean ret = true;

        if (TextUtils.equals(value.mStrValue, "set")) {
            value.setIntValue(IDataLoaderCommon.MODE_SET);
        } else if (TextUtils.equals(value.mStrValue, "append")) {
            value.setIntValue(IDataLoaderCommon.MODE_APPEND);
        } else {
            ret = super.parser(value);
            if (ret && (value.getmIntValue() != IDataLoaderCommon.MODE_SET && value.getmIntValue() != IDataLoaderCommon.MODE_APPEND)) {
                ret = false;
            }
        }

        return ret;
	}
}
