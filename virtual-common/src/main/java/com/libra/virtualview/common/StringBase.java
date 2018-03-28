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
 * Created by gujicheng on 16/11/8.
 */

public class StringBase {
    final public static String[] SYS_KEYS = {
            "VHLayout", "GridLayout", "FrameLayout", "NText", "VText", "NImage", "VImage", "TMNImage", "List", "Component",
            "id", "layoutWidth", "layoutHeight", "paddingLeft", "paddingRight", "paddingTop", "paddingBottom", "layoutMarginLeft", "layoutMarginRight", "layoutMarginTop",
            "layoutMarginBottom", "orientation", "text", "src", "name", "pos", "type", "gravity", "background", "color",
            "size", "layoutGravity", "colCount", "itemHeight", "flag", "data", "dataTag", "style", "action", "actionParam",
            "scaleType", "VLine", "textStyle", "FlexLayout", "flexDirection", "flexWrap", "flexFlow", "justifyContent", "alignItems", "alignContent",
            "alignSelf", "order", "flexGrow", "flexShrink", "flexBasis", "typeface", "Scroller", "minWidth", "minHeight", "TMVImage",
            "class", "onClick", "onLongClick", "self", "textColor", "textSize", "dataUrl", "this", "parent", "ancestor",
            "siblings", "module", "RatioLayout", "layoutRatio", "layoutDirection", "VH2Layout", "onAutoRefresh", "initValue", "uuid", "onBeforeDataLoad",
            "onAfterDataLoad", "Page", "onPageFlip", "autoSwitch", "canSlide", "stayTime", "animatorTime", "autoSwitchTime", "animatorStyle", "layoutOrientation", "Grid", "paintWidth",
            "itemHorizontalMargin", "itemVerticalMargin", "NLine", "visibility", "mode", "supportSticky", "VGraph", "diameterX", "diameterY", "itemWidth",
            "itemMargin", "VH", "onSetData", "children", "lines", "ellipsize", "autoDimDirection", "autoDimX", "autoDimY", "VTime",
            "containerID", "if", "elseif", "for", "while", "do", "else", "Slider", "Progress", "onScroll",
            "backgroundImage", "Container", "span", "paintStyle", "var", "vList", "dataParam", "autoRefreshThreshold", "dataMode", "waterfall",
            "supportHTMLStyle", "lineSpaceMultiplier", "lineSpaceExtra", "borderWidth", "borderColor", "maxLines", "dashEffect", "lineSpace", "firstSpace", "lastSpace",
            "maskColor", "blurRadius", "filterWhiteBg", "ratio", "disablePlaceHolder", "disableCache", "fixBy", "alpha", "ck","borderRadius",
            "borderTopLeftRadius", "borderTopRightRadius", "borderBottomLeftRadius", "borderBottomRightRadius", "tag", "lineHeight", "padding", "layoutMargin"
    };

    public static final int[] SYS_KEYS_INDEX = {1302701180, -1822277072, 1310765783, 74637979, 82026147, -1991132755,
            -1762099547, -2005645978, 2368702, 604060893, 3355, 2003872956, 1557524721, -1501175880, 713848971, 90130308,
            202355100, 1248755103, 62363524, -2037919555, 1481142723, -1439500848, 3556653, 114148, 3373707, 111188,
            3575610, 280523342, -1332194002, 94842723, 3530753, 516361156, -669528209, 1671241242, 3145580, 3076010,
            1443184528, 109780401, -1422950858, 1569332215, -1877911644, 81791338, -1048634236, -1477040989, -975171706,
            1744216035, 1743704263, 1860657097, -1063257157, -752601676, 1767100401, 106006350, 1743739820, 1031115618,
            -1783760955, -675792745, -337520550, -1375815020, -133587431, -1776612770, 94742904, -1351902487, 71235917,
            3526476, -1063571914, -1003668786, 1443186021, 3559070, -995424086, -973829677, 166965745, -1068784020,
            -2105120011, 1999032065, -1955718283, -494312694, 173466317, -266541503, 3601339, 361078798, -251005427,
            2479791, -665970021, -380157501, -137744447, 1322318022, 1347692116, 78802736, -1171801334, 1942742086, 2228070,
            793104392, 2129234981, 196203191, 74403170, 1941332754, 3357091, -977844584, -1763797352, 1360592235,
            1360592236, 2146088563, 1810961057, 2738, -974184371, 1659526655, 102977279, 1554823821, -1422893274,
            1438248735, 1438248736, 82029635, 207632732, 3357, -1300156394, 101577, 113101617, 3211, 3116345, -1815780095,
            -936434099, 1490730380, 1292595405, 1593011297, 3536714, 789757939, 116519, 111344180, -377785597, -51356769,
            1788852333, -213632750, 506010071, -667362093, -1118334530, 741115130, 722830999, 390232059, 1037639619,
            -1807275662, -172008394, 2002099216, -77812777, -1428201511, 617472950, 108285963, -1358064245, -1012322950,
            97444684, 92909918, 3176, 1349188574, -1228066334, 333432965, 581268560, 588239831, 114586, -515807685, -806339567, 1697244536};


