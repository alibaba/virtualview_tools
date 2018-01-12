1. 在模板目录下配置build.json文件，内容如下，注意顺序，模板顺序与生成的id有关:

```json
{
  "template": [
    {"type":"TM","path":"tm.xml","version":"1","platform":"android"}
  ]
}
```

2. 执行`java -jar compiler-tools.jar [path of template]`，会在模板目录下生成一个build文件夹，里面有编译好的文件。


## 修改编译工具

修改代码后，直接在ide gradle侧边栏的compiler-tools中，Tasks->other/generateJar，即可在compiler-tools工程的build/libs目录下看到打包好的jar（名字带capsule），所有依赖都自动导进去了。

