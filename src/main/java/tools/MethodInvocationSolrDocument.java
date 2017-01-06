///* *****************************************************************************
// * Copyright (c) {2017} {Software Design and Collaboration Laboratory (SDCL)
// *				, University of California, Irvine}.
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// *    {Software Design and Collaboration Laboratory (SDCL)
// *	, University of California, Irvine}
// *			- initial API and implementation and/or initial documentation
// *******************************************************************************/
//
///*
// * Created by Thomas Kwak
// */
//
//package tools;
//
//import entities.MethodInvocationObject;
//import entities.SuperEntityClass;
//import org.apache.solr.common.SolrInputDocument;
//
//import java.util.HashMap;
//import java.util.Map;
//
//class MethodInvocationSolrDocument {
//	private SolrInputDocument doc;
//	private Map<String, Boolean> simProperties; // todo: i don't think this is string string. it should be string boolean
//
//	MethodInvocationSolrDocument(Map<String, Boolean> simProperties) {
//		this.doc = new SolrInputDocument();
//		this.simProperties = simProperties;
//	}
//
//	void addTechnicalData(SuperEntityClass entity, SolrInputDocument solrDoc, String id) {
//		SolrInputDocument methodInvSolrDoc = new SolrInputDocument();
//		MethodInvocationObject mio = (MethodInvocationObject) entity;
//
//		methodInvSolrDoc.addField("parent",false);
//		methodInvSolrDoc.addField("is_method_invocation_child",true);
//
//		String idInvo = id + "&start=" + entity.getStartCharacter() + "&end=" + entity.getEndCharacter();
//		methodInvSolrDoc.addField("id", idInvo);
//		methodInvSolrDoc.addField(SolrKey.EXPAND_ID, id);
//
//		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_NAME, mio.getFullyQualifiedName());
//		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_NAME_DELIMITED, mio.getName());
//		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_START, ((Number)mio.getLineNumber()).longValue());
//		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_END, ((Number)mio.getEndLine()).longValue());
//
//		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_CALLING_CLASS, mio.getCallingClass());
//
//		if(!mio.getCallingClass().isEmpty()){
//			String callingClass = mio.getCallingClass();
//
//			if(callingClass.indexOf('<') > -1) {
//				String[] split1 = callingClass.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = callingClass.split("[.]");
//				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT, split[split.length-1]);
//			}
//		}
//
//		methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS, mio.getDeclaringClass());
//
//		if(!mio.getDeclaringClass().isEmpty()){
//			String declaringClass = mio.getDeclaringClass();
//
//			if(declaringClass.indexOf('<') > -1) {
//				String[] split1 = declaringClass.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split2[split2.length - 1] + "<" + split1[split1.length - 1]);
//			}
//			else {
//				String[] split = declaringClass.split("[.]");
//				methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT, split[split.length-1]);
//			}
//		}
//
//		Map<String, Integer> paramCount = new HashMap<>();
//		Map<String, Integer> paramCountShort = new HashMap<>();
//
//		int place = 0;
//		for(String argType: mio.getArgumentTypes()){
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES, argType);
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE, argType + "_" + place);
//
//			String shortName2;
//			if(argType.indexOf('<') > -1) {
//				String[] split1 = argType.split("<", 2);
//				String[] split2 = split1[0].split("[.]");
//				shortName2 = split2[split2.length - 1] + "<" + split1[split1.length - 1];
//			}
//			else {
//				String[] split = argType.split("[.]");
//				shortName2 = split[split.length - 1];
//			}
//
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT, shortName2);
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE, shortName2 + "_" + place);
//
//			if(paramCount.get(argType) == null) {
//				paramCount.put(argType, 1);
//			}
//			else {
//				int count = paramCount.get(argType)+1;
//				paramCount.put(argType, count);
//			}
//
//			if(paramCountShort.get(shortName2) == null) {
//				paramCountShort.put(shortName2, 1);
//			}
//			else {
//				int count = paramCountShort.get(shortName2)+1;
//				paramCountShort.put(shortName2, count);
//			}
//			place++;
//		}
//
//		for(Map.Entry<String, Integer> entry : paramCount.entrySet()) {
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT, entry.getKey() + "_" + entry.getValue());
//		}
//
//		for(Map.Entry<String, Integer> entry : paramCountShort.entrySet()) {
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT, entry.getKey() + "_" + entry.getValue());
//		}
//
//		for(Object argValue: mio.getArguments()){
//			methodInvSolrDoc.addField(SolrKey.SNIPPET_METHOD_INVOCATION_ARG_VALUES, argValue.toString());
//		}
//
//		solrDoc.addChildDocument(methodInvSolrDoc);
//	}
//
//}
