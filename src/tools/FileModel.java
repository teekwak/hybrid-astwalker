package tools;
import entities.Array__;
import entities.CatchClause__;
import entities.Class__;
import entities.ConditionalExpression__;
import entities.DoStatement__;
import entities.ForStatement__;
import entities.Generics__;
import entities.IfStatement__;
import entities.Import__;
import entities.InfixExpression__;
import entities.Interface__;
import entities.MethodDeclaration__;
import entities.MethodInvocation__;
import entities.Package__;
import entities.Primitive__;
import entities.SimpleName__;
import entities.SwitchStatement__;
import entities.ThrowStatement__;
import entities.TryStatement__;
import entities.WhileStatement__;
import entities.Wildcard__;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

public class FileModel {

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
	ThrowStatement__ throwStatement__;
	TryStatement__ tryStatement__;
	SimpleName__ simpleName__;
	SwitchStatement__ switchStatement__;
	WhileStatement__ whileStatement__;
	Wildcard__ wildcard__;

	public FileModel() {
		this.array__ = new Array__();
		this.catchClause__ = new CatchClause__();
		this.class__ = new Class__();
		this.conditionalExpression__ = new ConditionalExpression__();
		this.doStatement__ = new DoStatement__();
		this.forStatement__ = new ForStatement__();
		this.generics__ = new Generics__();
		this.ifStatement__ = new IfStatement__();
		this.infixExpression__ = new InfixExpression__();
		this.interface__ = new Interface__();
		this.import__ = new Import__();
		this.methodDeclaration__ = new MethodDeclaration__();
		this.methodInvocation__ = new MethodInvocation__();
		this.package__ = new Package__();
		this.primitive__ = new Primitive__();
		this.simpleName__ = new SimpleName__();
		this.switchStatement__ = new SwitchStatement__();
		this.throwStatement__ = new ThrowStatement__();
		this.tryStatement__ = new TryStatement__();
		this.whileStatement__ = new WhileStatement__();
		this.wildcard__ = new Wildcard__();
	}

	public FileModel parseDeclarations(String fileLocation) throws IOException, CoreException {
		ASTWalker astWalker = new ASTWalker();
		return astWalker.parseFile(fileLocation);
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
		this.simpleName__.printAllSimpleNames();
		this.switchStatement__.printAllSwitchStatements();
		this.throwStatement__.printAllThrowStatements();
		this.tryStatement__.printAllTryStatements();
		this.whileStatement__.printAllWhileStatements();
		this.wildcard__.printAllWildcards();
	}

	public void printToFile() {

	}

	public static void main(String[] args) throws IOException, CoreException {

		String fileLocation = "src/exampleCode/Example.java";
				
		if(fileLocation.substring(fileLocation.lastIndexOf(".")+1).equals("java")) {
			FileModel fileModel = new FileModel();

			fileModel = fileModel.parseDeclarations(fileLocation);

			fileModel.printEverything();
		}
	}
}
