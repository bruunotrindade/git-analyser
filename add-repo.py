from os import walk
import os
import io

folder = "/home/brunotrindade/Out"
output = ""

all_files = []

for fo in os.scandir(folder):
    if fo.is_dir():
        files = []
        for (dirpath, dirnames, filenames) in walk(fo.path):
            files.extend(filenames)
            break

        for fi in files:
            print(fi)
            f = open(f"{folder}/{fo.name}/{fi}", encoding='utf-8', errors='ignore')
            file_name = fi.replace(".csv","")

            for line in f.readlines():
                if not line.startswith("Hash"):
                    output += f"{file_name},{line}"

        planilha = open(f"{folder}/MESCLADO-NOVO.csv", "w")
        planilha.write(output)
        planilha.close()
