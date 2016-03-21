package entities;

import org.eclipse.jdt.core.dom.Type;

public class WildcardObject extends SuperEntityClass {

	Type type;
	
	public WildcardObject() {
		
	}
	
	public void setType(Type t) {
		type = t;
	}
	
	public Type getType() {
		return type;
	}
	
}
