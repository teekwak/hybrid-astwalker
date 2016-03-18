package entities;

import java.util.ArrayList;
import java.util.List;

public class ClassObject implements Entity {

	String name;
	int lineNumber;
	int columnNumber;
	List<Entity> entitiesInsideClass;

	public ClassObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
		entitiesInsideClass = new ArrayList<>();
	}
		
	public void addChild(Entity e) {
		entitiesInsideClass.add(e);
	}
	
}
