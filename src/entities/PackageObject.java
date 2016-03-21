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

}
