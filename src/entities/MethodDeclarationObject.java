package entities;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public class MethodDeclarationObject implements Entity {

	String name;
	int lineNumber;
	int columnNumber;
	List<Entity> entitiesInsideClass;
	
	public MethodDeclarationObject() {
		this.entitiesInsideClass = new ArrayList<>();
	}
	
	public MethodDeclarationObject(String string, String string2, String name, Type returnType2, boolean varargs,
			boolean constructor, boolean isStatic, boolean isAbstract, List<Object> parameters, int lineNumber,
			int columnNumber) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addChild(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
}