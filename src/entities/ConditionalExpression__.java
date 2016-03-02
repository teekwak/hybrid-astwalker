package entities;

import java.util.ArrayList;
import java.util.List;

class ConditionalExpressionObject {
	String expression;
	String elseExpression;
	String thenExpression;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	ConditionalExpressionObject(String e, String eE, String tE, String cn, String mn, int l, int c) {
		expression = e;
		elseExpression = eE;
		thenExpression = tE;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(expression + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class ConditionalExpression__ {
	public List<ConditionalExpressionObject> conditionalExpressionObjectList;

	public ConditionalExpression__() {
		this.conditionalExpressionObjectList = new ArrayList<>();
	}

	public void addConditionalExpression(String expression, String elseExpression, String thenExpression, String className, String methodName, int lineNumber, int columnNumber) {
		conditionalExpressionObjectList.add(new ConditionalExpressionObject(expression, elseExpression, thenExpression, className, methodName, lineNumber, columnNumber));
	}

	public List<ConditionalExpressionObject> getConditionalExpressionObjectList() {
		return this.conditionalExpressionObjectList;
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
	
	/*
	 * start testing
	 */
	
	/*
	 * end testing
	 */
}
