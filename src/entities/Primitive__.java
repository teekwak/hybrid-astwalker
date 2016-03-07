package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class PrimitiveObject {

	String name;
	String fullyQualifiedName;
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	PrimitiveObject(String n, String fqn, String cn, String mn, Type t, int l, int c) {
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

public class Primitive__ {
	// name, type, lineNumber, columnNumber
	List<PrimitiveObject> primitiveObjectList;

	public Primitive__() {
		this.primitiveObjectList = new ArrayList<>();
	}

	public void addPrimitive(String name, String fullyQualifiedName, String className, String methodName, Type type, int lineNumber, int columnNumber) {
		primitiveObjectList.add(new PrimitiveObject(name, fullyQualifiedName, className, methodName, type, lineNumber, columnNumber));
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

	public List<PrimitiveObject> getPrimitiveObjectList() {
		return this.primitiveObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(PrimitiveObject obj : primitiveObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(PrimitiveObject obj : primitiveObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(PrimitiveObject obj : primitiveObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
