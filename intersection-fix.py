from os import walk
import os
import io
import shlex
import subprocess

# Funções de coleta e tratamento de dados

def get_merge_parents(repo, hash_merge):
    return run_command(f"git log --pretty=%P -n 1 {hash_merge}", repo)[0].split(" ")
 
def get_merge_base(repo, parent1, parent2):
    return run_command(f"git merge-base {parent1} {parent2}", repo)[0]

def get_committers_list(repo, hash_base, hash_target):
    list = run_command(f"git shortlog -sne --no-merges {hash_base}..{hash_target}", repo)
    committers = []
    for c in list:
        com = get_splitted_committer_line(c)
    
        if not any(x for x in committers if x[0].strip() == com[0].strip() or x[1].strip() == com[1].strip()):
            committers.append(com)
    
    return committers

def get_merge_committers_intersection(repo, hash_merge):
    parents = get_merge_parents(repo, hash_merge)
    base = get_merge_base(repo, parents[0], parents[1])

    full_committers_b1 = get_committers_list(repo, base, parents[0])
    full_committers_b2 = get_committers_list(repo, base, parents[1])

    same = 0

    for c in full_committers_b2:
        if any(x for x in full_committers_b1 if x[0].strip() == c[0].strip() or x[1].strip() == c[1].strip()):
            same += 1
    
    diff = len(full_committers_b1)+len(full_committers_b2)-same*2

    return (same, diff)

def get_splitted_committer_line(line):
    line = line.strip()
    datas = line.split("\t")

    lower_index = datas[1].index("<")
    result = [datas[1][0:lower_index-1],
        datas[1][lower_index + 1:len(datas[1]) - 1],
        datas[0]
    ]

    return result

def run_command(command, dir=None):
    process = subprocess.Popen(shlex.split(command), stdout=subprocess.PIPE, cwd=dir)
    out, err = process.communicate() 
    out = out.splitlines()

    for o in range(len(out)):
        out[o] = out[o].decode('UTF-8', 'replace')

    rc = process.poll()
    if rc != 0:
        raise Exception(f"Falha ao executar comando: {command}")

    return out

# Dados de entrada e saída

opcao = int(input("0 => Usar diretórios setados no código / 1 => Inserir diretórios agora\n"))

folder = "/home/brunotrindade/Analisar"
repos_folder = "/home/brunotrindade/NovosReps"
output_folder = "/home/brunotrindade/Out"

if opcao == 1:
    folder = input("Caminho completo do diretório dos .csv = ")
    repos_folder = input("Caminho completo do diretório dos repositórios = ")
    output_folder = input("Caminho completo do diretório de saída = ")


print("\nIniciando busca pelos arquivos...")
output = ""

for fo in os.scandir(folder):
    if fo.is_dir():

        files = []
        for (dirpath, dirnames, filenames) in walk(fo.path):
            files.extend(filenames)
            break

        language = fo.name

        for fi in files:
            file = open(f"{fo.path}/{fi}", encoding='utf-8', errors='ignore')
            print(f"Arquivo: {fi}")

            file_name = fi.replace(".csv","")

            repo = f"{repos_folder}/{fo.name}/{file_name}"
            if not os.path.exists(repo):
                print(f"-> Falha: Repositório não encontrado [{repo}]")
                continue

            lines = ["Hash do merge,Data do merge,Branching Time,Merge time,Desenvolvedores 1,Desenvolvedores 2,Mesmos Desenvolvedores,Diferentes Desenvolvedores,Porcentagem de Mesmo Desenvolvedor,Intersecção de Desenvolvedores,Commits 1,Commits 2,Arquivos alterados 1,Arquivos alterados 2,Intersecção de Arquivos,Conflito,Arquivos,Chunks\n"]

            file_lines = file.readlines()
            count_merge = 0
            for line in file_lines:
                fields = line.split(",")

                if not fields[0].startswith("Hash"):

                    count_merge += 1
                    if count_merge % 200 == 0:
                        print(f"\tLinha {count_merge}/{len(file_lines)-1}")

                    try:
                        if float(fields[6]) == 0: # Todos os desenvolvedores diferentes
                            new_line = ",".join(fields[0:6]) + ",0.00," + ("%.2f" % ( float(fields[4]) + float(fields[5]) )) + ",0.00000," + ",".join(fields[6:15])
                        elif float(fields[6]) == 2: # Todos os desenvolvedores iguais
                            new_line = ",".join(fields[0:6]) + f",{'%.2f' % (float(fields[4]))},0.00,1.00000," + ",".join(fields[6:15])
                        else:

                            same, diff = get_merge_committers_intersection(repo, fields[0])
                            new_line = ",".join(fields[0:6]) + f",{'%.2f' % (same)},{'%.2f' % (diff)}," + ("%.5f," % (same/(same+diff))) + f"{'2.00' if diff == 0 else '1.00'}," + ",".join(fields[7:15])

                        if not new_line.endswith("\n"):
                            new_line += "\n"

                        lines.append(new_line)
                    except:
                        if not ',' in line:
                            print("=> Linha vazia ignorada.")

                        #traceback.print_exc()
                        #print(line)
                        #print(fields)

            sheet = open(f"{output_folder}/{language}/{file_name}.csv", "w")
            sheet.writelines(lines)
            sheet.close()