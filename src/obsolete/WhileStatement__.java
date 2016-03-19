package obsolete;

import java.util.ArrayList;
import java.util.List;

import entities.WhileStatementObject;
/*
class WhileStatementObject {
	String expression;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	WhileStatementObject(String ex, String cn, String mn, int l, int c) {
		expression = ex;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(expression + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}
*/
public class WhileStatement__ {
	public List<WhileStatementObject> whileStatementObjectList;

	public WhileStatement__() {
		this.whileStatementObjectList = new ArrayList<>();
	}

	public void addWhileStatement(String expression, String className, String methodName, int lineNumber, int columnNumber) {
		whileStatementObjectList.add(new WhileStatementObject(expression, className, methodName, lineNumber, columnNumber));
	}

	public void printAllWhileStatements() {
		System.out.println("--- While Statements ---");

		if(whileStatementObjectList.size() > 0) {
			for(WhileStatementObject obj : whileStatementObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
	/*
	 * start testing
	 */
	
	public List<WhileStatementObject> getWhileStatementObjectList() {
		return this.whileStatementObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(WhileStatementObject obj : whileStatementObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(WhileStatementObject obj : whileStatementObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getExpressions() {
		List<String> list = new ArrayList<>();
		for(WhileStatementObject obj : whileStatementObjectList) {
			list.add(obj.expression);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
