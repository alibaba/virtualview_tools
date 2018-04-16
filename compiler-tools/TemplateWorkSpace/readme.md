# 即将废弃，升级到 2.0 版本开发工具：[RealtimePreview](https://github.com/alibaba/virtualview_tools/tree/master/compiler-tools/RealtimePreview)

### 使用说明

#### 独立运行模式

##### 文件介绍

下载源码之后，可执行工具存放在目录 TemplateWorkSpace 里，包含以下几个文件/目录（或运行后产生）：

| 文件                      | 作用                        |
| ----------------------- | ------------------------- |
| config.properties       | 配置组件 ID、xml 属性对应的 value 类型    |
| templatelist.properties | 编译的模板文件列表               |
| build                   | 二进制文件的输出目录                |
| template                | xml 的存放路径                  |
| compiler.jar            | java 代码编译后 jar 文件，执行 xml 的编译逻辑 |
| buildTemplate.sh        | 编译执行文件                    |

##### 如何运行

- 打开命令行 执行 `sh buildTemplate.sh`
- 模板编译后的文件会输出到 build 路径下

##### 配置 config.properties

- `VIEW_ID_XXXX`
  - 配置 xml 节点 id
  - 如配置 `VIEW_ID_FrameLayout=1`，则 xml 节点中的 `<FrameLayout>` 在编译后会用数值1代替
  - 节点配置以 **`VIEW_ID_`** 开头
- `property=ValueType`
  - 配置属性值的类型，配置对所有模板生效，不支持在 1.xml 和 2.xml 中对相同的属性用不同的 ValueType 解析
  - 目前已经支持
    - 常规类型：`String`(默认，不需要配置)、`Float`、`Color`、`Expr`、`Number`、`Int`、`Bool`
    - 特殊类型 `Flag`、`Type`、`Align`、`LayoutWidthHeight`、`TextStyle`、`DataMode`、`Visibility`
    - 枚举类型 `Enum<name:value,……>`
	  - 枚举说明：
	    - 如配置 `flexDirection=Enum<row:0,row-reverse:1,column:2,column-reverse:3>`
	    - 在解析属性是配置 `row` 直接转化成 `int:0`，`row-reverse` 转成 `int:1`
- `DEFAULT_PROPERTY_XXXX`
  - 为了兼容就模板的编译，写的强制在二进制中写入一些属性类型定义，可以忽略

##### 配置 templatelist.properties

- 格式 `xmlFileName=outFileName,Version[,platform]`
  - `xmlFileName` 标识 template 目录下需要编译的 xml 文件名建议不带 `.xml` 后缀，目前做了兼容
  - `outFileName` 输出到 build 目录下的 `.out` 文件名
  - `Version` 表示 xml 编译后的版本号
  - `platform` 同时兼容 iOS 和 android 时不写，可填的值为 `android` 和`iphone`

##### xml 文件模板编写

- 和以前的方式一样，不需要额外写 Java 代码，只需要对新增的属性在config.properties 中配置 ValueType

#### 接口模式

除了直接使用命令行执行工具，还可以基于此搭建完整成熟的模板工具，它可以是个客户端，也可以是个后端服务，或者是个插件，所以需要提供接口模式供宿主程序调用。

```
//初始化构建对象
ViewCompilerApi viewCompiler = new ViewCompilerApi();
//设置配置文件加载器
viewCompiler.setConfigLoader(new LocalConfigLoader());
//读取模板数据
FileInputStream fis = new FileInputStream(rootDir);
//调用接口，传入必备参数，此时不区分平台，如果要区分平台，使用方单独编译即可
byte[] result = viewCompiler.compile(fis, "icon", 13);
```
