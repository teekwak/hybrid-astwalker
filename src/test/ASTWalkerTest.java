package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
		String fileLocation = "/home/kwak/Desktop/ReSender/src/com/gilevich/resender/GMailSender.java";
		
		//String fileLocation = "/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/Example.java";
		
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
	public void packageCheck() {
		// number of packages
		assertEquals(1, fileModel.getPackages().getPackageObjectList().size());
		
		// position of packages (starts after "package ")
		List<Integer> lineList = fileModel.getPackages().getLineNumbers();
		List<Integer> columnList = fileModel.getPackages().getColumnNumbers();
		List<String> nameList = fileModel.getPackages().getNames();
		
		int[] correctLineList = {1};
		int[] correctColumnList = {8};
		String[] correctNameList = {"com.gilevich.resender"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}

	@Test
	public void importCheck() {
		// number of imports
		assertEquals(14, fileModel.getImports().getImportObjectList().size());
		
		// position of imports (begins after "import ")
		List<Integer> lineList = fileModel.getImports().getLineNumbers();
		List<Integer> columnList = fileModel.getImports().getColumnNumbers();
		List<String> nameList = fileModel.getImports().getNames();
		
		int[] correctLineList = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		int[] correctColumnList = {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
		String[] correctNameList = {"javax.activation.DataHandler", 
									"javax.activation.DataSource", 
									"javax.mail.Message",
									"javax.mail.PasswordAuthentication",
									"javax.mail.Session",
									"javax.mail.Transport",
									"javax.mail.internet.InternetAddress",
									"javax.mail.internet.MimeMessage",	
									"java.io.ByteArrayInputStream",
									"java.io.IOException",
									"java.io.InputStream",
									"java.io.OutputStream",
									"java.security.Security",
									"java.util.Properties"
		};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	/*
	
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
	public void genericsCheck() {
		// number of generics
		assertEquals(8, fileModel.getGenerics().getGenericsObjectList().size());
		
		// position of generics (everywhere)
		List<Integer> lineList = fileModel.getGenerics().getLineNumbers();
		List<Integer> columnList = fileModel.getGenerics().getColumnNumbers();
		
		int[] correctLineList = {96, 102, 108, 112, 117, 118, 119, 144};
		int[] correctColumnList = {43, 49, 93, 93, 15, 15, 23, 44};
				
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
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
	public void methodDeclarationCheck() {
		// number of method declarations (including constructors)
		assertEquals(14, fileModel.getMethodDeclarations().getMethodDeclarationObjectList().size());
		
		// position of method declarations (begins at method name)
		List<Integer> lineList = fileModel.getMethodDeclarations().getLineNumbers();
		List<Integer> columnList = fileModel.getMethodDeclarations().getColumnNumbers();
		
		int[] correctLineList = {41, 45, 46, 51, 54, 60, 64, 73, 91, 96, 102, 108, 112, 116};
		int[] correctColumnList = {15, 6, 5, 22, 6, 6, 6, 1, 13, 13, 13, 12, 12, 20};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));	
	}
	
	@Test
	public void methodInvocationCheck() {
		// number of unique method invocations
		assertEquals(13, fileModel.getMethodInvocations().getMethodInvocationObjectList().size());
		
		// position of method invocations (where the actual name of the method starts)
		List<Map<String, Map<Integer, Integer>>> listOfMaps = fileModel.getMethodInvocations().getInvocationClassAndPositionsMaps();
		
		List<Integer> lineList = new ArrayList<>();
		List<Integer> columnList = new ArrayList<>();
		Map<Integer, Integer> allPositions = new TreeMap<>();
		
		for(Map<String, Map<Integer, Integer>> bigMap : listOfMaps) {
			for(Map.Entry<String, Map<Integer, Integer>> smallMap : bigMap.entrySet()) {
				for(Map.Entry<Integer, Integer> tinyMap : smallMap.getValue().entrySet()) {
					allPositions.put(tinyMap.getKey(), tinyMap.getValue());
				}
			}
		}
		
		// number of ALL method invocations
		assertEquals(25, allPositions.size());
		
		int[] correctLineList = {56, 61, 65, 92, 93, 104, 121, 122, 123, 129, 136, 140, 141, 142, 145, 148, 150, 152, 158, 163, 164, 171, 175, 181, 199};
		int[] correctColumnList = {14, 13, 13, 13, 13, 14, 7, 14, 6, 5, 14, 7, 7, 7, 15, 22, 22, 23, 14, 14, 14, 14, 5, 13, 14};
	
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void primitiveCheck() {
		// number of primitives
		assertEquals(27, fileModel.getPrimitives().getPrimitiveObjectList().size());
		
		// position of primitives (variable names found everywhere)
		List<Integer> lineList = fileModel.getPrimitives().getLineNumbers();
		List<Integer> columnList = fileModel.getPrimitives().getColumnNumbers();
		
		int[] correctLineList = {60, 64, 64, 71, 73, 81, 85, 86, 87, 88, 108, 108, 108, 108, 112, 112, 112, 112, 131, 157, 161, 162, 168, 180, 184, 190, 197};
		int[] correctColumnList = {16, 16, 23, 5, 17, 9, 5, 8, 7, 9, 39, 49, 60, 69, 39, 49, 60, 69, 6, 10, 10, 10, 6, 6, 7, 7, 6};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void throwStatementCheck() {
		// number of throw statements (not throws)
		assertEquals(1, fileModel.getThrowStatements().getThrowStatementObjectList().size());
		
		// position of throw statements (position of the word "throw")
		List<Integer> lineList = fileModel.getThrowStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getThrowStatements().getColumnNumbers();
		
		int[] correctLineList = {194};
		int[] correctColumnList = {3};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void tryStatementCheck() {
		// number of try statements
		assertEquals(1, fileModel.getTryStatements().getTryStatementObjectList().size());
		
		// position of try statements (position of the word "try")
		List<Integer> lineList = fileModel.getTryStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getTryStatements().getColumnNumbers();
		
		int[] correctLineList = {183};
		int[] correctColumnList = {2};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void simpleNameCheck() {
		// number of simple names
		assertEquals(8, fileModel.getSimpleNames().getSimpleNameObjectList().size());
		
		// position of simple names (variable name of everything that is not primitive)
		List<Integer> lineList = fileModel.getSimpleNames().getLineNumbers();
		List<Integer> columnList = fileModel.getSimpleNames().getColumnNumbers();
		
		int[] correctLineList = {70, 84, 89, 97, 103, 125, 139, 189};
		int[] correctColumnList = {13, 8, 9, 13, 13, 10, 14, 32};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void switchStatementCheck() {
		// number of switch statements
		assertEquals(1, fileModel.getSwitchStatements().getSwitchStatementObjectList().size());
	
		// position of switch statements (position of "switch")
		List<Integer> lineList = fileModel.getSwitchStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getSwitchStatements().getColumnNumbers();
		
		int[] correctLineList = {147};
		int[] correctColumnList = {2};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void whileStatementCheck() {
		// number of while statements
		assertEquals(1, fileModel.getWhileStatements().getWhileStatementObjectList().size());
		
		// position of while statements (position of "while", excludes do-while)
		List<Integer> lineList = fileModel.getWhileStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getWhileStatements().getColumnNumbers();
		
		int[] correctLineList = {170};
		int[] correctColumnList = {2};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
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
*/	
}
