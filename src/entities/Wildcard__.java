package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class WildcardObject {
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	WildcardObject(String cn, String mn, Type t, int l, int c) {
		className = cn;
		methodName = mn;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}

	void printEntity() {
		System.out.println(type + " => " + lineNumber + " | " + columnNumber);
		System.out.println("\tClass:\t" + className);
		System.out.println("\tMethod:\t" + methodName);
	}
}

public class Wildcard__ {
	List<WildcardObject> wildcardObjectList;

	public Wildcard__() {
		wildcardObjectList = new ArrayList<>();
	}

	public void addWildcard(String className, String methodName, Type type, int lineNumber, int columnNumber) {
		wildcardObjectList.add(new WildcardObject(className, methodName, type, lineNumber, columnNumber));
	}

	public void printAllWildcards() {
		System.out.println("--- Wild Cards ---");
		if(wildcardObjectList.size() > 0) {
			for(WildcardObject obj : wildcardObjectList) {
				obj.printEntity();
			}
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}

	}
}
