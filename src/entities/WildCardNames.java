package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class WildcardObject {
	Type type;
	int lineNumber;
	int columnNumber;
	
	WildcardObject(Type t, int l, int c) {
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(type + " => " + lineNumber + " | " + columnNumber);
	}
}

public class WildcardNames {
	List<WildcardObject> wildcardObjectList;
	
	public WildcardNames() {
		wildcardObjectList = new ArrayList<>();
	}
	
	public void addWildcard(Type type, int lineNumber, int columnNumber) {
		wildcardObjectList.add(new WildcardObject(type, lineNumber, columnNumber));
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
