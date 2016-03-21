package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationObject extends SuperEntityClass {
	
	boolean isStatic;
	boolean isAbstract;
	List<String> thrownExceptions;
	List<Entity> entitiesInsideMethodDeclaration;
	List<?> parametersList;
	
	List<Entity> catchClauseList;
	List<Entity> classList;
	List<Entity> conditionalExpressionList;
	List<Entity> doStatementList;
	List<Entity> forStatementList;
	List<Entity> ifStatementList;
	List<Entity> infixExpressionList;
	List<Entity> methodInvocationList;
	List<Entity> switchStatementList;
	List<Entity> throwStatementList;
	List<Entity> tryStatementList;
	List<Entity> whileStatementList;
	
	public MethodDeclarationObject() {
		this.thrownExceptions = new ArrayList<>();
		this.entitiesInsideMethodDeclaration = new ArrayList<>();
		this.parametersList = new ArrayList<>();
		
		this.catchClauseList = new ArrayList<>();
		this.classList = new ArrayList<>();
		this.conditionalExpressionList = new ArrayList<>();
		this.doStatementList = new ArrayList<>();
		this.forStatementList = new ArrayList<>();
		this.ifStatementList = new ArrayList<>();
		this.infixExpressionList = new ArrayList<>();
		this.methodInvocationList = new ArrayList<>();
		this.switchStatementList = new ArrayList<>();
		this.throwStatementList = new ArrayList<>();
		this.tryStatementList = new ArrayList<>();
		this.whileStatementList = new ArrayList<>();
	}

	public void addChild(Entity e) {
		entitiesInsideMethodDeclaration.add(e);		
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
		
		System.out.println(s.toString());
		
		/*
		for(Entity e : entitiesInsideMethodDeclaration) {
			e.printInfo();
		}
		*/
		
		for(Entity e : primitiveList) {
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
	
	
	
	public void addCatchClause(Entity cco) {
		this.catchClauseList.add(cco);
	}
	
	public void addDoStatement(Entity dso) {
		this.doStatementList.add(dso);
	}
	
	public void addForStatement(Entity fso) {
		this.forStatementList.add(fso);
	}	
	
	public void addIfStatement(Entity iso) {
		this.ifStatementList.add(iso);
	}
	
	public void addMethodInvocation(Entity mio) {
		this.methodInvocationList.add(mio);
	}
	
	public void addThrowStatement(Entity tso) {
		this.throwStatementList.add(tso);
	}
	
	public void addTryStatement(Entity tso) {
		this.tryStatementList.add(tso);
	}
	
	public void addWhileStatement(Entity wso) {
		this.whileStatementList.add(wso);
	} 

}