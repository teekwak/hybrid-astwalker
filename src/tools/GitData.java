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
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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
		this.insertions = 0;
		this.deletions = 0;
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
	
	public void setNumberOfLines(int n) {
		this.numberOfLines = n;
	}
	
	public int getNumberOfLines() {
		return this.numberOfLines;
	}
	
	public void setNumberOfCharacters(int n) {
		this.numberOfCharacters = n;
	}
	
	public int getNumberOfCharacters() {
		return this.numberOfCharacters;
	}
}

// data for all files in a repo
public class GitData {

	JavaFile javaFile;
	
	public GitData() {
		javaFile = null;
	}
	
	public JavaFile getJavaFile() {
		return this.javaFile;
	}

	public static int getLineCountOfFile(String javaFileName) {
		File file = new File(javaFileName);
		
		int count = 0;
		try {			
			Scanner scanner = new Scanner(file, "ISO-8859-1");
		
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
			Scanner scanner = new Scanner(file, "ISO-8859-1");
		
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
		javaFileObject.setNumberOfLines(getLineCountOfFile(javaFileName));
		
		// get number of characters in a file
		javaFileObject.setNumberOfCharacters(getCharacterCountOfFile(javaFileName));
		
		// get all commits of a single file
        ProcessBuilder pb = new ProcessBuilder("git", "log", "--reverse", "--numstat", "--format=format:Commit: %H%nAuthor: %an%nEmail: %ae%nDate: %ad%nMessage: %s", javaFileName);
		pb.directory(dir);
			
		Process proc = pb.start();
		InputStreamReader isr = new InputStreamReader(proc.getInputStream());
		BufferedReader br = new BufferedReader(isr);
						
		List<String> hashCodeList = new ArrayList<>();
		List<String> authorList = new ArrayList<>();
		List<String> emailList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		List<String> messageList = new ArrayList<>();
		List<Integer> insertionList = new ArrayList<>();
		List<Integer> deletionList = new ArrayList<>();
								
		String s = null;
				
		while((s = br.readLine()) != null) {				
            if(s.startsWith("Commit") && hashCodeList.size() == messageList.size() && hashCodeList.size() == insertionList.size()) {
            	hashCodeList.add(s.split(" ")[1]);
            }
            else if(s.startsWith("Commit") && hashCodeList.size() == messageList.size() && hashCodeList.size() != insertionList.size()) {
            	insertionList.add(0);
            	deletionList.add(0);
            	hashCodeList.add(s.split(" ")[1]);
            }
            else if(s.startsWith("Commit") && hashCodeList.size() != messageList.size() && hashCodeList.size() == insertionList.size()) {
                messageList.add("");
                hashCodeList.add(s.split(" ")[1]);
            }
            else if(s.startsWith("Commit") && hashCodeList.size() != messageList.size() && hashCodeList.size() != insertionList.size()) {
                messageList.add("");
            	insertionList.add(0);
            	deletionList.add(0);
            	hashCodeList.add(s.split(" ")[1]);
            }
            else if(s.startsWith("Author")) {
            	try {
                    authorList.add(s.split(" ")[1]);            		
            	} catch (ArrayIndexOutOfBoundsException e) {
            		authorList.add("");
            	}
            }
            else if(s.startsWith("Email")) {
            	try {
                    emailList.add(s.split(" ")[1]);            		
            	} catch (ArrayIndexOutOfBoundsException e) {
            		emailList.add("");
            	}
            }
            else if (s.startsWith("Date")) {
                dateList.add(s.split(" ", 2)[1]);
            }
            else if (s.startsWith("Message")){
            	try {
                    messageList.add(s.split(" ", 2)[1]);            		
            	} catch (ArrayIndexOutOfBoundsException e) {
            		messageList.add("");
            	}
            }
            else if(s.length() > 0 && Character.isDigit(s.charAt(0))) {
                String[] numParts = s.split("\t");
                insertionList.add(Integer.valueOf(numParts[0]));
                deletionList.add(Integer.valueOf(numParts[1]));   
            }
		}
				
		br.close();
		isr.close();
		proc.destroy();
		
		int hashCodeListSize = hashCodeList.size();
		for(int i = 0; i < hashCodeListSize; i++) {
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
			
			try {
				cd.setMessage(messageList.get(i));				
			} catch (IndexOutOfBoundsException e) {
				cd.setMessage("");
			}
			
			try {
				cd.setInsertions(insertionList.get(i).intValue());				
			} catch (IndexOutOfBoundsException e) {
				
			}

			try {
				cd.setInsertions(insertionList.get(i).intValue());				
			} catch (IndexOutOfBoundsException e) {
				cd.setInsertions(0);	
			}
			
			try {
				cd.setDeletions(deletionList.get(i).intValue());		
			} catch (IndexOutOfBoundsException e) {
				cd.setDeletions(0);
			}			
						
			// add object to list
			javaFileObject.commitDataList.add(cd);
		}
		
		javaFileObject.setUniqueAuthors(authorList);
		javaFileObject.setUniqueEmails(emailList);
		
		// set GitData javaFile
		javaFile = javaFileObject;
		
	}	
}
