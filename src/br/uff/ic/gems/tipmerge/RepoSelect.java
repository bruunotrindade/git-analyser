package br.uff.ic.gems.tipmerge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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


public class RepoSelect
{	
	public static Repository repos = null;
	public static String nome;
	public static int v2;
	
	public static void main(String[] args)
	{
		ArrayList<Repositorio> reps = new ArrayList<Repositorio>();
		String header[] = {"Projeto", "Linguagem", "Merges (Total)"};
		
		for(int i = 0; i < args.length; i++)
		{
			System.out.println("Repositorio: " + args[i]);
			repos = new RepositoryDao(new File(args[i])).getRepository();
			RepositoryDao rdao = new RepositoryDao(repos.getProject());
	        rdao.setGeneralBasicDatas(repos);
			reps.add(runAnalysis());
			System.out.println("Terminado\n");
		}
		
		try
		{
			FileWriter fw = new FileWriter(new File("Analise.csv"));
			
			//Header:
			fw.write(String.join(",", header)+"\n");
			
			//Lines
			for(Repositorio rep : reps)
				fw.write(rep.nome+","+rep.linguagem+","+rep.merges+"\n");
			
			fw.close();
		}
		catch(IOException ioe)
		{
			System.out.println("Export failed!");
		}
	}
	
	public static Repositorio runAnalysis()
	{	
		MergeFilesDao mergeFilesDao = new MergeFilesDao();
        List<String> merges = repos.getListOfMerges();
        int mergesCount = merges.size(), cont = 0, mergesACount = 0, conflitos = 0;
        return new Repositorio(repos.getName(), "", 0, mergesCount, 0);
        /*for(String hashMerge : merges) 
        {
        	System.out.printf("\t-> Merge %02d/%02d", ++cont, mergesCount);
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
            {continue;}
            
            System.out.println();
            mergesACount += 1;
            
            if(RevisionAnalyzer.hasConflict(repos.getProject().toString(), merge.getParents()[0], merge.getParents()[1])) 
            	conflitos += 1;
        }
        return new Repositorio(repos.getName(), "", conflitos, mergesCount, mergesACount);*/
	}
}
