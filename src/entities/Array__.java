package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class ArrayObject {
	String name;
	String fullyQualifiedName;
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	ArrayObject(String n, String fqn, String cn, String mn, Type t, int l, int c) {
		name = n;
		fullyQualifiedName = fqn;
		type = t;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + ": " + type + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class Array__ {
	List<ArrayObject> arrayObjectList;

	public Array__() {
		this.arrayObjectList = new ArrayList<>();
	}

	public void addArray(String name, String fullyQualifiedName, String className, String methodName, Type type, int lineNumber, int columnNumber) {
		arrayObjectList.add(new ArrayObject(name, fullyQualifiedName, className, methodName, type, lineNumber, columnNumber));
	}

	public void printAllArrays() {
		System.out.println("--- Arrays ---");

		if(arrayObjectList.size() > 0) {
			for(ArrayObject obj : arrayObjectList) {
				obj.printEntity();
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
	
	public List<ArrayObject> getArrayObjectList() {
		return this.arrayObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ArrayObject obj : arrayObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ArrayObject obj : arrayObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(ArrayObject obj : arrayObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
