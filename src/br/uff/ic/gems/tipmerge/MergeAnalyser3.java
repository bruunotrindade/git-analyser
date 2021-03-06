package br.uff.ic.gems.tipmerge;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import br.uff.ic.gems.tipmerge.dao.EditedFilesDao;
import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.dao.RepositoryDao;
import br.uff.ic.gems.tipmerge.experiment.RevisionAnalyzer;
import br.uff.ic.gems.tipmerge.model.Committer;
import br.uff.ic.gems.tipmerge.model.MergeCommits;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.Export;
import br.uff.ic.gems.tipmerge.util.RunGit;

public class MergeAnalyser3
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

        String header[] = {"Hash do merge", "Intersecção de Desenvolvedores", "Arquivos em Conflito", "Arquivos com Conflito causado pelo Mesmo", "Arquivos Null"};
		String linesStr = "";
        List<String> merges = repos.getListOfMerges();
        int max = merges.size(), cont = 0;
        
        /*for(int i = 10000; i < merges.size(); i++)
        {
        	String teste = merges.get(i).split(" ")[0];
        	if(teste.equals("ddaa390a62069928d89a7bd148f75e2df05d6572"))
        	{
        		System.out.println(i);
        		break;
        	}
        }*/
        
        
        /*for(String hashMerge : merges) 
        {*/
        
        String hashMerge = merges.get(20255);
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
	            	//continue;
	            }
	            else
	            	System.out.print(" - De ramos");
            }
            catch(NullPointerException npe)
            {
            	System.out.println();
            	//continue;
            }
            //=================// Qtde. de arquivos alterados //=================//
        	int numIntersec = 0;
            try
            {
            	//=================// Qtde. de desenvolvedores //=================//
            	MergeCommits mergeCommits = new MergeCommits(hashMerge, repos.getProject());
                mergeCommits.setHashBase(merge.getHashBase());
                mergeCommits.setParents(merge.getParents()[0], merge.getParents()[1]);
                mergeCommitsDao.setCommittersOnBranch(mergeCommits);
                
                int committers1 = 0;
                int committers2 = 0;
                List<String> committersR1 = new ArrayList<String>();
                
                for(Committer c : mergeCommits.getCommittersBranchOne())
                {
                	++committers1;
                	
                	committersR1.add(c.getNameEmail());
                }
                
                for(Committer c : mergeCommits.getCommittersBranchTwo())
                {
                	++committers2;
                	
                	if(committersR1.contains(c.getNameEmail()))
                		numIntersec += 1;
                }
                
                if(committers1 == committers2 && committers1 == numIntersec)
                	numIntersec = 2;
                else if(numIntersec > 0)
                	numIntersec = 1;
            }
            catch(NullPointerException npe)
            {}
            
            //================// Conflitos //==============//
            int arquivos = 0;
            int contAmbos = 0, contNull = 0;
        	String descricaoArquivos = "";
            if(RevisionAnalyzer.hasConflict(repos.getProject().toString(), merge.getParents()[0], merge.getParents()[1])) 
            {
            	System.out.println(" - Conflito");
            	
            	//=======// Arquivos //=======//
            	List<String> arquivosList = RunGit.getListOfResult("git diff --name-only --diff-filter=U", repos.getProject());
            	arquivos = arquivosList.size();
            
                if(numIntersec > 0)
                {
                	//Gerando a lista de arquivos em conflito
                	HashMap<String, ArquivoConflito> ac = new HashMap<String, ArquivoConflito>();
                	for(String arquivo : arquivosList)
                	{
                		ArquivoConflito aux = new ArquivoConflito(arquivo);
                		aux.deletado = checkDeletedFile(aux.nome);                		
                		ac.put(arquivo, aux);
                	}
                	System.out.println("Arquivos em conflito = " + ac.size());
                	
                	identificarCommitConflito(merge.getHashBase(), merge.getParents()[0], ac, 0);
                	identificarCommitConflito(merge.getHashBase(), merge.getParents()[1], ac, 1);
                	
                	
                	//Verificando se o mesmo causou o conflito dos dois lados
                	
                	for (Entry<String, ArquivoConflito> entry : ac.entrySet()) 
                	{
                		System.out.printf("%s: %s - %s\n", entry.getValue().nome, entry.getValue().causador[0], entry.getValue().causador[1]);
                		if(entry.getValue().causador[0] != null && entry.getValue().causador[0].equals(entry.getValue().causador[1]))
                			contAmbos += 1;
                		else if(entry.getValue().causador[0] == null || entry.getValue().causador[1] == null)
                			contNull += 1;
                		else
                			descricaoArquivos += String.format("%s,%s,%s;", entry.getValue().nome, entry.getValue().causador[0], entry.getValue().causador[1]);
                	}
                	System.out.println();
                }
                linesStr += merge.getHash()+","+numIntersec+","+arquivos+","+contAmbos+","+contNull+","+descricaoArquivos+"/x/";
            }
            else
            	System.out.println();
            
            //linesStr += merge.getHash()+","+numIntersec+","+arquivos+","+contAmbos+","+contNull+","+descricaoArquivos+"/x/";
       /* }*/
        //Export.toCSV2(repos, header, linesStr.substring(0, linesStr.length()-3).split("/x/"));
	}
	
	/*
	 * git diff HASH arquivo
	 * Quando os marcadores de conflito são criados e é feito o diff com o commit, se a linha foi modificada
	 * no commit do diff, a linha fica sem o +
	 * */
	
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
				System.out.println("git diff " + hash + " " + linha);
				List<String> linhasDiff = RunGit.getListOfResult("git diff " + hash + " " + linha, repos.getProject());
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
					System.out.println("git diff --summary HEAD.." + hash + " " + linha);
					String arquivoDeletado = RunGit.getResult("git diff --summary HEAD.." + hash + " " + linha, repos.getProject());
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
	
	public static boolean checkDeletedFile(String file)
	{
		System.out.println("git diff " + file);
		return RunGit.getResult("git diff " + file, repos.getProject()).contains("* Unmerged path " + file);
	}
	
	public static String getCommitAuthor(String hash)
	{
		return RunGit.getResult("git log --pretty=format:%an -1 " + hash, repos.getProject());
	}
}

/*Alguns comandos Git:
git log --reverse --ancestry-path HASH^..origin/BRANCH --pretty=format:%ci
git log --reverse --ancestry-path f12b46a0ceaf2925a75fc3efd45ff2d0e60640d6^..refs/remotes/origin/Ramo2 --pretty=format:%ci
git show-ref --head
git rev-list ANCESTOR_HASH ^..PARENT_HASH
git log --pretty=format:%HX%ci ANCESTOR_HASH PARENT_HASH*/
//=================================================//
