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

import com.libra.virtualview.compiler.ViewCompiler;
import com.libra.virtualview.compiler.alert.Switch;
import com.libra.virtualview.compiler.parser.ScrollerParser;
import com.libra.virtualview.compiler.parser.biz.*;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;

/**
 * Created by longerian on 2017/5/20.
 */
public class VirtualViewCompileTool {

    public static void main(String[] args) {
        if (args.length == 0 || (args.length == 1 && "jarBuild".equals(args[0]))) {
            compileInProject(args.length == 1);
            if (args.length == 0) {
                diff(false);
            }
        }
    }

    static private void diff(Boolean buildJar) {
        VirtualViewCompileTool t = new VirtualViewCompileTool();
        URL u = t.getClass().getClassLoader().getResource("");
        String path = u.getPath();
        if (!buildJar) {
            path = path + "../";
        }
        FileDiffTool f = new FileDiffTool(path + "/build/out", path + "/build1/out");
        f.diff();
    }

    static private void compileInProject(Boolean buildJar) {
        Switch.shouldAlert = true;
        VirtualViewCompileTool t = new VirtualViewCompileTool();
        URL u = t.getClass().getClassLoader().getResource("");
        String path = u.getPath();
        if (!buildJar) {
            path = path + "../";
        }
        String rootDir = path + "template/";
        CompilerResourceConfig c = new CompilerResourceConfig(buildJar);
        List<Template> RESOURCE_LIST = c.RESOURCE_LIST;
        int size = RESOURCE_LIST.size();
        for (int i = 0; i < size; i++) {
            Template node = RESOURCE_LIST.get(i);
            node.templatePath = rootDir + node.templatePath;
        }
        compile(rootDir, RESOURCE_LIST, path);
    }

    static private void compile(String readDir, List<Template> paths, String buildPath) {
        boolean ret = false;
        Boolean useNew = false;
        useNew = true;
        ViewCompiler compiler = new ViewCompiler(useNew);
        if (!useNew) {
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
        }


        if (useNew) {
            buildPath = buildPath + "build/";
        } else {
            buildPath = buildPath + "build1/";
        }

        File dirPath = new File(buildPath + "out/");
        File dirDytePath = new File(buildPath + "java/");
        File dirTextPath = new File(buildPath + "txt/");
        File dirSignPath = new File(buildPath + "sign/");

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

        File androidDirPath = new File(buildPath + "android/out/");
        File androidDirDytePath = new File(buildPath + "android/java/");
        File androidDirTextPath = new File(buildPath + "android/txt/");
        File androidDirSignPath = new File(buildPath + "android/sign/");

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

        File iphoneDirPath = new File(buildPath + "iphone/out/");
        File iphoneDirDytePath = new File(buildPath + "iphone/java/");
        File iphoneDirTextPath = new File(buildPath + "iphone/txt/");
        File iphoneDirSignPath = new File(buildPath + "iphone/sign/");

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
            String path = buildPath + platform + "out/" + resourceNode.type.replace("-", "_") + ".out";
            String bytePath = buildPath + platform + "java/" + resourceNode.type.replace("-", "_").toUpperCase() + ".java";
            String textPath = buildPath + platform + "txt/" + resourceNode.type.replace("-", "_") + ".txt";
            String signPath = buildPath + platform + "sign/" + resourceNode.type.replace("-", "_") + ".md5";
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

