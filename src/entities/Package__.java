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

public class Package__ {
	List<PackageObject> packageObjectList;

	public Package__() {
		packageObjectList = new ArrayList<>();
	}

	public void addPackage(String name, String fullyQualifiedName, int lineNumber, int columnNumber) {
		packageObjectList.add(new PackageObject(name, fullyQualifiedName, lineNumber, columnNumber));
	}
	
	public void printAllPackages() {
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
	
	/*
	 * start testing
	 */
	
	public List<PackageObject> getPackageObjectList() {
		return packageObjectList;
	}

	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(PackageObject obj : packageObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}
	
	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(PackageObject obj : packageObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}	
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(PackageObject obj : packageObjectList) {
			list.add(obj.name);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
