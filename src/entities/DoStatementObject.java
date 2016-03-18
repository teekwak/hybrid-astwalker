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

	@Override
	public void setSuperClass(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSuperClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addImplementsInterface(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getImplements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addThrowsException(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getThrowsException() {
		// TODO Auto-generated method stub
		return null;
	}

}
