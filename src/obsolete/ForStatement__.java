package obsolete;

import java.util.ArrayList;
import java.util.List;
/*
class ForStatementObject {
	String expression;
	String className;
	String methodName;
	boolean enhanced;
	int lineNumber;
	int columnNumber;

	ForStatementObject(String ex, String cn, String mn, boolean e, int l, int c) {
		expression = ex;
		className = cn;
		methodName = mn;
		enhanced = e;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(expression + " (enhanced: " + enhanced + ")"+ " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}
*/

import entities.ForStatementObject;

public class ForStatement__ {
	List<ForStatementObject> forStatementObjectList;

	public ForStatement__() {
		this.forStatementObjectList = new ArrayList<>();
	}

	public void addForStatement(String expression, String className, String methodName, boolean enhanced, int lineNumber, int columnNumber) {
		forStatementObjectList.add(new ForStatementObject(expression, className, methodName, enhanced, lineNumber, columnNumber));
	}
	
	public void printAllForStatements() {
		System.out.println("--- For Statements ---");

		if(forStatementObjectList.size() > 0) {
			for(ForStatementObject obj : forStatementObjectList) {
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
	
	public List<ForStatementObject> getForStatementObjectList() {
		return this.forStatementObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ForStatementObject obj : forStatementObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ForStatementObject obj : forStatementObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getExpressions() {
		List<String> list = new ArrayList<>();
		for(ForStatementObject obj : forStatementObjectList) {
			list.add(obj.expression);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
