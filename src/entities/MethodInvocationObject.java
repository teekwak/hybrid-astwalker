package entities;

import java.util.List;

public class MethodInvocationObject extends SuperEntityClass {

	String fullyQualifiedName;
	List<?> argumentsList;
	
	public MethodInvocationObject() {
		
	}
	
	public void setFullyQualifiedName(String fqn) {
		fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	public void setArguments(List<?> al) {
		argumentsList = al;
	}
	
	public List<?> getArguments() {
		return argumentsList;
	}
	
}
