package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class SwitchStatementObject {
	
	String expression;
	Map<String, Map<Integer, Integer>> switchCaseMap;
	int lineNumber;
	int columnNumber;

	SwitchStatementObject(String e, Map<String, Map<Integer, Integer>> s, int l, int c) {
		expression = e;
		switchCaseMap = s;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println("Switch Statement: " + expression + " => " + lineNumber + " | " + columnNumber);
	
		for(Map.Entry<String, Map<Integer, Integer>> entry : switchCaseMap.entrySet()) {
			System.out.println("Switch Case: " + entry.getKey() + " ");
			for(Map.Entry<Integer, Integer> positionEntry : entry.getValue().entrySet()) {
				System.out.print(positionEntry.getKey() + " | " + positionEntry.getValue());
			}
			System.out.println();
		}
	}
}

public class SwitchStatementNames {	
	public List<SwitchStatementObject> switchStatementObjectList;
	
	public SwitchStatementNames() {
		this.switchStatementObjectList = new ArrayList<>();
	}
	
	public void addSwitchStatement(String expression, Map<String, Map<Integer, Integer>> switchCaseMap, int lineNumber, int columnNumber) {
		switchStatementObjectList.add(new SwitchStatementObject(expression, switchCaseMap, lineNumber, columnNumber));
	}
	
	public void printAllSwitchStatements() {
		System.out.println("--- Switch Statements ---");
		
		if(switchStatementObjectList.size() > 0) {
			for(SwitchStatementObject obj : switchStatementObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
