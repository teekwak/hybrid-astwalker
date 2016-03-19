package entities;

import org.eclipse.jdt.core.dom.Type;

public class CatchClauseObject extends SuperEntityClass {
	Type exception;

	@Override
	public void printInfo() {
		System.out.println(this.name + " " + this.exception.toString());
	}

	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setType(Type t) {
		this.exception = t;
	}
	
	public Type getType() {
		return this.exception;
	}
	
}
