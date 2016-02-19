package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class CatchClauseObject {
	Type exception;
	String variableName;
	int lineNumber;
	int columnNumber;

	CatchClauseObject(Type e, String v, int l, int c) {
		exception = e;
		variableName = v;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(exception + " " + variableName + " => " + lineNumber + " | " + columnNumber);
	}
}

public class CatchClauseNames {
	List<CatchClauseObject> catchClauseObjectList;
	
	public CatchClauseNames() {
		catchClauseObjectList = new ArrayList<>();
	}
	
	public void addCatch(Type exception, String variableName, int lineNumber, int columnNumber) {
		catchClauseObjectList.add(new CatchClauseObject(exception, variableName, lineNumber, columnNumber));
	}
	
	public void printAllCatchClauses() {
		System.out.println("--- Catch Clauses ---");

		if(catchClauseObjectList.size() > 0) {
			for(CatchClauseObject obj : catchClauseObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
