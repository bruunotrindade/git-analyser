from os import walk
import os
import io

folder = "/home/brunotrindade/Pibic-Bruno/Resultados"
output = "Projeto,Linguagem,Merge,Conflitos,Média de Arquivos em Conflito,Conflitos com Mesmos, Conflitos com Diferentes\n"
for f in os.scandir(folder):
    if f.is_dir():
        language = f.name

        files = []
        for (dirpath, dirnames, filenames) in walk(f.path):
            files.extend(filenames)
            break

        for fi in files:
            if not fi.endswith("-conflito.csv"):    
                continue

            print("ARQUIVO " + fi)
            file_conflito = open(f"{f.path}/{fi}", encoding='utf-8', errors='ignore')

            file_name = fi.replace(".csv","")
            avg_same = 0
            avg_diff = 0
            avg_files = 0
            num_same = 0
            num = 0
            conflicts = 0

            for line in file_conflito.readlines():
                fields = line.split(",")

                if not fields[0].startswith("Hash"):
                    avg_same += float(fields[5])
                    avg_files += float(fields[2])
                    num_same += int(fields[3])
                    num += 1

            print(f"Soma same = {num_same}, {avg_files}, {num}")

            conflicts = avg_files

            num_diff = (avg_files - num_same) / avg_files
            num_same = num_same / avg_files
            avg_files = avg_files / num
            avg_same = avg_same / num
            avg_diff = avg_diff / num
            print(f"Same = {avg_same}, {avg_diff}")

            output += f"{file_name},{language},{num}," + "%d,%.5f,%.5f,%.5f\n" % (conflicts, avg_files, num_same, num_diff)


planilha = open(f"{folder}/Resultado-conflito.csv", "w")
planilha.write(output)
planilha.close()
