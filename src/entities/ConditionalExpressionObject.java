package entities;

public class ConditionalExpressionObject extends SuperEntityClass {
	String elseExpression;
	String thenExpression;
	
	public ConditionalExpressionObject() {
		
	}
	
	public void setElseExpression(String ee) {
		this.elseExpression = ee;
	}
	
	public String getElseExpression() {
		return this.elseExpression;
	}
	
	public void setThenExpression(String te) {
		this.thenExpression = te;
	}
	
	public String getThenExpression() {
		return this.thenExpression;
	}
	
}
