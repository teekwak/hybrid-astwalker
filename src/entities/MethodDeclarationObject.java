package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class MethodDeclarationObject extends SuperEntityClass {

	List<String> thrownExceptions;
	List<Entity> entitiesInsideMethodDeclaration;
	
	public MethodDeclarationObject() {
		this.thrownExceptions = new ArrayList<>();
		this.entitiesInsideMethodDeclaration = new ArrayList<>();
	}

	@Override
	public void addChild(Entity e) {
		entitiesInsideMethodDeclaration.add(e);		
	}

	@Override
	public void printInfo() {
		StringBuilder s = new StringBuilder();
		s.append(this.getName());
				
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

	@Override
	public void setSuperClass(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSuperClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addImplementsInterface(String s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getImplements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addThrowsException(String s) {
		this.thrownExceptions.add(s);
	}

	@Override
	public List<String> getThrowsException() {
		return this.thrownExceptions;
	}
}