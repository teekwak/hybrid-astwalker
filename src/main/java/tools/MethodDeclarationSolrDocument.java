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

import entities.MethodDeclarationObject;
import entities.SuperEntityClass;
import org.apache.solr.common.SolrInputDocument;

import java.util.HashMap;
import java.util.Map;

class MethodDeclarationSolrDocument {
	private SolrInputDocument doc;
	private Map<String, String> configProperties; // todo: i don't think this is string string. it should be string boolean

	MethodDeclarationSolrDocument(Map<String, String> configProperties) {
		this.doc = new SolrInputDocument();
		this.configProperties = configProperties;
	}

	void addTechnicalData(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
		MethodDeclarationObject mdo = (MethodDeclarationObject) entity;

		String idDec =  id + "&methodStart=" + entity.getStartCharacter() + "&methodEnd=" + entity.getEndCharacter();
		this.doc.addField("id", idDec);
		this.doc.addField(SolrKey.EXPAND_ID, id);

		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_NAME, mdo.getFullyQualifiedName());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_NAME_DELIMITED, mdo.getName());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_START, ((Number)mdo.getLineNumber()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_END, ((Number)mdo.getEndLine()).longValue());

		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_ABSTRACT, mdo.getIsAbstract());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_CONSTRUCTOR, mdo.getIsConstructor());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_GENERIC, mdo.getIsGenericType());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_STATIC, mdo.getIsStatic());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_VAR_ARGS, mdo.getIsVarargs());

		this.doc.addField("parent",false);
		this.doc.addField("is_method_dec_child",true);

		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PATH_COMPLEXITY, ((Number)mdo.getCyclomaticComplexity()).longValue());

		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_WHILE_COUNT, ((Number)mdo.getWhileStatementList().size()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_FOR_COUNT, ((Number)mdo.getForStatementList().size()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IF_COUNT, ((Number)mdo.getIfStatementList().size()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_CASE_COUNT, ((Number)mdo.getSwitchCaseList().size()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_TERNARY_COUNT, ((Number)mdo.getConditionalExpressionList().size()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_CATCH_COUNT, ((Number)mdo.getCatchClauseList().size()).longValue());
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_LOGICAL_COUNT, ((Number)mdo.getInfixExpressionList().size()).longValue());

		// this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_RECURSIVE, dec.isRecurisive);

		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_DECLARING_CLASS, mdo.getDeclaringClass());

		if(mdo.getDeclaringClass() != null){
			String declaringClass = mdo.getDeclaringClass();

			if(declaringClass.indexOf('<') > -1) {
				String[] split1 = declaringClass.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				String declaringClassShort = split2[split2.length - 1] + "<" + split1[split1.length - 1];
				this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, declaringClassShort);
			}
			else {
				String[] split3 = declaringClass.split("[.]");
				this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT, split3[split3.length-1]);
			}
		}

		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_RETURN_TYPE, mdo.getReturnType());

		int localVariableCount = mdo.getArrayList().size() + mdo.getGenericsList().size() + mdo.getPrimitiveList().size() + mdo.getSimpleList().size() - mdo.getParametersList().size();
		this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES, ((Number)localVariableCount).longValue());

		for(String typeParam : mdo.getGenericParametersList()) {
			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS, typeParam);
		}

		for(SuperEntityClass wc : mdo.getWildcardList()) {
			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS, wc.getBound());
		}

		Map<String, Integer> paramCount = new HashMap<>();
		Map<String, Integer> paramCountShort = new HashMap<>();

		int parameterTypesListSize = mdo.getParameterTypesList().size();
		for(int i = 0; i < parameterTypesListSize; i++) {
			String argType = mdo.getParameterTypesList().get(i);

			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES, argType);
			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE, argType+"_"+i);

			String shortName2;

			if(argType.indexOf('<') > -1) {
				String[] split1 = argType.split("<", 2);
				String[] split2 = split1[0].split("[.]");
				shortName2 = split2[split2.length - 1] + "<" + split1[split1.length - 1];
			}
			else {
				String[] split = argType.split("[.]");
				shortName2 = split[split.length-1];
			}

			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT, shortName2);
			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE, shortName2+"_"+i);

			if(paramCount.get(argType) == null) {
				paramCount.put(argType, 1);
			}
			else {
				int count = paramCount.get(argType) + 1;
				paramCount.put(argType, count);
			}

			if(paramCountShort.get(shortName2) == null) {
				paramCountShort.put(shortName2, 1);
			}
			else {
				int count = paramCountShort.get(shortName2) + 1;
				paramCountShort.put(shortName2, count);
			}
		}

		for (Map.Entry<String, Integer> entry : paramCount.entrySet()) {
			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT, entry.getKey() + "_" + entry.getValue());
		}

		for(Map.Entry<String, Integer> entry : paramCountShort.entrySet()) {
			this.doc.addField(SolrKey.SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT, entry.getKey() + "_"+ entry.getValue());
		}

		solrDoc.addChildDocument(this.doc);
	}
}
