/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.analyzer;

import br.uff.ic.gems.tipmerge.dao.CommitterDao;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.util.RunGit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author jjcfigueiredo
 */
public class MergesCounter {

    public static void main(String[] args) {

        JFileChooser fileChooser = new JFileChooser(new File("/"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Select projects directory");
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {

            System.out.println("You chose to open this directory: " + fileChooser.getSelectedFile().getName());
            
            //cria o arquivo com o nome resultados no mesmo diretório dos projetos...
            PrintWriter out;
            try {
                //cria um arquivo chamado results.txt e grava as informações lá
                out = new PrintWriter(new BufferedWriter(new FileWriter(fileChooser.getSelectedFile().toString() + "/results.txt", true)));
                out.println("[Project Name]" + "\t" + "[% of Merges >3]");
                out.close();
            } catch (IOException ex) {
                System.out.println("Impossível criar o arquivo para salvar os dados...");
                Logger.getLogger(MergesCounter.class.getName()).log(Level.SEVERE, null, ex);
            }


            for (File project : fileChooser.getSelectedFile().listFiles()) {
                if (project.isDirectory()) {
                    System.out.println("\nAnalyzing the project\t" + project.getName());

                    List<String> merges = RunGit.getListOfResult("git log --merges --all --pretty=%H%x09%ad --date=short", project);
                    System.out.println("Total of Merges\t" + merges.size());
                    double mergesIn = 0.0;
                    double mergesAll = merges.size();

                    String[] result = RunGit.getResult("git show -s --format=%ci%x09%H", project).split("\t");
                    String lastHash = result[1];
                    String firstHash = RunGit.getResult("git rev-list --max-parents=0 HEAD", project);
                    List<String> allCommitters = RunGit.getListOfResult("git shortlog -sne --no-merges " + firstHash + ".." + lastHash, project);
                    //List<String> allCommitters = RunGit.getListOfResult("git shortlog -sne --no-merges ", projectFolder);
                    System.out.println("Total of Committers\t" + allCommitters.size());

                    for(String merge : merges){
                        String data = merge.split("\t")[1];
                        merge = merge.split("\t")[0];
                        String hashParents = RunGit.getResult("git log --pretty=%P -n 1 " + merge, project);
                        //identifica os parents de cada ramo nas posições 0 e 1
                        String[] parent = new String[]{hashParents.split(" ")[0], hashParents.split(" ")[1]};
                        //identifica o hash base do merge
                        String hashBase = RunGit.getResult("git merge-base " + parent[0] + " " + parent[1], project);
                        
                        CommitterDao committerDao = new CommitterDao();
                        //pega a lista de pessoas que fizeram commit no ramo 1
                        List<Committer> committersb1 = committerDao.getCommittersList(hashBase, parent[0], project);
                        //pega a lista de pessoas que fizeram commit no ramo 2
                        List<Committer> committersb2 = committerDao.getCommittersList(hashBase, parent[1], project);
                        //conta quantas pessoas tem em cada ramo
                        int tam1 = committersb1.size(), tam2 = committersb2.size();
                        
                        //mostra todos os merges com a data....
                        //System.out.println("Merge\t" + merge + "\t" + tam1 + "\t" + tam2 + "\t" + data);
                        
                        //define a quantidade mínima de desenvolvedores em cada ramo
                        if(tam1 > 0 && tam2 > 0){
                            //verifica se tem ao menos 3 pessoas DIFERENTES somando-se os dois ramos
                            if((tam1 + tam2 >= 2) && (countUnique(committersb1, committersb2, 3))){
                                //mostra somente os merges complexos com as datas
                                System.out.println("Merge\t" + merge + "\t" + tam1 + "\t" + tam2 + "\t" + data);
                                mergesIn = mergesIn + 1;
                            }
                        }
                    }

                    //envia o resultado para o arquivo
                    System.out.println("mergesIn / mergesAll\t" + mergesIn + " " + mergesAll);
                    System.out.println(project.getName() + "\t" + mergesIn / mergesAll);
                    try {
                        //cria um arquivo chamado results.txt e grava as informações lá
                        out = new PrintWriter(new BufferedWriter(new FileWriter(fileChooser.getSelectedFile().toString() + "/results.txt", true)));
                        out.println(project.getName() + "\t" + mergesIn / mergesAll);
                        out.close();
                    } catch (IOException ex) {
                        System.out.println("Não foi possível salvar os dados deste projeto...");
                        Logger.getLogger(MergesCounter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }

        } else {
            System.out.println("You cancel the operation: " + fileChooser.getSelectedFile().getName());
        }
    }

    private static boolean countUnique(List<Committer> committersb1, List<Committer> committersb2, int min) {
        //System.out.println(committersb1.toString());
        //System.out.println(committersb2.toString());
        int count = committersb1.size();
        for(Committer cmt : committersb2){
            if(!committersb1.contains(cmt)){
                if(count++ >= min-1) return true;
            }
            //System.out.println("count\t" + count);
        }
        return false;
    }

}
