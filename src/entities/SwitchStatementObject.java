package entities;

import java.util.HashMap;
import java.util.Map;

public class SwitchStatementObject extends SuperEntityClass {

	Map<String, Map<Integer, Integer>> switchCaseMap;
	
	public SwitchStatementObject() {
		this.switchCaseMap = new HashMap<>();
	}
	
	public void setSwitchCaseMap(Map<String, Map<Integer, Integer>> map) {
		this.switchCaseMap = map;
	}
	
	public Map<String, Map<Integer, Integer>> getSwitchCaseMap() {
		return this.switchCaseMap;
	}
	
}
