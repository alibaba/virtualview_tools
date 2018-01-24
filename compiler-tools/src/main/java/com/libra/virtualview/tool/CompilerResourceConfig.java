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

package com.libra.virtualview.tool;

import com.libra.virtualview.compiler.alert.AlertView;
import com.libra.virtualview.compiler.config.ConfigManager;
import com.libra.virtualview.compiler.config.LocalConfigLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

public class CompilerResourceConfig {

	public List<Template> RESOURCE_LIST;

	public CompilerResourceConfig(Boolean buildJar) {
		super();
		ConfigManager.setConfigLoader(new LocalConfigLoader(buildJar));
		VirtualViewCompileTool t = new VirtualViewCompileTool();
		URL u = t.getClass().getClassLoader().getResource("");
    		String path = u.getPath();
		File file = new File(path + "/templatelist.properties");
		
		Properties prop = new Properties();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			prop.load(inputStream);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			AlertView.alert("templatelist.properties path error!");
		}

		List<Template> r = new ArrayList<>();
		for (Entry<Object, Object> e : prop.entrySet()) {
			String fileName = (String)e.getKey();
			String v = (String)e.getValue();
			String []vs = v.split(",");
			if(vs.length == 2){
				r.add(new Template(vs[0], fileName.endsWith(".xml") ? fileName:(fileName + ".xml"), Integer.parseInt(vs[1])));
			}else if(vs.length == 3){
				r.add(new Template(vs[0], fileName.endsWith(".xml") ? fileName:(fileName + ".xml"), Integer.parseInt(vs[1]), vs[2]));
			}
		}
		
		try {
			if(inputStream!=null){
				inputStream.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.RESOURCE_LIST = r;
	}

}
