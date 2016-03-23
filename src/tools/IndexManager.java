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
	
	private static int MAXDOC = 300;
	private static int MAX_CHILD_DOC = 4000;
	public static int CHILD_COUNT = 0;
	
	// Class
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
	public static final String AUTHOR_NAME = "";
	public static final String AUTHOR_EMAIL = "";
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
	public static final String SNIPPET_THIS_VERSION = "";
	public static final String SNIPPET_VERSION_COMMENT = "";
	public static final String SNIPPET_LAST_UPDATED = "";
	public static final String SNIPPET_NUMBER_OF_LINES = "";
	
	public static final String SNIPPET_NUMBER_OF_FUNCTIONS = "";
	
	public static final String SNIPPET_ALL_VERSIONS = "";
	public static final String SNIPPET_PATH_COMPLEXITY_SUM = "";
	public static final String SNIPPET_NUMBER_OF_FIELDS = "";
	
	public static final String SNIPPET_IS_GENERIC = "";
	
	
	public static final String SNIPPET_NAME = "";
	public static final String SNIPPET_NAME_DELIMITED = "";
	
	
	
	// Method Declaration
	public static final String SNIPPET_METHOD_DEC_WHILE_COUNT = "";
	public static final String SNIPPET_METHOD_DEC_FOR_COUNT = "";
	public static final String SNIPPET_METHOD_DEC_IF_COUNT = "";
	public static final String SNIPPET_METHOD_DEC_CASE_COUNT = "";
	public static final String SNIPPET_METHOD_DEC_TERNERARY_COUNT = "";
	public static final String SNIPPET_METHOD_DEC_CATCH_COUNT = "";
	
	
	public static final String SNIPPET_METHOD_DEC_IS_CONSTRUCTOR = "";
	public static final String SNIPPET_METHOD_DEC_IS_VAR_ARGS = "";
	
	
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
		makeMethodDeclarationSolrDoc(mdo);
		
		for(SuperEntityClass mi : mdo.getMethodInvocationList() ) {
			makeMethodInvocationSolrDoc(mi);
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

		CommitData headCommit = javaFile.getCommitDataList().get(javaFile.getCommitDataList().size() - 1);
		
		solrDoc.addField(IndexManager.AUTHOR_NAME, headCommit.getAuthor());
		solrDoc.addField(IndexManager.AUTHOR_EMAIL, headCommit.getEmail());
		
		/*
		
		// from solr project info
		
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
			solrDoc.addField(IndexManager.SNIPPET_ALL_DATES, cd.getSolrDate());
			solrDoc.addField(IndexManager.SNIPPET_ALL_VERSIONS, cd.getHashCode());
		}
		
		solrDoc.addField("year", headCommit.getYear());
		solrDoc.addField("month", headCommit.getMonth());
		solrDoc.addField("day", headCommit.getDay());
	
		solrDoc.addField(IndexManager.SNIPPET_PATH_COMPLEXITY_SUM, Integer.toString(jc.getCyclomaticComplexity()));
		
		/*
		solrDoc.addField(IndexManager.SNIPPET_HAS_JAVA_COMMENTS, snippet.hasComments);
		solrDoc.addField(IndexManager.SNIPPET_HUMAN_LANGUAGE, snippet.humanLanguage);
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FIELDS, Integer.toString(jc.getGlobalList().size()));
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FUNCTIONS, Integer.toString(jc.getMethodDeclarationList().size()));
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_INSERTIONS, Integer.toString(headCommit.getInsertions()));
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_DELETIONS, Integer.toString(headCommit.getDeletions()));		
		
		/*

		solrDoc.addField(IndexManager.SNIPPET_INSERTION_CODE_CHURN, snippet.insertionChurn); // number of insertions of head commit divided by lines of code
		solrDoc.addField(IndexManager.SNIPPET_DELETED_CODE_CHURN, snippet.deletedChurn);
		//solrDoc.addField(IndexManager.SNIPPET_CHANGED_CODE_CHURN, snippet.changedChurn);
		solrDoc.addField(IndexManager.SNIPPET_INSERTION_DELETION_CODE_CHURN, ___);
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_EXTENDS, jc.getSuperClass());
		if(jc.getSuperClass() != null){
			String[] split = jc.getSuperClass().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split[split.length-1]);
		}
		
		// TODO
		// not sure if this correct granularity
		solrDoc.addField(IndexManager.SNIPPET_GRANULARITY, "Class"); // String => Class, Method, Method Invocation
		
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
		solrDoc.addField(IndexManager.SNIPPET_THIS_VERSION, headCommit.getHashCode());
		
		solrDoc.addField(IndexManager.SNIPPET_NAME, jc.getFullyQualifiedName());
		solrDoc.addField(IndexManager.SNIPPET_NAME_DELIMITED, jc.getName());
			
		solrDoc.addField(IndexManager.SNIPPET_VERSION_COMMENT, headCommit.getMessage());
		solrDoc.addField(IndexManager.SNIPPET_LAST_UPDATED, headCommit.getSolrDate());
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_LINES, Integer.toString(jc.getEndLine() - jc.getLineNumber() + 1));
		
		/*
		
		for(String variableType: snippet.variableTypes){
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES, variableType); // for field declarations AND ALL VARIABLES IN METHODS
			
			String[] split2 = variableType.split("[.]");
			String shortName2 = split2[split2.length-1];
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES_SHORT, shortName2);
		}
		
		for(String variableName: snippet.variableNames){
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES, variableName); // for field decs
			
			snippetDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES_DELIMITED, variableName);
			
		}
		
		*/
		
		/*
		
		snippetDoc.addField(IndexManager.SNIPPET_IS_ABSTRACT,snippet.isAbstract); // do research
		
		*/
		
		solrDoc.addField(IndexManager.SNIPPET_IS_GENERIC, jc.getIsGenericType());
		
		/*
		snippetDoc.addField(IndexManager.SNIPPET_IS_WILDCARD,snippet.isWildCard); // do research
		
		for(String bound: snippet.wildCardBounds){
			snippetDoc.addField(IndexManager.SNIPPET_IS_WILDCARD_BOUNDS,bound);
		}
		
		for(String typeParameter: snippet.typeParameters){
			snippetDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS,typeParameter);
		}
		
		
		*/
		
		// TODO check this
		solrDoc.addField("parent",true);
		
		return solrDoc;
	}
	
	public static SolrInputDocument makeMethodDeclarationSolrDoc(SuperEntityClass entity) {
		SolrInputDocument methodDecSolrDoc = new SolrInputDocument();
		
		MethodDeclarationObject mdo = (MethodDeclarationObject) entity;
		
		
		
		/*
		String idDec =  id+"&methodStart="+dec.startLine+"&methodEnd="+dec.endLine;
		methodDec.addField("id", idDec);
		methodDec.addField(IndexManager.EXPAND_ID, id);
		*/
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_WHILE_COUNT, Integer.toString(mdo.getWhileStatementList().size()));
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_FOR_COUNT, Integer.toString(mdo.getForStatementList().size()));
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IF_COUNT, Integer.toString(mdo.getIfStatementList().size()));
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_CASE_COUNT, Integer.toString(mdo.getSwitchCaseList().size()));
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_TERNERARY_COUNT, Integer.toString(mdo.getConditionalExpressionList().size()));
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_CATCH_COUNT, Integer.toString(mdo.getCatchClauseList().size()));		
		
		/*
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_LOGICAL_COUNT, dec.methodLogicOperandcount);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_RECURSIVE, dec.isRecurisive);
		*/
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_CONSTRUCTOR, mdo.getConstructor());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_VAR_ARGS, mdo.getVarargs());		
		
		/*
		
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS, dec.declaringClass);
		
		if(dec.declaringClass != null){
			String[] split2 = dec.declaringClass.split("[.]");
			String shortName2 = split2[split2.length-1];
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, shortName2);
		}
		
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_RETURN_TYPE, dec.returnType);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES, dec.numberOfLocalVariables);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PATH_COMPLEXITY, dec.pathComplexity);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_NAME,dec.methodName);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_NAME_DELIMITED,dec.methodName);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_START,dec.startLine);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_END,dec.endLine);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_ABSTRACT,dec.isAbstract);
		
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_STATIC,dec.isStatic);
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC,dec.isGeneric);
		
		
		for(String typeParam: dec.typeParameters){
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS,typeParam);
		}
		
		for(String bound: dec.wildCardBounds){
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS,bound);
		}
		
		methodDec.addField("parent",false);
		methodDec.addField("is_method_dec_child",true);
		
		HashMap<String, Integer> paramCount = new HashMap<String, Integer>();
		HashMap<String, Integer> paramCountShort = new HashMap<String, Integer>();
		
		for(int i = 0; i<dec.parameters.size(); i++){
			String argType = dec.parameters.get(i);
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES, argType);
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE, argType+"_"+i);
			
			String[] split2 = dec.parameters.get(i).split("[.]");
			String shortName2 = split2[split2.length-1];
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, shortName2);
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE, shortName2+"_"+i);
			
			if(paramCount.get(argType) == null){
				paramCount.put(argType, 1);
			}else{
				int count = paramCount.get(argType)+1;
				paramCount.put(argType, count);
			}
			
			if(paramCountShort.get(shortName2) == null){
				paramCountShort.put(shortName2, 1);
			}else{
				int count = paramCountShort.get(shortName2)+1;
				paramCountShort.put(shortName2, count);
			}

		}
		
		for(String type: paramCount.keySet()){
			int count = paramCount.get(type);
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT, type+"_"+count);
		}
		
		for(String type: paramCountShort.keySet()){
			int count = paramCountShort.get(type);
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, type+"_"+count);
		}
		
		CHILD_COUNT++;
		snippetDoc.addChildDocument(methodDec);
		*/
		
		return methodDecSolrDoc;
	}
	
	public static SolrInputDocument makeMethodInvocationSolrDoc(SuperEntityClass entity) {
		SolrInputDocument methodInvSolrDoc = new SolrInputDocument();
		
		methodInvSolrDoc.addField("parent",false);
		methodInvSolrDoc.addField("is_method_invocation_child",true);
		
		//add child document
		/*
		
		String idInvo = id+"&start="+invocation.start+"&end="+invocation.end;
		methodInvocation.addField("id", idInvo);
		methodInvocation.addField(IndexManager.EXPAND_ID, id);
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME, invocation.methodName);
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME_DELIMITED, invocation.methodName);
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_START, invocation.start);
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_END, invocation.end);
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS, invocation.Callingclass);
		
		if(invocation.Callingclass != null){
		String[] split2 = invocation.Callingclass.split("[.]");
		String shortName2 = split2[split2.length-1];
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, shortName2);
		}
		
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS, invocation.declaringClass);
		
		if(invocation.declaringClass != null){
			String[] split2 = invocation.declaringClass.split("[.]");
			String shortName2 = split2[split2.length-1];
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, shortName2);
		}
		
		HashMap<String, Integer> paramCount = new HashMap<String, Integer>();
		HashMap<String, Integer> paramCountShort = new HashMap<String, Integer>();
		
		int place = 0;
		for(String argType: invocation.argumentTypes){
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES, argType);
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE, argType+"_"+place);
			
			String[] split2 = argType.split("[.]");
			String shortName2 = split2[split2.length-1];
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT, shortName2);
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE, argType+"_"+place);
			
			if(paramCount.get(argType) == null){
				paramCount.put(argType, 1);
			}else{
				int count = paramCount.get(argType)+1;
				paramCount.put(argType, count);
			}
			
			if(paramCountShort.get(shortName2) == null){
				paramCountShort.put(shortName2, 1);
			}else{
				int count = paramCountShort.get(shortName2)+1;
				paramCountShort.put(shortName2, count);
			}
			place++;
		}
		
		for(String type: paramCount.keySet()){
			int count = paramCount.get(type);
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT, type+"_"+count);
		}
		
		for(String type: paramCountShort.keySet()){
			int count = paramCountShort.get(type);
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT, type+"_"+count);
		}
		
		for(String argValue: invocation.argumentValues){
			methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_VALUES, argValue);
		}
		
		//this is how to do a block join
		snippetDoc.addChildDocument(methodInvocation);
		CHILD_COUNT++;
		*/
		
		return methodInvSolrDoc;
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