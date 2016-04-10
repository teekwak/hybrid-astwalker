package entities;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationObject extends SuperEntityClass {
	
	List<Entity> entityList;
	
	boolean isAbstract;
	boolean isConstructor;
	boolean isGeneric;
	boolean isStatic;
	boolean isVarargs;
		
	String declaringClass;
	String wildcardBound;
	String returnType;
	List<String> thrownExceptions;
	List<?> parametersList;
	List<String> parameterTypesList;
	List<String> genericParametersList;
	
	List<SuperEntityClass> arrayList;
	List<SuperEntityClass> catchClauseList;
	List<SuperEntityClass> classList;
	List<SuperEntityClass> conditionalExpressionList;
	List<SuperEntityClass> doStatementList;
	List<SuperEntityClass> forStatementList;
	List<SuperEntityClass> genericsList;
	List<SuperEntityClass> ifStatementList;
	List<SuperEntityClass> infixExpressionList;
	List<SuperEntityClass> methodDeclarationList;
	List<SuperEntityClass> methodInvocationList;
	List<SuperEntityClass> primitiveList;
	List<SuperEntityClass> simpleList;
	List<SuperEntityClass> switchCaseList;
	List<SuperEntityClass> switchStatementList;
	List<SuperEntityClass> throwStatementList;
	List<SuperEntityClass> tryStatementList;
	List<SuperEntityClass> unionList;
	List<SuperEntityClass> whileStatementList;
	List<SuperEntityClass> wildcardList;
	
	public MethodDeclarationObject() {
		this.entityList = new ArrayList<>();
		
		this.thrownExceptions = new ArrayList<>();
		this.parametersList = new ArrayList<>();
		this.parameterTypesList = new ArrayList<>();
		this.genericParametersList = new ArrayList<>();
				
		this.arrayList = new ArrayList<>();
		this.catchClauseList = new ArrayList<>();
		this.classList = new ArrayList<>();
		this.conditionalExpressionList = new ArrayList<>();
		this.doStatementList = new ArrayList<>();
		this.forStatementList = new ArrayList<>();
		this.genericsList = new ArrayList<>();
		this.ifStatementList = new ArrayList<>();
		this.infixExpressionList = new ArrayList<>();
		this.methodDeclarationList = new ArrayList<>();
		this.methodInvocationList = new ArrayList<>();
		this.primitiveList = new ArrayList<>();
		this.simpleList = new ArrayList<>();
		this.switchCaseList = new ArrayList<>();
		this.switchStatementList = new ArrayList<>();
		this.throwStatementList = new ArrayList<>();
		this.tryStatementList = new ArrayList<>();
		this.unionList = new ArrayList<>();
		this.whileStatementList = new ArrayList<>();
		this.wildcardList = new ArrayList<>();
	}

	public void setGenericParametersList(List<String> list) {
		this.genericParametersList = list;
	}
	
	public List<String> getGenericParametersList() {
		return this.genericParametersList;
	}
	
	public void setParameterTypesList(List<String> list) {
		this.parameterTypesList = list;
	}
	
	public List<String> getParameterTypesList() {
		return this.parameterTypesList;
	}
	
	public void setDeclaringClass(String s) {
		this.declaringClass = s;
	}
	
	public String getDeclaringClass() {
		return this.declaringClass;
	}
	
	public void setBound(String s) {
		this.wildcardBound = s;
	}
	
	public String getBound() {
		return this.wildcardBound;
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
	
	public void setIsVarargs(boolean iv) {
		this.isVarargs = iv;
	}
	
	public boolean getIsVarargs() {
		return this.isVarargs;
	}
	
	public void setIsConstructor(boolean ic) {
		this.isConstructor = ic;
	}
	
	public boolean getIsConstructor() {
		return this.isConstructor;
	}
	
	public void setIsStatic(boolean tf) {
		this.isStatic = tf;
	}
	
	public boolean getIsStatic() {
		return this.isStatic;
	}
	
	public void setIsAbstract(boolean tf) {
		this.isAbstract = tf;
	}
	
	public boolean getIsAbstract() {
		return this.isAbstract;
	}
	
	public void setIsGenericType(boolean g) {
		this.isGeneric = g;
	}
	
	public boolean getIsGenericType() {
		return this.isGeneric;
	}
	
	public void setReturnType(String r) {
		this.returnType = r;
	}
	
	public String getReturnType() {
		return this.returnType;
	}
	
	public void setSwitchCaseList(List<SuperEntityClass> scl) {
		this.switchCaseList = scl;
	}
	
	public List<SuperEntityClass> getArrayList() {
		return this.arrayList;
	}
	
	public List<SuperEntityClass> getCatchClauseList() {
		return this.catchClauseList;
	}
	
	public List<SuperEntityClass> getClassList() {
		return this.classList;
	}
	
	public List<SuperEntityClass> getConditionalExpressionList() {
		return this.conditionalExpressionList;
	}
	
	public List<SuperEntityClass> getDoStatementList() {
		return this.doStatementList;
	}
	
	public List<SuperEntityClass> getForStatementList() {
		return this.forStatementList;
	}
	
	public List<SuperEntityClass> getGenericsList() {
		return this.genericsList;
	}
	
	public List<SuperEntityClass> getIfStatementList() {
		return this.ifStatementList;
	}
	
	public List<SuperEntityClass> getInfixExpressionList() {
		return this.infixExpressionList;
	}
	
	public List<SuperEntityClass> getMethodDeclarationList() {
		return this.methodDeclarationList;
	}
	
	public List<SuperEntityClass> getMethodInvocationList() {
		return this.methodInvocationList;
	}
	
	public List<SuperEntityClass> getPrimitiveList() {
		return this.primitiveList;
	}
	
	public List<SuperEntityClass> getSimpleList() {
		return this.simpleList;
	}
	
	public List<SuperEntityClass> getSwitchCaseList() {
		return this.switchCaseList;
	}
	
	public List<SuperEntityClass> getSwitchStatementList() {
		return this.switchStatementList;
	}
	
	public List<SuperEntityClass> getThrowStatementList() {
		return this.throwStatementList;
	}
	
	public List<SuperEntityClass> getTryStatementList() {
		return this.tryStatementList;
	}
	
	public List<SuperEntityClass> getUnionList() {
		return this.unionList;
	}
	
	public List<SuperEntityClass> getWhileStatementList() {
		return this.whileStatementList;
	}
	
	public List<SuperEntityClass> getWildcardList() {
		return this.wildcardList;
	}
	
	public void addEntity(SuperEntityClass entity, EntityType ET) {
		this.entityList.add(entity);
		
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
		else if(ET == EntityType.METHOD_DECLARATION) {
			this.methodDeclarationList.add(entity);
		}
		else if(ET == EntityType.METHOD_INVOCATION) {
			this.methodInvocationList.add(entity);
		}
		else if(ET == EntityType.PRIMITIVE) {
			this.primitiveList.add(entity);
		}
		else if(ET == EntityType.SIMPLE) {
			this.simpleList.add(entity);
		}
		else if(ET == EntityType.SWITCH_STATEMENT) {
			this.switchStatementList.add(entity);
		}
		else if(ET == EntityType.THROW_STATEMENT) {
			this.throwStatementList.add(entity);
		}
		else if(ET == EntityType.TRY_STATEMENT) {
			this.tryStatementList.add(entity);
		}
		else if(ET == EntityType.UNION) {
			this.unionList.add(entity);
		}
		else if(ET == EntityType.WHILE_STATEMENT) {
			this.whileStatementList.add(entity);
		}
		else if(ET == EntityType.WILDCARD) {
			this.wildcardList.add(entity);
		}
	}
	
	public void addEntities(List<SuperEntityClass> listOfEntities, EntityType ET) {
		if(ET == EntityType.SWITCH_CASE) {
			this.switchCaseList.addAll(listOfEntities);
		}
	}

	public void setComplexities() {
		int cycloCount = 0;
		int methodInvCount = this.methodInvocationList.size();
		
		for(SuperEntityClass e : infixExpressionList) {
			if(e.getName().equals("&&") || e.getName().equals("||")) {
				cycloCount++;
			}
		}
		
		for(SuperEntityClass e : switchStatementList) {
			for(Entity f : e.getEntityList()) {
				if(!f.getName().equals("Default")) {
					cycloCount++;
				}
			}
		}
				
		for(SuperEntityClass cl : this.getClassList()) {
			cycloCount += ((JavaClass) cl).getCyclomaticComplexity();
			methodInvCount += ((JavaClass) cl).getTotalMethodInvocationCount();
		}
		
		for(SuperEntityClass mdo : this.getMethodDeclarationList()) {
			cycloCount += ((MethodDeclarationObject) mdo).getCyclomaticComplexity();
			methodInvCount += ((MethodDeclarationObject) mdo).getTotalMethodInvocationCount();
		}
		
		// cyclomatic complexity = 1 + if + for + while + switch case + catch + and + or + ternary + inner cyclomatic complexities
		this.cyclomaticComplexity = 1 + ifStatementList.size() + forStatementList.size() + whileStatementList.size() 
		+ catchClauseList.size() + conditionalExpressionList.size() + cycloCount;	
		this.totalMethodInvocationCount = methodInvCount;
	}
	
	public int getCyclomaticComplexity() {
		return this.cyclomaticComplexity;
	}
	
	public int getTotalMethodInvocationCount() {
		return this.totalMethodInvocationCount;
	}
}