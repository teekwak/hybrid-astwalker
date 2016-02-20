package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class GenericsObject {
	
	String name;
	Type type;
	int lineNumber;
	int columnNumber;
	
	GenericsObject(String n, Type t, int l, int c) {
		name = n;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + ": " + type + " => " + lineNumber + " | " + columnNumber);
	}
}

public class GenericsNames {
	// name, type, lineNumber, columnNumber
	List<GenericsObject> genericsObjectList;

	public GenericsNames() {
		this.genericsObjectList = new ArrayList<>();
	}
	
	public void addGenerics(String name, Type type, int lineNumber, int columnNumber) {
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
