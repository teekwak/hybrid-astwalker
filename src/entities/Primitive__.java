package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class PrimitiveObject {

	String name;
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	PrimitiveObject(String n, String cn, String mn, Type t, int l, int c) {
		name = n;
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

public class Primitive__ {
	// name, type, lineNumber, columnNumber
	List<PrimitiveObject> primitiveObjectList;

	public Primitive__() {
		this.primitiveObjectList = new ArrayList<>();
	}

	public void addPrimitive(String name, String className, String methodName, Type type, int lineNumber, int columnNumber) {
		primitiveObjectList.add(new PrimitiveObject(name, className, methodName, type, lineNumber, columnNumber));
	}

	public List<PrimitiveObject> getPrimitiveObjectList() {
		return this.primitiveObjectList;
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
	
	/*
	 * start testing
	 */
	
	/*
	 * end testing
	 */
}
