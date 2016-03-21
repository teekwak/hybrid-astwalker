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
	
}
