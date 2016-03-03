package entities;

import java.util.ArrayList;
import java.util.List;

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

	MethodDeclarationObject(String n, String cn, Type t, List<Object> p, int l, int c) {
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
	}

	void printEntity() {
		System.out.println(name + " (" + className + ") " + returnType + " => " + lineNumber + " | " + columnNumber);
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
	
	/*
	 * start testing
	 */

	public List<MethodDeclarationObject> getMethodDeclarationObjectList() {
		return this.methodDeclarationObjectList;
	}	
	
	/*
	 * end testing
	 */
}
