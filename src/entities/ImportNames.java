package entities;

import java.util.List;
import java.util.ArrayList;

class ImportObject {
	String name;
	int lineNumber;
	int columnNumber;

	ImportObject(String n, int l, int c) {
		name = n;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(name + " => " + lineNumber + " | " + columnNumber);
	}
}

public class ImportNames {
	List<ImportOject> importObjectList;
	
	public ImportNames() {
		importObjectList = new ArrayList();
	}
	
	public void addImport(String name, int lineNumber, int columnNumber) {
		importObjectList.add(new ImportObject(name, lineNumber, columnNumber));
	}

	public void printAllImports() {
		System.out.println("--- Imports ---");

		if(importObjectList.size() > 0) {
			for(ImportObject obj : importObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}	
		else {
			System.out.println("None\n");
		}
	}
}
