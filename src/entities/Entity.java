package entities;

import org.eclipse.jdt.core.dom.Type;

public interface Entity {
		
	Type t = null;
	String SolrKey = null;
	Object SolrValue = null;
	
	public void printInfo();
	
	public void setName(String n);
	public String getName();
	
	public void setFullyQualifiedName(String fullyQualifiedName);
	public String getFullyQualifiedName();
	
	public void setLineNumber(int n);
	public int getLineNumber();
	
	public void setColumnNumber(int n);
	public int getColumnNumber();
	
	public void setType(Type t);
	public Type getType();

	public void addArray(SuperEntityClass ao);
	public void addCatchClause(SuperEntityClass cco);
	public void addConditionalExpression(SuperEntityClass ceo);
	public void addDoStatement(SuperEntityClass dso);
	public void addForStatement(SuperEntityClass fso);
	public void addGenerics(SuperEntityClass go);
	public void addIfStatement(SuperEntityClass iso);
	public void addInfixExpression(SuperEntityClass ieo);
	public void addMethodDeclaration(MethodDeclarationObject temp);
	public void addMethodInvocation(MethodInvocationObject mio);
	public void addPrimitive(SuperEntityClass po);
	public void addSimple(SuperEntityClass so);
	public void addSwitchStatement(SuperEntityClass sso);
	public void addThrowStatement(SuperEntityClass to);
	public void addTryStatement(SuperEntityClass tso);
	public void addWhileStatement(SuperEntityClass wso);
	public void addWildcard(SuperEntityClass wo);
}
