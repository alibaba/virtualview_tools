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

package com.libra.virtualview.compiler.valueparser;

import com.libra.expr.compiler.ExprCompiler;
import com.libra.virtualview.compiler.ExprCodeStore;
import com.libra.virtualview.compiler.alert.AlertView;
import com.libra.virtualview.compiler.config.CompilerConfig;
import com.libra.virtualview.compiler.parser.Parser.AttrItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ValueParserCenter {

	Map<Integer, BaseValueParser> parserClassMap;
	
	Map<Integer,String> configMap;
	CompilerConfig config;
	
	public ValueParserCenter(CompilerConfig config) {
		super();

		this.config = config;
		this.configMap = config.getPropertyMap();
	}
	
	public boolean initValueParserClass(){
		parserClassMap = new HashMap<Integer, BaseValueParser>();
		
		for(Entry<Integer, String> e : configMap.entrySet()){
			String clsName = "com.libra.virtualview.compiler.valueparser." + e.getValue() + "ValueParser";
			try {
				BaseValueParser valueParser = (BaseValueParser)Class.forName(clsName).newInstance();
				if("Enum".equals(e.getValue())){
					EnumValueParser enumValueParser = (EnumValueParser)valueParser;
					enumValueParser.setEnumMap(config.getEnumMap());
				}
				parserClassMap.put(e.getKey(), valueParser);
			} catch (Exception e1) {
				e1.printStackTrace();
				
				String msg = "valueParser create error:" + e.getValue() + " check class:" + clsName;
				 System.out.println("error:" + msg);
				 AlertView.alert(msg);
				 return false;
			}
		}
		
		return true;
	}
	
	public boolean supportParser(String nameSpace){
		BaseValueParser p = parserClassMap.get(nameSpace.hashCode());
		return p!=null;
	}
	
	public boolean parseAttribute(int nameSpaceKey,int key, AttrItem item,ExprCompiler exprCompiler,ExprCodeStore mExprCodeStore){
		BaseValueParser p = parserClassMap.get(nameSpaceKey==0?key:nameSpaceKey);
		if(p!=null){
			p.setmExprCompiler(exprCompiler);
			p.setmExprCodeStore(mExprCodeStore);
			p.setKeyInt(key);
			
			return p.parser(item);
		}
		
		return true;
	}

}
