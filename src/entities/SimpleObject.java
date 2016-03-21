package entities;

import org.eclipse.jdt.core.dom.Type;

public class SimpleObject extends SuperEntityClass {
	
	Type type;
	
	public SimpleObject() {
		
	}
	
	public void setType(Type t) {
		this.type = t;
	}
	
	public Type getType() {
		return this.type;
	}
}
