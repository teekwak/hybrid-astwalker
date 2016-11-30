package entities;

import org.eclipse.jdt.core.dom.Type;

public interface Entity {
		
	Type t = null;
	String SolrKey = null;
	Object SolrValue = null;
	
	enum EntityType {
		ARRAY, CATCH_CLAUSE, CLASS, CONDITIONAL_EXPRESSION, DO_STATEMENT, 
		FOR_STATEMENT, GENERICS, GLOBAL, IF_STATEMENT, INFIX_EXPRESSION, 
		METHOD_DECLARATION, METHOD_INVOCATION, PRIMITIVE, SIMPLE, SWITCH_CASE,
		SWITCH_STATEMENT, THROW_STATEMENT, TRY_STATEMENT, UNION, WHILE_STATEMENT, 
		WILDCARD
	}
	
	void printInfo();
	
	void setName(String n);
	String getName();
	
	void setFullyQualifiedName(String fullyQualifiedName);
	String getFullyQualifiedName();
	
	void setLineNumber(int n);
	int getLineNumber();
	
	void setColumnNumber(int n);
	int getColumnNumber();

	void setType(Type t);
	Type getType();

	void addEntity(SuperEntityClass entity, EntityType ET);
}
