package entities;

import java.util.Map;

public class SwitchStatementObject extends SuperEntityClass {

	Map<String, Map<Integer, Integer>> switchCaseMap;
	
	public SwitchStatementObject() {
		
	}
	
	public void setSwitchCaseMap(Map<String, Map<Integer, Integer>> map) {
		this.switchCaseMap = map;
	}
	
	public Map<String, Map<Integer, Integer>> getSwitchCaseMap() {
		return this.switchCaseMap;
	}
	
}
