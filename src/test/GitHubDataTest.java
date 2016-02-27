package test;

import static org.junit.Assert.*;

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

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import tools.CommitObject;

public class GitHubDataTest {
	
	// testing with https://github.com/Thomas Kwak/jgit-test.git
	
	List<CommitObject> commitObjectList;
	Map<String, String> hashCodePairs;
	List<int[]> plusMinusList;
	
	public GitHubDataTest() throws IOException, NoHeadException, GitAPIException {
		
		commitObjectList = new ArrayList<>();
		hashCodePairs = new LinkedHashMap<>();
		plusMinusList = new ArrayList<>();
		
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";
		Git git = Git.open( new File (directoryLocation + ".git") );
				
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory); // all commits from earliest to latest
		
		for(RevCommit commit : commitHistory) {
			this.addToList(new CommitObject(getAuthorOfCommit(commit), getAuthorEmailOfCommit(commit), getHashCodeOfCommit(commit), getCommitMessage(commit), getDateOfCommit(commit)));
		}
		
		
		this.addHashCodePairsToMap(commitHistory);
		this.runBashScript(directoryLocation, hashCodePairs);
		
		git.getRepository().close();
		
	}
	
	public void addToList(CommitObject obj) {
		commitObjectList.add(obj);
	}
	
	public List<CommitObject> getCommitObjectList() {
		return this.commitObjectList;
	}
	
	public Map<String, String> getHashCodePairs() {
		return this.hashCodePairs;
	}
	
	public void addHashCodePairsToMap(List<RevCommit> commitHistory) {
		// shouldn't first pair be (root --> first commit)?
		this.hashCodePairs.put("--root", commitHistory.get(0).name());
		
		// puts rest of commit pairs into map
		for(int i = 0; i < commitHistory.size() - 1; i++) {
			this.hashCodePairs.put(commitHistory.get(i).name(), commitHistory.get(i + 1).name());
		}			
	}
	
	public void runBashScript(String directoryLocation, Map<String, String> hashCodePairs) throws IOException {
		File script = new File("scripts/gitDiffScript.sh"); // need script to be in scripts folder
		
		for(Map.Entry<String, String> entry : hashCodePairs.entrySet()) {
			
			Process proc = Runtime.getRuntime().exec(script.getAbsolutePath() + " " + directoryLocation + " " + entry.getKey() + " " + entry.getValue());
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			//System.out.println(entry.getKey() + " " + entry.getValue());
			
			String s = null;
			
			int[] temp = new int[2];
			
			while((s = br.readLine()) != null) {
				String[] parts = s.split("\t");
				
				if(parts.length == 3) { // for some reason, git diff-tree --numstat --root $3 also returns hash code of commit
					//System.out.println(parts[2] + "\nLines inserted:\t" + parts[0] + "\nLines deleted:\t" + parts[1]);	
					temp[0] += Integer.parseInt(parts[0]);
					temp[1] += Integer.parseInt(parts[1]);
 					
				}
			}
			this.plusMinusList.add(temp);
			//System.out.println();
			
			br.close();
		}
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
	
	public static String getDateOfCommit(RevCommit commit) {
		PersonIdent author = commit.getAuthorIdent();
		Date date = author.getWhen();
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		return dateFormat.format(date);
	}
	
	public static String getHashCodeOfCommit(RevCommit commit) {
		return commit.name();
	}
	
	public static String formatDate(Date date) {
		// Dec 25, 1993
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		return dateFormat.format(date);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	// at the time of writing, there are 10 commits
	public void checkNumberOfCommits() {		
		assertEquals(10, this.getCommitObjectList().size());
	}

	@Test
	// there is 1 empty commit message
	public void checkNumberOfMessages() {
			
		int count = 0;
		for(CommitObject c : this.getCommitObjectList()) {
			if(!c.getCommitMessage().isEmpty()) {
				count++;
			}
		}
		
		assertEquals(9, count);
	}
	
	@Test
	// check actual commit messages
	// apparently commit messages keep \n at the end
	public void checkCommitMessages() {
		String[] actualCommitMessages = {"added file\n", "modified text\n", "add and modify\n", "added text3\n", "modified text3.txt\n", "modified text2, deleted text3\n", "deleted and added line back\n", "90% same\n", "", "modified text3.txt\n"};
				
		// count commit messages
		int testObjMessageCount = 0;
		for(CommitObject c : this.getCommitObjectList()) {
			if(c.getCommitMessage() != null) {
				testObjMessageCount++;
			}
		}
		assertEquals(actualCommitMessages.length, testObjMessageCount);
		
		// check if commit messages are the same
		boolean same = true;
		for(int i = 0; i < actualCommitMessages.length; i++) {
			if(!actualCommitMessages[i].equals(this.getCommitObjectList().get(i).getCommitMessage())) {
				same = false;
				break;
			}	
		}
		assertTrue(same);
	}
	
	@Test
	// check authors of each commit
	public void checkAuthor() {
		String[] actualAuthors = {"Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak", "Thomas Kwak"};
	
		// count number of authors
		int authorCount = 0;
		for(CommitObject c : this.getCommitObjectList()) {
			if(c.getAuthor() != null) {
				authorCount++;
			}
		}
		assertEquals(actualAuthors.length, authorCount);
		
		// check if authors are the same
		boolean same = true;
		for(int i = 0; i < actualAuthors.length; i++) {
			if(!this.getCommitObjectList().get(i).getAuthor().equals(actualAuthors[i])) {
				same = false;
				break;
			}
		}
		assertTrue(same);
	}
	
	@Test
	// check emails of each commit
	public void checkEmail() {
		String[] actualEmails = {"teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com", "teekwak@gmail.com"};
	
		// count number of emails
		int emailCount = 0;
		for(CommitObject c : this.getCommitObjectList()) {
			if(c.getAuthorEmail() != null) {
				emailCount++;
			}
		}
		assertEquals(actualEmails.length, emailCount);
	
		// check if emails are the same
		boolean same = true;
		for(int i = 0; i < actualEmails.length; i++ ){
			if(!actualEmails[i].equals(this.getCommitObjectList().get(i).getAuthorEmail())) {
				same = false;
				break;
			}
		}
		assertTrue(same);
	}
	
	@Test
	// check dates of commits
	public void checkCalendarDate() {
		String[] actualDates = {"Feb 24, 2016", "Feb 24, 2016", "Feb 24, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016"};
				
		// count number of dates
		int testObjDateCount = 0;
		for(CommitObject c : this.getCommitObjectList()) {
			if(c.getDate() != null) {
				testObjDateCount++;
			}
		}
		assertEquals(actualDates.length, testObjDateCount);
		
		// check if dates are the same
		boolean same = true;
		for(int i = 0; i < actualDates.length; i++ ){
			if(!actualDates[i].equals(this.getCommitObjectList().get(i).getDate())) {
				same = false;
				break;
			}
		}
		assertTrue(same);
	}
	
	
	@Test
	// check hash code of each commit
	public void checkHashCode() { 
		String[] actualHashCodes = {"ba1854c33cbe8ea4f678f94f12c70e9f19cb54ec", "513238f0ce9e2eda3baa1c76c5e92b6f5ae275be", "b785c3f61a8ba941e3bf01f15892e5e239db419a", "d6eb886344ae23038a712e26d3f92a0f510bd2f7", "33efe94d02406c403835bcf220029b54989e306e", "0fe93bd443793e332fb89b94e04ce7ee3682dcaa", "ccc242b959b9ceebedb3ca8dd29d1479ca0c45ee", "46eece892de3fb570db9889884104e821b1c690d", "7e57cbaec3f02b3dd736dd782ec050bcc711f103", "29a195cc72705752904416803ea888ee717fa0f4"};
				
		// count number of hash codes
		int testObjHashCodeCount = 0;
		for(CommitObject c : this.getCommitObjectList()) {
			if(c.getHashCode() != null) {
				testObjHashCodeCount++;
			}
		}
		assertEquals(actualHashCodes.length, testObjHashCodeCount);		
	
		// check if hash codes are the same
		boolean same = true;
		for(int i = 0; i < actualHashCodes.length; i++ ){
			if(!actualHashCodes[i].equals(this.getCommitObjectList().get(i).getHashCode())) {
				same = false;
				break;
			}
		}
		assertTrue(same);
	}
	
	@Test
	// check number of differences between commits
	public void checkInsertionsAndDeletionsBetweenCommits() {
		List<String[]> hashCodeList = new ArrayList<>();
		List<int[]> insertionDeletionList = new ArrayList<>();
		
		hashCodeList.add(new String[]{"--root", "ba1854c33cbe8ea4f678f94f12c70e9f19cb54ec"});
		hashCodeList.add(new String[]{"ba1854c33cbe8ea4f678f94f12c70e9f19cb54ec", "513238f0ce9e2eda3baa1c76c5e92b6f5ae275be"});
		hashCodeList.add(new String[]{"513238f0ce9e2eda3baa1c76c5e92b6f5ae275be", "b785c3f61a8ba941e3bf01f15892e5e239db419a"});
		hashCodeList.add(new String[]{"b785c3f61a8ba941e3bf01f15892e5e239db419a", "d6eb886344ae23038a712e26d3f92a0f510bd2f7"});
		hashCodeList.add(new String[]{"d6eb886344ae23038a712e26d3f92a0f510bd2f7", "33efe94d02406c403835bcf220029b54989e306e"});
		hashCodeList.add(new String[]{"33efe94d02406c403835bcf220029b54989e306e", "0fe93bd443793e332fb89b94e04ce7ee3682dcaa"});
		hashCodeList.add(new String[]{"0fe93bd443793e332fb89b94e04ce7ee3682dcaa", "ccc242b959b9ceebedb3ca8dd29d1479ca0c45ee"});
		hashCodeList.add(new String[]{"ccc242b959b9ceebedb3ca8dd29d1479ca0c45ee", "46eece892de3fb570db9889884104e821b1c690d"});
		hashCodeList.add(new String[]{"46eece892de3fb570db9889884104e821b1c690d", "7e57cbaec3f02b3dd736dd782ec050bcc711f103"});
		hashCodeList.add(new String[]{"7e57cbaec3f02b3dd736dd782ec050bcc711f103", "29a195cc72705752904416803ea888ee717fa0f4"});
		
		insertionDeletionList.add(new int[]{0, 0});
		insertionDeletionList.add(new int[]{1, 0});
		insertionDeletionList.add(new int[]{2, 0});
		insertionDeletionList.add(new int[]{0, 0});
		insertionDeletionList.add(new int[]{6, 0});
		insertionDeletionList.add(new int[]{1, 6});
		insertionDeletionList.add(new int[]{1, 1});
		insertionDeletionList.add(new int[]{1, 1});
		insertionDeletionList.add(new int[]{0, 0});
		insertionDeletionList.add(new int[]{1, 0});
	
		// check number of hash code pairs
		assertEquals(hashCodeList.size(), this.getHashCodePairs().size());
	
		// check if pairs have the same hash codes
		boolean hclSame = true;
		int counter = 0;
		for(Map.Entry<String, String> entry : this.getHashCodePairs().entrySet()) {
			if(!entry.getKey().equals(hashCodeList.get(counter)[0]) || !entry.getValue().equals(hashCodeList.get(counter)[1])) {
				hclSame = false;
				break;
			}
			counter++;
		}
		assertTrue(hclSame);
		
		// compare total number of insertions/deletions
		int totalInsertions = 0;
		int totalDeletions = 0;
				
		for(int[] arr : this.plusMinusList) {
			totalInsertions += arr[0];
			totalDeletions += arr[1];
		}
		
		assertEquals(13, totalInsertions);
		assertEquals(8, totalDeletions);
		
		// compare each commit's insertions and deletions
		boolean same = true;
		for(int i = 0; i < insertionDeletionList.size(); i++) {
			if(insertionDeletionList.get(i)[0] != this.plusMinusList.get(i)[0]) {
				same = false;
				break;
			}
			
			if(insertionDeletionList.get(i)[1] != this.plusMinusList.get(i)[1]) {
				same = false;
				break;
			}
		}
		
		assertTrue(same);
	}
	
	// check paths lead to real files
	
	
	
	
}
