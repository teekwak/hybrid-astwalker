package tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;

import tools.FileModel;
import tools.GitData;

public class ASTWalkerAndGitData {
	
	public static void traverseUntilJava(File parentNode, String topDirectoryLocation) throws IOException, CoreException {
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
	
	public static void runASTandGitData(File parentNode, String topDirectoryLocation) throws IOException, CoreException {
		FileModel fileModel = new FileModel();
		fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
			
		GitData gitData = new GitData();
		Git git = Git.open( new File (topDirectoryLocation + ".git") );

		gitData.getCommitDataPerFile(topDirectoryLocation, parentNode.getAbsolutePath());
		
		
		for(JavaFile j : gitData.javaFileList) {
			for(CommitData c : j.commitDataList) {
				System.out.println(j.name + " " + j.numberOfLines + " " + j.numberOfCharacters);
			}
		}		
		
		
		git.close();	
	}
	
	public static void main(String[] args) throws IOException, CoreException, NoHeadException, GitAPIException {
		// String directoryLocation = args[0];
		
		ASTWalkerAndGitData obj = new ASTWalkerAndGitData();
		
		String topDirectoryLocation = "/home/kwak/Desktop/jgit-test/";
		File inputFolder = new File( topDirectoryLocation );
		traverseUntilJava(inputFolder, topDirectoryLocation);
	}
}