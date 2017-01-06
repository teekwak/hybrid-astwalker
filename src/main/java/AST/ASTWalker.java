package AST;

import entities.*;
import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrInputDocument;
import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.*;

/**
 * @author Thomas Kwak
 *
 * Table of Contents (out of date)
 *
 * 1. Class variables...........25 x
 * 2. Constructor...............41 x
 *
 * 3. validateProperties()......45 x
 * 4. parseFile()...............63 x
 * 5. test1()...................96 x
 * 6. main()....................121 x
 *
 */

@SuppressWarnings("Duplicates") // todo remove later
public class ASTWalker {
	private Map<String, Boolean> configProperties;
	private String containingClass;
	private Stack<Entity> entityStack;
	private boolean inMethod;



	private Set<String> methodInvocationNames;
	private Set<String> methodDeclarationNames;
	private Set<String> variableNames;
	private Set<String> importNames;
	private Set<String> fieldNames;
	private boolean isWildCard;

	private int cyclomaticComplexity;


	/**
	 * Constructor for class. Creates a FileModel object and takes in a config file and adds into a HashMap
	 *
	 * @param config hash map passed in from IndexManager class
	 */
	public ASTWalker(Map<String, Boolean> config) {
		this.containingClass = "";
		this.entityStack = new Stack<>();
		this.inMethod = false;
		this.configProperties = config;

		this.methodInvocationNames = new HashSet<>();
		this.methodDeclarationNames = new HashSet<>();
		this.variableNames = new HashSet<>();
		this.importNames = new HashSet<>();
		this.fieldNames = new HashSet<>();
		this.cyclomaticComplexity = 1; // cyclomatic complexity = 1 + if + for + while + switch case + catch + and + or + ternary + inner cyclomatic complexities
}


