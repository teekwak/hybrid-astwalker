package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class SuperEntityClass implements Entity {

	String name;
	String fullyQualifiedName;
	Type type;
	int lineNumber;
	int columnNumber;
	
	List<Entity> arrayList;
	
	public SuperEntityClass() {
		this.arrayList = new ArrayList<>();
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
	
	public void setFullyQualifiedName(String fqn) {
		this.fullyQualifiedName = fqn;
	}
	
	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}
	
	public void printInfo() {
		System.out.println(this.name + " (" + this.lineNumber + " | " + this.columnNumber + ")");		
	}

	public Type getType() {
		return this.type;
	}

	public void addArray(Entity ao) {
		this.arrayList.add(ao);
	}

	@Override
	public void addCatchClause(Entity cco) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addForStatement(Entity fso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addIfStatement(Entity iso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDoStatement(Entity dso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWhileStatement(Entity wso) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addWildcard(Entity wo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addGenerics(Entity go) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMethodDeclaration(MethodDeclarationObject temp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMethodInvocation(MethodInvocationObject mio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPrimitive(Entity po) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSimple(Entity so) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addThrowStatement(Entity to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addTryStatement(Entity tso) {
		// TODO Auto-generated method stub
		
	}
	
}
