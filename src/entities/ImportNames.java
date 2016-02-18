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
	
	public void printImports() {
		if(importsMap.size() != 0) {
			System.out.println("---Imports---");
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.importsMap.entrySet()) {
				System.out.println(entry.getKey());
			}			
		}
	}
}
