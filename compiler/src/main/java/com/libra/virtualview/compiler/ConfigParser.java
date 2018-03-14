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

package com.libra.virtualview.compiler;

import java.util.Map;

import com.libra.virtualview.compiler.alert.AlertView;
import com.libra.virtualview.compiler.config.CompilerConfig;
import com.libra.virtualview.compiler.config.ConfigManager;
import com.libra.virtualview.compiler.parser.Parser;
import com.libra.virtualview.compiler.valueparser.ValueParserCenter;

public class ConfigParser extends Parser {

    private String viewName;

    private CompilerConfig compilerConfig;
    private ValueParserCenter valueParserCenter;

    public static class Builder implements Parser.IBuilder {
        @Override
        public Parser build(String name) {
            ConfigParser parser = new ConfigParser();
            parser.setViewName(name);

            CompilerConfig config = ConfigManager.loadConfigFile();
            parser.setCompilerConfig(config);

            ValueParserCenter valueParserCenter = new ValueParserCenter(config);
            valueParserCenter.initValueParserClass();
            parser.setValueParserCenter(valueParserCenter);

            return parser;
        }
    }

    public void init() {
        super.init();
        this.initProperty();
    }

    private void initProperty() {
        Map<String, String[]> m = compilerConfig.getViewDefaultProperty();
        String[] propertys = m.get(viewName);
        if (propertys != null) {
            for (String p : propertys) {
                mStringSupport.getStringId(p, true);
            }
        }
    }

    public ValueParserCenter getValueParserCenter() {
        return valueParserCenter;
    }

    public void setValueParserCenter(ValueParserCenter valueParserCenter) {
        this.valueParserCenter = valueParserCenter;
    }

    public CompilerConfig getCompilerConfig() {
        return compilerConfig;
    }

    public void setCompilerConfig(CompilerConfig compilerConfig) {
        this.compilerConfig = compilerConfig;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public int getId() {
        Integer i = compilerConfig.getViewNameMap().get(viewName);
        if (i == null) {
            AlertView.alert("viewName:" + viewName + " is null");
        }
        return i;
    }

    public boolean supportNameSpace(String nameSpace){
    	return this.getValueParserCenter().supportParser(nameSpace);
    }
    
    public int convertAttribute(int nameSpaceKey,int key, AttrItem value) {
        boolean b = this.getValueParserCenter().parseAttribute(nameSpaceKey,key, value, this.mExprCompiler, this.mExprCodeStore);
        if (b) {
            return 1;
        } else {
            return -1;
        }
    }
    
    @Override
    public int convertAttribute(int key, AttrItem value) {
        boolean b = this.getValueParserCenter().parseAttribute(0,key, value, this.mExprCompiler, this.mExprCodeStore);
        if (b) {
            return 1;
        } else {
            return -1;
        }
    }

}
