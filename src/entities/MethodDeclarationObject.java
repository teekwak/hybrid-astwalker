package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationObject extends SuperEntityClass {
	
	boolean isStatic;
	boolean isAbstract;
	List<String> thrownExceptions;
	List<Entity> entitiesInsideMethodDeclaration;
	List<?> parametersList;
	
	List<Entity> arrayList;
	List<Entity> catchClauseList;
	List<Entity> conditionalExpressionList;
	List<Entity> doStatementList;
	List<Entity> forStatementList;
	List<Entity> genericsList;
	
	
	public MethodDeclarationObject() {
		this.thrownExceptions = new ArrayList<>();
		this.entitiesInsideMethodDeclaration = new ArrayList<>();
		this.parametersList = new ArrayList<>();
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
		
		for(Entity e : entitiesInsideMethodDeclaration) {
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
}