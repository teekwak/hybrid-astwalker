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
	
	@Test
	public void classCheck() {
		// number of classes (regular + extends super)
		int normalClassCount = fileModel.getClasses().getClassObjectList().size();
		int superClassCount = fileModel.getClasses().getSuperClassObjectList().size();
		
		assertEquals(2, normalClassCount);
		assertEquals(1, superClassCount);
	
		// position of classes (starts at "class" or "public class")
		List<Integer> lineList = fileModel.getClasses().getLineNumbers();
		List<Integer> columnList = fileModel.getClasses().getColumnNumbers();
		List<Integer> superClassLineList = fileModel.getClasses().getSuperClassLineNumbers();
		List<String> nameList = fileModel.getClasses().getNames();
		List<String> superClassNameList = fileModel.getClasses().getSuperClassNames();
		
		int[] correctLineList = {18, 67};
		int[] correctColumnList = {0, 4};
		int[] correctSuperClassLineList = {18};
		String[] correctNameList = {"GMailSender", "ByteArrayDataSource"};
		String[] correctSuperClassNameList = {"javax.mail.Authenticator"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctSuperClassLineList, superClassLineList));
		assertTrue(compareTwoSets(correctNameList, nameList));
		assertTrue(compareTwoSets(correctSuperClassNameList, superClassNameList));
	}
	
	@Test
	public void arrayCheck() {
		// number of arrays
		assertEquals(3, fileModel.getArrays().getArrayObjectList().size());
		
		// position of arrays (where variable name is, ignores arrays if return type)
		List<Integer> lineList = fileModel.getArrays().getLineNumbers();
		List<Integer> columnList = fileModel.getArrays().getColumnNumbers();
		List<String> nameList = fileModel.getArrays().getNames();
				
		int[] correctLineList = {68, 71, 77};
		int[] correctColumnList = {23, 42, 42};
		String[] correctNameList = {"data", "data", "data"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	@Test
	public void catchClauseCheck() {
		// number of catch clauses
		assertEquals(1, fileModel.getCatchClauses().getCatchClauseObjectList().size());
		
		// position of catch clauses (where the exception variable is)
		List<Integer> lineList = fileModel.getCatchClauses().getLineNumbers();
		List<Integer> columnList = fileModel.getCatchClauses().getColumnNumbers();
		List<String> nameList = fileModel.getCatchClauses().getNames();
		
		int[] correctLineList = {62};
		int[] correctColumnList = {25};
		String[] correctNameList = {"e"};		
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	
	@Test
	public void conditionalExpressionCheck() {
		// number of conditional expressions
		assertEquals(0, fileModel.getConditionalExpressions().getConditionalExpressionObjectList().size());
		
		// position of conditional expression (beginning of expression)
		List<Integer> lineList = fileModel.getConditionalExpressions().getLineNumbers();
		List<Integer> columnList = fileModel.getConditionalExpressions().getColumnNumbers();
		List<String> expressionList = fileModel.getConditionalExpressions().getExpressions();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctExpressionList = {};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	@Test
	public void doStatementCheck() {
		// number of do statements
		assertEquals(0, fileModel.getDoStatements().getDoStatementObjectList().size());
		
		// position of each do statement (where the word "do" is)
		List<Integer> lineList = fileModel.getDoStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getDoStatements().getColumnNumbers();
		List<String> expressionList = fileModel.getDoStatements().getExpressions();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctExpressionList = {};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));		
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	@Test
	public void forStatementCheck() {	
		// number of for statements
		assertEquals(0, fileModel.getForStatements().getForStatementObjectList().size());
		
		// position of for statements (where variable in enhanced, where the word "for" is for not)
		List<Integer> lineList = fileModel.getForStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getForStatements().getColumnNumbers();
		List<String> expressionList = fileModel.getForStatements().getExpressions();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctExpressionList = {};
				
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	@Test
	public void genericsCheck() {
		// number of generics
		assertEquals(0, fileModel.getGenerics().getGenericsObjectList().size());
		
		// position of generics (everywhere)
		List<Integer> lineList = fileModel.getGenerics().getLineNumbers();
		List<Integer> columnList = fileModel.getGenerics().getColumnNumbers();
		List<String> nameList = fileModel.getGenerics().getNames();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctNameList = {};		
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	@Test
	public void ifStatementCheck() {
		// number of if statements (even in "else if")
		assertEquals(2, fileModel.getIfStatements().getIfStatementObjectList().size());
		
		// position of if statements (starts at "if")
		List<Integer> lineList = fileModel.getIfStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getIfStatements().getColumnNumbers();
		List<String> expressionList = fileModel.getIfStatements().getExpressions();
				
		int[] correctLineList = {57, 87};
		int[] correctColumnList = {8, 12};
		String[] correctExpressionList = {"recipients.indexOf(',') > 0", "type == null"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	@Test
	public void infixExpressionCheck() {
		// number of infix expressions
		assertEquals(2, fileModel.getInfixExpressions().getInfixExpressionObjectList().size());
		
		// position of infix expression (position of left operand)
		List<Integer> lineList = fileModel.getInfixExpressions().getLineNumbers();
		List<Integer> columnList = fileModel.getInfixExpressions().getColumnNumbers();
		List<String> operatorList = fileModel.getInfixExpressions().getOperator();
		
		int[] correctLineList = {57, 87};
		int[] correctColumnList = {12, 16};
		String[] correctOperatorList = {">", "=="};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));		
		assertTrue(compareTwoSets(correctOperatorList, operatorList));
	}

	@Test
	public void interfaceCheck() {
		// number of interfaces (interfaces + implements interface)
		int interfaceCount = fileModel.getInterfaces().getInterfaceObjectList().size();
		int implementsCount = fileModel.getInterfaces().getImplementsObjectList().size();
		
		assertEquals(0, interfaceCount);
		assertEquals(1, implementsCount);
		
		// position of interfaces (starts at "interface")
		List<Integer> lineList = fileModel.getInterfaces().getLineNumbers();
		List<Integer> columnList = fileModel.getInterfaces().getColumnNumbers();
		List<Integer> implementsLineList = fileModel.getInterfaces().getImplementsLineNumbers();
		List<String> nameList = fileModel.getInterfaces().getNames();
		List<String> implementsNameList = fileModel.getInterfaces().getImplementsNames();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		int[] correctImplementsLineList = {67};
		String[] correctNameList = {};
		String[] correctImplementsNameList = {"DataSource"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctImplementsLineList, implementsLineList));
		assertTrue(compareTwoSets(correctNameList, nameList));
		assertTrue(compareTwoSets(correctImplementsNameList, implementsNameList));
	}
	
	@Test
	public void methodDeclarationCheck() {
		// number of method declarations (including constructors)
		assertEquals(10, fileModel.getMethodDeclarations().getMethodDeclarationObjectList().size());
		
		// position of method declarations (begins at method name)
		List<Integer> lineList = fileModel.getMethodDeclarations().getLineNumbers();
		List<Integer> columnList = fileModel.getMethodDeclarations().getColumnNumbers();
		List<String> nameList = fileModel.getMethodDeclarations().getNames();
		
		int[] correctLineList = 	{28, 46, 50, 71, 77, 82, 86, 93, 97, 101};
		int[] correctColumnList = 	{11, 37, 29, 15, 15, 20, 22, 27, 22, 28};
		String[] correctNameList = 	{"GMailSender", "getPasswordAuthentication", "sendMail", "ByteArrayDataSource", "ByteArrayDataSource", "setType", "getContentType", "getInputStream", "getName", "getOutputStream"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));	
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	@Test
	public void methodInvocationCheck() {
		// number of UNIQUE method invocations
		assertEquals(12, fileModel.getMethodInvocations().getMethodInvocationObjectList().size());
		
		// position of method invocations (where the actual name of the method starts)
		List<Map<String, Map<Integer, Integer>>> listOfMaps = fileModel.getMethodInvocations().getInvocationClassAndPositionsMaps();
	
		Map<Integer, List<Integer>> allPositions = new TreeMap<>();
		
		for(Map<String, Map<Integer, Integer>> bigMap : listOfMaps) {
			for(Map.Entry<String, Map<Integer, Integer>> smallMap : bigMap.entrySet()) {
				for(Map.Entry<Integer, Integer> tinyMap : smallMap.getValue().entrySet()) {
					if(allPositions.get(tinyMap.getKey()) != null) {
						allPositions.get(tinyMap.getKey()).add(tinyMap.getValue());
					}
					else {
						List<Integer> list = new ArrayList<>();
						list.add(tinyMap.getValue());
						allPositions.put(tinyMap.getKey(), list);
					}
				}
			}
		}
		
		List<Integer> lineList = new ArrayList<>();
		List<Integer> columnList = new ArrayList<>();
		List<String> nameList = fileModel.getMethodInvocations().getNames();
		
		for(Map.Entry<Integer, List<Integer>> entry : allPositions.entrySet()) {
			for(Integer i : entry.getValue()) {
				lineList.add(entry.getKey());
				columnList.add(i);
			}
		}
		
		// number of ALL method invocations
		assertEquals(17, lineList.size());
		assertEquals(17, columnList.size());
		assertEquals(12, nameList.size());
		
		int[] correctLineList = 	{33, 34, 35, 36, 37, 38, 40, 41, 43, 54, 55, 56, 57, 58, 58, 60, 61};
		int[] correctColumnList = 	{14, 14, 14, 14, 14, 14, 14, 14, 26, 16, 16, 16, 23, 20, 76, 20, 18};
		String[] correctNameList = 	{"setProperty", "setProperty", "put", "getDefaultInstance", "setSender", "setSubject", "setDataHandler", "indexOf", "setRecipients", "parse", "setRecipient", "send"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}

	@Test
	public void primitiveCheck() {
		// number of primitives
		assertEquals(0, fileModel.getPrimitives().getPrimitiveObjectList().size());
		
		// position of primitives (variable names found everywhere)
		List<Integer> lineList = fileModel.getPrimitives().getLineNumbers();
		List<Integer> columnList = fileModel.getPrimitives().getColumnNumbers();
		List<String> nameList = fileModel.getPrimitives().getNames();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctNameList = {};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	@Test
	public void throwStatementCheck() {
		// number of throw statements (not throws)
		assertEquals(1, fileModel.getThrowStatements().getThrowStatementObjectList().size());
		
		// position of throw statements (position of the word "throw")
		List<Integer> lineList = fileModel.getThrowStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getThrowStatements().getColumnNumbers();
		List<String> expressionList = fileModel.getThrowStatements().getExpressions();
		
		int[] correctLineList = {102};
		int[] correctColumnList = {12};
		String[] correctExpressionList = {"new IOException(\"Not Supported\")"};

		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	@Test
	public void tryStatementCheck() {
		// number of try statements
		assertEquals(1, fileModel.getTryStatements().getTryStatementObjectList().size());
		
		// position of try statements (position of the word "try")
		List<Integer> lineList = fileModel.getTryStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getTryStatements().getColumnNumbers();
		
		int[] correctLineList = {51};
		int[] correctColumnList = {8};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}
	
	@Test
	public void simpleNameCheck() {
		// number of simple names
		assertEquals(17, fileModel.getSimpleNames().getSimpleNameObjectList().size());
		
		// position of simple names (variable name of everything that is not primitive)
		List<Integer> lineList = fileModel.getSimpleNames().getLineNumbers();
		List<Integer> columnList = fileModel.getSimpleNames().getColumnNumbers();
		List<String> nameList = fileModel.getSimpleNames().getNames();
		
		int[] correctLineList = 	{19, 20, 21, 22, 28, 28, 32, 50, 50, 50, 50, 52, 53, 62, 69, 71, 82};
		int[] correctColumnList = 	{19, 19, 19, 20, 30, 43, 19, 45, 61, 74, 89, 20, 20, 25, 23, 55, 35};
		String[] correctNameList = 	{"mailhost", "user", "password", "session", "user", "password", "props", "subject", "body", "sender", "recipients", "message", "handler", "e", "type", "type", "type"};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctNameList, nameList));
	}
	
	@Test
	public void switchStatementCheck() {
		// number of switch statements
		assertEquals(0, fileModel.getSwitchStatements().getSwitchStatementObjectList().size());
	
		// position of switch statements (position of "switch")
		List<Integer> lineList = fileModel.getSwitchStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getSwitchStatements().getColumnNumbers();
		List<String> expressionList = fileModel.getSwitchStatements().getExpressions();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctExpressionList = {};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	
	
	@Test
	public void whileStatementCheck() {
		// number of while statements
		assertEquals(0, fileModel.getWhileStatements().getWhileStatementObjectList().size());
		
		// position of while statements (position of "while", excludes do-while)
		List<Integer> lineList = fileModel.getWhileStatements().getLineNumbers();
		List<Integer> columnList = fileModel.getWhileStatements().getColumnNumbers();
		List<String> expressionList = fileModel.getWhileStatements().getExpressions();
		
		int[] correctLineList = {};
		int[] correctColumnList = {};
		String[] correctExpressionList = {};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
		assertTrue(compareTwoSets(correctExpressionList, expressionList));
	}
	
	@Test
	public void wildcardCheck() {
		// number of wildcards (only in field declarations)
		assertEquals(0, fileModel.getWildcards().getWildcardObjectList().size());
		
		// position of wildcards (position of question mark)
		List<Integer> lineList = fileModel.getWildcards().getLineNumbers();
		List<Integer> columnList = fileModel.getWildcards().getColumnNumbers();
				
		int[] correctLineList = {};
		int[] correctColumnList = {};
		
		assertTrue(compareTwoSets(correctLineList, lineList));
		assertTrue(compareTwoSets(correctColumnList, columnList));
	}	
}
