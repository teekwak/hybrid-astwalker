package entities;

import java.util.ArrayList;
import java.util.List;

class InfixExpressionObject {
	String operator;
	String leftOperand;
	String rightOperand;
	
	String className;
	String methodName;
	
	int lineNumber;
	int columnNumber;

	InfixExpressionObject(String o, String left, String right, String cn, String mn, int l, int c) {
		operator = o;
		leftOperand = left;
		rightOperand = right;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(leftOperand + " " + operator + " " + rightOperand + " => " + lineNumber + " | " + columnNumber);
	}
}

public class InfixExpression__ {
	public List<InfixExpressionObject> infixExpressionObjectList;

	public InfixExpression__() {
		this.infixExpressionObjectList = new ArrayList<>();
	}

	public void addInfixExpression(String operator, String leftOperand, String rightOperand, String className, String methodName, int lineNumber, int columnNumber) {
		infixExpressionObjectList.add(new InfixExpressionObject(operator, leftOperand, rightOperand, className, methodName, lineNumber, columnNumber));
	}
	
	public void printAllInfixExpressions() {
		System.out.println("--- Infix Expressions ---");

		if(infixExpressionObjectList.size() > 0) {
			for(InfixExpressionObject obj : infixExpressionObjectList) {
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

	public List<InfixExpressionObject> getInfixExpressionObjectList() {
		return this.infixExpressionObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(InfixExpressionObject obj : infixExpressionObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(InfixExpressionObject obj : infixExpressionObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getOperator() {
		List<String> list = new ArrayList<>();
		for(InfixExpressionObject obj : infixExpressionObjectList) {
			list.add(obj.operator);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
