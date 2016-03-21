package obsolete;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

// this is going to get confused with SimpleName. I guarantee it.
// specifically to catch Object as a Type
/*
class SimpleNameObject {
	String variableName;
	String fullyQualifiedName;
	String className;
	String methodName;
	Type type;
	int lineNumber;
	int columnNumber;

	SimpleNameObject(String v, String fqn, String cn, String mn, Type t, int l, int c) {
		variableName = v;
		fullyQualifiedName = fqn;
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
*/
public class SimpleName__ {
	List<SimpleObject> simpleNameObjectList;

	public SimpleName__() {
		this.simpleNameObjectList = new ArrayList<>();
	}

	public void addSimpleName(String variableName, String fullyQualifiedName, String className, String methodName, Type type, int lineNumber, int columnNumber) {
		simpleNameObjectList.add(new SimpleObject(variableName, fullyQualifiedName, className, methodName, type, lineNumber, columnNumber));
	}
	
	public void printAllSimpleNames() {
		System.out.println("--- Simple Names ---");
		if(simpleNameObjectList.size() > 0) {
			for(SimpleObject obj : simpleNameObjectList) {
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
	
	public List<SimpleObject> getSimpleNameObjectList() {
		return this.simpleNameObjectList;
	}
	
	public List<Integer> getLineNumbers() {
		List<Integer> list = new ArrayList<>();
		for(SimpleObject obj : simpleNameObjectList) {
			list.add(obj.lineNumber);
		}
		return list;
	}

	public List<Integer> getColumnNumbers() {
		List<Integer> list = new ArrayList<>();
		for(SimpleObject obj : simpleNameObjectList) {
			list.add(obj.columnNumber);
		}
		return list;
	}
	
	public List<String> getNames() {
		List<String> list = new ArrayList<>();
		for(SimpleObject obj : simpleNameObjectList) {
			list.add(obj.variableName);
		}
		return list;
	}
	
	/*
	 * end testing
	 */
}
