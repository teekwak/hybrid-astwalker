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

package similarity;

import org.apache.solr.common.SolrInputDocument;
import properties.SocialPropertyParser;
import properties.TechnicalPropertyParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class IndexManager {
	private static Map<String, String> configProperties;
	private static String currentURL;
	private static int counter;


	/**
	 * Quickly traverse repository to find a file based on a list of directories
	 * @param fileName xxx
	 * @param pathToFileInRepo xxx
	 * @return xxx
	 *
	 */
	private static File findFileInRepository(String fileName, List<String> pathToFileInRepo) {
		File[] filesInDirectory = new File("folder1").listFiles();

		// todo: is there a way to write this using immutable variables
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
			return Arrays.stream(filesInDirectory).filter(f -> f.getName().equals(fileName)).findFirst().orElse(null);
		}

		return null;
	}


	/**
	 * xxx
	 * @param classFile xxx
	 */
	private static void createSolrDocsForURL(File classFile) {
		String[] urlSplit = currentURL.split("/");

		// create empty solr doc
		SolrInputDocument solrDoc = new SolrInputDocument();
		solrDoc.addField("id", currentURL);
		solrDoc.addField("parent", true);

		// add technical properties to solrDoc
		TechnicalPropertyParser tpp = new TechnicalPropertyParser(solrDoc, classFile.getAbsolutePath(), urlSplit[urlSplit.length - 1].split("\\.java")[0]);
		solrDoc = tpp.getProperties();
		tpp = null;

		// add social properties to solrDoc
		SocialPropertyParser spp = new SocialPropertyParser(solrDoc, currentURL, configProperties.get("passPath"));
		solrDoc = spp.getProperties();
		spp = null;

		// upload the document to the server
		Solrj.getInstance(configProperties.get("passPath")).addDoc(solrDoc);
		Solrj.getInstance(configProperties.get("passPath")).commitDocs(configProperties.get("hostNameAndPortNumber"), configProperties.get("collectionName"));
		solrDoc = null;
	}


	/**
	 * todo: what does init even mean?
	 */
	private static void init() {
		// clone repository
		String[] urlSplit = currentURL.split("/");

		// need to clone into special place!
		ClonedRepository clone = new ClonedRepository("https://test:test@github.com/" + urlSplit[3] + "/" + urlSplit[4] + ".git");

		clone.cloneRepository();

		// reset to saved version
		clone.resetRepositoryToVersion(urlSplit[5]);
		clone = null;

		// find file path and name
		List<String> pathToFileInRepo = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(urlSplit, 6, urlSplit.length - 1)));

		// get class file from repo
		File classFile = findFileInRepository(urlSplit[urlSplit.length - 1].split("\\?")[0], pathToFileInRepo);
		pathToFileInRepo = null;

		if(classFile != null) {
			createSolrDocsForURL(classFile);
		}

		classFile = null;
		urlSplit = null;

	}


	// todo: this does not belong in this file?
	private static void readCounter() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("resources/counter.txt"), "UTF-8"))) {
			for(String line; (line = br.readLine()) != null; ) {
				counter = Integer.parseInt(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// todo: this does not belong in this file?
	private static void incrementCounter() {
		counter += 1;
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("resources/counter.txt"), "UTF-8"))) {
			bw.write(counter + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		readCounter();

		try(BufferedReader urlBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToURLMapPath")), "UTF-8"))) {
			// skip lines
			for(int i = 1; i < counter; i++) {
				urlBr.readLine();
			}

			for(String url; (url = urlBr.readLine()) != null; ) {
				// todo: this should be a new function, maybe even a new class
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
