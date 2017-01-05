package tools;

import entities.JavaClass;

import java.util.ArrayList;
import java.util.List;

public class FileModel {

	private List<JavaClass> javaClassList;

	public FileModel() {
		this.javaClassList = new ArrayList<>();
	}

	public void addJavaClass(JavaClass co) {
		javaClassList.add(co);
	}

	List<JavaClass> getJavaClassList() {
		return this.javaClassList;
	}

}
