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

import com.libra.TextUtils;
import com.libra.Log;

import com.libra.expr.common.ExprCode;
import com.libra.expr.compiler.ExprCompiler;
import com.libra.virtualview.common.Common;
import com.libra.virtualview.compiler.alert.AlertView;
import com.libra.virtualview.compiler.parser.FlexLayoutParser;
import com.libra.virtualview.compiler.parser.FrameLayoutParser;
import com.libra.virtualview.compiler.parser.GridLayoutParser;
import com.libra.virtualview.compiler.parser.GridParser;
import com.libra.virtualview.compiler.parser.LayoutBaseImpParser;
import com.libra.virtualview.compiler.parser.NativeImageParser;
import com.libra.virtualview.compiler.parser.NativeLineParser;
import com.libra.virtualview.compiler.parser.NativeTextParser;
import com.libra.virtualview.compiler.parser.PageParser;
import com.libra.virtualview.compiler.parser.Parser;
import com.libra.virtualview.compiler.parser.RatioLayoutParser;
import com.libra.virtualview.compiler.parser.ScrollerParser;
import com.libra.virtualview.compiler.parser.SliderParser;
import com.libra.virtualview.compiler.parser.TMNImageParser;
import com.libra.virtualview.compiler.parser.TMVirtualImageParser;
import com.libra.virtualview.compiler.parser.VH2LayoutParser;
import com.libra.virtualview.compiler.parser.VHLayoutParser;
import com.libra.virtualview.compiler.parser.VHParser;
import com.libra.virtualview.compiler.parser.ViewBaseParser;
import com.libra.virtualview.compiler.parser.VirtualContainerParser;
import com.libra.virtualview.compiler.parser.VirtualGraphParser;
import com.libra.virtualview.compiler.parser.VirtualImageParser;
import com.libra.virtualview.compiler.parser.VirtualLineParser;
import com.libra.virtualview.compiler.parser.VirtualProgressParser;
import com.libra.virtualview.compiler.parser.VirtualTextParser;
import com.libra.virtualview.compiler.parser.VirtualTimeParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by gujicheng on 16/8/3.
 */
public class ViewCompiler implements ExprCompiler.Listener {
    private final static String TAG = "ViewCompiler_TMTEST";

    private CodeWriter mCodeWrite = new CodeWriter();
    private RandomAccessMemByte mMemByte = null;
    private RandomAccessFile mFile = null;
    private int mCodeTotalSize;
    private int mCodeTotalCount;
    private int mCodeStartOffset;
    private List<ViewBaseParser.IBuilder> mViewParserBuilds = new ArrayList<>();

    private Stack<Parser> mBuilderStack = new Stack<>();
    private ExprCompiler mExprCompiler = new ExprCompiler();
    private ExprCodeStore mExprCodeStore = new ExprCodeStore();
    private StringStore mStringStore = new StringStore();

    private LayoutBaseImpParser mRootLayoutParser = new LayoutBaseImpParser();
    private int mPageId;

    public ViewCompiler() {
        this(true);
    }

    public ViewCompiler(Boolean userNewParser) {
        mExprCompiler.setStringSupport(mStringStore);
        mExprCompiler.setListener(this);
        if (userNewParser) {
            mViewParserBuilds.add(new ConfigParser.Builder());
        } else {
            mViewParserBuilds.add(new FrameLayoutParser.Builder());
            mViewParserBuilds.add(new VHLayoutParser.Builder());
            mViewParserBuilds.add(new VH2LayoutParser.Builder());
            mViewParserBuilds.add(new RatioLayoutParser.Builder());
            mViewParserBuilds.add(new GridLayoutParser.Builder());
            mViewParserBuilds.add(new FlexLayoutParser.Builder());

            mViewParserBuilds.add(new NativeTextParser.Builder());
            mViewParserBuilds.add(new VirtualTextParser.Builder());
            mViewParserBuilds.add(new NativeImageParser.Builder());
            mViewParserBuilds.add(new VirtualImageParser.Builder());
            mViewParserBuilds.add(new TMVirtualImageParser.Builder());
            mViewParserBuilds.add(new TMNImageParser.Builder());
            mViewParserBuilds.add(new VirtualLineParser.Builder());
            mViewParserBuilds.add(new NativeLineParser.Builder());

            mViewParserBuilds.add(new VirtualGraphParser.Builder());
            mViewParserBuilds.add(new VirtualProgressParser.Builder());
            mViewParserBuilds.add(new VirtualContainerParser.Builder());
            mViewParserBuilds.add(new VirtualTimeParser.Builder());

            mViewParserBuilds.add(new ScrollerParser.Builder());
            mViewParserBuilds.add(new PageParser.Builder());
            mViewParserBuilds.add(new GridParser.Builder());
            mViewParserBuilds.add(new VHParser.Builder());
            mViewParserBuilds.add(new SliderParser.Builder());
        }
        reset();
    }

