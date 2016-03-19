package entities;

public class WhileStatementObject extends SuperEntityClass {

	public WhileStatementObject() {
		
	}
	
	@Override
	public void printInfo() {
		System.out.println(this.name + " " + this.lineNumber + " " + this.columnNumber);
		
	}

	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
