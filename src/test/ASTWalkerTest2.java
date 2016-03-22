package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import entities.Entity;
import entities.JavaClass;
import entities.MethodDeclarationObject;
import entities.SuperEntityClass;
import tools.FileModel;

public class ASTWalkerTest2 {

	private static FileModel fileModel_jabber;
	private static FileModel fileModel_gmail;
	
	public ASTWalkerTest2() throws IOException, CoreException {		
		String fileLocation_jabber = "/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/JabberChatEx.java";
		fileModel_jabber = new FileModel();
		fileModel_jabber = fileModel_jabber.parseDeclarations(fileLocation_jabber);
		
		String fileLocation_gmail = "/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/GMailSenderEx.java";
		fileModel_gmail = new FileModel();
		fileModel_gmail = fileModel_gmail.parseDeclarations(fileLocation_gmail);
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
		assertEquals(1, fileModel_jabber.getJavaClassList().size());
		
		// GMailSenderEx.java
		assertEquals(2, fileModel_gmail.getJavaClassList().size());
	}

	
	@Test
	public void methodDeclarationCheck() {
		// JabberChatEx.java
		int totalMethodDeclarationCount_jabber = 0;	
		for(JavaClass j : fileModel_jabber.getJavaClassList()) {
			totalMethodDeclarationCount_jabber += j.getMethodDeclarationList().size();
		}
		assertEquals(8, totalMethodDeclarationCount_jabber);
		
		// GMailSenderEx.java
		int totalMethodDeclarationCount_gmail = 0;
		for(JavaClass j : fileModel_gmail.getJavaClassList()) {
			totalMethodDeclarationCount_gmail += j.getMethodDeclarationList().size();
		}
		assertEquals(10, totalMethodDeclarationCount_gmail);		
	}
	
	@Test
	public void importCheck() {
		// JabberChatEx.java
		int totalImportCount_jabber = 0;
		totalImportCount_jabber = fileModel_jabber.getJavaClassList().get(0).getImportList().size();
		assertEquals(5, totalImportCount_jabber);
		
		// GMailSenderEx.java
		int totalImportCount_gmail = 0;
		totalImportCount_gmail = fileModel_gmail.getJavaClassList().get(0).getImportList().size();
		assertEquals(14, totalImportCount_gmail);
	}

	@Test 
	public void tryStatementCheck() {
		// JabberChatEx.java
		int count_jabber = 0;
		for(JavaClass j : fileModel_jabber.getJavaClassList()) {
			for(SuperEntityClass mdo : j.getMethodDeclarationList()) {
				count_jabber += ((MethodDeclarationObject) mdo).getTryStatementList().size();
			}
		}
		assertEquals(1, count_jabber);
		
		int count_gmail = 0;
		for(JavaClass j : fileModel_gmail.getJavaClassList()) {
			for(SuperEntityClass mdo : j.getMethodDeclarationList()) {
				count_gmail += ((MethodDeclarationObject) mdo).getTryStatementList().size();
			}
		}
		assertEquals(1, count_gmail);
	}
	
	
}