    public boolean compileExprCodeFile(String path, int pageId) {
        boolean ret = false;

        mPageId = pageId;
        mStringStore.setPageId(mPageId);
        mExprCodeStore.setPageId(mPageId);

        InputStream in = getFileInputStream(path);
        if (null != in) {
            try {
                int length = in.available();
                byte[] buffer = new byte[length];
                in.read(buffer);
                String content = new String(buffer);

                ret = mExprCompiler.compileBlock(content);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "read file failed");
            }
        }

        return ret;
    }

    public boolean registerParser(ViewBaseParser.IBuilder builder) {
        boolean ret = false;

        if (null != builder) {
            mViewParserBuilds.add(builder);
            ret = true;
        } else {
            Log.e(TAG, "registerParser failed, buildr is null");
        }

        return ret;
    }

    public void resetString() {
        mStringStore.resetForFile();
    }

    public void resetExpr() {
        mExprCodeStore.reset();
    }

    public void reset() {
        mStringStore.reset();
        mCodeWrite.init();
        if (null != mMemByte) {
            mMemByte.close();
            mMemByte = null;
            mCodeTotalSize = 4;
            mCodeTotalCount = 0;
        }
    }

    public boolean newOutputFile(String path, int pageId, int patchVersion) {
        return newOutputFile(path, pageId, null, patchVersion);
    }

    public boolean newOutputFile(String path, int pageId, int[] depPageIds, int patchVersion) {
        boolean ret = false;

        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            try {
                mFile = new RandomAccessFile(path, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ret = newOutputInit(pageId, depPageIds, patchVersion);
        }
        return ret;
    }

    public boolean newOutputInit(int pageId, int[] depPageIds, int patchVersion) {
        mPageId = pageId;
        mStringStore.setPageId(mPageId);
        mExprCodeStore.setPageId(mPageId);
        mMemByte = new RandomAccessMemByte();
        if (null != mMemByte) {
            // tag
            mMemByte.write(Common.TAG.getBytes());

            // version
            mMemByte.writeShort(Common.MAJOR_VERSION);
            mMemByte.writeShort(Common.MINOR_VERSION);
            mMemByte.writeShort(patchVersion);
            // 11

            // code start
            mMemByte.writeInt(0);
            // code len
            mMemByte.writeInt(0);

            // string start
            mMemByte.writeInt(0);
            // string len
            mMemByte.writeInt(0);

            // expr code start
            mMemByte.writeInt(0);
            // expr code len
            mMemByte.writeInt(0);

            // extra data start
            mMemByte.writeInt(0);
            // extra data len
            mMemByte.writeInt(0);

            // pageId
            mMemByte.writeShort(pageId);

            // dep pages
            if (null != depPageIds) {
                mMemByte.writeShort(depPageIds.length);
                for (int i = 0; i < depPageIds.length; ++i) {
                    mMemByte.writeShort(depPageIds[i]);
                }
            } else {
                mMemByte.writeShort(0);
            }

            mCodeStartOffset = (int) mMemByte.length();

            // uiCodeTab item count
            mMemByte.writeInt(0);
            return true;
        } else {
            return false;
        }
    }

    public boolean compile(String name, String path) {
        Log.d(TAG, "compile name: " + name + " path: " + path);
        return compile(name, getFileInputStream(path));
    }

    public void extraStart() {
        if (null != mMemByte) {
            mMemByte.seek(mMemByte.length());
        }
    }

    public void extraEnd(int len) {
        if (null != mMemByte) {
            mMemByte.seek(39);
            mMemByte.writeInt(len);
            finish();
        } else {
            Log.e(TAG, "file is null");
        }
    }

    public void writeInt(int v) {
        if (null != mMemByte) {
            mMemByte.writeInt(v);
        } else {
            Log.e(TAG, "file is null");
        }
    }

    private boolean finish() {
        boolean ret = true;
        if (mFile != null) {
            try {
                mFile.write(mMemByte.getByte());
                mFile.close();
                mFile = null;
            } catch (IOException e) {
                e.printStackTrace();
                ret = false;
            }
        }
        mMemByte.close();
        mMemByte = null;
        return ret;
    }

    public void clear(String path) {
        if (null != mMemByte) {
            mMemByte.close();
            mMemByte = null;
        }
        File file = new File(path);
        if (null != file) {
            file.delete();
        }
    }

    public byte[] compileEndAndGet() {
        byte[] bytes = null;
        compileEndInternally();
        if (mMemByte != null) {
            bytes = mMemByte.getByte();
            mMemByte.close();
            mMemByte = null;
        }
        return bytes;
    }

    public boolean compileEnd() {
        return compileEnd(false);
    }

    public boolean compileEnd(boolean hasExtra) {
        boolean ret = false;
        ret = compileEndInternally();
        if (!hasExtra) {
            ret &= finish();
        }
        return ret;
    }

    private boolean compileEndInternally() {
        if (mMemByte != null) {
            // write ui code total size
            mMemByte.seek(mCodeStartOffset);
            mMemByte.writeInt(mCodeTotalCount);
            mMemByte.seek(11);
            mMemByte.writeInt(mCodeStartOffset);
            mMemByte.writeInt(mCodeTotalSize);

            // write string table start pos
            int stringStart = (int) mMemByte.length();
            mMemByte.writeInt(stringStart);
            mMemByte.seek(stringStart);

            // write string table
            int totalStrLen = mStringStore.storeToFile(mMemByte);
            if (totalStrLen > 0) {
                // write string len
                mMemByte.seek(23);
                mMemByte.writeInt(totalStrLen);
            }

            // write expr code table start pos
            int exprCodeStart = (int) mMemByte.length();
            mMemByte.seek(27);
            mMemByte.writeInt(exprCodeStart);
            mMemByte.seek(exprCodeStart);

            // write expr table
            int totalExprCodeLen = mExprCodeStore.storeToFile(mMemByte);
            if (totalExprCodeLen > 0) {
                // write expr len
                mMemByte.seek(31);
                mMemByte.writeInt(totalExprCodeLen);
            }

            // write extra data start
            mMemByte.seek(35);
            mMemByte.writeInt((int) mMemByte.length());
            return true;
        }
        return false;
    }

    private Parser build(String name) {
        Parser ret = null;

        for (ViewBaseParser.IBuilder builder : mViewParserBuilds) {
            ret = builder.build(name);
            if (null != ret) {
                ret.setExprCompiler(mExprCompiler);
                ret.setExprCodeManager(mExprCodeStore);
                ret.setStringSupport(mStringStore);

                ret.init();
                break;
            }
        }

        return ret;
    }

    @Override
    public void onCodeBlock(String name, ExprCode codes) {
        if (null != codes) {
            mExprCodeStore.addCode(codes, name);
        } else {
            Log.e(TAG, "compile:" + name + " failed");
        }
    }

    static class UserVarItem {
        public UserVarItem(int type, int nameId, int value) {
            mType = type;
            mNameID = nameId;
            mValue = value;
        }

        int mType;
        int mNameID;
        int mValue;
    }

    public boolean compile(String name, InputStream is) {
        boolean ret = true;
        if (null != is && name != null && name.length() > 0) {
            mCodeTotalCount = 0;
            mCodeTotalSize = 4;
            ViewBaseParser.AttrItem attrItem = new ViewBaseParser.AttrItem();
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(is, "UTF-8");
                mBuilderStack.clear();
                Parser parentParser = null;

                Map<Integer, Integer> intAPDatas = new HashMap<>();
                Map<Integer, Integer> intDatas = new HashMap<>();
                Map<Integer, Float> floatDatas = new HashMap<>();
                Map<Integer, Float> floatAPDatas = new HashMap<>();
                Map<Integer, Integer> strDatas = new HashMap<>();
                Map<Integer, Integer> codeDatas = new HashMap<>();
                List<UserVarItem> userVars = new ArrayList<>();
                mStringStore.setPageId(mPageId);

                int eventType = parser.getEventType();
                while (ret && eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            mCodeWrite.writeByte(Common.CODE_START_TAG);
                            Parser viewParser = build(parser.getName());
                            if (null != viewParser) {
                                int id = viewParser.getId();
                                if (id != 0) {
                                    // write component id
                                    mCodeWrite.writeShort((short) id);
                                } else {
                                    ret = false;
                                    Log.e(TAG, "getString1 error:" + parser.getName());
                                    break;
                                }

                                if (mBuilderStack.size() > 0) {
                                    parentParser = mBuilderStack.peek();
                                }
                                mBuilderStack.push(viewParser);

                                intDatas.clear();
                                intAPDatas.clear();
                                floatDatas.clear();
                                floatAPDatas.clear();
                                strDatas.clear();
                                codeDatas.clear();
                                userVars.clear();

                                int count = parser.getAttributeCount();
                                for (int i = 0; i < count; ++i) {
                                    String strKey = parser.getAttributeName(i);
                                    
                                    int key = 0;
                                    int nameSpaceKey = 0;
                                    String nameSpaceStr = parser.getName() +  "." + strKey;
                                    if(viewParser.supportNameSpace(nameSpaceStr)){
                                    	nameSpaceKey = nameSpaceStr.hashCode();
                                    }

                                    key = mStringStore.getStringId(strKey, false);
                                    if (key != 0) {
                                        String value = parser.getAttributeValue(i);
                                        boolean result = convertAttribute(name, parser, attrItem, parentParser, viewParser,nameSpaceKey, key, strKey, value,
                                                intDatas, intAPDatas, floatDatas, floatAPDatas, strDatas, codeDatas);
                                        if (!result) {
                                            break;
                                        }
                                    } else {
                                        if (strKey.startsWith("var_")) {
                                            ret = parseUserVar(userVars, strKey, parser.getAttributeValue(i));
                                            if (!ret) {
                                                Log.e(TAG, "parseUserVar error:" + parser.getName() + "  attribute name:" + parser.getAttributeName(i) + "   value:" + parser.getAttributeValue(i));
                                                break;
                                            }
                                        } else {
                                            //store new key as string
                                            int key2 = mStringStore.getStringId(strKey, true);
                                            if (key2 == 0) {
                                                AlertView.alert("custom key error:" + strKey);
                                            }
                                            String value = parser.getAttributeValue(i);
                                            boolean result = convertAttribute(name, parser, attrItem, parentParser, viewParser,nameSpaceKey, key2, strKey, value,
                                                    intDatas, intAPDatas, floatDatas, floatAPDatas, strDatas, codeDatas);
                                            if (!result) {
                                                break;
                                            }
                                            ret = true;
                                            Log.e(TAG, "getString2 :" + parser.getName() + "  attribute name:" + parser.getAttributeName(i) + "   value:" + parser.getAttributeValue(i));
                                        }
                                    }
                                }

                                if (ret) {
                                    //write to byte array
                                    count = writeFile(intAPDatas, intDatas, floatDatas, floatAPDatas, strDatas,
                                            codeDatas, userVars);
                                } else {
                                    break;
                                }
                            } else {
                                Log.e(TAG, "can not recognize:" + parser.getName());
                                ret = false;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            mCodeWrite.writeByte(Common.CODE_END_TAG);

                            mBuilderStack.pop();
                            break;

                        case XmlPullParser.END_DOCUMENT:
                            break;
                    }

                    if (!ret) {
                        Log.e(TAG, "has error, exit");
                        break;
                    }
                    eventType = parser.next();
                }

                if (!ret) {
                    Log.e(TAG, "end document error");
                }
            } catch (XmlPullParserException e) {
                Log.e(TAG, "has error" + e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(TAG, "has error" + e);
                e.printStackTrace();
            }

            if (ret) {
                List<Byte> code = mCodeWrite.getCode();
                try {
                    byte[] nameByte = name.getBytes("UTF-8");
                    int nameSize = nameByte.length;
                    mMemByte.writeShort(nameSize);
                    mMemByte.write(nameByte);

                    int size = code.size();
                    mMemByte.writeShort(size);
                    for (Byte b : code) {
                        mMemByte.writeByte(b);
                    }

                    mCodeTotalSize += nameSize + 2 + size + 2;
                    ++mCodeTotalCount;
                    mCodeWrite.init();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "parse failed");
            }
        } else {
            Log.e(TAG, "stream is null or name is empty");
        }

        return ret;
    }

    private int writeFile(Map<Integer, Integer> intAPDatas, Map<Integer, Integer> intDatas,
                          Map<Integer, Float> floatDatas, Map<Integer, Float> floatAPDatas, Map<Integer, Integer> strDatas,
                          Map<Integer, Integer> codeDatas, List<UserVarItem> userVars) {
        int count;
        // write int attribute
        count = intDatas.size();
        mCodeWrite.writeByte((byte) count);
        Set<Integer> dataSet = intDatas.keySet();
        for (Integer key : dataSet) {
            mCodeWrite.writeInt(key);
            mCodeWrite.writeInt(intDatas.get(key));
        }

        // write int RP attribute
        count = intAPDatas.size();
        mCodeWrite.writeByte((byte) count);
        dataSet = intAPDatas.keySet();
        for (Integer key : dataSet) {
            mCodeWrite.writeInt(key);
            mCodeWrite.writeInt(intAPDatas.get(key));
        }

        // write float attribute
        count = floatDatas.size();
        mCodeWrite.writeByte((byte) count);
        dataSet = floatDatas.keySet();
        for (Integer key : dataSet) {
            mCodeWrite.writeInt(key);
            mCodeWrite.writeInt(Float.floatToIntBits(floatDatas.get(key)));
        }

        // write float RP attribute
        count = floatAPDatas.size();
        mCodeWrite.writeByte((byte) count);
        dataSet = floatAPDatas.keySet();
        for (Integer key : dataSet) {
            mCodeWrite.writeInt(key);
            mCodeWrite.writeInt(Float.floatToIntBits(floatAPDatas.get(key)));
        }

        // write string attribute
        count = strDatas.size();
        mCodeWrite.writeByte((byte) count);
        dataSet = strDatas.keySet();
        for (Integer key : dataSet) {
            mCodeWrite.writeInt(key);
            mCodeWrite.writeInt(strDatas.get(key));
        }

        // write expr code attribute
        count = codeDatas.size();
        mCodeWrite.writeByte((byte) count);
        dataSet = codeDatas.keySet();
        for (Integer key : dataSet) {
            mCodeWrite.writeInt(key);
            mCodeWrite.writeInt(codeDatas.get(key));
        }

        // write user var
        count = userVars.size();
        mCodeWrite.writeByte((byte) count);
        for (UserVarItem item : userVars) {
            mCodeWrite.writeByte((byte) item.mType);
            mCodeWrite.writeInt(item.mNameID);
            mCodeWrite.writeInt(item.mValue);
        }
        return count;
    }

    private boolean parseUserVar(List<UserVarItem> userVarItems, String strKey, String strValue) {
        boolean ret = false;

        if (null != userVarItems && !TextUtils.isEmpty(strKey) && !TextUtils.isEmpty(strValue)) {
            String[] arr = strKey.split("_");
            if (3 == arr.length) {
                String strType = arr[1];
                String strName = arr[2];

                int type = -1;
                int value = 0;
                if (strType.equals("int")) {
                    type = Common.TYPE_INT;
                    try {
                        value = Integer.parseInt(strValue);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "parseInteger error:" + e);
                    }
                } else if (strType.equals("float")) {
                    type = Common.TYPE_FLOAT;
                    try {
                        value = Float.floatToIntBits(Float.parseFloat(strValue));
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "parseInteger error:" + e);
                    }
                } else if (strType.equals("string")) {
                    type = Common.TYPE_STRING;
                    value = mStringStore.getStringId(strValue);
                } else {
                    Log.e(TAG, "parseUserVar type invalidate" + strType);
                }

                if (type > -1) {
                    int nameId = mStringStore.getStringId(strName);

                    userVarItems.add(new UserVarItem(type, nameId, value));
                    ret = true;
                }
            } else {
                Log.e(TAG, "parseUserVar key invalidate:" + strKey);
            }
        }

        return ret;
    }

    private boolean convertAttribute(String name, XmlPullParser parser, 
    		Parser.AttrItem attrItem, Parser parentParser, 
    		Parser viewParser,int nameSpaceKey, int key, String strKey, String value,
                                     Map<Integer, Integer> intDatas,
                                     Map<Integer, Integer> intAPDatas,
                                     Map<Integer, Float> floatDatas,
                                     Map<Integer, Float> floatAPDatas,
                                     Map<Integer, Integer> strDatas,
                                     Map<Integer, Integer> codeDatas) {
        boolean ret = true;
        int id = 0;
        attrItem.setStr(value);
        // this and parent convert attribute, layout
        int convertResult = 0;
        if(nameSpaceKey==0){
        	convertResult = viewParser.convertAttribute(key, attrItem);
        }else{
        	convertResult = viewParser.convertAttribute(nameSpaceKey, key, attrItem);
        }
        if (convertResult == -1) {
            AlertView.alert("FileName= " + name + " VALUE ERROR:key=" + strKey + " value=" + attrItem.mStrValue);
        }
        if ((ViewBaseParser.CONVERT_RESULT_FAILED == convertResult)) {
            if (null != parentParser) {
                convertResult = parentParser.convertAttribute(key, attrItem);
            } else {
                // root width, height
                convertResult = mRootLayoutParser.convertAttribute(key, attrItem);
            }
        }

        if (ViewBaseParser.CONVERT_RESULT_ERROR == convertResult) {
            ret = false;
            Log.e(TAG, "parse attr error..." + "  attribute name:" + strKey + "   value:" + value);
        } else if (ViewBaseParser.CONVERT_RESULT_OK == convertResult) {
            // write value now!
            if (ViewBaseParser.AttrItem.TYPE_int == attrItem.mType) {
                if (Parser.AttrItem.EXTRA_RP == attrItem.mExtra) {
                    intAPDatas.put(key, attrItem.getmIntValue());
                } else {
                    intDatas.put(key, attrItem.getmIntValue());
                }
            } else if (ViewBaseParser.AttrItem.TYPE_float == attrItem.mType) {
                if (Parser.AttrItem.EXTRA_RP == attrItem.mExtra) {
                    floatAPDatas.put(key, attrItem.mFloatValue);
                } else {
                    floatDatas.put(key, attrItem.mFloatValue);
                }
            } else if (ViewBaseParser.AttrItem.TYPE_code == attrItem.mType) {
                codeDatas.put(key, attrItem.getmIntValue());
            } else if (ViewBaseParser.AttrItem.TYPE_string == attrItem.mType) {
                // string
                id = mStringStore.getStringId(value);
                if (id != 0) {
                    // write string value
                    strDatas.put(key, id);
                } else {
                    ret = false;
                    Log.e(TAG, "getString3 error:" + parser.getName() + "  attribute name:" + strKey + "   value:" + value);
                }
            }
        } else {
            // string
            id = mStringStore.getStringId(value);
            System.out.println("==ABC " + viewParser.getClass().getName() + "=====" + strKey + " file=" + name);
            if (id != 0) {
                // write string value
                strDatas.put(key, id);
            } else {
                ret = false;
                Log.e(TAG, "getString3 error:" + parser.getName() + "  attribute name:" + strKey + "   value:" + value);
            }
        }
        return ret;
    }

    private InputStream getFileInputStream(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            return fis;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
