package entities;

import java.util.List;

public class ArrayObject implements Entity {

	String name;
	int lineNumber;
	int columnNumber;
	
	public ArrayObject() {
		
	}
	
	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub	
	}
	
	@Override
	public void printInfo() {
		
	}
	
	@Override
	public void setName(String n) {
		this.name = n;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setLineNumber(int n) {
		this.lineNumber = n;
		
	}

	@Override
	public int getLineNumber() {
		return this.lineNumber;
	}

	@Override
	public void setColumnNumber(int n) {
		this.columnNumber = n;
	}

	@Override
	public int getColumnNumber() {
		return this.columnNumber;
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

}
