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
	List<SuperClassObject> superClassObjectList;

	public Class__() {
		classObjectList = new ArrayList<>();
		superClassObjectList = new ArrayList<>();
	}

	public void addClass(String name, int lineNumber, int columnNumber) {
		classObjectList.add(new ClassObject(name, lineNumber, columnNumber));
	}

	public void addExtends(String name, int lineNumber) {
		superClassObjectList.add(new SuperClassObject(name, lineNumber));
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

		if(superClassObjectList.size() > 0) {
			for(SuperClassObject obj : superClassObjectList) {
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
	
	public List<ClassObject> getClassObjectList() {
		return this.classObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ClassObject obj : classObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ClassObject obj : classObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(ClassObject obj : classObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	public List<Integer> getSuperClassLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(SuperClassObject obj : superClassObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}
	
	public List<SuperClassObject> getSuperClassObjectList() {
		return this.superClassObjectList;
	}
	
	public List<String> getSuperClassNames() {
		List<String> list = new ArrayList<>();
		for(SuperClassObject obj : superClassObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
