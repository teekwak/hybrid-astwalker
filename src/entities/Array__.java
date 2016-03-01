package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class ArrayObject {
	String name;
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	ArrayObject(String n, String cn, String mn, Type t, int l, int c) {
		name = n;
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

	public void addArray(String name, String className, String methodName, Type type, int lineNumber, int columnNumber) {
		arrayObjectList.add(new ArrayObject(name, className, methodName, type, lineNumber, columnNumber));
	}

	public List<ArrayObject> getArrayObjectList() {
		return this.arrayObjectList;
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
}
