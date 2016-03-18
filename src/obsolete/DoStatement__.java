package obsolete;

import java.util.ArrayList;
import java.util.List;

class DoStatementObject {
	String expression;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	DoStatementObject(String ex, String cn, String mn, int l, int c) {
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

public class DoStatement__ {
	List<DoStatementObject> doStatementObjectList;

	public DoStatement__() {
		this.doStatementObjectList = new ArrayList<>();
	}

	public void addDoStatement(String expression, String className, String methodName, int lineNumber, int columnNumber) {
		doStatementObjectList.add(new DoStatementObject(expression, className, methodName, lineNumber, columnNumber));
	}
	
	public void printAllDoStatements() {
		System.out.println("--- Do Statements ---");

		if(doStatementObjectList.size() > 0) {
			for(DoStatementObject d : doStatementObjectList) {
				d.printEntity();
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
	
	public List<DoStatementObject> getDoStatementObjectList() {
		return this.doStatementObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(DoStatementObject obj : doStatementObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(DoStatementObject obj : doStatementObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getExpressions() {
		List<String> list = new ArrayList<>();
		for(DoStatementObject obj : doStatementObjectList) {
			list.add(obj.expression);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
