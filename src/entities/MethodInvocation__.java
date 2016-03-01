package entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;

class MethodInvocationObject {
	String name;
	String parentClass;
	List<Object> parameters;
	Map<String, Map<Integer, Integer>> invocationClassAndPositions;

	MethodInvocationObject(String n, String p, String classWhereMethodInvoked, List<Object> param, int l, int c) {
		name = n;
		parentClass = p;
		Map<Integer, Integer> position = new TreeMap<>();
		position.put(l, c);
		invocationClassAndPositions = new HashMap<>();
		invocationClassAndPositions.put(classWhereMethodInvoked, position);

		parameters = new ArrayList<>();
		for(Object obj : param) {
			parameters.add(obj.getClass().getSimpleName());
		}
	}

	// same name, parent class, and parameters
	// different parameters => different MethodInvocationObjects
	boolean equals(MethodInvocationObject obj) {
		if(this.name.equals(obj.name) && this.parentClass.equals(obj.parentClass) && this.parameters.size() == obj.parameters.size()) {
			for(Object p : this.parameters) {
				if(!obj.parameters.contains(p)) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	void printEntity() {
		System.out.println(name + " (" + parentClass + ")");
		System.out.print("\t");
		for(Object obj : parameters) {
			System.out.print(obj + " ");
		}
		if(parameters.size() == 0) {
			System.out.print("[No arguments]");
		}
		System.out.println();
		for(Map.Entry<String, Map<Integer, Integer>> entry : invocationClassAndPositions.entrySet()) {
			System.out.println("\tClass: " + entry.getKey());
			for(Map.Entry<Integer, Integer> position : entry.getValue().entrySet()) {
				System.out.println("\t\t" + position.getKey() + " | " + position.getValue());
			}
		}
	}
}

public class MethodInvocation__ {
	List<MethodInvocationObject> methodInvocationObjectList;

	public MethodInvocation__() {
		methodInvocationObjectList = new ArrayList<>();
	}

	public void addMethodInvocation(String name, String parentClass, String classWhereMethodInvoked, List<Object> parameters, int lineNumber, int columnNumber) {
		MethodInvocationObject temp = new MethodInvocationObject(name, parentClass, classWhereMethodInvoked, parameters, lineNumber, columnNumber);

		for(MethodInvocationObject obj : methodInvocationObjectList) {
			if(temp.equals(obj)) {
				try {
					obj.invocationClassAndPositions.get(classWhereMethodInvoked).put(lineNumber, columnNumber);
				}
				catch (NullPointerException e) {
					Map<Integer, Integer> tempMap = new HashMap<>();
					obj.invocationClassAndPositions.put(classWhereMethodInvoked, tempMap);
				}
				return;
			}
		}

		methodInvocationObjectList.add(temp);
	}

	public List<MethodInvocationObject> getMethodInvocationObjectList() {
		return this.methodInvocationObjectList;
	}
	
	public void printAllMethodInvocations() {
		System.out.println("--- Method Invocations ---");

		if(methodInvocationObjectList.size() > 0) {
			for(MethodInvocationObject obj : methodInvocationObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}

	}
}
