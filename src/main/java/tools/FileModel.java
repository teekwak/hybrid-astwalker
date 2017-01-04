package tools;

import entities.JavaClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

public class FileModel {

	private List<JavaClass> javaClassList;

	public FileModel() {
		this.javaClassList = new ArrayList<>();
	}

	public void addJavaClass(JavaClass co) {
		javaClassList.add(co);
	}

	public List<JavaClass> getJavaClassList() {
		return this.javaClassList;
	}
	
//	FileModel parseDeclarations(String fileLocation) throws IOException, CoreException {
//		ASTWalker astWalker = new ASTWalker();
//		return astWalker.parseFile(fileLocation);
//	}

}
