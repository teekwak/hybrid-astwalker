/* *****************************************************************************
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

/*
 * Created by Thomas Kwak
 */

package tools;

import AST.ASTWalker;
import entities.JavaClass;
import entities.MethodDeclarationObject;
import entities.SuperEntityClass;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.util.*;

public class IndexManager {
	private static IndexManager instance;
	private static Map<String, String> configProperties;
	private static Map<String, Boolean> astProperties;
	private static Map<String, Boolean> simProperties;

//	private static int MAXDOC = 300;
//	private static int MAX_CHILD_DOC = 4000;
//	static int CHILD_COUNT = 0;

	private static int MAXDOC = 1; // todo: we want to upload after each class doc (this is definitely going to be more than 1 document)
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


	/**
	 * recursively get method declarations (for those method declarations inside of each other)
	 *
	 * @param mdo x
	 * @param solrDoc x
	 * @param id x
	 */
	private static void findAllMethodDeclarations(MethodDeclarationObject mdo, SolrInputDocument solrDoc, String id) {
		MethodDeclarationSolrDocument mdSolrDoc = new MethodDeclarationSolrDocument(simProperties); // todo: i dont think configProperties belongs here
		mdSolrDoc.addTechnicalData(mdo, solrDoc, id);

		for(SuperEntityClass mi : mdo.getMethodInvocationList() ) {
			MethodInvocationSolrDocument miSolrDoc = new MethodInvocationSolrDocument(simProperties); // todo: i dont think configProperties belongs here
			miSolrDoc.addTechnicalData(mi, solrDoc, id);
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


		// todo: print time before download

		String[] urlSplit = rawURL.split("/");

		// file is done being written to here with location outputFileLocation
		// do ASTWalker and get the file model
		ASTWalker aw = new ASTWalker(astProperties); // todo change input to map (leave string for standalone)
		aw.parseFile(classFile.getAbsolutePath());
		if(aw.getFileModel() == null) {
			throw new IllegalArgumentException("[ERROR]: the file model is null!");
		}

		List<ClassSolrDocument> classSolrDocList = new ArrayList<>();

		for(JavaClass jClass : aw.getFileModel().getJavaClassList()) {
			ClassSolrDocument classSolrDoc = new ClassSolrDocument(jClass, rawURL, configProperties, simProperties);
			classSolrDoc.addProjectData("https://github.com/" + urlSplit[3] + "/" + urlSplit[4]);
			classSolrDoc.addSocialData("clone/" + urlSplit[4], getRelativeFileRepoPath(rawURL), classFile);
			classSolrDoc.addTechnicalData(jClass, aw.getFileModel(), "https://github.com/" + urlSplit[3] + "/" + urlSplit[4] + "/blob/master/" + getRelativeFileRepoPath(rawURL));

			for(SuperEntityClass md : jClass.getMethodDeclarationList()) {
				findAllMethodDeclarations((MethodDeclarationObject)md, classSolrDoc.getSolrDocument(), rawURL);
			}




			classSolrDocList.add(classSolrDoc);



		}

		classSolrDocList.forEach(classSolrDoc -> classSolrDoc.getSolrDocument().forEach((k, v) -> System.out.println(k + " -> " + v.getValue())));
		System.out.println("checkpoint reached");
		System.exit(0);


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
