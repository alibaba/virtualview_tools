#!/bin/bash

echo "############# Begin Scripts #############"
workPath=`pwd`
templatePath="$workPath/template/"
templatesPath="$workPath/templates/"
templateFileName="templatelist.properties"

clean() {
	rm -rf build
	rm -rf $templatePath
	rm -rf $templateFileName
}
clean

echo "############# Copy All .xml files #############"
copyTemplate() {
	rm -rf $templatePath
	mkdir $templatePath
	cp $1 $templatePath
}
copyTemplate "${templatesPath}/*/*.xml"

echo "############# Prebuild : templatelist.properties #############"
genTemplateProperties(){
	rm -rf $templateFileName
	touch $templateFileName
	currentTimestamp=`date +%s`
	ls -l $1 | grep .xml | awk '{split($9, a, "."); if (a[1]!="") printf "%s=%s,%s\n", a[1], a[1], '${currentTimestamp}' }' > $templateFileName
}
genTemplateProperties $templatePath

echo "############# Build: out files #############"
rm -rf build
buildTemplate() {
	java -jar $workPath/compiler.jar jarBuild
}
buildTemplate

echo "############# Build: data.json #############"
python gen_data_json.py $templatesPath $workPath/build/out

echo "############# Run HTTP Server #############"
## 获取主机IP地址
ASB_HOST_IP=`ifconfig | grep "inet " | grep -Fv 127.0.0.1 | awk '{print $2}' | grep -m1 ""`
HTTPServerPort=7788

## 清理端口进程
killHTTPServer() {
	lsof -ti:$HTTPServerPort | xargs kill
}
killHTTPServer

## 进入目录
ASB_DIR_MOCK_API="templates/"
mkdir -p $ASB_DIR_MOCK_API

## 开启网络服务
cd $ASB_DIR_MOCK_API
SCREEN -d -m python -m SimpleHTTPServer $HTTPServerPort
echo "Start HTTP Server : http://${ASB_HOST_IP}:$HTTPServerPort"

cd $workPath

echo "############# Move .out files #############"
moveOutFiles() {
	for name in build/out/*.out ; do
		fileName=`basename $name`

		# .out 文件名去后缀
		fileNameNoExt="${fileName%.*}"

		# 对应模版目录
		destDir=$workPath/templates/$fileNameNoExt

		# 生成二维码
		if [ -x "$(command -v qrencode)" ]; then
				qrencode -o "$destDir/${fileNameNoExt}_QR.png" "http://${ASB_HOST_IP}:$HTTPServerPort/$fileNameNoExt/data.json"
		fi
	done
}
moveOutFiles

echo "############# Clean #############"
clean

echo "############# Dir #############"
# 输出目录信息
python export_dir.py $templatesPath

echo "############# Watch dog #############"
pkill fswatch
fswatch $templatesPath/*/*.xml $templatesPath/*/*.json -e data.json | xargs -n1 | while read xmlFileNamePath ; do
  thisTemplatePath=`dirname $xmlFileNamePath`
	thisTemplateName=`basename $thisTemplatePath`
	copyTemplate "${thisTemplatePath}/*.xml"
	genTemplateProperties $thisTemplatePath
	buildTemplate
	python gen_data_json.py $templatesPath $workPath/build/out $thisTemplateName
	moveOutFiles
	clean
done

killHTTPServer

echo "############# Done #############"
