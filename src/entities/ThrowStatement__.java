package entities;

import java.util.ArrayList;
import java.util.List;

class ThrowStatementObject {
	String expression;
	int lineNumber;
	int columnNumber;

	ThrowStatementObject(String e, int l, int c) {
		expression = e;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(expression + " => " + lineNumber + " | " + columnNumber);
	}
}

public class ThrowStatement__ {
	List<ThrowStatementObject> ThrowStatementObjectList;
	
	public ThrowStatement__() {
		ThrowStatementObjectList = new ArrayList<>();
	}
	
	public void addThrowStatement(String expression, int lineNumber, int columnNumber) {
		ThrowStatementObjectList.add(new ThrowStatementObject(expression, lineNumber, columnNumber));
	}
	
	public void printAllThrowStatements() {
		System.out.println("--- Throw Statements ---");

		if(ThrowStatementObjectList.size() > 0) {
			for(ThrowStatementObject obj : ThrowStatementObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
