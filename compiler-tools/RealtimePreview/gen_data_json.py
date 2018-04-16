# -*- coding: UTF-8 -*-

import sys
import os
import base64
import json

templates_path = sys.argv[1]
xml_build_path = sys.argv[2]
progress_template_name = sys.argv[3] if len(sys.argv) > 3 else None

# print "=== 开始处理模版 ==="
# print "xml -> out : " + xml_build_path
# print "生成 data.json : " + templates_path

def get_dir_list(path):
    return filter(lambda x: os.path.isdir(os.path.join(path, x)), os.listdir(path))

def gen_data_json(template_name):
    template_path = os.path.join(templates_path, template_name)
    files=os.listdir(template_path)
    template_base64_list = []

    # 遍历该模版文件夹下所有 .xml 拿到对应 .out -> base64
    if files:
        for f in files:
            if not os.path.isdir(f):
                f_name = os.path.splitext(f)[0]
                f_ext = os.path.splitext(f)[1]
                if f_ext == '.xml':
                    # print "处理模版：" + f_name
                    out_file_path = os.path.join(xml_build_path, f_name + ".out")
                    if os.path.isfile(out_file_path):
                        with open(out_file_path, "rb") as file_data:
                            xml_base64_string = base64.b64encode(file_data.read())
                            template_base64_list.append(xml_base64_string)
                    else:
                        print "模版 .out 未找到 : " + f

    # 读取模版参数 JSON
    param_dict = {}
    param_json_file_path = os.path.join(templates_path, template_name, template_name + ".json")
    if os.path.isfile(param_json_file_path):
        with open(param_json_file_path) as param_json_data:
            param_dict = json.load(param_json_data)

    # 合并 data.json
    data_dict = {"data" : param_dict, "templates" : template_base64_list}
    data_json_file_path = os.path.join(templates_path, template_name , 'data.json')
    with open(data_json_file_path, 'w') as outfile:
        json.dump(data_dict, outfile, indent=4)



if progress_template_name:
    gen_data_json(progress_template_name)
else:
    templates_dir_list = get_dir_list(templates_path)
    if templates_dir_list:
        for template_name in templates_dir_list:
            gen_data_json(template_name)

# print "=== 处理模版结束 ==="
