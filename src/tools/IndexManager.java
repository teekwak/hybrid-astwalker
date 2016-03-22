package tools;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;

import tools.FileModel;
import tools.GitData;

public class IndexManager {
	
	public static List<FileModel> fileModelList = new ArrayList<>();
	public static List<GitData> gitDataList = new ArrayList<>();
	
	public static void traverseUntilJava(File parentNode, String topDirectoryLocation) throws IOException, CoreException, NoHeadException, GitAPIException, ParseException {
		if(parentNode.isDirectory()) {
			File childNodes[] = parentNode.listFiles();
						
			for(File c : childNodes) {
				if(!c.getName().startsWith(".")) {
					traverseUntilJava(c, topDirectoryLocation);
				}
			}
		}
		else {
			/*
			if(parentNode.getName().endsWith(".java")) {	
				runASTandGitData(parentNode, topDirectoryLocation);
			}
			*/
			if(parentNode.getName().equals("JabberConnectionDebugger.java")) {
			//if(parentNode.getName().equals("java1.java")) {
				runASTandGitData(parentNode, topDirectoryLocation);
			}
		}
	}
	
	/*
	 * fileModel holds all AST information
	 * 
	 * gitData.javaFileList has a list of Java file objects with data on each commit
	 * gitData.getCommitDataPerFile() creates commit objects
	 * gitData.addHashCodePairsToMap() populates hash codes
	 * gitData.getDiff() gets insertions and deletions for all commits
	 * 
	 * to get the insertions/deletions at each commit, stored in CommitData object
	 * 
	 * for(JavaFile j : gitData.javaFileList) {
	 *     for(CommitData c : j.commitDataList) {
	 *         System.out.println(j.name + " +" + c.insertions + " -" + c.deletions);
	 *     }
	 * }
	 * 
	 */
	public static void runASTandGitData(File parentNode, String topDirectoryLocation) throws IOException, CoreException, NoHeadException, GitAPIException, ParseException {
		FileModel fileModel = new FileModel();
		fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
		//fileModel.printAll();
		fileModelList.add(fileModel);
		
		GitData gitData = new GitData();
		Git git = Git.open( new File (topDirectoryLocation + ".git") );
		
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory);
		
		gitData.getCommitDataPerFile(topDirectoryLocation, parentNode.getAbsolutePath());
		gitData.addHashCodePairsToMap(commitHistory);
		gitData.getDiff(topDirectoryLocation, gitData.hashCodePairs);
		
		gitDataList.add(gitData);
		
		git.close();
		
		for(JavaFile j : gitData.javaFileList) {
			for(CommitData cd : j.commitDataList) {
				System.out.println(cd.solrDate);
			}
		}
	}
	
	public static void main(String[] args) throws IOException, CoreException, NoHeadException, GitAPIException, ParseException {
		fileModelList.clear();
		gitDataList.clear();
		
		//String topDirectoryLocation = args[0];
		String topDirectoryLocation = "/home/kwak/Desktop/jabber-plugin/";
		//String topDirectoryLocation = "/home/kwak/Desktop/jgit-test/";
		
		File inputFolder = new File( topDirectoryLocation );
		traverseUntilJava(inputFolder, topDirectoryLocation);
		

		
	}
}