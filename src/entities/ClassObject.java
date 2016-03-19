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
	PackageObject packageObject;
	List<ImportObject> importObjectList;
	
	public ClassObject() {
		this.implementsList = new ArrayList<>();
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
		
		for(Entity e : entitiesInsideClass) {
			e.printInfo();
		}
	}

	public void addImplementsInterface(String s) {
		this.implementsList.add(s);
	}

	public List<String> getImplements() {
		return this.implementsList;
	}
	
	public void setImportList(List<ImportObject> list ) {
		this.importObjectList = list;
	}
	
	public List<ImportObject> getImportList() {
		return this.importObjectList;
	}
	
	public void setPackageObject(PackageObject po) {
		packageObject = po;
	}
	
	public PackageObject getPackageObject() {
		return this.packageObject;
	}
}
