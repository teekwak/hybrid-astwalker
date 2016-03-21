package entities;

public abstract class SuperEntityClass implements Entity {

	String name;
	int lineNumber;
	int columnNumber;
	
	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public void setLineNumber(int n) {
		this.lineNumber = n;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}

	public void setColumnNumber(int n) {
		this.columnNumber = n;
	}

	public int getColumnNumber() {
		return this.columnNumber;
	}
	
	public void addChild(Entity e) {
		
	}
	
	public void printInfo() {
		System.out.println(this.name + " (" + this.lineNumber + " | " + this.columnNumber + ")");		
	}
	
}
