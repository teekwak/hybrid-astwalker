package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationObject extends SuperEntityClass {
	
	boolean isStatic;
	boolean isAbstract;
	List<String> thrownExceptions;
	List<?> parametersList;
	
	List<SuperEntityClass> arrayList;
	List<SuperEntityClass> catchClauseList;
	List<SuperEntityClass> classList;
	List<SuperEntityClass> conditionalExpressionList;
	List<SuperEntityClass> doStatementList;
	List<SuperEntityClass> forStatementList;
	List<SuperEntityClass> genericsList;
	List<SuperEntityClass> ifStatementList;
	List<SuperEntityClass> infixExpressionList;
	List<MethodInvocationObject> methodInvocationList;
	List<SuperEntityClass> primitiveList;
	List<SuperEntityClass> simpleList;
	List<SuperEntityClass> switchStatementList;
	List<SuperEntityClass> throwStatementList;
	List<SuperEntityClass> tryStatementList;
	List<SuperEntityClass> whileStatementList;
	List<SuperEntityClass> wildcardList;
	
	List<SuperEntityClass> switchCaseList;

	public MethodDeclarationObject() {
		this.thrownExceptions = new ArrayList<>();
		this.parametersList = new ArrayList<>();
		
		this.arrayList = new ArrayList<>();
		this.catchClauseList = new ArrayList<>();
		this.classList = new ArrayList<>();
		this.conditionalExpressionList = new ArrayList<>();
		this.doStatementList = new ArrayList<>();
		this.forStatementList = new ArrayList<>();
		this.genericsList = new ArrayList<>();
		this.ifStatementList = new ArrayList<>();
		this.infixExpressionList = new ArrayList<>();
		this.methodInvocationList = new ArrayList<>();
		this.primitiveList = new ArrayList<>();
		this.simpleList = new ArrayList<>();
		this.switchStatementList = new ArrayList<>();
		this.throwStatementList = new ArrayList<>();
		this.tryStatementList = new ArrayList<>();
		this.whileStatementList = new ArrayList<>();
		this.wildcardList = new ArrayList<>();
		
		this.switchCaseList = new ArrayList<>();
	}
	
	@Override
	public void printInfo() {
		StringBuilder s = new StringBuilder();
		s.append("MethodDeclarationObject " + this.getName());
				
		if(this.thrownExceptions.size() > 0) {
			s.append(" throws");
			for(String ex : this.thrownExceptions) {
				s.append(" " + ex);
			}
		}
		
		s.append(" (" + this.lineNumber + " | " + this.columnNumber + ")");
		
		System.out.println(s.toString());
		
		for(Entity e : arrayList) {
			e.printInfo();
		}
		
		for(Entity e : classList) {
			e.printInfo();
		}
		
		for(Entity e : catchClauseList) {
			e.printInfo();
		}
		
		for(Entity e : conditionalExpressionList) {
			e.printInfo();
		}
		
		for(Entity e : doStatementList) {
			e.printInfo();
		}
		
		for(Entity e : forStatementList) {
			e.printInfo();
		}
		
		for(Entity e : genericsList) {
			e.printInfo();
		}
		
		for(Entity e : ifStatementList) {
			e.printInfo();
		}
		
		for(Entity e : infixExpressionList) {
			e.printInfo();
		}
		
		for(Entity e : methodInvocationList) {
			e.printInfo();
		}
		
		for(Entity e : primitiveList) {
			e.printInfo();
		}
		
		for(Entity e : simpleList) {
			e.printInfo();
		}
		
		for(Entity e : switchStatementList) {
			e.printInfo();
		}
		
		for(Entity e : throwStatementList) {
			e.printInfo();
		}
		
		for(Entity e : tryStatementList) {
			e.printInfo();
		}
		
		for(Entity e : wildcardList) {
			e.printInfo();
		}
	}

	public void addThrowsException(String s) {
		this.thrownExceptions.add(s);
	}

	public List<String> getThrowsException() {
		return this.thrownExceptions;
	}
	
	public void setParametersList(List<?> pl) {
		this.parametersList = pl;
	}
	
	public List<?> getParametersList() {
		return this.parametersList;
	}
	
	public void setStatic(boolean tf) {
		this.isStatic = tf;
	}
	
	public boolean getStatic() {
		return this.isStatic;
	}
	
	public void setAbstract(boolean tf) {
		this.isAbstract = tf;
	}
	
	public boolean getAbstract() {
		return this.isAbstract;
	}
	
	public void setSwitchCaseList(List<SuperEntityClass> scl) {
		this.switchCaseList = scl;
	}
	
	public List<SuperEntityClass> getSwitchCaseList() {
		return this.switchCaseList;
	}
	
	public void addEntity(SuperEntityClass entity, EntityType ET) {
		if(ET == EntityType.ARRAY) {
			this.arrayList.add(entity);
		}
		else if(ET == EntityType.CATCH_CLAUSE) {
			this.catchClauseList.add(entity);
		}
		else if(ET == EntityType.CLASS) {
			this.classList.add(entity);
		}
		else if(ET == EntityType.CONDITIONAL_EXPRESSION) {
			this.conditionalExpressionList.add(entity);
		}
		else if(ET == EntityType.DO_STATEMENT) {
			this.doStatementList.add(entity);
		}
		else if(ET == EntityType.FOR_STATEMENT) {
			this.forStatementList.add(entity);
		}
		else if(ET == EntityType.GENERICS) {
			this.genericsList.add(entity);
		}
		else if(ET == EntityType.IF_STATEMENT) {
			this.ifStatementList.add(entity);
		}
		else if(ET == EntityType.INFIX_EXPRESSION) {
			this.infixExpressionList.add(entity);
		}
		else if(ET == EntityType.METHOD_INVOCATION) {
			this.methodInvocationList.add((MethodInvocationObject)entity);
		}
		else if(ET == EntityType.PRIMITIVE) {
			this.primitiveList.add(entity);
		}
		else if(ET == EntityType.SIMPLE) {
			this.simpleList.add(entity);
		}
		else if(ET == EntityType.SWITCH_STATEMENT) {
			this.switchCaseList.add(entity);
		}
		else if(ET == EntityType.THROW_STATEMENT) {
			this.throwStatementList.add(entity);
		}
		else if(ET == EntityType.TRY_STATEMENT) {
			this.tryStatementList.add(entity);
		}
		else if(ET == EntityType.WHILE_STATEMENT) {
			this.whileStatementList.add(entity);
		}
		else if(ET == EntityType.WILDCARD) {
			this.wildcardList.add(entity);
		}
	}
	
}