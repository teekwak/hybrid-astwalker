package entities;

import java.util.ArrayList;
import java.util.List;

class IfStatementObject {
	String expression;
	int lineNumber;
	int columnNumber;

	IfStatementObject(String e, int l, int c) {
		expression = e;
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
	
	public void addIfStatement(String expression, int lineNumber, int columnNumber) {		
		ifStatementObjectList.add(new IfStatementObject(expression, lineNumber, columnNumber));
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
}
