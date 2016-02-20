import entities.ArrayNames;
import entities.CatchClauseNames;
import entities.ClassNames;
import entities.ConditionalExpressionExpressions;
import entities.DoStatementExpressions;
import entities.ForStatementExpressions;
import entities.GenericsNames;
import entities.IfStatementExpressions;
import entities.ImportNames;
import entities.InfixExpressionExpressions;
import entities.InterfaceNames;
import entities.MethodNames;
import entities.PackageNames;
import entities.PrimitiveNames;
import entities.SimpleNames;
import entities.SwitchStatementNames;
import entities.ThrowStatementNames;
import entities.TryStatementNames;
import entities.WhileStatementExpressions;
import entities.WildcardNames;

import java.io.IOException;

public class FileModel {
	
	ClassNames classNames;
	PrimitiveNames primitiveNames;
	InterfaceNames interfaceNames;
	PackageNames packageNames;
	CatchClauseNames catchClauseNames;
	ImportNames importNames;
	MethodNames methodNames;
	ArrayNames arrayNames;
	GenericsNames genericsNames;
	WhileStatementExpressions whileStatementExpressions;
	DoStatementExpressions doStatementExpressions;
	ForStatementExpressions forStatementExpressions;
	IfStatementExpressions ifStatementExpressions;
	InfixExpressionExpressions infixExpressionExpressions;
	ThrowStatementNames throwStatementNames;
	TryStatementNames tryStatementNames;
	ConditionalExpressionExpressions conditionalExpressionExpressions;
	SwitchStatementNames switchStatementNames;
	WildcardNames wildcardNames;
	SimpleNames simpleNames;
	
	public FileModel() {
		this.classNames = new ClassNames();
		this.primitiveNames = new PrimitiveNames();
		this.interfaceNames = new InterfaceNames();
		this.packageNames = new PackageNames();
		this.catchClauseNames = new CatchClauseNames();
		this.importNames = new ImportNames();
		this.methodNames = new MethodNames();
		this.arrayNames = new ArrayNames();
		this.genericsNames = new GenericsNames();
		this.whileStatementExpressions = new WhileStatementExpressions();
		this.doStatementExpressions = new DoStatementExpressions();
		this.forStatementExpressions = new ForStatementExpressions();
		this.ifStatementExpressions = new IfStatementExpressions();
		this.infixExpressionExpressions = new InfixExpressionExpressions();
		this.throwStatementNames = new ThrowStatementNames();
		this.tryStatementNames = new TryStatementNames();
		this.conditionalExpressionExpressions = new ConditionalExpressionExpressions();
		this.switchStatementNames = new SwitchStatementNames();
		this.wildcardNames = new WildcardNames();
		this.simpleNames = new SimpleNames();
	}
	
	public FileModel parseDeclarations(String fileLocation) throws IOException {
		ASTWalker astWalker = new ASTWalker();
		return astWalker.parseFile(fileLocation);
	}
	
	public void printDeclarationsToFile() {
		
	}
	
	public static void main(String[] args) throws IOException {
		String fileLocation = "/Users/Kwak/Documents/workspace/ASTTest/src/exampleCode/Example.java";
		
		FileModel fileModel = new FileModel();		

		fileModel = fileModel.parseDeclarations(fileLocation);	

		/*
		
		fileModel.packageNames.printAllPackages();

		fileModel.importNames.printAllImports();

		fileModel.classNames.printAllClasses();

		fileModel.classNames.printAllExtends();
		
		fileModel.interfaceNames.printAllImplements();
		
		fileModel.interfaceNames.printAllInterfaces();
		
		fileModel.primitiveNames.printAllPrimitives();
		
		fileModel.arrayNames.printAllArrays();
		
		fileModel.genericsNames.printAllGenerics();
		
		fileModel.whileStatementExpressions.printAllWhileStatements();
		
		fileModel.catchClauseNames.printAllCatchClauses();
		
		fileModel.doStatementExpressions.printAllDoStatements();
		
		fileModel.forStatementExpressions.printAllForStatements();
	
		fileModel.ifStatementExpressions.printAllIfStatements();
		
		fileModel.infixExpressionExpressions.printAllInfixExpressions();
		
		fileModel.throwStatementNames.printAllThrowStatements();
		
		fileModel.tryStatementNames.printAllTryStatements();
		
		fileModel.conditionalExpressionExpressions.printAllConditionalExpressions();

		fileModel.methodNames.printAllMethods();
		
		fileModel.switchStatementNames.printAllSwitchStatements();


		*/
		
		fileModel.wildcardNames.printAllWildcards();
		
		fileModel.simpleNames.printAllSimpleNames();
		
	}
}
