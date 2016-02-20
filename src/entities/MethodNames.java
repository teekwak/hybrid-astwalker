package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class MethodObject {
	String name;
	Object returnType;
	List<String> parameterNames;
	List<String> parameterTypes;
	List<String> statements;
	Map<Integer, Integer> invocationPositions;
	
	MethodObject(String n, Object r, List p, int l, int c) {
		name = n;
		returnType = r;
		
		parameterNames = new ArrayList<>();
		parameterTypes = new ArrayList<>();
		
		for(Object line : p) {
			String[] parts = line.toString().split(" ");
			parameterNames.add(parts[1]);
			parameterTypes.add(parts[0]);			
		}
		
		invocationPositions = new TreeMap<>();
		invocationPositions.put(l, c);
	}
	
	void printMethod() {
		System.out.println(name + ": " + returnType);
		for(int i = 0; i < parameterNames.size(); i++) {
			System.out.println("\t" + parameterNames.get(i) + ": " + parameterTypes.get(i));
			
		}
		for(Map.Entry<Integer, Integer> entry : invocationPositions.entrySet()) {
			System.out.println("\t" + entry.getKey() + " | " + entry.getValue());
		}
	}

	// this function is funky
	boolean equals(MethodObject original) {
		if(!this.name.equals(original.name)) {
			return false;
		}
		
		for(int i = 0; i < original.parameterNames.size(); i++) {
			if(!this.parameterNames.get(i).equals(original.parameterNames.get(i))) {
				return false;
			}
			else if(!this.parameterTypes.get(i).equals(original.parameterTypes.get(i))) {
				return false;
			}
		}
		
		return true;
	}
}

public class MethodNames {
	List<MethodObject> methodObjectList;
	
	public MethodNames() {
		methodObjectList = new ArrayList<>();
	}
	
	// what to do if there are same named methods in different classes? for example, void print()?
	// bodies are different, but .getBody() is deprecated
	// idk why, but it works now. go with it
	public void addMethod(String name, Object returnType, List parameters, int lineNumber, int columnNumber) {
		MethodObject methodObject = new MethodObject(name, returnType, parameters, lineNumber, columnNumber);
		
		// accounts for duplicates versus overloading function
		for(MethodObject m : methodObjectList) {
			if(methodObject.equals(m)) {
				m.invocationPositions.put(lineNumber, columnNumber);
				return;
			}
		}
		
		methodObjectList.add(methodObject);			
	}
		
	public void addMethodInvocation(String methodInvokedName, int lineNumber, int columnNumber) {
		for(MethodObject m : methodObjectList) {
			if(methodInvokedName.equals(m.name)) {
				m.invocationPositions.put(lineNumber, columnNumber);
				return;
			}
		}
		
		addMethod(methodInvokedName, "not declared in file", new ArrayList<>() , lineNumber, columnNumber);
	}
	
	public void printAllMethods() {
		if(methodObjectList.size() > 0) {
			System.out.println("--- Methods ---");
			for(MethodObject m : methodObjectList) {
				m.printMethod();
			}			
		}
	}
}
