package br.uff.ic.gems.tipmerge;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.experiment.RevisionAnalyzer;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.EditedFile;
import br.uff.ic.gems.tipmerge.model.MergeCommits;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.Export;
import br.uff.ic.gems.tipmerge.util.RunGit;
import br.uff.ic.gems.tipmerge.util.Statistics;

public class MergeAnalyser2
{
	public static Repository repos = null;
	
	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			System.out.println("Reposit�rio: " + args[i]);
			repos = new RepositoryDao(new File(args[i])).getRepository();
			RepositoryDao rdao = new RepositoryDao(repos.getProject());
	        rdao.setGeneralBasicDatas(repos);
			runAnalysis();
			System.out.println("Terminado\n");
		}
	}
	
	public static void runAnalysis()
	{	
		MergeFilesDao mergeFilesDao = new MergeFilesDao();
		MergeCommitsDao mergeCommitsDao = new MergeCommitsDao(repos.getProject());

        String header[] = {"Hash do merge", "Data do merge", "Tempo de isolamento 1 (dias)", "Tempo de isolamento 2 (dias)", "Tempo de isolamento (MAX)", "Desenvolvedores 1", "Desenvolvedores 2", "Intersecção de Desenvolvedores", "Commits 1", "Commits 2", "Arquivos alterados 1", "Arquivos alterados 2", "Intersecção de Arquivos", "Conflito", "Arquivos", "Chunks"};
        String linesStr = "";
        List<String> merges = repos.getListOfMerges();
        int max = merges.size(), cont = 0;
        for(String hashMerge : merges) 
        {
        	System.out.printf("\t-> Merge %02d/%02d", ++cont, max);
            hashMerge = hashMerge.split(" ")[0];

            MergeFiles merge = mergeFilesDao.getMerge(hashMerge, repos.getProject());
            merge.setFilesOnBranchOne(new EditedFilesDao().getFiles(merge.getHashBase(), merge.getParents()[0], repos.getProject(), "All Files"));
            merge.setFilesOnBranchTwo(new EditedFilesDao().getFiles(merge.getHashBase(), merge.getParents()[1], repos.getProject(), "All Files"));
            
            try
            {
	            if(merge.getHashBase().equals(merge.getParents()[0]) || merge.getHashBase().equals(merge.getParents()[1]))
	            {
	            	System.out.println();
	            	continue;
	            }
	            else
	            	System.out.print(" (De ramos)");
            }
            catch(NullPointerException npe)
            {
            	System.out.println();
            	continue;
            }
            
            System.out.println();
            
            MergeCommits mergeCommits = new MergeCommits(hashMerge, repos.getProject());
            mergeCommits.setHashBase(merge.getHashBase());
            mergeCommits.setParents(merge.getParents()[0], merge.getParents()[1]);
            
            //=================// Qtde. de desenvolvedores //=================//
            mergeCommitsDao.setCommittersOnBranch(mergeCommits);
            int committers1 = 0, commits1 = 0;
            int committers2 = 0, commits2 = 0;
            int numIntersec = 0;
            List<String> committersR1 = new ArrayList<String>();
            
            for(Committer c : mergeCommits.getCommittersBranchOne())
            {
            	commits1 += c.getCommits();
            	++committers1;
            	
            	committersR1.add(c.getNameEmail());
            }
            
            for(Committer c : mergeCommits.getCommittersBranchTwo())
            {
            	commits2 += c.getCommits();
            	++committers2;
            	
            	if(committersR1.contains(c.getNameEmail()))
            		numIntersec += 1;
            }
            
            if(committers1 == committers2 && committers1 == numIntersec)
            	numIntersec = 2;
            else if(numIntersec > 0)
            	numIntersec = 1;

            //=================// Data do merge //=================//
            String mergeTimestamp = getMergeTimestamp(hashMerge);
            
            //=================// Tempo de isolamento //=================//
            String isolamento1 = tempoIsolamento(merge.getHashBase(), merge.getParents()[0]);
            String isolamento2 = tempoIsolamento(merge.getHashBase(), merge.getParents()[1]);
            String isolamentoMax = Math.max(Double.parseDouble(isolamento1), Double.parseDouble(isolamento2))+"";
            
            //=================// Qtde. de arquivos alterados //=================//
            int arquivosAlterados1 = 0, arquivosAlterados2 = 0, arqIntersec = 0;
            try
            {
            	arquivosAlterados1 = merge.getFilesOnBranchOne().size();   
            	arquivosAlterados2 = merge.getFilesOnBranchTwo().size();
            	
            	//=================// Qtde. de desenvolvedores //=================//
                mergeCommitsDao.setCommittersOnBranch(mergeCommits);
                List<String> arqsR1 = new ArrayList<String>();
                
                for(EditedFile c : merge.getFilesOnBranchOne())
                	arqsR1.add(c.getFileName());
                
                for(EditedFile c : merge.getFilesOnBranchTwo())
                	if(arqsR1.contains(c.getFileName()))
                		arqIntersec += 1;
                
                if(arquivosAlterados1 == arquivosAlterados2 && arquivosAlterados1 == arqIntersec)
                	arqIntersec = 2;
                else if(arqIntersec > 0)
                	arqIntersec = 1;
            }
            catch(NullPointerException npe)
            {}
            
            //================// Conflitos //==============//
            int arquivos = 0, chunks = 0;
            boolean conflito = false;
            if(RevisionAnalyzer.hasConflict(repos.getProject().toString(), merge.getParents()[0], merge.getParents()[1])) 
            {
            	//=======// Chunks //=======//
                conflito = true;                
                for(String line : RunGit.getListOfResult("git diff", repos.getProject()))
                	if(line.replace("+","").startsWith("======="))
                		chunks++;
            
                //=======// Arquivos //=======//
                arquivos = RunGit.getListOfResult("git diff --name-only --diff-filter=U", repos.getProject()).size();
                
                /*Alguns comandos Git:
                git log --reverse --ancestry-path HASH^..origin/BRANCH --pretty=format:%ci
                git log --reverse --ancestry-path f12b46a0ceaf2925a75fc3efd45ff2d0e60640d6^..refs/remotes/origin/Ramo2 --pretty=format:%ci
                git show-ref --head
                git rev-list ANCESTOR_HASH ^..PARENT_HASH
                git log --pretty=format:%HX%ci ANCESTOR_HASH PARENT_HASH*/
                //=================================================//
            }
            
            linesStr += merge.getHash()+","+mergeTimestamp+","+isolamento1+","+isolamento2+","+isolamentoMax+","+committers1+","+committers2+","+numIntersec+","+commits1+","+commits2+","+arquivosAlterados1+","+arquivosAlterados2+","+arqIntersec+","+ (conflito ? "SIM" : "NAO") +","+arquivos+","+chunks+"/x/";
        }
        Export.toCSV2(repos, header, linesStr.substring(0, linesStr.length()-3).split("/x/"));
	}
	
	public static String getMergeTimestamp(String hashMerge)
	{
		return RunGit.getResult("git log -1 --pretty=format:%ci " + hashMerge, repos.getProject()).substring(0, 19);
	}
	
	public static String tempoIsolamento(String hashBase, String hashParent)
	{
		//System.out.println("BASE = " + hashBase + " / " + hashParent);
		//==========//   Ancestor's data   //==========//
		List<String> beginLineTotal = RunGit.getListOfResult("git log --pretty=format:%HX%ciX%ct " + hashBase + ".." + hashParent, repos.getProject());
        if(beginLineTotal.size() > 0)
        {
        	//System.out.println("CMD = " + "git log --pretty=format:%HX%ciX%ct " + hashBase + ".." + hashParent);
            String parts[] = beginLineTotal.get(beginLineTotal.size()-1).split("X")/*, hashNextAncestor = parts[0], timestampNextAncestor = parts[1].substring(0, 19)*/;
            //==========//   Parent's data   //==========//
            String parentData = RunGit.getResult("git log -1 --pretty=format:%ciX%ct " + hashParent, repos.getProject()),  parts2[] = parentData.split("X");
            
          	//==========//  Dates   //==========//
            //System.out.println("PART1 = " + parts[2]);
            //System.out.println("PART2 = " + parts2[1]);
            long ancestorUnixTs = Long.parseLong(parts[2]), parentUnixTs = Long.parseLong(parts2[1]);
            //System.out.println("ISO = " + (parentUnixTs - ancestorUnixTs));
            return String.format("%.2f", Statistics.timeToDays(parentUnixTs - ancestorUnixTs)).replace(",", ".");
        }
        return "0.00";
	}
}
