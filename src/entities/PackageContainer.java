package entities;

import java.util.List;
import java.util.ArrayList;

class PackageObject {
	String name;
	String fullyQualifiedName;
	int lineNumber;
	int columnNumber;

	PackageObject(String n, String fqn, int l, int c) {
		name = n;
		fullyQualifiedName = fqn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber + " | " + columnNumber);
	}
}

public class PackageContainer {
	List<PackageObject> packageObjectList;

	public PackageContainer() {
		packageObjectList = new ArrayList<>();
	}

	public void addPackage(String name, String fullyQualifiedName, int lineNumber, int columnNumber) {
		packageObjectList.add(new PackageObject(name, fullyQualifiedName, lineNumber, columnNumber));
	}
	
	public void printAll() {
		System.out.println("--- Packages ---");

		if(packageObjectList.size() > 0) {
			for(PackageObject obj : packageObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
