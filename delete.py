from os import walk
import os
import io

dir = "JavaScript"
mypath = "/home/brunotrindade/Conflito/" + dir
paths = []
for (dirpath, dirnames, filenames) in walk(mypath):
    paths.extend(filenames)
    break

try:
    os.stat("/home/brunotrindade/Novos/"+dir)
except:
    os.mkdir("/home/brunotrindade/Novos/"+dir)


for p in paths:
    name = p.split("-")[0]
    file_conflito = open(f"{mypath}/{p}", encoding='utf-8', errors='ignore')
    
    new_conflito = open(f"/home/brunotrindade/Novos/{dir}/{name}.csv", "w")
    
    for line in file_conflito.readlines():
        fields = line.split(",")
        if fields[0].startswith("Hash"):
            new_conflito.write(line)
        else:
            nl = ""
            for i in range(0, 5):
                nl += f"{fields[i]},"
            nl += "%.7f," % (int(fields[3])/int(fields[2]))
            nl += "%.7f" % (1 - int(fields[3])/int(fields[2]))
            new_conflito.write(nl+"\n")
    
    new_conflito.close()
