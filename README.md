# VirtualView Tools

[中文文档](README-ch.md)

This project is a tool library as a part of VirtualView library, used to compile XML files:

# How-to

#### Run From CMD

##### Tools Introduction

Download the source code, the executable is located at folder `TemplateWorkSpace`. There are several files here:

| File                      | Introduction                        |
| ----------------------- | ------------------------- |
| config.properties       | configs of widget's ID and attribute's value type  |
| templatelist.properties | the file list of XML template               |
| build                   | output folder after compiling XML files     |
| template                | the source code XML files                  |
| compiler.jar            | executable jar file exported from the total source code |
| buildTemplate.sh        | the command script to run compiler.jar                  |

##### How to Run

- Execute the follow command in Terminal: `sh buildTemplate.sh`
- The artifacts is located at folder `build`

##### How to Configure `config.properties`

- `VIEW_ID_XXXX`
  - Define the widget's id, User's custom widget's should start from 1000;
  - For example, `VIEW_ID_FrameLayout=1` means the tag `<FrameLayout>` in XML will be compiled to number 1;
  - The property name in should start with **`VIEW_ID_`**;
- `property=ValueType`
  - Define the attribute's value type, which decide how to parse and compile original value into a compiled data in.
  - Currently supported defination is:
    - Normal type：`String`(Default, no need configuration)、`Float`、`Color`、`Expr`、`Number`、`Int`、`Bool`
    - Special type: `Flag`、`Type`、`Align`、`LayoutWidthHeight`、`TextStyle`、`DataMode`、`Visibility`
    - Enum type: `Enum<name:value,……>`
	  - Enum desp：
	    - For example: `flexDirection=Enum<row:0,row-reverse:1,column:2,column-reverse:3>`
	    - This means the attribute `row` will be converted into number `0`, and `row-reverse` into number `1`

##### How to Configure `templatelist.properties`

- Format: `xmlFileName=outFileName,Version[,platform]`
  - `xmlFileName`； The file name need to be compiled under folder template, no need to add file extension here;
  - `outFileName`: The output file name after compiling;
  - `Version`: XML file's version;
  - `platform`: Optional, valid value are `android` and `iphone`. Used to describe which platform current XML is target to, the output artifacts will be stored at a separate folder;

##### How to Write XML

- The same way as before, while no need to write Java code to process new attributes, the only thing need to do is add a ValueType defination in config.properties;

##### Artifacts after Compiling

- File under folder `out`: the binary data serialized from template file, all the other three outputs are generated from this.
- File under folder `java`: the binary data in form of Java byte array. May be copied to Android project to run.
- File udner folder `sign`: the md5 of binary data;
- File under folder `txt`: the binary data in form of HEX string.

#### Use as API

Except that use as a command tool to compile file, there is an another way to compile XML data from an IDE plugin, a client, a backend service or somewhere else. So it's possible to use JAVA API to compile data, the example code is as follow:

```
//Build a ViewCompilerApi instance
ViewCompilerApi viewCompiler = new ViewCompilerApi();
//init a ConfigLoader to load config.properties.
viewCompiler.setConfigLoader(new LocalConfigLoader());
//read XML data from file or somewhere else
FileInputStream fis = new FileInputStream(rootDir);
//Parameters are XML data's InputStream, template name, template version
byte[] result = viewCompiler.compile(fis, "icon", 13);
```

# Contributing

Before you open an issue or create a pull request, please read [Contributing Guide](CONTRIBUTING.md) first.

# LICENSE

VirtualView tools is available under the MIT license.
