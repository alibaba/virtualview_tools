这是一个加速开发 Virtual View 模版的小脚本，让你能脱离繁重的开发环境 Xcode 和 Android Studio，只需一个轻量级的文本编辑器如 VSCode/Atom/SublimeText 即可开始进入开发，并且提供热加载能力，大大加速提高开发调试效率。

![screen_record.gif](https://raw.githubusercontent.com/alibaba/virtualview_tools/master/compiler-tools/RealtimePreview/screenshot.gif) 


## 依赖

- java *编译 VV*
- python 2 *跑 WebServer*
- fswatch *监听文件修改* `brew install fswatch`
- qrencode *生成二维码* `brew install qrencode`

若需要脱离 iOS/Android 开发环境开发 VV，则需要安装对应客户端到真机进行预览、调试、开发。

- [iOS Playground](https://github.com/alibaba/VirtualView-iOS)
- [Android Playground](https://github.com/alibaba/Virtualview-Android)

## 目录结构

```
.
├── compiler.jar   (VV 的编译工具)
├── config.properties   (VV 编译模版需要的描述文件)
├── run.sh    (主运行脚本)
└── templates (按文件夹分割存放模版)
    └── helloworld
        ├── helloworld.json   (该模版所需参数)
        ├── helloworld.out    (该模版编译后的二进制)
        ├── helloworld.xml    (该模版源文件)
        └── helloworld_QR.png (该模版 URL 供于扫码加载)
```

> 参上，模版相关文件名必须和目录名一致！

模版统一存放在 templates 目录中，其中包含所有将会展示在 Playground 的 VV 模版。

> 为了方便模版开发和管理，你可以将这里的 `templates` 目录软链到你自己的额模版 Git 仓库。

## 使用

一、 首先确保上述依赖都安装完毕，然后将本项目拉到本地：


二、 然后切到 `/compiler-tools/realtime-preview` 目录执行 `run.sh` 即可开启服务，启动服务后正常情况会有如下输出：

```
➜  VVTool git:(master) ✗ ./run.sh
############# Begin Scripts #############
############# Copy All .xml files #############
############# Prebuild : templatelist.properties #############
############# Build: out files #############
compile name: helloworld path: /realtime-preview/template/helloworld.xml
############# Run HTTP Server #############
Start HTTP Server : http://127.0.0.1:7788
```

三、 这时候打开 iOS/Android Playground 即可看到所有 template 目录下的模版。

**二维码扫描**

每个模版目录下会生成类似 `xx_QR.png` 的二维码图片，当前模版对应的 HTTP 地址，如 *http://127.0.0.1:7788/helloworld/data.json* ，对应 iOS/Android Playground 应用可通过二维码扫描读取该路径中的模版和数据，然后在客户端加载。

> 实时预览要求 Playground 应用和本服务在同一网络环境