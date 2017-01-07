//package tools;
//
//import java.io.*;
//import java.net.URL;
//import java.util.*;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//
//import org.apache.commons.io.FileUtils;
//import org.eclipse.core.runtime.CoreException;
//
//import entities.JavaClass;
//import entities.MethodDeclarationObject;
//import entities.MethodInvocationObject;
//import entities.SuperEntityClass;
//
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.common.SolrInputDocument;
//
//class ClonedRepository {
//	String rawGithubURL;
//	String githubCloneURL;
//	String localCloneDirectory;
//
//	ClonedRepository(String r, String g, String l) {
//		this.rawGithubURL = r;
//		this.githubCloneURL = g;
//		this.localCloneDirectory = l;
//	}
//
//	String getRawGithubURL() {
//		return this.rawGithubURL;
//	}
//
//	String getGithubCloneURL() {
//		return this.githubCloneURL;
//	}
//
//	String getLocalDirectory() {
//		return this.localCloneDirectory;
//	}
//
//	void deleteCloneDirectory() {
//		try {
//			FileUtils.deleteDirectory(new File(this.localCloneDirectory));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	void cloneRepository() {
//		ProcessBuilder pb = new ProcessBuilder("git", "clone", this.githubCloneURL, this.localCloneDirectory);
//		try {
//			Process proc = pb.start();
//			proc.waitFor();
//			proc.destroy();
//		} catch (InterruptedException|IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	String getFileName() {
//		return this.rawGithubURL.split("/")[this.rawGithubURL.split("/").length - 1].split("\\?start=")[0];
//	}
//}
//
//public class IndexManager {
//	private static IndexManager instance;
//
//	private static String crashListFileName = null;
//	private static int repoCount = 0;
//	private static String passwordFilePath = null;
//	private static String hostName = null;
//	private static String collectionName = null;
//	private static int portNumber = -1;
//	private static boolean successfulUpload = true;
//
//	private static int MAXDOC = 300;
//	private static int MAX_CHILD_DOC = 4000;
//	static int CHILD_COUNT = 0;
//
//	private static Map<String, Boolean> properties;
//
//	// Class
//	private static final String AUTHOR_AVATAR = "snippet_author_avatar";
//	private static final String AUTHOR_EMAIL = "snippet_author_email";
//	private static final String AUTHOR_IS_SITE_ADMIN = "snippet_author_site_admin";
//	private static final String AUTHOR_NAME = "snippet_author_name";
//	private static final String AUTHOR_TYPE = "snippet_author_type";
//	private static final String EXPAND_ID = "expand_id";
//	private static final String PROJECT_ADDRESS = "snippet_project_address";
//	private static final String PROJECT_DESCRIPTION = "snippet_project_description";
//	private static final String PROJECT_IS_FORK = "snippet_project_is_fork";
//	private static final String PROJECT_NAME = "snippet_project_name";
//	private static final String PROJECT_OWNER = "snippet_project_owner";
//	private static final String PROJECT_OWNER_AVATAR = "snippet_project_owner_avatar";
//	private static final String SNIPPET_ADDRESS = "snippet_address";
//	private static final String SNIPPET_ADDRESS_LOWER_BOUND = "snippet_address_lower_bound";
//	private static final String SNIPPET_ADDRESS_UPPER_BOUND ="snippet_address_upper_bound";
//	private static final String SNIPPET_ALL_AUTHORS = "snippet_all_authors";
//	private static final String SNIPPET_ALL_AUTHOR_AVATARS = "snippet_all_author_avatars";
//	private static final String SNIPPET_ALL_AUTHOR_EMAILS = "snippet_all_author_emails";
//	private static final String SNIPPET_ALL_COMMENTS = "snippet_all_version_comments";
//	private static final String SNIPPET_ALL_DATES = "snippet_all_dates";
//	private static final String SNIPPET_ALL_VERSIONS = "snippet_all_versions";
//	private static final String SNIPPET_AUTHOR_COUNT = "snippet_author_count";
//	private static final String SNIPPET_COMPLEXITY_DENSITY = "snippet_complexity_density";
//	private static final String SNIPPET_CONTAINING_CLASS_ID = "snippet_containing_class_id";
//	private static final String SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM = "snippet_containing_class_complexity_sum";
//	private static final String SNIPPET_CODE = "snippet_code";
//	private static final String SNIPPET_DELETED_CODE_CHURN = "snippet_deleted_code_churn";
//	private static final String SNIPPET_EXTENDS = "snippet_extends";
//	private static final String SNIPPET_EXTENDS_SHORT = "snippet_extends_short";
//	private static final String SNIPPET_GRANULARITY = "snippet_granularity";
//	private static final String SNIPPET_HAS_JAVA_COMMENTS = "snippet_has_java_comments";
//	private static final String SNIPPET_IMPORTS = "snippet_imports";
//	private static final String SNIPPET_IMPORTS_SHORT = "snippet_imports_short";
//	private static final String SNIPPET_IMPORTS_COUNT = "snippet_imports_count";
//	private static final String SNIPPET_IMPLEMENTS = "snippet_implements";
//	private static final String SNIPPET_IMPLEMENTS_SHORT = "snippet_implements_short";
//	private static final String SNIPPET_INSERTION_CODE_CHURN = "snippet_insertion_code_churn";
//	private static final String SNIPPET_INSERTION_DELETION_CODE_CHURN = "snippet_insertion_deletion_code_churn";
//	private static final String SNIPPET_IS_ABSTRACT = "snippet_is_abstract";
//	private static final String SNIPPET_IS_ANONYMOUS = "snippet_is_anonymous";
//	private static final String SNIPPET_IS_GENERIC = "snippet_is_generic";
//	private static final String SNIPPET_IS_INNERCLASS = "snippet_is_innerClass";
//	private static final String SNIPPET_IS_WILDCARD = "snippet_is_wildcard";
//	private static final String SNIPPET_IS_WILDCARD_BOUNDS = "snippet_wildcard_bounds";
//	private static final String SNIPPET_LAST_UPDATED = "snippet_last_updated";
//	private static final String SNIPPET_METHOD_DEC_NAMES = "snippet_method_dec_names";
//	private static final String SNIPPET_METHOD_INVOCATION_NAMES = "snippet_method_invocation_names";
//	private static final String SNIPPET_NAME = "snippet_class_name";
//	private static final String SNIPPET_NAME_DELIMITED = "snippet_class_name_delimited";
//	private static final String SNIPPET_NUMBER_OF_DELETIONS = "snippet_number_of_deletions";
//	private static final String SNIPPET_NUMBER_OF_FIELDS = "snippet_number_of_fields";
//	private static final String SNIPPET_NUMBER_OF_FUNCTIONS = "snippet_number_of_functions";
//	private static final String SNIPPET_NUMBER_OF_INSERTIONS = "snippet_number_of_insertions";
//	private static final String SNIPPET_NUMBER_OF_LINES = "snippet_number_of_lines";
//	private static final String SNIPPET_PACKAGE = "snippet_package";
//	private static final String SNIPPET_PACKAGE_SHORT = "snippet_package_short";
//	private static final String SNIPPET_PATH_COMPLEXITY_SUM = "snippet_path_complexity_class_sum";
//	private static final String SNIPPET_SIZE = "snippet_size";
//	private static final String SNIPPET_THIS_VERSION = "snippet_this_version";
//	private static final String SNIPPET_TOTAL_DELETIONS = "snippet_total_deletions";
//	private static final String SNIPPET_TOTAL_INSERTIONS = "snippet_total_insertions";
//	private static final String SNIPPET_VARIABLE_TYPES = "snippet_variable_types";
//	private static final String SNIPPET_VARIABLE_TYPES_SHORT = "snippet_variable_types_short";
//	private static final String SNIPPET_VARIABLE_NAMES = "snippet_variable_names";
//	private static final String SNIPPET_VARIABLE_NAMES_DELIMITED = "snippet_variable_names_delimited";
//	private static final String SNIPPET_VERSION_COMMENT = "snippet_version_comment";
//
//	// Method Declaration
//	private static final String SNIPPET_METHOD_DEC_CASE_COUNT = "snippet_method_dec_case_count";
//	private static final String SNIPPET_METHOD_DEC_CATCH_COUNT = "snippet_method_dec_catch_count";
//	private static final String SNIPPET_METHOD_DEC_DECLARING_CLASS = "snippet_method_dec_declaring_class";
//	private static final String SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT = "snippet_method_dec_declaring_class_short";
//	private static final String SNIPPET_METHOD_DEC_END = "snippet_method_dec_end";
//	private static final String SNIPPET_METHOD_DEC_FOR_COUNT = "snippet_method_dec_for_count";
//	private static final String SNIPPET_METHOD_DEC_IF_COUNT = "snippet_method_dec_if_count";
//	private static final String SNIPPET_METHOD_DEC_IS_ABSTRACT = "snippet_method_dec_is_abstract";
//	private static final String SNIPPET_METHOD_DEC_IS_CONSTRUCTOR = "snippet_method_dec_is_constructor";
//	private static final String SNIPPET_METHOD_DEC_IS_GENERIC = "snippet_method_dec_is_generic";
//	private static final String SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS = "snippet_method_dec_is_generic_type_parameters";
//	private static final String SNIPPET_METHOD_DEC_IS_STATIC = "snippet_method_dec_is_static";
//	private static final String SNIPPET_METHOD_DEC_IS_VAR_ARGS = "snippet_method_dec_is_var_args";
//	private static final String SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS = "snippet_method_dec_is_wildcard_bounds";
//	private static final String SNIPPET_METHOD_DEC_LOGICAL_COUNT = "snippet_method_dec_logical_count";
//	private static final String SNIPPET_METHOD_DEC_NAME = "snippet_method_dec_name";
//	private static final String SNIPPET_METHOD_DEC_NAME_DELIMITED = "snippet_method_dec_name_delimited";
//	private static final String SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES = "snippet_method_dec_number_of_local_variables";
//	private static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES = "snippet_method_dec_parameter_types";
//	private static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT = "snippet_method_dec_parameter_types_count";
//	private static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE = "snippet_method_dec_parameter_types_place";
//	private static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT = "snippet_method_dec_parameter_types_short";
//	private static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE = "snippet_method_dec_parameter_types_short_place";
//	private static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT = "snippet_method_dec_parameter_types_short_count";
//	private static final String SNIPPET_METHOD_DEC_PATH_COMPLEXITY = "snippet_method_dec_path_complexity_method";
//	private static final String SNIPPET_METHOD_DEC_RETURN_TYPE = "snippet_method_dec_return_type";
//	private static final String SNIPPET_METHOD_DEC_START = "snippet_method_dec_start";
//	private static final String SNIPPET_METHOD_DEC_TERNARY_COUNT = "snippet_method_dec_ternary_count";
//	private static final String SNIPPET_METHOD_DEC_WHILE_COUNT = "snippet_method_dec_while_count";
//
//	// Method Invocation
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES = "snippet_method_invocation_arg_types";
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT = "snippet_method_invocation_arg_types_count";
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE = "snippet_method_invocation_arg_types_place";
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT = "snippet_method_invocation_arg_types_short";
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT = "snippet_method_invocation_arg_types_short_count";
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE = "snippet_method_invocation_arg_types_short_place";
//	private static final String SNIPPET_METHOD_INVOCATION_ARG_VALUES = "snippet_method_invocation_arg_values";
//	private static final String SNIPPET_METHOD_INVOCATION_CALLING_CLASS = "snippet_method_invocation_calling_class";
//	private static final String SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT = "snippet_method_invocation_calling_class_short";
//	private static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS = "snippet_method_invocation_declaring_class";
//	private static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT = "snippet_method_invocation_declaring_class_short";
//	private static final String SNIPPET_METHOD_INVOCATION_END = "snippet_method_invocation_end";
//	private static final String SNIPPET_METHOD_INVOCATION_NAME = "snippet_method_invocation_name";
//	private static final String SNIPPET_METHOD_INVOCATION_NAME_DELIMITED = "snippet_method_invocation_name_delimited";
//	private static final String SNIPPET_METHOD_INVOCATION_START = "snippet_method_invocation_start";
//
//	private IndexManager(){
//
//	}
//
//	static IndexManager getInstance(){
//		if(instance == null)
//			instance = new IndexManager();
//
//		return instance;
//	}
//
//	@SuppressWarnings({ "unused", "unchecked" })
//	// todo this doesnt make sense. where is the project data supposed to draw from?
//	// todo i think this comes from codeexchange
//	private static void addProjectData(SolrInputDocument solrDoc, String pathToDirectory, String projectURL) {
//
//		// todo check the htmlURL object
//		String htmlURL = "\""+projectURL+"\"";
//
//		String hostName = "grok.ics.uci.edu";
//		int portNumber = 9551;
//		String collectionName = "MoreLikeThisIndex";
//
//		SolrDocumentList list = Solrj.getInstance(passwordFilePath).query("id:" + htmlURL, hostName, portNumber, collectionName, 1);
//
//		if(list.isEmpty()) {
//			throw new IllegalArgumentException("[ERROR]: project data is null!");
//		}
//
//		SolrDocument doc = list.get(0);
//
//		File project = null;
//
//		if(doc.getFieldValue("htmlURL") != null) {
//			solrDoc.addField(IndexManager.PROJECT_ADDRESS, doc.getFieldValue("htmlURL").toString());
//		}
//
//		if(doc.getFieldValue("avatarURL") != null) {
//			solrDoc.addField(IndexManager.PROJECT_OWNER_AVATAR, doc.getFieldValue("avatarURL").toString());
//		}
//
//		if(doc.getFieldValue("description") != null){
//			solrDoc.addField(IndexManager.PROJECT_DESCRIPTION, doc.getFieldValue("description").toString());
//		}
//
//		if(doc.getFieldValue("fork") != null){
//			solrDoc.addField(IndexManager.PROJECT_IS_FORK, doc.getFieldValue("fork").toString());
//		}
//
//		if(doc.getFieldValue("projectName") != null){
//			solrDoc.addField(IndexManager.PROJECT_NAME, doc.getFieldValue("projectName").toString());
//		}
//
//		if(doc.getFieldValue("siteAdmin") != null){
//			solrDoc.addField(IndexManager.AUTHOR_IS_SITE_ADMIN, doc.getFieldValue("siteAdmin").toString());
//		}
//
//		if(doc.getFieldValue("userName") != null){
//			solrDoc.addField(IndexManager.PROJECT_OWNER, doc.getFieldValue("userName").toString());
//		}
//
//		if(doc.getFieldValue("userType") != null){
//			solrDoc.addField(IndexManager.AUTHOR_TYPE, doc.getFieldValue("userType").toString());
//		}
//	}
//
////	private String toGitHubAddress(String owner, String projectName, File file, String thisVersion){
////		String gitHubAddress = "https://raw.github.com/"+owner+"/"+projectName+"/"+thisVersion+"/";
////		String path = file.getAbsolutePath();
////
////		int indexOfName = path.indexOf(projectName);
////		String filePath = path.substring(indexOfName + projectName.length()+1);
////		gitHubAddress = gitHubAddress + filePath;
////
////		return gitHubAddress;
////	}
//
////	private static void makeClassSolrDoc(SuperEntityClass entity, JavaFile javaFile) {
////		SolrInputDocument solrDoc = new SolrInputDocument();
////
////		File file = new File(javaFile.getFileLocation());
////
////		JavaClass jc = (JavaClass) entity;
////
////		solrDoc.addField(IndexManager.SNIPPET_NAME, jc.getFullyQualifiedName());
////		solrDoc.addField(IndexManager.SNIPPET_NAME_DELIMITED, jc.getName());
////
////		solrDoc.addField(IndexManager.SNIPPET_GRANULARITY, "Class"); // String => Class, Method, Method Invocation
////		solrDoc.addField("parent", true);
////
////		solrDoc.addField(IndexManager.SNIPPET_CODE, jc.getSourceCode());
////		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_LOWER_BOUND, ((Number)jc.getEndLine()).longValue());
////		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_UPPER_BOUND, ((Number)jc.getLineNumber()).longValue());
////
////		solrDoc.addField(IndexManager.SNIPPET_HAS_JAVA_COMMENTS, jc.getHasComments());
////
////		solrDoc.addField(IndexManager.SNIPPET_IS_ABSTRACT, jc.getIsAbstract());
////		solrDoc.addField(IndexManager.SNIPPET_IS_ANONYMOUS, jc.getIsAnonymous());
////		solrDoc.addField(IndexManager.SNIPPET_IS_GENERIC, jc.getIsGenericType());
////		solrDoc.addField(IndexManager.SNIPPET_IS_INNERCLASS, jc.getIsInnerClass());
////
////		boolean has_wildcard_method = false;
////		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
////			if(((MethodDeclarationObject) md).getWildcardList().size() > 0) {
////				has_wildcard_method =  true;
////				break;
////			}
////		}
////
////		solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD, has_wildcard_method);
////
////		for(SuperEntityClass importStr : jc.getImportList()) {
////			solrDoc.addField(IndexManager.SNIPPET_IMPORTS, importStr.getFullyQualifiedName());
////
////			String[] split = importStr.getFullyQualifiedName().split("[.]");
////			solrDoc.addField(IndexManager.SNIPPET_IMPORTS_SHORT, split[split.length - 1]);
////		}
////		solrDoc.addField(IndexManager.SNIPPET_IMPORTS_COUNT, ((Number)jc.getImportList().size()).longValue());
////
////		for(String interfaceStr : jc.getImplements()) {
////			solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS, interfaceStr);
////
////			if(interfaceStr.indexOf('<') > -1) {
////				String[] split1 = interfaceStr.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
////			}
////			else {
////				String[] split = interfaceStr.split("[.]");
////				solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS_SHORT, split[split.length - 1]);
////			}
////		}
////
////		solrDoc.addField(IndexManager.SNIPPET_EXTENDS, jc.getSuperClass());
////		if(jc.getSuperClass() != null){
////			String superClass = jc.getSuperClass();
////
////			if(superClass.indexOf('<') > -1) {
////				String[] split1 = superClass.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
////			}
////			else {
////				String[] split = superClass.split("[.]");
////				solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split[split.length - 1]);
////			}
////		}
////
////		solrDoc.addField(IndexManager.SNIPPET_PACKAGE, jc.getPackage().getFullyQualifiedName());
////
////		if(jc.getPackage().getFullyQualifiedName() != null){
////			String packageName = jc.getPackage().getFullyQualifiedName();
////
////			if(packageName.indexOf('<') > -1) {
////				String[] split1 = packageName.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
////			}
////			else {
////				String[] split = packageName.split("[.]");
////				solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split[split.length - 1]);
////			}
////		}
////
////		int complexity = jc.getCyclomaticComplexity();
////
////		solrDoc.addField(IndexManager.SNIPPET_PATH_COMPLEXITY_SUM, ((Number)complexity).longValue());
////
////		int methodInvCount = jc.getTotalMethodInvocationCount();
////		int codeSize = jc.getSourceCode().length();
////
////		if(methodInvCount == 0) {
////			methodInvCount = 1;
////		}
////		if(codeSize == 0) {
////			codeSize = 1;
////		}
////
////		solrDoc.addField(IndexManager.SNIPPET_COMPLEXITY_DENSITY, (double)jc.getCyclomaticComplexity() * (1 / (double)methodInvCount) * (1 / (double)codeSize));
////
////		//solrDoc.addField(IndexManager.SNIPPET_HUMAN_LANGUAGE, snippet.humanLanguage);
////
////		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FIELDS, ((Number)jc.getGlobalList().size()).longValue());
////		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FUNCTIONS, ((Number)jc.getMethodDeclarationList().size()).longValue());
////
////		Set<String> methodDecNames = new HashSet<>();
////		for(String name : jc.getMethodDeclarationNames()) {
////			methodDecNames.add(name);
////		}
////		for(String name : methodDecNames) {
////			solrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAMES, name);
////		}
////
////		Set<String> methodInvNames = new HashSet<>();
////		for(String name : jc.getMethodInvocationNames()) {
////			methodInvNames.add(name);
////		}
////		for(String name : methodInvNames) {
////			solrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAMES, name);
////		}
////
////		if(has_wildcard_method) {
////			for(SuperEntityClass md : jc.getMethodDeclarationList()) {
////				for(SuperEntityClass wild : ((MethodDeclarationObject)md).getWildcardList()) {
////					solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD_BOUNDS, wild.getBound());
////				}
////			}
////		}
////
////		for(String typeParameter: jc.getGenericParametersList()){
////			solrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParameter);
////		}
////
////		for(String author : javaFile.getUniqueAuthors()) {
////			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHORS, author);
////		}
////
////		solrDoc.addField(IndexManager.SNIPPET_AUTHOR_COUNT, ((Number)javaFile.getUniqueAuthors().size()).longValue());
////
////		for(String email : javaFile.getUniqueEmails()) {
////			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_AVATARS, makeGravaterURL(email));
////			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_EMAILS, email);
////		}
////
////		CommitData headCommit = javaFile.getCommitDataList().get(javaFile.getCommitDataList().size() - 1);
////
////		solrDoc.addField(IndexManager.AUTHOR_NAME, headCommit.getAuthor());
////		solrDoc.addField(IndexManager.AUTHOR_EMAIL, headCommit.getEmail());
////
////		solrDoc.addField(IndexManager.AUTHOR_AVATAR, makeGravaterURL(headCommit.getEmail()));
////
////		solrDoc.addField(IndexManager.AUTHOR_IS_SITE_ADMIN, currentProject.siteAdmin);
////		solrDoc.addField(IndexManager.AUTHOR_TYPE, currentProject.type);
////		solrDoc.addField(IndexManager.PROJECT_ADDRESS, currentProject.project_address);
////		solrDoc.addField(IndexManager.PROJECT_NAME, currentProject.project_name);
////		solrDoc.addField(IndexManager.PROJECT_OWNER, currentProject.project_owner);
////		solrDoc.addField(IndexManager.PROJECT_OWNER_AVATAR, currentProject.project_owner_avatar);
////		solrDoc.addField(IndexManager.PROJECT_IS_FORK, currentProject.fork);
////		solrDoc.addField(IndexManager.PROJECT_DESCRIPTION, currentProject.project_description);
////
////		for(CommitData cd : javaFile.getCommitDataList()) {
////			solrDoc.addField(IndexManager.SNIPPET_ALL_COMMENTS, cd.getMessage());
////			solrDoc.addField(IndexManager.SNIPPET_ALL_DATES, cd.getSolrDate());
////			solrDoc.addField(IndexManager.SNIPPET_ALL_VERSIONS, cd.getHashCode());
////		}
////
////		solrDoc.addField("month", headCommit.getMonth());
////		solrDoc.addField("day", headCommit.getDay());
////		solrDoc.addField("year", headCommit.getYear());
////
////		int totalInsertions = 0;
////		int totalDeletions = 0;
////
////		for(CommitData cd : javaFile.getCommitDataList()) {
////			totalInsertions += cd.getInsertions();
////			totalDeletions += cd.getDeletions();
////		}
////
////		solrDoc.addField(IndexManager.SNIPPET_SIZE, jc.getSourceCode().length());
////		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_LINES, ((Number)(jc.getEndLine() - jc.getLineNumber() + 1)).longValue());
////
////		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_INSERTIONS, ((Number)headCommit.getInsertions()).longValue());
////		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_DELETIONS, ((Number)headCommit.getDeletions()).longValue());
////
////		solrDoc.addField(IndexManager.SNIPPET_TOTAL_INSERTIONS, totalInsertions);
////		solrDoc.addField(IndexManager.SNIPPET_TOTAL_DELETIONS, totalDeletions);
////
////		//solrDoc.addField(IndexManager.SNIPPET_CHANGED_CODE_CHURN, snippet.changedChurn);
////
////		double calculation;
////
////		calculation = (double)headCommit.getInsertions() / (double)javaFile.getNumberOfLines();
////		solrDoc.addField(IndexManager.SNIPPET_INSERTION_CODE_CHURN, calculation);
////
////		calculation = (double)headCommit.getDeletions() / (double)javaFile.getNumberOfLines();
////		solrDoc.addField(IndexManager.SNIPPET_DELETED_CODE_CHURN, calculation);
////
////		calculation = (double)(headCommit.getInsertions() + headCommit.getDeletions()) / (double)javaFile.getNumberOfLines();
////		solrDoc.addField(IndexManager.SNIPPET_INSERTION_DELETION_CODE_CHURN, calculation);
////
////		solrDoc.addField(IndexManager.SNIPPET_THIS_VERSION, headCommit.getHashCode());
////
////		solrDoc.addField(IndexManager.SNIPPET_VERSION_COMMENT, headCommit.getMessage());
////		solrDoc.addField(IndexManager.SNIPPET_LAST_UPDATED, headCommit.getSolrDate());
////
////		String githubAddress = this.toGitHubAddress(currentProject.project_owner,currentProject.project_name, file, headCommit.getHashCode());
////		solrDoc.addField(IndexManager.SNIPPET_ADDRESS, githubAddress);
////
////		String id = githubAddress+"?start="+jc.getStartCharacter()+"&end="+jc.getEndCharacter();
////		solrDoc.addField("id", id);
////		solrDoc.addField(IndexManager.EXPAND_ID, id);
////
////		for(JavaClass cl : fileModel.getJavaClassList()) {
////			try {
////				if(cl.getName() != null && cl.getName().equals(jc.getContainingClass())) {
////					String containingID = githubAddress + "?start=" + cl.getLineNumber() + "&end=" + cl.getEndLine();
////					solrDoc.addField(SNIPPET_CONTAINING_CLASS_ID, containingID);
////					solrDoc.addField(IndexManager.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)cl.getCyclomaticComplexity()).longValue());
////					break;
////				}
////			} catch (NullPointerException e) {
////				String containingID = githubAddress + "?start=" + jc.getLineNumber() + "&end=" + jc.getEndLine();
////				solrDoc.addField(SNIPPET_CONTAINING_CLASS_ID, containingID);
////				solrDoc.addField(IndexManager.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)jc.getCyclomaticComplexity()).longValue());
////				break;
////			}
////		}
////
////		// method declaration is right below this current method
////		Set<String> variableTypes = new HashSet<>();
////		Set<String> variableTypesShort = new HashSet<>();
////		Set<String> variableNames = new HashSet<>();
////
////		// TODO
////		addVariableListToSolrDoc(jc.getArrayList(), variableTypes, variableTypesShort, variableNames);
////
////		// method declaration is right below this current method
////		addVariablesFromMethodDeclaration(jc.getMethodDeclarationList(), variableTypes, variableTypesShort, variableNames);
////
////		for(String t : variableTypes) {
////			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES, t);
////		}
////		for(String ts : variableTypesShort) {
////			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES_SHORT, ts);
////		}
////		for(String n : variableNames) {
////			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES, n);
////			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES_DELIMITED, n);
////		}
////
////		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
////			findAllMethodDeclarations((MethodDeclarationObject)md, solrDoc, id);
////		}
////
////		Solrj.getInstance(passwordFilePath).addDoc(solrDoc);
////
////		try {
////			if(Solrj.getInstance(passwordFilePath).req.getDocuments().size() >= MAXDOC || CHILD_COUNT >= MAX_CHILD_DOC) {
////				Solrj.getInstance(passwordFilePath).commitDocs(hostName, portNumber, collectionName);
////			}
////		} catch (Exception e) {
////      Solrj.getInstance(passwordFilePath).clearDocs();
////      successfulUpload = false;
////		}
////	}
////
////	/**
////	 * recursively get method declarations (for those method declarations inside of each other)
////	 *
////	 * @param mdo x
////	 * @param solrDoc x
////	 * @param id x
////	 */
////	private static void findAllMethodDeclarations(MethodDeclarationObject mdo, SolrInputDocument solrDoc, String id) {
////		makeMethodDeclarationSolrDoc(mdo, solrDoc, id);
////
////		for(SuperEntityClass mi : mdo.getMethodInvocationList() ) {
////			makeMethodInvocationSolrDoc(mi, solrDoc, id);
////		}
////
////		if(mdo.getMethodDeclarationList().size() > 0) {
////			for(SuperEntityClass mdChild : mdo.getMethodDeclarationList()) {
////				findAllMethodDeclarations((MethodDeclarationObject)mdChild, solrDoc, id);
////			}
////		}
////	}
//
//	// TODO
//	// this function is named improperly
//	// the variables are added to the list, which are then returned
//	private static void getVariablesFromSuperEntityList(List<SuperEntityClass> list, Map<String, Set<String>> variables) {
//		for(SuperEntityClass entity : list) {
//			variables.get("variableNames").add(entity.getName());
//
//			String fqn = entity.getFullyQualifiedName();
//			variables.get("variableTypes").add(fqn);
//
//			if(fqn.indexOf('<') > -1) {
//				String[] split1 = fqn.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				variables.get("variableTypesShort").add(split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = fqn.split("[.]");
//				variables.get("variableTypesShort").add(split[split.length-1]);
//			}
//		}
//	}
//
//	// TODO
//	// same with this function
//	// the method decs are not necessarily added to the solr doc, but rather just to lists
//	private static void getVariablesFromMethodDeclaration(List<SuperEntityClass> methodDeclarationList, Map<String, Set<String>> variables) {
//		for(SuperEntityClass methodDec : methodDeclarationList) {
//			MethodDeclarationObject mdo = (MethodDeclarationObject) methodDec;
//			getVariablesFromSuperEntityList(mdo.getArrayList(), variables);
//			getVariablesFromSuperEntityList(mdo.getGenericsList(), variables);
//			getVariablesFromSuperEntityList(mdo.getPrimitiveList(), variables);
//			getVariablesFromSuperEntityList(mdo.getSimpleList(), variables);
//
//			if(mdo.getMethodDeclarationList().size() > 0) {
//				getVariablesFromMethodDeclaration(mdo.getMethodDeclarationList(), variables);
//			}
//		}
//	}
//
////	private static void makeMethodDeclarationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
////		SolrInputDocument methodDecSolrDoc = new SolrInputDocument();
////		MethodDeclarationObject mdo = (MethodDeclarationObject) entity;
////
////		String idDec =  id + "&methodStart=" + entity.getStartCharacter() + "&methodEnd=" + entity.getEndCharacter();
////		methodDecSolrDoc.addField("id", idDec);
////		methodDecSolrDoc.addField(IndexManager.EXPAND_ID, id);
////
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAME, mdo.getFullyQualifiedName());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAME_DELIMITED, mdo.getName());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_START, ((Number)mdo.getLineNumber()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_END, ((Number)mdo.getEndLine()).longValue());
////
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_ABSTRACT, mdo.getIsAbstract());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_CONSTRUCTOR, mdo.getIsConstructor());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC, mdo.getIsGenericType());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_STATIC, mdo.getIsStatic());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_VAR_ARGS, mdo.getIsVarargs());
////
////		methodDecSolrDoc.addField("parent",false);
////		methodDecSolrDoc.addField("is_method_dec_child",true);
////
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PATH_COMPLEXITY, ((Number)mdo.getCyclomaticComplexity()).longValue());
////
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_WHILE_COUNT, ((Number)mdo.getWhileStatementList().size()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_FOR_COUNT, ((Number)mdo.getForStatementList().size()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IF_COUNT, ((Number)mdo.getIfStatementList().size()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_CASE_COUNT, ((Number)mdo.getSwitchCaseList().size()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_TERNARY_COUNT, ((Number)mdo.getConditionalExpressionList().size()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_CATCH_COUNT, ((Number)mdo.getCatchClauseList().size()).longValue());
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_LOGICAL_COUNT, ((Number)mdo.getInfixExpressionList().size()).longValue());
////
////		// methodDec.addField(IndexManager.SNIPPET_METHOD_DEC_IS_RECURSIVE, dec.isRecurisive);
////
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS, mdo.getDeclaringClass());
////
////		if(mdo.getDeclaringClass() != null){
////			String declaringClass = mdo.getDeclaringClass();
////
////			if(declaringClass.indexOf('<') > -1) {
////				String[] split1 = declaringClass.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				String declaringClassShort = split2[split2.length - 1] + "<" + split1[split1.length - 1];
////				methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, declaringClassShort);
////			}
////			else {
////				String[] split3 = declaringClass.split("[.]");
////				methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, split3[split3.length-1]);
////			}
////		}
////
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_RETURN_TYPE, mdo.getReturnType());
////
////		int localVariableCount = mdo.getArrayList().size() + mdo.getGenericsList().size() + mdo.getPrimitiveList().size() + mdo.getSimpleList().size() - mdo.getParametersList().size();
////		methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES, ((Number)localVariableCount).longValue());
////
////		for(String typeParam : mdo.getGenericParametersList()) {
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParam);
////		}
////
////		for(SuperEntityClass wc : mdo.getWildcardList()) {
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS, wc.getBound());
////		}
////
////		Map<String, Integer> paramCount = new HashMap<>();
////		Map<String, Integer> paramCountShort = new HashMap<>();
////
////		int parameterTypesListSize = mdo.getParameterTypesList().size();
////		for(int i = 0; i < parameterTypesListSize; i++) {
////			String argType = mdo.getParameterTypesList().get(i);
////
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES, argType);
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE, argType+"_"+i);
////
////			String shortName2;
////
////			if(argType.indexOf('<') > -1) {
////				String[] split1 = argType.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				shortName2 = split2[split2.length - 1] + "<" + split1[split1.length - 1];
////			}
////			else {
////				String[] split = argType.split("[.]");
////				shortName2 = split[split.length-1];
////			}
////
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, shortName2);
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE, shortName2+"_"+i);
////
////			if(paramCount.get(argType) == null) {
////				paramCount.put(argType, 1);
////			}
////			else {
////				int count = paramCount.get(argType) + 1;
////				paramCount.put(argType, count);
////			}
////
////			if(paramCountShort.get(shortName2) == null) {
////				paramCountShort.put(shortName2, 1);
////			}
////			else {
////				int count = paramCountShort.get(shortName2) + 1;
////				paramCountShort.put(shortName2, count);
////			}
////		}
////
////		for (Map.Entry<String, Integer> entry : paramCount.entrySet()) {
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT, entry.getKey() + "_" + entry.getValue());
////		}
////
////		for(Map.Entry<String, Integer> entry : paramCountShort.entrySet()) {
////			methodDecSolrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT, entry.getKey() + "_"+ entry.getValue());
////		}
////
////		CHILD_COUNT++;
////		solrDoc.addChildDocument(methodDecSolrDoc);
////	}
////
////	private static void makeMethodInvocationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
////		SolrInputDocument methodInvSolrDoc = new SolrInputDocument();
////		MethodInvocationObject mio = (MethodInvocationObject) entity;
////
////		methodInvSolrDoc.addField("parent",false);
////		methodInvSolrDoc.addField("is_method_invocation_child",true);
////
////		String idInvo = id + "&start=" + entity.getStartCharacter() + "&end=" + entity.getEndCharacter();
////		methodInvSolrDoc.addField("id", idInvo);
////		methodInvSolrDoc.addField(IndexManager.EXPAND_ID, id);
////
////		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME, mio.getFullyQualifiedName());
////		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAME_DELIMITED, mio.getName());
////		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_START, ((Number)mio.getLineNumber()).longValue());
////		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_END, ((Number)mio.getEndLine()).longValue());
////
////		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS, mio.getCallingClass());
////
////		if(!mio.getCallingClass().isEmpty()){
////			String callingClass = mio.getCallingClass();
////
////			if(callingClass.indexOf('<') > -1) {
////				String[] split1 = callingClass.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
////			}
////			else {
////				String[] split = callingClass.split("[.]");
////				methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, split[split.length-1]);
////			}
////		}
////
////		methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS, mio.getDeclaringClass());
////
////		if(!mio.getDeclaringClass().isEmpty()){
////			String declaringClass = mio.getDeclaringClass();
////
////			if(declaringClass.indexOf('<') > -1) {
////				String[] split1 = declaringClass.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
////			}
////			else {
////				String[] split = declaringClass.split("[.]");
////				methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split[split.length-1]);
////			}
////		}
////
////		Map<String, Integer> paramCount = new HashMap<>();
////		Map<String, Integer> paramCountShort = new HashMap<>();
////
////		int place = 0;
////		for(String argType: mio.getArgumentTypes()){
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES, argType);
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE, argType + "_" + place);
////
////			String shortName2;
////			if(argType.indexOf('<') > -1) {
////				String[] split1 = argType.split("<", 2);
////				String[] split2 = split1[0].split("[.]");
////				shortName2 = split2[split2.length - 1] + "<" + split1[split1.length - 1];
////			}
////			else {
////				String[] split = argType.split("[.]");
////				shortName2 = split[split.length - 1];
////			}
////
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT, shortName2);
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE, shortName2 + "_" + place);
////
////			if(paramCount.get(argType) == null) {
////				paramCount.put(argType, 1);
////			}
////			else {
////				int count = paramCount.get(argType)+1;
////				paramCount.put(argType, count);
////			}
////
////			if(paramCountShort.get(shortName2) == null) {
////				paramCountShort.put(shortName2, 1);
////			}
////			else {
////				int count = paramCountShort.get(shortName2)+1;
////				paramCountShort.put(shortName2, count);
////			}
////			place++;
////		}
////
////		for(Map.Entry<String, Integer> entry : paramCount.entrySet()) {
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT, entry.getKey() + "_" + entry.getValue());
////		}
////
////		for(Map.Entry<String, Integer> entry : paramCountShort.entrySet()) {
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT, entry.getKey() + "_" + entry.getValue());
////		}
////
////		for(Object argValue: mio.getArguments()){
////			methodInvSolrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_ARG_VALUES, argValue.toString());
////		}
////
////		CHILD_COUNT++;
////		solrDoc.addChildDocument(methodInvSolrDoc);
////	}
//
//	private static String makeGravaterURL(String authorEmail) {
//		if(authorEmail == null)
//			return "";
//
//		String md5 = md5Java(authorEmail);
//		return "http://www.gravatar.com/avatar/"+md5;
//	}
//
//	private static String md5Java(String message){
//		String digest = null;
//		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//
//			byte[] hash = md.digest(message.getBytes("UTF-8")); //converting byte array to Hexadecimal
//			StringBuilder sb = new StringBuilder(2*hash.length);
//			for(byte b : hash){
//				sb.append(String.format("%02x", b&0xff));
//			}
//			digest = sb.toString();
//		} catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
//			ex.printStackTrace();
//		}
//		return digest;
//	}
//
//	// todo is this kind of like my old "init" function that started everythimg?
////	/**
////	 * Gets all classes, method declarations, and method invocation from FileModel
////	 */
////	private static void createSolrDocs() {
////		for(JavaClass jc : fileModel.getJavaClassList()) {
////			JavaFile jf = gitData.getJavaFile();
////
////			if(successfulUpload) {
////				IndexManager.getInstance().makeClassSolrDoc(jc, jf);
////			}
////			else {
////				return;
////			}
////		}
////	}
//
//	/**
//	 * Creates AST and gathers Git data for each file
//	 * Runs createSolrDoc() method
//	 *
//	 * @param parentNode x
//	 * @param topDirectoryLocation x
//	 */
////	private static void runASTandGitData(File parentNode, String topDirectoryLocation) throws IOException, CoreException, ParseException {
//////		fileModel = new FileModel();
//////		fileModel = fileModel.parseDeclarations(parentNode.getAbsolutePath());
////
////		// TODO
////		ASTRefactor astObject = new ASTRefactor("path to config");
////		fileModel = astObject.getFileModel();
////
////		if(fileModel == null) {
////			successfulUpload = false;
////			return;
////		}
////
////		// get social data
////		if(fileModel.getJavaClassList().size() > 0) {
////			gitData = new GitData();
////			gitData.getCommitDataPerFile(topDirectoryLocation, parentNode.getAbsolutePath());
////			if(successfulUpload) {
////				createSolrDocs();
////			}
////			else {
////				return;
////			}
////		}
////
////		fileModel = null;
////		gitData = null;
////	}
//
//
//
//	/**
//	 * Resets IndexManager, gathers information about the repo, and then traverses the repo to find Java files
//	 *
//	 * @param topDirectoryLocation x
//	 * @param URL x
//	 */
////	private static void processRepository(String topDirectoryLocation, String URL) throws IOException, CoreException, ParseException {
////		try {
////			IndexManager.getInstance().processRepo(topDirectoryLocation, URL);
////
////			traverseUntilJava(new File(topDirectoryLocation), topDirectoryLocation);
////
////			if(successfulUpload) {
////				// delete repo URL line number from file if successful
////				try(
////					LineNumberReader lnr = new LineNumberReader(new FileReader(new File(crashListFileName)));
////					BufferedReader br = new BufferedReader(new FileReader(new File(crashListFileName)));
////					BufferedWriter bw = new BufferedWriter(new FileWriter(new File(crashListFileName)))
////				) {
////					// get number of lines in a file
////					// actual number = lnr.getLineNumber() + 1
////
////					lnr.skip(Long.MAX_VALUE);
////
////					StringBuilder sb = new StringBuilder();
////
////					String line;
////					int count = 1;
////
////					while((line = br.readLine()) != null && count < lnr.getLineNumber()) {
////						sb.append(line);
////						sb.append(System.getProperty("line.separator"));
////						count++;
////					}
////
////					bw.write(sb.toString());
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		IndexManager.getInstance().currentProject = null;
////	}
//
//	/**
//	 * Takes in the path + URL map file and pairs the path with the URL
//	 *
//	 */
//
//	// todo
//	// we need to create one large file that holds the times of how long things took to upload
//	// print time right before downloading
//	// print time when upload starts
//	// print time when upload finishes
//	// print a delimiter between batches
//
//	// also need to just read in a file from the web instead of cloning a repositry
//
////	private static void readMapFile(File file, String pathToClonedRepos, int start, int end, boolean cloneRepo) throws CoreException, ParseException {
////    boolean path = false;
////    boolean url = false;
////    String[] arr = {"", ""};
////
////    try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
////      int count = start;
////
////      for(int i = 0; i < start - 1; i++) {
////        br.readLine();
////      }
////
////      for(String line; (line = br.readLine()) != null && count <= end;) {
////        if (line.startsWith("'")) {
////          arr[0] = line.replace("'", "") + "/";
////
////          if(!cloneRepo) {
////              arr[0] = arr[0].replaceFirst("./", pathToClonedRepos);
////          }
////
////          path = true;
////          count++;
////        } else if (line.startsWith("http")) {
////          arr[1] = line;
////          url = true;
////
////          // write repo URL line number to file
////          try(
////		        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(crashListFileName), true))
////	        ) {
////            bw.write(count + "\n");
////          } catch (IOException e) {
////            e.printStackTrace();
////          }
////
////          count++;
////        }
////
////        if(path && url) {
////          path = false;
////          url = false;
////
////          if(cloneRepo) {
////            File dir = new File("./clones/");
////            if(!dir.mkdirs()) {
////              throw new IllegalArgumentException("[ERROR]: failed to create directory");
////            }
////            String[] httpParts = arr[1].split("//", 2);
////            String[] name = arr[0].split("/");
////            ProcessBuilder pb = new ProcessBuilder("git", "clone", httpParts[0] + "//test:test@" + httpParts[1] + ".git", "./" + name[name.length - 1].replace("/", "") );
////            pb.directory(dir);
////
////            try {
////              Process proc = pb.start();
////              // TODO
////              // change to \r
////              System.out.println("Downloading " + name[name.length - 1].replace("/", ""));
////              proc.waitFor();
////              proc.destroy();
////            } catch (InterruptedException e) {
////							e.printStackTrace();
////						}
////
////            arr[0] = arr[0].replaceFirst("./", "./clones/");
////
////            successfulUpload = true;
////            processRepository(arr[0], arr[1]);
////
////            ProcessBuilder pb2 = new ProcessBuilder("rm", "-rf", dir.getAbsolutePath());
////            try {
////              Process proc2 = pb2.start();
////              proc2.waitFor();
////              proc2.destroy();
////            } catch (InterruptedException e) {
////              e.printStackTrace();
////            }
////
////            // TODO
////            if(successfulUpload) {
////              // run bash script to increment online counter
////              ProcessBuilder onlinepb = new ProcessBuilder("./incrementCounter.sh");
////              try {
////                Process proc = onlinepb.start();
////                proc.waitFor();
////                proc.destroy();
////              } catch (InterruptedException e) {
////                e.printStackTrace();
////              }
////            }
////          }
////          else {
////            successfulUpload = true;
////            processRepository(arr[0], arr[1]);
////          }
////
////          // add remaining Solr documents
////          try {
////            Solrj.getInstance(passwordFilePath).commitDocs(hostName, portNumber, collectionName);
////          } catch (Exception e) {
////            e.printStackTrace();
////            Solrj.getInstance(passwordFilePath).clearDocs();
////            successfulUpload = false;
////          }
////
////          if(successfulUpload) {
////            repoCount++;
////            //System.out.println(arr[0] + " | [" + repoCount + "]");
////          }
////          else {
////            System.out.println(arr[0] + " failed to process");
////          }
////        }
////      }
////    } catch (FileNotFoundException e) {
////      System.err.println("File not found!");
////      e.printStackTrace();
////    } catch (IOException e) {
////      e.printStackTrace();
////    }
////	}
//
//
//
//	// print timestamp to file
//	private static void printTimestampToFile(String fileName, String key) {
//		try(PrintWriter pw = new PrintWriter(new FileOutputStream(fileName, true))) {
//			pw.write(key + "_&_" + System.currentTimeMillis());
//			pw.write(System.lineSeparator());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//	// writes URL contents to file
//	private static void urlToFile(String urlAsString, String outputFileLocation) {
//		try {
//			URL url = new URL(urlAsString);
//			BufferedReader githubBr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//			for (String javaLine; (javaLine = githubBr.readLine()) != null; ) {
//				try(PrintWriter pw = new PrintWriter(new FileOutputStream(outputFileLocation, true))) {
//					pw.write(javaLine);
//					pw.write(System.lineSeparator());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			githubBr.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//
//
//
//
//
//
//
//
//	//	private static void makeClassSolrDoc(SuperEntityClass entity, JavaFile javaFile) {
////		SolrInputDocument solrDoc = new SolrInputDocument();
////
////		File file = new File(javaFile.getFileLocation());
////
////		JavaClass jc = (JavaClass) entity;
////
////
////    todo this is the stuff left over from commenting
////
//
////
//
////
//
////
//
////
//
////
//
////
//
//
////
//
////		String githubAddress = this.toGitHubAddress(currentProject.project_owner,currentProject.project_name, file, headCommit.getHashCode());
////		solrDoc.addField(IndexManager.SNIPPET_ADDRESS, githubAddress);
////
////		String id = githubAddress+"?start="+jc.getStartCharacter()+"&end="+jc.getEndCharacter();
////		solrDoc.addField("id", id);
////		solrDoc.addField(IndexManager.EXPAND_ID, id);
////
//
////
//
////
//
////	}
//
//	private static void addTechnicalData(SolrInputDocument solrDoc, JavaClass jc) {
//		// we need jc, or a place where
//		// todo: how do we incorporate the properties here?
//		// todo: are the properties only used in mining?
//
//		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_LOWER_BOUND, ((Number)jc.getEndLine()).longValue());
//		solrDoc.addField(IndexManager.SNIPPET_ADDRESS_UPPER_BOUND, ((Number)jc.getLineNumber()).longValue());
//		solrDoc.addField(IndexManager.SNIPPET_CODE, jc.getSourceCode());
//		solrDoc.addField(IndexManager.SNIPPET_HAS_JAVA_COMMENTS, jc.getHasComments());
//		solrDoc.addField(IndexManager.SNIPPET_IS_ABSTRACT, jc.getIsAbstract());
//		solrDoc.addField(IndexManager.SNIPPET_IS_ANONYMOUS, jc.getIsAnonymous());
//		solrDoc.addField(IndexManager.SNIPPET_IS_GENERIC, jc.getIsGenericType());
//		solrDoc.addField(IndexManager.SNIPPET_IS_INNERCLASS, jc.getIsInnerClass());
//		boolean has_wildcard_method = false;
//		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
//			if(((MethodDeclarationObject) md).getWildcardList().size() > 0) {
//				has_wildcard_method =  true;
//				break;
//			}
//		}
//		solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD, has_wildcard_method);
//		if(has_wildcard_method) {
//			for(SuperEntityClass md : jc.getMethodDeclarationList()) {
//				for(SuperEntityClass wild : ((MethodDeclarationObject)md).getWildcardList()) {
//					solrDoc.addField(IndexManager.SNIPPET_IS_WILDCARD_BOUNDS, wild.getBound());
//				}
//			}
//		}
//		for(SuperEntityClass importStr : jc.getImportList()) {
//			solrDoc.addField(IndexManager.SNIPPET_IMPORTS, importStr.getFullyQualifiedName());
//			String[] split = importStr.getFullyQualifiedName().split("[.]");
//			solrDoc.addField(IndexManager.SNIPPET_IMPORTS_SHORT, split[split.length - 1]);
//		}
//		solrDoc.addField(IndexManager.SNIPPET_IMPORTS_COUNT, ((Number)jc.getImportList().size()).longValue());
//		for(String interfaceStr : jc.getImplements()) {
//			solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS, interfaceStr);
//			if(interfaceStr.indexOf('<') > -1) {
//				String[] split1 = interfaceStr.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = interfaceStr.split("[.]");
//				solrDoc.addField(IndexManager.SNIPPET_IMPLEMENTS_SHORT, split[split.length - 1]);
//			}
//		}
//		solrDoc.addField(IndexManager.SNIPPET_EXTENDS, jc.getSuperClass());
//		if(jc.getSuperClass() != null){
//			String superClass = jc.getSuperClass();
//			if(superClass.indexOf('<') > -1) {
//				String[] split1 = superClass.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = superClass.split("[.]");
//				solrDoc.addField(IndexManager.SNIPPET_EXTENDS_SHORT, split[split.length - 1]);
//			}
//		}
//		solrDoc.addField(IndexManager.SNIPPET_PACKAGE, jc.getPackage().getFullyQualifiedName());
//		if(jc.getPackage().getFullyQualifiedName() != null){
//			String packageName = jc.getPackage().getFullyQualifiedName();
//			if(packageName.indexOf('<') > -1) {
//				String[] split1 = packageName.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = packageName.split("[.]");
//				solrDoc.addField(IndexManager.SNIPPET_PACKAGE_SHORT, split[split.length - 1]);
//			}
//		}
//
//		// todo not sure what this is for
//		int complexity = jc.getCyclomaticComplexity();
//
//		solrDoc.addField(IndexManager.SNIPPET_PATH_COMPLEXITY_SUM, ((Number)complexity).longValue());
//		int methodInvCount = jc.getTotalMethodInvocationCount();
//		int codeSize = jc.getSourceCode().length();
//		if(methodInvCount == 0) methodInvCount = 1;
//		if(codeSize == 0) codeSize = 1;
//		solrDoc.addField(IndexManager.SNIPPET_COMPLEXITY_DENSITY, (double)jc.getCyclomaticComplexity() * (1 / (double)methodInvCount) * (1 / (double)codeSize));
//		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FIELDS, ((Number)jc.getGlobalList().size()).longValue());
//		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_FUNCTIONS, ((Number)jc.getMethodDeclarationList().size()).longValue());
//		Set<String> methodDecNames = new HashSet<>(jc.getMethodDeclarationNames());
//		for(String name : methodDecNames) {
//			solrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_NAMES, name);
//		}
//		Set<String> methodInvNames = new HashSet<>(jc.getMethodInvocationNames());
//		for(String name : methodInvNames) {
//			solrDoc.addField(IndexManager.SNIPPET_METHOD_INVOCATION_NAMES, name);
//		}
//		for(String typeParameter: jc.getGenericParametersList()){
//			solrDoc.addField(IndexManager.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParameter);
//		}
//		solrDoc.addField(IndexManager.SNIPPET_SIZE, jc.getSourceCode().length());
//		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_LINES, ((Number)(jc.getEndLine() - jc.getLineNumber() + 1)).longValue());
//
//		// this could be social data instead?
//		for(JavaClass cl : fileModel.getJavaClassList()) {
//			try {
//				if(cl.getName() != null && cl.getName().equals(jc.getContainingClass())) {
//					// todo seems like the containingID is just the line that was originally passed in
//					String containingID = githubAddress + "?start=" + cl.getLineNumber() + "&end=" + cl.getEndLine();
//					solrDoc.addField(SNIPPET_CONTAINING_CLASS_ID, containingID);
//					solrDoc.addField(IndexManager.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)cl.getCyclomaticComplexity()).longValue());
//					break;
//				}
//			} catch (NullPointerException e) {
//				String containingID = githubAddress + "?start=" + jc.getLineNumber() + "&end=" + jc.getEndLine();
//				solrDoc.addField(SNIPPET_CONTAINING_CLASS_ID, containingID);
//				solrDoc.addField(IndexManager.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)jc.getCyclomaticComplexity()).longValue());
//				break;
//			}
//		}
//
//		// method declaration is right below this current method
//		Map<String, Set<String>> variables = new HashMap<>();
//		{
//			variables.put("variableTypes", new HashSet<>());
//			variables.put("variableTypesShort", new HashSet<>());
//			variables.put("variableNames", new HashSet<>());
//		}
//
//		// TODO
//		getVariablesFromSuperEntityList(jc.getArrayList(), variables);
//
//		// method declaration is right below this current method
//		getVariablesFromMethodDeclaration(jc.getMethodDeclarationList(), variables);
//
//		for(String variableTypes : variables.get("variableTypes")) {
//			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES, variableTypes);
//		}
//		for(String variableTypesShort : variables.get("variableTypesShort")) {
//			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_TYPES_SHORT, variableTypesShort);
//		}
//		for(String variableNames : variables.get("variableNames")) {
//			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES, variableNames);
//			solrDoc.addField(IndexManager.SNIPPET_VARIABLE_NAMES_DELIMITED, variableNames);
//		}
//	}
//
//
//
//	// what happens when there are files with the same name in different directories?
//
//	/**
//	 *
//	 * @param parentFile
//	 * @param fileName
//	 * @return
//	 */
//	private static String lookForFileInDirectory(File parentFile, String fileName) {
//		Stack<File> fileStack = new Stack<>();
//		fileStack.push(parentFile);
//
//		while(!fileStack.isEmpty()) {
//			File topOfStack = fileStack.pop();
//
//			if(topOfStack.isDirectory()) {
//				File[] childrenFiles = parentFile.listFiles();
//
//				if(childrenFiles != null) {
//					for(File child : childrenFiles) {
//						if(!child.getName().startsWith(".")) fileStack.push(child);
//					}
//				}
//			}
//			else if(topOfStack.getName().equals(fileName)) {
//				return topOfStack.getAbsolutePath();
//			}
//		}
//
//		throw new IllegalArgumentException("[ERROR]: " + fileName + " not found in the cloned repo!");
//	}
//
//
//
//	/**
//	 *
//	 * @param solrDoc
//	 * @param clone
//	 */
//	private static void addSocialData(SolrInputDocument solrDoc, ClonedRepository clone) {
//		// todo check to make sure that the traverseRepos produces the correct string
//		JavaGitHubData jghd = new JavaGitHubData(lookForFileInDirectory(new File(clone.getLocalDirectory()), clone.getFileName()));
//		for(String author : jghd.getUniqueAuthors()) {
//			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHORS, author);
//		}
//		solrDoc.addField(IndexManager.SNIPPET_AUTHOR_COUNT, ((Number)jghd.getUniqueAuthors().size()).longValue());
//		for(String email : jghd.getUniqueEmails()) {
//			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_AVATARS, makeGravaterURL(email));
//			solrDoc.addField(IndexManager.SNIPPET_ALL_AUTHOR_EMAILS, email);
//		}
//
//		Commit headCommit = jghd.getListOfCommits().get(jghd.getListOfCommits().size() - 1);
//		solrDoc.addField(IndexManager.AUTHOR_NAME, headCommit.getAuthor());
//		solrDoc.addField(IndexManager.AUTHOR_EMAIL, headCommit.getEmail());
//		solrDoc.addField(IndexManager.AUTHOR_AVATAR, makeGravaterURL(headCommit.getEmail()));
//		for(Commit cd : jghd.getListOfCommits()) {
//			solrDoc.addField(IndexManager.SNIPPET_ALL_COMMENTS, cd.getMessage());
//			solrDoc.addField(IndexManager.SNIPPET_ALL_DATES, cd.getSolrDate());
//			solrDoc.addField(IndexManager.SNIPPET_ALL_VERSIONS, cd.getHashCode());
//		}
//		solrDoc.addField("month", headCommit.getMonth());
//		solrDoc.addField("day", headCommit.getDay());
//		solrDoc.addField("year", headCommit.getYear());
//		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_INSERTIONS, ((Number)headCommit.getInsertions()).longValue());
//		solrDoc.addField(IndexManager.SNIPPET_NUMBER_OF_DELETIONS, ((Number)headCommit.getDeletions()).longValue());
//		solrDoc.addField(IndexManager.SNIPPET_INSERTION_CODE_CHURN, (double)headCommit.getInsertions() / jghd.getNumberOfLines());
//		solrDoc.addField(IndexManager.SNIPPET_DELETED_CODE_CHURN, (double)headCommit.getDeletions() / jghd.getNumberOfLines());
//		solrDoc.addField(IndexManager.SNIPPET_INSERTION_DELETION_CODE_CHURN, (double)(headCommit.getInsertions() + headCommit.getDeletions()) / jghd.getNumberOfLines());
//		solrDoc.addField(IndexManager.SNIPPET_THIS_VERSION, headCommit.getHashCode());
//		solrDoc.addField(IndexManager.SNIPPET_VERSION_COMMENT, headCommit.getMessage());
//		solrDoc.addField(IndexManager.SNIPPET_LAST_UPDATED, headCommit.getSolrDate());
//		int totalInsertions = 0;
//		int totalDeletions = 0;
//		for(Commit cd : jghd.getListOfCommits()) {
//			totalInsertions += cd.getInsertions();
//			totalDeletions += cd.getDeletions();
//		}
//		solrDoc.addField(IndexManager.SNIPPET_TOTAL_INSERTIONS, totalInsertions);
//		solrDoc.addField(IndexManager.SNIPPET_TOTAL_DELETIONS, totalDeletions);
//	}
//
//
//
//	/**
//	 *
//	 * @param jc
//	 * @param fullURL
//	 * @return
//	 */
//	private static SolrInputDocument createEmptyClassSolrDoc(JavaClass jc, String fullURL) {
//		SolrInputDocument classDoc = new SolrInputDocument();
//
//		classDoc.addField(IndexManager.SNIPPET_NAME, jc.getFullyQualifiedName());
//		classDoc.addField(IndexManager.SNIPPET_NAME_DELIMITED, jc.getName());
//		classDoc.addField(IndexManager.SNIPPET_ADDRESS, fullURL.split("\\?start=")[0]);
//		classDoc.addField("id", fullURL);
//		classDoc.addField(IndexManager.EXPAND_ID, fullURL);
//		classDoc.addField(IndexManager.SNIPPET_GRANULARITY, "Class");
//		classDoc.addField("parent", true);
//
//		return classDoc;
//	}
//
//
//
//	/**
//	 *
//	 * @param URLFilePath
//	 */
//	private static void init(String URLFilePath) {
//		// for each line of URL file
//		// read the URL to file
//		// anyway, create the JavaClass object from the SimilarityASTWalker
//		// then print to file the current time in milliseconds
//		// don't forget to catch things when uploading fails
//
//		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(URLFilePath), "UTF-8"))) {
//			for(String line; (line = br.readLine()) != null; ) { // todo does not loop because of the system.exit
//				// print time before download
//				printTimestampToFile("resources/times.txt", line);
//
//				// convert url to .java file
//				String[] urlSplit = line.split("/");
//				String outputFileLocation = "resources/" + urlSplit[urlSplit.length - 1].split("\\?start=")[0];
//				urlToFile(line, outputFileLocation);
//
//				// file is done being written to here with location outputFileLocation
//				// do SimilarityASTWalker and get the file model
//				ASTRefactor ar = new ASTRefactor("resources/astconfig.properties");
//				ar.parseFile(outputFileLocation);
//				if(ar.getFileModel() == null) {
//					throw new IllegalArgumentException("[ERROR]: the file model is null!");
//				}
//
//
//
//				// todo do i need to time how long the cloning took? probably
//				ClonedRepository clone = new ClonedRepository(line, "https://test:test@github.com/" + urlSplit[3] + "/" + urlSplit[4] + ".git", "resources/" + urlSplit[4]);
//				clone.cloneRepository();
//
//
//
//				// todo
//				// 1. function to create solr docs (empty)
//				// 2. add technical data
//				// 3. add social data
//				List<SolrInputDocument> classSolrDocList = new ArrayList<>();
//				List<SolrInputDocument> methodDecSolrDocList = new ArrayList<>();
//
//				for(JavaClass jc : ar.getFileModel().getJavaClassList()) {
//					SolrInputDocument classSolrDoc = createEmptyClassSolrDoc(jc, line);
//
//					// we need to check the url
//					// need to hit the url on the grok server
//					// problem is that the grok server is not up
//					addProjectData(classSolrDoc, clone.getLocalDirectory(), /* something */);
//
//					// if add social data is true
//					// this comes from cloning
//					addSocialData(classSolrDoc, clone);
//
//					// if add technical data is true
//					addTechnicalData(classSolrDoc, jc);
//
//
//					classSolrDocList.add(classSolrDoc);
//
//
//					System.exit(0);
//
//
//					// todo
//					// do not pass this line! (at least not yet)
//
//					// todo: what if we have classSolrDoc objects and methodDecSolrObjects as other classes?
//					// todo: we could overload functions so the syntax is nicer
//					// but idk what the point of that would be
//
//
//					for(SuperEntityClass md : jc.getMethodDeclarationList()) {
//						// findAllMethodDeclarations((MethodDeclarationObject)md, classSolrDoc, line);
//
//						// recursively find inner method decs
//						// create emptyMethodSolrDoc
//						// add social
//						// add technical
//					}
//
//
//
//					// todo need to add multiple classes and method decs
//
//					Solrj.getInstance(passwordFilePath).addDoc(classSolrDoc);
//
//					try {
//						if(Solrj.getInstance(passwordFilePath).req.getDocuments().size() >= MAXDOC || CHILD_COUNT >= MAX_CHILD_DOC) {
//							Solrj.getInstance(passwordFilePath).commitDocs(hostName, portNumber, collectionName);
//						}
//					} catch (Exception e) {
//			      Solrj.getInstance(passwordFilePath).clearDocs();
//			      successfulUpload = false;
//					}
//
//				}
//
//
//
//				System.exit(0);
//
//
//				// first, transform the url to match the repo
//
//
//
//				// second, need to clone the damn thing
//
//
//
//				// do upload
//
//
//
//				// should i print deletion time? :/
//
//				// delete repo files
//				clone.deleteCloneDirectory();
//
//				// delete .java file
//				if(!new File(outputFileLocation).delete()) {
//					throw new IllegalArgumentException("[ERROR]: " + outputFileLocation + " failed to delete!");
//				}
//
//				// TODO
//				// i need to upload the remaining solr docs somewhere
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static int setStartLineNumber(int alternateStartLineNumber, int processNumber, int linesPerProcess) {
//		int startLineNumber = (linesPerProcess * processNumber) + 1;
//		int endLineNumber = linesPerProcess * (processNumber + 1);
//
//		if(alternateStartLineNumber > 0) {
//			// alternate start line number should be odd
//			if(alternateStartLineNumber % 2 == 1 && alternateStartLineNumber < endLineNumber) {
//				startLineNumber = alternateStartLineNumber;
//			}
//			else {
//				throw new IllegalArgumentException("Something is wrong with the alternate starting line!");
//			}
//		}
//
//		return startLineNumber;
//	}
//
//	private static int setEndLineNumber(int alternateEndLineNumber, int processNumber, int linesPerProcess, int maxLine) {
//		int startLineNumber = (linesPerProcess * processNumber) + 1;
//		int endLineNumber = linesPerProcess * (processNumber + 1);
//
//		if(alternateEndLineNumber > 0) {
//			if(alternateEndLineNumber % 2 == 0 && alternateEndLineNumber > startLineNumber && alternateEndLineNumber < maxLine) {
//				endLineNumber = alternateEndLineNumber;
//			}
//			else {
//				throw new IllegalArgumentException("Something is wrong with the alternate ending line!");
//			}
//		}
//
//		return endLineNumber;
//	}
//
//	private static void validateProperties(Properties prop) {
//		StringBuilder errors = new StringBuilder();
//
//		if(prop.getProperty("hostName").isEmpty()) {
//			errors.append("\nHost name is not defined!");
//		}
//
//		if(prop.getProperty("collectionName").isEmpty()) {
//			errors.append("\nCore name is not defined!");
//		}
//
//		if(prop.getProperty("portNumber").isEmpty()) {
//			errors.append("\nPort number is not defined!");
//		}
//
//		if(prop.getProperty("passPath").isEmpty()) {
//			errors.append("Password file path is not defined!");
//		}
//
//		if(prop.getProperty("cloneFlag").isEmpty()) {
//			errors.append("Clone flag is not defined!");
//		}
//
//		if(prop.getProperty("repoPath").isEmpty()) {
//			errors.append("Repo path is not defined!");
//		}
//
//		if(prop.getProperty("pathToURLMapPath").isEmpty()) {
//			errors.append("PathToURLMap file path is not defined!");
//		}
//
//		if(prop.getProperty("crashListPath").isEmpty()) {
//			errors.append("Crash list file path is not defined!");
//		}
//
//		if(prop.getProperty("processNumber").isEmpty()) {
//			errors.append("Process number is not defined!");
//		}
//
//		if(prop.getProperty("totalNumberOfProcesses").isEmpty()) {
//			errors.append("Total number of processes is not defined!");
//		}
//
//		if(Integer.parseInt(prop.getProperty("processNumber")) < 0 || Integer.parseInt(prop.getProperty("totalNumberOfProcesses")) < 0) {
//			errors.append("Negative arguments are not allowed!");
//		}
//
//		if(Integer.parseInt(prop.getProperty("processNumber")) > Integer.parseInt(prop.getProperty("totalNumberOfProcesses"))) {
//			errors.append("Process number cannot exceed total number of processes!");
//		}
//
//		if(errors.toString().length() > 0) {
//			throw new IllegalArgumentException(errors.toString());
//		}
//	}
//
//	public static void main(String[] args) {
//		// this is the actual
//		// File configFile = new File(args[0]);
//
//
//
//		// just for testing
//		// todo this properties file should be the one that tells which things to mine for (the whole bitvector thing)
//		// todo maybe we should just take the bitvector as input?
////		String configPath = "resources/config.properties";
////
////		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configPath)))) {
////			properties = new HashMap<>();
////
////			for(String line; (line = br.readLine()) != null; ) {
////				if(!line.startsWith("#")) {
////					properties.put(line.split("=")[0], Boolean.parseBoolean(line.split("=")[1]));
////				}
////			}
////		} catch (IOException e) {
////			throw new IllegalArgumentException("[ERROR]: config file not found!");
////		}
////
////
////		Properties prop = new Properties();
////		try(InputStream input = new FileInputStream(configPath)) {
////			prop.load(input);
////
////			if(prop.getProperty("alternateStartLine").isEmpty()) {
////				prop.setProperty("alternateStartLine", "-1");
////			}
////
////			if(prop.getProperty("alternateEndLine").isEmpty()) {
////				prop.setProperty("alternateEndLine", "-1");
////			}
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////
////		validateProperties(prop);
//
//		/*
//
//		// math to calculate start/end lines [must start on odd number and end on even number]
//		final int PATH_TO_URL_MAP_LINE_COUNT = 576474;
//		int linesPerProcess = Math.round(PATH_TO_URL_MAP_LINE_COUNT / Integer.parseInt(prop.getProperty("totalNumberOfProcesses")) + 2) / 2 * 2;
//		int startLineNumber = setStartLineNumber(Integer.parseInt(prop.getProperty("alternateStartLine")), Integer.parseInt(prop.getProperty("processNumber")), linesPerProcess);
//		int endLineNumber = setEndLineNumber(Integer.parseInt(prop.getProperty("alternateEndLine")), Integer.parseInt(prop.getProperty("processNumber")), linesPerProcess, PATH_TO_URL_MAP_LINE_COUNT);
//
//		fileModel = null;
//		gitData = null;
//		crashListFileName = null;
//
//		IndexManager.getInstance().currentProject = null;
//
//		// create crashList file
//		crashListFileName = prop.getProperty("crashListPath") + "crashList_" + System.currentTimeMillis() / 1000L + ".txt";
//
//		File file = new File(crashListFileName);
//		if(!file.createNewFile()) {
//			throw new IllegalArgumentException("[ERROR]: could not create file");
//		}
//
//		// set path to URL map
//		File pathToURLMap = new File(prop.getProperty("pathToURLMapPath"));
//
//		// start everything
//		// readMapFile(pathToURLMap, prop.getProperty("repoPath"), startLineNumber, endLineNumber, Boolean.parseBoolean(prop.getProperty("cloneFlag")));
//
//		*/
//
//
//
//		// TODO
//		// init("resources/urls.txt");
//
//		System.out.println("----------------------------------------------------------");
//		System.out.println("Finished " + repoCount + " repositories");
//		System.out.println("----------------------------------------------------------");
//	}
//}