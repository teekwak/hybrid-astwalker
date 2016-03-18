package entities;

import java.util.ArrayList;
import java.util.List;

public class ClassObject implements Entity {

	String name;
	int lineNumber;
	int columnNumber;
	List<Entity> entitiesInsideClass;

	public ClassObject() {
		this.entitiesInsideClass = new ArrayList<>();
	}
	
	public ClassObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
		entitiesInsideClass = new ArrayList<>();
	}
		
	public void addChild(Entity e) {
		entitiesInsideClass.add(e);
	}
	
	public void printChildren() {
		
	}

	@Override
	public void printInfo() {
		System.out.println(this.name);
		for(Entity e : entitiesInsideClass) {
			e.printInfo();
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String n) {
		this.name = n;
	}

	@Override
	public void setLineNumber(int n) {
		this.lineNumber = n;
	}

	@Override
	public int getLineNumber() {
		return this.lineNumber;
	}

	@Override
	public void setColumnNumber(int n) {
		this.columnNumber = n;
	}

	@Override
	public int getColumnNumber() {
		return this.columnNumber;
	}
}
