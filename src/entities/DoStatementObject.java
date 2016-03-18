package entities;

public class DoStatementObject implements Entity {

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

}
