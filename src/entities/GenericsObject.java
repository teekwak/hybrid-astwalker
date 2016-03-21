package entities;

import org.eclipse.jdt.core.dom.Type;

public class GenericsObject extends SuperEntityClass {
	
	Type type;
	
	public GenericsObject() {
		
	}

	public void setType(Type t) {
		this.type = t;
	}
	
	public Type getType() {
		return this.type;
	}
}
