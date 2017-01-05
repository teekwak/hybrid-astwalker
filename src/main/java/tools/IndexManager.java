/*******************************************************************************
 * Copyright (c) {2017} {Software Design and Collaboration Laboratory (SDCL)
 *				, University of California, Irvine}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {Software Design and Collaboration Laboratory (SDCL)
 *	, University of California, Irvine}
 *			- initial API and implementation and/or initial documentation
 *******************************************************************************/

/**
 * Created by Thomas Kwak
 */

package tools;

import AST.ASTWalker;
import entities.JavaClass;
import entities.MethodDeclarationObject;
import entities.MethodInvocationObject;
import entities.SuperEntityClass;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class IndexManager {
	private static IndexManager instance;
	private static Map<String, String> configProperties;
	private static Map<String, Boolean> astProperties;
	private static Map<String, Boolean> simProperties;

//	private static int MAXDOC = 300;
//	private static int MAX_CHILD_DOC = 4000;
//	static int CHILD_COUNT = 0;

	private static int MAXDOC = 1; // todo: we want to upload after each class doc
	private static int MAX_CHILD_DOC = 4000;
	static int CHILD_COUNT = 0;

	static IndexManager getInstance() {
		if(instance == null) {
			instance = new IndexManager();
		}

		return instance;
	}



	/**
	 * Quickly traverse repository to find a file based on a list of directories
	 * @param parentDirectory x
	 * @param fileName x
	 * @param pathToFileInRepo x
	 * @return x
	 */
	private static File findFileInRepository(String parentDirectory, String fileName, List<String> pathToFileInRepo) {
		File[] filesInDirectory = new File(parentDirectory).listFiles();

		for(String directoryName : pathToFileInRepo) {
			if(filesInDirectory != null) {
				for(File f : filesInDirectory) {
					if(f.getName().equals(directoryName)) {
						filesInDirectory = f.listFiles();
						break;
					}
				}
			}
		}

		if(filesInDirectory != null) {
			for(File f : filesInDirectory) {
				if(f.getName().equals(fileName)) {
					return f;
				}
			}
		}

		throw new IllegalArgumentException("[ERROR]: file does not exist in repository!");
	}









	/**
	 * Utility function
	 * @param authorEmail x
	 * @return x
	 */
//	private static String makeGravaterURL(String authorEmail) {
//		if(authorEmail == null)
//			return "";
//
//		String md5 = md5Java(authorEmail);
//		return "http://www.gravatar.com/avatar/"+md5;
//	}



	/**
	 * Utility function
	 * @param message x
	 * @return x
	 */
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







	/**
	 * Get the relative path to the file in the repository
	 * @param url x
	 * @return x
	 */
	private static String getRelativeFileRepoPath(String url) {
		String[] urlSplit = url.split("/");
		StringBuilder repoPath = new StringBuilder();
		for(int i = 6; i < urlSplit.length - 1; i++) {
			repoPath.append(urlSplit[i]);
			repoPath.append("/");
		}
		repoPath.append(urlSplit[urlSplit.length - 1].split("\\?")[0]);
		return repoPath.toString();
	}



	private static void makeMethodDeclarationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
		SolrInputDocument methodDecSolrDoc = new SolrInputDocument();
		MethodDeclarationObject mdo = (MethodDeclarationObject) entity;

		String idDec =  id + "&methodStart=" + entity.getStartCharacter() + "&methodEnd=" + entity.getEndCharacter();
		methodDecSolrDoc.addField("id", idDec);
		methodDecSolrDoc.addField(SolrKey.EXPAND_ID, id);

		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_NAME, mdo.getFullyQualifiedName());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_NAME_DELIMITED, mdo.getName());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_START, ((Number)mdo.getLineNumber()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_END, ((Number)mdo.getEndLine()).longValue());

		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_ABSTRACT, mdo.getIsAbstract());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_CONSTRUCTOR, mdo.getIsConstructor());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_GENERIC, mdo.getIsGenericType());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_STATIC, mdo.getIsStatic());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_VAR_ARGS, mdo.getIsVarargs());

		methodDecSolrDoc.addField("parent",false);
		methodDecSolrDoc.addField("is_method_dec_child",true);

		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PATH_COMPLEXITY, ((Number)mdo.getCyclomaticComplexity()).longValue());

		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_WHILE_COUNT, ((Number)mdo.getWhileStatementList().size()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_FOR_COUNT, ((Number)mdo.getForStatementList().size()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IF_COUNT, ((Number)mdo.getIfStatementList().size()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_CASE_COUNT, ((Number)mdo.getSwitchCaseList().size()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_TERNARY_COUNT, ((Number)mdo.getConditionalExpressionList().size()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_CATCH_COUNT, ((Number)mdo.getCatchClauseList().size()).longValue());
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_LOGICAL_COUNT, ((Number)mdo.getInfixExpressionList().size()).longValue());

		// methodDec.addField(SolrKey.SNIPPET_METHOD_DEC_IS_RECURSIVE, dec.isRecurisive);

		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_DECLARING_CLASS, mdo.getDeclaringClass());

		if(mdo.getDeclaringClass() != null){
			String declaringClass = mdo.getDeclaringClass();

			if(declaringClass.indexOf('<') > -1) {
				String[] split1 = declaringClass.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				String declaringClassShort = split2[split2.length - 1] + "<" + split1[split1.length - 1];
				methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, declaringClassShort);
			}
			else {
				String[] split3 = declaringClass.split("[.]");
				methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, split3[split3.length-1]);
			}
		}

		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_RETURN_TYPE, mdo.getReturnType());

		int localVariableCount = mdo.getArrayList().size() + mdo.getGenericsList().size() + mdo.getPrimitiveList().size() + mdo.getSimpleList().size() - mdo.getParametersList().size();
		methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES, ((Number)localVariableCount).longValue());

		for(String typeParam : mdo.getGenericParametersList()) {
			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParam);
		}

		for(SuperEntityClass wc : mdo.getWildcardList()) {
			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS, wc.getBound());
		}

		Map<String, Integer> paramCount = new HashMap<>();
		Map<String, Integer> paramCountShort = new HashMap<>();

		int parameterTypesListSize = mdo.getParameterTypesList().size();
		for(int i = 0; i < parameterTypesListSize; i++) {
			String argType = mdo.getParameterTypesList().get(i);

			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES, argType);
			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE, argType+"_"+i);

			String shortName2;

			if(argType.indexOf('<') > -1) {
				String[] split1 = argType.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				shortName2 = split2[split2.length - 1] + "<" + split1[split1.length - 1];
			}
			else {
				String[] split = argType.split("[.]");
				shortName2 = split[split.length-1];
			}

			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, shortName2);
			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE, shortName2+"_"+i);

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

		for (Map.Entry<String, Integer> entry : paramCount.entrySet()) {
			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT, entry.getKey() + "_" + entry.getValue());
		}

		for(Map.Entry<String, Integer> entry : paramCountShort.entrySet()) {
			methodDecSolrDoc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT, entry.getKey() + "_"+ entry.getValue());
		}

		CHILD_COUNT++;
		solrDoc.addChildDocument(methodDecSolrDoc);
	}






	private static void makeMethodInvocationSolrDoc(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
		SolrInputDocument methodInvSolrDoc = new SolrInputDocument();
		MethodInvocationObject mio = (MethodInvocationObject) entity;

		methodInvSolrDoc.addField("parent",false);
		methodInvSolrDoc.addField("is_method_invocation_child",true);

		String idInvo = id + "&start=" + entity.getStartCharacter() + "&end=" + entity.getEndCharacter();
		methodInvSolrDoc.addField("id", idInvo);
		methodInvSolrDoc.addField(SolrKey.EXPAND_ID, id);

		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_NAME, mio.getFullyQualifiedName());
		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_NAME_DELIMITED, mio.getName());
		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_START, ((Number)mio.getLineNumber()).longValue());
		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_END, ((Number)mio.getEndLine()).longValue());

		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_CALLING_CLASS, mio.getCallingClass());

		if(!mio.getCallingClass().isEmpty()){
			String callingClass = mio.getCallingClass();

			if(callingClass.indexOf('<') > -1) {
				String[] split1 = callingClass.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
			}
			else {
				String[] split = callingClass.split("[.]");
				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, split[split.length-1]);
			}
		}

		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS, mio.getDeclaringClass());

		if(!mio.getDeclaringClass().isEmpty()){
			String declaringClass = mio.getDeclaringClass();

			if(declaringClass.indexOf('<') > -1) {
				String[] split1 = declaringClass.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
			}
			else {
				String[] split = declaringClass.split("[.]");
				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split[split.length-1]);
			}
		}

		Map<String, Integer> paramCount = new HashMap<>();
		Map<String, Integer> paramCountShort = new HashMap<>();

		int place = 0;
		for(String argType: mio.getArgumentTypes()){
			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES, argType);
			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE, argType + "_" + place);

			String shortName2;
			if(argType.indexOf('<') > -1) {
				String[] split1 = argType.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				shortName2 = split2[split2.length - 1] + "<" + split1[split1.length - 1];
			}
			else {
				String[] split = argType.split("[.]");
				shortName2 = split[split.length - 1];
			}

			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT, shortName2);
			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE, shortName2 + "_" + place);

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

		for(Map.Entry<String, Integer> entry : paramCount.entrySet()) {
			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT, entry.getKey() + "_" + entry.getValue());
		}

		for(Map.Entry<String, Integer> entry : paramCountShort.entrySet()) {
			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT, entry.getKey() + "_" + entry.getValue());
		}

		for(Object argValue: mio.getArguments()){
			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_VALUES, argValue.toString());
		}

		CHILD_COUNT++;
		solrDoc.addChildDocument(methodInvSolrDoc);
	}











	/**
	 * recursively get method declarations (for those method declarations inside of each other)
	 *
	 * @param mdo x
	 * @param solrDoc x
	 * @param id x
	 */
	private static void findAllMethodDeclarations(MethodDeclarationObject mdo, SolrInputDocument solrDoc, String id) {
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



	/**
	 *
	 * @param rawURL x
	 */
	private static void createSolrDocsForURL(String rawURL, File classFile) {
		// todo for each class file
		// todo create a solr doc
		// todo for each other class in a class
		// todo create a solr doc
		// todo for each method dec
		// todo create a solr doc
		// todo upload them to the server


		System.out.println("Started creating solr doc for " + classFile.getName());


		// print time before download

		String[] urlSplit = rawURL.split("/");

		// file is done being written to here with location outputFileLocation
		// do ASTWalker and get the file model
		ASTWalker aw = new ASTWalker(astProperties); // todo change input to map (leave string for standalone)
		aw.parseFile(classFile.getAbsolutePath());
		if(aw.getFileModel() == null) {
			throw new IllegalArgumentException("[ERROR]: the file model is null!");
		}

		// todo
		// 1. function to create solr docs (empty)
		// 2. add technical data
		// 3. add social data
		List<ClassSolrDocument> classSolrDocList = new ArrayList<>();
		List<SolrInputDocument> methodDecSolrDocList = new ArrayList<>();

		for(JavaClass jClass : aw.getFileModel().getJavaClassList()) {
			ClassSolrDocument classSolrDoc = new ClassSolrDocument(jClass, rawURL, configProperties);
			classSolrDoc.addProjectData("https://github.com/" + urlSplit[3] + "/" + urlSplit[4]);
			classSolrDoc.addSocialData("clone/" + urlSplit[4], getRelativeFileRepoPath(rawURL), classFile);
			classSolrDoc.addTechnicalData(jClass, aw.getFileModel(), "https://github.com/" + urlSplit[3] + "/" + urlSplit[4] + "/blob/master/" + getRelativeFileRepoPath(rawURL));




			classSolrDoc.getSolrDocument().forEach((k, v) -> System.out.println(k + " -> " + v.getValue()));
			System.out.println("checkpoint reached");
			System.exit(0); // todo: current






//			for(SuperEntityClass md : jClass.getMethodDeclarationList()) {
//				findAllMethodDeclarations((MethodDeclarationObject)md, classSolrDoc, rawURL);
//
//				// recursively find inner method decs
//				// create emptyMethodSolrDoc
//				// add social
//				// add technical
//			}









			// if add technical data is true



			classSolrDocList.add(classSolrDoc);


			System.exit(0);
		}



		// todo still more to do!
		// todo: specifically, need to create method dec and method inv solr doc
		// todo: then we can upload them all
	}


	/**
	 * create clone folder on program start
	 */
	private static void setup() {
		if(!(new File("clone").exists()) && !(new File("clone").mkdir())) {
			throw new IllegalArgumentException("[ERROR]: could not make \"clone\" directory");
		}
	}

	/**
	 * delete clone folder on program end
	 */
	private static void teardown() {
		if(!(new File("clone").delete())) {
			throw new IllegalArgumentException("[ERROR]: could not delete \"clone\" directory");
		}
	}

	/**
	 * Just a normal main function
	 *
	 * todo: Press Ctrl-Alt-r to change the command line arguments
	 * todo: Currently set to "resources/config.properties" and "resources/astconfig.properties
	 *
	 * todo: the order of things
	 * todo: clone the repo
	 * todo: load the astconfig file
	 * todo: for each similarity function
	 * todo: create and upload the solr doc
	 * todo: delete the repository
	 * todo: move onto the next repository
	 *
	 * @param args arguments from the command line
	 */
	public static void main(String[] args) {
		configProperties = PropertyReader.createConfigPropertiesMap(args[0]);
		astProperties = PropertyReader.createASTPropertiesMap(args[1]); // todo this is wrong. this needs to be a file with a LIST of files of ast properties. this is different from the original ast properties

		setup();

		try(BufferedReader urlBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToURLMapPath")), "UTF-8"))) {
			for(String url; (url = urlBr.readLine()) != null; ) {

				// todo: add timestmap to see how long it takes to download the repo

				// clone repository
				String[] urlSplit = url.split("/");
				ClonedRepository clone = new ClonedRepository(url, "https://test:test@github.com/" + urlSplit[3] + "/" + urlSplit[4] + ".git", "clone/" + urlSplit[4]);
				clone.cloneRepository();

				// find file path and name
				List<String> pathToFileInRepo = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(urlSplit, 6, urlSplit.length - 1)));

				// get class file from repo
				File classFile = findFileInRepository("clone/" + urlSplit[4], urlSplit[urlSplit.length - 1].split("\\?")[0], pathToFileInRepo);

				// run each similarity function on the cloned repository
				try(BufferedReader simBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToSimFunctions")), "UTF-8"))) {
					for(String bitVector; (bitVector = simBr.readLine()) != null; ) {
						simProperties = PropertyReader.createSimilarityFunctionPropertiesMap(bitVector);
						createSolrDocsForURL(url, classFile);
					}
				} catch(IOException e) {
					e.printStackTrace();
				}

				// delete repository
				clone.deleteRepository();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		teardown();
	}
}
