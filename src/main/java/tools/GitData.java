package tools;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// data for each commit
class CommitData {
	private String hashCode;
	private String author;
	private String email;
	private String solrDate;
	private String day;
	private String month;
	private String year;
	private String message;
	private int insertions;
	private int deletions;

	CommitData() {
		this.insertions = 0;
		this.deletions = 0;
	}

	void setSolrDate(String sd) {
		this.solrDate = sd;
	}

	String getSolrDate() {
		return this.solrDate;
	}

	void setDay(String d) {
		this.day = d;
	}

	String getDay() {
		return this.day;
	}

	void setMonth(String m) {
		this.month = m;
	}

	String getMonth() {
		return this.month;
	}

	void setYear(String y) {
		this.year = y;
	}

	String getYear() {
		return this.year;
	}

	void setHashCode(String hc) {
		this.hashCode = hc;
	}

	String getHashCode() {
		return this.hashCode;
	}

	void setAuthor(String a) {
		this.author = a;
	}

	String getAuthor() {
		return this.author;
	}

	void setEmail(String e) {
		this.email = e;
	}

	String getEmail() {
		return this.email;
	}

	void setMessage(String m) {
		this.message = m;
	}

	String getMessage() {
		return this.message;
	}

	void setInsertions(int i) {
		this.insertions = i;
	}

	int getInsertions() {
		return this.insertions;
	}

	void setDeletions(int d) {
		this.deletions = d;
	}

	int getDeletions() {
		return this.deletions;
	}

}

// data for each file
class JavaFile {
	private String fileLocation;
	private int numberOfLines;
	private int numberOfCharacters;
	private List<CommitData> commitDataList;
	private Set<String> uniqueAuthors;
	private Set<String> uniqueEmails;
	
	JavaFile(String n) {
		commitDataList = new ArrayList<>();
		uniqueAuthors = new HashSet<>();
		uniqueEmails = new HashSet<>();
		fileLocation = n;
	}

	String getFileLocation() {
		return this.fileLocation;
	}
	
	void setUniqueAuthors(List<String> set) {
		uniqueAuthors = new HashSet<>(set);
	}
	
	Set<String> getUniqueAuthors() {
		return this.uniqueAuthors;
	}
	
	void setUniqueEmails(List<String> set) {
		uniqueEmails = new HashSet<>(set);
	}
	
	Set<String> getUniqueEmails() {
		return this.uniqueEmails;
	}
	
	List<CommitData> getCommitDataList() {
		return this.commitDataList;
	}
	
	void setNumberOfLines(int n) {
		this.numberOfLines = n;
	}
	
	int getNumberOfLines() {
		return this.numberOfLines;
	}
	
	void setNumberOfCharacters(int n) {
		this.numberOfCharacters = n;
	}
	
//	public int getNumberOfCharacters() {
//		return this.numberOfCharacters;
//	}
}

// data for all files in a repo
class GitData {

	private JavaFile javaFile;
	
	GitData() {
		javaFile = null;
	}
	
	JavaFile getJavaFile() {
		return this.javaFile;
	}

	private static int getLineCountOfFile(String javaFileName) {
		File file = new File(javaFileName);
		
		int count = 0;
		try(Scanner scanner = new Scanner(file, "ISO-8859-1")) {
			while (scanner.hasNext()) {
				count++;
				scanner.nextLine();
			}
		} catch (IOException e) {
			System.out.println("Data is null!");
		}
		
		return count;	
	}
	
	private static int getCharacterCountOfFile(String javaFileName) {
		File file = new File(javaFileName);
		
		int count = 0;
		try(Scanner scanner = new Scanner(file, "ISO-8859-1")) {
			while (scanner.hasNextLine()) {
				count += scanner.nextLine().length();
			}
		} catch (Exception e) {
			System.out.println("Data is null!");
		}
		
		return count;
	}
	
	void getCommitDataPerFile(String directoryLocation, String javaFileName) throws IOException, ParseException {
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
								
		String s;
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
				cd.setInsertions(insertionList.get(i));
			} catch (IndexOutOfBoundsException e) {
				cd.setInsertions(0);	
			}
			
			try {
				cd.setDeletions(deletionList.get(i));
			} catch (IndexOutOfBoundsException e) {
				cd.setDeletions(0);
			}			
						
			// add object to list
			javaFileObject.getCommitDataList().add(cd);
		}
		
		javaFileObject.setUniqueAuthors(authorList);
		javaFileObject.setUniqueEmails(emailList);
		
		// set GitData javaFile
		javaFile = javaFileObject;
		
	}	
}
