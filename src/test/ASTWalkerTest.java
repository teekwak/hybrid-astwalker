package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import tools.FileModel;

/**
 * 
 * ASTWalker JUnit test against src/exampleCode/Example.java
 * 
 * @author Thomas Kwak
 *
 */

public class ASTWalkerTest {

	private static FileModel fileModel;

	public ASTWalkerTest() throws IOException, CoreException {		
		String fileLocation = "src/exampleCode/Example.java";
		
		fileModel = new FileModel();
		
		if(fileLocation.substring(fileLocation.lastIndexOf(".")+1).equals("java")) {
			fileModel = fileModel.parseDeclarations(fileLocation);
		}
	}
	
	@Test
	// check number of packages
	public void packageCount() {
		assertEquals(1, fileModel.getPackages().getPackageObjectList().size());
	}

	@Test
	// check number of imports
	public void importCount() {
		assertEquals(6, fileModel.getImports().getImportObjectList().size());
	}
	
	@Test
	// check number of classes (regular + super)
	public void classCount() {
		int normalClassCount = fileModel.getClasses().getClassObjectList().size();
		int superClassCount = fileModel.getClasses().getSuperClassObjectList().size();
		assertEquals(5, normalClassCount + superClassCount);
	}
}
