package entities;

public class PackageObject extends SuperEntityClass {
	
	String fullyQualifiedName;
	
	public PackageObject() {
		
	}

	public void setFullyQualifiedName(String fqn) {
		this.fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	public void printInfo() {
		System.out.println(this.fullyQualifiedName + " => " + lineNumber + " | " + columnNumber);		
	}

	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
