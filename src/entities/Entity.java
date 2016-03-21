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

	public void addArray(Entity ao);
	public void addCatchClause(Entity cco);
	public void addConditionalExpression(Entity ceo);
	public void addDoStatement(Entity dso);
	public void addForStatement(Entity fso);
	public void addGenerics(Entity go);
	public void addIfStatement(Entity iso);
	public void addInfixExpression(Entity ieo);
	public void addMethodDeclaration(MethodDeclarationObject temp);
	public void addMethodInvocation(MethodInvocationObject mio);
	public void addPrimitive(Entity po);
	public void addSimple(Entity so);
	public void addSwitchStatement(Entity sso);
	public void addThrowStatement(Entity to);
	public void addTryStatement(Entity tso);
	public void addWhileStatement(Entity wso);
	public void addWildcard(Entity wo);
}
