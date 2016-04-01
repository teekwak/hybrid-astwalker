package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

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
	ProjectInfo currentProject = new ProjectInfo();
	
	private static IndexManager instance;
	
	public static FileModel fileModel = null;
	public static GitData gitData = null;
	public static String crashListFileName = null;
	public static int pathToURLMapLineNumber = 0;
	public static int fileCount = 0;
	
	private static int MAXDOC = 300;
	private static int MAX_CHILD_DOC = 4000;
	public static int CHILD_COUNT = 0;
	
	// Class
	public static final String AUTHOR_AVATAR = "snippet_author_avatar";
	public static final String AUTHOR_EMAIL = "snippet_author_email";
	public static final String AUTHOR_IS_SITE_ADMIN = "snippet_author_site_admin";
	public static final String AUTHOR_NAME = "snippet_author_name";
	public static final String AUTHOR_TYPE = "snippet_author_type";
	public static final String EXPAND_ID = "expand_id";
	public static final String PROJECT_ADDRESS = "snippet_project_address";
	public static final String PROJECT_IS_FORK = "snippet_project_is_fork";
	public static final String PROJECT_NAME = "snippet_project_name";
	public static final String PROJECT_OWNER = "snippet_project_owner";
	public static final String PROJECT_OWNER_AVATAR = "snippet_project_owner_avatar";
	public static final String SNIPPET_ADDRESS = "snippet_address";
	public static final String SNIPPET_ADDRESS_LOWER_BOUND = "snippet_address_lower_bound";
	public static final String SNIPPET_ADDRESS_UPPER_BOUND ="snippet_address_upper_bound";
	public static final String SNIPPET_ALL_AUTHORS = "snippet_all_authors";
	public static final String SNIPPET_ALL_AUTHOR_AVATARS = "snippet_all_author_avatars";
	public static final String SNIPPET_ALL_AUTHOR_EMAILS = "snippet_all_author_emails";
	public static final String SNIPPET_ALL_COMMENTS = "snippet_all_version_comments";
	public static final String SNIPPET_ALL_DATES = "snippet_all_dates";
	public static final String SNIPPET_ALL_VERSIONS = "snippet_all_versions";
	public static final String SNIPPET_AUTHOR_COUNT = "snippet_author_count";
	public static final String SNIPPET_CONTAINING_CLASS_ID = "snippet_containing_class_id";
	public static final String SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM = "snippet_containing_class_complexity_sum";
	public static final String SNIPPET_CODE = "snippet_code";
	public static final String SNIPPET_DELETED_CODE_CHURN = "snippet_deleted_code_churn";
	public static final String SNIPPET_EXTENDS = "snippet_extends";
	public static final String SNIPPET_EXTENDS_SHORT = "snippet_extends_short";
	public static final String SNIPPET_GRANULARITY = "snippet_granularity";
	public static final String SNIPPET_HAS_JAVA_COMMENTS = "snippet_has_java_comments";
	public static final String SNIPPET_IMPORTS = "snippet_imports";
	public static final String SNIPPET_IMPORTS_SHORT = "snippet_imports_short";
	public static final String SNIPPET_IMPORTS_COUNT = "snippet_imports_count";
	public static final String SNIPPET_IMPLEMENTS = "snippet_implements";
	public static final String SNIPPET_IMPLEMENTS_SHORT = "snippet_implements_short";
	public static final String SNIPPET_INSERTION_CODE_CHURN = "snippet_insertion_code_churn";
	public static final String SNIPPET_INSERTION_DELETION_CODE_CHURN = "snippet_insertion_deletion_code_churn";
	public static final String SNIPPET_IS_ABSTRACT = "snippet_is_abstract";
	public static final String SNIPPET_IS_GENERIC = "snippet_is_generic";
	public static final String SNIPPET_IS_INNERCLASS = "snippet_is_innerClass";
	public static final String SNIPPET_IS_WILDCARD = "snippet_is_wildcard";
	public static final String SNIPPET_IS_WILDCARD_BOUNDS = "snippet_wildcard_bounds";
	public static final String SNIPPET_LAST_UPDATED = "snippet_last_updated";
	public static final String SNIPPET_NAME = "snippet_class_name";
	public static final String SNIPPET_NAME_DELIMITED = "snippet_class_name_delimited";
	public static final String SNIPPET_NUMBER_OF_DELETIONS = "snippet_number_of_deletions";
	public static final String SNIPPET_NUMBER_OF_FIELDS = "snippet_number_of_fields";
	public static final String SNIPPET_NUMBER_OF_FUNCTIONS = "snippet_number_of_functions";	
	public static final String SNIPPET_NUMBER_OF_INSERTIONS = "snippet_number_of_insertions";
	public static final String SNIPPET_NUMBER_OF_LINES = "snippet_number_of_lines";
	public static final String SNIPPET_PACKAGE = "snippet_package";
	public static final String SNIPPET_PACKAGE_SHORT = "snippet_package_short";
	public static final String SNIPPET_PATH_COMPLEXITY_SUM = "snippet_path_complexity_class_sum";
	public static final String SNIPPET_SIZE = "snippet_size";
	public static final String SNIPPET_THIS_VERSION = "snippet_this_version";
	public static final String SNIPPET_TOTAL_DELETIONS = "snippet_total_deletions";
	public static final String SNIPPET_TOTAL_INSERTIONS = "snippet_total_insertions";
	public static final String SNIPPET_VARIABLE_TYPES = "snippet_variable_types";
	public static final String SNIPPET_VARIABLE_TYPES_SHORT = "snippet_variable_types_short";
	public static final String SNIPPET_VARIABLE_NAMES = "snippet_variable_names";
	public static final String SNIPPET_VARIABLE_NAMES_DELIMITED = "snippet_variable_names_delimited";
	public static final String SNIPPET_VERSION_COMMENT = "snippet_version_comment";

	// Method Declaration
	public static final String SNIPPET_METHOD_DEC_CASE_COUNT = "snippet_method_dec_case_count";
	public static final String SNIPPET_METHOD_DEC_CATCH_COUNT = "snippet_method_dec_catch_count";
	public static final String SNIPPET_METHOD_DEC_DECLARING_CLASS = "snippet_method_dec_declaring_class";
	public static final String SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT = "snippet_method_dec_declaring_class_short";
	public static final String SNIPPET_METHOD_DEC_END = "snippet_method_dec_end";
	public static final String SNIPPET_METHOD_DEC_FOR_COUNT = "snippet_method_dec_for_count";
	public static final String SNIPPET_METHOD_DEC_IF_COUNT = "snippet_method_dec_if_count";
	public static final String SNIPPET_METHOD_DEC_IS_ABSTRACT = "snippet_method_dec_is_abstract";
	public static final String SNIPPET_METHOD_DEC_IS_CONSTRUCTOR = "snippet_method_dec_is_constructor";
	public static final String SNIPPET_METHOD_DEC_IS_GENERIC = "snippet_method_dec_is_generic";
	public static final String SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS = "snippet_method_dec_is_generic_type_parameters";
	public static final String SNIPPET_METHOD_DEC_IS_STATIC = "snippet_method_dec_is_static";
	public static final String SNIPPET_METHOD_DEC_IS_VAR_ARGS = "snippet_method_dec_is_var_args";
	public static final String SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS = "snippet_method_dec_is_wildcard_bounds";
	public static final String SNIPPET_METHOD_DEC_LOGICAL_COUNT = "snippet_method_dec_logical_count";
	public static final String SNIPPET_METHOD_DEC_NAME = "snippet_method_dec_name";
	public static final String SNIPPET_METHOD_DEC_NAME_DELIMITED = "snippet_method_dec_name_delimited";
	public static final String SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES = "snippet_method_dec_number_of_local_variables";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES = "snippet_method_dec_parameter_types";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT = "snippet_method_dec_parameter_types_count";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE = "snippet_method_dec_parameter_types_place";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT = "snippet_method_dec_parameter_types_short";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE = "snippet_method_dec_parameter_types_short_place";
	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT = "snippet_method_dec_parameter_types_short_count";
	public static final String SNIPPET_METHOD_DEC_PATH_COMPLEXITY = "snippet_method_dec_path_complexity_method";	
	public static final String SNIPPET_METHOD_DEC_RETURN_TYPE = "snippet_method_dec_return_type";
	public static final String SNIPPET_METHOD_DEC_START = "snippet_method_dec_start";
	public static final String SNIPPET_METHOD_DEC_TERNERARY_COUNT = "snippet_method_dec_ternerary_count";
	public static final String SNIPPET_METHOD_DEC_WHILE_COUNT = "snippet_method_dec_while_count";

	// Method Invocation
	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES = "snippet_method_invocation_arg_types";
	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT = "snippet_method_invocation_arg_types_count";
	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE = "snippet_method_invocation_arg_types_place";
	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT = "snippet_method_invocation_arg_types_short";
	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT = "snippet_method_invocation_arg_types_short_count";
	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE = "snippet_method_invocation_arg_types_short_place";
	public static final String SNIPPET_METHOD_INVOCATION_ARG_VALUES = "snippet_method_invocation_arg_values";
	public static final String SNIPPET_METHOD_INVOCATION_CALLING_CLASS = "snippet_method_invocation_calling_class";
	public static final String SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT = "snippet_method_invocation_calling_class_short";
	public static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS = "snippet_method_invocation_declaring_class";
	public static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT = "snippet_method_invocation_declaring_class_short";
	public static final String SNIPPET_METHOD_INVOCATION_END = "snippet_method_invocation_end";
	public static final String SNIPPET_METHOD_INVOCATION_NAME = "snippet_method_invocation_name";
	public static final String SNIPPET_METHOD_INVOCATION_NAME_DELIMITED = "snippet_method_invocation_name_delimited";
	public static final String SNIPPET_METHOD_INVOCATION_START = "snippet_method_invocation_start";
	
	private IndexManager(){
		
	}
	
	public static IndexManager getInstance(){
		if(instance == null)
			instance = new IndexManager();
		
		return instance;
	}
	
	class ProjectInfo {
		public String project_owner;
		public String project_name;
		public String project_address;
		public String project_owner_avatar;
		public String private_project;
		public String fork;
		public String siteAdmin;
		public String type;
		
		public ArrayList<String> langauge_count = new ArrayList<String>();
		public ArrayList<String> languages = new ArrayList<String>();
		public ArrayList<String> versions = new ArrayList<String>();
		public ArrayList<String> comments = new ArrayList<String>();
		public ArrayList<String> authors = new ArrayList<String>();
		public ArrayList<String> allAuthorIDs = new ArrayList<String>();
		
		public String project_description;
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void processRepo(String pathToDirectory, String projectURL) {
		String htmlURL = "\""+projectURL+"\"";
		
		currentProject = new ProjectInfo();
		
		SolrDocumentList list = Solrj.getInstance().query("id:"+htmlURL, "githubprojects", 1, 0, 9001);
		
		SolrDocument doc = (SolrDocument)list.get(0);
		File project = null;
		
		if(doc.getFieldValue("htmlURL") != null) {
			currentProject.project_address = doc.getFieldValue("htmlURL").toString();
		}
		
		if(doc.getFieldValue("avatarURL") != null) {
			currentProject.project_owner_avatar = doc.getFieldValue("avatarURL").toString();
		}
		
		if(doc.getFieldValue("description") != null){
			currentProject.project_description = doc.getFieldValue("description").toString();
		}
		
		if(doc.getFieldValue("fork") != null){
			currentProject.fork = doc.getFieldValue("fork").toString();
		}
		
		if(doc.getFieldValue("languageCount") != null){
			ArrayList<Long> countArray = (ArrayList<Long>)doc.getFieldValue("languageCount");

			ArrayList<String> languageCount = new ArrayList<String>();
			for(Long count: countArray){
				languageCount.add(count.toString());
			}

			currentProject.langauge_count.addAll(languageCount);
		}

		if(doc.getFieldValue("languages") != null){
			ArrayList<String> languages = (ArrayList<String>)doc.getFieldValue("languages");
			currentProject.languages.addAll(languages);
		}

		if(doc.getFieldValue("privateProject") != null){
			String privateProject = doc.getFieldValue("privateProject").toString();
			currentProject.private_project = privateProject;
		}

		if(doc.getFieldValue("projectName") != null){
			currentProject.project_name = doc.getFieldValue("projectName").toString();
			project = new File(pathToDirectory);
		}
		
		if(doc.getFieldValue("siteAdmin") != null){
			currentProject.siteAdmin = doc.getFieldValue("siteAdmin").toString();
		}

		if(doc.getFieldValue("userName") != null){
			currentProject.project_owner = doc.getFieldValue("userName").toString();
		}

		if(doc.getFieldValue("userType") != null){
			currentProject.type = doc.getFieldValue("userType").toString();
		}

	}
	
	public String toGitHubAddress(String owner, String projectName, File file, String thisVersion){
		String gitHubAddress = "https://raw.github.com/"+owner+"/"+projectName+"/"+thisVersion+"/";
		String path = file.getAbsolutePath();

		int indexOfName = path.indexOf(projectName);
		String filePath = path.substring(indexOfName + projectName.length()+1);
		gitHubAddress = gitHubAddress + filePath;

		return gitHubAddress;
	}
	
	public void makeClassSolrDoc(SuperEntityClass entity, JavaFile javaFile) {
		SolrInputDocument solrDoc = new SolrInputDocument();
		
		File file = new File(javaFile.getFileLocation());
		
		JavaClass jc = (JavaClass) entity;
				
		solrDoc.addField(IndexManager.SNIPPET_NAME, jc.getFullyQualifiedName());
		solrDoc.addField(IndexManager.SNIPPET_NAME_DELIMITED, jc.getName());
		
		solrDoc.addField(IndexManager.SNIPPET_GRANULARITY, "Class"); // String => Class, Method, Method Invocation
		solrDoc.addField("parent", true);
		
		solrDoc.addField(IndexManager.SNIPPET_CODE, jc.getSourceCode());
		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_LOWER_BOUND, ((Number)jc.getEndLine()).longValue());
		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_UPPER_BOUND, ((Number)jc.getLineNumber()).longValue());
	
		solrDoc.addField(IndexManager.SNIPPET_HAS_JAVA_COMMENTS, jc.getHasComments());
		
		solrDoc.addField(IndexManager.SNIPPET_IS_ABSTRACT, jc.getIsAbstract());
		solrDoc.addField(IndexManager.SNIPPET_IS_GENERIC, jc.getIsGenericType());
		solrDoc.addField(IndexManager.SNIPPET_IS_INNERCLASS, jc.getIsInnerClass());
		
		boolean has_wildcard_method = false;
		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
			if(((MethodDeclarationObject) md).getWildcardList().size() > 0) {
				has_wildcard_method =  true;
				break;
			}			
		}
		
		solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD, has_wildcard_method); 
				
		for(SuperEntityClass importStr : jc.getImportList()) {
			solrDoc.addField(IndexManager.SNIPPET_IMPORTS, importStr.getFullyQualifiedName());
						
			String[] split = importStr.getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_IMPORTS_SHORT, split[split.length - 1]);
		}
		solrDoc.addField(IndexManager.SNIPPET_IMPORTS_COUNT, ((Number)jc.getImportList().size()).longValue());
		
		for(String interfaceStr : jc.getImplements()) {
			solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS, interfaceStr);
			String[] split = interfaceStr.split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS_SHORT, split[split.length - 1]);
		}	
		
		solrDoc.addField(IndexManager.SNIPPET_EXTENDS, jc.getSuperClass());
		if(jc.getSuperClass() != null){
			String[] split = jc.getSuperClass().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split[split.length-1]);
		}
		
		solrDoc.addField(IndexManager.SNIPPET_PACKAGE, jc.getPackage().getFullyQualifiedName());
		
		if(jc.getPackage().getFullyQualifiedName() != null){
			String[] split = jc.getPackage().getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split[split.length-1]);
		}	
		
		solrDoc.addField(IndexManager.SNIPPET_PATH_COMPLEXITY_SUM, ((Number)jc.getCyclomaticComplexity()).longValue());
		
		//solrDoc.addField(IndexManager.SNIPPET_HUMAN_LANGUAGE, snippet.humanLanguage);
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FIELDS, ((Number)jc.getGlobalList().size()).longValue());
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FUNCTIONS, ((Number)jc.getMethodDeclarationList().size()).longValue());
		
		if(has_wildcard_method == true) {
			for(SuperEntityClass md : jc.getMethodDeclarationList()) {
				for(SuperEntityClass wild : ((MethodDeclarationObject)md).getWildcardList()) {
					solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD_BOUNDS, wild.getBound());
				}
			}
		}
	
		for(String typeParameter: jc.getGenericParametersList()){
			solrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParameter);
		}
		
		for(String author : javaFile.getUniqueAuthors()) {
			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHORS, author);
		}
		
		solrDoc.addField(IndexManager.SNIPPET_AUTHOR_COUNT, ((Number)javaFile.getUniqueAuthors().size()).longValue());
		
		for(String email : javaFile.getUniqueEmails()) {
			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_AVATARS, makeGravaterURL(email));
			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_EMAILS, email);
		}
		
		CommitData headCommit = javaFile.getCommitDataList().get(javaFile.getCommitDataList().size() - 1);
		
		solrDoc.addField(IndexManager.AUTHOR_NAME, headCommit.getAuthor());
		solrDoc.addField(IndexManager.AUTHOR_EMAIL, headCommit.getEmail());
		
		solrDoc.addField(IndexManager.AUTHOR_AVATAR, makeGravaterURL(headCommit.getEmail()));
		
		solrDoc.addField(IndexManager.AUTHOR_IS_SITE_ADMIN, currentProject.siteAdmin);
		solrDoc.addField(IndexManager.AUTHOR_TYPE, currentProject.type);
		solrDoc.addField(IndexManager.PROJECT_ADDRESS, currentProject.project_address);
		solrDoc.addField(IndexManager.PROJECT_NAME, currentProject.project_name);
		solrDoc.addField(IndexManager.PROJECT_OWNER, currentProject.project_owner);
		solrDoc.addField(IndexManager.PROJECT_OWNER_AVATAR, currentProject.project_owner_avatar);
		solrDoc.addField(IndexManager.PROJECT_IS_FORK, currentProject.fork);
		
		for(CommitData cd : javaFile.getCommitDataList()) {
			solrDoc.addField(IndexManager.SNIPPET_ALL_COMMENTS, cd.getMessage());			
			solrDoc.addField(IndexManager.SNIPPET_ALL_DATES, cd.getSolrDate());
			solrDoc.addField(IndexManager.SNIPPET_ALL_VERSIONS, cd.getHashCode());
		}
		
		solrDoc.addField("month", headCommit.getMonth());
		solrDoc.addField("day", headCommit.getDay());
		solrDoc.addField("year", headCommit.getYear());
		
		int totalInsertions = 0;
		int totalDeletions = 0;
		
		for(CommitData cd : javaFile.getCommitDataList()) {
			totalInsertions += cd.getInsertions();
			totalDeletions += cd.getDeletions();
		}
		
		solrDoc.addField(IndexManager.SNIPPET_SIZE, jc.getSourceCode().length());
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_LINES, ((Number)(jc.getEndLine() - jc.getLineNumber() + 1)).longValue());
		
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_INSERTIONS, ((Number)headCommit.getInsertions()).longValue());
		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_DELETIONS, ((Number)headCommit.getDeletions()).longValue());		
		
		solrDoc.addField(IndexManager.SNIPPET_TOTAL_INSERTIONS, totalInsertions);
		solrDoc.addField(IndexManager.SNIPPET_TOTAL_DELETIONS, totalDeletions);
		
		//solrDoc.addField(IndexManager.SNIPPET_CHANGED_CODE_CHURN, snippet.changedChurn);

		double calculation = 0;
		
		calculation = (double)headCommit.getInsertions() / (double)javaFile.getNumberOfLines();
		solrDoc.addField(IndexManager.SNIPPET_INSERTION_CODE_CHURN, calculation);
				
		calculation = (double)headCommit.getDeletions() / (double)javaFile.getNumberOfLines();
		solrDoc.addField(IndexManager.SNIPPET_DELETED_CODE_CHURN, calculation);
				
		calculation = (double)(headCommit.getInsertions() + headCommit.getDeletions()) / (double)javaFile.getNumberOfLines();
		solrDoc.addField(IndexManager.SNIPPET_INSERTION_DELETION_CODE_CHURN, calculation);	
		
		solrDoc.addField(IndexManager.SNIPPET_THIS_VERSION, headCommit.getHashCode());
		
		solrDoc.addField(IndexManager.SNIPPET_VERSION_COMMENT, headCommit.getMessage());
		solrDoc.addField(IndexManager.SNIPPET_LAST_UPDATED, headCommit.getSolrDate());
		
		String githubAddress = this.toGitHubAddress(currentProject.project_owner,currentProject.project_name, file, headCommit.getHashCode());
		solrDoc.addField(IndexManager.SNIPPET_ADDRESS, githubAddress);
		
		String id = githubAddress+"?start="+jc.getStartCharacter()+"&end="+jc.getEndCharacter();
		solrDoc.addField("id", id);
		solrDoc.addField(IndexManager.EXPAND_ID, id);
		
		if(jc.getClassList().size() > 0){
			for(SuperEntityClass c : jc.getClassList()) {
				JavaClass innerjc = (JavaClass) c;
				
				String containingID= githubAddress+"?start="+innerjc.getLineNumber()+"&end="+innerjc.getEndLine();	
				solrDoc.addField(SNIPPET_CONTAINING_CLASS_ID, containingID);
				solrDoc.addField(IndexManager.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)innerjc.getCyclomaticComplexity()).longValue());
			}
		}
		
		// method declaration is right below this current method
		addVariableListToSolrDoc(jc.getArrayList(), solrDoc);
		addVariableListToSolrDoc(jc.getGenericsList(), solrDoc);
		addVariableListToSolrDoc(jc.getPrimitiveList(), solrDoc);
		addVariableListToSolrDoc(jc.getSimpleList(), solrDoc);
		
		// method declaration is right below this current method
		addVariablesFromMethodDeclaration(jc.getMethodDeclarationList(), solrDoc);		
		
		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
			findAllMethodDeclarations((MethodDeclarationObject)md, solrDoc, id);
		}
		
		Solrj.getInstance().addDoc(solrDoc);
		
		if(Solrj.getInstance().req.getDocuments().size() >= MAXDOC || CHILD_COUNT >= MAX_CHILD_DOC) {
			Solrj.getInstance().commitDocs("MoreLikeThisIndex", 9452);	
		}
	}
	
	/**
	 * recursively get method declarations (for those method declarations inside of each other)
	 * 
	 * @param mdo
	 * @param solrDoc
	 * @param id
	 */
	public static void findAllMethodDeclarations(MethodDeclarationObject mdo, SolrInputDocument solrDoc, String id) {
		makeMethodDeclarationSolrDoc(mdo, solrDoc, id);
		
		for(SuperEntityClass mi : mdo.getMethodInvocationList() ) {
			makeMethodInvocationSolrDoc(mi, solrDoc, id);
		}
		
		if(mdo.getMethodDeclarationList().size() > 0) {
			for(SuperEntityClass mdChild : mdo.getMethodDeclarationList()) {
				findAllMethodDeclarations((MethodDeclarationObject)mdChild, solrDoc, id);
			}
		}
	}
	
	public static void addVariableListToSolrDoc(List<SuperEntityClass> list, SolrInputDocument solrDoc) {
		for(SuperEntityClass entity : list) {
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES, entity.getFullyQualifiedName());
			String[] split = entity.getFullyQualifiedName().split("[.]");
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES_SHORT, split[split.length-1]);	
			
			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES, entity.getName());
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
	
	public static void makeMethodDeclarationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
		SolrInputDocument methodDecSolrDoc = new SolrInputDocument();
		MethodDeclarationObject mdo = (MethodDeclarationObject) entity;
		
		String idDec =  id + "&methodStart=" + entity.getStartCharacter() + "&methodEnd=" + entity.getEndCharacter();
		methodDecSolrDoc.addField("id", idDec);
		methodDecSolrDoc.addField(IndexManager.EXPAND_ID, id);
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAME, mdo.getFullyQualifiedName());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAME_DELIMITED, mdo.getName());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_START, ((Number)mdo.getLineNumber()).longValue());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_END, ((Number)mdo.getEndLine()).longValue());
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_ABSTRACT, mdo.getIsAbstract());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_CONSTRUCTOR, mdo.getIsConstructor());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC, mdo.getIsGenericType());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_STATIC, mdo.getIsStatic());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_VAR_ARGS, mdo.getIsVarargs());		
		
		methodDecSolrDoc.addField("parent",false);
		methodDecSolrDoc.addField("is_method_dec_child",true);
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PATH_COMPLEXITY, ((Number)mdo.getCyclomaticComplexity()).longValue());
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_WHILE_COUNT, ((Number)mdo.getWhileStatementList().size()).longValue());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_FOR_COUNT, ((Number)mdo.getForStatementList().size()).longValue());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IF_COUNT, ((Number)mdo.getIfStatementList().size()).longValue());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_CASE_COUNT, ((Number)mdo.getSwitchCaseList().size()).longValue());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_TERNERARY_COUNT, ((Number)mdo.getConditionalExpressionList().size()).longValue());
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_CATCH_COUNT, ((Number)mdo.getCatchClauseList().size()).longValue());		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_LOGICAL_COUNT, ((Number)mdo.getInfixExpressionList().size()).longValue());
		
		// methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_RECURSIVE, dec.isRecurisive);
		
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS, mdo.getDeclaringClass());

		if(mdo.getDeclaringClass() != null){
			String[] split2 = mdo.getDeclaringClass().split("[.]");
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, split2[split2.length-1]);
		}
				
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_RETURN_TYPE, mdo.getReturnType());
	
		int localVariableCount = mdo.getArrayList().size() + mdo.getGenericsList().size() + mdo.getPrimitiveList().size() + mdo.getSimpleList().size() - mdo.getParametersList().size();	
		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES, ((Number)localVariableCount).longValue());
		
		for(String typeParam : mdo.getGenericParametersList()) {
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParam);
		}
		
		for(SuperEntityClass wc : mdo.getWildcardList()) {
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS, wc.getBound());
		}
		
		Map<String, Integer> paramCount = new HashMap<String, Integer>();
		Map<String, Integer> paramCountShort = new HashMap<String, Integer>();
		
		int parameterTypesListSize = mdo.getParameterTypesList().size(); 
		for(int i = 0; i < parameterTypesListSize; i++) {
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
			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT, type+"_"+count);
		}
		
		CHILD_COUNT++;
		solrDoc.addChildDocument(methodDecSolrDoc);
	}
	
	public static void makeMethodInvocationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
		SolrInputDocument methodInvSolrDoc = new SolrInputDocument();
		MethodInvocationObject mio = (MethodInvocationObject) entity;
		
		methodInvSolrDoc.addField("parent",false);
		methodInvSolrDoc.addField("is_method_invocation_child",true);
		
		String idInvo = id + "&start=" + entity.getStartCharacter() + "&end=" + entity.getEndCharacter();
		methodInvSolrDoc.addField("id", idInvo);
		methodInvSolrDoc.addField(IndexManager.EXPAND_ID, id);
		
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME, mio.getFullyQualifiedName());
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME_DELIMITED, mio.getName());
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_START, ((Number)mio.getLineNumber()).longValue());
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_END, ((Number)mio.getEndLine()).longValue());

		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS, mio.getCallingClass());
		
		if(!mio.getCallingClass().isEmpty()){
			String[] parts = mio.getCallingClass().split("[.]");
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, parts[parts.length-1]);
		}
		
		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS, mio.getDeclaringClass());
				
		if(!mio.getDeclaringClass().isEmpty()){
			String declaringClass = mio.getDeclaringClass();
			
			if(declaringClass.indexOf('<') > -1) {
				declaringClass = declaringClass.split("<")[0];
			}
			
			String[] split = declaringClass.split("[.]");
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split[split.length-1]);
		}

		Map<String, Integer> paramCount = new HashMap<>();
		Map<String, Integer> paramCountShort = new HashMap<>();

		int place = 0;
		for(String argType: mio.getArgumentTypes()){
			
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES, argType);
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE, argType + "_" + place);
			
			String[] split2 = argType.split("[.]");
			String shortName2 = split2[split2.length-1];
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT, shortName2);
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE, argType + "_" + place);
						
			if(paramCount.get(argType) == null) {
				paramCount.put(argType, 1);
			}
			else {
				int count = paramCount.get(argType)+1;
				paramCount.put(argType, count);
			}
			
			if(paramCountShort.get(shortName2) == null) {
				paramCountShort.put(shortName2, 1);
			}
			else {
				int count = paramCountShort.get(shortName2)+1;
				paramCountShort.put(shortName2, count);
			}
			place++;
		}
		
		for(String type: paramCount.keySet()){
			int count = paramCount.get(type);
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT, type + "_" + count);
		}
		
		for(String type: paramCountShort.keySet()){
			int count = paramCountShort.get(type);
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT, type + "_" + count);
		}
		
		for(Object argValue: mio.getArguments()){
			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_VALUES, argValue.toString());
		}
		
		CHILD_COUNT++;
		solrDoc.addChildDocument(methodInvSolrDoc);
	}
	
	public static String makeGravaterURL(String authorEmail) {
		String email = authorEmail;
		String url = "";

		if(email == null)
			return url;

		String md5 = md5Java(email);
		url = "http://www.gravatar.com/avatar/"+md5;
		return url;
	}

	public static String md5Java(String message){
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] hash = md.digest(message.getBytes("UTF-8")); //converting byte array to Hexadecimal
			StringBuilder sb = new StringBuilder(2*hash.length);
			for(byte b : hash){
				sb.append(String.format("%02x", b&0xff));
			}
			digest = sb.toString();
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			} catch (NoSuchAlgorithmException ex) {
				ex.printStackTrace();
			}
		return digest;
	}
	
	/**
	 * Gets all classes, method declarations, and method invocation from FileModel
	 */
	public static void createSolrDocs() {
		for(JavaClass jc : fileModel.getJavaClassList()) {
			JavaFile jf = gitData.getJavaFile();
				
			IndexManager.getInstance().makeClassSolrDoc(jc, jf);
		}
	}
	
	/**
	 * Creates AST and gathers Git data for each file
	 * Runs createSolrDoc() method
	 * 
	 * @param parentNode
	 * @param topDirectoryLocation
	 */
	public static void runASTandGitData(File parentNode, String topDirectoryLocation) throws IOException, CoreException, ParseException {
		fileModel = new FileModel();
		fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
		
		if(fileModel.getJavaClassList().size() > 0) {
			gitData = new GitData();
			gitData.getCommitDataPerFile(topDirectoryLocation, parentNode.getAbsolutePath());
			createSolrDocs();			
		}
				
		fileModel = null;
		gitData = null;
	}
	
	/**
	 * Looks for files that end with .java
	 * Recursively travels down directories
	 * Ignores invisible directories
	 * 
	 * @param parentNode
	 * @param topDirectoryLocation
	 */
	public static void traverseUntilJava(File parentNode, String topDirectoryLocation) throws IOException, CoreException, ParseException {
		if(parentNode.isDirectory()) {
			File childNodes[] = parentNode.listFiles();
						
			for(File c : childNodes) {
				if(!c.getName().startsWith(".")) {
					traverseUntilJava(c, topDirectoryLocation);
				}
			}
		}
		else {
			if(parentNode.getName().endsWith(".java")) {
				fileCount++;
				//System.out.println("Checking: " + parentNode.getName() + " | " + fileCount);
				runASTandGitData(parentNode, topDirectoryLocation);	
			}
		}
	}
	
	/**
	 * Resets IndexManager, gathers information about the repo, and then traverses the repo to find Java files
	 * 
	 * @param topDirectoryLocation
	 * @param URL
	 */
	public static void processRepository(String topDirectoryLocation, String URL) throws IOException, CoreException, ParseException {
		IndexManager.getInstance().processRepo(topDirectoryLocation, URL);
		try {

			pathToURLMapLineNumber += 2;
			
			// write repo URL line number to file
			try {
				FileWriter fw = new FileWriter(new File(crashListFileName), true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(pathToURLMapLineNumber + " ");
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			traverseUntilJava(new File(topDirectoryLocation), topDirectoryLocation);	
			
			// delete repo URL line number from file if successful
			RandomAccessFile randomAccessFile = new RandomAccessFile(crashListFileName, "rw");
			byte b;
			long length = randomAccessFile.length() ;
			if (length != 0) {
			    do {
			        length -= 1;
			        randomAccessFile.seek(length);
			        b = randomAccessFile.readByte();
			    } while (b != 10 && length > 0);
			    randomAccessFile.setLength(length);
			    randomAccessFile.close();
			}
			
		} catch (Exception e) {
			// continue;
		}
		
		IndexManager.getInstance().currentProject = null;
	}
	
	/**
	 * Takes in the path + URL map file and pairs the path with the URL
	 * 
	 * @param file
	 */
	public static void readMapFile(File file, int start, int end) throws CoreException, ParseException {
        boolean path = false;
        boolean url = false;
        String[] arr = {"", ""};

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            int count = 0;
            
            for(int i = 0; i < start; i++) {
            	br.readLine();
            	count++;
            }
                        
            for(String line; (line = br.readLine()) != null && count <= end;) {
                if (line.startsWith("'")) {
                    arr[0] = line.replace("'", "") + "/";
                    
                    // TODO remove this line
                    arr[0] = arr[0].replaceFirst("./", "/home/kwak/Desktop/clones/");
                    
                    path = true;
                    count++;
                } else if (line.startsWith("http")) {
                    arr[1] = line;
                    url = true;
                    count++;
                }
                
                if(path == true && url == true) {
                    path = false;
                    url = false;

                    processRepository(arr[0], arr[1]);
                }
            }

            br.close();

        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
			
	public static void main(String[] args) throws IOException, CoreException, ParseException {
		// arg[0] = start line in pathToURLMap
		// arg[1] = end line in pathToURLMap
		// arg[2] = if not null, start here
		
		// needs to start on an odd number, end on an even number
		int start = 11;
		int end = 20;
		int optionalStart = 13;
		
		fileModel = null;
		gitData = null;
		crashListFileName = null;
		
		IndexManager.getInstance().currentProject = null;
		
		// TODO fix crashList path, pathToURLMap, remove timing, remove print statements
		
		// create crashList file
		crashListFileName = "/home/kwak/Desktop/crashList_" + System.currentTimeMillis() / 1000L + ".txt";
		File file = new File(crashListFileName);
		file.createNewFile();
		
		// set path to URL map
		File pathToURLMap = new File("/home/kwak/Desktop/testMap.txt");
			
		// used if crashed somewhere 
		if(optionalStart > start) {
			start = optionalStart;
		}
		
		// start everything
		readMapFile(pathToURLMap, start, end);
		
		// add remaining Solr documents
		Solrj.getInstance().commitDocs("MoreLikeThisIndex", 9452);
			
		System.out.println("-------------------------------------");
		System.out.println("Finished");
		System.out.println("-------------------------------------");
	}
}