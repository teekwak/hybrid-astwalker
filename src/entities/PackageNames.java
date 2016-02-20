package entities;

import java.util.HashMap;
import java.util.Map;

public class PackageNames {
	Map<String, Map<Integer, Integer>> packagesMap;
	
	public PackageNames() {
		packagesMap = new HashMap<>();
	}
	
	public void addPackage(String name, int lineNumber, int columnNumber) {
		Map<Integer, Integer> tempMap = new HashMap<>();
		tempMap.put(lineNumber, columnNumber);
		this.packagesMap.put(name, tempMap);
	}
	
	public void printAllPackages() {
		System.out.println("--- Packages ---");

		if(packagesMap.size() > 0) {
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.packagesMap.entrySet()) {
				System.out.print(entry.getKey() + " => ");
				for(Map.Entry<Integer, Integer> position : entry.getValue().entrySet()) {
					System.out.print(position.getKey() + " | " + position.getValue());
				}
				System.out.println();
			}			
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
