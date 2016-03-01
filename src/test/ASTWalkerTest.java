package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import entities.SimpleName__;
import entities.SwitchStatement__;
import entities.ThrowStatement__;
import entities.TryStatement__;
import entities.WhileStatement__;
import entities.Wildcard__;
import tools.FileModel;

/**
 * 
 * ASTWalker JUnit test against src/exampleCode/Example.java
 * 
 * @author Thomas Kwak
 *
 */

public class ASTWalkerTest {

	private static FileModel fileModel;

	public ASTWalkerTest() throws IOException, CoreException {		
		String fileLocation = "src/exampleCode/Example.java";
		
		fileModel = new FileModel();
		
		if(fileLocation.substring(fileLocation.lastIndexOf(".")+1).equals("java")) {
			fileModel = fileModel.parseDeclarations(fileLocation);
		}
	}
	
	@Test
	// check number of packages
	public void packageCount() {
		assertEquals(1, fileModel.getPackages().getPackageObjectList().size());
	}

	@Test
	// check number of imports
	public void importCount() {
		assertEquals(6, fileModel.getImports().getImportObjectList().size());
	}
	
	@Test
	// check number of classes (regular + extends super)
	public void classCount() {
		int normalClassCount = fileModel.getClasses().getClassObjectList().size();
		int superClassCount = fileModel.getClasses().getSuperClassObjectList().size();
		
		assertEquals(5, normalClassCount);
		assertEquals(2, superClassCount);
	}
	
	@Test
	// check number of interfaces (interfaces + implements interface)
	public void interfaceCount() {
		int interfaceCount = fileModel.getInterfaces().getInterfaceObjectList().size();
		int implementsCount = fileModel.getInterfaces().getImplementsObjectList().size();
		
		assertEquals(1, interfaceCount);
		assertEquals(2, implementsCount);
	}

	@Test
	public void arrayCount() {
		
	}
	
	@Test
	public void catchClauseCount() {
		
	}
	
	@Test
	public void conditionalExpressionCount() {
		
	}
	
	@Test
	public void doStatementCount() {
		
	}
	
	@Test
	public void forStatementCount() {
		
	}
	
	@Test
	public void genericsCount() {
		
	}
	
	@Test
	public void ifStatementCount() {
		
	}
	
	@Test
	public void infixExpressionCount() {
		
	}
	
	@Test
	// checks number of method declarations (includes constructors)
	public void methodDeclarationCount() {
		assertEquals(8, fileModel.getMethodDeclarations().getMethodDeclarationObjectList().size());
	}
	
	@Test
	public void methodInvocationCount() {
		
	}
	
	@Test
	public void primitiveCount() {
		
	}
	
	@Test
	public void throwStatementCount() {
		
	}
	
	@Test
	public void tryStatementCount() {
		
	}
	
	@Test
	public void simpleNameCount() {
		
	}
	
	@Test
	public void switchStatementCount() {
		
	}
	
	@Test
	public void whileStatementCount() {
		
	}
	
	@Test
	public void wildcardCount() {
		
	}
	
}
