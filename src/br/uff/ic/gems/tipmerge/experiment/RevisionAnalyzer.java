/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.experiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.uff.ic.gems.tipmerge.util.RunGit;

/**
 *
 * @author j2cf
 */
public class RevisionAnalyzer {

    public static boolean analyze(String revisionSHA, String repositoryPath) {
        /*
         Revision revision = new Revision();

         revision.setSha(revisionSHA);

         Git.reset(repositoryPath);
         List<String> parents = Git.getParents(repositoryPath, revisionSHA);

         if (parents.size() == 2) {

         String leftParent = parents.get(0);
         String rightParent = parents.get(1);
         //Filling parents
         revision.setLeftSha(leftParent);
         revision.setRightSha(rightParent);

         String mergeBase = Git.getMergeBase(repositoryPath, leftParent, rightParent);
         //Filling base revision
         revision.setBaseSha(mergeBase);

         Git.checkout(repositoryPath, leftParent);

         List<String> mergeOutput = Git.merge(repositoryPath, rightParent, false, true);

         if (MergeStatusAnalizer.isConflict(mergeOutput)) {
                
         revision.setStatus(MergeStatus.CONFLICTING);

         List<String> conflictedFiles = Git.conflictedFiles(repositoryPath);
         revision.setNumberConflictingFiles(conflictedFiles.size());
         int javaFiles = 0;

         List<ConflictingFile> conflictingFiles = new ArrayList<>();
                
         for (String conflictedFile : conflictedFiles) {
         try {
         ConflictingFile conflictingFile = ConflictingFileAnalyzer.analyze(conflictedFile, repositoryPath, leftParent, rightParent, revisionSHA);
         conflictingFiles.add(conflictingFile);
         //                        try {
         //                            conflictingFileDAO.save(conflictingFile);
         //                        } catch (Exception ex) {
         //                            Logger.getLogger(RevisionAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
         //                        }
                        
         revision.getConflictingFiles().add(conflictingFile);
                        
         if (conflictingFile.isJava()) {
         javaFiles++;
         }
         } catch (IOException ex) {
         Logger.getLogger(RevisionAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
         System.out.println("Investigate!!!!!");
         }
         }

         revision.setConflictingFiles(conflictingFiles);
         revision.setNumberJavaConflictingFiles(javaFiles);

         } else if (MergeStatusAnalizer.isFastForward(mergeOutput)) {
         revision.setStatus(MergeStatus.FAST_FORWARD);
         } else {
         revision.setStatus(MergeStatus.NON_CONFLICTING);
         }

         } else {
         System.out.println("Implement!!!!!");
         }

         return revision;
         */
        return true;
    }

    public static List<String> gitReset(String path) {
        //System.out.println("vai realizar reset");
        return Git.reset(path);
        
    }

    public static boolean hasConflict(String repositoryPath, String leftParent, String rightParent) {

        //String mergeBase = Git.getMergeBase(repositoryPath, leftParent, rightParent);
    	//System.out.println("git reset --hard");
        Git.reset(repositoryPath);

        //System.out.println("git checkout " + leftParent);
        Git.checkout(repositoryPath, leftParent);

        //System.out.println("git merge --no-commit " + rightParent);
        List<String> mergeOutput = Git.merge(repositoryPath, rightParent, false, true);
        
        return MergeStatusAnalizer.isConflict(mergeOutput);
    }
    
    public static int hasConflictNum(String repositoryPath, String leftParent, String rightParent) {

        //String mergeBase = Git.getMergeBase(repositoryPath, leftParent, rightParent);
    	//System.out.println("git reset --hard");
        Git.reset(repositoryPath);

        //System.out.println("git checkout " + leftParent);
        Git.checkout(repositoryPath, leftParent);

        //System.out.println("git merge --no-commit " + rightParent);
        List<String> mergeOutput = Git.merge(repositoryPath, rightParent, false, true);
        
        if(MergeStatusAnalizer.isConflict(mergeOutput))
        	return MergeStatusAnalizer.countConflictedFiles(mergeOutput);

        return 0;
    }
}
