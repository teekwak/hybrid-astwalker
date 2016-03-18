package entities;

import java.util.ArrayList;
import java.util.List;

public class ClassContainer {
	List<ClassObject> classObjectList;
	
	public ClassContainer() {
		classObjectList = new ArrayList<>();
	}
	
	public void printAll() {
		for(ClassObject co : classObjectList) {
			System.out.println(co.getName());
			
			for(Entity e : co.entitiesInsideClass) {
				e.printInfo();
			}		
		}
	}
	
	public void addClass(ClassObject co) {
		classObjectList.add(co);
	}
}
