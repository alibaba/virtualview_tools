/*
 * MIT License
 *
 * Copyright (c) 2017 Alibaba Group
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

package com.libra.virtualview.tool;

import com.google.gson.annotations.SerializedName;

/**
 * Created by longerian on 2017/5/20.
 */
public class Template {

    @SerializedName("type")
    public final String type;

    @SerializedName("path")
    public String templatePath;

    @SerializedName("version")
    public int version;

    @SerializedName("platform")
    public String platform;

    public Template(String type, String templatePath, int version) {
        this.type = type;
        this.templatePath = templatePath;
        this.version = version;
    }

    public Template(String type, String templatePath, int version, String platform) {
        this.type = type;
        this.templatePath = templatePath;
        this.version = version;
        this.platform = platform;
    }

}
