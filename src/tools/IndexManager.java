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
	public static final String SNIPPET_ALL_AUTHORS = "";
	public static final String SNIPPET_AUTHOR_COUNT = "";
	public static final String SNIPPET_ALL_AUTHOR_EMAILS = "";
	public static final String SNIPPET_ALL_COMMENTS = "";
	public static final String SNIPPET_ALL_DATES = "";
	public static final String SNIPPET_NUMBER_OF_INSERTIONS = "";
	public static final String SNIPPET_NUMBER_OF_DELETIONS = "";
	public static final String SNIPPET_EXTENDS = "";
	public static final String SNIPPET_EXTENDS_SHORT = "";
	public static final String SNIPPET_GRANULARITY = "";
	public static final String SNIPPET_PACKAGE = "";
	public static final String SNIPPET_PACKAGE_SHORT = "";
	public static final String SNIPPET_SIZE = "";
	public static final String SNIPPET_NUMBER_OF_LINES = "";
	
	public static final boolean SNIPPET_IS_GENERIC = false;
	
	
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
		
		//System.out.println("this is a class " + entity.getName());
		
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
		
		for(String author : javaFile.getUniqueAuthors()) {
			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHORS, author);
		}
		
		solrDoc.addField(IndexManager.SNIPPET_AUTHOR_COUNT, Integer.toString(javaFile.getUniqueAuthors().size()));
		
		for(String email : javaFile.getUniqueEmails()) {
			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_EMAILS, email);
		}

		/*
		snippetDoc.addField(IndexManager.AUTHOR_NAME, snippet.thisAuthor);
		
		snippetDoc.addField(IndexManager.AUTHOR_EMAIL, snippet.thisAuthorInfo.email);
		snippetDoc.addField(IndexManager.AUTHOR_AVATAR, snippet.thisAuthorInfo.avatar);
		snippetDoc.addField(IndexManager.AUTHOR_IS_SITE_ADMIN, snippet.thisAuthorInfo.siteAdmin);
		snippetDoc.addField(IndexManager.AUTHOR_TYPE, snippet.thisAuthorInfo);
		
		snippetDoc.addField(IndexManager.PROJECT_ADDRESS, currentProject.project_address);
		snippetDoc.addField(IndexManager.PROJECT_NAME, currentProject.project_name);
		snippetDoc.addField(IndexManager.PROJECT_OWNER, currentProject.project_owner);
		snippetDoc.addField(IndexManager.PROJECT_IS_FORK, currentProject.fork);
		*/
		
		for(CommitData cd : javaFile.getCommitDataList()) {
			solrDoc.addField(IndexManager.SNIPPET_ALL_COMMENTS, cd.getMessage());
			
			// can i do this?
			solrDoc.addField(IndexManager.SNIPPET_ALL_DATES, cd.getSolrDate());
			//snippetDoc.addField(IndexManager.SNIPPET_ALL_VERSIONS, version);
			
			
		}
		
		CommitData headCommit = javaFile.getCommitDataList().get(javaFile.getCommitDataList().size() - 1);
		
		solrDoc.addField("year", headCommit.getYear());
		solrDoc.addField("month", headCommit.getMonth());
		solrDoc.addField("day", headCommit.getDay());
	
		/*
		
		solrDoc.addField(IndexManager.SNIPPET_PATH_COMPLEXITY_SUM, snippet.pathComplexitySum);

		solrDoc.addField(IndexManager.SNIPPET_HAS_COMMENTS, snippet.hasComments);
		solrDoc.addField(IndexManager.SNIPPET_HUMAN_LANGUAGE, snippet.humanLanguage);
		*/
		
		
		
		/*
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FIELDS, snippet.number_of_fields);
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FUNCTIONS, snippet.number_of_functions);
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_INSERTIONS, Integer.toString(headCommit.getInsertions()));
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_DELETIONS, Integer.toString(headCommit.getDeletions()));		
		
		/*

		solrDoc.addField(IndexManager.SNIPPET_INSERTION_CODE_CHURN, snippet.insertionChurn);
		solrDoc.addField(IndexManager.SNIPPET_DELETED_CODE_CHURN, snippet.deletedChurn);
		solrDoc.addField(IndexManager.SNIPPET_CHANGED_CODE_CHURN, snippet.changedChurn);
		
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_EXTENDS, jc.getSuperClass());
		if(jc.getSuperClass() != null){
			String[] split = jc.getSuperClass().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split[split.length-1]);
		}
		
		// TODO
		// not sure if this correct granularity
		solrDoc.addField(IndexManager.SNIPPET_GRANULARITY, "Class");
		
		solrDoc.addField(IndexManager.SNIPPET_PACKAGE, jc.getPackage());
		
		if(jc.getPackage().getFullyQualifiedName() != null){
			String[] split = jc.getPackage().getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split[split.length-1]);
		}		
		
		/*

		String githubAddress = this.toGitHubAddres
				(currentProject.project_owner,currentProject.project_name, file, snippet.thisVersion);
		snippetDoc.addField(IndexManager.SNIPPET_ADDRESS, githubAddress);
		
		String id = githubAddress+"?start="+snippet.startLine+"&end="+snippet.endLine;
		snippetDoc.addField("id",id);
		snippetDoc.addField(IndexManager.EXPAND_ID, id);
		
		if(snippet.hasContainingClass){
			String containingID= githubAddress+"?start="+snippet.containingClassStartLine+
					"&end="+snippet.containingClassEndLine;
			snippetDoc.addField(SNIPPET_CONTAINING_CLASS_ID,containingID);
			snippetDoc.addField(IndexManager.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM
					,snippet.containgClassComplexitySum);
		}
		
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_SIZE, jc.getSourceCode().length());
		
		/*
		
		snippetDoc.addField(IndexManager.SNIPPET_THIS_VERSION, snippet.thisVersion);
		
		snippetDoc.addField(IndexManager.SNIPPET_NAME, snippet.name);
		snippetDoc.addField(IndexManager.SNIPPET_NAME_DELIMITED, snippet.name);
		
		snippetDoc.addField(IndexManager.SNIPPET_VERSION_COMMENT,snippet.thisComment);
		
		snippetDoc.addField(IndexManager.SNIPPET_LAST_UPDATED, snippet.thisDate);
				
		*/		
				
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_LINES, Integer.toString(jc.getEndLine() - jc.getLineNumber() + 1));
		
		/*
		
		for(String variableType: snippet.variableTypes){
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES, variableType);
			
			String[] split2 = variableType.split("[.]");
			String shortName2 = split2[split2.length-1];
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES_SHORT, shortName2);
		}
		
		for(String variableName: snippet.variableNames){
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES, variableName);
			
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES_DELIMITED, variableName);
			
		}
		
		*/
		
		/*
		
		snippetDoc.addField(IndexManager.SNIPPET_IS_ABSTRACT,snippet.isAbstract);
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_IS_GENERIC,jc.getIsGenericType());
		
		/*
		snippetDoc.addField(IndexManager.SNIPPET_IS_WILDCARD,snippet.isWildCard);
		
		for(String bound: snippet.wildCardBounds){
			snippetDoc.addField(IndexManager.SNIPPET_IS_WILDCARD_BOUNDS,bound);
		}
		
		for(String typeParameter: snippet.typeParameters){
			snippetDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS,typeParameter);
		}
		
		snippetDoc.addField("parent",true);
		*/
		
		
		/*
		 *  Missing stuff goes here
		 */
		
		return solrDoc;
	}
	
	public static SolrInputDocument makeSolrDoc(SuperEntityClass entity) {
		SolrInputDocument solrDoc = new SolrInputDocument();
				
		if(entity instanceof MethodDeclarationObject) {
			MethodDeclarationObject mdo = (MethodDeclarationObject) entity;
			
			//System.out.println("this is a method declaration " + entity.getName());
		}
		
		else if(entity instanceof MethodInvocationObject) {
			MethodInvocationObject mio = (MethodInvocationObject) entity;
			
			//System.out.println("this is a method invocation " + entity.getName());
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