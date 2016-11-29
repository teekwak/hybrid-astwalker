package entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaClass extends SuperEntityClass {

	private String fileName;
	private String superClass;
	private String sourceCode;
	private String containingClass;
	private boolean isAbstract;
	private boolean isGenericType;
	private boolean isInnerClass;
	private boolean isAnonymous;
	private boolean hasComments;
	private List<String> implementsList;
	private List<String> genericParametersList;

	private Set<String> methodDeclarationNames;
	private Set<String> methodInvocationNames;

	private List<SuperEntityClass> arrayList;
	private List<SuperEntityClass> classList;
	private List<SuperEntityClass> genericsList;
	private List<SuperEntityClass> globalList;
	private List<SuperEntityClass> importList;
	private List<SuperEntityClass> methodDeclarationList;
	private List<SuperEntityClass> methodInvocationList;
	private List<SuperEntityClass> primitiveList;
	private List<SuperEntityClass> simpleList;
	private List<SuperEntityClass> wildcardList;

	private SuperEntityClass packageObject;

	public JavaClass() {
		this.implementsList = new ArrayList<>();
		this.genericParametersList = new ArrayList<>();
		
		this.methodDeclarationNames = new HashSet<>();
		this.methodInvocationNames = new HashSet<>();
		
		this.arrayList = new ArrayList<>();
		this.classList = new ArrayList<>();
		this.genericsList = new ArrayList<>();
		this.globalList = new ArrayList<>();
		this.importList = new ArrayList<>();
		this.methodDeclarationList = new ArrayList<>();
		this.methodInvocationList = new ArrayList<>();
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

	public List<SuperEntityClass> getMethodInvocationList() {
		return this.methodInvocationList;
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
	
	public void setIsInnerClass(boolean ic) {
		this.isInnerClass = ic;
	}
	
	public boolean getIsInnerClass() {
		return this.isInnerClass;
	}
	
	public void setIsAnonymous(boolean ia) {
		this.isAnonymous = ia;
	}
	
	public boolean getIsAnonymous() {
		return this.isAnonymous;
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
	
	public void setContainingClass(String pc) {
		this.containingClass = pc;
	}
	
	public String getContainingClass() {
		return this.containingClass;
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
		else if(ET == EntityType.METHOD_INVOCATION) {
			this.methodInvocationList.add(entity);
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
	
	public void setMethodDeclarationNames() {
		for(SuperEntityClass cl : this.classList) {
			this.methodDeclarationNames.addAll(((JavaClass) cl).getMethodDeclarationNames());			
		}
		
		for(SuperEntityClass mdo : this.methodDeclarationList) {
			this.methodDeclarationNames.addAll(((MethodDeclarationObject) mdo).getMethodDeclarationNames());
		}
	}
	
	public Set<String> getMethodDeclarationNames() {
		return this.methodDeclarationNames;
	}
	
	public void setMethodInvocationNames() {
		for(SuperEntityClass cl : this.classList) {
			this.methodInvocationNames.addAll(((JavaClass) cl).getMethodInvocationNames());			
		}
		
		for(SuperEntityClass mdo : this.methodDeclarationList) {
			this.methodInvocationNames.addAll(((MethodDeclarationObject) mdo).getMethodInvocationNames());
		}
	}
	
	public Set<String> getMethodInvocationNames() {
		return this.methodInvocationNames;
	}
	
	public void setComplexities() {
		int cycloCount = 0;
		int methodInvCount = this.methodInvocationList.size();
		
		for(SuperEntityClass cl : this.classList) {
			cycloCount += ((JavaClass) cl).getCyclomaticComplexity();
			methodInvCount += ((JavaClass) cl).getMethodInvocationList().size();
		}
		
		for(SuperEntityClass mdo : this.methodDeclarationList) {
			cycloCount += ((MethodDeclarationObject) mdo).getCyclomaticComplexity();
			methodInvCount += ((MethodDeclarationObject) mdo).getTotalMethodInvocationCount();
		}
		
		this.cyclomaticComplexity = cycloCount;
		this.totalMethodInvocationCount = methodInvCount;
	}
	
	public int getCyclomaticComplexity() {
		return this.cyclomaticComplexity;
	}
	
	public int getTotalMethodInvocationCount() {
		return this.totalMethodInvocationCount;
	}
	
	public double getClassComplexity() {
		return (double)this.cyclomaticComplexity * (1 / ((double)this.totalMethodInvocationCount)) * (1 / (double)this.getSourceCode().length());
	}
}