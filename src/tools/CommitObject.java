package tools;

public class CommitObject {
	String author;
	String authorEmail;
	String hashCode;
	String commitMessage;
	String date;
		
	public CommitObject(String a, String ae, String hc, String cm, String d) {
		author = a;
		authorEmail = ae;
		commitMessage = cm;
		hashCode = hc;
		date = d;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getAuthorEmail() {
		return authorEmail;
	}
	
	public String getHashCode() {
		return hashCode;
	}
	
	public String getCommitMessage() {
		return commitMessage;
	}
	
	public String getDate() {
		return date;
	}
	
}
