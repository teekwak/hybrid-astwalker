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
