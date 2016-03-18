package entities;

import java.util.List;

public class DoStatementObject extends SuperEntityClass {

	String name;
	int lineNumber;
	int columnNumber;
	
	public DoStatementObject() {
		
	}
	
	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void printInfo() {
		System.out.println(this.name + " " + this.lineNumber + " " + this.columnNumber);
	}
}
