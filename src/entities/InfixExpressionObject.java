package entities;

public class InfixExpressionObject extends SuperEntityClass {
	String operator;
	String leftOperand;
	String rightOperand;
	
	public void setOperator(String o) {
		this.operator = o;
	}
	
	public String getOperator() {
		return this.operator;
	}
	
	public void setLeftOperand(String lo) {
		this.leftOperand = lo;
	}
	
	public String getLeftOperand() {
		return this.leftOperand;
	}
	
	public void setRightOperand(String ro) {
		this.rightOperand = ro;
	}
	
	public String getRightOperand() {
		return this.rightOperand;
	}
	
}
