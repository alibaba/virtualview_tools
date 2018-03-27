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

package com.libra.virtualview.compiler.parser.biz;

import com.libra.TextUtils;
import com.libra.virtualview.common.BizCommon;
import com.libra.virtualview.compiler.parser.Parser;
import com.libra.virtualview.compiler.parser.ViewBaseParser;

/**
 * Created by jiangbin.jiangb on 2017/5/9.
 */

@Deprecated
public class TM620RecommendBenefitViewParser extends ViewBaseParser {

    private final static String TAG = "TMTipsViewParser_TMTEST";

    public static class Builder implements IBuilder {
        @Override
        public Parser build(String name) {
            if (TextUtils.equals(name, "TM620RecommendBenefit")) {
                return new TM620RecommendBenefitViewParser();
            }
            return null;
        }
    }

    @Override
    public int getId() {
        return BizCommon.TM_620_RECOMMEND_BENEFITVIEW;
    }


}