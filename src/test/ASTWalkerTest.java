package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	
	public static boolean compareTwoSets(int[] correct, List<Integer> test) {
		boolean same = true;
		
		for(int i = 0; i < test.size(); i++) {
			if(correct[i] != test.get(i)) {
				same = false;
			}
		}
		
		return same;
	}
	
	@Test
	public void packageCheck() {
		// number of packages
		assertEquals(1, fileModel.getPackages().getPackageObjectList().size());
		
		// position of packages
		List<Integer> lineList = fileModel.getPackages().getLineNumbers();
		List<Integer> columnList = fileModel.getPackages().getColumnNumbers();
		
		int[] correctLineList = {30};
		int[] correctColumnList = {8};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}

	@Test
	public void importCheck() {
		// number of imports
		assertEquals(7, fileModel.getImports().getImportObjectList().size());
		
		// position of imports
		List<Integer> lineList = fileModel.getImports().getLineNumbers();
		List<Integer> columnList = fileModel.getImports().getColumnNumbers();
		
		int[] correctLineList = {32, 33, 34, 35, 36, 37, 38};
		int[] correctColumnList = {7, 7, 7, 7, 7, 7, 7};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		
	}
	
	@Test
	public void classCheck() {
		// number of classes (regular + extends super)
		int normalClassCount = fileModel.getClasses().getClassObjectList().size();
		int superClassCount = fileModel.getClasses().getSuperClassObjectList().size();
		
		assertEquals(4, normalClassCount);
		assertEquals(2, superClassCount);
	
		// position of classes (starts at "class" or "public class")
		List<Integer> lineList = fileModel.getClasses().getLineNumbers();
		List<Integer> columnList = fileModel.getClasses().getColumnNumbers();
		List<Integer> superClassLineList = fileModel.getClasses().getSuperClassLineNumbers();
		
		int[] correctLineList = {49, 69, 78, 80};
		int[] correctColumnList = {0, 0, 0, 1};
		int[] correctSuperClassLineList = {49, 69};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctSuperClassLineList, superClassLineList));
	}
	
	@Test
	public void interfaceCheck() {
		// number of interfaces (interfaces + implements interface)
		int interfaceCount = fileModel.getInterfaces().getInterfaceObjectList().size();
		int implementsCount = fileModel.getInterfaces().getImplementsObjectList().size();
		
		assertEquals(3, interfaceCount);
		assertEquals(2, implementsCount);
		
		// position of interfaces (starts at "interface")
		List<Integer> lineList = fileModel.getInterfaces().getLineNumbers();
		List<Integer> columnList = fileModel.getInterfaces().getColumnNumbers();
		List<Integer> implementsLineList = fileModel.getInterfaces().getImplementsLineNumbers();
		
		int[] correctLineList = {40, 44, 50};
		int[] correctColumnList = {0, 0, 1};
		int[] correctImplementsLineList = {69, 69};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctImplementsLineList, implementsLineList));
	}

	@Test
	public void arrayCheck() {
		// number of arrays
		assertEquals(4, fileModel.getArrays().getArrayObjectList().size());
		
		// position of arrays, ignores arrays if return type
		List<Integer> lineList = fileModel.getArrays().getLineNumbers();
		List<Integer> columnList = fileModel.getArrays().getColumnNumbers();
				
		int[] correctLineList = {108, 112, 116, 156};
		int[] correctColumnList = {104, 107, 34, 8};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void catchClauseCheck() {
		assertEquals(1, fileModel.getCatchClauses().getCatchClauseObjectList().size());
	}
	
	@Test
	public void conditionalExpressionCheck() {
		assertEquals(1, fileModel.getConditionalExpressions().getConditionalExpressionObjectList().size());
	}
	
	@Test
	public void doStatementCheck() {
		assertEquals(2, fileModel.getDoStatements().getDoStatementObjectList().size());
	}
	
	@Test
	// check number of for statements
	public void forStatementCheck() {		
		assertEquals(4, fileModel.getForStatements().getForStatementObjectList().size());
	}
	
	@Test
	// check number of generics
	// CHECK THIS AGAIN
	public void genericsCheck() {
		assertEquals(8, fileModel.getGenerics().getGenericsObjectList().size());
	}
	
	@Test
	// check number of if statements
	// every time the word IF shows up, +1 count
	public void ifStatementCheck() {
		assertEquals(3, fileModel.getIfStatements().getIfStatementObjectList().size());
	}
	
	@Test
	public void infixExpressionCheck() {
		assertEquals(10, fileModel.getInfixExpressions().getInfixExpressionObjectList().size());
	}
	
	@Test
	// checks number of method declarations (includes constructors)
	public void methodDeclarationCheck() {
		assertEquals(14, fileModel.getMethodDeclarations().getMethodDeclarationObjectList().size());
	}
	
	@Test
	public void methodInvocationCheck() {
		assertEquals(13, fileModel.getMethodInvocations().getMethodInvocationObjectList().size());
	}
	
	@Test
	public void primitiveCheck() {
		assertEquals(27, fileModel.getPrimitives().getPrimitiveObjectList().size());
	}
	
	@Test
	// specifically looking for the word THROW, not THROWS
	public void throwStatementCheck() {
		assertEquals(1, fileModel.getThrowStatements().getThrowStatementObjectList().size());
	}
	
	@Test
	public void tryStatementCheck() {
		assertEquals(1, fileModel.getTryStatements().getTryStatementObjectList().size());
	}
	
	@Test
	public void simpleNameCheck() {
		assertEquals(8, fileModel.getSimpleNames().getSimpleNameObjectList().size());
	}
	
	@Test
	public void switchStatementCheck() {
		assertEquals(1, fileModel.getSwitchStatements().getSwitchStatementObjectList().size());
	}
	
	@Test
	public void whileStatementCheck() {
		assertEquals(1, fileModel.getWhileStatements().getWhileStatementObjectList().size());
	}
	
	@Test
	// only found in field declarations
	public void wildcardCheck() {
		assertEquals(1, fileModel.getWildcards().getWildcardObjectList().size());
	}
	
}
