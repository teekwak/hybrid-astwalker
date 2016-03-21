package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class SuperEntityClass implements Entity {

	List<Entity> entityList;
	
	String name;
	String fullyQualifiedName;
	Type type;
	int lineNumber;
	int columnNumber;
	
	public SuperEntityClass() {
		entityList = new ArrayList<>();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
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
	
	public void setType(Type t) {
		this.type = t;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String getTypeName() {
		return this.type.toString();
	}
	
	public void setFullyQualifiedName(String fqn) {
		this.fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	public List<Entity> getEntityList() {
		return this.entityList;
	}
	
	public void printInfo() {
		System.out.println(this.name + " (" + this.lineNumber + " | " + this.columnNumber + ")");		
	}
	
	public void addEntity(SuperEntityClass entity, EntityType ET) {
		entityList.add(entity);
	}
	
	public void addEntities(List<SuperEntityClass> listOfEntities, EntityType ET) {
		entityList.addAll(listOfEntities);
	}
}
