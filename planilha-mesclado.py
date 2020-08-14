from os import walk
from statistics import median
import os
import io

class Attribute():
    def __init__(self):
        self.list = []

    def append(self, item, status):
        att = {
            "value": item,
            "conflict": status
        }
        self.list.append(att)

    def get_median(self, status):
        filtered = [ x["value"] for x in self.list if x["conflict"] == status ]
        return median(filtered)

    def get_value_count(self, value, status):
        filtered = [ x["value"] for x in self.list if x["conflict"] == status and x["value"] == value ]
        return len(filtered)


folder = "/home/brunotrindade/Pibic-Bruno/Resultados"
output = ["Mediana de Desenvolvedores 1 (com conflito),Mediana de Desenvolvedores 1 (sem conflito),Mediana de Desenvolvedores 2 (com conflito),Mediana de Desenvolvedores 2 (sem conflito)," \
    "Mediana de Intersecção de Desenvolvedores (com conflito),Mediana de Intersecção de Desenvolvedores (sem conflito),Mediana de Commits 1 (com conflito),Mediana de Commits 1 (sem conflito),Mediana de Commits 2 (com conflito)," \
    "Mediana de Commits 2 (sem conflito),Mediana de Arquivos Alterados 1 (com conflito),Mediana de Arquivos Alterados 1 (sem conflito),Mediana de Arquivos Alterados 2 (com conflito),Mediana de Arquivos Alterados 2 (sem conflito)," \
    "Mediana de Branching Time (com conflito),Mediana de Branching Time (sem conflito),Nenhuma Intersecção de Dev (com conflito),Alguma Intersecção de Dev (com conflito),Total Intersecção de Dev (com conflito)," \
    "Nenhuma Intersecção de Dev (sem conflito),Alguma Intersecção de Dev (sem conflito),Total Intersecção de Dev (sem conflito)\n"]


file_conflito = open(f"{folder}/MESCLADO.csv", encoding='utf-8', errors='ignore')

dev_1 = Attribute()
dev_2 = Attribute()
commits_1 = Attribute()
commits_2 = Attribute()
files_1 = Attribute()
files_2 = Attribute()
dev_intersec = Attribute()
time = Attribute()

for line in file_conflito.readlines():
    fields = line.split(",")

    if not fields[0].startswith("Hash"):
        dev_1.append(int(fields[8]), True if fields[16] == "SIM" else False)
        dev_2.append(int(fields[9]), True if fields[16] == "SIM" else False)
        dev_intersec.append(int(fields[10]), True if fields[16] == "SIM" else False)
        commits_1.append(int(fields[11]), True if fields[16] == "SIM" else False)
        commits_2.append(int(fields[12]), True if fields[16] == "SIM" else False)
        files_1.append(int(fields[13]), True if fields[16] == "SIM" else False)
        files_2.append(int(fields[14]), True if fields[16] == "SIM" else False)
        time.append(float(fields[19]), True if fields[16] == "SIM" else False)

output.append((f"{dev_1.get_median(True)},{dev_1.get_median(False)}" \
    f",{dev_2.get_median(True)},{dev_2.get_median(False)},{dev_intersec.get_median(True)},{dev_intersec.get_median(False)}" \
    f",{commits_1.get_median(True)},{commits_1.get_median(False)},{commits_2.get_median(True)},{commits_2.get_median(False)}" \
    f",{files_1.get_median(True)},{files_1.get_median(False)},{files_2.get_median(True)},{files_2.get_median(False)}" \
    f",{time.get_median(True)},{time.get_median(False)}" \
    f",{dev_intersec.get_value_count(0, True)},{dev_intersec.get_value_count(1, True)},{dev_intersec.get_value_count(2, True)}" \
    f",{dev_intersec.get_value_count(0, False)},{dev_intersec.get_value_count(1, False)},{dev_intersec.get_value_count(2, False)}\n"))


planilha = open(f"{folder}/Resultado-mesclado.csv", "w")
planilha.writelines(output)
planilha.close()
