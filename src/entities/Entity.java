package entities;

import org.eclipse.jdt.core.dom.Type;

public interface Entity {
		
	Type t = null;
	String SolrKey = null;
	Object SolrValue = null;
	
	public enum EntityType {
		ARRAY, CATCH_CLAUSE, CLASS, CONDITIONAL_EXPRESSION, DO_STATEMENT, 
		FOR_STATEMENT, GENERICS, IF_STATEMENT, INFIX_EXPRESSION, 
		METHOD_DECLARATION, METHOD_INVOCATION, PRIMITIVE, SIMPLE, 
		SWITCH_STATEMENT, THROW_STATEMENT, TRY_STATEMENT, WHILE_STATEMENT, 
		WILDCARD
	}
	
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

	public void addEntity(SuperEntityClass entity, EntityType ET);
}
