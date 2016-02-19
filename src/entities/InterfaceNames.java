package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceNames {
	Map<String, Map<Integer, Integer>> interfacesMap;
	List<String> implementsList;

	public InterfaceNames() {
		interfacesMap = new HashMap<>();
		implementsList = new ArrayList<>();
	}
	
	public void addImplements(String name) {
		implementsList.add(name);
	}
	
	public void addInterface(String name, int lineNumber, int columnNumber) {
		Map<Integer, Integer> tempMap = new HashMap<>();
		tempMap.put(lineNumber, columnNumber);
		this.interfacesMap.put(name, tempMap);
	}
	
	public void printAllInterfaces() {
		if(interfacesMap.size() > 0) {
			System.out.println("--- Interfaces ---");
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.interfacesMap.entrySet()) {
				System.out.println(entry.getKey());
			}			
		}
	}
	
	public void printAllImplements() {
		System.out.println("--- Implements ---");

		if(implementsList.size() > 0) {
			for(String s : this.implementsList) {
				System.out.println(s);
			}			
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
