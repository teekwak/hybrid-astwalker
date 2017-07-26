package standalone;

import standalone.entities.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;
import standalone.entities.FileModel;

import java.io.*;
import java.util.*;

/**
 * @author Thomas Kwak
 */
public class ASTStandalone {
	private Map<String, Boolean> configProperties;
	private String containingClass;
	private Stack<Entity> entityStack;
	private FileModel fileModel;
	private boolean hasComments;
	private List<SuperEntityClass> importList;
	private boolean inMethod;
	private SuperEntityClass packageObject;


	// find comments
//	class CommentVisitor extends ASTVisitor {
//		CompilationUnit cu;
//		String source;
//
//		CommentVisitor(CompilationUnit cu, String source) {
//			super();
//			this.cu = cu;
//			this.source = source;
//		}
//
//		public boolean visit(LineComment node) {
//			hasComments = true;
//			return true;
//		}
//
//		public boolean visit(BlockComment node) {
//			hasComments = true;
//			return true;
//		}
//	}

	public ASTStandalone() {
		containingClass = "";
		entityStack = new Stack<>();
		fileModel = new FileModel();
		hasComments = false; // TODO
		importList = new ArrayList<>();
		inMethod = false;
		packageObject = new SuperEntityClass();

		// convert to hash map so that boolean parsing happens only once
		configProperties = new HashMap<>();
		configProperties.put("AnonymousClassDeclaration", true);
		configProperties.put("CatchClause", true);
		configProperties.put("ConditionalExpression", true);
		configProperties.put("DoStatement", true);
		configProperties.put("EnhancedForStatement", true);
		configProperties.put("FieldDeclaration", true);
		configProperties.put("ForStatement", true);
		configProperties.put("IfStatement", true);
		configProperties.put("ImportDeclaration", true);
		configProperties.put("InfixExpression", true);
		configProperties.put("MethodDeclaration", true);
		configProperties.put("MethodInvocation", true);
		configProperties.put("PackageDeclaration", true);
		configProperties.put("SingleVariableDeclaration", true);
		configProperties.put("SwitchStatement", true);
		configProperties.put("ThrowStatement", true);
		configProperties.put("TryStatement", true);
		configProperties.put("TypeDeclaration", true);
		configProperties.put("VariableDeclarationStatement", true);
		configProperties.put("VariableDeclarationExpression", true);
		configProperties.put("WhileStatement", true);
		configProperties.put("WildcardType", true);
	}

