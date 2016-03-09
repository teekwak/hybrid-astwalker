package tools;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.FileUtils;

public class GitHubData {

	List<CommitObject> commitObjectList;	// stores author name, email, commit message, date of commit
	Map<String, String> hashCodePairs;		// stores pairs of commit hash codes

	public GitHubData() {
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

	public void addToList(CommitObject obj) {
		commitObjectList.add(obj);
	}
	
	public List<CommitObject> getCommitObjectList() {
		return commitObjectList;
	}
	
	public Map<?, ?> getHashCodePairs() {
		return hashCodePairs;
	}
	
	// prints all change types for each commit in a repository
	public void printChangeType(Git git, List<RevCommit> commitHistory) throws IncorrectObjectTypeException, IOException {
		DiffFormatter df = new DiffFormatter( new ByteArrayOutputStream() );
			df.setRepository(git.getRepository());
			df.setDiffComparator(RawTextComparator.DEFAULT);
			df.setDetectRenames(true);
		
		RevCommit prev = null;
		RevCommit current = null;
		
		System.out.println("\tadd\tcopy\tdelete\tmodify\trename\n");
		
		for(RevCommit commit : commitHistory) {
			
			int add = 0;
			int copy = 0;
			int delete = 0;
			int modify = 0;
			int rename = 0;
			
			current = commit;
			
			List<DiffEntry> entries = null;
			
			if(prev == null) {
				RevWalk rw = new RevWalk(git.getRepository());
				entries = df.scan(new EmptyTreeIterator(),
				        new CanonicalTreeParser(null, rw.getObjectReader(), current.getTree()));
				rw.close();
			}
			else {				
				entries = df.scan(prev.getTree(), current.getTree());	
			}
			
			for(DiffEntry d : entries) {
				System.out.println(d.toString());
				switch(d.getChangeType()) {
					case ADD:
						add++;
						break;
					case DELETE:
						delete++;
						break;
					case MODIFY:
						modify++;
						break;
					case COPY:
						copy++;
						break;
					case RENAME:
						rename++;
						break;
				}
			}
			
			prev = current;
			
			System.out.println("\t" + add + "\t" + copy + "\t" + delete + "\t" + modify + "\t" + rename);
			System.out.println("---------------------------------------------------------------");
		}	

		df.close();
		
	}
	
	public void addHashCodePairsToMap(List<RevCommit> commitHistory) {
		// shouldn't first pair be (root --> first commit)?
		this.hashCodePairs.put("--root", commitHistory.get(0).name());
		
		// puts rest of commit pairs into map
		for(int i = 0; i < commitHistory.size() - 1; i++) {
			this.hashCodePairs.put(commitHistory.get(i).name(), commitHistory.get(i + 1).name());
		}			
	}

	// gets number of additions and deletions between two commits for each file using bash script
	public void runBashScript(String directoryLocation, Map<String, String> hashCodePairs) throws IOException {
		File script = new File("scripts/gitDiffScript.sh"); // need script to be in scripts folder

		for(Map.Entry<String, String> entry : hashCodePairs.entrySet()) {
			
			Process proc = Runtime.getRuntime().exec(script.getAbsolutePath() + " " + directoryLocation + " " + entry.getKey() + " " + entry.getValue());
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
	
	public void runGitCommand(String directoryLocation, Map<String, String> hashCodePairs) throws IOException {	
		System.out.println(directoryLocation);
		
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
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";

		//String directoryLocation = "/home/kwak/Desktop/didactic-octo-capsicum/";
		
		Git git = Git.open( new File (directoryLocation + ".git") );

		GitHubData gitHubData = new GitHubData();

		// all commits from latest to earliest
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory); // all commits from earliest to latest
			
		for(RevCommit commit : commitHistory) {
			gitHubData.commitObjectList.add(new CommitObject(getAuthorOfCommit(commit), getAuthorEmailOfCommit(commit), getHashCodeOfCommit(commit), getCommitMessage(commit), formatDate(getDateOfCommit(commit))));
		}
			
		gitHubData.addHashCodePairsToMap(commitHistory);
		//gitHubData.runBashScript(directoryLocation, gitHubData.hashCodePairs);	
		gitHubData.runGitCommand(directoryLocation, gitHubData.hashCodePairs);
		
		git.close();
	
	}
}
