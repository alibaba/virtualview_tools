#
# MIT License
#
# Copyright (c) 2018 Alibaba Group
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
#


#coding=utf-8

import os
import sys
import subprocess
import ConfigParser

def __exit__():
	exit()

cf = ConfigParser.ConfigParser()
cf.read('./previewconfig.conf')
targetPath = cf.get('path', 'target')
print 'your template would be pushed at %s' % targetPath

activity = cf.get('preview', 'activity')
print 'your template would be preview at %s' % activity

cmdLength = len(sys.argv)

print sys.argv

template = ''
if cmdLength < 2:
    print 'miss arg for template type'
    __exit__()
else:
    template = sys.argv[1]
    print "build all templates and push [%s] to your mobile" % template

os.system('sh ../buildTemplate.sh')

print 'your connected device is:'
returnCode = subprocess.call('adb devices', shell=True)

dest = ''
if template:
    templateFile = template.replace("-", "_") + ".out"
    source = '../build/out/%s' % templateFile
    if not os.path.exists(source):
        source = '../build/android/out/%s' % templateFile
    dest = targetPath + templateFile
    print 'start pushing %s to %s' % (source, dest)
    os.system('adb push %s %s' % (source, dest))
else:
    print 'start pushing all'
    os.system('adb push ../build/out/ %s' % targetPath)
    if not os.listdir('../build/android/out/'):
        os.system('adb push ../build/android/out/ %s' % targetPath)

print 'start activity to your device to reload preview'

os.system('adb shell am start -n %s -e name %s -e path %s --activity-single-top' %(activity, template, dest))

