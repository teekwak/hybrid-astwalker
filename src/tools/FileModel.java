package tools;

import entities.JavaClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import tools.ASTWalker;

public class FileModel {
	
	List<JavaClass> javaClassList;
	
	public void printAll() {
		for(JavaClass co : javaClassList) {		
			co.printInfo();
		}
	}
	
	public void addJavaClass(JavaClass co) {
		javaClassList.add(co);
	}
		
	public FileModel() {
		this.javaClassList = new ArrayList<>();
	}

	public List<JavaClass> getJavaClassList() {
		return this.javaClassList;
	}
	
	public FileModel parseDeclarations(String fileLocation) throws IOException, CoreException {
		ASTWalker astWalker = new ASTWalker();
		return astWalker.parseFile(fileLocation);
	}

}
