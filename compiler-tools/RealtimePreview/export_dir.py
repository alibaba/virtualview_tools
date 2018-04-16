import os,sys,json

d = sys.argv[1]

dir_list = filter(lambda x: os.path.isdir(os.path.join(d, x)), os.listdir(d))

# remove .git
if ".git" in dir_list:
	dir_list.remove(".git")

# print
print "templates: " + json.dumps(dir_list)

# Writing JSON data
with open(os.path.join(d, '.dir'), 'w') as f:
    json.dump(dir_list, f)