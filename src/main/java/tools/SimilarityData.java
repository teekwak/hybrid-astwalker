package tools;


import entities.JavaClass;
import entities.MethodDeclarationObject;
import entities.SuperEntityClass;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.File;
import java.util.*;

// the purpose of this class is to house both the social and technical data
// the data should be stored in a map <string, list<object>>
// the string is one of the similarity function properties
// we want a list because we can then iterate over the list nicely
class SimilarityData {
	private Map<String, List<Object>> dataMap;
	private String sourceCode;

	SimilarityData(Map<String, Boolean> simProperties) {
		this.dataMap = new HashMap<>();

		for(Map.Entry<String, Boolean> entry : simProperties.entrySet()) {
			if(entry.getValue()) {
				dataMap.put(entry.getKey(), new ArrayList<>());
			}
		}
	}

	List<Object> getValue(String key) {
		return this.dataMap.get(key);
	}

	String getSourceCode() {
		return this.sourceCode;
	}

	// todo: i think i can shorten this. just add directly from the AST walker
	void extractData(FileModel f, String className) {
		JavaClass jc = null;

		for(JavaClass c : f.getJavaClassList()) {
			if(c.getName().equals(className)) {
				jc = c;
			}
		}

		if(jc == null) throw new IllegalArgumentException("[ERROR]: current java class is null");

		this.sourceCode = jc.getSourceCode();

		if(dataMap.containsKey("importsScore")) {
			for(SuperEntityClass importStr : jc.getImportList()) {
				dataMap.get("importsScore").add(importStr.getFullyQualifiedName());
			}
		}

		if(dataMap.containsKey("variableNameScore")) {
			Set<String> variableNames = new HashSet<>();
			getVariablesFromSuperEntityList(jc.getArrayList(), variableNames);
			getVariablesFromMethodDeclaration(jc.getMethodDeclarationList(), variableNames);
			dataMap.get("variableNameScore").addAll(variableNames);
		}

		if(dataMap.containsKey("classNameScore")) {
			dataMap.get("classNameScore").add(jc.getFullyQualifiedName());
		}

		if(dataMap.containsKey("methodCallScore")) {
			dataMap.get("methodCallScore").addAll(new HashSet<>(jc.getMethodInvocationNames()));
		}

		if(dataMap.containsKey("methodDecScore")) {
			dataMap.get("methodDecScore").addAll(new HashSet<>(jc.getMethodDeclarationNames()));
		}

		if(dataMap.containsKey("sizeScore")) {
			dataMap.get("sizeScore").add(jc.getSourceCode().length());
		}

		if(dataMap.containsKey("importNumScore")) {
			dataMap.get("importNumScore").add(((Number)jc.getImportList().size()).longValue());
		}

		if(dataMap.containsKey("complexityScore")) {
			dataMap.get("complexityScore").add(((Number)jc.getCyclomaticComplexity()).longValue());
		}

		if(dataMap.containsKey("extendsScore")) {
			dataMap.get("extendsScore").add(jc.getSuperClass());
		}

		if(dataMap.containsKey("packageScore")) {
			dataMap.get("packageScore").add(jc.getPackage().getFullyQualifiedName());
		}

		if(dataMap.containsKey("fieldsScore")) {
			dataMap.get("fieldsScore").add(((Number)jc.getGlobalList().size()).longValue());
		}

		if(dataMap.containsKey("isGenericScore")) {
			dataMap.get("isGenericScore").add(jc.getIsGenericType());
		}

		if(dataMap.containsKey("isAbstractScore")) {
			dataMap.get("isAbstractScore").add(jc.getIsAbstract());
		}

		if(dataMap.containsKey("isWildCardScore")) {
			boolean has_wildcard_method = false;
			for(SuperEntityClass md : jc.getMethodDeclarationList()) {
				if(((MethodDeclarationObject) md).getWildcardList().size() > 0) {
					has_wildcard_method =  true;
					break;
				}
			}

			dataMap.get("isWildCardScore").add(has_wildcard_method);
		}
	}

	/**
	 * xxx
	 * @param methodDeclarationList xxx
	 * @param variableNames xxx
	 */
	private void getVariablesFromMethodDeclaration(List<SuperEntityClass> methodDeclarationList, Set<String> variableNames) {
		for(SuperEntityClass methodDec : methodDeclarationList) {
			MethodDeclarationObject mdo = (MethodDeclarationObject) methodDec;
			getVariablesFromSuperEntityList(mdo.getArrayList(), variableNames);
			getVariablesFromSuperEntityList(mdo.getGenericsList(), variableNames);
			getVariablesFromSuperEntityList(mdo.getPrimitiveList(), variableNames);
			getVariablesFromSuperEntityList(mdo.getSimpleList(), variableNames);

			if(mdo.getMethodDeclarationList().size() > 0) {
				getVariablesFromMethodDeclaration(mdo.getMethodDeclarationList(), variableNames);
			}
		}
	}


	/**
	 * xxx
	 * @param list xxx
	 * @param variableNames xxx
	 */
	private void getVariablesFromSuperEntityList(List<SuperEntityClass> list, Set<String> variableNames) {
		for(SuperEntityClass entity : list) {
			variableNames.add(entity.getName());
		}
	}

	void extractData(JavaGitHubData jghd, String cloneDirectory, String fileRepoPath, File classFile) {
		jghd.getCommits(cloneDirectory, fileRepoPath, classFile);
		Commit headCommit = jghd.getListOfCommits().get(jghd.getListOfCommits().size() - 1);
		dataMap.put("authorScore", Collections.singletonList(headCommit.getAuthor()));
	}

	void extractProjectData(String projectURL, String passPath) {
		String htmlURL = "\""+projectURL+"\"";
		String hostName = "codeexchange.ics.uci.edu";
		int portNumber = 9001;
		String collectionName = "githubprojects";

		SolrDocumentList list = Solrj.getInstance(passPath).query("id:" + htmlURL, hostName, portNumber, collectionName, 1);
		if(list.isEmpty()) throw new IllegalArgumentException("[ERROR]: project data is null!");

		SolrDocument doc = list.get(0);

		if(dataMap.containsKey("ownerScore")) {
			dataMap.put("ownerScore", Collections.singletonList(doc.getFieldValue("userName").toString()));
		}

		if(dataMap.containsKey("projectScore")) {
			dataMap.put("projectScore", Collections.singletonList(doc.getFieldValue("projectName").toString()));
		}
	}
}
