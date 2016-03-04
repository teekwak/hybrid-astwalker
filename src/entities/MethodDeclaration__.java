package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.Type;

class MethodDeclarationObject {
	String name;
	String className;
	Type returnType;
	int lineNumber;
	int columnNumber;
	List<String> parameterNames;
	List<String> parameterTypes;
	List<String> statements;
	
	Map<String, Integer> entitiesInsideMethod;

	MethodDeclarationObject(String n, String cn, Type t, List<Object> p, int l, int c) {
		// if t == null, then probably constructor
		name = n;
		className = cn;
		returnType = t;
		lineNumber = l;
		columnNumber = c;

		parameterNames = new ArrayList<>();
		parameterTypes = new ArrayList<>();

		for(Object line : p) {
			String[] parts = line.toString().split(" ");
			parameterNames.add(parts[1]);
			parameterTypes.add(parts[0]);
		}
		
		entitiesInsideMethod = new HashMap<>();
	}

	void printEntity() {
		System.out.println(name + " (" + className + ") " + returnType + " => " + lineNumber + " | " + columnNumber);
		for(Map.Entry<String, Integer> entry : entitiesInsideMethod.entrySet()) {
			System.out.println("\t" + entry.getKey() + ": " + entry.getValue());
		}
	}
	
	boolean has(String name, List<Object> pT) {
		if(!this.name.equals(name)) {
			return false;
		}
		
		for(int i = 0; i < pT.size(); i++) {
			String[] parts = pT.get(i).toString().split(" ");

			if(!this.parameterTypes.get(i).equals(parts[0])) {
				return false;
			}
		}
		
		return true;
	}
}

public class MethodDeclaration__ {
	List<MethodDeclarationObject> methodDeclarationObjectList;

	public MethodDeclaration__() {
		methodDeclarationObjectList = new ArrayList<>();
	}

	// check that the class names and parameters are all different
	public void addMethodDeclaration(String name, String className, Type returnType, List<Object> parameters, int lineNumber, int columnNumber) {
		methodDeclarationObjectList.add(new MethodDeclarationObject(name, className, returnType, parameters, lineNumber, columnNumber));
	}
	
	public void printAllMethodDeclarations() {
		if(methodDeclarationObjectList.size() > 0) {
			System.out.println("--- Methods Declarations ---");
			for(MethodDeclarationObject m : methodDeclarationObjectList) {
				m.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
	public void addOneToCounter(String name, List<Object> parameterTypes, String entity) {		
		for(MethodDeclarationObject m : methodDeclarationObjectList) {
			if(m.has(name, parameterTypes)) {
				if(m.entitiesInsideMethod.get(entity) != null) {
					m.entitiesInsideMethod.put(entity, m.entitiesInsideMethod.get(entity) + 1);
				}
				else {
					m.entitiesInsideMethod.put(entity, 1);	
				}
				return;
			}
		}
	}
	
	/*
	 * start testing
	 */

	public List<MethodDeclarationObject> getMethodDeclarationObjectList() {
		return this.methodDeclarationObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(MethodDeclarationObject obj : methodDeclarationObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(MethodDeclarationObject obj : methodDeclarationObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(MethodDeclarationObject obj : methodDeclarationObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
