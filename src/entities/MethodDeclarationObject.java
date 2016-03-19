package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class MethodDeclarationObject extends SuperEntityClass {

	boolean isStatic;
	boolean isAbstract;
	List<String> thrownExceptions;
	List<Entity> entitiesInsideMethodDeclaration;
	
	public MethodDeclarationObject() {
		this.thrownExceptions = new ArrayList<>();
		this.entitiesInsideMethodDeclaration = new ArrayList<>();
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