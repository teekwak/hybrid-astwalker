package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class PrimitiveObject {
	
	String name;
	Type type;
	int lineNumber;
	int columnNumber;
	
	PrimitiveObject(String n, Type t, int l, int c) {
		name = n;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(name + " " + type);
	}
}

public class PrimitiveNames {
	// name, type, lineNumber, columnNumber
	List<PrimitiveObject> primitiveObjectList;

	public PrimitiveNames() {
		this.primitiveObjectList = new ArrayList<>();
	}
	
	public void addPrimitive(String name, Type type, int lineNumber, int columnNumber) {
		primitiveObjectList.add(new PrimitiveObject(name, type, lineNumber, columnNumber));
	}
	
	public void printAllPrimitives() {
		System.out.println("--- Primitives ---");

		if(primitiveObjectList.size() > 0) {
			for(PrimitiveObject p : primitiveObjectList) {
				p.printEntity();
			}	
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
