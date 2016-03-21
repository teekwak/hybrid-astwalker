package obsolete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entities.SwitchStatementObject;
/*
class SwitchStatementObject {

	String expression;
	String className;
	String methodName;
	Map<String, Map<Integer, Integer>> switchCaseMap;
	int lineNumber;
	int columnNumber;

	SwitchStatementObject(String e, String cn, String mn, Map<String, Map<Integer, Integer>> s, int l, int c) {
		expression = e;
		className = cn;
		methodName = mn;
		switchCaseMap = s;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println("Switch Statement: " + expression + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
		
		for(Map.Entry<String, Map<Integer, Integer>> entry : switchCaseMap.entrySet()) {
			System.out.print("Switch Case: " + entry.getKey() + " => ");
			for(Map.Entry<Integer, Integer> positionEntry : entry.getValue().entrySet()) {
				System.out.print(positionEntry.getKey() + " | " + positionEntry.getValue());
			}
			System.out.println();
		}
	}
}
*/
public class SwitchStatement__ {
	public List<SwitchStatementObject> switchStatementObjectList;

	public SwitchStatement__() {
		this.switchStatementObjectList = new ArrayList<>();
	}

	public void addSwitchStatement(String expression, String className, String methodName, Map<String, Map<Integer, Integer>> switchCaseMap, int lineNumber, int columnNumber) {
		switchStatementObjectList.add(new SwitchStatementObject(expression, className, methodName, switchCaseMap, lineNumber, columnNumber));
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
	
	/*
	 * start testing
	 */
	
	public List<SwitchStatementObject> getSwitchStatementObjectList() {
		return this.switchStatementObjectList;
	}	
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(SwitchStatementObject obj : switchStatementObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(SwitchStatementObject obj : switchStatementObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getExpressions() {
		List<String> list = new ArrayList<>();
		for(SwitchStatementObject obj : switchStatementObjectList) {
			list.add(obj.expression);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
