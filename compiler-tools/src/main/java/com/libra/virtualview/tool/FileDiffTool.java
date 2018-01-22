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

import com.libra.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileDiffTool {
	String path1;
	String path2;
	public FileDiffTool(String path1,String path2) {
		super();
		
		
		this.path1 = path1;
		this.path2 = path2;
	}
	
	public void diff(){
		File rootPath1 = new File(path1);
		
		File[] f1List = rootPath1.listFiles();
		
		for(File f1SubFile : f1List){
			if(f1SubFile.isFile() && !f1SubFile.getName().startsWith(".")){
				String f2Path = path2 + "/" + f1SubFile.getName();
				File f2 = new File(f2Path);
				try {
					if(diffFile(f1SubFile,f2)){
						Log.i("FileDiffTool", ("===OK!!!! file=" + f1SubFile.getName()).replaceAll("_", "-")   );
					}else{
						Log.e("FileDiffTool", ("===ERROR!!! file=" + f1SubFile.getName()).replaceAll("_", "-") );
					}
				} catch (IOException e) {
					Log.e("FileDiffTool", "Exception filename=" + f1SubFile.getName(),e);
				}
			}
		}
	}
	
	public boolean diffFile(File f1,File f2) throws IOException{
		FileInputStream f1In = new FileInputStream(f1);
		byte []f1bytes = new byte[1024];
		
		FileInputStream f2In = new FileInputStream(f2);
		byte []f2bytes = new byte[f1bytes.length];
		
		boolean returnBoolean = true;
		boolean flag = true;
		int index1 = 0;
		while (flag) {
			int s1 = f1In.read(f1bytes);
			int s2 = f2In.read(f2bytes);
//			System.out.println("s1="+ s1 + " s2="+ s2);
			index1 +=s1;
			
			if(s1!=s2){
				returnBoolean = false;
				flag = false;
				break;
			}
			if(s1<=0){
				break;
			}
			for(int index=0;index < s1 ; index++){
				byte b1 = f1bytes[index];
				byte b2 = f2bytes[index];
				if(b1!=b2){
					returnBoolean = false;
					flag = false;
					break;
				}
			}
			int j = 0;
			j ++;
		}
		
		close(f1In);
		close(f2In);
		
		return returnBoolean;
	}
	
	private void close(InputStream f1In){
		try {
			f1In.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
