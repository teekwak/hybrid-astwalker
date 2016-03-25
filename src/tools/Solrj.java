package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

public class Solrj {

	private String queryStr;
	private static Solrj instance = null;
	private SolrDocumentList recommendations = new SolrDocumentList();
	private static String pass;
	private String host = "codeexchange.ics.uci.edu";
	public UpdateRequest req = new UpdateRequest();
	
	private Solrj() {
	}
	
	public static Solrj getInstance(){
		if(instance == null){
			instance = new Solrj();
		}
		
		File file = new File("/home/kwak/Pass");
		
		Scanner scan;
		try {
			scan = new Scanner(file);

			while(scan.hasNextLine()){
				pass = scan.nextLine();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return instance;
	}


	public Solrj(String queryStr) {
		this.queryStr = queryStr;

		System.out.println("okay");

	} 

    protected class PreEmptiveBasicAuthenticator implements HttpRequestInterceptor {
        private final UsernamePasswordCredentials credentials;

        public PreEmptiveBasicAuthenticator(String user, String pass) {
          credentials = new UsernamePasswordCredentials(user, pass);
        }

 


	public void process(HttpRequest request, HttpContext context)
            throws HttpException, IOException {
          request.addHeader(BasicScheme.authenticate(credentials,"US-ASCII",false));
        }
      }


    /**
     * create a doc with the same id as the one you want to update and pass for field doc
     * ids must be unique in your solr collection
     * @param field
     * @param value
     * @param doc
     * @param collectionName
     */
//    public void updateDocList(String field, List<String> value, SolrInputDocument doc,String collectionName){
//    	
//    	Map<String, List<String>> partialUpdate = new HashMap<String, List<String>>();
//    	//use set to update a value, add to add a value (
//    	//To update a multi-valued field with multiple values, create a Map<String, List<A>>)
//    	//, and inc to increment a value
//    	partialUpdate.put("set", value);
//    	doc.addField(field, partialUpdate);
//    	
//    //	System.out.println("found doc! "+ doc.getFieldValue("id") +" "+doc.getFieldValue(field));
//    	
//    	//now upload
//    	addDoc(doc,collectionName);
//    }
//    
//    public void updateDoc(String field, String value, SolrInputDocument doc,String collectionName){
//    	
//    	Map<String, String> partialUpdate = new HashMap<String, String>();
//    	//use set to update a value, add to add a value (
//    	//To update a multi-valued field with multiple values, create a Map<String, List<A>>)
//    	//, and inc to increment a value
//    	partialUpdate.put("set", value);
//    	doc.addField(field, partialUpdate);
//    	
//    //	System.out.println("found doc! "+ doc.getFieldValue("id") +" "+doc.getFieldValue(field));
//    	
//    	//now upload
//    	addDoc(doc,collectionName);
//    }
    
//	public void addDoc(SolrInputDocument doc,String collectionName) {
//		if(doc == null)
//			return;
//		HttpSolrServer server;
//		server = new HttpSolrServer("http://"+host+":9000/solr/"+collectionName);
//		try {
//
//			
//	    //    AbstractHttpClient httpClient = (AbstractHttpClient) server.getHttpClient(); 
//	   //    httpClient.addRequestInterceptor(new PreEmptiveBasicAuthenticator("admin",""));
//
//	        AbstractHttpClient httpClient = (AbstractHttpClient) server.getHttpClient(); 
//	       httpClient.addRequestInterceptor(new PreEmptiveBasicAuthenticator("admin",pass));
//	       
//			  server.add(doc);
//			 
//			  req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
//			  req.add( doc );
//			  UpdateResponse rsp = req.process( server );
//			  
//		//	  System.out.println(rsp.getResponse());
//		
//			  
//		} catch (SolrServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
	
	public void addDoc(SolrInputDocument doc) {
		
		if(doc == null)
			return;


			  req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
			  req.add( doc );	
			  req.getDocuments().size();

	}

	public void commitDocs(String collectionName, int port){
		System.out.println("===uploading ["+req.getDocuments().size()+"] docs to codeexchange===");
		HttpSolrServer server;
		  server = new HttpSolrServer("http://"+host+":"+port+"/solr/"+collectionName);
		  
		  AbstractHttpClient httpClient = (AbstractHttpClient) server.getHttpClient(); 
	      httpClient.addRequestInterceptor(new PreEmptiveBasicAuthenticator("admin",pass));  
		  
	      //server.add(doc);
		  try {
			UpdateResponse rsp = req.process( server );
			req.clear();
			System.out.println("--- documents remaining after clear:["+req.getDocuments().size()+"]");
			System.out.println("--- child:["+IndexManager.getInstance().CHILD_COUNT+"]");
			IndexManager.getInstance().CHILD_COUNT = 0;

		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public long queryCountDocs() throws IOException{
		HttpSolrServer server;
		
		try { 
			server = new HttpSolrServer("http://"+host+":9452/solr");
			ModifiableSolrParams params = new ModifiableSolrParams();
			params.add("q", "contentID:*");
			QueryResponse rq = server.query(params);
		
			return rq.getResults().getNumFound(); 
		} 
		catch (org.apache.solr.client.solrj.SolrServerException e) { 
			System.err.println("Query problem"); } 
		
		return -1; 
	}
	
	public SolrDocumentList queryFavorites(String collectionName){

		
		File file = new File("data/favorites");

		ArrayList<String> IDs = new ArrayList<String>();
		Scanner scan =  null;
		try {
			scan = new Scanner(file);
			while(scan.hasNext()){
				String id = scan.nextLine();
				IDs.add(id);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			scan.close();
		}
		
		String query = "id:(";
		String idLogic = "";
		for(int i = 0; i<IDs.size();i++){
			String id = IDs.get(i);
			if(i == 0)
				idLogic = idLogic + id;
			else
				idLogic = idLogic +" OR "+id;
		}
		idLogic = idLogic+")";
		query = query+idLogic;
		
		if(IDs.size() == 0)
			return null;
		else
		
		return query(query,collectionName,100);
		
	}
	

	/**
	 * using a filtered query (fq) on the solr is supposed
	 * to improve performance and cache results
	 * @param query
	 * @param fq
	 * @param rows is max results
	 * @return
	 */
	public SolrDocumentList query(String query,String collectionName, int rows) {
		HttpSolrServer server;
		try {
			server = new HttpSolrServer("http://"+host+":9452/solr/"+collectionName);

		//	  System.out.println("QUERY: "+query);
			
			System.out.println("QUERY: "+query);
		
			  SolrQuery solrQuery = new  SolrQuery();
					  solrQuery.setQuery(query);
					  solrQuery.setRows(rows);
					  solrQuery.setIncludeScore(true);
					  
					  
//					  if(fq !=null && fq.length() > 0)
//						  solrQuery.addFilterQuery(fq);
					  
			  QueryResponse rsp = server.query(solrQuery);
			 
		//	ModifiableSolrParams params = new ModifiableSolrParams();

	
//			params.add("q", query);
//		
//			params.set("start", "0");
//			params.set("rows", 20);

		//	QueryResponse response = server.query(params);
			SolrDocumentList results = rsp.getResults();

			
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
	 * @param fq
	 * @param rows is max results
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public SolrDocumentList query(String query,String collectionName, int rows, int start, int port) {
		HttpSolrServer server;
		try {
			server = new HttpSolrServer("http://"+host+":"+port+"/solr/"+collectionName);
			
			System.out.println("QUERY: "+query);
		
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