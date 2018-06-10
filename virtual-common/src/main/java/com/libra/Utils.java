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

package com.libra;

/**
 * Created by gujicheng on 16/8/18.
 */
public class Utils {
    final private static String TAG = "Utils_TMTEST";

    private static int UED_SCREEN = 750;
    private static float sDensity;
    private static int sScreenWidth;

    private static final LruCache<String, Integer> colorCache = new LruCache<>(100);

    public static void init(float density, int screenWidth) {
        sDensity = density;
        sScreenWidth = screenWidth;
    }

    public static void setUedScreenWidth(int uedScreenWidth) {
        UED_SCREEN = uedScreenWidth;
    }

    public static int rp2px(double rpValue) {
        return (int)((rpValue * sScreenWidth) / UED_SCREEN + 0.5f);
    }

    public static int dp2px(double dpValue) {
        final float scale = sDensity < 0 ? 1.0f : sDensity;

        int finalValue;
        if (dpValue >= 0) {
            finalValue = (int) (dpValue * scale + 0.5f);
        } else {
            finalValue = -((int) (-dpValue * scale + 0.5f));
        }
        return finalValue;
    }

    public  static int px2dp(float pxValue) {
        final float scale = sDensity < 0 ? 1.0f : sDensity;
        int result = (int) (pxValue / scale + 0.5f);
        return result;
    }

    public static boolean isSpace(char ch) {
        return ((' ' == ch) || ('\t' == ch) || ('\n' == ch));
    }

    public static boolean isDigit(char ch) {
        return ((ch >= '0') && (ch <= '9'));
    }

    public static boolean isHex(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
    }

    public static boolean isEL(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        int length = value.length();
        if (length <= 3) {
            return false;
        }
        return (value.charAt(0) == '$' && value.charAt(1) == '{' && value.charAt(length - 1) == '}')
                || (value.charAt(0) == '@' && value.charAt(1) == '{' && value.charAt(length - 1) == '}');
    }

    public static boolean isThreeUnknown(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        int length = value.length();
        return (value.charAt(0) == '@' && value.charAt(1) == '{' && value.charAt(length - 1) == '}');
    }

    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if ("true".equalsIgnoreCase(stringValue)) {
                return true;
            } else if ("false".equalsIgnoreCase(stringValue)) {
                return false;
            }
        }
        return null;
    }

    public static Float toFloat(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Double) {
            return ((Double)value).floatValue();
        } else if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            try {
                return Float.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static Double toDouble(Object value) {
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.valueOf((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static Integer toInteger(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return (int) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static Long toLong(Object value) {
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return (long) Double.parseDouble((String) value);
            } catch (NumberFormatException ignored) {
            }
        }
        return null;
    }

    public static String toString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return String.valueOf(value);
        }
        return null;
    }

    public static int parseColor(String value) {
        try {
            Integer integer = colorCache.get(value);
            if (integer != null) {
                return integer.intValue();
            } else {
                integer = Color.parseColor(value);
                colorCache.put(value, integer);
                return integer.intValue();
            }
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }

}
