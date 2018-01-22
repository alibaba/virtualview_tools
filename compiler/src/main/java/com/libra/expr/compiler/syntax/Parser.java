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

package com.libra.expr.compiler.syntax;

import com.libra.expr.common.StringSupport;
import com.libra.expr.compiler.lex.Token;

/**
 * Created by gujicheng on 16/9/9.
 */
public abstract class Parser {
    private final static String TAG = "Parser_EXPR";

    public final static int RESULT_FAILED = 0;
    public final static int RESULT_NEED_MORE = 1;
    public final static int RESULT_FINISH_BACK_1 = 2;
    public final static int RESULT_FINISH = 3;

    protected Expr mExpr;
    protected CodeGenerator mCodeGenerator;
    protected RegisterManager mRegisterManager;
    protected StringSupport mStringManager;

    public Parser() {
        mExpr = null;
    }

    public void reset() {
        mExpr = null;
    }

    public void setStringManager(StringSupport sm) {
        mStringManager = sm;
    }

    public void setCodeGenerator(CodeGenerator codeGenerator) {
        mCodeGenerator = codeGenerator;
    }

    public void setRegisterManager(RegisterManager registerManager) {
        mRegisterManager = registerManager;
    }

    final public int addToken(Token token) {
        int ret = parse(token);

        if (RESULT_FINISH == ret || RESULT_FINISH_BACK_1 == ret) {
            mExpr = buildExpr();
//            if (null == mExpr) {
//                Log.e(TAG, "buildExpr failed");
//                ret = RESULT_FAILED;
//            }
        }

        return ret;
    }

    protected abstract int parse(Token token);

    protected abstract Expr buildExpr();

    final public Expr getExpr() {
        return mExpr;
    }
}
