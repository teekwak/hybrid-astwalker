package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import entities.JavaClass;
import tools.FileModel;

public class ASTWalkerTest2 {

	private static FileModel fileModel;
	
	public ASTWalkerTest2() throws IOException, CoreException {		
		 String fileLocation = "/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/JabberChatEx.java";
		//String fileLocation = "/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/GMailSenderEx.java";
				
		fileModel = new FileModel();
		
		if(fileLocation.endsWith("java")) {
			fileModel = fileModel.parseDeclarations(fileLocation);
		}
	}
	
	public static boolean compareTwoSets(int[] correct, List<Integer> test) {
		boolean same = true;
		
		for(int i = 0; i < test.size(); i++) {
			if(correct[i] != test.get(i)) {
				same = false;
			}
		}
		
		return same;
	}
	
	public static boolean compareTwoSets(String[] correct, List<String> test) {
		boolean same = true;
		
		for(int i = 0; i < test.size(); i++) {
			if(!correct[i].equals(test.get(i))) {
				same = false;
			}
		}
		
		return same;
	}

	@Test
	public void classCheck() {
		// JabberChatEx.java
		assertEquals(1, fileModel.getJavaClassList().size());
		
		// GMailSenderEx.java
		//assertEquals(2, fileModel.getJavaClassList().size());
	}

	
	@Test
	public void methodDeclarationCheck() {
		// JabberChatEx.java
		for(JavaClass j : fileModel.getJavaClassList()) {
			for(Entity e : )
		}
		
		assertEquals(8, fileModel.get);
	
	}
}
