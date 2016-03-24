package tools;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static final String SNIPPET_IMPORTS = "snippet_imports";
	public static final String SNIPPET_IMPORTS_SHORT = "snippet_imports_short";
	public static final String SNIPPET_IMPORTS_COUNT = "snippet_imports_count";
	public static final String SNIPPET_IMPLEMENTS = "snippet_implements";
	public static final String SNIPPET_IMPLEMENTS_SHORT = "snippet_implements_short";
	public static final String SNIPPET_ALL_AUTHORS = "snippet_all_authors";
	public static final String SNIPPET_AUTHOR_COUNT = "snippet_author_count";
	public static final String SNIPPET_ALL_AUTHOR_EMAILS = "snippet_all_author_emails";
	public static final String AUTHOR_NAME = "snippet_author_name";
	public static final String AUTHOR_EMAIL = "snippet_author_email";
	public static final String SNIPPET_ALL_COMMENTS = "snippet_all_version_comments";
	public static final String SNIPPET_ALL_DATES = "snippet_all_dates";
	public static final String SNIPPET_NUMBER_OF_INSERTIONS = "snippet_number_of_insertions";
	public static final String SNIPPET_NUMBER_OF_DELETIONS = "snippet_number_of_deletions";
	public static final String SNIPPET_INSERTION_CODE_CHURN = "snippet_insertion_code_churn";
	public static final String SNIPPET_DELETED_CODE_CHURN = "snippet_deleted_code_churn";
	public static final String SNIPPET_INSERTION_DELETION_CODE_CHURN = "snippet_insertion_deletion_code_churn";
	
	public static final String SNIPPET_EXTENDS = "snippet_extends";
	public static final String SNIPPET_EXTENDS_SHORT = "snippet_extends_short";
	public static final String SNIPPET_GRANULARITY = "snippet_granularity";
	public static final String SNIPPET_PACKAGE = "snippet_package";
	public static final String SNIPPET_PACKAGE_SHORT = "snippet_package_short";
	public static final String SNIPPET_SIZE = "snippet_size";
	public static final String SNIPPET_THIS_VERSION = "snippet_this_version";
	public static final String SNIPPET_VERSION_COMMENT = "snippet_version_comment";
	public static final String SNIPPET_LAST_UPDATED = "snippet_last_updated";
	public static final String SNIPPET_NUMBER_OF_LINES = "snippet_number_of_lines";
	
	public static final String SNIPPET_NUMBER_OF_FUNCTIONS = "snippet_number_of_functions";
	
	public static final String SNIPPET_ALL_VERSIONS = "snippet_all_versions";
	public static final String SNIPPET_PATH_COMPLEXITY_SUM = "snippet_path_complexity_class_sum";
	public static final String SNIPPET_HAS_JAVA_COMMENTS = "snippet_has_java_comments";
	public static final String SNIPPET_NUMBER_OF_FIELDS = "snippet_number_of_fields";
	
	public static final String SNIPPET_IS_GENERIC = "snippet_is_generic";
	public static final String SNIPPET_IS_ABSTRACT = "snippet_is_abstract";
	
	public static final String SNIPPET_NAME = "snippet_class_name";
	public static final String SNIPPET_NAME_DELIMITED = "snippet_class_name_delimited";
	
	public static final String SNIPPET_VARIABLE_TYPES = "snippet_variable_types";
	public static final String SNIPPET_VARIABLE_TYPES_SHORT = "snippet_variable_types_short";
	public static final String SNIPPET_VARIABLE_NAMES = "snippet_variable_names";
	public static final String SNIPPET_VARIABLE_NAMES_DELIMITED = "snippet_variable_names_delimited";
	
	public static final String SNIPPET_IS_WILDCARD = "";
	public static final String SNIPPET_IS_WILDCARD_BOUNDS = "";
	
	// Method Declaration
	public static final String SNIPPET_METHOD_DEC_WHILE_COUNT = "snippet_method_dec_while_count";
	public static final String SNIPPET_METHOD_DEC_FOR_COUNT = "snippet_method_dec_for_count";
	public static final String SNIPPET_METHOD_DEC_IF_COUNT = "snippet_method_dec_if_count";
	public static final String SNIPPET_METHOD_DEC_CASE_COUNT = "snippet_method_dec_case_count";
	public static final String SNIPPET_METHOD_DEC_TERNERARY_COUNT = "snippet_method_dec_ternerary_count";
	public static final String SNIPPET_METHOD_DEC_CATCH_COUNT = "snippet_method_dec_catch_count";
	
	public static final String SNIPPET_METHOD_DEC_IS_CONSTRUCTOR = "snippet_method_dec_is_constructor";
	public static final String SNIPPET_METHOD_DEC_IS_VAR_ARGS = "snippet_method_dec_is_var_args";
	public static final String SNIPPET_METHOD_DEC_DECLARING_CLASS = "";
	public static final String SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT = "";
	
	public static final String SNIPPET_METHOD_DEC_RETURN_TYPE = "";
	
	public static final String SNIPPET_METHOD_DEC_PATH_COMPLEXITY = "";
	public static final String SNIPPET_METHOD_DEC_NAME = "snippet_method_dec_name";
	public static final String SNIPPET_METHOD_DEC_NAME_DELIMITED = "snippet_method_dec_name_delimited";
	public static final String SNIPPET_METHOD_DEC_START = "snippet_method_dec_start";
	public static final String SNIPPET_METHOD_DEC_END = "snippet_method_dec_end";
	public static final String SNIPPET_METHOD_DEC_IS_ABSTRACT = "snippet_method_dec_is_abstract";
	
	public static final String SNIPPET_METHOD_DEC_IS_STATIC = "snippet_method_dec_is_static";
	public static final String SNIPPET_METHOD_DEC_IS_GENERIC = "snippet_method_dec_is_generic";
	
	public static final String SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS = "";
	
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES = "";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE = "";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT = "";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE = "";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT = "";
	
	// Method Invocation
	public static final String SNIPPET_METHOD_INVOCATION_NAME = "";
	public static final String SNIPPET_METHOD_INVOCATION_NAME_DELIMITED = "";
	public static final String SNIPPET_METHOD_INVOCATION_START = "";
	public static final String SNIPPET_METHOD_INVOCATION_END = "";
	
	public static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS = "";
	public static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT = "";
	
	
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
		solrDoc.addField(IndexManager.SNIPPET_HAS_JAVA_COMMENTS, jc.getHasComments());
		
		//solrDoc.addField(IndexManager.SNIPPET_HUMAN_LANGUAGE, snippet.humanLanguage);
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FIELDS, Integer.toString(jc.getGlobalList().size()));
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FUNCTIONS, Integer.toString(jc.getMethodDeclarationList().size()));
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_INSERTIONS, Integer.toString(headCommit.getInsertions()));
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_DELETIONS, Integer.toString(headCommit.getDeletions()));		
		
		//solrDoc.addField(IndexManager.SNIPPET_CHANGED_CODE_CHURN, snippet.changedChurn);

		long calculation = 0;
		calculation = headCommit.getInsertions() / (jc.getEndLine() - jc.getLineNumber() + 1);
		solrDoc.addField(IndexManager.SNIPPET_INSERTION_CODE_CHURN, calculation);
		
		calculation = headCommit.getDeletions() / (jc.getEndLine() - jc.getLineNumber() + 1);
		solrDoc.addField(IndexManager.SNIPPET_DELETED_CODE_CHURN, calculation);
		
		calculation = (headCommit.getInsertions() + headCommit.getDeletions()) / (jc.getEndLine() - jc.getLineNumber() + 1);
		solrDoc.addField(IndexManager.SNIPPET_INSERTION_DELETION_CODE_CHURN, calculation);
		
		solrDoc.addField(IndexManager.SNIPPET_EXTENDS, jc.getSuperClass());
		if(jc.getSuperClass() != null){
			String[] split = jc.getSuperClass().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split[split.length-1]);
		}
		
		solrDoc.addField(IndexManager.SNIPPET_GRANULARITY, "Class"); // String => Class, Method, Method Invocation
		
		solrDoc.addField(IndexManager.SNIPPET_PACKAGE, jc.getPackage());
		
		if(jc.getPackage().getFullyQualifiedName() != null){
			String[] split = jc.getPackage().getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split[split.length-1]);
		}		
		
		/*

		// solr doc

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
		
		// method declaration is right below this current method
		addVariableListToSolrDoc(jc.getArrayList(), solrDoc);
		addVariableListToSolrDoc(jc.getGenericsList(), solrDoc);
		addVariableListToSolrDoc(jc.getPrimitiveList(), solrDoc);
		addVariableListToSolrDoc(jc.getSimpleList(), solrDoc);
		
		// method declaration is right below this current method
		addVariablesFromMethodDeclaration(jc.getMethodDeclarationList(), solrDoc);		
		
		solrDoc.addField(IndexManager.SNIPPET_IS_ABSTRACT, jc.getIsAbstract());
		solrDoc.addField(IndexManager.SNIPPET_IS_GENERIC, jc.getIsGenericType());
		
		/* 
		 		
		for(String typeParameter: snippet.typeParameters){
			snippetDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS,typeParameter);
		}
		
		*/		
		
		boolean has_wildcard_method = false;
		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
			if(((MethodDeclarationObject) md).getWildcardList().size() > 0) {
				has_wildcard_method =  true;
				break;
			}			
		}
		solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD, has_wildcard_method); 
		
		if(has_wildcard_method == true) {
			for(SuperEntityClass md : jc.getMethodDeclarationList()) {
				for(SuperEntityClass wild : ((MethodDeclarationObject)md).getWildcardList()) {
					solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD_BOUNDS, wild.getBound());
				}
			}
		}
	
		solrDoc.addField("parent", true);
		
		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
			findAllMethodDeclarations((MethodDeclarationObject)md, solrDoc);
		}

		return solrDoc; // returns solrDoc for class, method declaration, AND method invocation
	}
	
	// recursively get method declarations (for those method declarations inside of each other)
	// TODO revise to only make one doc, starting from class
	public static void findAllMethodDeclarations(MethodDeclarationObject mdo, SolrInputDocument solrDoc) {
		makeMethodDeclarationSolrDoc(mdo, solrDoc);
		
		for(SuperEntityClass mi : mdo.getMethodInvocationList() ) {
			makeMethodInvocationSolrDoc(mi, solrDoc);
		}
		
		if(mdo.getMethodDeclarationList().size() > 0) {
			for(SuperEntityClass mdChild : mdo.getMethodDeclarationList()) {
				findAllMethodDeclarations((MethodDeclarationObject)mdChild, solrDoc);
			}
		}
	}
	
	public static void addVariableListToSolrDoc(List<SuperEntityClass> list, SolrInputDocument solrDoc) {
		for(SuperEntityClass entity : list) {
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES, entity.getType().toString());
			String[] split = entity.getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES_SHORT, split[split.length-1]);	
			
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES, entity.getFullyQualifiedName());
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES_DELIMITED, entity.getName());
		}
	}
	
	public static void addVariablesFromMethodDeclaration(List<SuperEntityClass> methodDeclarationList, SolrInputDocument solrDoc) {
		for(SuperEntityClass methodDec : methodDeclarationList) {
			MethodDeclarationObject mdo = (MethodDeclarationObject) methodDec;
				addVariableListToSolrDoc(mdo.getArrayList() , solrDoc);
				addVariableListToSolrDoc(mdo.getGenericsList() , solrDoc);
				addVariableListToSolrDoc(mdo.getPrimitiveList() , solrDoc);
				addVariableListToSolrDoc(mdo.getSimpleList(), solrDoc);
				
			if(mdo.getMethodDeclarationList().size() > 0) {
				addVariablesFromMethodDeclaration(mdo.getMethodDeclarationList(), solrDoc);
			}
		}
	}
	
	public static void makeMethodDeclarationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc) {
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
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS, mdo.getDeclaringClass());
		if(!mdo.getDeclaringClass().isEmpty()){
			String[] split2 = mdo.getDeclaringClass().split("[.]");
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, split2[split2.length-1]);
		}
				
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_RETURN_TYPE, mdo.getReturnType());
		
		/*
		methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES, dec.numberOfLocalVariables);
		
		*/
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PATH_COMPLEXITY, Integer.toString(mdo.getCyclomaticComplexity()));
		
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAME, mdo.getFullyQualifiedName());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAME_DELIMITED, mdo.getName());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_START, mdo.getLineNumber());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_END, mdo.getEndLine());
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_ABSTRACT, mdo.getAbstract());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_STATIC, mdo.getStatic());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC, mdo.getIsGenericType());
		
		/*		
		
		for(String typeParam: dec.typeParameters){
			methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS,typeParam);
		}
		
		*/
		
		for(SuperEntityClass wc : mdo.getWildcardList()) {
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS, wc.getBound());
		}
		
		methodDecSolrDoc.addField("parent",false);
		methodDecSolrDoc.addField("is_method_dec_child",true);
		
		Map<String, Integer> paramCount = new HashMap<String, Integer>();
		Map<String, Integer> paramCountShort = new HashMap<String, Integer>();
		
		/*
		for(int i = 0; i < mdo.getParametersList().size(); i++) {
			String argType = mdo.getParametersList().get(i).toString();
			System.out.println(argType.substring(0, argType.lastIndexOf(" ")) + " | " + argType.substring(argType.lastIndexOf(" ") + 1));
		}
		*/
		
		for(int i = 0; i < mdo.getParameterTypesList().size(); i++) {
			String argType = mdo.getParameterTypesList().get(i);
			
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES, argType);
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE, argType+"_"+i);
			
			String[] split2 = argType.split("[.]");
			String shortName2 = split2[split2.length-1];
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, shortName2);
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE, shortName2+"_"+i);
			
			
			if(paramCount.get(argType) == null) {
				paramCount.put(argType, 1);
			}
			else {
				int count = paramCount.get(argType) + 1;
				paramCount.put(argType, count);
			}
			
			if(paramCountShort.get(shortName2) == null) {
				paramCountShort.put(shortName2, 1);
			}
			else {
				int count = paramCountShort.get(shortName2) + 1;
				paramCountShort.put(shortName2, count);
			}  
		}
		
		for(String type: paramCount.keySet()){
			int count = paramCount.get(type);
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT, type+"_"+count);
		}
		
		for(String type: paramCountShort.keySet()){
			int count = paramCountShort.get(type);
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, type+"_"+count);
		}
		
		CHILD_COUNT++;
		
		solrDoc.addChildDocument(methodDecSolrDoc);
		
	}
	
	public static void makeMethodInvocationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc) {
		SolrInputDocument methodInvSolrDoc = new SolrInputDocument();
		MethodInvocationObject mio = (MethodInvocationObject) entity;
		
		methodInvSolrDoc.addField("parent",false);
		methodInvSolrDoc.addField("is_method_invocation_child",true);
		
		//add child document
		/*
		
		String idInvo = id+"&start="+invocation.start+"&end="+invocation.end;
		methodInvocation.addField("id", idInvo);
		methodInvocation.addField(IndexManager.EXPAND_ID, id);
		
		*/
		
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME, mio.getFullyQualifiedName());
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME_DELIMITED, mio.getName());
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_START, mio.getLineNumber());
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_END, mio.getEndLine());
		
		/*
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS, invocation.Callingclass);
	
		if(invocation.Callingclass != null){
		String[] split2 = invocation.Callingclass.split("[.]");
		String shortName2 = split2[split2.length-1];
		methodInvocation.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, shortName2);
		}
		*/
		
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS, mio.getDeclaringClass());
				
		if(!mio.getDeclaringClass().isEmpty()){
			String[] split = mio.getDeclaringClass().split("[.]");
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split[split.length-1]);
		}
		/*
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
		*/

		/*
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
		*/
		
		/*
		CHILD_COUNT++;
		*/

		solrDoc.addChildDocument(methodInvSolrDoc);
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