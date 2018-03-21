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

package com.libra.virtualview.compiler.parser;

import com.libra.TextUtils;
import com.libra.Log;

import com.libra.Utils;
import com.libra.expr.common.StringSupport;
import com.libra.expr.compiler.ExprCompiler;
import com.libra.virtualview.common.ViewBaseCommon;
import com.libra.virtualview.compiler.ExprCodeStore;

/**
 * Created by gujicheng on 16/10/13.
 */
public abstract class  Parser {
    private final static String TAG = "Parser_TMTEST";

    public final static int CONVERT_RESULT_ERROR = -1;
    public final static int CONVERT_RESULT_FAILED = 0;
    public final static int CONVERT_RESULT_OK = 1;

    protected ExprCompiler mExprCompiler;
    protected ExprCodeStore mExprCodeStore;
    protected StringSupport mStringSupport;

    protected final static String RP = "rp";

    // view type id
    public abstract int getId();

    public final void setExprCompiler(ExprCompiler exprCompiler) {
        mExprCompiler = exprCompiler;
    }

    public final void setStringSupport(StringSupport ss) {
        mStringSupport = ss;
    }

    public final void setExprCodeManager(ExprCodeStore exprCodeStore) {
        mExprCodeStore = exprCodeStore;
    }

    public void init() {
    }

    public static class AttrItem {
        public final static int TYPE_int = 0;
        public final static int TYPE_float = 1;
        public final static int TYPE_code = 2;
        public final static int TYPE_string = 3;

        public final static int EXTRA_RP = 1;

        public int mExtra;
        public String mStrValue;
        private int mIntValue;
        public float mFloatValue;

        
        
        public int getmIntValue() {
			return mIntValue;
		}

		public int mType;

        public AttrItem() {
            reset();
        }

        public void reset() {
            mType = TYPE_int;
            mExtra = 0;
            mStrValue = null;
            mIntValue = 0;
            mFloatValue = 0;
        }

        @Override
        public String toString() {
            switch (mType) {
                case TYPE_int:
                    return String.format("strValue:%s, v:%d, extra:%d", mStrValue, mIntValue, mExtra);

                case TYPE_float:
                    return String.format("strValue:%s, v:%f, extra:%d", mStrValue, mFloatValue, mExtra);

                case TYPE_code:
                    return String.format("strValue:%s, v:%s, extra:%d", mStrValue, mStrValue, mExtra);

                case TYPE_string:
                    return String.format("strValue:%s, v:%s, extra:%d", mStrValue, mStrValue, mExtra);
            }

            return "";
        }

        public void setCode(int c) {
            mType = TYPE_code;
            mIntValue = c;
        }

        public void setStr(String str) {
            reset();
            mType = TYPE_string;
            mStrValue = str;
        }

        public void setIntValue(int v) {
            mType = TYPE_int;
            mIntValue = v;
        }

        public void setFloatValue(float f) {
            mType = TYPE_float;
            mFloatValue = f;
        }
    }

    public boolean supportNameSpace(String nameSpace){
    	return false;
    }

    public  int convertAttribute(int nameSpaeKey,int key, AttrItem value){
    	return -1;
    }
    
    public abstract int convertAttribute(int key, AttrItem value);

    // only parse integer
    protected static boolean parseInteger(AttrItem value) {
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

    // only parse float
    protected static boolean parseFloat(AttrItem value) {
        boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            String str = value.mStrValue.trim();
            if (str.endsWith(RP)) {
                str = str.substring(0, str.length() - 2);
                value.mExtra = AttrItem.EXTRA_RP;
            }

            try {
                // float
                value.setFloatValue(Float.parseFloat(str));
                ret = true;
            } catch (NumberFormatException e) {
                if (Utils.isEL(str)) {
                    value.setStr(str);
                    ret = true;
                } else {
                    Log.e(TAG, "parseFloat error:" + e);
                }
            }
        } else {
            Log.e(TAG, "parseFloat value invalidate:" + value);
        }
        return ret;
    }

    protected static boolean parseVH(AttrItem value) {
        boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            ret = true;
            if (TextUtils.equals(value.mStrValue, "H")) {
                value.setIntValue(ViewBaseCommon.HORIZONTAL);
            } else if (TextUtils.equals(value.mStrValue, "V")) {
                value.setIntValue(ViewBaseCommon.VERTICAL);
            } else {
                ret = false;
                Log.e(TAG, "orientation value error:" + value);
            }
        } else {
            Log.e(TAG, "parseNumber value invalidate:" + value);
        }
        return ret;
    }

    protected static boolean parseLayoutOrientation(AttrItem value) {
        boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            ret = true;
            if (TextUtils.equals(value.mStrValue, "normal")) {
                value.setIntValue(ViewBaseCommon.NORMAL);
            } else if (TextUtils.equals(value.mStrValue, "reverse")) {
                value.setIntValue(ViewBaseCommon.REVERSE);
            } else {
                ret = false;
                Log.e(TAG, "layout orientation value error:" + value);
            }
        } else {
            Log.e(TAG, "parseNumber value invalidate:" + value);
        }
        return ret;
    }

    // parse integer and float
    protected static boolean parseNumber(AttrItem value) {
        boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            String str = value.mStrValue.trim();
            ret = true;
            if (str.equals("true")) {
                value.setIntValue(1);
            } else if (str.equals("false")) {
                value.setIntValue(0);
            } else {
                if (str.endsWith(RP)) {
                    str = str.substring(0, str.length() - 2);
                    value.mExtra = AttrItem.EXTRA_RP;
                }
                try {
                    if (str.indexOf('.') > 0) {
                        // float
                        value.setFloatValue(Float.parseFloat(str));
                    } else {
                        // int
                        value.setIntValue(Integer.parseInt(str));
                    }
                } catch (NumberFormatException e) {
                    if (Utils.isEL(str)) {
                        value.setStr(str);
                        ret = true;
                    } else {
                        Log.e(TAG, "parseNumber error:" + e);
                        ret = false;
                    }
                }
            }
        } else {
            Log.e(TAG, "parseNumber value invalidate:" + value);
        }
        return ret;
    }

    protected static boolean parseAnimationStyle(AttrItem value) {
        boolean ret = false;
        if (null != value && !TextUtils.isEmpty(value.mStrValue)) {
            ret = true;
            if (TextUtils.equals(value.mStrValue, "linear")) {
                value.setIntValue(ViewBaseCommon.ANIMATION_LINEAR);
            } else if (TextUtils.equals(value.mStrValue, "decelerate")) {
                value.setIntValue(ViewBaseCommon.ANIMATION_DECELERATE);
            } else if (TextUtils.equals(value.mStrValue, "accelerate")) {
                value.setIntValue(ViewBaseCommon.ANIMATION_ACCELERATE);
            } else if (TextUtils.equals(value.mStrValue, "accelerateDecelerate")) {
                value.setIntValue(ViewBaseCommon.ANIMATION_ACCELERATEDECELERATE);
            } else if (TextUtils.equals(value.mStrValue, "spring")) {
                value.setIntValue(ViewBaseCommon.ANIMATION_SPRING);
            } else {
                ret = false;
                Log.e(TAG, "animation style value error:" + value);
            }
        } else {
            Log.e(TAG, "parseNumber value invalidate:" + value);
        }
        return ret;
    }

    public interface IBuilder {
        Parser build(String name);
    }
}
