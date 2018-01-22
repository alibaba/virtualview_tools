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

package com.libra.expr.compiler;

import com.libra.Log;
import com.libra.TextUtils;
import com.libra.Utils;
import com.libra.expr.common.ExprCode;
import com.libra.expr.common.StringSupport;
import com.libra.expr.compiler.lex.LexParser;
import com.libra.expr.compiler.lex.Token;
import com.libra.expr.compiler.syntax.CodeGenerator;
import com.libra.expr.compiler.syntax.Expr;
import com.libra.expr.compiler.syntax.RegisterManager;
import com.libra.expr.compiler.syntax.SyntaxParser;

/**
 * Created by gujicheng on 16/9/8.
 */

public class ExprCompiler implements LexParser.Listener {
    private final static String TAG = "ExprCompiler_TMTEST";

    private StringSupport mStringStore;
    private LexParser mLexParser = new LexParser();
    private SyntaxParser mSyntaxParser = new SyntaxParser();
    private CodeGenerator mCodeGenerator = new CodeGenerator();
    private RegisterManager mRegisterManager = new RegisterManager();

    public ExprCompiler() {
        mLexParser.setListener(this);
    }

    private Listener mListener;

    public interface Listener {
        void onCodeBlock(String name, ExprCode codes);
    }

    public void setListener(Listener lis) {
        mListener = lis;
    }

    public void setStringSupport(StringSupport sm) {
        mStringStore = sm;
        mLexParser.setStringStore(mStringStore);
        mSyntaxParser.setStringStore(mStringStore);

        mSyntaxParser.setCodeGenerator(mCodeGenerator);
        mSyntaxParser.setRegisterManager(mRegisterManager);
    }

    private void reset() {
        mLexParser.reset();
        mSyntaxParser.reset();

        mCodeGenerator.reset();
        mRegisterManager.reset();
    }

    private boolean isSpace(String str) {
        boolean ret = true;

        if (!TextUtils.isEmpty(str)) {
            int len = str.length();
            for (int i = 0; i < len; ++i) {
                char c = str.charAt(i);
                if (!Utils.isSpace(c)) {
                    ret = false;
                    break;
                }
            }
        }

        return ret;
    }

    private String preProcess(String expr) {
        if (!TextUtils.isEmpty(expr)) {
            String[] exprSentences = expr.split(";");
//            Log.d(TAG, "preProcess sentence count:" + exprSentences.length);
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (int i = 0; i < exprSentences.length; ++i) {
                if (!isSpace(exprSentences[i])) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(";");
                    }

                    sb.append(exprSentences[i]);
                }
            }

            expr = sb.toString();
        }

        return expr;
    }

    public boolean compileBlock(String blocks) {
        boolean ret = true;
        if (!TextUtils.isEmpty(blocks) && (null != mListener)) {
            String[] blockArr = blocks.split("block");
            for (int i = 0; i < blockArr.length; ++i) {
                String block = blockArr[i].trim();
                if (!TextUtils.isEmpty(block)) {
//                    Log.d(TAG, "block:" + block);
                    int start = block.indexOf('{');
                    int end = block.lastIndexOf('}');
                    if (start < end && start > -1) {
                        String name = block.substring(0, start).trim();
                        String content = block.substring(start + 1, end);
//                        Log.d(TAG, "name:" + name + "  content:" + content);

                        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(content)) {
                            if (ret = doCompile(content)) {
                                mListener.onCodeBlock(name, getCode());
                            } else {
                                Log.e(TAG, "compile failed");
                                mListener.onCodeBlock(name, null);
                                break;
                            }
//                            Log.d(TAG, "ret:" + ret);
                        } else {
                            ret = false;
                            Log.e(TAG, "name or content is empty block:" + block);
                            break;
                        }
                    } else {
                        ret = false;
                        Log.e(TAG, "format error:" + block);
                        break;
                    }
                } else {
                    Log.w(TAG, "block is empty");
                }
            }
        } else {
            Log.e(TAG, "blocks is empty or mListener is null");
            ret = false;
        }

        return ret;
    }

    public boolean doCompile(String expr) {
        boolean ret = false;
        reset();

        expr = preProcess(expr);

        Log.d(TAG, "compile expr start:" + expr);

        if (!TextUtils.isEmpty(expr)) {
            // must add " "
            ret = mLexParser.parse(expr + " ");
        } else {
            Log.e(TAG, "empty expr:" + expr);
        }

        Log.d(TAG, "compile expr end:" + ret);
        return ret;
    }

    public boolean compile(String expr) {
        boolean ret = false;

        if (!TextUtils.isEmpty(expr)) {
            if (expr.startsWith("${") && expr.endsWith("}")) {
                expr = expr.substring(2, expr.length() - 1);

                ret = doCompile(expr);
            } else {
                Log.e(TAG, "error format:" + expr);
            }
        } else {
            Log.e(TAG, "empty expr");
        }

        return ret;
    }

    @Override
    public void onLexParseStart() {
//        Log.d(TAG, "onLexParseStart");
        mSyntaxParser.reset();
    }

    @Override
    public boolean onLexParseToken(Token token) {
//        Log.d(TAG, "onLexParseToken:" + token);
        boolean ret = mSyntaxParser.parse(token);
//        Log.d(TAG, "onLexParseToken ret:" + ret);

        return ret;
    }

    @Override
    public boolean onLexParseEnd(boolean result) {
//        Log.d(TAG, "onLexParseEnd:" + result);

        if (result) {
            mSyntaxParser.forceFinish();
//            mSyntaxParser.parse(new SymbolToken(SymbolToken.END));

            Expr expr = mSyntaxParser.getExpr();
            if (null == expr) {
//                result = false;
                Log.e(TAG, "onLexParseEnd get expr is null");
            }
//            Log.d(TAG, "result expr:" + expr + "  code:" + mSyntaxParser.getExprCode());
        } else {
            Log.e(TAG, "onLexParseEnd parse failed");
        }

        return result;
    }

    public ExprCode getCode() {
        ExprCode ret = mSyntaxParser.getExprCode();
        if (null != ret) {
            ret = ret.clone();
        }

        return ret;
    }
}
