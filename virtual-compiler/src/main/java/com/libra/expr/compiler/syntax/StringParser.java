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

import com.libra.expr.compiler.lex.StringToken;
import com.libra.expr.compiler.lex.Token;

/**
 * Created by gujicheng on 16/9/9.
 */
public class StringParser extends Parser {
    private final static String TAG = "StringParser_TMTEST";

    private int mValue;

    @Override
    protected int parse(Token token) {
        if (Token.TYPE_STRING == token.mType) {
            StringToken st = (StringToken)token;
            mValue = st.mValue;

            return RESULT_FINISH;
        }
        return RESULT_FAILED;
    }

    @Override
    protected Expr buildExpr() {
        return new StringExpr(mValue);
    }
}
