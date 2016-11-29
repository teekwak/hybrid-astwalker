package tools;

public class CommitObject {
	private String author;
	private String authorEmail;
	private String hashCode;
	private String commitMessage;
	private String date;
	
	public CommitObject(String a, String ae, String hc, String cm, String d) {
		author = a;
		authorEmail = ae;
		hashCode = hc;
		commitMessage = cm;
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
