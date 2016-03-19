package tools;

import entities.JavaFile;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import tools.ASTWalker;

public class FileModel {
	
	JavaFile javaFile;
	
	public FileModel() {
		
		this.javaFile = new JavaFile(); 
		
	}

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

				fileModel.javaFile.printAll();
				
			}
		}
	}
	
	public static void main(String[] args) throws IOException, CoreException {		
		File inputFolder = new File("/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/Example.java");
		
		traverseUntilJava(inputFolder);
	}
}
