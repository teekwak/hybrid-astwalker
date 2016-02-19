package entities;

import java.util.ArrayList;
import java.util.List;

class WhileStatementObject {
	String condition;
	int lineNumber;
	int columnNumber;
	
	WhileStatementObject(String cond, int l, int c) {
		condition = cond;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(condition + " => " + lineNumber + " | " + columnNumber);
	}
}

public class WhileStatementExpressions {
	public List<WhileStatementObject> whileStatementObjectList;
	
	public WhileStatementExpressions() {
		this.whileStatementObjectList = new ArrayList<>();
	}
	
	public void addWhileStatement(String condition, int lineNumber, int columnNumber) {		
		whileStatementObjectList.add(new WhileStatementObject(condition, lineNumber, columnNumber));
	}
	
	public void printAllWhileStatements() {
		System.out.println("---While Statements---");

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
