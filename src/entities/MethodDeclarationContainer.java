package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationContainer {
	List<MethodDeclarationObject> methodDeclarationObjectList;
	
	public MethodDeclarationContainer() {
		methodDeclarationObjectList = new ArrayList<>();
	}
	
	public void printAll() {
		for(MethodDeclarationObject mdo : methodDeclarationObjectList) {
			System.out.println(mdo.getName());
		}
	}
	
	public void addMethodDeclaration(MethodDeclarationObject mdo) {
		methodDeclarationObjectList.add(mdo);
	}
	
}
