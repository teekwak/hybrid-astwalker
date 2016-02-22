package entities;

import java.util.ArrayList;
import java.util.List;

class ConditionalExpressionObject {
	String expression;
	String elseExpression;
	String thenExpression;
	int lineNumber;
	int columnNumber;
	
	ConditionalExpressionObject(String e, String eE, String tE, int l, int c) {
		expression = e;
		elseExpression = eE;
		thenExpression = tE;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(expression + " => " + lineNumber + " | " + columnNumber);
	}
}

public class ConditionalExpression__ {
	public List<ConditionalExpressionObject> conditionalExpressionObjectList;
	
	public ConditionalExpression__() {
		this.conditionalExpressionObjectList = new ArrayList<>();
	}
	
	public void addConditionalExpression(String expression, String elseExpression, String thenExpression, int lineNumber, int columnNumber) {
		conditionalExpressionObjectList.add(new ConditionalExpressionObject(expression, elseExpression, thenExpression, lineNumber, columnNumber));
	}
	
	public void printAllConditionalExpressions() {
		System.out.println("--- Conditional Expressions ---");

		if(conditionalExpressionObjectList.size() > 0) {
			for(ConditionalExpressionObject obj : conditionalExpressionObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
