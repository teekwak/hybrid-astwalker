package tools;

import AST.ASTWalker;
import entities.JavaClass;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
	 * @param parentDirectory
	 * @param fileName
	 * @param pathToFileInRepo
	 * @return
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
	 *
	 * @param urlAsString
	 * @param outputFileLocation
	 */
	private static void urlToFile(String urlAsString, String outputFileLocation) {
		try {
			URL url = new URL(urlAsString);
			BufferedReader githubBr = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			for (String javaLine; (javaLine = githubBr.readLine()) != null; ) {
				try(OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputFileLocation, true), "UTF-8")) {
					osw.write(javaLine);
					osw.write(System.lineSeparator());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			githubBr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Queries CodeExchange for project information
	 * @param solrDoc
	 * @param projectURL
	 */
	private static void addProjectData(SolrInputDocument solrDoc, String projectURL) {
		// todo check the htmlURL object
		String htmlURL = "\""+projectURL+"\"";
		String hostName = "codeexchange.ics.uci.edu";
		int portNumber = 9001;
		String collectionName = "githubprojects";

		// todo we need the solrj instance
		SolrDocumentList list = Solrj.getInstance(configProperties.get("passPath")).query("id:" + htmlURL, hostName, portNumber, collectionName, 1);

		if(list.isEmpty()) throw new IllegalArgumentException("[ERROR]: project data is null!");

		SolrDocument doc = list.get(0);

		if(doc.getFieldValue("htmlURL") != null) { solrDoc.addField(SolrKey.PROJECT_ADDRESS, doc.getFieldValue("htmlURL").toString()); }
		if(doc.getFieldValue("avatarURL") != null) { solrDoc.addField(SolrKey.PROJECT_OWNER_AVATAR, doc.getFieldValue("avatarURL").toString()); }
		if(doc.getFieldValue("description") != null){ solrDoc.addField(SolrKey.PROJECT_DESCRIPTION, doc.getFieldValue("description").toString()); }
		if(doc.getFieldValue("fork") != null) { solrDoc.addField(SolrKey.PROJECT_IS_FORK, doc.getFieldValue("fork").toString()); }
		if(doc.getFieldValue("projectName") != null) { solrDoc.addField(SolrKey.PROJECT_NAME, doc.getFieldValue("projectName").toString()); }
		if(doc.getFieldValue("siteAdmin") != null) { solrDoc.addField(SolrKey.AUTHOR_IS_SITE_ADMIN, doc.getFieldValue("siteAdmin").toString()); }
		if(doc.getFieldValue("userName") != null) { solrDoc.addField(SolrKey.PROJECT_OWNER, doc.getFieldValue("userName").toString()); }
		if(doc.getFieldValue("userType") != null) { solrDoc.addField(SolrKey.AUTHOR_TYPE, doc.getFieldValue("userType").toString()); }
	}

	/**
	 *
	 * @param jc
	 * @param fullURL
	 * @return
	 */
	private static SolrInputDocument createEmptyClassSolrDoc(JavaClass jc, String fullURL) {
		SolrInputDocument classDoc = new SolrInputDocument();

		classDoc.addField(SolrKey.SNIPPET_NAME, jc.getFullyQualifiedName());
		classDoc.addField(SolrKey.SNIPPET_NAME_DELIMITED, jc.getName());
		classDoc.addField(SolrKey.SNIPPET_ADDRESS, fullURL.split("\\?start=")[0]);
		classDoc.addField("id", fullURL);
		classDoc.addField(SolrKey.EXPAND_ID, fullURL);
		classDoc.addField(SolrKey.SNIPPET_GRANULARITY, "Class");
		classDoc.addField("parent", true);

		return classDoc;
	}

	/**
	 *
	 * @param rawURL
	 */
	private static void createSolrDocsForURL(String rawURL, File classFile) {
		// todo for each class file
		// todo create a solr doc
		// todo for each other class in a class
		// todo create a solr doc
		// todo for each method dec
		// todo create a solr doc
		// todo upload them to the server



		// print time before download
		// convert url to .java file
		// todo: no longer need to download the file because the repo is cloned already
		// todo: just need to find the file based on the file path given in the URL

		String[] urlSplit = rawURL.split("/");
		String outputFileLocation = "resources/" + urlSplit[urlSplit.length - 1].split("\\?start=")[0];
		urlToFile(rawURL, outputFileLocation); // downloads file

		// file is done being written to here with location outputFileLocation
		// do ASTWalker and get the file model
		ASTWalker ar = new ASTWalker(astProperties); // todo change input to map (leave string for standalone)
		ar.parseFile(outputFileLocation);
		if(ar.getFileModel() == null) {
			throw new IllegalArgumentException("[ERROR]: the file model is null!");
		}

		// todo maybe print time here to see how long repo took to download?
		// todo do we always need to clone the repo? the answer is yes
		// however, we need to clone this WAAAY before


		// todo
		// 1. function to create solr docs (empty)
		// 2. add technical data
		// 3. add social data
		List<SolrInputDocument> classSolrDocList = new ArrayList<>();
		List<SolrInputDocument> methodDecSolrDocList = new ArrayList<>();

		for(JavaClass jc : ar.getFileModel().getJavaClassList()) {
			SolrInputDocument classSolrDoc = createEmptyClassSolrDoc(jc, rawURL);

			// we need to check the url
			// need to hit the url on the grok server
			// problem is that the grok server is not up
			// addProjectData(classSolrDoc, clone.getLocalDirectory(), /* something */);

			// if add social data is true
			// this comes from cloning
			// addSocialData(classSolrDoc, clone);

			// if add technical data is true
			// addTechnicalData(classSolrDoc, jc);


			classSolrDocList.add(classSolrDoc);


			System.exit(0);
		}



		// todo still more to do!
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

		try(BufferedReader urlBr = new BufferedReader(new InputStreamReader(new FileInputStream(configProperties.get("pathToURLMapPath")), "UTF-8"))) {
			for(String url; (url = urlBr.readLine()) != null; ) {

				// todo: add timestmap to see how long it takes to download the repo

				// clone repository
				String[] urlSplit = url.split("/");
				ClonedRepository clone = new ClonedRepository(url, "https://test:test@github.com/" + urlSplit[3] + "/" + urlSplit[4] + ".git", "resources/" + urlSplit[4]);
				clone.cloneRepository();

				// find file path and name
				List<String> pathToFileInRepo = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(urlSplit, 6, urlSplit.length - 1)));

				// get class file from repo
				File classFile = findFileInRepository("resources/" + urlSplit[4], urlSplit[urlSplit.length - 1].split("\\?")[0], pathToFileInRepo);

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
	}
}
