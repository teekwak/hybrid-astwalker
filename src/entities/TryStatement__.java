package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CatchClause;

class TryStatementObject {
	String tryBody;
	String className;
	String methodName;
	List<CatchClause> catchClauseList;
	String finallyBody;
	int lineNumber;
	int columnNumber;

	TryStatementObject(String t, String cn, String mn, List<CatchClause> list, String f, int l, int c) {
		tryBody = t;
		className = cn;
		methodName = mn;
		catchClauseList = list;
		finallyBody = f;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println("Try statement => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class TryStatement__ {
	List<TryStatementObject> tryStatementObjectList;

	public TryStatement__() {
		tryStatementObjectList = new ArrayList<>();
	}

	public void addTryStatement(String tryBody, String className, String methodName, List<CatchClause> catchClauseList, String finallyBody, int lineNumber, int columnNumber) {
		tryStatementObjectList.add(new TryStatementObject(tryBody, className, methodName, catchClauseList, finallyBody, lineNumber, columnNumber));
	}

	public void printAllTryStatements() {
		System.out.println("--- Try Statements ---");

		if(tryStatementObjectList.size() > 0) {
			for(TryStatementObject obj : tryStatementObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
