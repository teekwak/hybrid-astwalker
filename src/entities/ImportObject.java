package entities;

public class ImportObject extends SuperEntityClass {

	String fullyQualifiedName;

	public ImportObject() {

	}

	public void setFullyQualifiedName(String fqn) {
		this.fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	@Override
	public void printInfo() {
		System.out.println(this.name + " => " + this.lineNumber + " | " + this.columnNumber);
	}

	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
}
