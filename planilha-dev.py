from os import walk
import os
import io

folder = "/home/brunotrindade/Out/OK"
out_folder = "/home/brunotrindade/Out"
for f in os.scandir(folder):
    if f.is_dir():
        language = f.name

        files = []
        for (dirpath, dirnames, filenames) in walk(f.path):
            files.extend(filenames)
            break

        for fi in files:
            print("ARQUIVO " + fi)
            file_conflito = open(f"{f.path}/{fi}", encoding='utf-8', errors='ignore')

            file_name = fi.replace(".csv","")
            output = ["Hash do merge,Data do merge,Branching Time,Merge time,Desenvolvedores 1,Desenvolvedores 2,Mesmos Desenvolvedores,Diferentes Desenvolvedores,Porcentagem de Mesmo Desenvolvedor,Intersecção de Desenvolvedores,Commits 1,Commits 2,Arquivos alterados 1,Arquivos alterados 2,Intersecção de Arquivos,Conflito,Arquivos,Chunks\n"]
            for line in file_conflito.readlines():
                fields = line.split(",")

                new_line = line
                if not fields[0].startswith("Hash"):
                    same = float(fields[6])
                    diff = float(fields[7])

                    try:
                        new_line = ",".join(fields[0:8]) + "," + ("%.5f" % (same/(same+diff))) + "," + ",".join(fields[8:17])
                        output.append(new_line)
                    except ZeroDivisionError:
                        print("Merge zerado removido = " + fields[0])


            planilha = open(f"{out_folder}/{file_name}.csv", "w")
            planilha.writelines(output)
            planilha.close()
