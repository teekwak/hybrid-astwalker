package entities;

import java.util.ArrayList;
import java.util.List;

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

public class IfStatement__ {
	public List<IfStatementObject> ifStatementObjectList;

	public IfStatement__() {
		this.ifStatementObjectList = new ArrayList<>();
	}

	public void addIfStatement(String expression, String className, String methodName, int lineNumber, int columnNumber) {
		ifStatementObjectList.add(new IfStatementObject(expression, className, methodName, lineNumber, columnNumber));
	}

	public List<IfStatementObject> getIfStatementObjectList() {
		return this.ifStatementObjectList;
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
	
	/*
	 * end testing
	 */
}
