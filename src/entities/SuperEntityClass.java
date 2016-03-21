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
	List<SuperEntityClass> switchCaseList;
	
	List<SuperEntityClass> arrayList;
	List<SuperEntityClass> genericsList;
	List<SuperEntityClass> primitiveList;
	List<SuperEntityClass> simpleList;
	List<SuperEntityClass> wildcardList;
	
	public SuperEntityClass() {
		this.arrayList = new ArrayList<>();
		this.genericsList = new ArrayList<>();
		this.primitiveList = new ArrayList<>();
		this.simpleList = new ArrayList<>();
		this.wildcardList = new ArrayList<>();
		
		this.switchCaseList = new ArrayList<>();
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

	public void setSwitchCaseList(List<SuperEntityClass> scl) {
		this.switchCaseList = scl;
	}
	
	public List<SuperEntityClass> getSwitchCaseList() {
		return this.switchCaseList;
	}
	
	public void addArray(SuperEntityClass ao) {
		this.arrayList.add(ao);
	}

	public void addCatchClause(SuperEntityClass cco) {
		// Method Declaration only
	}

	public void addConditionalExpression(SuperEntityClass ceo) {
		// Method Declaration only
	}
	
	public void addDoStatement(SuperEntityClass dso) {
		// Method Declaration only
	}
	
	public void addForStatement(SuperEntityClass fso) {
		// Method Declaration only		
	}
	
	public void addGenerics(SuperEntityClass go) {
		this.genericsList.add(go);
	}

	public void addIfStatement(SuperEntityClass iso) {
		// Method Declaration only
	}

	public void addInfixExpression(SuperEntityClass ieo) {
		// Method Declaration only
	}
	
	public void addMethodDeclaration(MethodDeclarationObject temp) {
		// Class only	
	}
	
	public void addMethodInvocation(MethodInvocationObject mio) {
		// Method Declaration only
	}
	
	public void addPrimitive(SuperEntityClass po) {
		this.primitiveList.add(po);
	}
	
	public void addSimple(SuperEntityClass so) {
		this.simpleList.add(so);
	}

	public void addSwitchStatement(SuperEntityClass sso) {
		// Method Declaration only
	}
	
	public void addThrowStatement(SuperEntityClass to) {
		// Method Declaration only
	}

	public void addTryStatement(SuperEntityClass tso) {
		// Method Declaration only
	}
	
	public void addWhileStatement(SuperEntityClass wso) {
		// Method Declaration only
	}

	// not working
	public void addWildcard(SuperEntityClass wo) {
		this.wildcardList.add(wo);
	}
	
}
