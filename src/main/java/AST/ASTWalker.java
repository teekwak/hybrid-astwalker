package AST;

import entities.*;
import org.apache.commons.io.FileUtils;
import org.apache.solr.common.SolrInputDocument;
import org.eclipse.jdt.core.dom.*;
import tools.FileModel;

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
	private List<SuperEntityClass> importList;
	private boolean inMethod;
	private SuperEntityClass packageObject;


	private Set<String> methodInvocationNames;
	private Set<String> methodDeclarationNames;
	private Set<String> variableNames;
	private Set<String> importNames;
	private Set<String> fieldNames;

	/**
	 * Constructor for class. Creates a FileModel object and takes in a config file and adds into a HashMap
	 *
	 * @param config hash map passed in from IndexManager class
	 */
	public ASTWalker(Map<String, Boolean> config) {
		containingClass = "";
		entityStack = new Stack<>();
		importList = new ArrayList<>();
		inMethod = false;
		packageObject = new SuperEntityClass();
		this.configProperties = config;

		methodInvocationNames = new HashSet<>();
		methodDeclarationNames = new HashSet<>();
		variableNames = new HashSet<>();
		importNames = new HashSet<>();
		fieldNames = new HashSet<>();
	}

	private String getClassSourceCode(String fileLocation, int startLine, int endLine) {
		// get source code
		StringBuilder classSourceCode = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileLocation)), "ISO-8859-1"))) {
			for(int i = 0; i < startLine - 1; i++) {
				br.readLine();
			}

			for(int j = startLine - 1; j < endLine - 1; j++) {
				classSourceCode.append(br.readLine());
				classSourceCode.append(System.getProperty("line.separator"));
			}

			classSourceCode.append(br.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classSourceCode.toString();
	}

	/**
	 *
	 */
	public SolrInputDocument parseFileIntoSolrDoc(String fileLocation) {
		SolrInputDocument solrDoc = new SolrInputDocument();

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

		solrDoc.addField("snippet_code", sourceCode); // todo this is wrong

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

		// continue code
		// when we are writing visit and exit functions, make sure to use the properties!
		// we want to visit the node (because there will be children inside!) so always return true
		// however, we do not want to record any information
		// but make sure we do not skip over children!

		cu.accept(new ASTVisitor() {

			// TODO
			// nothing changed
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

			// TODO
			// nothing changed
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
//				}
//			}

			public boolean visit(CatchClause node) {
				if(inMethod && configProperties.get("CatchClause")) {
					SimpleName name = node.getException().getName();

//					SuperEntityClass cco =  new SuperEntityClass(
//									name.toString(),
//									cu.getLineNumber(name.getStartPosition()),
//									cu.getColumnNumber(name.getStartPosition())
//					);
//					cco.setType(node.getException().getType());
//					entityStack.peek().addEntity(cco, Entity.EntityType.CATCH_CLAUSE);
				}

				return true;
			}

			public boolean visit(ConditionalExpression node){
				if(inMethod && configProperties.get("ConditionalExpression")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//						new SuperEntityClass(
//							name,
//							cu.getLineNumber(node.getStartPosition()),
//							cu.getColumnNumber(node.getStartPosition())
//						),
//						Entity.EntityType.CONDITIONAL_EXPRESSION
//					);
				}

				return true;
			}

			public boolean visit(DoStatement node) {
				if(inMethod && configProperties.get("DoStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//									new SuperEntityClass(
//													name,
//													cu.getLineNumber(node.getStartPosition()),
//													cu.getColumnNumber(node.getStartPosition())
//									),
//									Entity.EntityType.DO_STATEMENT
//					);
				}
				return true;
			}

			public boolean visit(EnhancedForStatement node) {
				if(inMethod && configProperties.get("EnhancedForStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//									new SuperEntityClass(
//													name,
//													cu.getLineNumber(node.getStartPosition()),
//													cu.getColumnNumber(node.getStartPosition())
//									),
//									Entity.EntityType.FOR_STATEMENT
//					);
				}

				return true;
			}

			// TODO
			// nothing changed
			public boolean visit(FieldDeclaration node) {
				if(configProperties.get("FieldDeclaration")) {
					Type nodeType = node.getType();

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

//						SuperEntityClass fdEntity = new SuperEntityClass(
//										name.toString(),
//										fullyQualifiedName,
//										nodeType,
//										cu.getLineNumber(name.getStartPosition()),
//										cu.getColumnNumber(name.getStartPosition())
//						);

//						if(nodeType.isArrayType()) {
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.ARRAY);
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
//						}
//						else if(nodeType.isParameterizedType()) {
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GENERICS);
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
//						}
//						else if(nodeType.isPrimitiveType()) {
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.PRIMITIVE);
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
//						}
//						else if(nodeType.isSimpleType()) {
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.SIMPLE);
//							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
//						}
//						else {
//							System.out.println("Something is missing " + nodeType);
//						}
					}
				}

				return true;
			}

			public boolean visit(ForStatement node) {
				if(inMethod && configProperties.get("ForStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//									new SuperEntityClass(
//													name,
//													cu.getLineNumber(node.getStartPosition()),
//													cu.getColumnNumber(node.getStartPosition())
//									), Entity.EntityType.FOR_STATEMENT
//					);
				}

				return true;
			}

			public boolean visit(IfStatement node) {
				if(inMethod && configProperties.get("IfStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//						new SuperEntityClass(
//							name,
//							cu.getLineNumber(node.getStartPosition()),
//							cu.getColumnNumber(node.getStartPosition())
//						), Entity.EntityType.IF_STATEMENT
//					);
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

//					importList.add(
//						new SuperEntityClass(
//							name.toString(),
//							fullyQualifiedName,
//							cu.getLineNumber(name.getStartPosition()),
//							cu.getColumnNumber(name.getStartPosition())
//						)
//					);
				}

				return true;
			}

			public boolean visit(InfixExpression node){
				if(inMethod && configProperties.get("InfixExpression")) {
//					entityStack.peek().addEntity(
//						new SuperEntityClass(
//							node.getOperator().toString(),
//							cu.getLineNumber(node.getLeftOperand().getStartPosition()),
//							cu.getColumnNumber(node.getLeftOperand().getStartPosition())
//						),
//						Entity.EntityType.INFIX_EXPRESSION
//					);
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
//					boolean isStatic = false;
//					boolean isAbstract = false;

					// get fully qualified name
					String fullyQualifiedName;
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = name.toString();
					}

					methodDeclarationNames.add(fullyQualifiedName);

//					// is method declaration abstract?
//					int mod = node.getModifiers();
//					if(Modifier.isAbstract(mod)) {
//						isAbstract = true;
//					}
//
//					// is method declaration static?
//					if(Modifier.isStatic(mod)) {
//						isStatic = true;
//					}
//
//					IMethodBinding binding = node.resolveBinding();
//
//					// get type of each parameter
//					List<String> parameterTypes = new ArrayList<>();
//					for(Object obj : node.parameters()) {
//						ITypeBinding tb = ((SingleVariableDeclaration) obj).getType().resolveBinding();
//						String fqn;
//						try {
//							fqn = tb.getQualifiedName();
//						} catch (NullPointerException e) {
//							fqn = name.toString();
//						}
//						parameterTypes.add(fqn);
//					}
//
//					md.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
//					try {
//						md.setDeclaringClass(binding.getDeclaringClass().getQualifiedName());
//					} catch (NullPointerException e) {
//						md.setDeclaringClass(null);
//					}
//					md.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
//					md.setEndLine(cu.getLineNumber(node.getStartPosition() + node.getLength() - 1));
//					md.setFullyQualifiedName(fullyQualifiedName);
//
//					md.setIsAbstract(isAbstract);
//					md.setIsConstructor(node.isConstructor());
//
//					// to avoid API from setting constructor return type to void
//					if(node.isConstructor()) {
//						md.setReturnType(null);
//					}
//					else {
//						try {
//							md.setReturnType(binding.getReturnType().getQualifiedName());
//						} catch (NullPointerException e) {
//							md.setReturnType(null);
//						}
//					}
//
//					// get generic parameters
//					List<String> genericParametersList = new ArrayList<>();
//					try {
//						if(binding.isGenericMethod()) {
//							md.setIsGenericType(binding.isGenericMethod());
//							for(Object o : binding.getTypeParameters()) {
//								genericParametersList.add(o.toString());
//							}
//						}
//					} catch (NullPointerException e) {
//						md.setIsGenericType(false);
//					}
//
//					md.setGenericParametersList(genericParametersList);
//					md.setIsStatic(isStatic);
//					md.setIsVarargs(node.isVarargs());
//					md.setLineNumber(cu.getLineNumber(name.getStartPosition()));
//					md.setName(name.toString());
//					md.setNumberOfCharacters(node.getLength());
//					md.setParametersList(node.parameters());
//					md.setParameterTypesList(parameterTypes);
//					md.setStartCharacter(name.getStartPosition());
//
//					if(node.thrownExceptionTypes().size() > 0) {
//						for(Object o : node.thrownExceptionTypes()) {
//							md.addThrowsException(o.toString());
//						}
//					}
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

			// TODO
			// nothing changed
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

//					// get declaring class
//					IMethodBinding binding = node.resolveMethodBinding();
//					String declaringClass;
//					try {
//						declaringClass = binding.getDeclaringClass().getQualifiedName();
//					} catch (NullPointerException e) {
//						declaringClass = "";
//					}
//
//					// get calling class
//					String callingClass;
//					try {
//						callingClass = node.getExpression().resolveTypeBinding().getQualifiedName();
//					} catch (NullPointerException e) {
//						callingClass = "";
//					}
//
//					// get argument types
//					List<String> argumentTypes = new ArrayList<>();
//					for(Object t : node.arguments()) {
//						ITypeBinding tb = ((Expression)t).resolveTypeBinding();
//
//						try {
//							argumentTypes.add(tb.getQualifiedName());
//						} catch (NullPointerException e) {
//							argumentTypes.add("");
//						}
//					}
//
//					MethodInvocationObject mio = new MethodInvocationObject();
//					mio.setName(name.toString());
//					mio.setFullyQualifiedName(fullyQualifiedName);
//					mio.setDeclaringClass(declaringClass);
//					mio.setCallingClass(callingClass);
//					mio.setArguments(node.arguments());
//					mio.setArgumentTypes(argumentTypes);
//					mio.setLineNumber(cu.getLineNumber(name.getStartPosition()));
//					mio.setEndLine(cu.getLineNumber(node.getStartPosition() + node.getLength() - 1));
//					mio.setStartCharacter(name.getStartPosition());
//					mio.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
//					mio.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
//					entityStack.peek().addEntity(mio, Entity.EntityType.METHOD_INVOCATION);
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

//					packageObject = new SuperEntityClass(
//						node.getName().toString(),
//						fullyQualifiedName,
//						cu.getLineNumber(name.getStartPosition()),
//						cu.getColumnNumber(name.getStartPosition())
//					);
				}

				return true;
			}

			// TODO
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

//					SuperEntityClass svdEntity = new SuperEntityClass(
//									name.toString(),
//									fullyQualifiedName,
//									node.getType(),
//									cu.getLineNumber(name.getStartPosition()),
//									cu.getColumnNumber(name.getStartPosition())
//					);
//
//					if(node.getType().isArrayType()) {
//						entityStack.peek().addEntity(svdEntity, Entity.EntityType.ARRAY);
//					}
//					else if(node.getType().isParameterizedType()) {
//						entityStack.peek().addEntity(svdEntity, Entity.EntityType.GENERICS);
//					}
//					else if(node.getType().isPrimitiveType()) {
//						entityStack.peek().addEntity(svdEntity, Entity.EntityType.PRIMITIVE);
//					}
//					else if(node.getType().isSimpleType()) {
//						entityStack.peek().addEntity(svdEntity, Entity.EntityType.SIMPLE);
//					}
//					else if(node.getType().isUnionType()) {
//						entityStack.peek().addEntity(svdEntity, Entity.EntityType.UNION);
//					}
//					else {
//						System.out.println("Something is missing " + node.getType());
//					}
				}

				return true;
			}

			public boolean visit(SwitchStatement node) {
				if(inMethod && configProperties.get("SwitchStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

					SuperEntityClass sso = new SuperEntityClass(
						name,
						cu.getLineNumber(node.getStartPosition()),
						cu.getColumnNumber(node.getStartPosition())
					);

					List<SuperEntityClass> switchCaseList = new ArrayList<>();

					for(Object s : node.statements()) {
						if(s instanceof SwitchCase) {
							String expression;
							try {
								expression = ((SwitchCase) s).getExpression().toString();
							} catch (NullPointerException e) {
								expression = "Default";
							}

							switchCaseList.add(
								new SuperEntityClass(
									expression,
									cu.getLineNumber(((SwitchCase) s).getStartPosition()),
									cu.getColumnNumber(((SwitchCase)s).getStartPosition())
								)
							);
						}
					}

					sso.addEntities(switchCaseList, Entity.EntityType.SWITCH_CASE);
//					entityStack.peek().addEntity(sso, Entity.EntityType.SWITCH_STATEMENT);
				}

				return true;
			}

			public boolean visit(ThrowStatement node) {
				if(inMethod && configProperties.get("ThrowStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//						new SuperEntityClass(
//							name,
//							cu.getLineNumber(node.getStartPosition()),
//							cu.getColumnNumber(node.getStartPosition())
//						),
//						Entity.EntityType.THROW_STATEMENT
//					);
				}
				return true;
			}

			public boolean visit(TryStatement node) {
				if(inMethod && configProperties.get("TryStatement")) {
					// TODO
					// this seems like it does not even "try" to get the body of a try statement (hehe)

//					entityStack.peek().addEntity(
//						new SuperEntityClass(
//							"Try Statement",
//							cu.getLineNumber(node.getStartPosition()),
//							cu.getColumnNumber(node.getStartPosition())
//						),
//						Entity.EntityType.TRY_STATEMENT
//					);
				}

				return true;
			}

			// TODO
			// unchanged
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

						if(containingClass.isEmpty()) {
							containingClass = node.getName().toString();
						}

						solrDoc.addField("classNameScore", fullyQualifiedName);
						solrDoc.addField("isGenericScore", binding.isGenericType());
						solrDoc.addField("sizeScore", node.getLength());

						if(node.getSuperclassType() != null) {
							solrDoc.addField("extendsScore", node.getSuperclassType().toString());
						}

//						co.setIsAnonymous(binding.isAnonymous());
//						co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
//						co.setEndLine(endLine);
//						co.setLineNumber(startLine);
//						co.setName(node.getName().toString());
//						co.setNumberOfCharacters(node.getLength());
//						co.setFileName(fileLocation);
//						co.setFullyQualifiedName(fullyQualifiedName);
//
//						// get generic parameters
//						List<String> genericParametersList = new ArrayList<>();
//						try {
//							if(binding.isGenericType()) {
//								co.setIsGenericType(binding.isGenericType());
//								for(Object o : binding.getTypeParameters()) {
//									genericParametersList.add(o.toString());
//								}
//							}
//						} catch (NullPointerException e) {
//							co.setIsGenericType(false);
//						}
//						co.setGenericParametersList(genericParametersList);

						co.setImportList(importList);
						co.setPackage(packageObject);
						co.setSourceCode(getClassSourceCode(fileLocation, startLine, endLine));
						co.setStartCharacter(node.getStartPosition());
						co.setEndCharacter(node.getStartPosition() + node.getLength() - 1);

//						if(node.superInterfaceTypes().size() > 0) {
//							for(Object o : node.superInterfaceTypes()) {
//								co.addImplementsInterface(o.toString());
//							}
//						}

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

			// TODO
			// unchanged
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

			// TODO
			public boolean visit(VariableDeclarationStatement node) {
				if(configProperties.get("VariableDeclarationStatement")) {
//					Type nodeType = node.getType();

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

//						SuperEntityClass vdsEntity = new SuperEntityClass(
//							name.toString(),
//							fullyQualifiedName,
//							nodeType,
//							cu.getLineNumber(name.getStartPosition()),
//							cu.getColumnNumber(name.getStartPosition())
//						);
//
//						if(nodeType.isArrayType()) {
//							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.ARRAY);
//						}
//						else if(nodeType.isParameterizedType()) {
//							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.GENERICS);
//						}
//						else if(nodeType.isPrimitiveType()) {
//							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.PRIMITIVE);
//						}
//						else if(nodeType.isSimpleType()) {
//							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.SIMPLE);
//						}
//						else {
//							System.out.println("Something is missing " + nodeType);
//						}
					}
				}

				return true;
			}

			// TODO
			public boolean visit(VariableDeclarationExpression node) {
				if(configProperties.get("VariableDeclarationExpression")) {
//					Type nodeType = node.getType();

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

//						SuperEntityClass vdeEntity = new SuperEntityClass(
//										name.toString(),
//										fullyQualifiedName,
//										nodeType,
//										cu.getLineNumber(name.getStartPosition()),
//										cu.getColumnNumber(name.getStartPosition())
//						);

//						if(nodeType.isArrayType()) {
//							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.ARRAY);
//						}
//						else if(nodeType.isParameterizedType()) {
//							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.GENERICS);
//						}
//						else if(nodeType.isPrimitiveType()) {
//							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.PRIMITIVE);
//						}
//						else if(nodeType.isSimpleType()) {
//							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.SIMPLE);
//						}
//						else {
//							System.out.println("Something is missing " + nodeType);
//						}
					}
				}

				return true;
			}

			public boolean visit(WhileStatement node){
				if(inMethod && configProperties.get("WhileStatement")) {
					String name;
					try {
						name = node.getExpression().toString();
					} catch (NullPointerException e) {
						name = "";
					}

//					entityStack.peek().addEntity(
//						new SuperEntityClass(
//							name,
//							cu.getLineNumber(node.getStartPosition()),
//							cu.getColumnNumber(node.getStartPosition())
//						),
//						Entity.EntityType.WHILE_STATEMENT
//					);
				}

				return true;
			}

			// TODO
			// unchanged
//			public boolean visit(WildcardType node) {
//				if(inMethod && configProperties.get("WildcardType")) {
//					SuperEntityClass wo = new SuperEntityClass();
//					wo.setName("Wildcard");
//
//					String bound;
//					try {
//						bound = node.getBound().toString();
//					} catch (NullPointerException e) {
//						bound = "none";
//					}
//
//					wo.setBound(bound);
//					wo.setType(((ParameterizedType) node.getParent()).getType());
//					wo.setLineNumber(cu.getLineNumber(node.getStartPosition()));
//					wo.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
//					entityStack.peek().addEntity(wo, Entity.EntityType.WILDCARD);
//				}
//
//				return false;
//			}

		});

		// we should be adding names at the end because we want to keep a SET
		// todo: need if statements everywhere, or is iterating over an empty set okay?
		// todo: wildcardScore, complexityScore
		solrDoc.addField("importNumScore", importNames.size());
		solrDoc.addField("fieldsScore", fieldNames.size());

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
