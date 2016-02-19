package entities;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassNames {
	Map<String, Map<Integer, Integer>> classesMap;
	List<String> extendsList;

	public ClassNames() {
		classesMap = new HashMap<>();
		extendsList = new ArrayList<>();
	}
	
	public void addClass(String name, int lineNumber, int columnNumber) {
		Map<Integer, Integer> tempMap = new HashMap<>();
		tempMap.put(lineNumber, columnNumber);
		this.classesMap.put(name, tempMap);
	}
	
	public void addExtends(String name) {
		extendsList.add(name);
	}
	
	public void printAllClasses() {
		System.out.println("--- Classes ---");

		if(classesMap.size() > 0) {
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.classesMap.entrySet()) {
				System.out.println(entry.getKey());
			}			
		}
		else {
			System.out.println("None\n");
		}
	}
	
	public void printAllExtends() {
		System.out.println("--- Extends ---");

		if(extendsList.size() > 0) {
			for(String s : this.extendsList) {
				System.out.println(s);
			}
			System.out.println();
		} 
		else {
			System.out.println("None\n");
		}
	}
}