	public FileModel getFileModel() {
		return this.fileModel;
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

	public void parseFile(String fileLocation) {
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

//		for (Comment comment : (List<Comment>) cu.getCommentList()) {
//			comment.accept(new ASTRefactor.CommentVisitor(cu, sourceCode));
//		}

		cu.accept(new ASTVisitor() {
			public boolean visit(AnonymousClassDeclaration node) {
				JavaClass co = new JavaClass();

				if(configProperties.get("AnonymousClassDeclaration")) {
					ITypeBinding binding = node.resolveBinding();

					int startLine = cu.getLineNumber(node.getStartPosition());
					int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);

					co.setIsAnonymous(binding.isAnonymous());
					co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					co.setEndLine(endLine);
					co.setLineNumber(startLine);

					System.out.println(startLine);

					co.setNumberOfCharacters(node.getLength());
					co.setFileName(fileLocation);

					List<String> genericParametersList = new ArrayList<>();
					try {
						if(binding.isGenericType()) {
							co.setIsGenericType(binding.isGenericType());
							for(Object o : binding.getTypeParameters()) {
								genericParametersList.add(o.toString());
							}
						}
					} catch (NullPointerException e) {
						co.setIsGenericType(false);
					}
					co.setGenericParametersList(genericParametersList);

					co.setHasComments(hasComments);
					co.setSourceCode(getClassSourceCode(fileLocation, startLine, endLine));
					co.setStartCharacter(node.getStartPosition());
					co.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
					co.setImportList(importList);
					co.setPackage(packageObject);
					co.setIsAnonymous(true);
				}

				entityStack.push(co);

				return true;
			}

			public void endVisit(AnonymousClassDeclaration node) {
				if(configProperties.get("AnonymousClassDeclaration")) {
					JavaClass temp = (JavaClass) entityStack.pop();

					temp.setIsInnerClass(true);

					temp.setComplexities();
					temp.setMethodDeclarationNames();
					temp.setMethodInvocationNames();

					if(!containingClass.isEmpty()) {
						temp.setContainingClass(containingClass);
					}

					try {
						entityStack.peek().addEntity(temp, Entity.EntityType.CLASS);
					} catch (EmptyStackException e) {
						// should not be possible
					}

					fileModel.addJavaClass(temp);

					hasComments = false;
				}
			}

			public boolean visit(CatchClause node) {
				if(inMethod && configProperties.get("CatchClause")) {
					SimpleName name = node.getException().getName();

					SuperEntityClass cco =  new SuperEntityClass(
									name.toString(),
									cu.getLineNumber(name.getStartPosition()),
									cu.getColumnNumber(name.getStartPosition())
					);
					cco.setType(node.getException().getType());
					entityStack.peek().addEntity(cco, Entity.EntityType.CATCH_CLAUSE);
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									),
									Entity.EntityType.CONDITIONAL_EXPRESSION
					);
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									),
									Entity.EntityType.DO_STATEMENT
					);
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									),
									Entity.EntityType.FOR_STATEMENT
					);
				}

				return true;
			}

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

						SuperEntityClass fdEntity = new SuperEntityClass(
										name.toString(),
										fullyQualifiedName,
										nodeType,
										cu.getLineNumber(name.getStartPosition()),
										cu.getColumnNumber(name.getStartPosition())
						);

						if(nodeType.isArrayType()) {
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.ARRAY);
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
						}
						else if(nodeType.isParameterizedType()) {
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GENERICS);
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
						}
						else if(nodeType.isPrimitiveType()) {
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.PRIMITIVE);
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
						}
						else if(nodeType.isSimpleType()) {
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.SIMPLE);
							entityStack.peek().addEntity(fdEntity, Entity.EntityType.GLOBAL);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									), Entity.EntityType.FOR_STATEMENT
					);
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									), Entity.EntityType.IF_STATEMENT
					);
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

					importList.add(
									new SuperEntityClass(
													name.toString(),
													fullyQualifiedName,
													cu.getLineNumber(name.getStartPosition()),
													cu.getColumnNumber(name.getStartPosition())
									)
					);
				}

				return true;
			}

			public boolean visit(InfixExpression node){
				if(inMethod && configProperties.get("InfixExpression")) {
					entityStack.peek().addEntity(
									new SuperEntityClass(
													node.getOperator().toString(),
													cu.getLineNumber(node.getLeftOperand().getStartPosition()),
													cu.getColumnNumber(node.getLeftOperand().getStartPosition())
									),
									Entity.EntityType.INFIX_EXPRESSION
					);
				}

				return true;
			}

			public boolean visit(MethodDeclaration node) {
				inMethod = true;
				MethodDeclarationObject md = new MethodDeclarationObject();

				if(configProperties.get("MethodDeclaration")) {
					SimpleName name = node.getName();
					boolean isStatic = false;
					boolean isAbstract = false;

					// get fully qualified name
					String fullyQualifiedName;
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = name.toString();
					}

					// is method declaration abstract?
					int mod = node.getModifiers();
					if(Modifier.isAbstract(mod)) {
						isAbstract = true;
					}

					// is method declaration static?
					if(Modifier.isStatic(mod)) {
						isStatic = true;
					}

					IMethodBinding binding = node.resolveBinding();

					// get type of each parameter
					List<String> parameterTypes = new ArrayList<>();
					for(Object obj : node.parameters()) {
						ITypeBinding tb = ((SingleVariableDeclaration) obj).getType().resolveBinding();
						String fqn;
						try {
							fqn = tb.getQualifiedName();
						} catch (NullPointerException e) {
							fqn = name.toString();
						}
						parameterTypes.add(fqn);
					}

					md.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					try {
						md.setDeclaringClass(binding.getDeclaringClass().getQualifiedName());
					} catch (NullPointerException e) {
						md.setDeclaringClass(null);
					}
					md.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
					md.setEndLine(cu.getLineNumber(node.getStartPosition() + node.getLength() - 1));
					md.setFullyQualifiedName(fullyQualifiedName);

					md.setIsAbstract(isAbstract);
					md.setIsConstructor(node.isConstructor());

					// to avoid API from setting constructor return type to void
					if(node.isConstructor()) {
						md.setReturnType(null);
					}
					else {
						try {
							md.setReturnType(binding.getReturnType().getQualifiedName());
						} catch (NullPointerException e) {
							md.setReturnType(null);
						}
					}

					// get generic parameters
					List<String> genericParametersList = new ArrayList<>();
					try {
						if(binding.isGenericMethod()) {
							md.setIsGenericType(binding.isGenericMethod());
							for(Object o : binding.getTypeParameters()) {
								genericParametersList.add(o.toString());
							}
						}
					} catch (NullPointerException e) {
						md.setIsGenericType(false);
					}

					md.setGenericParametersList(genericParametersList);
					md.setIsStatic(isStatic);
					md.setIsVarargs(node.isVarargs());
					md.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					md.setName(name.toString());
					md.setNumberOfCharacters(node.getLength());
					md.setParametersList(node.parameters());
					md.setParameterTypesList(parameterTypes);
					md.setStartCharacter(name.getStartPosition());

					if(node.thrownExceptionTypes().size() > 0) {
						for(Object o : node.thrownExceptionTypes()) {
							md.addThrowsException(o.toString());
						}
					}
				}

				entityStack.push(md);

				return true;
			}

			public void endVisit(MethodDeclaration node) {
				MethodDeclarationObject temp = (MethodDeclarationObject) entityStack.pop();

				if(configProperties.get("MethodDeclaration")) {
					temp.setComplexities();
					temp.setMethodDeclarationNames();
					temp.setMethodInvocationNames();
					entityStack.peek().addEntity(temp, Entity.EntityType.METHOD_DECLARATION);
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

					// get declaring class
					IMethodBinding binding = node.resolveMethodBinding();
					String declaringClass;
					try {
						declaringClass = binding.getDeclaringClass().getQualifiedName();
					} catch (NullPointerException e) {
						declaringClass = "";
					}

					// get calling class
					String callingClass;
					try {
						callingClass = node.getExpression().resolveTypeBinding().getQualifiedName();
					} catch (NullPointerException e) {
						callingClass = "";
					}

					// get argument types
					List<String> argumentTypes = new ArrayList<>();
					for(Object t : node.arguments()) {
						ITypeBinding tb = ((Expression)t).resolveTypeBinding();

						try {
							argumentTypes.add(tb.getQualifiedName());
						} catch (NullPointerException e) {
							argumentTypes.add("");
						}
					}

					MethodInvocationObject mio = new MethodInvocationObject();
					mio.setName(name.toString());
					mio.setFullyQualifiedName(fullyQualifiedName);
					mio.setDeclaringClass(declaringClass);
					mio.setCallingClass(callingClass);
					mio.setArguments(node.arguments());
					mio.setArgumentTypes(argumentTypes);
					mio.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					mio.setEndLine(cu.getLineNumber(node.getStartPosition() + node.getLength() - 1));
					mio.setStartCharacter(name.getStartPosition());
					mio.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
					mio.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addEntity(mio, Entity.EntityType.METHOD_INVOCATION);
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

					packageObject = new SuperEntityClass(
									node.getName().toString(),
									fullyQualifiedName,
									cu.getLineNumber(name.getStartPosition()),
									cu.getColumnNumber(name.getStartPosition())
					);
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

					SuperEntityClass svdEntity = new SuperEntityClass(
									name.toString(),
									fullyQualifiedName,
									node.getType(),
									cu.getLineNumber(name.getStartPosition()),
									cu.getColumnNumber(name.getStartPosition())
					);

					if(node.getType().isArrayType()) {
						entityStack.peek().addEntity(svdEntity, Entity.EntityType.ARRAY);
					}
					else if(node.getType().isParameterizedType()) {
						entityStack.peek().addEntity(svdEntity, Entity.EntityType.GENERICS);
					}
					else if(node.getType().isPrimitiveType()) {
						entityStack.peek().addEntity(svdEntity, Entity.EntityType.PRIMITIVE);
					}
					else if(node.getType().isSimpleType()) {
						entityStack.peek().addEntity(svdEntity, Entity.EntityType.SIMPLE);
					}
					else if(node.getType().isUnionType()) {
						entityStack.peek().addEntity(svdEntity, Entity.EntityType.UNION);
					}
					else {
						System.out.println("Something is missing " + node.getType());
					}
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
					entityStack.peek().addEntity(sso, Entity.EntityType.SWITCH_STATEMENT);
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									),
									Entity.EntityType.THROW_STATEMENT
					);
				}
				return true;
			}

			public boolean visit(TryStatement node) {
				if(inMethod && configProperties.get("TryStatement")) {
					entityStack.peek().addEntity(
									new SuperEntityClass(
													"Try Statement",
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									),
									Entity.EntityType.TRY_STATEMENT
					);
				}

				return true;
			}

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

						co.setIsAnonymous(binding.isAnonymous());
						co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
						co.setEndLine(endLine);
						co.setLineNumber(startLine);
						co.setName(node.getName().toString());
						co.setNumberOfCharacters(node.getLength());
						co.setFileName(fileLocation);
						co.setFullyQualifiedName(fullyQualifiedName);

						// get generic parameters
						List<String> genericParametersList = new ArrayList<>();
						try {
							if(binding.isGenericType()) {
								co.setIsGenericType(binding.isGenericType());
								for(Object o : binding.getTypeParameters()) {
									genericParametersList.add(o.toString());
								}
							}
						} catch (NullPointerException e) {
							co.setIsGenericType(false);
						}
						co.setGenericParametersList(genericParametersList);

						co.setHasComments(hasComments);
						co.setImportList(importList);
						co.setPackage(packageObject);
						co.setSourceCode(getClassSourceCode(fileLocation, startLine, endLine));
						co.setStartCharacter(node.getStartPosition());
						co.setEndCharacter(node.getStartPosition() + node.getLength() - 1);

						if(node.getSuperclassType() != null) {
							co.setSuperClass(node.getSuperclassType().toString());
						}

						if(node.superInterfaceTypes().size() > 0) {
							for(Object o : node.superInterfaceTypes()) {
								co.addImplementsInterface(o.toString());
							}
						}

						int mod = node.getModifiers();
						if(Modifier.isAbstract(mod)) {
							co.setIsAbstract(true);
						}
						else {
							co.setIsAbstract(false);
						}

					}
				}

				entityStack.push(co);

				return true;
			}

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
						entityStack.peek().addEntity(temp, Entity.EntityType.CLASS);

					} catch (EmptyStackException e) {
						containingClass = "";
					}

					fileModel.addJavaClass(temp);
				}

				hasComments = false;
			}

			public boolean visit(VariableDeclarationStatement node) {
				if(configProperties.get("VariableDeclarationStatement")) {
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

						SuperEntityClass vdsEntity = new SuperEntityClass(
										name.toString(),
										fullyQualifiedName,
										nodeType,
										cu.getLineNumber(name.getStartPosition()),
										cu.getColumnNumber(name.getStartPosition())
						);

						if(nodeType.isArrayType()) {
							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.ARRAY);
						}
						else if(nodeType.isParameterizedType()) {
							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.GENERICS);
						}
						else if(nodeType.isPrimitiveType()) {
							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.PRIMITIVE);
						}
						else if(nodeType.isSimpleType()) {
							entityStack.peek().addEntity(vdsEntity, Entity.EntityType.SIMPLE);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				}

				return true;
			}

			public boolean visit(VariableDeclarationExpression node) {
				if(configProperties.get("VariableDeclarationExpression")) {
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

						SuperEntityClass vdeEntity = new SuperEntityClass(
										name.toString(),
										fullyQualifiedName,
										nodeType,
										cu.getLineNumber(name.getStartPosition()),
										cu.getColumnNumber(name.getStartPosition())
						);

						if(nodeType.isArrayType()) {
							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.ARRAY);
						}
						else if(nodeType.isParameterizedType()) {
							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.GENERICS);
						}
						else if(nodeType.isPrimitiveType()) {
							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.PRIMITIVE);
						}
						else if(nodeType.isSimpleType()) {
							entityStack.peek().addEntity(vdeEntity, Entity.EntityType.SIMPLE);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
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

					entityStack.peek().addEntity(
									new SuperEntityClass(
													name,
													cu.getLineNumber(node.getStartPosition()),
													cu.getColumnNumber(node.getStartPosition())
									),
									Entity.EntityType.WHILE_STATEMENT
					);
				}

				return true;
			}

			public boolean visit(WildcardType node) {
				if(inMethod && configProperties.get("WildcardType")) {
					SuperEntityClass wo = new SuperEntityClass();
					wo.setName("Wildcard");

					String bound;
					try {
						bound = node.getBound().toString();
					} catch (NullPointerException e) {
						bound = "none";
					}

					wo.setBound(bound);
					wo.setType(((ParameterizedType) node.getParent()).getType());
					wo.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					wo.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addEntity(wo, Entity.EntityType.WILDCARD);
				}

				return false;
			}

		});
	}

	public static void main(String[] args) {
		ASTStandalone a = new ASTStandalone("resources/astconfig.properties");
		a.parseFile("resources/Test.java");

		for(SuperEntityClass obj : a.importList) {
			System.out.println(obj.getName());
		}
	}
}
