package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitHubData_Final {

	List<CommitObject> commitObjectList;	// stores author name, email, commit message, date of commit
	Map<String, String> hashCodePairs;		// stores pairs of commit hash codes

	public GitHubData_Final() {
		commitObjectList = new ArrayList<>();
		hashCodePairs = new LinkedHashMap<>();
	}
	
	public static String getAuthorOfCommit(RevCommit commit) {
		PersonIdent author = commit.getAuthorIdent();
		return author.getName();
	}
	
	public static String getAuthorEmailOfCommit(RevCommit commit) {
		PersonIdent author = commit.getAuthorIdent();
		return author.getEmailAddress();
	}
	
	public static String getCommitMessage(RevCommit commit) {
		return commit.getFullMessage();
	}
	
	public static Date getDateOfCommit(RevCommit commit) {
		PersonIdent author = commit.getAuthorIdent();
		return author.getWhen();
	}
	
	public static String getHashCodeOfCommit(RevCommit commit) {
		return commit.name();
	}
	
	public static String formatDate(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("MM dd yyyy hh:mm:ss");
		return dateFormat.format(date);		
	}

	public void addHashCodePairsToMap(List<RevCommit> commitHistory) {
		this.hashCodePairs.put("--root", commitHistory.get(0).name());
		
		for(int i = 0; i < commitHistory.size() - 1; i++) {
			this.hashCodePairs.put(commitHistory.get(i).name(), commitHistory.get(i + 1).name());
		}			
	}
	
	public void runGitCommand(String directoryLocation, Map<String, String> hashCodePairs) throws IOException {			
		for(Map.Entry<String, String> entry : hashCodePairs.entrySet()) {								
			ProcessBuilder pb = new ProcessBuilder("git", "diff-tree", "--numstat", entry.getKey(), entry.getValue());
			pb.directory( new File(directoryLocation) );
		
			Process proc = pb.start();
			InputStreamReader isr = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			String s = null;
			while((s = br.readLine()) != null) {
				String[] parts = s.split("\t");
				
				if(parts.length == 3) { // for some reason, git diff-tree --numstat --root $3 also returns hash code of commit
					System.out.println(parts[2] + "\nLines inserted:\t" + parts[0] + "\nLines deleted:\t" + parts[1]);	
				}
			}
			
			br.close();
			isr.close();
			proc.destroy();
		}
		
	}

	public static void main(String[] args) throws IOException, NoWorkTreeException, GitAPIException {	
		String directoryLocation = args[0];
		
		Git git = Git.open( new File (directoryLocation + ".git") );
		GitHubData_Final gitHubData = new GitHubData_Final();

		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory);
			
		for(RevCommit commit : commitHistory) {
			gitHubData.commitObjectList.add(new CommitObject(getAuthorOfCommit(commit), getAuthorEmailOfCommit(commit), getHashCodeOfCommit(commit), getCommitMessage(commit), formatDate(getDateOfCommit(commit))));
		}
			
		gitHubData.addHashCodePairsToMap(commitHistory);
		gitHubData.runGitCommand(directoryLocation, gitHubData.hashCodePairs);
		
		git.close();	
	}
}
