import entities.CatchClauseNames;
import entities.ClassNames;
import entities.ImportNames;
import entities.InterfaceNames;
import entities.MethodNames;
import entities.PackageNames;
import entities.PrimitiveNames;

import java.io.IOException;

public class FileModel {
	
	ClassNames classNames;
	PrimitiveNames primitiveNames;
	InterfaceNames interfaceNames;
	PackageNames packageNames;
	CatchClauseNames catchClauseNames;
	ImportNames importNames;
	MethodNames methodNames;
	
	public FileModel() {
		this.classNames = new ClassNames();
		this.primitiveNames = new PrimitiveNames();
		this.interfaceNames = new InterfaceNames();
		this.packageNames = new PackageNames();
		this.catchClauseNames = new CatchClauseNames();
		this.importNames = new ImportNames();
		this.methodNames = new MethodNames();
	}
	
	public FileModel parseDeclarations(String fileLocation) throws IOException {
		ASTWalker astWalker = new ASTWalker();
		return astWalker.parseFile(fileLocation); hello
	}
	
	public void printDeclarationsToFile() {
		
	}
	
	public static void main(String[] args) throws IOException {
		String fileLocation = "/Users/Kwak/Documents/workspace/ASTTest/src/exampleCode/Example.java";
		
		FileModel fileModel = new FileModel();
		
		fileModel = fileModel.parseDeclarations(fileLocation);	
		
		/* works
		fileModel.packageNames.printPackages();

		fileModel.importNames.printImports();

		fileModel.classNames.printClasses();

		fileModel.classNames.printExtends();
		
		fileModel.interfaceNames.printImplements();
		
		fileModel.interfaceNames.printInterfaces();
		*/
		
		fileModel.methodNames.printMethods();
				
	}
}
