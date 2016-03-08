package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SuperClassObject {
	String name;
	int lineNumber;

	SuperClassObject(String n, int l) {
		name = n;
		lineNumber = l;
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber);
	}
}

class ClassObject {
	String name;
	List<String> methodDeclarations;
	int lineNumber;
	int columnNumber;
	Map<String, Integer> entitiesInsideClass;
	Map<String, Integer> entitiesInsideMethod;

	ClassObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
		methodDeclarations = new ArrayList<>();
		entitiesInsideClass = new HashMap<>();
		entitiesInsideMethod = new HashMap<>();
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tMember variables in class");
		if(entitiesInsideClass.size() > 0) {
			for(Map.Entry<String, Integer> entry : entitiesInsideClass.entrySet()) {
				System.out.println("\t\t" + entry.getKey() + ": " + entry.getValue());
			}
		}
		else {
			System.out.println("\tNone");
		}
		
		System.out.println("\tMethod declarations in class");
		if(methodDeclarations.size() > 0) {
			for(String s : methodDeclarations) {
				System.out.println("\t\t" + s);
			}			
		}
		else {
			System.out.println("\t\tNone");
		}
		System.out.println();
	}
	
	int getComplexity() {
		int complexity = 0;
		
		try {
			complexity += entitiesInsideClass.get("MethodDeclaration");
		} catch (NullPointerException e) {
			
		}
		
		for(Map.Entry<String, Integer> entry : entitiesInsideMethod.entrySet()) {			
			switch(entry.getKey()) {
				case "CatchClause":							
					complexity += entry.getValue();
					break;	
				case "ConditionalExpression":				
					complexity += entry.getValue();
					break;
				case "DoStatement":							
					complexity += entry.getValue();
					break;	
				case "EnhancedForStatement":				
					complexity += entry.getValue();
					break;
				case "ForStatement":						
					complexity += entry.getValue();
					break;
				case "IfStatement":							
					complexity += entry.getValue();
					break;
				case "InfixAndOr":							
					complexity += entry.getValue();
					break;
				case "SwitchCase":							
					complexity += entry.getValue();
					break;
				case "WhileStatement":						
					complexity += entry.getValue();
					break;
			}
		}
		
		return complexity;
	}
	
	void printComplexity() {
		System.out.println(this.name + " | " + this.getComplexity());
	}
}

public class Class__ {

	List<ClassObject> classObjectList;
	List<SuperClassObject> superClassObjectList;

	public Class__() {
		classObjectList = new ArrayList<>();
		superClassObjectList = new ArrayList<>();
	}

	public void addClass(String name, int lineNumber, int columnNumber) {
		classObjectList.add(new ClassObject(name, lineNumber, columnNumber));
	}
	
	public void addExtends(String name, int lineNumber) {
		superClassObjectList.add(new SuperClassObject(name, lineNumber));
	}

	public void addMethodDeclarationToClass(String className, String methodDeclaration) {
		for(ClassObject obj : classObjectList) {
			if(obj.name.equals(className)) {
				obj.methodDeclarations.add(methodDeclaration);
				break;
			}
		}
	}
	
	public void printAllClasses() {
		System.out.println("--- Classes ---");

		if(classObjectList.size() > 0) {
			for(ClassObject obj : classObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
	public void printAllClassComplexities() {
		System.out.println("--- Class Complexities ---");

		if(classObjectList.size() > 0) {
			for(ClassObject obj : classObjectList) {
				obj.printComplexity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
	public void printAllExtends() {
		System.out.println("--- Extends ---");

		if(superClassObjectList.size() > 0) {
			for(SuperClassObject obj : superClassObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
	// adds variables outside of methods
	public void addOneToCounter(String className, String entity) {
		for(ClassObject c : classObjectList) {
			if(c.name.equals(className)) {
				if(c.entitiesInsideClass.get(entity) != null) {
					c.entitiesInsideClass.put(entity, c.entitiesInsideClass.get(entity) + 1);
				}
				else {
					c.entitiesInsideClass.put(entity, 1);	
				}
			}
		}
	}
	
	// adds variables inside of methods
	public void addOneToMethodCounter(String className, String entity) {
		for(ClassObject c : classObjectList) {
			if(c.name.equals(className)) {
				if(c.entitiesInsideMethod.get(entity) != null) {
					c.entitiesInsideMethod.put(entity, c.entitiesInsideMethod.get(entity) + 1);
				}
				else {
					c.entitiesInsideMethod.put(entity, 1);	
				}
			}
		}
	}
	
	/*
	 * start testing
	 */
	
	public List<ClassObject> getClassObjectList() {
		return this.classObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ClassObject obj : classObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ClassObject obj : classObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(ClassObject obj : classObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	public List<Integer> getSuperClassLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(SuperClassObject obj : superClassObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}
	
	public List<SuperClassObject> getSuperClassObjectList() {
		return this.superClassObjectList;
	}
	
	public List<String> getSuperClassNames() {
		List<String> list = new ArrayList<>();
		for(SuperClassObject obj : superClassObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
