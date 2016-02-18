package entities;

import java.util.HashMap;
import java.util.Map;

public class CatchClauseNames {
	Map<String, Map<Integer, Integer>> catchClauseMap;
	
	public CatchClauseNames() {
		catchClauseMap = new HashMap<>();
	}
	
	public void addCatch(String name, int lineNumber, int columnNumber) {
		Map<Integer, Integer> tempMap = new HashMap<>();
		tempMap.put(lineNumber, columnNumber);
		this.catchClauseMap.put(name, tempMap);
	}
	
	public void printCatchClauses() {
		if(catchClauseMap.size() != 0) {
			System.out.println("---Catch Clauses---");
			for(Map.Entry<String, Map<Integer, Integer>> entry : this.catchClauseMap.entrySet()) {
				System.out.println(entry.getKey());
			}			
		}
	}
}
