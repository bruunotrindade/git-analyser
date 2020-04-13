package br.uff.ic.gems.tipmerge;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Map.Entry;

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

public class MergeAnalyser5
{
	public static class ArquivoConflito 
	{
		String nome;
		String causador[], commit[];
		boolean deletado;
		
		public ArquivoConflito(String nome)
		{
			this.nome = nome;
			causador = new String[2];
			commit = new String[2];
			deletado = false;
		}
	}

	public static Repository repos = null;
	
	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			System.out.println("Repositorio: " + args[i]);

			try 
			{
				repos = new RepositoryDao(new File(args[i])).getRepository();
				RepositoryDao rdao = new RepositoryDao(repos.getProject());
				rdao.setGeneralBasicDatas(repos);
				runAnalysis();
			}
			catch(NullPointerException ex)
			{
				System.out.println("\tRepositório inválido!");
			}
			System.out.println("Terminado\n");
		}
	}
	
	public static void runAnalysis()
	{	
		MergeFilesDao mergeFilesDao = new MergeFilesDao();
		MergeCommitsDao mergeCommitsDao = new MergeCommitsDao(repos.getProject());

        String headerTime[] = {"Hash do merge", "Data do merge", "Branching Time"};
        
        String linesTime = "";
        List<String> merges = repos.getListOfMerges();
        int max = merges.size(), cont = 0;
        
        for(String hashMerge : merges) 
        {
            hashMerge = hashMerge.split(" ")[0];
        	System.out.printf("\t-> Merge (%s) %02d/%02d", hashMerge, ++cont, max);

        	
            MergeFiles merge = mergeFilesDao.getMerge(hashMerge, repos.getProject());
            //merge.setFilesOnBranchOne(new EditedFilesDao().getFiles(merge.getHashBase(), merge.getParents()[0], repos.getProject(), "All Files"));
            //merge.setFilesOnBranchTwo(new EditedFilesDao().getFiles(merge.getHashBase(), merge.getParents()[1], repos.getProject(), "All Files"));
            
            try
            {
	            if(merge.getHashBase().equals(merge.getParents()[0]) || merge.getHashBase().equals(merge.getParents()[1]))
	            {
	            	System.out.println();
	            	continue;
	            }
	            else
	            	System.out.println(" - De ramos");
            }
            catch(NullPointerException npe)
            {
            	System.out.println();
            	continue;
            }
            
          //=================// Data do merge //=================//
            String mergeTimestamp = getMergeTimestamp(hashMerge);
            mergeTimestamp = convertToDefaultTimeZone(mergeTimestamp);
            
			//=================// Tempo de isolamento //=================//
			
			String commitNext1 = getAncestorNextHash(merge.getHashBase(), merge.getParents()[0]), commitNext2 = getAncestorNextHash(merge.getHashBase(), merge.getParents()[1]);
			long bTime = 0;

			long timeNext1 = getCommitUnixTime(commitNext1), timeNext2 = getCommitUnixTime(commitNext2);
			if(timeNext1 > timeNext2)
				bTime = timeNext2;
			else
				bTime = timeNext1;

			long timeParent1 = getCommitUnixTime(merge.getParents()[0]), timeParent2 = getCommitUnixTime(merge.getParents()[1]);
			if(timeParent1 > timeParent2)
				bTime = timeParent1 - bTime;
			else
				bTime = timeParent2 - bTime;
			
			String branchingTime = String.format("%.7f", Statistics.timeToDays(bTime)).replace(",", ".");

            linesTime += merge.getHash()+","+mergeTimestamp+","+branchingTime+"/x/";
        }
        try
        {
	        Export.toCSV2(repos, "time", headerTime, linesTime.substring(0, linesTime.length()-3).split("/x/"));
        }
        catch(StringIndexOutOfBoundsException ex)
        {
        	System.out.println("\tNenhum merge foi analisado.");
        }
    }
	
	/*
	 * git diff HASH arquivo
	 * Quando os marcadores de conflito são criados e é feito o diff com o commit, se a linha foi modificada
	 * no commit do diff, a linha fica sem o +
	 * */
	
	//Identificar o causador do conflito de cada arquivo
	public static void identificarCommitConflito(String hashBase, String hashParent, HashMap<String, ArquivoConflito> ac, int ramoID)
	{
		List<String> linhasLog = RunGit.getListOfResult("git log -m --name-only --pretty=format:%H " + hashBase + "^.." + hashParent, repos.getProject());
		String hash = null;
		for(String linha : linhasLog) //Linha = arquivo modificado
		{
			if(hash == null)
				hash = linha;
			else if(linha.length() == 0)
				hash = null;
			else if(ac.get(linha) != null && ac.get(linha).causador[ramoID] == null)
			{
				//Abrir o arquivo nesse commit pra ver
				List<String> linhasDiff = RunGit.getListOfResultSpecial(new String[]{"git", "diff", hash, linha}, repos.getProject());
				int tagStatus = 0;
				String tags[] = {"", "+<<<<<<<", "+=======", "+>>>>>>>"};
				ArquivoConflito arq = ac.get(linha);
				if(ac.get(linha).deletado == false)
				{
					for(String lD : linhasDiff)
					{
						//Verificando se a linha é algum marcador
						for(int i = 1; i < 4; i++)
							if(lD.startsWith(tags[i]))
							{
								tagStatus = i;
								break;
							}
						
						//Se estiver nas tags do ramo analisado e uma das linhas entre as tags não começar com +, então a modificação foi nesse commit
						if(ramoID == tagStatus && !linha.startsWith("+"))
						{
							arq.causador[ramoID] = getCommitAuthor(hash);
							arq.commit[ramoID] = hash;
							break;
						}
					}
				}
				else
				{
					String arquivoDeletado = RunGit.getResultSpecial(new String[]{"git", "diff", "--summary" ,"HEAD.." + hash, linha}, repos.getProject());
					if(arquivoDeletado != null && arquivoDeletado.contains("delete mode")) //Encontra o commit onde foi deletado
					{
						arq.causador[ramoID] = getCommitAuthor(hash);
						arq.commit[ramoID] = hash;
						arq.deletado = true;
					}
					else
					{
						arq.causador[ramoID] = getCommitAuthor(hash); //Encontra o commit onde foi modificado
						arq.commit[ramoID] = hash;
						arq.deletado = true;
					}
				}
			}
		}
	}
	
	//Verificar se um arquivo foi removido
	public static boolean checkDeletedFile(String file)
	{
		try
		{
			return RunGit.getResultSpecial(new String[]{"git", "diff", file}, repos.getProject()).contains("* Unmerged path " + file);
		}
		catch(NullPointerException npe)
		{
			return RunGit.getResultSpecial(new String[]{"git", "status", file, "-s"}, repos.getProject()).startsWith("DD ");
		}
	}
	
	//Recuperar o autor de um commit
	public static String getCommitAuthor(String hash)
	{
		return RunGit.getResult("git log --pretty=format:%an -1 " + hash, repos.getProject());
	}
	
	//Recuperar timestamp do merge
	public static String getMergeTimestamp(String hashMerge)
	{
		return RunGit.getResult("git log -1 --pretty=format:%ci " + hashMerge, repos.getProject())/*.substring(0, 19)*/;
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

	//Recuperar o hash do commit após o base
	public static String getAncestorNextHash(String hashBase, String hashParent)
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
			return parts[0];
        }
		return "null";
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
		//Removido: não faz sentido analisar tudo em um mesmo fuso, já que as pessoas geralmente fazem commit no horário da cidade delas
        String converted_date = Date;
        try {

            /*DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcFormat.setTimeZone(TimeZone.getTimeZone("GMT-05:00"));

            Date date = utcFormat.parse(Date);

            DateFormat currentTFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            currentTFormat.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));

            converted_date =  currentTFormat.format(date);*/
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

/*Alguns comandos Git:
git log --reverse --ancestry-path HASH^..origin/BRANCH --pretty=format:%ci
git log --reverse --ancestry-path f12b46a0ceaf2925a75fc3efd45ff2d0e60640d6^..refs/remotes/origin/Ramo2 --pretty=format:%ci
git show-ref --head
git rev-list ANCESTOR_HASH ^..PARENT_HASH
git log --pretty=format:%HX%ci ANCESTOR_HASH PARENT_HASH*/
//=================================================//
