package entities;

public class ReturnStatementObject extends SuperEntityClass {
	
	public ReturnStatementObject() {
		
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