package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class MethodDeclarationObject extends SuperEntityClass {

	List<Entity> entitiesInsideMethodDeclaration;
	
	public MethodDeclarationObject() {
		this.entitiesInsideMethodDeclaration = new ArrayList<>();
	}
	
	public MethodDeclarationObject(String name, int lineNumber, int columnNumber) {
		this.name = name;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}
	
	public MethodDeclarationObject(String string, String string2, String name, Type returnType2, boolean varargs,
			boolean constructor, boolean isStatic, boolean isAbstract, List<Object> parameters, int lineNumber,
			int columnNumber) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addChild(Entity e) {
		entitiesInsideMethodDeclaration.add(e);		
	}

	@Override
	public void printInfo() {
		System.out.println(this.name);
		for(Entity e : entitiesInsideMethodDeclaration) {
			e.printInfo();
		}
	}

	@Override
	public void setSuperClass(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSuperClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addImplementsInterface(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getImplements() {
		// TODO Auto-generated method stub
		return null;
	}
}