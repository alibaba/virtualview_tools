import sys

if len(sys.argv) <= 1:
    print("python vv_hash.py property_name")
    exit(0)

propertyName = sys.argv[1]
if len(propertyName) == 0:
    print("empty element name")
    exit(0)

hashCode = 0
for i in range(0, len(propertyName)):
    hashCode = (31 * hashCode + ord(propertyName[i])) & 0xFFFFFFFF
    if hashCode > 0x7FFFFFFF:
        hashCode = hashCode - 0x100000000
print("hash code: %d" % (hashCode))