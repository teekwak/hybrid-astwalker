package entities;

import java.util.ArrayList;
import java.util.List;

class DoStatementObject {
	String condition;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	DoStatementObject(String cond, String cn, String mn, int l, int c) {
		condition = cond;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(condition + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class DoStatement__ {
	List<DoStatementObject> doStatementObjectList;

	public DoStatement__() {
		this.doStatementObjectList = new ArrayList<>();
	}

	public void addDoStatement(String condition, String className, String methodName, int lineNumber, int columnNumber) {
		doStatementObjectList.add(new DoStatementObject(condition, className, methodName, lineNumber, columnNumber));
	}

	public List<DoStatementObject> getDoStatementObjectList() {
		return this.doStatementObjectList;
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
	
	/*
	 * end testing
	 */
}
