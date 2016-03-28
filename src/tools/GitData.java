package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.jgit.revwalk.RevCommit;

// data for each commit
class CommitData {
	String hashCode;
	String author;
	String email;
	String solrDate;
	String day;
	String month;
	String year;
	String message;
	int insertions;
	int deletions;
	
	CommitData() {
	
	}
	
	public void setSolrDate(String sd) {
		this.solrDate = sd;
	}
	
	public String getSolrDate() {
		return this.solrDate;
	}
	
	public void setDay(String d) {
		this.day = d;
	}
	
	public String getDay() {
		return this.day;
	}
	
	public void setMonth(String m) {
		this.month = m;
	} 
	
	public String getMonth() {
		return this.month;
	}
	
	public void setYear(String y) {
		this.year = y;
	}
	
	public String getYear() {
		return this.year;
	}
	
	public void setHashCode(String hc) {
		this.hashCode = hc;
	}
	
	public String getHashCode() {
		return this.hashCode;
	}
	
	public void setAuthor(String a) {
		this.author = a;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setEmail(String e) {
		this.email = e;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setMessage(String m) {
		this.message = m;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void setInsertions(int i) {
		this.insertions = i;
	}
	
	public int getInsertions() {
		return this.insertions;
	}
	
	public void setDeletions(int d) {
		this.deletions = d;
	}
	
	public int getDeletions() {
		return this.deletions;
	}
	
}

// data for each file
class JavaFile {
	String fileLocation;
	int numberOfLines;
	int numberOfCharacters;
	List<CommitData> commitDataList;
	Set<String> uniqueAuthors;
	Set<String> uniqueEmails;
	
	JavaFile(String n) {
		commitDataList = new ArrayList<>();
		uniqueAuthors = new HashSet<>();
		uniqueEmails = new HashSet<>();
		fileLocation = n;
	}

	public String getFileLocation() {
		return this.fileLocation;
	}
	
	public void setUniqueAuthors(List<String> set) {
		uniqueAuthors = new HashSet<>(set);
	}
	
	public Set<String> getUniqueAuthors() {
		return this.uniqueAuthors;
	}
	
	public void setUniqueEmails(List<String> set) {
		uniqueEmails = new HashSet<>(set);
	}
	
	public Set<String> getUniqueEmails() {
		return this.uniqueEmails;
	}
	
	public List<CommitData> getCommitDataList() {
		return this.commitDataList;
	}
}

// data for all files in a repo
public class GitData {

	Map<String, String> hashCodePairs;		// stores pairs of commit hash codes
	List<JavaFile> javaFileList;
	
	public GitData() {
		hashCodePairs = new LinkedHashMap<>();
		javaFileList = new ArrayList<>();
	}
	
	public void addHashCodePairsToMap(List<RevCommit> commitHistory) {
		this.hashCodePairs.put("--root", commitHistory.get(0).name());
		
		for(int i = 0; i < commitHistory.size() - 1; i++) {
			this.hashCodePairs.put(commitHistory.get(i).name(), commitHistory.get(i + 1).name());
		}			
	}
	
	public List<JavaFile> getJavaFileList() {
		return this.javaFileList;
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
							if(j.fileLocation.equals(nameParts[nameParts.length - 1])) {
								for(CommitData c : j.commitDataList) {
									if(c.hashCode.equals(entry.getValue())) {
										c.setInsertions(Integer.parseInt(parts[0]));
										c.setDeletions(Integer.parseInt(parts[1]));
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

	public static int getLineCountOfFile(String javaFileName) {
		File file = new File(javaFileName);
		
		int count = 0;
		try {			
			Scanner scanner = new Scanner(file);
		
			while (scanner.hasNext()) {
				count++;
				scanner.nextLine();
			}
			
			scanner.close();
			
		} catch (Exception e) {
			System.out.println("Data is null!");
		}
		
		return count;	
	}
	
	public static int getCharacterCountOfFile(String javaFileName) {
		File file = new File(javaFileName);
		
		int count = 0;
		
		try {			
			Scanner scanner = new Scanner(file);
		
			while (scanner.hasNextLine()) {
				count += scanner.nextLine().length();
			}
			
			scanner.close();
			
		} catch (Exception e) {
			System.out.println("Data is null!");
		}
		
		return count;
	}
	
	public void getCommitDataPerFile(String directoryLocation, String javaFileName) throws IOException, ParseException {				
		JavaFile javaFileObject = new JavaFile(javaFileName);
		
		File dir = new File(directoryLocation);
		
		// get number of lines in a file
		javaFileObject.numberOfLines = getLineCountOfFile(javaFileName);
		
		// get number of characters in a file
		javaFileObject.numberOfCharacters = getCharacterCountOfFile(javaFileName);
		
		// get all commits of a single file
		ProcessBuilder pb3 = new ProcessBuilder("git", "log", "--follow", javaFileName);
		pb3.directory(dir);
			
		Process proc3 = pb3.start();
		InputStreamReader isr3 = new InputStreamReader(proc3.getInputStream());
		BufferedReader br3 = new BufferedReader(isr3);
						
		List<String> hashCodeList = new ArrayList<>();
		List<String> authorList = new ArrayList<>();
		List<String> emailList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		List<String> messageList = new ArrayList<>();
			
		String s = null;
		while((s = br3.readLine()) != null) {
				
			if(s.startsWith("commit")) {
				hashCodeList.add(s.split(" ")[1]);
			}
			else if(s.startsWith("Author")) {
				String[] parts = s.split(" ");
				List<String> authorParts = new ArrayList<>();
					
				for(int i = 1; i < parts.length; i++) {
					if(!parts[i].startsWith("<")) {
						authorParts.add(parts[i]);
					}
					else {
						emailList.add(parts[i].replace("<", "").replace(">", ""));
					}
				}
				authorList.add(String.join(" ", authorParts).trim());
			}
			else if (s.startsWith("Date")) {
				String[] parts = s.split(" ");
				List<String> dateParts = new ArrayList<>();
				for(int i = 3; i < parts.length; i++) {
					dateParts.add(parts[i]);
				}
					
				dateList.add(String.join(" ", dateParts).trim());
			}
			else if (!s.isEmpty()){
				messageList.add(s.trim());
			}
		}
		
		br3.close();
		isr3.close();
		proc3.destroy();
		
		
		for(int i = 0; i < hashCodeList.size(); i++) {
			CommitData cd = new CommitData();
			
			// format date
			Date javaDate = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").parse(dateList.get(i));
			cd.setSolrDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(javaDate));
			cd.setDay(new SimpleDateFormat("dd").format(javaDate));
			cd.setMonth(new SimpleDateFormat("MM").format(javaDate));
			cd.setYear(new SimpleDateFormat("yyyy").format(javaDate));
			
			// add everything else
			cd.setAuthor(authorList.get(i));
			cd.setEmail(emailList.get(i));
			cd.setHashCode(hashCodeList.get(i));
			cd.setMessage(messageList.get(i));
			
			// add object to list
			javaFileObject.commitDataList.add(cd);
		}
		
		javaFileObject.setUniqueAuthors(authorList);
		javaFileObject.setUniqueEmails(emailList);
		javaFileList.add(javaFileObject);
		
	}
	
}
