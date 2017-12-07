# VirtualView Tools

[English Document](README.md)

本项目是 VirtualView 工程的配套工具项目，它主要用来编译 XML 模板，包含三个模块：

+ virtual-common：定义基础的工具类、常量；在 virtualview 和 compiler 模块里均要引入依赖；
+ compiler：模板编译工具，定义了基础组件的解析器、表达式解析器，甚至自定义组件解析器，用来将 XML 模板编译成二进制数据；它只用来开发编译工具，组件渲染运行不需要依赖它；
+ compiler-tools：基于 common 和 compiler 开发的编译工具，可在项目中运行，也可以导出 jar 包来运行；其中导出的 jar 包可用于进一步开发 IDE 的插件；目前在使用的时候直接在 IntelliJ 中运行编译；

因为组件模板与平台无关，因此编译器工具与平台无关，编译结果可运行到 Android，iOS 的宿主程序里。

# 使用说明

在完善开发工具之前，目前是直接使用 IntelliJ 里运行项目编译模板的，做几个规范说明：

### 代码规范

+ 自定义业务基础组件，常量定义可以另起一个在 biz-common 模块的来定义，用来提供给模板解析器及运行时 sdk 使用；
+ 自定义业务基础组件，解析器定义在 compiler 模块的 `com.libra.virtualview.compiler.parser.biz` 包下；
+ Tangram 业务组件模板编写，存放到 compiler-tools 模块的 template 文件夹下；

### 通过模块 compiler-tools 添加、编译新业务组件

+ 如果有新的自定义业务组件，先编写对应的解析器，参考这里的[文档](http://tangram.pingguohe.net/docs/android/add-a-custom-element)
+ 添加新的业务组件的模板 XML 到 template 文件夹下；
+ 在 `VirtualViewCompileTool` 的 `RESOURCE_LIST` 添加一条新的记录，有三个参数：
	+ type：组件名称，是业务开发时定义的；
	+ name：组件模板的文件名；
	+ version：**组件的版本号，它会被编译到组件的生成文件里，客户端读取组件的版本号，应该从模板数据里解析获取；版本号从 1 开始，每次要到线上做发布变更加 1，开发阶段不需要加 1；**
+ 运行工程，输出结果在 template 文件夹的 build 目录下，输入文件的文件名都是以上述 type 为基础，转换大小写、替换分隔符、加后缀来命名，包含四部分（每一份 XML 文件编译之后都会生成四份内容存到对应的目录下）：
	+ out目录：XML 模板编译成二进制数据的文件，其他内容都是以此为基础生成，上传到 cdn，通过模板管理后台下发的也是这里的文件；
	+ java目录：XML 模板编译成二进制数据之后的 Java 字节数组形式，可以直接拷贝到 Android 开发工程里使用，作为打底数据；
	+ sign目录：out 格式文件的 md5 码，供模板管理平台下发模板给客户端校验使用；
	+ txt目录：XML 模板编译成二进制数据之后的十六进制字符串形式，转换成二进制数据就是 java 目录下的字节数组；

这样就编译好了 XML 模板文件，开发阶段，可以直接拷贝 out 文件或者 java 文件到客户端工程里使用；

# 贡献代码

在提 Issue 或者 PR 之前，建议先阅读 [Contributing Guide](CONTRIBUTING.md)。按照规范提建议。

# 开源许可证

VirtualView Tools 遵循 MIT 开源许可证协议。
