package tools;

import java.io.File;
import java.io.IOException;
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

public class ASTWalkerAndGitData {
	
	public static void traverseUntilJava(File parentNode, String topDirectoryLocation) throws IOException, CoreException, NoHeadException, GitAPIException {
		if(parentNode.isDirectory()) {
			File childNodes[] = parentNode.listFiles();
						
			for(File c : childNodes) {
				if(!c.getName().startsWith(".")) {
					traverseUntilJava(c, topDirectoryLocation);
				}
			}
		}
		else {
			if(parentNode.getName().endsWith(".java")) {	
				// AST model for each file created here
				runASTandGitData(parentNode, topDirectoryLocation);
			}
		}
	}
	
	/*
	 * fileModel holds all AST information
	 * 
	 * gitData.javaFileList has a list of Java file objects with data on each commit
	 */
	public static void runASTandGitData(File parentNode, String topDirectoryLocation) throws IOException, CoreException, NoHeadException, GitAPIException {
		FileModel fileModel = new FileModel();
		fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
			
		GitData gitData = new GitData();
		Git git = Git.open( new File (topDirectoryLocation + ".git") );
		
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory);
		
		// create commit objects here
		gitData.getCommitDataPerFile(topDirectoryLocation, parentNode.getAbsolutePath());
		
		// populate hash codes
		gitData.addHashCodePairsToMap(commitHistory);
		
		// get insertions and deletions count
		gitData.getDiff(topDirectoryLocation, gitData.hashCodePairs);
		
		git.close();
		
		/*
		for(JavaFile j : gitData.javaFileList) {
			for(CommitData c : j.commitDataList) {
				System.out.println(j.name + " +" + c.insertions + " -" + c.deletions);
			}
		}
		*/
	}
	
	public static void main(String[] args) throws IOException, CoreException, NoHeadException, GitAPIException {
		// String directoryLocation = args[0];
				
		String topDirectoryLocation = "/home/kwak/Desktop/jgit-test/";
		File inputFolder = new File( topDirectoryLocation );
		traverseUntilJava(inputFolder, topDirectoryLocation);
	}
}