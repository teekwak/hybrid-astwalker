package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.Test;

import tools.CommitObject;
import tools.GitHubData;

public class GitHubDataTest {
	
	// testing with https://github.com/teekwak/jgit-test.git
	
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
		// Dec 25, 1993
		DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		return dateFormat.format(date);
	}
	
	
	
	@Test
	// at the time of writing, there are 10 commits
	public void checkNumberOfCommits() throws NoHeadException, GitAPIException, IOException {
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";
	
		Git git = Git.open( new File (directoryLocation + ".git") );
	
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
	
		git.getRepository().close();
		
		assertEquals(10, commitHistory.size());
	}

	@Test
	// there is 1 empty commit message
	public void checkNumberOfMessages() throws IOException, NoHeadException, GitAPIException {
		
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";
	
		GitHubData gitHubData = new GitHubData();
		
		Git git = Git.open( new File (directoryLocation + ".git") );
	
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory);

		for(RevCommit commit : commitHistory) {
			gitHubData.addToList(new CommitObject(getAuthorOfCommit(commit), getAuthorEmailOfCommit(commit), getHashCodeOfCommit(commit), getCommitMessage(commit), formatDate(getDateOfCommit(commit))));
		}
		
		int count = 0;
		for(Object c : gitHubData.getCommitObjectList()) {
			if(!((CommitObject)c).getCommitMessage().isEmpty()) {
				count++;
			}
		}
		
		git.getRepository().close();
		
		assertEquals(count, 9);
	}
	
	@Test
	// check actual commit messages
	// apparently commit messages keep \n at the end
	public void checkCommitMessages() throws IOException, NoHeadException, GitAPIException {
		String[] actualCommitMessages = {"added file\n", "modified text\n", "add and modify\n", "added text3\n", "modified text3.txt\n", "modified text2, deleted text3\n", "deleted and added line back\n", "90% same\n", "", "modified text3.txt\n"};
	
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";
				
		Git git = Git.open( new File (directoryLocation + ".git") );
	
		List<String> commitMessages = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitMessages.add(commit.getFullMessage());
		}
		Collections.reverse(commitMessages);
		
		assertEquals(actualCommitMessages.length, commitMessages.size());
		
		boolean same = true;
		
		for(int i = 0; i < actualCommitMessages.length; i++) {
			if(!actualCommitMessages[i].equals(commitMessages.get(i))) {
				
				same = false;
			}	
		}
		
		assertTrue(same);
	}
	
	@Test
	// check dates of commits
	public void checkCalendarDate() throws IOException, NoHeadException, GitAPIException {
		String[] actualDates = {"Feb 24, 2016", "Feb 24, 2016", "Feb 24, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016", "Feb 25, 2016"};
		
		String directoryLocation = "/home/kwak/Desktop/jgit-test/";
		
		Git git = Git.open( new File (directoryLocation + ".git") );
	
		List<String> commitDates = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitDates.add(formatDate(commit.getAuthorIdent().getWhen()));
		}
		Collections.reverse(commitDates);
		
		assertEquals(actualDates.length, commitDates.size());
		
		boolean same = true;
		
		for(int i = 0; i < actualDates.length; i++ ){
			if(!actualDates[i].equals(commitDates.get(i))) {
				same = false;
			}
		}
		
		assertTrue(same);
	}
	
	// check hashcode of each commit
	
	// check number of differences between commits

	// check paths lead to real files
	
	
	
	
}
