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

package com.libra.virtualview.compiler.config;

import java.util.Map;

public class CompilerConfig {
	
	private Map<String, Integer> viewNameMap;
	
	private Map<Integer,String> propertyMap;

	private Map<String, String[]> viewDefaultProperty;
	
	Map<Integer,Map<String, Integer>>  enumMap;

	
	
	public Map<Integer, Map<String, Integer>> getEnumMap() {
		return enumMap;
	}

	public void setEnumMap(Map<Integer, Map<String, Integer>> enumMap) {
		this.enumMap = enumMap;
	}

	public Map<String, String[]> getViewDefaultProperty() {
		return viewDefaultProperty;
	}

	public void setViewDefaultProperty(Map<String, String[]> viewDefaultProperty) {
		this.viewDefaultProperty = viewDefaultProperty;
	}

	public Map<String, Integer> getViewNameMap() {
		return viewNameMap;
	}

	public void setViewNameMap(Map<String, Integer> viewNameMap) {
		this.viewNameMap = viewNameMap;
	}

	public Map<Integer, String> getPropertyMap() {
		return propertyMap;
	}

	public void setPropertyMap(Map<Integer, String> propertyMap) {
		this.propertyMap = propertyMap;
	}
	
	
}