    final public static int STR_ID_SYS_KEY_COUNT = SYS_KEYS.length;

    //use hashCode of string as index
    final public static int STR_ID_VHLayout = 1302701180;
    final public static int STR_ID_GridLayout = -1822277072;
    final public static int STR_ID_FrameLayout = 1310765783;
    final public static int STR_ID_NText = 74637979;
    final public static int STR_ID_VText = 82026147;
    final public static int STR_ID_NImage = -1991132755;
    final public static int STR_ID_VImage = -1762099547;
    final public static int STR_ID_TMNImage = -2005645978;
    final public static int STR_ID_List = 2368702;
    final public static int STR_ID_Component = 604060893;
    final public static int STR_ID_id = 3355;
    final public static int STR_ID_layoutWidth = 2003872956;
    final public static int STR_ID_layoutHeight = 1557524721;
    final public static int STR_ID_paddingLeft = -1501175880;
    final public static int STR_ID_paddingRight = 713848971;
    final public static int STR_ID_paddingTop = 90130308;
    final public static int STR_ID_paddingBottom = 202355100;
    final public static int STR_ID_layoutMarginLeft = 1248755103;
    final public static int STR_ID_layoutMarginRight = 62363524;
    final public static int STR_ID_layoutMarginTop = -2037919555;
    final public static int STR_ID_layoutMarginBottom = 1481142723;
    final public static int STR_ID_orientation = -1439500848;
    final public static int STR_ID_text = 3556653;
    final public static int STR_ID_src = 114148;
    final public static int STR_ID_name = 3373707;
    final public static int STR_ID_pos = 111188;
    final public static int STR_ID_type = 3575610;
    final public static int STR_ID_gravity = 280523342;
    final public static int STR_ID_background = -1332194002;
    final public static int STR_ID_color = 94842723;
    final public static int STR_ID_size = 3530753;
    final public static int STR_ID_layoutGravity = 516361156;
    final public static int STR_ID_colCount = -669528209;
    final public static int STR_ID_itemHeight = 1671241242;
    final public static int STR_ID_flag = 3145580;
    final public static int STR_ID_data = 3076010;
    final public static int STR_ID_dataTag = 1443184528;
    final public static int STR_ID_style = 109780401;
    final public static int STR_ID_action = -1422950858;
    final public static int STR_ID_actionParam = 1569332215;
    final public static int STR_ID_scaleType = -1877911644;
    final public static int STR_ID_VLine = 81791338;
    final public static int STR_ID_textStyle = -1048634236;
    final public static int STR_ID_FlexLayout = -1477040989;
    final public static int STR_ID_flexDirection = -975171706;
    final public static int STR_ID_flexWrap = 1744216035;
    final public static int STR_ID_flexFlow = 1743704263;
    final public static int STR_ID_justifyContent = 1860657097;
    final public static int STR_ID_alignItems = -1063257157;
    final public static int STR_ID_alignContent = -752601676;
    final public static int STR_ID_alignSelf = 1767100401;
    final public static int STR_ID_order = 106006350;
    final public static int STR_ID_flexGrow = 1743739820;
    final public static int STR_ID_flexShrink = 1031115618;
    final public static int STR_ID_flexBasis = -1783760955;
    final public static int STR_ID_typeface = -675792745;
    final public static int STR_ID_Scroller = -337520550;
    final public static int STR_ID_minWidth = -1375815020;
    final public static int STR_ID_minHeight = -133587431;
    final public static int STR_ID_TMVImage = -1776612770;
    final public static int STR_ID_class = 94742904;
    final public static int STR_ID_onClick = -1351902487;
    final public static int STR_ID_onLongClick = 71235917;
    final public static int STR_ID_self = 3526476;
    final public static int STR_ID_textColor = -1063571914;
    final public static int STR_ID_textSize = -1003668786;
    final public static int STR_ID_dataUrl = 1443186021;
    final public static int STR_ID_this = 3559070;
    final public static int STR_ID_parent = -995424086;
    final public static int STR_ID_ancestor = -973829677;
    final public static int STR_ID_siblings = 166965745;
    final public static int STR_ID_module = -1068784020;
    final public static int STR_ID_RatioLayout = -2105120011;
    final public static int STR_ID_layoutRatio = 1999032065;
    final public static int STR_ID_layoutDirection = -1955718283;
    final public static int STR_ID_VH2Layout = -494312694;
    final public static int STR_ID_onAutoRefresh = 173466317;
    final public static int STR_ID_initValue = -266541503;
    final public static int STR_ID_uuid = 3601339;
    final public static int STR_ID_onBeforeDataLoad = 361078798;
    final public static int STR_ID_onAfterDataLoad = -251005427;
    final public static int STR_ID_Page = 2479791;
    final public static int STR_ID_onPageFlip = -665970021;
    final public static int STR_ID_autoSwitch = -380157501;
    final public static int STR_ID_canSlide = -137744447;
    final public static int STR_ID_stayTime = 1322318022;
    final public static int STR_ID_animatorTime = 1347692116;
    final public static int STR_ID_autoSwitchTime = 78802736;
    final public static int STR_ID_animatorStyle = -1171801334;
    final public static int STR_ID_layoutOrientation = 1942742086;
    final public static int STR_ID_Grid = 2228070;
    final public static int STR_ID_paintWidth = 793104392;
    final public static int STR_ID_itemHorizontalMargin = 2129234981;
    final public static int STR_ID_itemVerticalMargin = 196203191;
    final public static int STR_ID_NLine = 74403170;
    final public static int STR_ID_visibility = 1941332754;
    final public static int STR_ID_mode = 3357091;
    final public static int STR_ID_supportSticky = -977844584;
    final public static int STR_ID_VGraph = -1763797352;
    final public static int STR_ID_diameterX = 1360592235;
    final public static int STR_ID_diameterY = 1360592236;
    final public static int STR_ID_itemWidth = 2146088563;
    final public static int STR_ID_itemMargin = 1810961057;
    final public static int STR_ID_VH = 2738;
    final public static int STR_ID_onSetData = -974184371;
    final public static int STR_ID_children = 1659526655;
    final public static int STR_ID_lines = 102977279;
    final public static int STR_ID_ellipsize = 1554823821;
    final public static int STR_ID_autoDimDirection = -1422893274;
    final public static int STR_ID_autoDimX = 1438248735;
    final public static int STR_ID_autoDimY = 1438248736;
    final public static int STR_ID_VTime = 82029635;
    final public static int STR_ID_containerID = 207632732;
    final public static int STR_ID_if = 3357;
    final public static int STR_ID_elseif = -1300156394;
    final public static int STR_ID_for = 101577;
    final public static int STR_ID_while = 113101617;
    final public static int STR_ID_do = 3211;
    final public static int STR_ID_else = 3116345;
    final public static int STR_ID_Slider = -1815780095;
    final public static int STR_ID_Progress = -936434099;
    final public static int STR_ID_onScroll = 1490730380;
    final public static int STR_ID_backgroundImage = 1292595405;
    final public static int STR_ID_Container = 1593011297;
    final public static int STR_ID_span = 3536714;
    final public static int STR_ID_paintStyle = 789757939;
    final public static int STR_ID_var = 116519;
    final public static int STR_ID_vList = 111344180;
    final public static int STR_ID_dataParam = -377785597;
    final public static int STR_ID_autoRefreshThreshold = -51356769;
    final public static int STR_ID_dataMode = 1788852333;
    final public static int STR_ID_waterfall = -213632750;
    final public static int STR_ID_supportHTMLStyle = 506010071;
    final public static int STR_ID_lineSpaceMultiplier = -667362093;
    final public static int STR_ID_lineSpaceExtra = -1118334530;
    final public static int STR_ID_borderWidth = 741115130;
    final public static int STR_ID_borderColor = 722830999;
    final public static int STR_ID_maxLines = 390232059;
    final public static int STR_ID_dashEffect = 1037639619;
    final public static int STR_ID_lineSpace = -1807275662;
    final public static int STR_ID_firstSpace = -172008394;
    final public static int STR_ID_lastSpace = 2002099216;
    final public static int STR_ID_maskColor = -77812777;
    final public static int STR_ID_blurRadius = -1428201511;
    final public static int STR_ID_filterWhiteBg = 617472950;
    final public static int STR_ID_ratio = 108285963;
    final public static int STR_ID_disablePlaceHolder = -1358064245;
    final public static int STR_ID_disableCache = -1012322950;
    final public static int STR_ID_fixBy = 97444684;
    final public static int STR_ID_alpha = 92909918;
    final public static int STR_ID_ck = 3176;
    final public static int STR_ID_borderRadius = 1349188574;
    final public static int STR_ID_borderTopLeftRadius = -1228066334;
    final public static int STR_ID_borderTopRightRadius = 333432965;
    final public static int STR_ID_borderBottomLeftRadius = 581268560;
    final public static int STR_ID_borderBottomRightRadius = 588239831;
    final public static int STR_ID_tag = 114586;
    final public static int STR_ID_lineHeight = -515807685;
    final public static int STR_ID_padding = -806339567;
    final public static int STR_ID_layoutMargin = 1697244536;
}
