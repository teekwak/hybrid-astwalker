package entities;

import java.util.List;

import org.eclipse.jdt.core.dom.Type;

public interface Entity {
		
	Type t = null;
	String SolrKey = null;
	Object SolrValue = null;
	
	public void addChild(Entity e);
	public void printInfo();
	
	public void setName(String n);
	public String getName();
	
	public void setLineNumber(int n);
	public int getLineNumber();
	
	public void setColumnNumber(int n);
	public int getColumnNumber();
	
}
