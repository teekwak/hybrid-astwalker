package entities;

import java.util.List;
import java.util.ArrayList;

class ImportObject {
	String name;
	String fullyQualifiedName;
	int lineNumber;
	int columnNumber;

	ImportObject(String n, String fqn, int l, int c) {
		name = n;
		fullyQualifiedName = fqn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber + " | " + columnNumber);
	}
}

public class ImportContainer {
	List<ImportObject> importObjectList;

	public ImportContainer() {
		importObjectList = new ArrayList<>();
	}

	public void addImport(String name, String fullyQualifiedName, int lineNumber, int columnNumber) {
		importObjectList.add(new ImportObject(name, fullyQualifiedName, lineNumber, columnNumber));
	}
	
	public void printAll() {
		if(importObjectList.size() > 0) {
			for(ImportObject obj : importObjectList) {
				obj.printEntity();
			}
		}
		else {
			System.out.println("No Imports\n");
		}
	}
}
