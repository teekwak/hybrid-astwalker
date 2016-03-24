package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationObject extends SuperEntityClass {

	String declaringClass;
	String callingClass;
	String fullyQualifiedName;
	List<?> argumentsList;
	List<String> argumentTypesList;
	
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
	
	public void printInfo() {
		StringBuilder s = new StringBuilder();
		s.append("\t" + this.name);
		
		s.append("(");
		
		for(int i = 0; i < this.argumentsList.size(); i++) {
			if(i == this.argumentsList.size() - 1) {
				s.append(this.argumentsList.get(i));
			}
			else {
				s.append(this.argumentsList.get(i) + ", ");
			}	
		}
		
		s.append(")");
		s.append(" [" + this.lineNumber + " | " + this.columnNumber + "]");
		
		System.out.println(s.toString());
	}
	
}
