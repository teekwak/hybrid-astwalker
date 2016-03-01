package entities;

import java.util.List;
import java.util.ArrayList;

class PackageObject {
	String name;
	int lineNumber;
	int columnNumber;

	PackageObject(String n, int l, int c) {
		name = n;
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

	public void addPackage(String name, int lineNumber, int columnNumber) {
		packageObjectList.add(new PackageObject(name, lineNumber, columnNumber));
	}

	public List<PackageObject> getPackageObjectList() {
		return packageObjectList;
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
}
