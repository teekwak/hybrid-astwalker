package entities;

import java.util.ArrayList;
import java.util.List;

class WhileStatementObject {
	String condition;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	WhileStatementObject(String cond, String cn, String mn, int l, int c) {
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

public class WhileStatement__ {
	public List<WhileStatementObject> whileStatementObjectList;

	public WhileStatement__() {
		this.whileStatementObjectList = new ArrayList<>();
	}

	public void addWhileStatement(String condition, String className, String methodName, int lineNumber, int columnNumber) {
		whileStatementObjectList.add(new WhileStatementObject(condition, className, methodName, lineNumber, columnNumber));
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
}
