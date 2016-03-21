package entities;

import org.eclipse.jdt.core.dom.Type;

public class ArrayObject extends SuperEntityClass {
	
	Type type;
	
	public ArrayObject() {
		
	}
	
	public void setType(Type t) {
		this.type = t;
	}
	
	public Type getType() {
		return this.type;
	}
	
	@Override
	public void printInfo() {
		System.out.println(this.name + " " + this.type);
	}
	
}
