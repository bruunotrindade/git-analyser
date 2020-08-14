/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.tipmerge.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has information about each merge, as: committers on b1, b2, both branches and previous history (and all information in Merge)
 * @author j2cf, Catarina
 */
public class MergeCommits extends Merge{
	
	private List<Committer> cmterBranchOne;
	private List<Committer> cmterBranchTwo;
	private List<Committer> cmterBothBranches;
	private List<Committer> cmterPreviousHistory;

	public MergeCommits(String hashOfMerge, File pathToRepository) {
		super(hashOfMerge, pathToRepository);
	}
        
    public MergeCommits(String hash1, String hash2, File file){
        super(hash1, hash2, file);
    }

	/**
	 * @return the cmterPreviousHistory
	 */
	public List<Committer> getCommittersPreviousHistory() {
		return cmterPreviousHistory;
	}

	/**
	 * @param cmterPreviousHistory the cmterPreviousHistory to set
	 */
	public void setCommittersPreviousHistory(List<Committer> cmterPreviousHistory) {
		this.cmterPreviousHistory = cmterPreviousHistory;
	}

	/**
	 * @return the cmterBranchOne
	 */
	public List<Committer> getCommittersBranchOne() {
		return cmterBranchOne;
	}

	/**
	 * @param cmterBranchOne the cmterBranchOne to set
	 */
	public void setCommittersBranchOne(List<Committer> cmterBranchOne) {
		this.cmterBranchOne = cmterBranchOne;
	}

	/**
	 * @return the cmterBranchTwo
	 */
	public List<Committer> getCommittersBranchTwo() {
		return cmterBranchTwo;
	}

	/**
	 * @param cmterBranchTwo the cmterBranchTwo to set
	 */
	public void setCommittersBranchTwo(List<Committer> cmterBranchTwo) {
		this.cmterBranchTwo = cmterBranchTwo;
	}

	/**
	 * @return the cmterBothBranches
	 */
	public List<Committer> getCommittersBothBranches() {
		if (cmterBothBranches == null){
			this.getCommittersBranchOne().stream().forEach((cmter1) -> {
				for(Committer cmter2 : this.getCommittersBranchTwo()){
					if (cmter1.equals(cmter2)){
						if (cmter1.getCommits() < cmter2.getCommits())
							this.addCommitterBothBranches(cmter1);
						else
							this.addCommitterBothBranches(cmter2);
					}
				}
			});
		}
		return cmterBothBranches;
	}

	/**
	 * @param cmterBothBranches the cmterBothBranches
	 */
	public void setCommittersBothBranches(List<Committer> cmterBothBranches) {
		this.cmterBothBranches = cmterBothBranches;
	}
	
	public void addCommitterBothBranches(Committer cmterBothBranches){
		if(this.cmterBothBranches == null)
			this.cmterBothBranches = new ArrayList<>();
		this.cmterBothBranches.add(cmterBothBranches);
	}

	public List<Conciliator> getConciliators() {
		List<Conciliator> conciliatorCandidates = new ArrayList<>();
		List<Committer> committersOnBranch2 = new ArrayList<Committer>(this.getCommittersBranchTwo());
		
		for(Committer committer1 : this.getCommittersBranchOne()){
			Conciliator conciliator = new Conciliator(committer1);
			conciliator.setCommitsBranch1(committer1.getCommits());
			
			Committer cmterTemp = null;
			for(Committer committer2 : committersOnBranch2){
				if (committer1.equals(committer2)){
					conciliator.setCommitsBranch2(committer2.getCommits());
					conciliator.setCommitsBoothBranchs(Math.min(conciliator.getCommitsBranch1(), conciliator.getCommitsBranch2()));
					cmterTemp = committer2;
					break;					
				}
			}if(cmterTemp != null) committersOnBranch2.remove(cmterTemp);
				conciliatorCandidates.add(conciliator);
		}
		if (committersOnBranch2.size() > 0)
			committersOnBranch2.stream().map((committer2) -> {
				Conciliator conciliator = new Conciliator(committer2);
				conciliator.setCommitsBranch2(committer2.getCommits());
				return conciliator;
			}).forEach((conciliator) -> {
				conciliatorCandidates.add(conciliator);
			});
		
		//Now it will be included the list of all committers who are "out of branches" in previous history
		this.getCommittersPreviousHistory().stream().forEach((cmter) -> {
			boolean isOnBranches = false;
			for (Conciliator conciliator : conciliatorCandidates){
				if (conciliator.getCommitter().equals(cmter)){
					conciliator.setCommitsPreviousHistory(cmter.getCommits());
					isOnBranches = true;
				}
			}
			if (!isOnBranches) {
				Conciliator newConciliator = new Conciliator(cmter);
				newConciliator.setCommitsPreviousHistory(cmter.getCommits());
				conciliatorCandidates.add(newConciliator);
			}
		});
		return conciliatorCandidates;
	}
	
	//this method verifies that the merge is branches. It was decided that whenever a merge has more than 2 people in each branch, it is merge branches. This leads to a case of never be a false positive, but there may be false negative.
	public boolean isMergeOfBranches(){
		return (this.getCommittersBranchOne().size() >= 2) && (this.getCommittersBranchTwo().size() >= 2);
	}
	
	public boolean hasDifferentDevelopers(){
		if (this.getCommittersBranchOne().isEmpty() || this.getCommittersBranchTwo().isEmpty())
			return false;
		if (this.getCommittersBranchOne().size() == 1 && this.getCommittersBranchTwo().size() == 1)
			return !this.getCommittersBranchOne().get(0).equals(this.getCommittersBranchTwo().get(0));
		return true;
	}
}
