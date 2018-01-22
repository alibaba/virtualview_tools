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
import com.libra.virtualview.common.ViewBaseCommon;
import com.libra.virtualview.compiler.parser.Parser.AttrItem;

public class FlagValueParser extends BaseValueParser {

	@Override
	public boolean parser(AttrItem item) {
		boolean ret = true;

		int value = 0;
		String[] strArr = item.mStrValue.split("\\|");
		for (String str : strArr) {
			str = str.trim();
			if (TextUtils.equals("flag_draw", str)) {
				value |= ViewBaseCommon.FLAG_DRAW;
			} else if (TextUtils.equals("flag_event", str)) {
				value |= ViewBaseCommon.FLAG_EVENT;
			} else if (TextUtils.equals("flag_dynamic", str)) {
				value |= ViewBaseCommon.FLAG_DYNAMIC;
			} else if (TextUtils.equals("flag_software", str)) {
				value |= ViewBaseCommon.FLAG_SOFTWARE;
			} else if (TextUtils.equals("flag_exposure", str)) {
				value |= ViewBaseCommon.FLAG_EXPOSURE;
			} else if (TextUtils.equals("flag_clickable", str)) {
				value |= ViewBaseCommon.FLAG_CLICKABLE;
			} else if (TextUtils.equals("flag_longclickable", str)) {
				value |= ViewBaseCommon.FLAG_LONG_CLICKABLE;
			} else if (TextUtils.equals("flag_touchable", str)) {
				value |= ViewBaseCommon.FLAG_TOUCHABLE;
			} else {
				Log.e(TAG, "invalidate value:" + str);
				ret = false;
				break;
			}
		}

		if (ret) {
			item.setIntValue(value);
		}

		return ret;

	}
}
