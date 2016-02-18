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
	
	public void printPackages() {
		if(packagesMap.size() != 0) {
			System.out.println("---Packages---");
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.packagesMap.entrySet()) {
				System.out.println(entry.getKey());
			}			
		}
	}
}
