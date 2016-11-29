package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationObject extends SuperEntityClass {

	private String declaringClass;
	private String callingClass;
	private String fullyQualifiedName;
	private List<?> argumentsList;
	private List<String> argumentTypesList;
	
	public MethodInvocationObject() {
		this.argumentsList = new ArrayList<>();
	}
	
	public void setCallingClass(String s) {
		this.callingClass = s;
	}
	
	public String getCallingClass() {
		return this.callingClass;
	}
	
	public void setFullyQualifiedName(String fqn) {
		fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	public void setDeclaringClass(String c) {
		this.declaringClass = c;
	}
	
	public String getDeclaringClass() {
		return this.declaringClass;
	}
	
	public void setArguments(List<?> al) {
		argumentsList = al;
	}
	
	public List<?> getArguments() {
		return argumentsList;
	}
	
	public void setArgumentTypes(List<String> at) {
		this.argumentTypesList = at;
	}
	
	public List<String> getArgumentTypes() {
		return this.argumentTypesList;
	}
}
