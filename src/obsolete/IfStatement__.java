package obsolete;

import java.util.ArrayList;
import java.util.List;
/*
class IfStatementObject {
	String expression;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	IfStatementObject(String e, String cn, String mn, int l, int c) {
		expression = e;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(expression + " => " + lineNumber + " | " + columnNumber);
	}
}
*/

import entities.IfStatementObject;

public class IfStatement__ {
	public List<IfStatementObject> ifStatementObjectList;

	public IfStatement__() {
		this.ifStatementObjectList = new ArrayList<>();
	}

	public void addIfStatement(String expression, String className, String methodName, int lineNumber, int columnNumber) {
		ifStatementObjectList.add(new IfStatementObject(expression, className, methodName, lineNumber, columnNumber));
	}
	
	public void printAllIfStatements() {
		System.out.println("--- If Statements ---");

		if(ifStatementObjectList.size() > 0) {
			for(IfStatementObject obj : ifStatementObjectList) {
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
	
	public List<IfStatementObject> getIfStatementObjectList() {
		return this.ifStatementObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(IfStatementObject obj : ifStatementObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(IfStatementObject obj : ifStatementObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getExpressions() {
		List<String> list = new ArrayList<>();
		for(IfStatementObject obj : ifStatementObjectList) {
			list.add(obj.expression);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
