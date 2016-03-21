package entities;

import java.util.ArrayList;
import java.util.List;

public class ClassObject extends SuperEntityClass {

	String name;
	int lineNumber;
	int columnNumber;
	String superClass;
	List<String> implementsList;
	
	List<SuperEntityClass> classList;
	List<SuperEntityClass> importObjectList;
	List<MethodDeclarationObject> methodDeclarationList;
	SuperEntityClass packageObject;
	
	public ClassObject() {
		this.implementsList = new ArrayList<>();
		
		this.classList = new ArrayList<>();
		this.importObjectList = new ArrayList<>();
		this.methodDeclarationList = new ArrayList<>();
		this.packageObject = null;
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

		s.append(" (" + this.lineNumber + " | " + this.columnNumber + ")");
		
		System.out.println(s.toString());
		
		for(Entity e : this.classList) {
			e.printInfo();
		}
		
		for(MethodDeclarationObject e : methodDeclarationList) {
			e.printInfo();
		}		
		
		for(Entity e : arrayList) {
			e.printInfo();
		}
		
		for(Entity e : genericsList) {
			e.printInfo();
		}
		
		for(Entity e : primitiveList) {
			e.printInfo();
		}
		
		for(Entity e : simpleList) {
			e.printInfo();
		}
		
		for(Entity e : wildcardList) {
			e.printInfo();
		}
	}

	public void addImplementsInterface(String s) {
		this.implementsList.add(s);
	}

	public List<String> getImplements() {
		return this.implementsList;
	}
	
	public void setImportList(List<SuperEntityClass> list ) {
		this.importObjectList = list;
	}
	
	public List<SuperEntityClass> getImportList() {
		return this.importObjectList;
	}
	
	public void setPackageObject(SuperEntityClass po) {
		packageObject = po;
	}
	
	public Entity getPackageObject() {
		return this.packageObject;
	}

	public void addMethodDeclaration(MethodDeclarationObject mdo) {
		this.methodDeclarationList.add(mdo);
	}	
	
}