package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class ArrayObject {
	String name;
	Type type;
	int lineNumber;
	int columnNumber;
	
	ArrayObject(String n, Type t, int l, int c) {
		name = n;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(name + ": " + type + " => " + lineNumber + " | " + columnNumber);
	}
}

public class ArrayNames {
	List<ArrayObject> arrayObjectList;

	public ArrayNames() {
		this.arrayObjectList = new ArrayList<>();
	}
	
	public void addArray(String name, Type type, int lineNumber, int columnNumber) {
		arrayObjectList.add(new ArrayObject(name, type, lineNumber, columnNumber));
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
