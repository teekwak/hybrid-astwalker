package entities;

import java.util.ArrayList;
import java.util.List;

class PrimitiveObject {
	String name;
	String type;
	int lineNumber;
	int columnNumber;
	
	PrimitiveObject(String n, String t, int l, int c) {
		name = n;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printName() {
		System.out.println(name);
	}
}

public class PrimitiveNames {
	// name, type, lineNumber, columnNumber
	List<PrimitiveObject> primitiveObjectList;

	public PrimitiveNames() {
		this.primitiveObjectList = new ArrayList<>();
	}
	
	public void add(String name, String type, int lineNumber, int columnNumber) {
		primitiveObjectList.add(new PrimitiveObject(name, type, lineNumber, columnNumber));
	}
	
	public void printIntegers() {
		System.out.println("---Primitives---");
		for(PrimitiveObject p : primitiveObjectList) {
			p.printName();
		}
	}
	
}
