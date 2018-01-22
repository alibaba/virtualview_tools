# 文件介绍

| 文件                      | 作用                        |
| ----------------------- | ------------------------- |
| config.properties       | 配置组件ID、xml属性对应的value类型    |
| templatelist.properties | 编译的vv模板文件列表               |
| build                   | 二进制文件的输出目录                |
| template                | xml的存放路径                  |
| compiler.jar            | java代码编译后jar文件，执行xml的编译逻辑 |
| buildTemplate.sh        | 编译执行文件                    |

# 如何运行

- 打开命令行 执行 sh buildTemplate.sh
- 模板编译后的文件会输出到 build路径下



# 配置config.properties

- VIEW_ID_XXXX 
  - 配置xml节点id
  - 如配置VIEW_ID_FrameLayout=1，则xml节点中的<FrameLayout>在编译后会用数值1代替
  - 节点配置以`VIEW_ID_`开头
- property=ValueType
  - 配置属性值的类型，配置对所有模板生效，不支持在1.xml和2.xml中对相同的属性用不同的ValueType解析
  - 目前已经支持
    - 常规类型：String(默认，不需要配置)、Float、Color、Expr、Number、Int、Bool
    - 特殊类型Flag、Type、Align、LayoutWidthHeight、TextStyle、DataMode、Visibility
    - 枚举类型Enum\<name:value,……\>
  - 枚举说明：
    - 如配置flexDirection=Enum\<row:0,row-reverse:1,column:2,column-reverse:3\>
    - 在解析属性是配置row直接转化成int:0,row-reverse转成int:1
- DEFAULT_PROPERTY_XXXX
  - 为了兼容就模板的编译，写的强制在二进制中写入一些属性类型定义，可以忽略





# 配置templatelist.properties

- 格式xmlFileName=outFileName,Version[,platform]
  - xmlFileName标识template目录下需要编译的xml文件名建议不带`.xml`后缀，目前做了兼容
  - outFileName输出到build目录下的`.out`文件名
  - xml编译后的版本号
  - platform同时兼容iOS和android时不写，可填的值为`android`和`iphone`

# xml文件模板编写

- 和以前的方式一样，不需要额外写java代码，只需要对新增的属性在config.propertiesz中配置ValueType