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
		
		// position of packages (starts after "package ")
		List<Integer> lineList = fileModel.getPackages().getLineNumbers();
		List<Integer> columnList = fileModel.getPackages().getColumnNumbers();
		
		int[] correctLineList = {30};
		int[] correctColumnList = {8};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}

	@Test
	public void importCheck() {
		// number of imports (starts after "import ")
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
		
		// position of arrays (ignores arrays if return type)
		List<Integer> lineList = fileModel.getArrays().getLineNumbers();
		List<Integer> columnList = fileModel.getArrays().getColumnNumbers();
				
		int[] correctLineList = {108, 112, 116, 156};
		int[] correctColumnList = {104, 107, 34, 8};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void catchClauseCheck() {
		// number of catch clauses
		assertEquals(1, fileModel.getCatchClauses().getCatchClauseObjectList().size());
		
		// position of catch clauses (where the exception variable is)
		List<Integer> lineList = fileModel.getCatchClauses().getLineNumbers();
		List<Integer> columnList = fileModel.getCatchClauses().getColumnNumbers();
		
		int[] correctLineList = {189};
		int[] correctColumnList = {32};
				
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void conditionalExpressionCheck() {
		// number of conditional expressions
		assertEquals(1, fileModel.getConditionalExpressions().getConditionalExpressionObjectList().size());
		
		// position of conditional expression (beginning of expression)
		List<Integer> lineList = fileModel.getConditionalExpressions().getLineNumbers();
		List<Integer> columnList = fileModel.getConditionalExpressions().getColumnNumbers();
		
		int[] correctLineList = {181};
		int[] correctColumnList = {21};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void doStatementCheck() {
		// number of do statements
		assertEquals(2, fileModel.getDoStatements().getDoStatementObjectList().size());
		
		// position of each do statement (where the word "do" is)
		List<Integer> lineList = fileModel.getDoStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getDoStatements().getColumnNumbers();
		
		int[] correctLineList = {185, 198};
		int[] correctColumnList = {3, 2};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));		
	}
	
	@Test
	public void forStatementCheck() {	
		// number of for statements
		assertEquals(4, fileModel.getForStatements().getForStatementObjectList().size());
		
		// position of for statements (where variable in enhanced, where the word "for" is for not)
		List<Integer> lineList = fileModel.getForStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getForStatements().getColumnNumbers();
		
		int[] correctLineList = {97, 103, 157, 161};
		int[] correctColumnList = {13, 13, 10, 2};
				
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	// check number of generics
	// CHECK THIS AGAIN
	public void genericsCheck() {
		assertEquals(8, fileModel.getGenerics().getGenericsObjectList().size());
	}
	
	@Test
	public void ifStatementCheck() {
		// number of if statements (even in "else if")
		assertEquals(3, fileModel.getIfStatements().getIfStatementObjectList().size());
		
		// position of if statements (starts at "if")
		List<Integer> lineList = fileModel.getIfStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getIfStatements().getColumnNumbers();
				
		int[] correctLineList = {55, 132, 191};
		int[] correctColumnList = {2, 2, 3};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void infixExpressionCheck() {
		// number of infix expressions
		assertEquals(10, fileModel.getInfixExpressions().getInfixExpressionObjectList().size());
		
		// position of infix expression (position of operator)
		List<Integer> lineList = fileModel.getInfixExpressions().getLineNumbers();
		List<Integer> columnList = fileModel.getInfixExpressions().getColumnNumbers();
		
		int[] correctLineList = {55, 65, 132, 161, 170, 177, 181, 188, 191, 201};
		int[] correctColumnList = {7, 23, 7, 19, 13, 23, 26, 12, 8, 12};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));		
	}
	
	@Test
	public void methodDeclarationCheck() {
		// number of method declarations (including constructors)
		assertEquals(14, fileModel.getMethodDeclarations().getMethodDeclarationObjectList().size());
		
		
	}
	
	@Test
	public void methodInvocationCheck() {
		// number of method invocations
		assertEquals(13, fileModel.getMethodInvocations().getMethodInvocationObjectList().size());
	}
	
	@Test
	public void primitiveCheck() {
		// number of primitives
		assertEquals(27, fileModel.getPrimitives().getPrimitiveObjectList().size());
	}
	
	@Test
	public void throwStatementCheck() {
		// number of throw statements (not throws)
		assertEquals(1, fileModel.getThrowStatements().getThrowStatementObjectList().size());
	}
	
	@Test
	public void tryStatementCheck() {
		// number of try statements
		assertEquals(1, fileModel.getTryStatements().getTryStatementObjectList().size());
	}
	
	@Test
	public void simpleNameCheck() {
		// number of simple names
		assertEquals(8, fileModel.getSimpleNames().getSimpleNameObjectList().size());
	}
	
	@Test
	public void switchStatementCheck() {
		// number of switch statements
		assertEquals(1, fileModel.getSwitchStatements().getSwitchStatementObjectList().size());
	}
	
	@Test
	public void whileStatementCheck() {
		// number of while statements
		assertEquals(1, fileModel.getWhileStatements().getWhileStatementObjectList().size());
	}
	
	@Test
	public void wildcardCheck() {
		// number of wildcards (only in field declarations)
		assertEquals(1, fileModel.getWildcards().getWildcardObjectList().size());
		
		// position of wildcards (position of question mark)
		List<Integer> lineList = fileModel.getWildcards().getLineNumbers();
		List<Integer> columnList = fileModel.getWildcards().getColumnNumbers();
				
		int[] correctLineList = {96};
		int[] correctColumnList = {40};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
}
