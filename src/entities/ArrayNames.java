package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class ArrayObject {
	String name;
	String type;
	int lineNumber;
	int columnNumber;
	
	ArrayObject(String n, String t, int l, int c) {
		name = n;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printName() {
		System.out.println(name + " " + type);
	}
}

public class ArrayNames {
	// name, type, lineNumber, columnNumber
	List<ArrayObject> arrayObjectList;

	public ArrayNames() {
		this.arrayObjectList = new ArrayList<>();
	}
	
	public void addArray(String name, String type, int lineNumber, int columnNumber) {
		arrayObjectList.add(new ArrayObject(name, type, lineNumber, columnNumber));
	}
	
	public void printAllArrays() {
		System.out.println("--- Arrays ---");

		if(arrayObjectList.size() > 0) {
			for(ArrayObject a : arrayObjectList) {
				a.printName();
			}
			System.out.println();
		}	
		else {
			System.out.println("None\n");
		}
	}
}
