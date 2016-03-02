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

public class Import__ {
	List<ImportObject> importObjectList;

	public Import__() {
		importObjectList = new ArrayList<>();
	}

	public void addImport(String name, int lineNumber, int columnNumber) {
		importObjectList.add(new ImportObject(name, lineNumber, columnNumber));
	}

	public List<ImportObject> getImportObjectList() {
		return this.importObjectList;
	}

	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ImportObject obj : importObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(ImportObject obj : importObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
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
	
	/*
	 * start testing
	 */
	
	/*
	 * end testing
	 */
}
