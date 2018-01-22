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

package com.libra.expr.compiler.api;

import com.libra.virtualview.compiler.ViewCompiler;
import com.libra.virtualview.compiler.config.ConfigManager;

import java.io.InputStream;

/**
 * Created by longerian on 2018/1/22.
 */
public class ViewCompilerApi {

    public void setConfigLoader(ConfigManager.ConfigLoader configLoader) {
        ConfigManager.setConfigLoader(configLoader);
    }

    public byte[] compile(InputStream xmlInputStream, String name, int version) {
        ViewCompiler compiler = new ViewCompiler();
        compiler.resetString();
        compiler.resetExpr();
        byte[] result = null;
        if (compiler.newOutputInit(1, null, version)) {
            if (!compiler.compile(name, xmlInputStream)) {
                System.out.println("compile file error --> " + name);
            }
            result = compiler.compileEndAndGet();
            if (result == null) {
                System.out.println("compile xml end error --> " + name);
            }
        } else {
            System.out.println("init byte info failed --> " + name);
        }
        return result;
    }

}
