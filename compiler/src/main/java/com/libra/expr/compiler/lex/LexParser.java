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

package com.libra.expr.compiler.lex;

import com.libra.TextUtils;
import com.libra.Log;

import com.libra.Utils;
import com.libra.expr.common.StringSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gujicheng on 16/9/8.
 */
public class LexParser {
    private final static String TAG = "LexParser_TMTEST";

    public interface Listener {
        void onLexParseStart();

        boolean onLexParseToken(Token token);

        boolean onLexParseEnd(boolean result);
    }

    private Listener mListener;
    private List<Parser> mLexers = new ArrayList<Parser>();

    public LexParser() {
        mLexers.add(new NumberParser());
        mLexers.add(new StringParser());
        mLexers.add(new SymbolParser());
        mLexers.add(new WordParser());
    }

    public void setStringStore(StringSupport sm) {
        for(Parser p : mLexers) {
            p.setStringManager(sm);
        }
    }

    public void reset() {
        for(Parser p : mLexers) {
            p.reset();
        }
    }

    public void setListener(Listener lis) {
        mListener = lis;
    }

    public boolean parse(String expr) {
        boolean ret = false;

        if (!TextUtils.isEmpty(expr)) {
            if (null != mListener) {
                mListener.onLexParseStart();

                int len = expr.length();
                Parser curLexer = null;
                int result = Parser.RESULT_FAILED;
                ret = true;
                for (int i = 0; i < len; ++i) {
                    char ch = expr.charAt(i);

                    if (null == curLexer) {
                        if (!Utils.isSpace(ch)) {
                            for (Parser lexer : mLexers) {
                                result = lexer.addChar(ch);
                                if (Parser.RESULT_FAILED != result) {
                                    curLexer = lexer;
                                    break;
                                }
                            }
                        } else {
                            continue;
                        }
                    } else {
                        result = curLexer.addChar(ch);
                    }

                    if (null != curLexer) {
                        switch (result) {
                            case Parser.RESULT_FAILED:
                                ret = false;
                                Log.e(TAG, "failed str:" + expr + "  char:" + ch);
                                break;

                            case Parser.RESULT_FINISHED_REMAIN:
                                if (!Utils.isSpace(ch)) {
                                    --i;
                                }
                            case Parser.RESULT_FINISHED:
                                Token token = curLexer.getToken();
                                if (null != token) {
                                    ret = mListener.onLexParseToken(token);
                                } else {
                                    ret = false;
                                    Log.e(TAG, "token is null");
                                }

                                curLexer.reset();
                                curLexer = null;
                                break;
                        }
                    } else {
                        ret = false;
                        Log.e(TAG, "can not reg char:" + ch);
                        break;
                    }

                    if (!ret) {
                        if (null != curLexer) {
                            curLexer.reset();
                        }
                        break;
                    }
                }

                ret = mListener.onLexParseEnd(ret);
            } else {
                Log.e(TAG, "listener is null");
            }
        } else {
            Log.e(TAG, "str is empty");
        }

        return ret;
    }
}
