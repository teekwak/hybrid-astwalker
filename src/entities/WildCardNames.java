package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

class WildCardObject {
	Type type;
	int lineNumber;
	int columnNumber;
	
	WildCardObject(Type t, int l, int c) {
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(type + " => " + lineNumber + " | " + columnNumber);
	}
}

public class WildCardNames {
	List<WildCardObject> wildCardObjectList;
	
	public WildCardNames() {
		wildCardObjectList = new ArrayList<>();
	}
	
	public void addWildCard(Type type, int lineNumber, int columnNumber) {
		wildCardObjectList.add(new WildCardObject(type, lineNumber, columnNumber));
	}
	
	public void printAllWildCards() {
		System.out.println("--- Wild Cards ---");
		if(wildCardObjectList.size() > 0) {
			for(WildCardObject obj : wildCardObjectList) {
				obj.printEntity();
			}
		}
		else {
			System.out.println("None\n");
		}
		
	}
}
