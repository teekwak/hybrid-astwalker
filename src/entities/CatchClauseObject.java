package entities;

public class CatchClauseObject extends SuperEntityClass {

	public CatchClauseObject() {
		
	}
	
	@Override
	public void printInfo() {
		System.out.println(this.name + " " + this.type.toString());
	}
	
}
