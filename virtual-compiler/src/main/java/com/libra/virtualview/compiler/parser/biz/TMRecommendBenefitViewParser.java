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

import com.libra.Log;
import com.libra.TextUtils;
import com.libra.virtualview.common.BizCommon;
import com.libra.virtualview.compiler.parser.Parser;
import com.libra.virtualview.compiler.parser.ViewBaseParser;

/**
 * Created by jiangbin.jiangb on 2017/5/9.
 */

@Deprecated
public class TMRecommendBenefitViewParser extends ViewBaseParser {

    private final static String TAG = "TMTipsViewParser_TMTEST";

    private int benefitTitleId;
    private int benefitColorId;
    private int benefitImgUrlId;
    private int benefitLogoUrlId;


    @Override
    public void init() {
        super.init();
        benefitTitleId = mStringSupport.getStringId("benefitTitle", true);
        benefitColorId = mStringSupport.getStringId("benefitColor", true);
        benefitImgUrlId = mStringSupport.getStringId("benefitImgUrl", true);
        benefitLogoUrlId = mStringSupport.getStringId("benefitLogoUrl", true);

    }

    @Override
    public int convertAttribute(int key, AttrItem value) {
        int ret = super.convertAttribute(key, value);

        if (ViewBaseParser.CONVERT_RESULT_FAILED == ret) {
            ret = ViewBaseParser.CONVERT_RESULT_OK;

            if (key == benefitTitleId || key == benefitColorId || key == benefitImgUrlId || key == benefitLogoUrlId) {
                if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
                    value.setStr(value.mStrValue);
                } else {
                    Log.e(TAG, "parse value invalidate:" + value);
                    ret = CONVERT_RESULT_ERROR;
                }
            } else {
                ret = CONVERT_RESULT_FAILED;
            }
        }

        return ret;
    }

    public static class Builder implements IBuilder {
        @Override
        public Parser build(String name) {
            if (TextUtils.equals(name, "TMRecommendBenefit")) {
                return new TMRecommendBenefitViewParser();
            }
            return null;
        }
    }

    @Override
    public int getId() {
        return BizCommon.TM_RECOMMEND_BENEFITVIEW;
    }


}