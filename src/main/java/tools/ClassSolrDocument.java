///* *****************************************************************************
// * Copyright (c) {2017} {Software Design and Collaboration Laboratory (SDCL)
// *				, University of California, Irvine}.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *    {Software Design and Collaboration Laboratory (SDCL)
// *	, University of California, Irvine}
// *			- initial API and implementation and/or initial documentation
// *******************************************************************************/
//
///*
// * Created by Thomas Kwak
// */
//
//package tools;
//
//import entities.JavaClass;
//import entities.MethodDeclarationObject;
//import entities.SuperEntityClass;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrDocumentList;
//import org.apache.solr.common.SolrInputDocument;
//
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//
//class ClassSolrDocument {
//	private SolrInputDocument doc;
//	private Map<String, String> configProperties;
//	private Map<String, Boolean> simProperties;
//
//
//	/**
//	 * Constructor
//	 * @param jc xxx
//	 * @param fullURL xxx
//	 * @param simProperties xxx
//	 */
//	ClassSolrDocument(JavaClass jc, String fullURL, Map<String, String> configProperties, Map<String, Boolean> simProperties) {
//		this.doc = new SolrInputDocument();
//		this.doc.addField(SolrKey.SNIPPET_NAME, jc.getFullyQualifiedName());
//		this.doc.addField(SolrKey.SNIPPET_NAME_DELIMITED, jc.getName());
//		this.doc.addField(SolrKey.SNIPPET_ADDRESS, fullURL.split("\\?start=")[0]);
//		this.doc.addField("id", fullURL);
//		this.doc.addField(SolrKey.EXPAND_ID, fullURL);
//		this.doc.addField(SolrKey.SNIPPET_GRANULARITY, "Class");
//		this.doc.addField("parent", true);
//
//		this.configProperties = configProperties;
//		this.simProperties = simProperties;
//	}
//
//
//	/**
//	 * Get the SolrInputDocument
//	 * @return xxx
//	 */
//	SolrInputDocument getSolrDocument() {
//		return this.doc;
//	}
//
//
//
//
//
//
//	/**
//	 * Queries CodeExchange for project information
//	 * @param projectURL xxx
//	 */
//	void addProjectData(String projectURL) {
//		// todo check the htmlURL object
//		String htmlURL = "\""+projectURL+"\"";
//		String hostName = "codeexchange.ics.uci.edu";
//		int portNumber = 9001;
//		String collectionName = "githubprojects";
//
//		SolrDocumentList list = Solrj.getInstance(configProperties.get("passPath")).query("id:" + htmlURL, hostName, portNumber, collectionName, 1);
//
//		if(list.isEmpty()) throw new IllegalArgumentException("[ERROR]: project data is null!");
//
//		SolrDocument doc = list.get(0);
//		if(doc.getFieldValue("htmlURL") != null) this.doc.addField(SolrKey.PROJECT_ADDRESS, doc.getFieldValue("htmlURL").toString());
//		if(doc.getFieldValue("avatarURL") != null) this.doc.addField(SolrKey.PROJECT_OWNER_AVATAR, doc.getFieldValue("avatarURL").toString());
//		if(doc.getFieldValue("description") != null)this.doc.addField(SolrKey.PROJECT_DESCRIPTION, doc.getFieldValue("description").toString());
//		if(doc.getFieldValue("fork") != null) this.doc.addField(SolrKey.PROJECT_IS_FORK, doc.getFieldValue("fork").toString());
//		if(doc.getFieldValue("projectName") != null) this.doc.addField(SolrKey.PROJECT_NAME, doc.getFieldValue("projectName").toString());
//		if(doc.getFieldValue("siteAdmin") != null) this.doc.addField(SolrKey.AUTHOR_IS_SITE_ADMIN, doc.getFieldValue("siteAdmin").toString());
//		if(doc.getFieldValue("userName") != null) this.doc.addField(SolrKey.PROJECT_OWNER, doc.getFieldValue("userName").toString());
//		if(doc.getFieldValue("userType") != null) this.doc.addField(SolrKey.AUTHOR_TYPE, doc.getFieldValue("userType").toString());
//	}
//
//
//	/**
//	 * xxx
//	 * @param cloneDirectory xxx
//	 * @param fileRepoPath xxx
//	 * @param classFile xxx
//	 */
//	void addSocialData(String cloneDirectory, String fileRepoPath, File classFile) {
//		// todo check to make sure that the traverseRepos produces the correct string
//		JavaGitHubData jghd = new JavaGitHubData(fileRepoPath);
//		jghd.getCommits(cloneDirectory, fileRepoPath, classFile); // NEED COMMIT DATA FOR EVERYTHING
//
//		Commit headCommit = jghd.getListOfCommits().get(jghd.getListOfCommits().size() - 1);
//		this.doc.addField(SolrKey.AUTHOR_NAME, headCommit.getAuthor());
////		this.doc.addField(SolrKey.AUTHOR_EMAIL, headCommit.getEmail());
////		this.doc.addField(SolrKey.AUTHOR_AVATAR, makeGravaterURL(headCommit.getEmail()));
////		for(Commit cd : jghd.getListOfCommits()) {
////			this.doc.addField(SolrKey.SNIPPET_ALL_COMMENTS, cd.getMessage());
////			this.doc.addField(SolrKey.SNIPPET_ALL_DATES, cd.getSolrDate());
////			this.doc.addField(SolrKey.SNIPPET_ALL_VERSIONS, cd.getHashCode());
////		}
////		this.doc.addField("month", headCommit.getMonth());
////		this.doc.addField("day", headCommit.getDay());
////		this.doc.addField("year", headCommit.getYear());
//		this.doc.addField(SolrKey.SNIPPET_NUMBER_OF_INSERTIONS, ((Number)headCommit.getInsertions()).longValue());
//		this.doc.addField(SolrKey.SNIPPET_NUMBER_OF_DELETIONS, ((Number)headCommit.getDeletions()).longValue());
//		this.doc.addField(SolrKey.SNIPPET_INSERTION_CODE_CHURN, (double)headCommit.getInsertions() / jghd.getNumberOfLines());
//		this.doc.addField(SolrKey.SNIPPET_DELETED_CODE_CHURN, (double)headCommit.getDeletions() / jghd.getNumberOfLines());
//		this.doc.addField(SolrKey.SNIPPET_INSERTION_DELETION_CODE_CHURN, (double)(headCommit.getInsertions() + headCommit.getDeletions()) / jghd.getNumberOfLines());
////		this.doc.addField(SolrKey.SNIPPET_THIS_VERSION, headCommit.getHashCode());
////		this.doc.addField(SolrKey.SNIPPET_VERSION_COMMENT, headCommit.getMessage());
////		this.doc.addField(SolrKey.SNIPPET_LAST_UPDATED, headCommit.getSolrDate());
//		int totalInsertions = 0;
//		int totalDeletions = 0;
//		for(Commit cd : jghd.getListOfCommits()) {
//			totalInsertions += cd.getInsertions();
//			totalDeletions += cd.getDeletions();
//		}
//		this.doc.addField(SolrKey.SNIPPET_TOTAL_INSERTIONS, totalInsertions);
//		this.doc.addField(SolrKey.SNIPPET_TOTAL_DELETIONS, totalDeletions);
//		for(String author : jghd.getUniqueAuthors()) {
//			this.doc.addField(SolrKey.SNIPPET_ALL_AUTHORS, author);
//		}
////		this.doc.addField(SolrKey.SNIPPET_AUTHOR_COUNT, ((Number)jghd.getUniqueAuthors().size()).intValue());
////		for(String email : jghd.getUniqueEmails()) {
////			this.doc.addField(SolrKey.SNIPPET_ALL_AUTHOR_AVATARS, makeGravaterURL(email));
////			this.doc.addField(SolrKey.SNIPPET_ALL_AUTHOR_EMAILS, email);
////		}
//	}
//
//
//	void addTechnicalData(JavaClass jc, FileModel fm, String githubAddress) {
////		this.doc.addField(SolrKey.SNIPPET_ADDRESS_LOWER_BOUND, ((Number)jc.getEndLine()).longValue());
////		this.doc.addField(SolrKey.SNIPPET_ADDRESS_UPPER_BOUND, ((Number)jc.getLineNumber()).longValue());
////		this.doc.addField(SolrKey.SNIPPET_CODE, jc.getSourceCode());
////		this.doc.addField(SolrKey.SNIPPET_HAS_JAVA_COMMENTS, jc.getHasComments());
//		this.doc.addField(SolrKey.SNIPPET_IS_ABSTRACT, jc.getIsAbstract());
//		this.doc.addField(SolrKey.SNIPPET_IS_ANONYMOUS, jc.getIsAnonymous());
//		this.doc.addField(SolrKey.SNIPPET_IS_GENERIC, jc.getIsGenericType());
//		this.doc.addField(SolrKey.SNIPPET_IS_INNERCLASS, jc.getIsInnerClass());
//		boolean has_wildcard_method = false;
//		for(SuperEntityClass md : jc.getMethodDeclarationList()) {
//			if(((MethodDeclarationObject) md).getWildcardList().size() > 0) {
//				has_wildcard_method =  true;
//				break;
//			}
//		}
//		this.doc.addField(SolrKey.SNIPPET_IS_WILDCARD, has_wildcard_method);
//		if(has_wildcard_method) {
//			for(SuperEntityClass md : jc.getMethodDeclarationList()) {
//				for(SuperEntityClass wild : ((MethodDeclarationObject)md).getWildcardList()) {
//					this.doc.addField(SolrKey.SNIPPET_IS_WILDCARD_BOUNDS, wild.getBound());
//				}
//			}
//		}
//		for(SuperEntityClass importStr : jc.getImportList()) {
//			this.doc.addField(SolrKey.SNIPPET_IMPORTS, importStr.getFullyQualifiedName());
//			String[] split = importStr.getFullyQualifiedName().split("[.]");
//			this.doc.addField(SolrKey.SNIPPET_IMPORTS_SHORT, split[split.length - 1]);
//		}
//		this.doc.addField(SolrKey.SNIPPET_IMPORTS_COUNT, ((Number)jc.getImportList().size()).longValue());
//		for(String interfaceStr : jc.getImplements()) {
//			this.doc.addField(SolrKey.SNIPPET_IMPLEMENTS, interfaceStr);
//			if(interfaceStr.indexOf('<') > -1) {
//				String[] split1 = interfaceStr.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				this.doc.addField(SolrKey.SNIPPET_IMPLEMENTS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = interfaceStr.split("[.]");
//				this.doc.addField(SolrKey.SNIPPET_IMPLEMENTS_SHORT, split[split.length - 1]);
//			}
//		}
//		this.doc.addField(SolrKey.SNIPPET_EXTENDS, jc.getSuperClass());
//		if(jc.getSuperClass() != null){
//			String superClass = jc.getSuperClass();
//			if(superClass.indexOf('<') > -1) {
//				String[] split1 = superClass.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				this.doc.addField(SolrKey.SNIPPET_EXTENDS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = superClass.split("[.]");
//				this.doc.addField(SolrKey.SNIPPET_EXTENDS_SHORT, split[split.length - 1]);
//			}
//		}
//		this.doc.addField(SolrKey.SNIPPET_PACKAGE, jc.getPackage().getFullyQualifiedName());
//		if(jc.getPackage().getFullyQualifiedName() != null){
//			String packageName = jc.getPackage().getFullyQualifiedName();
//			if(packageName.indexOf('<') > -1) {
//				String[] split1 = packageName.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				this.doc.addField(SolrKey.SNIPPET_PACKAGE_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = packageName.split("[.]");
//				this.doc.addField(SolrKey.SNIPPET_PACKAGE_SHORT, split[split.length - 1]);
//			}
//		}
//
//		this.doc.addField(SolrKey.SNIPPET_PATH_COMPLEXITY_SUM, ((Number)jc.getCyclomaticComplexity()).longValue());
//		int methodInvCount = jc.getTotalMethodInvocationCount();
//		int codeSize = jc.getSourceCode().length();
//		if(methodInvCount == 0) methodInvCount = 1;
//		if(codeSize == 0) codeSize = 1;
//		this.doc.addField(SolrKey.SNIPPET_COMPLEXITY_DENSITY, (double)jc.getCyclomaticComplexity() * (1 / (double)methodInvCount) * (1 / (double)codeSize));
//		this.doc.addField(SolrKey.SNIPPET_NUMBER_OF_FIELDS, ((Number)jc.getGlobalList().size()).longValue());
//		this.doc.addField(SolrKey.SNIPPET_NUMBER_OF_FUNCTIONS, ((Number)jc.getMethodDeclarationList().size()).longValue());
//		Set<String> methodDecNames = new HashSet<>(jc.getMethodDeclarationNames());
//		for(String name : methodDecNames) {
//			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_NAMES, name);
//		}
//		Set<String> methodInvNames = new HashSet<>(jc.getMethodInvocationNames());
//		for(String name : methodInvNames) {
//			this.doc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_NAMES, name);
//		}
//		for(String typeParameter: jc.getGenericParametersList()){
//			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParameter);
//		}
//		this.doc.addField(SolrKey.SNIPPET_SIZE, jc.getSourceCode().length());
//		this.doc.addField(SolrKey.SNIPPET_NUMBER_OF_LINES, ((Number)(jc.getEndLine() - jc.getLineNumber() + 1)).longValue());
//
//		for(JavaClass cl : fm.getJavaClassList()) {
//			try {
//				if(cl.getName() != null && cl.getName().equals(jc.getContainingClass())) {
//					// todo seems like the containingID is just the line that was originally passed in
//					String containingID = githubAddress + "?start=" + cl.getLineNumber() + "&end=" + cl.getEndLine();
//					this.doc.addField(SolrKey.SNIPPET_CONTAINING_CLASS_ID, containingID);
//					this.doc.addField(SolrKey.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)cl.getCyclomaticComplexity()).longValue());
//					break;
//				}
//			} catch (NullPointerException e) {
//				String containingID = githubAddress + "?start=" + jc.getLineNumber() + "&end=" + jc.getEndLine();
//				this.doc.addField(SolrKey.SNIPPET_CONTAINING_CLASS_ID, containingID);
//				this.doc.addField(SolrKey.SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM, ((Number)jc.getCyclomaticComplexity()).longValue());
//				break;
//			}
//		}
//
//		Map<String, Set<String>> variables = new HashMap<>();
//		{
//			variables.put("variableTypes", new HashSet<>());
//			variables.put("variableTypesShort", new HashSet<>());
//			variables.put("variableNames", new HashSet<>());
//		}
//
//		getVariablesFromSuperEntityList(jc.getArrayList(), variables);
//		getVariablesFromMethodDeclaration(jc.getMethodDeclarationList(), variables);
//
//		for(String variableTypes : variables.get("variableTypes")) {
//			this.doc.addField(SolrKey.SNIPPET_VARIABLE_TYPES, variableTypes);
//		}
//		for(String variableTypesShort : variables.get("variableTypesShort")) {
//			this.doc.addField(SolrKey.SNIPPET_VARIABLE_TYPES_SHORT, variableTypesShort);
//		}
//		for(String variableNames : variables.get("variableNames")) {
//			this.doc.addField(SolrKey.SNIPPET_VARIABLE_NAMES, variableNames);
//			this.doc.addField(SolrKey.SNIPPET_VARIABLE_NAMES_DELIMITED, variableNames);
//		}
//	}
//
//
//	/**
//	 * xxx
//	 * @param methodDeclarationList xxx
//	 * @param variables xxx
//	 */
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
//
//	/**
//	 * xxx
//	 * @param list xxx
//	 * @param variables xxx
//	 */
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
//
//	/**
//	 * Utility function
//	 * @param authorEmail x
//	 * @return x
//	 */
//	private static String makeGravaterURL(String authorEmail) {
//		if(authorEmail == null)
//			return "";
//
//		String md5 = md5Java(authorEmail);
//		return "http://www.gravatar.com/avatar/"+md5;
//	}
//
//
//	/**
//	 * Utility function
//	 * @param message x
//	 * @return x
//	 */
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
//}
