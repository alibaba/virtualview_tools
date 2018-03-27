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

package com.libra.virtualview.common;

/**
 * Created by longerian on 2017/5/20.
 */
@Deprecated
public class BizCommon {

    public static final int SCROLL_VIEW = Common.USER_VIEW_ID_START + 1;

    public static final int TIPS_VIEW = SCROLL_VIEW + 1;

    public static final int TM_RECOMMEND_TEXTVIEW = TIPS_VIEW + 1;

    public static final int TM_RECOMMEND_BENEFITVIEW = TM_RECOMMEND_TEXTVIEW + 1;

    public static final int TM_COMMODITY_UPGRADE_SCENCE_VIEW = TM_RECOMMEND_BENEFITVIEW + 1;

    public static final int TM_COMMODITY_UPGRADE_3D_MODEL_VIEW = TM_COMMODITY_UPGRADE_SCENCE_VIEW + 1;

    public static final int TM_COMMODITY_UPGRADE_SHOW_WINDOW_VIEW = TM_COMMODITY_UPGRADE_3D_MODEL_VIEW + 1;

    public static final int TM_620_RECOMMEND_BENEFITVIEW = TM_COMMODITY_UPGRADE_SHOW_WINDOW_VIEW + 1;

    public static final int TM_PRICE_TEXTVIEW = TM_620_RECOMMEND_BENEFITVIEW + 1;

    public static final int TM_TOTAL_CONTAINER = TM_PRICE_TEXTVIEW + 1;

    public static final int TM_RECOMMEND_MAGIC_WAND = TM_TOTAL_CONTAINER + 1;

    public static final int TM_COUNTDOWN_VIEW = TM_RECOMMEND_MAGIC_WAND + 1;

    public static final int TM_830_TAB_HEADER = TM_COUNTDOWN_VIEW + 1;

}
