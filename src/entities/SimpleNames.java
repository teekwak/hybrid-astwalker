package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

// this is going to get confused with SimpleName. I guarantee it.
// specifically to catch Object as a Type

class SimpleNameObject {
	String variableName;
	Type type;
	int lineNumber;
	int columnNumber;
	
	SimpleNameObject(String v, Type t, int l, int c) {
		variableName = v;
		type = t;
		lineNumber = l;
		columnNumber = c;
	}
	
	void printEntity() {
		System.out.println(type + " => " + lineNumber + " | " + columnNumber);
	}
}

public class SimpleNames {
	List<SimpleNameObject> simpleNameObjectList;
	
	public SimpleNames() {
		this.simpleNameObjectList = new ArrayList<>();
	}
	
	public void addSimpleName(String variableName, Type type, int lineNumber, int columnNumber) {
		simpleNameObjectList.add(new SimpleNameObject(variableName, type, lineNumber, columnNumber));
	}
	
	public void printAllSimpleNames() {
		System.out.println("--- Simple Names ---");
		if(simpleNameObjectList.size() > 0) {
			for(SimpleNameObject obj : simpleNameObjectList) {
				obj.printEntity();
			}	
			System.out.println();
		}
		else {
			System.out.println("None\n");
		}
	}
}
