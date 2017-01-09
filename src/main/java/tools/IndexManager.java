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

// todo: create mapping of sim function to server and port number

package tools;

import AST.SimilarityASTWalker;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class IndexManager {
	private static IndexManager instance;
	private static Map<String, String> configProperties;
	private static Map<String, String> serverProperties;
	private static Map<String, Boolean> simProperties;
	private static String currentBitVector;
	private static final String COLLECTION_NAME = "MoreLikeThisIndex";

//	private static int MAXDOC = 300;
//	private static int MAX_CHILD_DOC = 4000;
//	static int CHILD_COUNT = 0;

	private static int MAXDOC = 300; // todo: we want to upload after each class doc (this is definitely going to be more than 1 document)
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
	 * @param parentDirectory xxx
	 * @param fileName xxx
	 * @param pathToFileInRepo xxx
	 * @return xxx
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
	 * @param url xxx
	 * @return xxx
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
	 * xxx
	 * @param rawURL xxx
	 */
	private static void createSolrDocsForURL(String rawURL, File classFile) {
		System.out.println("Started creating solr doc for " + classFile.getName());

		// todo: no longer create method dec and method inv classes

		// run for loop to create class solr doc over the keys (see the class file for the method dec)
		String[] urlSplit = rawURL.split("/");
		String className = urlSplit[urlSplit.length - 1].split("\\.java")[0];

		SolrInputDocument solrDoc = null;

		// extract technical data
		if(simProperties.get("importsScore")
			|| simProperties.get("variableNameScore")
			|| simProperties.get("classNameScore")
			|| simProperties.get("methodCallScore")
			|| simProperties.get("methodDecScore")
			|| simProperties.get("sizeScore")
			|| simProperties.get("importNumScore")
			|| simProperties.get("complexityScore")
			|| simProperties.get("extendsScore")
			|| simProperties.get("packageScore")
			|| simProperties.get("fieldsScore")
			|| simProperties.get("isGenericScore")
			|| simProperties.get("isAbstractScore")
			|| simProperties.get("isWildCardScore")
		) {
			SimilarityASTWalker saw = new SimilarityASTWalker(className, simProperties); // todo change input to map (leave string for standalone)
			solrDoc = saw.parseFileIntoSolrDoc(rawURL, classFile.getAbsolutePath());
		}

		if(solrDoc == null) {
			solrDoc = new SolrInputDocument();
			solrDoc.addField("id", rawURL);
		}

		solrDoc.addField("parent", true);

		if(simProperties.get("authorScore")) {
			JavaGitHubData jghd = new JavaGitHubData(getRelativeFileRepoPath(rawURL));
			jghd.getCommits("clone/" + urlSplit[4], getRelativeFileRepoPath(rawURL), classFile);
			Commit headCommit = jghd.getListOfCommits().get(jghd.getListOfCommits().size() - 1);
			solrDoc.addField("snippet_author_name", headCommit.getAuthor());
		}

		if(simProperties.get("ownerScore") || simProperties.get("projectScore")) {
			SolrDocumentList list = Solrj.getInstance(configProperties.get("passPath")).query("id:\"https://github.com/" + urlSplit[3] + "/" + urlSplit[4]+"\"", "codeexchange.ics.uci.edu", 9001, "githubprojects", 1);
			if(list.isEmpty()) throw new IllegalArgumentException("[ERROR]: project data is null!");

			SolrDocument doc = list.get(0);

			if(simProperties.get("ownerScore")) {
				solrDoc.addField("snippet_project_owner", doc.getFieldValue("userName").toString());
			}

			if(simProperties.get("projectScore")) {
				solrDoc.addField("snippet_project_name", doc.getFieldValue("projectName").toString());
			}
		}

		System.exit(0); // DO NOT RUN!!!
		Solrj.getInstance(configProperties.get("passPath")).addDoc(solrDoc);
		Solrj.getInstance(configProperties.get("passPath")).commitDocs(serverProperties.get(currentBitVector), COLLECTION_NAME);
	}


	/**
	 * xxx
	 * @param urlAndErrorCode xxx
	 */
	private static void printErrorURL(String urlAndErrorCode) {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("resources/errorURLs.txt", true), "UTF-8"))) {
			bw.write(urlAndErrorCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * xxx
	 * @param url xxx
	 */
	private static void workOnURL(String url) {
		// todo: add timestamp to see how long it takes to download the repo

		// clone repository
		String[] urlSplit = url.split("/");
		ClonedRepository clone = new ClonedRepository("https://test:test@github.com/" + urlSplit[3] + "/" + urlSplit[4] + ".git", "clone/" + urlSplit[4]);
		clone.cloneRepository();

		// find file path and name
		List<String> pathToFileInRepo = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(urlSplit, 6, urlSplit.length - 1)));

		// get class file from repo
		File classFile = findFileInRepository("clone/" + urlSplit[4], urlSplit[urlSplit.length - 1].split("\\?")[0], pathToFileInRepo);

		// run each similarity function on the cloned repository
		try(BufferedReader simBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToSimFunctions")), "UTF-8"))) {
			for(String bitVector; (bitVector = simBr.readLine()) != null; ) {
				simProperties = PropertyReader.createSimilarityFunctionPropertiesMap(bitVector);
				currentBitVector = bitVector;
				createSolrDocsForURL(url, classFile);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		// delete repository
		clone.deleteRepository();
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
	 * todo: Currently set to "resources/config.properties" and "resources/serverConfig.properties
	 *
	 * @param args arguments from the command line
	 */
	public static void main(String[] args) {
		configProperties = PropertyReader.createConfigPropertiesMap(args[0]);
		serverProperties = PropertyReader.createConfigPropertiesMap(args[1]);

		setup();

		try(BufferedReader urlBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToURLMapPath")), "UTF-8"))) {
			for(String url; (url = urlBr.readLine()) != null; ) {

				// todo: do a 200 check to make sure the repo still exists
				try {
					URL urlObj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
					con.setRequestMethod("GET");
					con.setConnectTimeout(500);
					con.connect();

					int code = con.getResponseCode();
					if (code == 200) {
						// url exists :D
						workOnURL(url);
					}
					else {
						// print url to error file
						printErrorURL(url + ":::" + code);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		teardown();
	}
}
