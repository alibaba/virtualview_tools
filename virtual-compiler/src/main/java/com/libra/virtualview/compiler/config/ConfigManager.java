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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.libra.virtualview.compiler.alert.AlertView;

public class ConfigManager {

    private static CompilerConfig staticConfig;

    private static ConfigLoader sConfigLoader;

    public static void setConfigLoader(ConfigLoader configLoader) {
        sConfigLoader = configLoader;
    }

    public static void clear() {
        sConfigLoader = null;
        staticConfig = null;
    }

    public static CompilerConfig loadConfigFile() {
        if (staticConfig != null) {
            return staticConfig;
        }
        CompilerConfig config = new CompilerConfig();
        Properties prop = new Properties();
        if (sConfigLoader != null) {
            try {
                InputStream inputStream = sConfigLoader.getConfigResource();
                prop.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

        Map<String, Integer> viewNameMap = new HashMap<String, Integer>();
        Map<Integer, String> viewIDMap = new HashMap<Integer, String>();

        Map<Integer, Map<String, Integer>> enumMap = new HashMap<Integer, Map<String, Integer>>();

        Map<String, String[]> viewDefaultProperty = new HashMap<String, String[]>();

        Map<Integer, String> propertyMap = new HashMap<Integer, String>();
        for (Entry<Object, Object> e : prop.entrySet()) {
            String s = (String) e.getKey();
            String v = (String) e.getValue();
            if (s.startsWith("VIEW_ID_")) {
                //viewID
                String viewName = s.substring("VIEW_ID_".length(), s.length());
                Integer i = Integer.parseInt(v);
                if (viewNameMap.get(viewName) != null) {
                    continue;
                }

                if (viewIDMap.get(i) != null) {
                    String msg = "config error: same viewId(" + i + ") for " + viewIDMap.get(i) + " and " + viewName;
                    AlertView.alert(msg);
                    System.out.println("error:" + msg);
                    return null;
                } else {
                    viewNameMap.put(viewName, i);
                    viewIDMap.put(i, viewName);
                }
            } else if (s.startsWith("DEFAULT_PROPERTY_")) {
                String viewName = s.substring("DEFAULT_PROPERTY_".length(), s.length());
                String[] propertys = v.split("\\|");
                viewDefaultProperty.put(viewName, propertys);
            } else {
                int ret = s.hashCode();
                if (v.startsWith("Enum<") && v.endsWith(">")) {
                    String subString = v.substring(5, v.length() - 1);
                    String[] kvArray = subString.split(",");
                    Map<String, Integer> m = new HashMap<String, Integer>();
                    for (String kvStr : kvArray) {
                        String kv[] = kvStr.split(":");
                        m.put(kv[0].toUpperCase(), Integer.parseInt(kv[1]));
                    }
                    enumMap.put(ret, m);
                    v = "Enum";
                }
                propertyMap.put(ret, v);
            }
        }

        config.setEnumMap(enumMap);
        config.setPropertyMap(propertyMap);
        config.setViewNameMap(viewNameMap);
        config.setViewDefaultProperty(viewDefaultProperty);

        ConfigManager.staticConfig = config;

        return config;
    }

    public interface ConfigLoader {
        InputStream getConfigResource();
    }

}
