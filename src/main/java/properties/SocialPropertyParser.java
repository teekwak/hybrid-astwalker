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

package properties;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import github.Commit;
import github.JavaGitHubData;
import similarity.Solrj;

public class SocialPropertyParser implements IPropertyParser {
	private SolrInputDocument solrDoc;
	private String currentURL;
	private String passPath;
	private String clonePath;
	private String[] urlSplit;

	public SocialPropertyParser(SolrInputDocument s, String c, String pp, String cp) {
		this.solrDoc = s;
		this.currentURL = c;
		this.passPath = pp;
		this.clonePath = cp;
	}

	/**
	 * Get the relative path to the file in the repository
	 * @param url xxx
	 * @return xxx
	 */
	private String getRelativeFileRepoPath(String url) {
		this.urlSplit = url.split("/");
		StringBuilder repoPath = new StringBuilder();
		for(int i = 6; i < this.urlSplit.length - 1; i++) {
			repoPath.append(this.urlSplit[i]);
			repoPath.append("/");
		}
		repoPath.append(this.urlSplit[this.urlSplit.length - 1].split("\\?")[0]);
		return repoPath.toString();
	}

	public SolrInputDocument getProperties() {
		// getting the author name
		JavaGitHubData jghd = new JavaGitHubData(this.clonePath);
		jghd.getCommits(this.getRelativeFileRepoPath(this.currentURL));

		Commit headCommit = jghd.getListOfCommits().get(jghd.getListOfCommits().size() - 1);
		this.solrDoc.addField("snippet_author_name", headCommit.getAuthor());
		jghd = null;
		headCommit = null;

		// getting the project name and project owner
		SolrDocumentList list = Solrj.getInstance(this.passPath).query("id:\"https://github.com/" + this.urlSplit[3] + "/" + this.urlSplit[4]+"\"", "grok.ics.uci.edu", 9001, "githubprojects", 1);
		if(list.isEmpty()) throw new IllegalArgumentException("[ERROR]: project data is null!");
		SolrDocument doc = list.get(0);
		this.solrDoc.addField("snippet_project_owner", doc.getFieldValue("userName").toString());
		this.solrDoc.addField("snippet_project_name", doc.getFieldValue("projectName").toString());
		list = null;
		doc = null;

		return this.solrDoc;
	}
}
