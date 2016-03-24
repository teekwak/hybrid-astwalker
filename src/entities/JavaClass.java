package entities;

import java.util.ArrayList;
import java.util.List;

public class JavaClass extends SuperEntityClass {

	String fileName;
	String superClass;
	String sourceCode;
	boolean isAbstract;
	boolean isGenericType;
	boolean isInnerClass;
	boolean hasComments;
	List<String> implementsList;
	List<String> genericParametersList;
	
	List<SuperEntityClass> arrayList;
	List<SuperEntityClass> classList;
	List<SuperEntityClass> genericsList;
	List<SuperEntityClass> globalList;
	List<SuperEntityClass> importList;
	List<SuperEntityClass> methodDeclarationList;
	List<SuperEntityClass> primitiveList;
	List<SuperEntityClass> simpleList;
	List<SuperEntityClass> wildcardList;
	
	SuperEntityClass packageObject;

	public JavaClass() {
		this.implementsList = new ArrayList<>();
		this.genericParametersList = new ArrayList<>();
		
		this.arrayList = new ArrayList<>();
		this.classList = new ArrayList<>();
		this.genericsList = new ArrayList<>();
		this.globalList = new ArrayList<>();
		this.importList = new ArrayList<>();
		this.methodDeclarationList = new ArrayList<>();
		this.primitiveList = new ArrayList<>();
		this.simpleList = new ArrayList<>();
		this.wildcardList = new ArrayList<>();
		
		this.packageObject = null;
	}
	
	public void setSuperClass(String s) {
		this.superClass = s;
	}
	
	public String getSuperClass() {
		return this.superClass;
	}
	
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

		s.append(" [" + this.lineNumber + " | " + this.columnNumber + "]");
		
		System.out.println(s.toString());
		
		for(Entity e : this.classList) {
			e.printInfo();
		}
		
		for(Entity e : methodDeclarationList) {
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

	public void setGenericParametersList(List<String> list) {
		this.genericParametersList = list;
	}
	
	public List<String> getGenericParametersList() {
		return this.genericParametersList;
	}
	
	public void addImplementsInterface(String s) {
		this.implementsList.add(s);
	}

	public List<String> getImplements() {
		return this.implementsList;
	}
	
	public void setImportList(List<SuperEntityClass> list ) {
		this.importList = list;
	}
	
	public List<SuperEntityClass> getImportList() {
		return this.importList;
	}
	
	public void setPackage(SuperEntityClass po) {
		packageObject = po;
	}
	
	public Entity getPackage() {
		return this.packageObject;
	}

	public void setHasComments(boolean c) {
		this.hasComments = c;
	}
	
	public boolean getHasComments() {
		return this.hasComments;
	}
	
	public void setFileName(String s) {
		this.fileName = s;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public void setInnerClass(boolean ic) {
		this.isInnerClass = ic;
	}
	
	public boolean getInnerClass() {
		return this.isInnerClass;
	}
	
	public void setSourceCode(String s) {
		this.sourceCode = s;
	}
	
	public String getSourceCode() {
		return this.sourceCode;
	}
	
	public void setIsGenericType(boolean g) {
		this.isGenericType = g;
	}
	
	public boolean getIsGenericType() {
		return this.isGenericType;
	}
	
	public void setIsAbstract(boolean a) {
		this.isAbstract = a;
	}
	
	public boolean getIsAbstract() {
		return this.isAbstract;
	}
	
	public List<SuperEntityClass> getArrayList() {
		return this.arrayList;
	}
	
	public List<SuperEntityClass> getClassList() {
		return this.classList;
	}
	
	public List<SuperEntityClass> getGenericsList() {
		return this.genericsList;
	}
	
	public List<SuperEntityClass> getGlobalList() {
		return this.globalList;
	}
	
	public List<SuperEntityClass> getMethodDeclarationList() {
		return this.methodDeclarationList;
	}
	
	public List<SuperEntityClass> getPrimitiveList() {
		return this.primitiveList;
	}
	
	public List<SuperEntityClass> getSimpleList() {
		return this.simpleList;
	}
	
	public List<SuperEntityClass> getWildcardList() {
		return this.wildcardList;
	}
	
	public void addEntity(SuperEntityClass entity, EntityType ET) {
		this.entityList.add(entity);
		
		// global variables only
		if(ET == EntityType.GLOBAL) {
			this.globalList.add(entity);
		}
		
		if(ET == EntityType.ARRAY) {
			this.arrayList.add(entity);
		}
		else if(ET == EntityType.CLASS) {
			this.classList.add(entity);
		}
		else if(ET == EntityType.GENERICS) {
			this.genericsList.add(entity);
		}
		else if(ET == EntityType.METHOD_DECLARATION) {
			this.methodDeclarationList.add(entity);
		}
		else if(ET == EntityType.PRIMITIVE) {
			this.primitiveList.add(entity);
		}
		else if(ET == EntityType.SIMPLE) {
			this.simpleList.add(entity);
		}
		else if(ET == EntityType.WILDCARD) {
			this.wildcardList.add(entity);
		}
	}
	
	public void setCyclomaticComplexity() {
		int count = 0;
		for(SuperEntityClass md : this.methodDeclarationList) {
			count += ((MethodDeclarationObject) md).getCyclomaticComplexity();
		}
		this.cyclomaticComplexity = count;
	}
	
	public int getCyclomaticComplexity() {
		return this.cyclomaticComplexity;
	}
	
}