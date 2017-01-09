package tools;

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
import org.apache.solr.common.params.ModifiableSolrParams;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class Solrj {

	private String queryStr;
	private static Solrj instance = null;
	private SolrDocumentList recommendations = new SolrDocumentList();
	private static String pass;
	public UpdateRequest req = new UpdateRequest();

	private Solrj() {
	}

	public static Solrj getInstance(String passwordFilePath){
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

	public Solrj(String queryStr) {
		this.queryStr = queryStr;

		System.out.println("okay");
	}

	public void clearDocs() {
		req.clear();
	}

    protected class PreEmptiveBasicAuthenticator implements HttpRequestInterceptor {
        private final UsernamePasswordCredentials credentials;

        public PreEmptiveBasicAuthenticator(String user, String pass) {
          credentials = new UsernamePasswordCredentials(user, pass);
        }

	@SuppressWarnings("deprecation")
	public void process(HttpRequest request, HttpContext context)
            throws IOException {
          request.addHeader(BasicScheme.authenticate(credentials,"US-ASCII",false));
        }
      }

	public void addDoc(SolrInputDocument doc) {
		if(doc == null)
			return;

		req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
		req.add( doc );
		req.getDocuments().size();

	}

	@SuppressWarnings("deprecation")
	public void commitDocs(String hostNameAndPortNumber, String collectionName){
		//System.out.println("===uploading ["+req.getDocuments().size()+"] docs to codeexchange===");
		HttpSolrServer server;
		  server = new HttpSolrServer("http://"+hostNameAndPortNumber+"/solr/"+collectionName);

		  AbstractHttpClient httpClient = (AbstractHttpClient) server.getHttpClient();
	      httpClient.addRequestInterceptor(new PreEmptiveBasicAuthenticator("admin",pass));

	      //server.add(doc);
		  try {
			UpdateResponse rsp = req.process( server );
			req.clear();
			//System.out.println("--- documents remaining after clear:["+req.getDocuments().size()+"]");
			//System.out.println("--- child:["+IndexManager.getInstance().CHILD_COUNT+"]");
			IndexManager.getInstance().CHILD_COUNT = 0;

		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("deprecation")
	public long queryCountDocs(String hostName, int portNumber) throws IOException{
		try(HttpSolrServer server = new HttpSolrServer("http://"+hostName+":"+portNumber+"/solr")) {
			ModifiableSolrParams params = new ModifiableSolrParams();
			params.add("q", "contentID:*");
			QueryResponse rq = server.query(params);

			server.close();

			return rq.getResults().getNumFound();
		}
		catch (org.apache.solr.client.solrj.SolrServerException e) {
			System.err.println("Query problem");
		}

		return -1;
	}

	public SolrDocumentList queryFavorites(String hostName, int portNumber, String collectionName){
		File file = new File("data/favorites");

		ArrayList<String> IDs = new ArrayList<String>();
		try(Scanner scan = new Scanner(file)) {
			while(scan.hasNext()){
				String id = scan.nextLine();
				IDs.add(id);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String query = "id:(";
		StringBuilder idLogic = new StringBuilder();
		for(int i = 0; i < IDs.size(); i++){
			String id = IDs.get(i);
			if(i == 0) {
				idLogic.append(id);
			}
			else {
				idLogic.append(" OR ");
				idLogic.append(id);
			}
		}
		idLogic.append(")");
		query = query + idLogic.toString();

		if(IDs.size() == 0)
			return null;
		else

		return query(query,hostName, portNumber, collectionName,100);

	}


	/**
	 * using a filtered query (fq) on the solr is supposed
	 * to improve performance and cache results
	 * @param query
	 * @param hostName
	 * @param portNumber
	 * @param collectionName
	 * @param rows is max results
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public SolrDocumentList query(String query, String hostName, int portNumber, String collectionName, int rows) {
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


	/**
	 * using a filtered query (fq) on the solr is supposed
	 * to improve performance and cache results
	 * @param query
	 * @param hostName
	 * @param portNumber
	 * @param collectionName
	 * @param rows is max results
	 * @param start
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public SolrDocumentList query(String query, String hostName, int portNumber, String collectionName, int rows, int start) {
		HttpSolrServer server;
		try {
			server = new HttpSolrServer("http://"+hostName+":"+portNumber+"/solr/"+collectionName);

			//System.out.println("QUERY: "+query);

			SolrQuery solrQuery = new SolrQuery();
					  solrQuery.setQuery(query);
					  solrQuery.setRows(rows);
					  solrQuery.setIncludeScore(true);
					  solrQuery.setStart(start);

			QueryResponse rsp = server.query(solrQuery);
			SolrDocumentList results = rsp.getResults();

			server.close();

			return results;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}