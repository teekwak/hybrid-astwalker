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
	
	public void printClasses() {
		if(classesMap.size() != 0) {
			System.out.println("---Classes---");
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.classesMap.entrySet()) {
				System.out.println(entry.getKey());
			}			
		}
	}
	
	public void printExtends() {
		if(extendsList.size() != 0) {
			System.out.println("---Extends---");
			for(String s : this.extendsList) {
				System.out.println(s);
			}				
		} 
	}
}
