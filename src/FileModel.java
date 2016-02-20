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
	
	ArrayNames arrayNames;
	CatchClauseNames catchClauseNames;
	ClassNames classNames;
	ConditionalExpressionExpressions conditionalExpressionExpressions;
	DoStatementExpressions doStatementExpressions;
	ForStatementExpressions forStatementExpressions;
	GenericsNames genericsNames;
	IfStatementExpressions ifStatementExpressions;
	ImportNames importNames;
	InfixExpressionExpressions infixExpressionExpressions;
	InterfaceNames interfaceNames;
	MethodNames methodNames;
	PackageNames packageNames;
	PrimitiveNames primitiveNames;
	ThrowStatementNames throwStatementNames;
	TryStatementNames tryStatementNames;
	SimpleNames simpleNames;
	SwitchStatementNames switchStatementNames;
	WhileStatementExpressions whileStatementExpressions;
	WildcardNames wildcardNames;
	
	public FileModel() {
		this.arrayNames = new ArrayNames();
		this.catchClauseNames = new CatchClauseNames();
		this.classNames = new ClassNames();
		this.conditionalExpressionExpressions = new ConditionalExpressionExpressions();
		this.doStatementExpressions = new DoStatementExpressions();
		this.forStatementExpressions = new ForStatementExpressions();
		this.genericsNames = new GenericsNames();
		this.ifStatementExpressions = new IfStatementExpressions();
		this.infixExpressionExpressions = new InfixExpressionExpressions();
		this.interfaceNames = new InterfaceNames();
		this.importNames = new ImportNames();
		this.methodNames = new MethodNames();
		this.packageNames = new PackageNames();
		this.primitiveNames = new PrimitiveNames();
		this.simpleNames = new SimpleNames();
		this.switchStatementNames = new SwitchStatementNames();
		this.throwStatementNames = new ThrowStatementNames();
		this.tryStatementNames = new TryStatementNames();
		this.whileStatementExpressions = new WhileStatementExpressions();
		this.wildcardNames = new WildcardNames();
	}
	
	public FileModel parseDeclarations(String fileLocation) throws IOException {
		ASTWalker astWalker = new ASTWalker();
		return astWalker.parseFile(fileLocation);
	}
	
	public void printEverything() {
		this.arrayNames.printAllArrays();
		this.catchClauseNames.printAllCatchClauses();
		this.classNames.printAllClasses();
		this.classNames.printAllExtends();
		this.conditionalExpressionExpressions.printAllConditionalExpressions();
		this.doStatementExpressions.printAllDoStatements();
		this.forStatementExpressions.printAllForStatements();
		this.genericsNames.printAllGenerics();
		this.importNames.printAllImports();
		this.ifStatementExpressions.printAllIfStatements();
		this.infixExpressionExpressions.printAllInfixExpressions();
		this.interfaceNames.printAllImplements();	
		this.interfaceNames.printAllInterfaces();
		this.methodNames.printAllMethods();
		this.packageNames.printAllPackages();
		this.primitiveNames.printAllPrimitives();
		this.simpleNames.printAllSimpleNames();	
		this.switchStatementNames.printAllSwitchStatements();
		this.throwStatementNames.printAllThrowStatements();
		this.tryStatementNames.printAllTryStatements();		
		this.whileStatementExpressions.printAllWhileStatements();
		this.wildcardNames.printAllWildcards();	
	}
	
	public void printDeclarationsToFile() {

	}
	
	public static void main(String[] args) throws IOException {
		String fileLocation = "/Users/Kwak/Documents/workspace/ASTTest/src/exampleCode/Example.java";
		
		FileModel fileModel = new FileModel();		

		fileModel = fileModel.parseDeclarations(fileLocation);	

		fileModel.printEverything();
	}
}
