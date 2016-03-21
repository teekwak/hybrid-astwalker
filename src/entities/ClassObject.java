package entities;

import java.util.ArrayList;
import java.util.List;

public class ClassObject extends SuperEntityClass {

	String name;
	int lineNumber;
	int columnNumber;
	String superClass;
	List<String> implementsList;
	List<Entity> entitiesInsideClass;
	
	List<Entity> classList;
	List<Entity> genericsList;
	List<Entity> importObjectList;
	List<MethodDeclarationObject> methodDeclarationList;
	Entity packageObject;
	List<Entity> primitiveList;
	List<Entity> simpleList;
	List<Entity> wildcardList;
	
	public ClassObject() {
		this.implementsList = new ArrayList<>();
		this.entitiesInsideClass = new ArrayList<>();
		
		this.classList = new ArrayList<>();
		this.genericsList = new ArrayList<>();
		this.importObjectList = new ArrayList<>();
		this.methodDeclarationList = new ArrayList<>();
		this.packageObject = null;
		this.primitiveList = new ArrayList<>();
		this.simpleList = new ArrayList<>();
		this.wildcardList = new ArrayList<>();
	}
	
	public ClassObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
		entitiesInsideClass = new ArrayList<>();
	}
	
	public void setSuperClass(String s) {
		this.superClass = s;
	}
	
	public String getSuperClass() {
		return this.superClass;
	}
	
	@Override
	public void printInfo() {
		StringBuilder s = new StringBuilder();
		s.append("ClassObject " + this.getName());
				
		if(this.getSuperClass() != null) {
			s.append(" extends " + this.getSuperClass());
		}
		
		if(this.getImplements().size() > 0) {
			s.append(" implements");
			for(String imp : this.getImplements()) {
				s.append(" " + imp);
			}
		}

		System.out.println(s.toString());
		
		/*
		for(Entity e : entitiesInsideClass) {
			e.printInfo();
		}
		*/
		
		for(Entity e : arrayList) {
			e.printInfo();
		}
	}

	public void addImplementsInterface(String s) {
		this.implementsList.add(s);
	}

	public List<String> getImplements() {
		return this.implementsList;
	}
	
	public void setImportList(List<Entity> list ) {
		this.importObjectList = list;
	}
	
	public List<Entity> getImportList() {
		return this.importObjectList;
	}
	
	public void setPackageObject(Entity po) {
		packageObject = po;
	}
	
	public Entity getPackageObject() {
		return this.packageObject;
	}
	
	public void addChild(Entity e) {
		entitiesInsideClass.add(e);
	}

	
	
}