	/**
	 *
	 */
	public SolrInputDocument parseFileIntoSolrDoc(String url, String fileLocation) {
		SolrInputDocument solrDoc = new SolrInputDocument();
		solrDoc.addField("id", url);

		// no way to avoid this. we need to read the entire file into memory
		String sourceCode = null;
		try {
			sourceCode = FileUtils.readFileToString(new File(fileLocation), "ISO-8859-1");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// just in case somehow things escape the try-catch statement
		if(sourceCode == null) {
			throw new IllegalStateException("[ERROR]: source code is null!");
		}

		solrDoc.addField("snippet_code", sourceCode);

		// create parser and set properties
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setUnitName(fileLocation);
		parser.setEnvironment(null, null, null, true);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {
			// actually gonna need inner classes after all
//			public boolean visit(AnonymousClassDeclaration node) {
//				JavaClass co = new JavaClass();
//
//				if(configProperties.get("AnonymousClassDeclaration")) {
//					ITypeBinding binding = node.resolveBinding();
//
//					int startLine = cu.getLineNumber(node.getStartPosition());
//					int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);
//
//					co.setIsAnonymous(binding.isAnonymous());
//					co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
//					co.setEndLine(endLine);
//					co.setLineNumber(startLine);
//					co.setNumberOfCharacters(node.getLength());
//					co.setFileName(fileLocation);
//					// get generic parameters
//					List<String> genericParametersList = new ArrayList<>();
//					try {
//						if(binding.isGenericType()) {
//							co.setIsGenericType(binding.isGenericType());
//							for(Object o : binding.getTypeParameters()) {
//								genericParametersList.add(o.toString());
//							}
//						}
//					} catch (NullPointerException e) {
//						co.setIsGenericType(false);
//					}
//					co.setGenericParametersList(genericParametersList);
//
//					co.setHasComments(hasComments);
//					co.setSourceCode(getClassSourceCode(fileLocation, startLine, endLine));
//					co.setStartCharacter(node.getStartPosition());
//					co.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
//					co.setImportList(importList);
//					co.setPackage(packageObject);
//					co.setIsAnonymous(true);
//				}
//
//				entityStack.push(co);
//
//				return true;
//			}
//
//			// TODO
//			// nothing changed
//			public void endVisit(AnonymousClassDeclaration node) {
//				if(configProperties.get("AnonymousClassDeclaration")) {
//					JavaClass temp = (JavaClass) entityStack.pop();
//
//					temp.setIsInnerClass(true);
//
//					temp.setComplexities();
//					temp.setMethodDeclarationNames();
//					temp.setMethodInvocationNames();
//
//					if(!containingClass.isEmpty()) {
//						temp.setContainingClass(containingClass);
//					}
//
//					try {
//						entityStack.peek().addEntity(temp, Entity.EntityType.CLASS);
//					} catch (EmptyStackException e) {
//						// should not be possible
//					}
//
//					fileModel.addJavaClass(temp);
//
//					hasComments = false;
//				}
//			}


			// todo: change configproperties to simproperties
			public boolean visit(CatchClause node) {
				if(inMethod && configProperties.get("CatchClause")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			public boolean visit(ConditionalExpression node){
				if(inMethod && configProperties.get("ConditionalExpression")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			public boolean visit(DoStatement node) {
				if(inMethod && configProperties.get("DoStatement")) {
					cyclomaticComplexity++;
				}
				return true;
			}

			public boolean visit(EnhancedForStatement node) {
				if(inMethod && configProperties.get("EnhancedForStatement")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			// TODO
			// nothing changed
			public boolean visit(FieldDeclaration node) {
				if(configProperties.get("FieldDeclaration")) {
					for(Object v : node.fragments()) {
						SimpleName name = ((VariableDeclarationFragment) v).getName();

						// get fully qualified name
						ITypeBinding binding = node.getType().resolveBinding();
						String fullyQualifiedName;
						try {
							fullyQualifiedName = binding.getQualifiedName();
						} catch (NullPointerException e) {
							fullyQualifiedName = name.toString();
						}

						fieldNames.add(fullyQualifiedName);
					}
				}

				return true;
			}

			public boolean visit(ForStatement node) {
				if(inMethod && configProperties.get("ForStatement")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			public boolean visit(IfStatement node) {
				if(inMethod && configProperties.get("IfStatement")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			public boolean visit(ImportDeclaration node) {
				if(configProperties.get("ImportDeclaration")) {
					Name name = node.getName();

					String fullyQualifiedName;
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = "";
					}

					importNames.add(fullyQualifiedName);
				}

				return true;
			}

			public boolean visit(InfixExpression node){
				if(inMethod && configProperties.get("InfixExpression")) {
					if(node.getOperator().toString().equals("&&") || node.getOperator().toString().equals("||")) {
						cyclomaticComplexity++;
					}
				}

				return true;
			}

			// TODO
			// nothing has changed
			// actually, should we only be checking the parse boolean when we SAVE the method dec?
			// this is because we always have to visit the method, but we dont necessarily have to save it
			public boolean visit(MethodDeclaration node) {
				inMethod = true;
				MethodDeclarationObject md = new MethodDeclarationObject();

				if(configProperties.get("MethodDeclaration")) {
					SimpleName name = node.getName();

					// get fully qualified name
					String fullyQualifiedName;
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = name.toString();
					}

					methodDeclarationNames.add(fullyQualifiedName);
				}

				entityStack.push(md);

				return true;
			}

			// TODO
			// same comment as above, should we just pop and forget about it?
			// that means we should push it, but then just pop it when we leave
			// just don't record anything???!?!?!?!
			//
			// i think i fixed it
			public void endVisit(MethodDeclaration node) {
				MethodDeclarationObject temp = (MethodDeclarationObject) entityStack.pop();

				if(configProperties.get("MethodDeclaration")) {
					// TODO
					// merge these two methods together
					temp.setComplexities();
					temp.setMethodDeclarationNames();
					temp.setMethodInvocationNames();
//					entityStack.peek().addEntity(temp, Entity.EntityType.METHOD_DECLARATION);
				}

				inMethod = false;
			}

			public boolean visit(MethodInvocation node) {
				if(configProperties.get("MethodInvocation")) {
					SimpleName name = node.getName();

					// get fully qualified name
					String fullyQualifiedName;
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = name.toString();
					}

					methodInvocationNames.add(fullyQualifiedName);
				}

				return true;
			}

			public boolean visit(PackageDeclaration node){
				if(configProperties.get("PackageDeclaration")) {
					Name name = node.getName();

					// get fully qualified name
					String fullyQualifiedName;
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = "";
					}

					solrDoc.addField("packageScore", fullyQualifiedName);
				}

				return true;
			}

			public boolean visit(SingleVariableDeclaration node) {
				if(configProperties.get("SingleVariableDeclaration")) {
					SimpleName name = node.getName();

					// get fully qualified name
					ITypeBinding binding = node.getType().resolveBinding();
					String fullyQualifiedName;
					try {
						fullyQualifiedName = binding.getQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = name.toString();
					}

					variableNames.add(fullyQualifiedName);
				}

				return true;
			}

			public boolean visit(SwitchStatement node) {
				if(inMethod && configProperties.get("SwitchStatement")) {
					for(Object s : node.statements()) {
						if(s instanceof SwitchCase) {
							String expression;
							try {
								expression = ((SwitchCase) s).getExpression().toString();
								cyclomaticComplexity++;
							} catch (NullPointerException e) {
								expression = "Default";
							}
						}
					}
				}

				return true;
			}

			// TODO: ???
			public boolean visit(TypeDeclaration node) {
				JavaClass co = new JavaClass();

				if(configProperties.get("TypeDeclaration")) {
					if(node.isInterface()) {
						return false;
					}

					else {
						int startLine = cu.getLineNumber(node.getStartPosition());
						int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);

						ITypeBinding binding = node.resolveBinding();

						// get fully qualified name
						String fullyQualifiedName;
						try {
							fullyQualifiedName = node.getName().getFullyQualifiedName();
						} catch (NullPointerException e) {
							fullyQualifiedName = node.getName().toString();
						}

						solrDoc.addField("classNameScore", fullyQualifiedName);
						solrDoc.addField("isGenericScore", binding.isGenericType());
						solrDoc.addField("sizeScore", node.getLength());

						if(node.getSuperclassType() != null) {
							solrDoc.addField("extendsScore", node.getSuperclassType().toString());
						}

						if(Modifier.isAbstract(node.getModifiers())) {
							solrDoc.addField("isAbstractScore", true);
						}
						else {
							solrDoc.addField("isAbstractScore", false);
						}
					}
				}

				entityStack.push(co);

				return true;
			}

			// TODO: ???
			public void endVisit(TypeDeclaration node) {
				JavaClass temp = (JavaClass) entityStack.pop();

				if(!node.isInterface() && configProperties.get("TypeDeclaration")) {
					// check if current class is an inner class
					boolean isInnerClass = true;
					try {
						entityStack.peek();
					} catch (EmptyStackException e) {
						isInnerClass = false;
					}
					temp.setIsInnerClass(isInnerClass);

					temp.setComplexities();
					temp.setMethodDeclarationNames();
					temp.setMethodInvocationNames();

					try {
						if(!containingClass.isEmpty()) {
							temp.setContainingClass(containingClass);
						}
//						entityStack.peek().addEntity(temp, Entity.EntityType.CLASS);

					} catch (EmptyStackException e) {
						containingClass = "";
					}
				}
			}

			public boolean visit(VariableDeclarationStatement node) {
				if(configProperties.get("VariableDeclarationStatement")) {
					for(Object v : node.fragments()) {
						SimpleName name = ((VariableDeclarationFragment) v).getName();

						// get fully qualified name
						ITypeBinding binding = node.getType().resolveBinding();
						String fullyQualifiedName;
						try {
							fullyQualifiedName = binding.getQualifiedName();
						} catch (NullPointerException e) {
							fullyQualifiedName = name.toString();
						}

						// todo
						variableNames.add(fullyQualifiedName);
					}
				}

				return true;
			}

			public boolean visit(VariableDeclarationExpression node) {
				if(configProperties.get("VariableDeclarationExpression")) {
					for(Object v : node.fragments()) {
						SimpleName name = ((VariableDeclarationFragment) v).getName();

						// get fully qualified name
						ITypeBinding binding = node.getType().resolveBinding();
						String fullyQualifiedName;
						try {
							fullyQualifiedName = binding.getQualifiedName();
						} catch (NullPointerException e) {
							fullyQualifiedName = name.toString();
						}

						variableNames.add(fullyQualifiedName);
					}
				}

				return true;
			}

			public boolean visit(WhileStatement node){
				if(inMethod && configProperties.get("WhileStatement")) {
					cyclomaticComplexity++;
				}

				return true;
			}

			// TODO: ????????
			// unchanged
			public boolean visit(WildcardType node) {
				if(inMethod && configProperties.get("WildcardType")) {
					isWildCard = true;
				}

				return false;
			}

		});

		// we should be adding names at the end because we want to keep a SET
		// todo: need if statements everywhere, or is iterating over an empty set okay?
		// todo: pass in id from index manager
		// todo: complexityScore, upload source code
		//
		solrDoc.addField("importNumScore", importNames.size());
		solrDoc.addField("fieldsScore", fieldNames.size());
		solrDoc.addField("wildcardScore", isWildCard);

		for(String name : importNames) {
			solrDoc.addField("importsScore", name);
		}

		for(String name : methodInvocationNames) {
			solrDoc.addField("methodCallScore", name);
		}

		for(String name : methodDeclarationNames) {
			solrDoc.addField("methodDecScore", name);
		}

		for(String name : variableNames) {
			solrDoc.addField("variableNameScore", name);
		}

		return solrDoc;
	}
}
