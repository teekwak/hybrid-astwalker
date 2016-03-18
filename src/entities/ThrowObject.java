package entities;

import java.util.List;

public class ThrowObject extends SuperEntityClass {

	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printInfo() {
		System.out.println(this.name + " " + this.lineNumber + " " + this.columnNumber);
	}
}
