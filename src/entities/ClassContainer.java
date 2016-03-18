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
			StringBuilder s = new StringBuilder();
			s.append(co.getName());
					
			if(co.getSuperClass() != null) {
				s.append(" extends " + co.getSuperClass());
			}
			
			if(co.getImplements().size() > 0) {
				s.append(" implements");
				for(String imp : co.getImplements()) {
					s.append(" " + imp);
				}
			}

			System.out.println(s.toString());
			
			for(Entity e : co.entitiesInsideClass) {
				e.printInfo();
			}		
		}
	}
	
	public void addClass(ClassObject co) {
		classObjectList.add(co);
	}
}
