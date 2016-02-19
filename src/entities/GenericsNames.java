package entities;

import java.util.ArrayList;
import java.util.List;

class GenericsObject {
	String name;
	String type;
	int lineNumber;
	int columnNumber;
	
	GenericsObject(String n, String t, int l, int c) {
		name = n;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + " " + type);
	}
}

public class GenericsNames {
	// name, type, lineNumber, columnNumber
	List<GenericsObject> genericsObjectList;

	public GenericsNames() {
		this.genericsObjectList = new ArrayList<>();
	}
	
	public void addGenerics(String name, String type, int lineNumber, int columnNumber) {
		genericsObjectList.add(new GenericsObject(name, type, lineNumber, columnNumber));
	}
	
	public void printAllGenerics() {
		System.out.println("--- Generics ---");

		if(genericsObjectList.size() > 0) {
			for(GenericsObject p : genericsObjectList) {
				p.printEntity();
			}	
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
