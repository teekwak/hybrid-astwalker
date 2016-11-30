package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class SuperEntityClass implements Entity {

	List<Entity> entityList;

	private String name;
	private String fullyQualifiedName;
	private Type type;
	private String bound;
	private int lineNumber;
	private int columnNumber;
	private int startCharacter;
	private int endCharacter;
	private int numberOfCharacters;
	private int endLine;
	int cyclomaticComplexity;
	int totalMethodInvocationCount;
	
	public SuperEntityClass() {
		entityList = new ArrayList<>();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public void setStartCharacter(int n) {
		this.startCharacter = n;
	}
	
	public int getStartCharacter() {
		return this.startCharacter;
	}
	
	public void setEndCharacter(int n) {
		this.endCharacter = n;
	}
	
	public int getEndCharacter() {
		return this.endCharacter;
	} 
	
	public void setLineNumber(int n) {
		this.lineNumber = n;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}

	public void setColumnNumber(int n) {
		this.columnNumber = n;
	}

	public int getColumnNumber() {
		return this.columnNumber;
	}
	
	public void setNumberOfCharacters(int n) {
		this.numberOfCharacters = n;
	}
	
//	public int getNumberOfCharacters() {
//		return this.numberOfCharacters;
//	}
	
	public void setEndLine(int n) {
		this.endLine = n;
	}
	
	public int getEndLine() {
		return this.endLine;
	}
	public void setType(Type t) {
		this.type = t;
	}
	
	public Type getType() {
		return this.type;
	}
	
//	public String getTypeName() {
//		return this.type.toString();
//	}
	
	public void setFullyQualifiedName(String fqn) {
		this.fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	List<Entity> getEntityList() {
		return this.entityList;
	}
	
	public void printInfo() {
		System.out.println("\t" + this.name + " [" + this.lineNumber + " | " + this.columnNumber + "]");		
	}
	
	public void addEntity(SuperEntityClass entity, EntityType ET) {
		entityList.add(entity);
	}
	
	public void addEntities(List<SuperEntityClass> listOfEntities, EntityType ET) {
		entityList.addAll(listOfEntities);
	}

	public void setBound(String s) {
		this.bound = s;		
	}
	
	public String getBound() {
		return this.bound;
	}
}
