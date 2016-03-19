package obsolete;

import java.util.ArrayList;
import java.util.List;

import entities.ReturnStatementObject;



public class ReturnStatement__ {
	List<ReturnStatementObject> returnStatementObjectList;
	
	public ReturnStatement__() {
		returnStatementObjectList = new ArrayList<>();
	}

	public void addReturnStatement(String expression, String className, String methodName, int lineNumber, int columnNumber) {
		returnStatementObjectList.add(new ReturnStatementObject(expression, className, methodName, lineNumber, columnNumber));
	}
	
	public void printAllReturnStatements() {
		System.out.println("--- Return Statements ---");

		if(returnStatementObjectList.size() > 0) {
			for(ReturnStatementObject obj : returnStatementObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
}
