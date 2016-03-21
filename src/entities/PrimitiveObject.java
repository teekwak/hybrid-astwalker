package entities;

import org.eclipse.jdt.core.dom.Type;

public class PrimitiveObject extends SuperEntityClass {
	
	Type type;
	
	public PrimitiveObject() {
		
	}

	public void setType(Type t) {
		this.type = t;
	}
	
	public Type getType() {
		return this.type;
	}
}
