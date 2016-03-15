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
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;

class CommitData {
	String hashCode;
	String author;
	String email;
	String date;
	String message;
	int insertions;
	int deletions;
	
	CommitData(String h, String a, String e, String d, String m) {
		hashCode = h;
		author = a;
		email = e;
		date = d;
		message = m;		
	}
}

class JavaFile {
	String name;
	int numberOfLines;
	int numberOfCharacters;
	List<CommitData> commitDataList;
	
	JavaFile(String n) {
		commitDataList = new ArrayList<>();
		name = n;
	}
}

public class GitData {

	List<CommitObject> commitObjectList;	// stores author name, email, commit message, date of commit
	Map<String, String> hashCodePairs;		// stores pairs of commit hash codes
	List<JavaFile> javaFileList;
	
	public GitData() {
		commitObjectList = new ArrayList<>();
		hashCodePairs = new LinkedHashMap<>();
		javaFileList = new ArrayList<>();
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
	
	/*
	 * gets insertions and deletions for all commits
	 * parses and adds insertions/deletions based on hash code and file name
	 */
	public void getDiff(String directoryLocation, Map<String, String> hashCodePairs) throws IOException {					
		for(Map.Entry<String, String> entry : hashCodePairs.entrySet()) {								
			ProcessBuilder pb = new ProcessBuilder("git", "diff-tree", "--numstat", entry.getKey(), entry.getValue(), directoryLocation);
			pb.directory( new File(directoryLocation) );
		
			Process proc = pb.start();
			InputStreamReader isr = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			String s = null;
			while((s = br.readLine()) != null) {
				if(s.endsWith(".java")){
					String[] parts = s.split("\t");
					
					if(parts.length == 3) { // for some reason, git diff-tree --numstat --root $3 also returns hash code of commit
						String[] nameParts = parts[2].split("/"); 
					
						for(JavaFile j : javaFileList) {
							if(j.name.equals(nameParts[nameParts.length - 1])) {
								for(CommitData c : j.commitDataList) {
									if(c.hashCode.equals(entry.getValue())) {
										c.insertions = Integer.parseInt(parts[0]);
										c.deletions = Integer.parseInt(parts[1]);
									}
								}
							}
						}
					}
				}
			}
			
			br.close();
			isr.close();
			proc.destroy();
		}
	}

	public void getCommitDataPerFile(String directoryLocation, String javaFileName) throws IOException {
		String[] name = javaFileName.split("/");
		
		JavaFile javaFileObject = new JavaFile(name[name.length - 1]);
				
		File dir = new File(directoryLocation);
		
		// get number of lines in a file
		ProcessBuilder pb1 = new ProcessBuilder("wc", "-l", javaFileName);
		pb1.directory(dir);
		
		Process proc1 = pb1.start();
		
		InputStreamReader isr1 = new InputStreamReader(proc1.getInputStream());
		BufferedReader br1 = new BufferedReader(isr1);
		
		String s1 = null;
		while((s1 = br1.readLine()) != null) {
			javaFileObject.numberOfLines = Integer.parseInt(s1.split(" ")[0]);
		}
		
		br1.close();
		isr1.close();
		proc1.destroy();	
		
		// get number of characters in a file
		ProcessBuilder pb2 = new ProcessBuilder("wc", "-c", javaFileName);
		pb2.directory(dir);
		
		Process proc2 = pb2.start();
		
		InputStreamReader isr2 = new InputStreamReader(proc2.getInputStream());
		BufferedReader br2 = new BufferedReader(isr2);
		
		String s2 = null;
		while((s2 = br2.readLine()) != null) {
			javaFileObject.numberOfCharacters = Integer.parseInt(s2.split(" ")[0]);
		}
		
		br2.close();
		isr2.close();
		proc2.destroy();
		
		// get all commits of a single file
		ProcessBuilder pb3 = new ProcessBuilder("git", "log", "--follow", javaFileName);
		pb3.directory(dir);
			
		Process proc3 = pb3.start();
		InputStreamReader isr3 = new InputStreamReader(proc3.getInputStream());
		BufferedReader br3 = new BufferedReader(isr3);
						
		List<String> hashCode = new ArrayList<>();
		List<String> author = new ArrayList<>();
		List<String> email = new ArrayList<>();
		List<String> date = new ArrayList<>();
		List<String> message = new ArrayList<>();
			
		String s = null;
		while((s = br3.readLine()) != null) {
				
			if(s.startsWith("commit")) {
				hashCode.add(s.split(" ")[1]);
			}
			else if(s.startsWith("Author")) {
				String[] parts = s.split(" ");
				List<String> authorParts = new ArrayList<>();
					
				for(int i = 1; i < parts.length; i++) {
					if(!parts[i].startsWith("<")) {
						authorParts.add(parts[i]);
					}
					else {
						email.add(parts[i].replace("<", "").replace(">", ""));
					}
				}
				author.add(String.join(" ", authorParts).trim());
			}
			else if (s.startsWith("Date")) {
				String[] parts = s.split(" ");
				List<String> dateParts = new ArrayList<>();
				for(int i = 3; i < parts.length; i++) {
					dateParts.add(parts[i]);
				}
					
				date.add(String.join(" ", dateParts).trim());
			}
			else if (!s.isEmpty()){
				message.add(s.trim());
			}
		}
			
		for(int i = 0; i < hashCode.size(); i++) {
			CommitData c = new CommitData(hashCode.get(i), author.get(i), email.get(i), date.get(i), message.get(i)); 
			javaFileObject.commitDataList.add(c);
		}

		javaFileList.add(javaFileObject);
			
		br3.close();
		isr3.close();
		proc3.destroy();	
	}
	
	public static void main(String[] args) throws IOException, NoWorkTreeException, GitAPIException {	
		//String directoryLocation = args[0];
		/*
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";
		
		Git git = Git.open( new File (directoryLocation + ".git") );
				
		GitData gitData = new GitData();

		
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory);
			
		for(RevCommit commit : commitHistory) {
			gitData.commitObjectList.add(new CommitObject(getAuthorOfCommit(commit), getAuthorEmailOfCommit(commit), getHashCodeOfCommit(commit), getCommitMessage(commit), formatDate(getDateOfCommit(commit))));
		}
			
		gitData.addHashCodePairsToMap(commitHistory);
		gitData.getDiff(directoryLocation, gitData.hashCodePairs);
		
		git.close();	
		*/
	}
}
