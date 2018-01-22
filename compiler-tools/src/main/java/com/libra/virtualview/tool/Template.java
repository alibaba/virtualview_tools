package com.libra.virtualview.tool;

/**
 * Created by longerian on 2017/5/20.
 */
public class Template {

    public final String type;

    public String templatePath;

    public int version;

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
