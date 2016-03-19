package obsolete;

import java.util.ArrayList;
import java.util.List;

class ThrowStatementObject {
	String expression;
	String className; 
	String methodName;
	int lineNumber;
	int columnNumber;

	ThrowStatementObject(String e, String cn, String mn, int l, int c) {
		expression = e;
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

public class ThrowStatement__ {
	List<ThrowStatementObject> throwStatementObjectList;

	public ThrowStatement__() {
		throwStatementObjectList = new ArrayList<>();
	}

	public void addThrowStatement(String expression, String className, String methodName, int lineNumber, int columnNumber) {
		throwStatementObjectList.add(new ThrowStatementObject(expression, className, methodName, lineNumber, columnNumber));
	}
	
	public void printAllThrowStatements() {
		System.out.println("--- Throw Statements ---");

		if(throwStatementObjectList.size() > 0) {
			for(ThrowStatementObject obj : throwStatementObjectList) {
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

	public List<ThrowStatementObject> getThrowStatementObjectList() {
		return this.throwStatementObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ThrowStatementObject obj : throwStatementObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ThrowStatementObject obj : throwStatementObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getExpressions() {
		List<String> list = new ArrayList<>();
		for(ThrowStatementObject obj : throwStatementObjectList) {
			list.add(obj.expression);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
