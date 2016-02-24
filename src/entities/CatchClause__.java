package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class CatchClauseObject {
	Type exception;
	String variableName;
	String className;
	String methodName;
	int lineNumber;
	int columnNumber;

	CatchClauseObject(Type e, String v, String cn, String mn, int l, int c) {
		exception = e;
		variableName = v;
		className = cn;
		methodName = mn;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(exception + " " + variableName + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class CatchClause__ {
	List<CatchClauseObject> catchClauseObjectList;

	public CatchClause__() {
		catchClauseObjectList = new ArrayList<>();
	}

	public void addCatch(Type exception, String variableName, String className, String methodName, int lineNumber, int columnNumber) {
		catchClauseObjectList.add(new CatchClauseObject(exception, variableName, className, methodName, lineNumber, columnNumber));
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
