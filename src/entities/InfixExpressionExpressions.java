package entities;

import java.util.ArrayList;
import java.util.List;

class InfixExpressionObject {
	String operator;
	String leftOperand;
	String rightOperand;

	int lineNumber;
	int columnNumber;
	
	InfixExpressionObject(String o, String left, String right, int l, int c) {
		operator = o;
		leftOperand = left;
		rightOperand = right;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(leftOperand + " " + operator + " " + rightOperand + " => " + lineNumber + " | " + columnNumber);
	}
}

public class InfixExpressionExpressions {
	public List<InfixExpressionObject> infixExpressionObjectList;
	
	public InfixExpressionExpressions() {
		this.infixExpressionObjectList = new ArrayList<>();
	}
	
	public void addInfixExpression(String operator, String leftOperand, String rightOperand, int lineNumber, int columnNumber) {		
		infixExpressionObjectList.add(new InfixExpressionObject(operator, leftOperand, rightOperand, lineNumber, columnNumber));
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
}
