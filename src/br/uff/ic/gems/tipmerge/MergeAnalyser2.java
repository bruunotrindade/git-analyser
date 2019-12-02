package br.uff.ic.gems.tipmerge;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
			System.out.println("Repositorio: " + args[i]);
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

        String header[] = {"Hash do merge", "Data do merge", "Tempo de isolamento 1 [ancestral](dias)", "Tempo de isolamento 2 [ancestral](dias)", "Tempo de isolamento 1 [pai](dias)", "Tempo de isolamento 2 [pai](dias)", "Tempo de isolamento [merge]", "Desenvolvedores 1", "Desenvolvedores 2", "Intersecção de Desenvolvedores", "Commits 1", "Commits 2", "Arquivos alterados 1", "Arquivos alterados 2", "Intersecção de Arquivos", "Conflito", "Arquivos", "Chunks"};
        String linesStr = "";
        List<String> merges = repos.getListOfMerges();
        int max = merges.size(), cont = 0;
        
        for(String hashMerge : merges) 
        {
        	 
        	
        	System.out.printf("\t-> Merge %02d/%02d", ++cont, max);
            hashMerge = hashMerge.split(" ")[0];
            System.out.println(hashMerge);
        	if(hashMerge.equals("40664616731c003dd35c67af0100135c7378718a") == false)
        		continue;
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
            mergeTimestamp = convertToDefaultTimeZone(mergeTimestamp);
            
            //=================// Tempo de isolamento //=================//

            /**
             * Tempo de isolamento Ancestor: tempo do commit Base+1 até cada Parent.
             * Tempo de isolamento Parent: tempo do commit Base até cada Parent;
             * Tempo de isolamento Merge: tempo do commit Base até o Merge.
             **/
            
            String isolamentoAncestor1 = getAncestorIsolationTime(merge.getHashBase(), merge.getParents()[0]);
            String isolamentoAncestor2 = getAncestorIsolationTime(merge.getHashBase(), merge.getParents()[1]);
            String isolamentoParent1 = getIsolationTimeBetweenCommits(merge.getHashBase(), merge.getParents()[0]);
            String isolamentoParent2 = getIsolationTimeBetweenCommits(merge.getHashBase(), merge.getParents()[1]);
            String isolamentoMerge = getIsolationTimeBetweenCommits(merge.getHashBase(), hashMerge);
            
            //String isolamentoMax = Math.max(Double.parseDouble(isolamentoAncestor1), Double.parseDouble(isolamentoAncestor2))+"";
            
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
            int arquivos = RevisionAnalyzer.hasConflictNum(repos.getProject().toString(), merge.getParents()[0], merge.getParents()[1]);
            int chunks = 0;
            boolean conflito = false;
            
            //Removendo untracked files
            cleanUntrackedFiles();
            if(arquivos > 0) //Se o número de arquivos em conflito for positivo 
            {
            	//=======// Chunks //=======//
                conflito = true;                
                for(String line : RunGit.getListOfResult("git diff", repos.getProject()))
                	if(line.replace("+","").startsWith("======="))
                		chunks++;
            
                //=======// Arquivos //=======//
                //arquivos = RunGit.getListOfResult("git diff --name-only --diff-filter=U", repos.getProject()).size();
                
                /*Alguns comandos Git:
                git log --reverse --ancestry-path HASH^..origin/BRANCH --pretty=format:%ci
                git log --reverse --ancestry-path f12b46a0ceaf2925a75fc3efd45ff2d0e60640d6^..refs/remotes/origin/Ramo2 --pretty=format:%ci
                git show-ref --head
                git rev-list ANCESTOR_HASH ^..PARENT_HASH
                git log --pretty=format:%HX%ci ANCESTOR_HASH PARENT_HASH*/
                //=================================================//
            }
            
            linesStr += merge.getHash()+","+mergeTimestamp+","+isolamentoAncestor1+","+isolamentoAncestor2+","+isolamentoParent1+","+isolamentoParent2+","+isolamentoMerge+","+committers1+","+committers2+","+numIntersec+","+commits1+","+commits2+","+arquivosAlterados1+","+arquivosAlterados2+","+arqIntersec+","+ (conflito ? "SIM" : "NAO") +","+arquivos+","+chunks+"/x/";
        }
        Export.toCSV2(repos, header, linesStr.substring(0, linesStr.length()-3).split("/x/"));
	}
	
	//Recuperar timestamp do merge
	public static String getMergeTimestamp(String hashMerge)
	{
		return RunGit.getResult("git log -1 --pretty=format:%ci " + hashMerge, repos.getProject()).substring(0, 19);
	}
	
	//Recuperar o tempo de isolamento do ancestral
	public static String getAncestorIsolationTime(String hashBase, String hashParent)
	{
		List<String> beginLineTotal = RunGit.getListOfResult("git log --pretty=format:%HX%ciX%ct " + hashBase + ".." + hashParent, repos.getProject());
		if(beginLineTotal.size() > 0)
        {
			long parentUnixTs = getCommitUnixTime(hashParent);
			
			String parts[] = beginLineTotal.get(beginLineTotal.size()-1).split("X");
        	long ancestorUnixTs = Long.parseLong(parts[2]);
        	
        	for(int i = beginLineTotal.size()-2; i >= 0 && ancestorUnixTs > parentUnixTs; i--) //Evitando que o tempo do ancestral seja maior que o do pai (tempo negativo)
        	{
        		parts = beginLineTotal.get(i).split("X");
        		ancestorUnixTs = Long.parseLong(parts[2]);
        	}
        	return String.format("%.7f", Statistics.timeToDays(parentUnixTs - ancestorUnixTs)).replace(",", ".");
        }
		return "0.00000";
	}
	
	//Recuperar tempo de isolamento entre dois hashes
	public static String getIsolationTimeBetweenCommits(String hashA, String hashB)
	{
		long commitAtime = getCommitUnixTime(hashA), commitBtime = getCommitUnixTime(hashB);
        return String.format("%.7f", Statistics.timeToDays(commitBtime-commitAtime)).replace(",", ".");
	}
	
	//Converter para GMT-00 ou UTC
	public static String convertToDefaultTimeZone(String Date) 
	{
        String converted_date = "";
        try {

            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));

            Date date = utcFormat.parse(Date);

            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTFormat.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));

            converted_date =  currentTFormat.format(date);
        }catch (Exception e){ e.printStackTrace();}

        return converted_date;
	}
	
	//Recuperar horário do commit em Unix
	public static long getCommitUnixTime(String hash)
	{
		String parentData = RunGit.getResult("git log -1 --pretty=format:%ciX%ct " + hash, repos.getProject()),  parts2[] = parentData.split("X");
    	return Long.parseLong(parts2[1]);
	}
	
	//Remover arquivos não gerenciados pelo Git
	public static void cleanUntrackedFiles()
	{
		RunGit.getResult("git clean -df", repos.getProject());
	}
}
