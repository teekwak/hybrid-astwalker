package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationObject extends SuperEntityClass {

	String fullyQualifiedName;
	List<?> argumentsList;
	
	public MethodInvocationObject() {
		this.argumentsList = new ArrayList<>();
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
