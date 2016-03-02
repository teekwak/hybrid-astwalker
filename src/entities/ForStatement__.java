package entities;

import java.util.ArrayList;
import java.util.List;

class ForStatementObject {
	String condition;
	String className;
	String methodName;
	boolean enhanced;
	int lineNumber;
	int columnNumber;

	ForStatementObject(String cond, String cn, String mn, boolean e, int l, int c) {
		condition = cond;
		className = cn;
		methodName = mn;
		enhanced = e;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(condition + " (enhanced: " + enhanced + ")"+ " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class ForStatement__ {
	List<ForStatementObject> forStatementObjectList;

	public ForStatement__() {
		this.forStatementObjectList = new ArrayList<>();
	}

	public void addForStatement(String condition, String className, String methodName, boolean enhanced, int lineNumber, int columnNumber) {
		forStatementObjectList.add(new ForStatementObject(condition, className, methodName, enhanced, lineNumber, columnNumber));
	}

	public List<ForStatementObject> getForStatementObjectList() {
		return this.forStatementObjectList;
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
	
	/*
	 * end testing
	 */
}
