package entities;

import java.util.ArrayList;
import java.util.List;

class ForStatementObject {
	String condition;
	boolean enhanced;
	int lineNumber;
	int columnNumber;
	
	ForStatementObject(String cond, boolean e, int l, int c) {
		condition = cond;
		enhanced = e;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(condition + " (enhanced: " + enhanced + ") "+ " => " + lineNumber + " | " + columnNumber);
	}
}

public class ForStatementExpressions {
	List<ForStatementObject> forStatementObjectList;
	
	public ForStatementExpressions() {
		this.forStatementObjectList = new ArrayList<>();
	}
	
	public void addForStatement(String condition, boolean enhanced, int lineNumber, int columnNumber) {		
		forStatementObjectList.add(new ForStatementObject(condition, enhanced, lineNumber, columnNumber));
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
}
