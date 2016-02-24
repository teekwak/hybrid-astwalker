package entities;

import java.util.ArrayList;
import java.util.List;

class InterfaceObject {
	String name;
	int lineNumber;
	int columnNumber;

	InterfaceObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber + " | " + columnNumber);
	}
}

class ImplementsObject {
	String name;
	int lineNumber;

	ImplementsObject(String n, int l) {
		name = n;
		lineNumber = l;
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber);
	}
}

public class Interface__ {
	List<InterfaceObject> interfaceObjectList;
	List<ImplementsObject> implementsObjectList;

	public Interface__() {
		interfaceObjectList = new ArrayList<>();
		implementsObjectList = new ArrayList<>();
	}

	public void addImplements(String name, int lineNumber) {
		implementsObjectList.add(new ImplementsObject(name, lineNumber));
	}

	public void addInterface(String name, int lineNumber, int columnNumber) {
		interfaceObjectList.add(new InterfaceObject(name, lineNumber, columnNumber));
	}

	public void printAllInterfaces() {
		if(interfaceObjectList.size() > 0) {
			System.out.println("--- Interfaces ---");
			for(InterfaceObject obj : interfaceObjectList) {
				obj.printEntity();
			}

			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}

	public void printAllImplements() {
		System.out.println("--- Implements ---");

		if(implementsObjectList.size() > 0) {
			for(ImplementsObject obj : this.implementsObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
