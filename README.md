# VirtualView Tools

[中文文档](README-ch.md)

This project is a tool library as a part of VirtualView library, it includes three modules:

+ common：defines basic utils and constants, both library virtualview and compiler denpend it;
+ compiler: a compiler to compile template files, defines the basic component parser, expression parser, it converts original XML template file into binary data. It is used to develop compiler tool and not necessary to render views;
+ compiler-tools: a executable tool based on library common and compiler. It could be directly run in Intellij IDEA, or exported as a jar file to run. The exported jar file could be used to develop plugin of IDE in the future; Currently we run it directly in  Intellij IDEA;

The output files produced by this project are cross platform, they can be both run on Android and iOS.

# User guide

Before an ideal tool was completed, we could use IntelliJ to develop and compile component template. So here show you how to do it:

### A little code style

+ For custom business basic component, the related constants should be defined in a new module such as biz-common which is provided to module compiler and main sdk;
+ For custom business basic component, the related parser should be defined in package `com.libra.virtualview.compiler.parser.biz`;
+ For business component template, put them under the folder of `template` in module compiler-tools;

### Add and compile new business component

+ Put the template file under the folder of `template` in module compiler-tools;
+ In class `VirtualViewCompileTool`, add a new record to `RESOURCE_LIST`, there are three parameters:
    + type: the name of business component;
    + name: the name of template file name;
    + version: the version of template file, it would be compiled into final binary data. The app running it should read version from binary data as the template's version. The version starts with number 1, when you make some changes and publish it online to override existing template data in App, increase by 1.
+ After the preparing work has been done, run the main module, the output files are created under folder of `build` in folder `template`. Each template file would create four output file in four different folds as follow:
    + File under folder `out`: the binary data serialized from template file, all the other three outputs are generated from this.
    + File under folder `java`: the binary data in form of Java byte array. May be copied to Android project to run.
    + File udner folder `sign`: the md5 of binary data;
    + File under folder `txt`: the binary data in form of HEX string.

# Contributing

Before you open an issue or create a pull request, please read [Contributing Guide](CONTRIBUTING.md) first.

# LICENSE

VirtualView tools is available under the MIT license.
