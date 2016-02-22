package entities;

import java.util.ArrayList;
import java.util.List;

class SuperClassObject {
	String name;
	int lineNumber;
	
	SuperClassObject(String n, int l) {
		name = n;
		lineNumber = l;
	}
	
	void printEntity() {
		System.out.println(name + " => " + lineNumber);
	}
}

class ClassObject {
	String name;
	int lineNumber;
	int columnNumber;
	
	ClassObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(name + " => " + lineNumber + " | " + columnNumber);
	}
}

public class Class__ {
	
	List<ClassObject> classObjectList;
	List<SuperClassObject> superClassList;

	public Class__() {
		classObjectList = new ArrayList<>();
		superClassList = new ArrayList<>();
	}
	
	public void addClass(String name, int lineNumber, int columnNumber) {
		classObjectList.add(new ClassObject(name, lineNumber, columnNumber));
	}
	
	public void addExtends(String name, int lineNumber) {
		superClassList.add(new SuperClassObject(name, lineNumber));
	}
	
	public void printAllClasses() {
		System.out.println("--- Classes ---");

		if(classObjectList.size() > 0) {
			for(ClassObject obj : classObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
	
	public void printAllExtends() {
		System.out.println("--- Extends ---");

		if(superClassList.size() > 0) {
			for(SuperClassObject obj : superClassList) {
				obj.printEntity();
			}
			System.out.println();
		} 
		else {
			System.out.println("None\n");
		}
	}
}
