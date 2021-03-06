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

public class MergeAnalyser
{
	public Repository repos = null;
	
	/*public static void main(String[] args)
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
	}*/
	
	public MergeAnalyser(Repository repos)
	{
		this.repos = repos;
		RepositoryDao rdao = new RepositoryDao(repos.getProject());
        rdao.setGeneralBasicDatas(repos);
	}
	
	public void runAnalysis(ArrayList<Atributo> atributos, File saida)
	{	
		MergeFilesDao mergeFilesDao = new MergeFilesDao();
		MergeCommitsDao mergeCommitsDao = new MergeCommitsDao(repos.getProject());
		boolean boolAtrib [] = new boolean[16];
		int ordemAtrib [] = new int[16];
		ArrayList<String> header = new ArrayList<String>();
		
		int index = 0;
		for(Atributo at : atributos)
		{
			boolAtrib[at.codigo] = true;
			ordemAtrib[index++] = at.codigo;
			header.add(at.checkbox.getText());
		}
		
        String linesStr = "";
        List<String> merges = repos.getListOfMerges();
        int max, cont = 0;
        try
        {
        	max = merges.size();
        }
        catch(NullPointerException npe)
        {
        	System.out.println("SEM MERGES");
        	return;
        }
        int num = 0;
        for(String hashMerge : merges) 
        {
        	if(num++ == 5)
        		break;
        	
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
            
            if(boolAtrib[7] || boolAtrib[5] || boolAtrib[8]) //Intersec, Commits 1 ou Devs 1
	            for(Committer c : mergeCommits.getCommittersBranchOne())
	            {
	            	commits1 += c.getCommits();
	            	++committers1;
	            	
	            	committersR1.add(c.getNameEmail());
	            }
            
            if(boolAtrib[7] || boolAtrib[6] || boolAtrib[9]) //Intersec, Commits 2 ou Devs 2
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
            String isolamento1 = (boolAtrib[2] || boolAtrib[4]) ? tempoIsolamento(merge.getHashBase(), merge.getParents()[0]) : ""; //Iso 1 ou Iso MAX
            String isolamento2 = (boolAtrib[3] || boolAtrib[4]) ? tempoIsolamento(merge.getHashBase(), merge.getParents()[1]) : ""; //Iso 2 ou Iso MAX
            String isolamentoMax = (boolAtrib[4]) ? Math.max(Double.parseDouble(isolamento1), Double.parseDouble(isolamento2))+"" : ""; //Iso MAX
            
            //=================// Qtde. de arquivos alterados //=================//
            int arquivosAlterados1 = 0, arquivosAlterados2 = 0, arqIntersec = 0;
            try
            {
            	arquivosAlterados1 = merge.getFilesOnBranchOne().size();   
            	arquivosAlterados2 = merge.getFilesOnBranchTwo().size();
            	
                mergeCommitsDao.setCommittersOnBranch(mergeCommits);
                
                //=================// Intersecção de Arquivos Alterados //=================//
                if(boolAtrib[12]) //Intersec Arq
                {
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
                if(boolAtrib[15])
	                for(String line : RunGit.getListOfResult("git diff", repos.getProject()))
	                	if(line.replace("+","").startsWith("======="))
                		chunks++;
            
                //=======// Arquivos //=======//
                if(boolAtrib[14])
                	arquivos = RunGit.getListOfResult("git diff --name-only --diff-filter=U", repos.getProject()).size();
                
                /*Alguns comandos Git:
                git log --reverse --ancestry-path HASH^..origin/BRANCH --pretty=format:%ci
                git log --reverse --ancestry-path f12b46a0ceaf2925a75fc3efd45ff2d0e60640d6^..refs/remotes/origin/Ramo2 --pretty=format:%ci
                git show-ref --head
                git rev-list ANCESTOR_HASH ^..PARENT_HASH
                git log --pretty=format:%HX%ci ANCESTOR_HASH PARENT_HASH*/
                //=================================================//
            }
            
            //linesStr += merge.getHash()+","+mergeTimestamp+","+isolamento1+","+isolamento2+","+isolamentoMax+","+committers1+","+committers2+","+numIntersec+","+commits1+","+commits2+","+arquivosAlterados1+","+arquivosAlterados2+","+arqIntersec+","+ (conflito ? "SIM" : "NAO") +","+arquivos+","+chunks+"/x/";
        
            String valoresAtrib [] = new String[12];
            valoresAtrib = new String[] {hashMerge, mergeTimestamp, isolamento1, isolamento2, isolamentoMax, committers1+"", committers2+"", numIntersec+"", commits1+"", commits2+"", arquivosAlterados1+"", arquivosAlterados2+"", arqIntersec+"", (conflito ? "SIM" : "NAO"), arquivos+"", chunks+""};
            
            String lineStr = "";
            for(int i = 0; i < index; i++)
            	lineStr += valoresAtrib[ordemAtrib[i]] + (i != index-1 ? "," : "/x/");
            
            linesStr += lineStr;
            
        }
        Export.toCSV(repos, header.toArray(new String[0]), linesStr.substring(0, linesStr.length()-3).split("/x/"), saida);
	}
	
	public String getMergeTimestamp(String hashMerge)
	{
		return RunGit.getResult("git log -1 --pretty=format:%ci " + hashMerge, repos.getProject()).substring(0, 19);
	}
	
	public String tempoIsolamento(String hashBase, String hashParent)
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
