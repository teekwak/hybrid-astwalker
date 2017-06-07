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

import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrInputDocument;
import similarity.SimilarityASTWalker;

import java.io.File;
import java.io.IOException;

public class TechnicalPropertyParser implements IPropertyParser {
	private SolrInputDocument solrDoc;
	private String classFilePath;
	private String className;

	public TechnicalPropertyParser(SolrInputDocument s, String cfp, String cn) {
		this.solrDoc = s;
		this.classFilePath = cfp;
		this.className = cn;
	}

	// get source code from file
	private String getSourceCode() {
		String sourceCode = null;
		try {
			sourceCode = FileUtils.readFileToString(new File(this.classFilePath), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.solrDoc.addField("snippet_code", sourceCode);
		return sourceCode;
	}

	// basically, take in a solr doc, add stuff, return solr doc. static method
	public SolrInputDocument getProperties() {
		SimilarityASTWalker saw = new SimilarityASTWalker(this.className);
		saw.parseFileIntoSolrDoc(this.solrDoc, this.getSourceCode(), this.classFilePath); // this confuses me
		saw = null;
		return solrDoc;
	}
}
