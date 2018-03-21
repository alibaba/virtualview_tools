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
import com.libra.virtualview.common.ViewBaseCommon;
import com.libra.virtualview.compiler.parser.Parser.AttrItem;

public class AlignValueParser extends BaseValueParser {

	public boolean parser(AttrItem item) {
		boolean ret = true;
		if (Utils.isEL(item.mStrValue)) {
			item.setStr(item.mStrValue);
		} else {
			int value = 0;
			String[] strArr = item.mStrValue.split("\\|");
			for (String str : strArr) {
				str = str.trim();
				if (TextUtils.equals("left", str)) {
					value |= ViewBaseCommon.LEFT;
				} else if (TextUtils.equals("right", str)) {
					value |= ViewBaseCommon.RIGHT;
				} else if (TextUtils.equals("h_center", str)) {
					value |= ViewBaseCommon.H_CENTER;
				} else if (TextUtils.equals("top", str)) {
					value |= ViewBaseCommon.TOP;
				} else if (TextUtils.equals("bottom", str)) {
					value |= ViewBaseCommon.BOTTOM;
				} else if (TextUtils.equals("v_center", str)) {
					value |= ViewBaseCommon.V_CENTER;
				} else {
					Log.e(TAG, "invalidate value:" + str);
					ret = false;
					break;
				}
			}

			if (ret) {
				item.setIntValue(value);
			}

		}

		return ret;
	}
}
