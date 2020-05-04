package br.uff.ic.gems.tipmerge;

import java.util.List;
import java.util.concurrent.Callable;

import br.uff.ic.gems.tipmerge.dao.MergeCommitsDao;
import br.uff.ic.gems.tipmerge.dao.MergeFilesDao;
import br.uff.ic.gems.tipmerge.model.MergeFiles;
import br.uff.ic.gems.tipmerge.model.Repository;
import br.uff.ic.gems.tipmerge.util.RunGit;
import br.uff.ic.gems.tipmerge.util.Statistics;

public class Multithreading implements Callable<String>
{
	public Repository repos = null;
	public List<String> merges = null;
	
	public Multithreading(Repository rep, List<String> merges) 
	{
		repos = rep;
		this.merges = merges;
	}
	
	public String call() throws Exception 
	{
		MergeFilesDao mergeFilesDao = new MergeFilesDao();

        String linesTime = "";
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
		return linesTime;
	}
	
	//Recuperar timestamp do merge
	public String getMergeTimestamp(String hashMerge)
	{
		return RunGit.getResult("git log -1 --pretty=format:%ci " + hashMerge, repos.getProject())/*.substring(0, 19)*/;
	}
	
	//Recuperar o hash do commit após o base
	public String getAncestorNextHash(String hashBase, String hashParent)
	{
		List<String> beginLineTotal = RunGit.getListOfResult("git log --first-parent --pretty=format:%HX%ciX%ct " + hashBase + ".." + hashParent, repos.getProject());
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
	public String getIsolationTimeBetweenCommits(String hashA, String hashB)
	{
		long commitAtime = getCommitUnixTime(hashA), commitBtime = getCommitUnixTime(hashB);
        return String.format("%.7f", Statistics.timeToDays(commitBtime-commitAtime)).replace(",", ".");
	}
	
	//Recuperar horário do commit em Unix
	public long getCommitUnixTime(String hash)
	{
		String parentData = RunGit.getResult("git log -1 --pretty=format:%ciX%ct " + hash, repos.getProject()),  parts2[] = parentData.split("X");
    	return Long.parseLong(parts2[1]);
	}
	
	//Converter para GMT-00 ou UTC
	public String convertToDefaultTimeZone(String Date) 
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
}