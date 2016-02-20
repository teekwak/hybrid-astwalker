package entities;

import java.util.HashMap;
import java.util.Map;

public class ImportNames {
	Map<String, Map<Integer, Integer>> importsMap;
	
	public ImportNames() {
		importsMap = new HashMap<>();
	}
	
	public void addImport(String name, int lineNumber, int columnNumber) {
		Map<Integer, Integer> tempMap = new HashMap<>();
		tempMap.put(lineNumber, columnNumber);
		this.importsMap.put(name, tempMap);
	}
	
	public void printAllImports() {
		System.out.println("--- Imports ---");

		if(importsMap.size() > 0) {
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.importsMap.entrySet()) {
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
