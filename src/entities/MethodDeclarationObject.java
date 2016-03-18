package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class MethodDeclarationObject implements Entity {

	String name;
	int lineNumber;
	int columnNumber;
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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String n) {
		this.name = n;
	}
	
	@Override
	public void setLineNumber(int n) {
		this.lineNumber = n;
	}

	@Override
	public int getLineNumber() {
		return this.lineNumber;
	}

	@Override
	public void setColumnNumber(int n) {
		this.columnNumber = n;
	}

	@Override
	public int getColumnNumber() {
		return this.columnNumber;
	}
	
}