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
 * Created by Lee Martie
 */

package similarity;

import java.io.*;
import java.util.*;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

class Solrj {

	private static Solrj instance = null;
	private static String pass;
	private UpdateRequest req = new UpdateRequest();


	static Solrj getInstance(String passwordFilePath){
		if(instance == null){
			instance = new Solrj();
		}

		File file = new File(passwordFilePath);

		Scanner scan;
		try {
			scan = new Scanner(file);

			while(scan.hasNextLine()){
				pass = scan.nextLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return instance;
	}


  protected class PreEmptiveBasicAuthenticator implements HttpRequestInterceptor {
    private final UsernamePasswordCredentials credentials;

	  PreEmptiveBasicAuthenticator(String user, String pass) {
      credentials = new UsernamePasswordCredentials(user, pass);
    }

		@SuppressWarnings("deprecation")
		public void process(HttpRequest request, HttpContext context)
	            throws IOException {
	          request.addHeader(BasicScheme.authenticate(credentials,"US-ASCII",false));
	        }
  }


	void addDoc(SolrInputDocument doc) {
		if(doc == null)
			return;

		req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
		req.add( doc );
		req.getDocuments().size();

	}


	@SuppressWarnings("deprecation")
	void commitDocs(String hostNameAndPortNumber, String collectionName){
		//System.out.println("===uploading ["+req.getDocuments().size()+"] docs to codeexchange===");
		HttpSolrServer server;
		  server = new HttpSolrServer("http://"+hostNameAndPortNumber+"/solr/"+collectionName);

		  AbstractHttpClient httpClient = (AbstractHttpClient) server.getHttpClient();
      httpClient.addRequestInterceptor(new PreEmptiveBasicAuthenticator("admin",pass));

		  try {
			UpdateResponse rsp = req.process( server );
			req.clear();

		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * using a filtered query (fq) on the solr is supposed
	 * to improve performance and cache results
	 * @param query xxx
	 * @param hostName xxx
	 * @param portNumber xxx
	 * @param collectionName xxx
	 * @param rows is max results
	 * @return xxx
	 */
	@SuppressWarnings("deprecation")
	SolrDocumentList query(String query, String hostName, int portNumber, String collectionName, int rows) {
		HttpSolrServer server;
		try {
			server = new HttpSolrServer("http://"+hostName+":"+portNumber+"/solr/"+collectionName);

		//	  System.out.println("QUERY: "+query);

			  SolrQuery solrQuery = new  SolrQuery();
					  solrQuery.setQuery(query);
					  solrQuery.setRows(rows);
					  solrQuery.setIncludeScore(true);

			  QueryResponse rsp = server.query(solrQuery);

			SolrDocumentList results = rsp.getResults();

			server.close();

			return results;

		} catch (Exception e) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(),
					"Problem parsing query", e.toString());
		}

		return null;

	}
}