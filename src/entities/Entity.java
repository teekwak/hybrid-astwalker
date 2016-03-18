package entities;

import org.eclipse.jdt.core.dom.Type;

public interface Entity {
		
	Type t = null;
	String SolrKey = null;
	Object SolrValue = null;
	
	public void addChild(Entity e);
	public String getName();
	public void setName(String n);
}
