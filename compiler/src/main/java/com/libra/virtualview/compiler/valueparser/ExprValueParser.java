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
import com.libra.expr.common.ExprCode;
import com.libra.virtualview.compiler.parser.Parser.AttrItem;

public class ExprValueParser extends BaseValueParser {
	
	public boolean parser(AttrItem value) {
		boolean ret = true;

        if (value.mStrValue.startsWith("${") && value.mStrValue.endsWith("}")) {
            if (mExprCompiler.compile(value.mStrValue)) {
                ExprCode code = mExprCompiler.getCode();
                if (null != code) {
                    int codeIndex = value.mStrValue.hashCode();
                    mExprCodeStore.addCode(code, codeIndex);
                    value.setCode(codeIndex);
                } else {
                    ret = false;
                    Log.e(TAG, "compile result code is null:" + value.mStrValue);
                }
            } else {
                ret = false;
                Log.e(TAG, "compile expr failed:" + value.mStrValue);
            }
        } else {
            int codeIndex = mExprCodeStore.getIndex(value.mStrValue.trim());
            if (codeIndex != 0) {
                value.setCode(codeIndex);
            } else {
                ret = false;
                Log.e(TAG, "onClick is not expr:" + value.mStrValue);
            }
        }

        return ret;
	}
}
