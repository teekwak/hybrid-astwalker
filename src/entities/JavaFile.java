package entities;

import java.util.ArrayList;
import java.util.List;

public class JavaFile {
	List<ClassObject> classObjectList;
	
	public JavaFile() {
		classObjectList = new ArrayList<>();
	}
	
	public void printAll() {
		for(ClassObject co : classObjectList) {			
			co.printInfo();
		}
	}
	
	public void addClass(ClassObject co) {
		classObjectList.add(co);
	}
}
