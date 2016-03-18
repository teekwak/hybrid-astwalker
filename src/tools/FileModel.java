package tools;
import entities.Array__;
import entities.CatchClause__;
import entities.ClassContainer;
import entities.ConditionalExpression__;
import entities.DoStatement__;
import entities.ForStatement__;
import entities.Generics__;
import entities.IfStatement__;
import entities.Import__;
import entities.InfixExpression__;
import entities.Interface__;
import entities.MethodDeclarationContainer;
import entities.MethodInvocation__;
import entities.Package__;
import entities.Primitive__;
import entities.ReturnStatement__;
import entities.SimpleName__;
import entities.SwitchStatement__;
import entities.ThrowStatement__;
import entities.TryStatement__;
import entities.WhileStatement__;
import entities.Wildcard__;
import obsolete.Class__;
import obsolete.MethodDeclaration__;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import tools.ASTWalker;

public class FileModel {
	
	ClassContainer classContainer;
	MethodDeclarationContainer methodDeclarationContainer;
/*
	Array__ array__;
	CatchClause__ catchClause__;
	Class__ class__;
	ConditionalExpression__ conditionalExpression__;
	DoStatement__ doStatement__;
	ForStatement__ forStatement__;
	Generics__ generics__;
	IfStatement__ ifStatement__;
	Import__ import__;
	InfixExpression__ infixExpression__;
	Interface__ interface__;
	MethodDeclaration__ methodDeclaration__;
	MethodInvocation__ methodInvocation__;
	Package__ package__;
	Primitive__ primitive__;
	ReturnStatement__ returnStatement__;
	ThrowStatement__ throwStatement__;
	TryStatement__ tryStatement__;
	SimpleName__ simpleName__;
	SwitchStatement__ switchStatement__;
	WhileStatement__ whileStatement__;
	Wildcard__ wildcard__;
*/
	public FileModel() {
		
		this.classContainer = new ClassContainer();
		this.methodDeclarationContainer = new MethodDeclarationContainer();
		/*
		this.array__ = new Array__();
		this.catchClause__ = new CatchClause__();
		this.class__ = new Class__();
		this.conditionalExpression__ = new ConditionalExpression__();
		this.doStatement__ = new DoStatement__();
		this.forStatement__ = new ForStatement__();
		this.generics__ = new Generics__();
		this.ifStatement__ = new IfStatement__();
		this.import__ = new Import__();
		this.infixExpression__ = new InfixExpression__();
		this.interface__ = new Interface__();
		this.methodDeclaration__ = new MethodDeclaration__();
		this.methodInvocation__ = new MethodInvocation__();
		this.package__ = new Package__();
		this.primitive__ = new Primitive__();
		this.returnStatement__ = new ReturnStatement__();
		this.simpleName__ = new SimpleName__();
		this.switchStatement__ = new SwitchStatement__();
		this.throwStatement__ = new ThrowStatement__();
		this.tryStatement__ = new TryStatement__();
		this.whileStatement__ = new WhileStatement__();
		this.wildcard__ = new Wildcard__();
		*/
	}
/*
	public Array__ getArrays() {
		return this.array__;
	}
	
	public CatchClause__ getCatchClauses() {
		return this.catchClause__;
	}

	public Class__ getClasses() {
		return this.class__;
	}	
	
	public ConditionalExpression__ getConditionalExpressions() {
		return this.conditionalExpression__;
	}
	
	public DoStatement__ getDoStatements() {
		return this.doStatement__;
	}
	
	public ForStatement__ getForStatements() {
		return this.forStatement__;
	}
	
	public Generics__ getGenerics() {
		return this.generics__;
	}
	
	public IfStatement__ getIfStatements() {
		return this.ifStatement__;
	}
	
	public Import__ getImports() {
		return this.import__;
	}
	
	public InfixExpression__ getInfixExpressions() {
		return this.infixExpression__;
	} 
	
	public Interface__ getInterfaces() {
		return this.interface__;
	}
	
	public MethodDeclaration__ getMethodDeclarations() {
		return this.methodDeclaration__;
	}
	
	public MethodInvocation__ getMethodInvocations() {
		return this.methodInvocation__;
	}
	
	public Package__ getPackages() {
		return this.package__;
	}
	
	public Primitive__ getPrimitives() {
		return this.primitive__;
	}
	
	public ReturnStatement__ getReturnStatements() {
		return this.returnStatement__;
	}
	
	public SimpleName__ getSimpleNames() {
		return this.simpleName__;
	}
	
	public SwitchStatement__ getSwitchStatements() {
		return this.switchStatement__;
	}

	public ThrowStatement__ getThrowStatements() {
		return this.throwStatement__;
	}
	
	public TryStatement__ getTryStatements() {
		return this.tryStatement__;
	}
	
	public WhileStatement__ getWhileStatements() {
		return this.whileStatement__;
	}
	
	public Wildcard__ getWildcards() {
		return this.wildcard__;
	}
	
	public void printEverything() {
		this.array__.printAllArrays();
		this.catchClause__.printAllCatchClauses();
		this.class__.printAllClasses();
		this.class__.printAllExtends();
		this.conditionalExpression__.printAllConditionalExpressions();
		this.doStatement__.printAllDoStatements();
		this.forStatement__.printAllForStatements();
		this.generics__.printAllGenerics();
		this.import__.printAllImports();
		this.ifStatement__.printAllIfStatements();
		this.infixExpression__.printAllInfixExpressions();
		this.interface__.printAllImplements();
		this.interface__.printAllInterfaces();
		this.methodDeclaration__.printAllMethodDeclarations();
		this.methodInvocation__.printAllMethodInvocations();
		this.package__.printAllPackages();
		this.primitive__.printAllPrimitives();
		this.returnStatement__.printAllReturnStatements();
		this.simpleName__.printAllSimpleNames();
		this.switchStatement__.printAllSwitchStatements();
		this.throwStatement__.printAllThrowStatements();
		this.tryStatement__.printAllTryStatements();
		this.whileStatement__.printAllWhileStatements();
		this.wildcard__.printAllWildcards();
	}
*/	
	
	public FileModel parseDeclarations(String fileLocation) throws IOException, CoreException {
		ASTWalker astWalker = new ASTWalker();
		return astWalker.parseFile(fileLocation);
	}
	
	public static void traverseUntilJava(File parentNode) throws IOException, CoreException {
		if(parentNode.isDirectory()) {
			File childNodes[] = parentNode.listFiles();
						
			for(File c : childNodes) {
				if(!c.getName().startsWith(".")) {
					traverseUntilJava(c);
				}
			}
		}
		else {
			if(parentNode.getName().endsWith(".java")) {						
				FileModel fileModel = new FileModel();
				
				fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
								
				fileModel.classContainer.printAll();
				
				System.out.println("-------------------------");
				
				fileModel.methodDeclarationContainer.printAll();
								
			}
		}
	}
	
	public static void main(String[] args) throws IOException, CoreException {		
		File inputFolder = new File("/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/Example.java");
		
		traverseUntilJava(inputFolder);
	}
}
