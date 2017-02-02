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
 *
 * Error codes: 404 - not found
 *              100 - commit no longer exists (502 was old error code)
 */

package similarity;

import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class IndexManager {
	private static Map<String, String> configProperties;
	private static Map<String, String> serverProperties;
	private static Map<String, Boolean> simProperties;
	private static String currentURL;
	private static int counter;


	/**
	 * xxx
	 * @param message xxx
	 */
	private static void writeToTimesFile(String message) {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configProperties.get("pathToTimestampsFile"), true), "UTF-8"))) {
			bw.write(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Quickly traverse repository to find a file based on a list of directories
	 * @param fileName xxx
	 * @param pathToFileInRepo xxx
	 * @return xxx
	 */
	private static File findFileInRepository(String fileName, List<String> pathToFileInRepo) {
		File[] filesInDirectory = new File("clone").listFiles();

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

		printErrorURL(currentURL, 100);
		return null;
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
		urlSplit = null;
		return repoPath.toString();
	}


	/**
	 * xxx
	 * @param classFile xxx
	 */
	private static void createSolrDocsForURL(File classFile, String currentBitVector) {
		String[] urlSplit = currentURL.split("/");

		// create solr doc
		writeToTimesFile("Started creating empty solr doc$$" + currentBitVector + "::" + System.currentTimeMillis());
		SolrInputDocument solrDoc = new SolrInputDocument();
		solrDoc.addField("id", currentURL);
		solrDoc.addField("parent", true);
		writeToTimesFile("Finished creating empty solr doc$$" + currentBitVector + "::" + System.currentTimeMillis());

		// extract source code
		writeToTimesFile("Started source code$$" + currentBitVector + "::" + System.currentTimeMillis());
		String sourceCode = null;
		try {
			sourceCode = FileUtils.readFileToString(new File(classFile.getAbsolutePath()), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		solrDoc.addField("snippet_code", sourceCode);
		writeToTimesFile("Finished source code$$" + currentBitVector + "::" + System.currentTimeMillis());

		// extract technical data
		writeToTimesFile("Started technical$$" + currentBitVector + "::" + System.currentTimeMillis());
		if(!simProperties.get("authorScore") && !simProperties.get("ownerScore") && !simProperties.get("projectScore")) {
			String className = urlSplit[urlSplit.length - 1].split("\\.java")[0];
			SimilarityASTWalker saw = new SimilarityASTWalker(className, simProperties);
			className = null;
			saw.parseFileIntoSolrDoc(solrDoc, sourceCode, classFile.getAbsolutePath());
			saw = null;
		}
		writeToTimesFile("Finished technical$$" + currentBitVector + "::" + System.currentTimeMillis());
		sourceCode = null;

		// extract social data
		writeToTimesFile("Started social$$" + currentBitVector + "::" + System.currentTimeMillis());
		if(simProperties.get("authorScore")) {
			JavaGitHubData jghd = new JavaGitHubData();
			jghd.getCommits(getRelativeFileRepoPath(currentURL));
			Commit headCommit = jghd.getListOfCommits().get(jghd.getListOfCommits().size() - 1);
			solrDoc.addField("snippet_author_name", headCommit.getAuthor());
			jghd = null;
			headCommit = null;
		}

		if(simProperties.get("ownerScore") || simProperties.get("projectScore")) {
			SolrDocumentList list = Solrj.getInstance(configProperties.get("passPath")).query("id:\"https://github.com/" + urlSplit[3] + "/" + urlSplit[4]+"\"", "grok.ics.uci.edu", 9001, "githubprojects", 1);
			if(list.isEmpty()) throw new IllegalArgumentException("[ERROR]: project data is null!");

			SolrDocument doc = list.get(0);

			if(simProperties.get("ownerScore")) {
				solrDoc.addField("snippet_project_owner", doc.getFieldValue("userName").toString());
			}

			if(simProperties.get("projectScore")) {
				solrDoc.addField("snippet_project_name", doc.getFieldValue("projectName").toString());
			}

			list = null;
			doc = null;
		}
		writeToTimesFile("Finished social$$" + currentBitVector + "::" + System.currentTimeMillis());
		urlSplit = null;

		Solrj.getInstance(configProperties.get("passPath")).addDoc(solrDoc);
		writeToTimesFile("Started uploading$$" + currentBitVector + "::" + System.currentTimeMillis());
		Solrj.getInstance(configProperties.get("passPath")).commitDocs(serverProperties.get(currentBitVector), configProperties.get("collectionName"));
		writeToTimesFile("Finished uploading$$" + currentBitVector + "::" + System.currentTimeMillis());
		solrDoc = null;
	}


	/**
	 * Prints URL and error code to file
	 * @param url the URL
	 * @param errorCode the error code returned by the URL
	 */
	private static void printErrorURL(String url, int errorCode) {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configProperties.get("pathToErrorFile"), true), "UTF-8"))) {
			bw.write(url + "::" + errorCode + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * xxx
	 */
	private static void init() {
		writeToTimesFile("URL::" + currentURL);
		writeToTimesFile("Started process::" + System.currentTimeMillis());

		// clone repository
		String[] urlSplit = currentURL.split("/");

		// need to clone into special place!
		ClonedRepository clone = new ClonedRepository("https://test:test@github.com/" + urlSplit[3] + "/" + urlSplit[4] + ".git");
		writeToTimesFile("Started cloning::" + System.currentTimeMillis());

		clone.cloneRepository();
		writeToTimesFile("Finished cloning::" + System.currentTimeMillis());

		writeToTimesFile("Started setup after cloning::" + System.currentTimeMillis());
		// reset to saved version
		clone.resetRepositoryToVersion(urlSplit[5]);
		clone = null;

		// find file path and name
		List<String> pathToFileInRepo = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(urlSplit, 6, urlSplit.length - 1)));

		// get class file from repo
		File classFile = findFileInRepository(urlSplit[urlSplit.length - 1].split("\\?")[0], pathToFileInRepo);
		pathToFileInRepo = null;
		writeToTimesFile("Finished setup after cloning::" + System.currentTimeMillis());

		if(classFile != null) {
			// run each similarity function on the cloned repository
			for(String bitVector : serverProperties.keySet()) {
				System.out.print("\rWorking on: " + urlSplit[3] + "/" + urlSplit[4] + " - " + bitVector);
				simProperties = PropertyReader.createSimilarityFunctionPropertiesMap(bitVector);
				createSolrDocsForURL(classFile, bitVector);
			}
		}
		else {
			writeToTimesFile("Error - class file is null::" + System.currentTimeMillis());
		}

		classFile = null;
		urlSplit = null;

		writeToTimesFile("Finished process::" + System.currentTimeMillis());
		writeToTimesFile("=====");
	}


	private static void readCounter() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("resources/counter.txt"), "UTF-8"))) {
			for(String line; (line = br.readLine()) != null; ) {
				counter = Integer.parseInt(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static void incrementCounter() {
		counter += 1;
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("resources/counter.txt"), "UTF-8"))) {
			bw.write(counter + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static String addTimestampToFileName(String path) {
		String[] split = path.split("_");
		return split[0] + "_" + System.currentTimeMillis() + "_" + split[1];
	}


	/**
	 * Just a normal main function
	 *
	 * todo: Press Ctrl-Alt-r to change the command line arguments
	 * todo: Currently set to "resources/config.properties"∫
	 *
	 * @param args arguments from the command line
	 */
	public static void main(String[] args) {
		configProperties = PropertyReader.fileToStringStringMap(args[0]);
		serverProperties = PropertyReader.fileToStringStringMap(configProperties.get("serverConfigPath"));
		configProperties.put("pathToTimestampsFile", addTimestampToFileName(configProperties.get("pathToTimestampsFile")));
		configProperties.put("pathToErrorFile", addTimestampToFileName(configProperties.get("pathToErrorFile")));
		readCounter();

		try(BufferedReader urlBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToURLMapPath")), "UTF-8"))) {
			// skip lines
			for(int i = 1; i < counter; i++) {
				urlBr.readLine();
			}

			for(String url; (url = urlBr.readLine()) != null; ) {

				// todo: do a 200 check to make sure the repo still exists
				try {
					URL urlObj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
					con.setRequestMethod("GET");
					con.setConnectTimeout(3000);
					con.connect();

					int code = con.getResponseCode();
					if (code == 200) {
						// url exists :D
						urlObj = null;
						con = null;

						// update counter
						incrementCounter();

						currentURL = url;
						init();
					}
					else {
						urlObj = null;
						con = null;
						// print url to error file
						printErrorURL(url, code);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\nProcess finished gracefully");
	}
}
