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

import com.google.gson.Gson;
import com.libra.virtualview.common.StringBase;
import com.libra.virtualview.compiler.ViewCompiler;
import com.libra.virtualview.compiler.parser.ScrollerParser;
import com.libra.virtualview.compiler.parser.biz.*;
import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longerian on 2017/5/20.
 */
public class VirtualViewCompileTool {

    public static List<Template> RESOURCE_LIST = new ArrayList<>();

    static {
        RESOURCE_LIST.add(new Template("NText", "ntext.xml", 1));
        RESOURCE_LIST.add(new Template("VText", "vtext.xml", 1));
        RESOURCE_LIST.add(new Template("NImage", "nimage.xml", 1));
        RESOURCE_LIST.add(new Template("VImage", "vimage.xml", 1));
        RESOURCE_LIST.add(new Template("NLine", "nline.xml", 1));
        RESOURCE_LIST.add(new Template("VLine", "vline.xml", 1));
        RESOURCE_LIST.add(new Template("Progress", "progress.xml", 1));
        RESOURCE_LIST.add(new Template("VGraph", "vgraph.xml", 1));
        RESOURCE_LIST.add(new Template("Page", "page.xml", 1));
        RESOURCE_LIST.add(new Template("PageItem", "532_tmall_hot_item.xml", 1));
        RESOURCE_LIST.add(new Template("PageScrollScript", "page_script.xml", 1));
        RESOURCE_LIST.add(new Template("Slider", "slider.xml", 1));
        RESOURCE_LIST.add(new Template("SliderItem", "slider_item.xml", 1));
        RESOURCE_LIST.add(new Template("FrameLayout", "framelayout.xml", 1));
        RESOURCE_LIST.add(new Template("RatioLayout", "ratiolayout.xml", 1));
        RESOURCE_LIST.add(new Template("GridLayout", "gridlayout.xml", 1));
        RESOURCE_LIST.add(new Template("Grid", "grid.xml", 1));
        RESOURCE_LIST.add(new Template("GridItem", "grid_item.xml", 1));
        RESOURCE_LIST.add(new Template("VHLayout", "vhlayout.xml", 1));
        RESOURCE_LIST.add(new Template("VH2Layout", "vh2layout.xml", 1));
        RESOURCE_LIST.add(new Template("VH", "vh.xml", 1));
        RESOURCE_LIST.add(new Template("ScrollerVL", "scroll_vl.xml", 1));
        RESOURCE_LIST.add(new Template("ScrollerVS", "scroll_vs.xml", 1));
        RESOURCE_LIST.add(new Template("ScrollerH", "scroll_h.xml", 1));
        RESOURCE_LIST.add(new Template("TotalContainer", "totalcontainer.xml", 1));
        RESOURCE_LIST.add(new Template("ClickScript", "click_script.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent1", "tmall_component_1.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent2", "tmall_component_2.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent3", "tmall_component_3.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent4", "tmall_component_4.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent5", "tmall_component_5.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent6", "tmall_component_6.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent7", "tmall_component_7.xml", 1));
        RESOURCE_LIST.add(new Template("TmallComponent8", "tmall_component_8.xml", 1));

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            compileInProject();
        } else {
            compileFromCmd(args[0]);
        }
    }

    static private void compileFromCmd(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                String configName = path + "/build.json";
                File config = new File(configName);
                if (config.exists()) {
                    Gson gson = new Gson();
                    try {
                        Reader configReader = new BufferedReader(new FileReader(config.getCanonicalPath()));
                        CompileModel model = gson.fromJson(configReader, CompileModel.class);
                        if (model == null || model.template == null) {
                            System.out.println("config file error, check it");
                        }
                        int size = model.template.size();
                        String rootDir = path + "/";
                        for (int i = 0; i < size; i++) {
                            Template node = RESOURCE_LIST.get(i);
                            node.templatePath = rootDir + node.templatePath;
                        }
                        compile(rootDir, RESOURCE_LIST);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("build.json not found under " + path);
                }
            } else {
                System.out.println("please provide a dir path");
            }
        } else {
            System.out.println("dir " + path + " not found");
        }
    }

    static private void compileInProject() {
        String rootDir = System.getProperty("user.dir") + "/compiler-tools/template/";
        System.out.println(rootDir);
        int size = RESOURCE_LIST.size();
        for (int i = 0; i < size; i++) {
            Template node = RESOURCE_LIST.get(i);
            node.templatePath = rootDir + node.templatePath;
        }
        compile(rootDir, RESOURCE_LIST);
    }

    static private void compile(String rootDir, List<Template> paths) {
        boolean ret = false;
        ViewCompiler compiler = new ViewCompiler();
        compiler.registerParser(new ScrollerParser.Builder());
        compiler.registerParser(new TMTipsViewParser.Builder());
        compiler.registerParser(new TMRecommendTextViewParser.Builder());
        compiler.registerParser(new TMRecommendBenefitViewParser.Builder());
        compiler.registerParser(new TMCommodityScenceViewParser.Builder());
        compiler.registerParser(new TMCommodity3DModelViewParser.Builder());
        compiler.registerParser(new TMCommodityShowWindowViewParser.Builder());
        compiler.registerParser(new TM620RecommendBenefitViewParser.Builder());
        compiler.registerParser(new TMPriceTextViewParser.Builder());
        compiler.registerParser(new TotalContainerParser.Builder());
        compiler.registerParser(new TM630RecommendMagicWandViewParser.Builder());
        compiler.registerParser(new TM630CountDownViewParser.Builder());
        compiler.registerParser(new TM830TabHeaderViewParser.Builder());

        File dirPath = new File(rootDir + "build/out/");
        File dirDytePath = new File(rootDir + "build/java/");
        File dirTextPath = new File(rootDir + "build/txt/");
        File dirSignPath = new File(rootDir + "build/sign/");

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        if (!dirDytePath.exists()) {
            dirDytePath.mkdirs();
        }

        if (!dirTextPath.exists()) {
            dirTextPath.mkdirs();
        }

        if (!dirSignPath.exists()) {
            dirSignPath.mkdirs();
        }

        File androidDirPath = new File(rootDir + "build/android/out/");
        File androidDirDytePath = new File(rootDir + "build/android/java/");
        File androidDirTextPath = new File(rootDir + "build/android/txt/");
        File androidDirSignPath = new File(rootDir + "build/android/sign/");

        if (!androidDirPath.exists()) {
            androidDirPath.mkdirs();
        }

        if (!androidDirDytePath.exists()) {
            androidDirDytePath.mkdirs();
        }

        if (!androidDirTextPath.exists()) {
            androidDirTextPath.mkdirs();
        }

        if (!androidDirSignPath.exists()) {
            androidDirSignPath.mkdirs();
        }

        File iphoneDirPath = new File(rootDir + "build/iphone/out/");
        File iphoneDirDytePath = new File(rootDir + "build/iphone/java/");
        File iphoneDirTextPath = new File(rootDir + "build/iphone/txt/");
        File iphoneDirSignPath = new File(rootDir + "build/iphone/sign/");

        if (!iphoneDirPath.exists()) {
            iphoneDirPath.mkdirs();
        }

        if (!iphoneDirDytePath.exists()) {
            iphoneDirDytePath.mkdirs();
        }

        if (!iphoneDirTextPath.exists()) {
            iphoneDirTextPath.mkdirs();
        }

        if (!iphoneDirSignPath.exists()) {
            iphoneDirSignPath.mkdirs();
        }

        String platform = "";
        for (Template resourceNode : paths) {
            if ("android".equalsIgnoreCase(resourceNode.platform)) {
                platform = "android/";
            } else if ("iphone".equalsIgnoreCase(resourceNode.platform)) {
                platform = "iphone/";
            } else {
                platform = "";
            }
            String path = rootDir + "build/" + platform + "out/" + resourceNode.type.replace("-", "_") + ".out";
            String bytePath = rootDir + "build/" + platform + "java/" + resourceNode.type.replace("-", "_").toUpperCase() + ".java";
            String textPath = rootDir + "build/" + platform + "txt/" + resourceNode.type.replace("-", "_") + ".txt";
            String signPath = rootDir + "build/" + platform + "sign/" + resourceNode.type.replace("-", "_") + ".md5";
            compiler.resetString();
            compiler.resetExpr();
            if (compiler.newOutputFile(path, 1, resourceNode.version)) {
                if (!compiler.compile(resourceNode.type, resourceNode.templatePath)) {
                    System.out.println("compile file error --> " + path);
                    continue;
                }
                ret = compiler.compileEnd();
                if (!ret) {
                    System.out.println("compile file end error --> " + path);
                } else {
                    compileByProduce(path, resourceNode.type, bytePath, textPath, signPath);
                }
            } else {
                System.out.println("new output file failed --> " + path);
            }
        }
    }

    static void compileByProduce(String sourcePath, String type, String bytePath, String textPath, String signPath) {
        try {
            FileInputStream fin = new FileInputStream(sourcePath);
            int length = fin.available();
            byte[] buf = new byte[length];
            fin.read(buf);
            fin.close();
            StringBuilder stringBuilder = new StringBuilder("");
            stringBuilder.append("public class ");
            stringBuilder.append(type.replace("-", "_").toUpperCase());
            stringBuilder.append("{\n");
            stringBuilder.append("public static final byte[] BIN = new byte[] {");
            stringBuilder.append("\n");
            for (int i = 0; i < buf.length; i++) {
                int byteValue = buf[i];
                stringBuilder.append(byteValue);
                stringBuilder.append(", ");
            }
            stringBuilder.append("\n");
            stringBuilder.append("};");
            stringBuilder.append("\n");
            stringBuilder.append("}");
            FileOutputStream fout = new FileOutputStream(bytePath);
            byte[] txt = stringBuilder.toString().getBytes();
            fout.write(txt);
            fout.flush();
            fout.close();

            stringBuilder = new StringBuilder("");
            for (int i = 0; i < buf.length; i++) {
                int v = buf[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            fout = new FileOutputStream(textPath);
            txt = stringBuilder.toString().getBytes();
            fout.write(txt);
            fout.flush();
            fout.close();

            fout = new FileOutputStream(signPath);
            String md5 = getByteMD5(buf);
            fout.write(md5.getBytes());
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getByteMD5(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] b = md.digest();
            String rtn = bytesToHexString(b);
            return rtn;
        } catch (Exception ex) {
            return null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        return bytesToHexString(src, true);
    }

    public static String bytesToHexString(byte[] src, boolean toLowerCase) {
        if (src == null || src.length <= 0) {
            return "";
        }

        char[] array = bytesToHex(src, toLowerCase);
        return new String(array);
    }

    public static char[] bytesToHex(byte[] data, boolean toLowerCase) {
        return bytesToHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    protected static char[] bytesToHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];

        int i = 0;
        for (int j = 0; i < l; i++) {
            out[(j++)] = toDigits[((0xF0 & data[i]) >>> 4)];
            out[(j++)] = toDigits[(0xF & data[i])];
        }
        return out;
    }
}

