package tools;

import entities.ClassObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import tools.ASTWalker;

public class FileModel {
	
	List<ClassObject> classObjectList;
	
	public void printAll() {
		for(ClassObject co : classObjectList) {		
			co.printInfo();
		}
	}
	
	public void addClass(ClassObject co) {
		classObjectList.add(co);
	}
		
	public FileModel() {
		this.classObjectList = new ArrayList<>();
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

				fileModel.printAll();
				
			}
		}
	}
	
	public static void main(String[] args) throws IOException, CoreException {		
		File inputFolder = new File("/home/kwak/Documents/workspace/ASTWalker/src/exampleCode/Example.java");
		
		traverseUntilJava(inputFolder);
	}
}
