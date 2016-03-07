package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class GenericsObject {
	String name;
	String fullyQualifiedName;
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	GenericsObject(String n, String fqn, String cn, String mn, Type t, int l, int c) {
		name = n;
		fullyQualifiedName = fqn;
		className = cn;
		methodName = mn;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + ": " + type + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class Generics__ {
	// name, type, lineNumber, columnNumber
	List<GenericsObject> genericsObjectList;

	public Generics__() {
		this.genericsObjectList = new ArrayList<>();
	}

	public void addGenerics(String name, String fullyQualifiedName, String className, String methodName, Type type, int lineNumber, int columnNumber) {
		genericsObjectList.add(new GenericsObject(name, fullyQualifiedName, className, methodName, type, lineNumber, columnNumber));
	}
	
	public void printAllGenerics() {
		System.out.println("--- Generics ---");

		if(genericsObjectList.size() > 0) {
			for(GenericsObject p : genericsObjectList) {
				p.printEntity();
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

	public List<GenericsObject> getGenericsObjectList() {
		return this.genericsObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(GenericsObject obj : genericsObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(GenericsObject obj : genericsObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(GenericsObject obj : genericsObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
