package entities;

import java.util.ArrayList;
import java.util.List;

class DoStatementObject {
	String condition;
	int lineNumber;
	int columnNumber;
	
	DoStatementObject(String cond, int l, int c) {
		condition = cond;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(condition + " => " + lineNumber + " | " + columnNumber);
	}
}

public class DoStatementExpressions {
	List<DoStatementObject> doStatementObjectList;
	
	public DoStatementExpressions() {
		this.doStatementObjectList = new ArrayList<>();
	}
	
	public void addDoStatement(String condition, int lineNumber, int columnNumber) {		
		doStatementObjectList.add(new DoStatementObject(condition, lineNumber, columnNumber));
	}
	
	public void printAllDoStatements() {
		System.out.println("---Do Statements---");

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
}
