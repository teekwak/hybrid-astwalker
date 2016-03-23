package tools;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;

import entities.JavaClass;
import entities.MethodDeclarationObject;
import entities.MethodInvocationObject;
import entities.SuperEntityClass;
import tools.FileModel;
import tools.GitData;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class IndexManager {
	
	public static List<FileModel> fileModelList = new ArrayList<>();
	public static List<GitData> gitDataList = new ArrayList<>();
	
	public static final String SNIPPET_CODE = "snippet_code";
	public static final String SNIPPET_ADDRESS_LOWER_BOUND = "snippet_address_lower_bound";
	public static final String SNIPPET_ADDRESS_UPPER_BOUND ="snippet_address_upper_bound";
	public static final String SNIPPET_IS_INNERCLASS = "snippet_is_innerClass";
	public static final String SNIPPET_IMPORTS = "";
	public static final String SNIPPET_IMPORTS_SHORT = "";
	public static final String SNIPPET_IMPORTS_COUNT = "";
	public static final String SNIPPET_IMPLEMENTS = "";
	public static final String SNIPPET_IMPLEMENTS_SHORT = "";
	
	public static void traverseUntilJava(File parentNode, String topDirectoryLocation) throws IOException, CoreException, NoHeadException, GitAPIException, ParseException {
		if(parentNode.isDirectory()) {
			File childNodes[] = parentNode.listFiles();
						
			for(File c : childNodes) {
				if(!c.getName().startsWith(".")) {
					traverseUntilJava(c, topDirectoryLocation);
				}
			}
		}
		else {
			/*
			if(parentNode.getName().endsWith(".java")) {	
				runASTandGitData(parentNode, topDirectoryLocation);
			}
			*/
			if(parentNode.getName().equals("JabberConnectionDebugger.java")) {
			//if(parentNode.getName().equals("java1.java")) {
				runASTandGitData(parentNode, topDirectoryLocation);
			}
		}
	}
	
	/*
	 * fileModel holds all AST information
	 * 
	 * gitData.javaFileList has a list of Java file objects with data on each commit
	 * gitData.getCommitDataPerFile() creates commit objects
	 * gitData.addHashCodePairsToMap() populates hash codes
	 * gitData.getDiff() gets insertions and deletions for all commits
	 * 
	 * to get the insertions/deletions at each commit, stored in CommitData object
	 * 
	 * for(JavaFile j : gitData.javaFileList) {
	 *     for(CommitData c : j.commitDataList) {
	 *         System.out.println(j.name + " +" + c.insertions + " -" + c.deletions);
	 *     }
	 * }
	 * 
	 */
	public static void runASTandGitData(File parentNode, String topDirectoryLocation) throws IOException, CoreException, NoHeadException, GitAPIException, ParseException {
		FileModel fileModel = new FileModel();
		fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
		//fileModel.printAll();
		fileModelList.add(fileModel);
		
		GitData gitData = new GitData();
		Git git = Git.open( new File (topDirectoryLocation + ".git") );
		
		List<RevCommit> commitHistory = new ArrayList<>();
		for(RevCommit commit : git.log().call()) {
			commitHistory.add(commit);
		}
		Collections.reverse(commitHistory);
		
		gitData.getCommitDataPerFile(topDirectoryLocation, parentNode.getAbsolutePath());
		gitData.addHashCodePairsToMap(commitHistory);
		gitData.getDiff(topDirectoryLocation, gitData.hashCodePairs);
		
		gitDataList.add(gitData);
		
		git.close();
		/*
		for(JavaFile j : gitData.javaFileList) {
			for(CommitData cd : j.commitDataList) {
				System.out.println(cd.solrDate);
			}
		}
		*/
	}
	
	// gets all classes, method declarations, and method invocation from each FileModel
	public static void getFileModelConstructs(/*File file*/) {
		for(FileModel f : fileModelList) {
			for(JavaClass jc : f.getJavaClassList()) {
				JavaFile jf = null;
				
				// match JavaFile with JavaClass
				for(GitData g : gitDataList) {
					for(JavaFile temp : g.getJavaFileList()) {
						if(temp.getFileLocation().equals(jc.getFileName())) {
							jf = temp;
						}
					}
				}
				
				makeClassSolrDoc(jc, jf);
				
				for(SuperEntityClass md : jc.getMethodDeclarationList()) {
					findAllMethodDeclarations((MethodDeclarationObject)md);
				}
			}
		}
	}
	
	// recursively get method declarations (for those method declarations inside of each other)
	public static void findAllMethodDeclarations(MethodDeclarationObject mdo) {
		makeSolrDoc(mdo);
		
		for(SuperEntityClass mi : mdo.getMethodInvocationList() ) {
			makeSolrDoc(mi);
		}
		
		if(mdo.getMethodDeclarationList().size() > 0) {
			for(SuperEntityClass mdChild : mdo.getMethodDeclarationList()) {
				findAllMethodDeclarations((MethodDeclarationObject)mdChild);
			}
		}
	}
	
	public static SolrInputDocument makeClassSolrDoc(SuperEntityClass entity, JavaFile javaFile) {
		SolrInputDocument solrDoc = new SolrInputDocument();
		
		JavaClass jc = (JavaClass) entity;
		
		System.out.println("this is a class " + entity.getName());
		
		solrDoc.addField(IndexManager.SNIPPET_CODE, jc.getSourceCode());
		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_LOWER_BOUND, Integer.toString(jc.getEndLine()));
		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_UPPER_BOUND, Integer.toString(jc.getLineNumber()));
	
		solrDoc.addField(IndexManager.SNIPPET_IS_INNERCLASS, jc.getInnerClass());
		
		for(SuperEntityClass importStr : jc.getImportList()) {
			solrDoc.addField(IndexManager.SNIPPET_IMPORTS, importStr.getFullyQualifiedName());
			
			String[] split = importStr.getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_IMPORTS_SHORT, split[split.length - 1]);
		}
		solrDoc.addField(IndexManager.SNIPPET_IMPORTS_COUNT, Integer.toString(jc.getImportList().size()));
		
		for(String interfaceStr : jc.getImplements()) {
			solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS, interfaceStr);
			
			String[] split = interfaceStr.split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS_SHORT, split[split.length - 1]);
		}		
		
		/*
		 *  Missing stuff goes here
		 */
		
		return solrDoc;
	}
	
	public static SolrInputDocument makeSolrDoc(SuperEntityClass entity) {
		SolrInputDocument solrDoc = new SolrInputDocument();
				
		if(entity instanceof MethodDeclarationObject) {
			MethodDeclarationObject mdo = (MethodDeclarationObject) entity;
			
			System.out.println("this is a method declaration " + entity.getName());
		}
		
		else if(entity instanceof MethodInvocationObject) {
			MethodInvocationObject mio = (MethodInvocationObject) entity;
			
			System.out.println("this is a method invocation " + entity.getName());
		}
		
		return solrDoc;
	}
	
	public static void main(String[] args) throws IOException, CoreException, NoHeadException, GitAPIException, ParseException {
		fileModelList.clear();
		gitDataList.clear();
		
		//String topDirectoryLocation = args[0];
		String topDirectoryLocation = "/home/kwak/Desktop/jabber-plugin/";
		//String topDirectoryLocation = "/home/kwak/Desktop/jgit-test/";
		
		File inputFolder = new File( topDirectoryLocation );
		traverseUntilJava(inputFolder, topDirectoryLocation);	
		getFileModelConstructs();
	}
}