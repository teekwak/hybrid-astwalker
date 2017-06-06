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
import org.eclipse.jdt.core.dom.*;

import java.util.*;

class SimilarityASTWalker {
	private String className;
	private Set<String> methodInvocationNames;
	private Set<String> methodDeclarationNames;
	private Set<String> variableNames;
	private Set<String> importNames;
	private Set<String> fieldNames;
	private boolean isWildCard;

	private int cyclomaticComplexity;


	/**
	 * Constructor for class
	 */
	SimilarityASTWalker(String className) {
		this.className = className;
		this.methodInvocationNames = new HashSet<>();
		this.methodDeclarationNames = new HashSet<>();
		this.variableNames = new HashSet<>();
		this.importNames = new HashSet<>();
		this.fieldNames = new HashSet<>();
		this.cyclomaticComplexity = 1; // cyclomatic complexity = 1 + if + for + while + switch case + catch + and + or + ternary + inner cyclomatic complexities
	}


	/**
	 *
	 * @param solrDoc xxx
	 * @param sourceCode xxx
	 * @param fileLocation xxx
	 */
	void parseFileIntoSolrDoc(SolrInputDocument solrDoc, String sourceCode, String fileLocation) {
		// create parser and set properties
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setUnitName(fileLocation);
		parser.setEnvironment(null, null, null, true);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		// null out source code for possible garbage collection
		sourceCode = null;

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {
			public boolean visit(AnonymousClassDeclaration node) {
				return true;
			}

			public boolean visit(CatchClause node) {
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(ConditionalExpression node){
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(DoStatement node) {
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(EnhancedForStatement node) {
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(FieldDeclaration node) {
				for(Object v : node.fragments()) {
					SimpleName name = ((VariableDeclarationFragment) v).getName();
					fieldNames.add(name.toString());
				}

				return true;
			}

			public boolean visit(ForStatement node) {
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(IfStatement node) {
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(ImportDeclaration node) {
				Name name = node.getName();

				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}

				importNames.add(fullyQualifiedName);

				return true;
			}

			public boolean visit(InfixExpression node){
				if(node.getOperator().toString().equals("&&") || node.getOperator().toString().equals("||")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			public boolean visit(MethodDeclaration node) {
				SimpleName name = node.getName();
				methodDeclarationNames.add(name.toString());

				return true;
			}

			public boolean visit(MethodInvocation node) {
				SimpleName name = node.getName();
				methodInvocationNames.add(name.toString());

				return true;
			}

			public boolean visit(PackageDeclaration node){
				Name name = node.getName();

				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}

				solrDoc.addField("snippet_package", fullyQualifiedName);

				return true;
			}

			public boolean visit(SingleVariableDeclaration node) {
				SimpleName name = node.getName();
				variableNames.add(name.toString());

				return true;
			}

			public boolean visit(SwitchStatement node) {
				for(Object s : node.statements()) {
					if(s instanceof SwitchCase) {
						try {
							// if failure to get expression, then do not increase cyclomatic complexity
							String expression = ((SwitchCase) s).getExpression().toString();
							cyclomaticComplexity++;
						} catch (NullPointerException e) {
							// do nothing
						}
					}
				}

				return true;
			}

			public boolean visit(TypeDeclaration node) {
				if(node.isInterface()) {
					return false;
				} else {
					if(node.getName().toString().equals(className)) {
						ITypeBinding binding = node.resolveBinding();

						String fullyQualifiedName;
						try {
							fullyQualifiedName = node.getName().getFullyQualifiedName();
						} catch (NullPointerException e) {
							fullyQualifiedName = node.getName().toString();
						}

						solrDoc.addField("snippet_class_name", fullyQualifiedName);
						solrDoc.addField("snippet_is_generic", binding.isGenericType());
						solrDoc.addField("snippet_size", node.getLength());

						if(node.getSuperclassType() != null) {
							solrDoc.addField("snippet_extends", node.getSuperclassType().toString());
						} else {
							solrDoc.addField("snippet_extends", "");
						}

						if(Modifier.isAbstract(node.getModifiers())) {
							solrDoc.addField("snippet_is_abstract", true);
						}
						else {
							solrDoc.addField("snippet_is_abstract", false);
						}

					}
				}

				return true;
			}

			public boolean visit(VariableDeclarationStatement node) {
				for(Object v : node.fragments()) {
					SimpleName name = ((VariableDeclarationFragment) v).getName();
					variableNames.add(name.toString());
				}

				return true;
			}

			public boolean visit(VariableDeclarationExpression node) {
				for(Object v : node.fragments()) {
					SimpleName name = ((VariableDeclarationFragment) v).getName();
					variableNames.add(name.toString());
				}

				return true;
			}

			public boolean visit(WhileStatement node){
				cyclomaticComplexity++;
				return true;
			}

			public boolean visit(WildcardType node) {
				isWildCard = true;
				return false;
			}
		});

		solrDoc.addField("snippet_imports_count", importNames.size());
		for(String name : importNames) {
			solrDoc.addField("snippet_imports", name);
		}
		importNames = null;

		solrDoc.addField("snippet_number_of_fields", fieldNames.size());
		fieldNames = null;

		for(String name : methodInvocationNames) {
			solrDoc.addField("snippet_method_invocation_names", name);
		}
		methodInvocationNames = null;

		for(String name : methodDeclarationNames) {
			solrDoc.addField("snippet_method_dec_names", name);
		}
		methodDeclarationNames = null;

		for(String name : variableNames) {
			solrDoc.addField("snippet_variable_names", name);
		}
		variableNames = null;

		solrDoc.addField("snippet_is_wildcard", isWildCard);
		solrDoc.addField("snippet_path_complexity_class_sum", cyclomaticComplexity);
	}
}
