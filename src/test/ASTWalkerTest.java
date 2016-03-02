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
		assertEquals(7, fileModel.getImports().getImportObjectList().size());
	}
	
	@Test
	// check number of classes (regular + extends super)
	public void classCount() {
		int normalClassCount = fileModel.getClasses().getClassObjectList().size();
		int superClassCount = fileModel.getClasses().getSuperClassObjectList().size();
		
		assertEquals(4, normalClassCount);
		assertEquals(2, superClassCount);
	}
	
	@Test
	// check number of interfaces (interfaces + implements interface)
	public void interfaceCount() {
		int interfaceCount = fileModel.getInterfaces().getInterfaceObjectList().size();
		int implementsCount = fileModel.getInterfaces().getImplementsObjectList().size();
		
		assertEquals(3, interfaceCount);
		assertEquals(2, implementsCount);
	}

	@Test
	public void arrayCount() {
		assertEquals(4, fileModel.getArrays().getArrayObjectList().size());
	}
	
	@Test
	public void catchClauseCount() {
		assertEquals(1, fileModel.getCatchClauses().getCatchClauseObjectList().size());
	}
	
	@Test
	public void conditionalExpressionCount() {
		assertEquals(1, fileModel.getConditionalExpressions().getConditionalExpressionObjectList().size());
	}
	
	@Test
	public void doStatementCount() {
		assertEquals(2, fileModel.getDoStatements().getDoStatementObjectList().size());
	}
	
	@Test
	// check number of for statements
	public void forStatementCount() {		
		assertEquals(4, fileModel.getForStatements().getForStatementObjectList().size());
	}
	
	@Test
	// check number of generics
	// CHECK THIS AGAIN
	public void genericsCount() {
		assertEquals(8, fileModel.getGenerics().getGenericsObjectList().size());
	}
	
	@Test
	// check number of if statements
	// every time the word IF shows up, +1 count
	public void ifStatementCount() {
		assertEquals(3, fileModel.getIfStatements().getIfStatementObjectList().size());
	}
	
	@Test
	public void infixExpressionCount() {
		assertEquals(10, fileModel.getInfixExpressions().getInfixExpressionObjectList().size());
	}
	
	@Test
	// checks number of method declarations (includes constructors)
	public void methodDeclarationCount() {
		assertEquals(14, fileModel.getMethodDeclarations().getMethodDeclarationObjectList().size());
	}
	
	@Test
	public void methodInvocationCount() {
		assertEquals(13, fileModel.getMethodInvocations().getMethodInvocationObjectList().size());
	}
	
	@Test
	public void primitiveCount() {
		assertEquals(27, fileModel.getPrimitives().getPrimitiveObjectList().size());
	}
	
	@Test
	// specifically looking for the word THROW, not THROWS
	public void throwStatementCount() {
		assertEquals(1, fileModel.getThrowStatements().getThrowStatementObjectList().size());
	}
	
	@Test
	public void tryStatementCount() {
		assertEquals(1, fileModel.getTryStatements().getTryStatementObjectList().size());
	}
	
	@Test
	public void simpleNameCount() {
		assertEquals(8, fileModel.getSimpleNames().getSimpleNameObjectList().size());
	}
	
	@Test
	public void switchStatementCount() {
		assertEquals(1, fileModel.getSwitchStatements().getSwitchStatementObjectList().size());
	}
	
	@Test
	public void whileStatementCount() {
		assertEquals(1, fileModel.getWhileStatements().getWhileStatementObjectList().size());
	}
	
	@Test
	// only found in field declarations
	public void wildcardCount() {
		assertEquals(1, fileModel.getWildcards().getWildcardObjectList().size());
	}
	
}
