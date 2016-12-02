package tools;

import entities.*;
import org.apache.commons.io.FileUtils;
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
public class ASTRefactor {
	private Map<String, Boolean> configProperties;
	private String containingClass;
	private Stack<Entity> entityStack;
	private FileModel fileModel;
	private boolean hasComments;
	private List<SuperEntityClass> importList;
	private boolean inMethod;
	private SuperEntityClass packageObject;


	/**
	 * Constructor for class. Creates a FileModel object and takes in a config file and adds into a HashMap
	 * Properties class is used for easily reading a file and making sure the user uses a .properties file
	 *
	 * @param configFilePath path to config file
	 */
	private ASTRefactor(String configFilePath) {
		containingClass = "";
		entityStack = new Stack<>();
		fileModel = new FileModel();
		hasComments = false; // TODO
		importList = new ArrayList<>();
		inMethod = false;
		packageObject = new SuperEntityClass();

		if(!configFilePath.endsWith(".properties")) {
			throw new IllegalArgumentException("[ERROR]: config file has the wrong file type!");
		}

		Properties properties = new Properties();
		try(InputStream input = new FileInputStream(configFilePath)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}

		validateProperties(properties);

		// convert to hash map so that boolean parsing happens only once
		configProperties = new HashMap<>();
		for(Map.Entry<Object, Object> entry : properties.entrySet()) {
			configProperties.put(entry.getKey().toString(), Boolean.parseBoolean(entry.getValue().toString()));
		}
	}


	/**
	 * Validates the properties from the config file
	 */
	private void validateProperties(Properties properties) {
		StringBuilder errors = new StringBuilder();

		for(Map.Entry entry : properties.entrySet()) {
			if(entry.getValue() == null
				|| entry.getValue().toString().isEmpty()
				|| (!entry.getValue().toString().toLowerCase().equals("true") && !entry.getValue().toString().toLowerCase().equals("false"))
			) {
				errors.append("\n");
				errors.append(entry.getKey());
				errors.append(" is not properly defined!");
			}
		}

		if(errors.length() != 0) {
			throw new IllegalArgumentException(errors.toString());
		}
	}


	/**
	 *
	 */
	private void parseFile(String fileLocation) {
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

		// some comment functionality here? i left it out on purpose

		// continue code
		// when we are writing visit and exit functions, make sure to use the properties!
		// we want to visit the node (because there will be children inside!) so always return true
		// however, we do not want to record any information
		// but make sure we do not skip over children!

		cu.accept(new ASTVisitor() {

			// TODO
			// nothing changed
			public boolean visit(AnonymousClassDeclaration node) {
				JavaClass co = new JavaClass();

				if(configProperties.get("AnonymousClassDeclaration")) {
					ITypeBinding binding = node.resolveBinding();

					int startLine = cu.getLineNumber(node.getStartPosition());
					int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);
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

					co.setIsAnonymous(binding.isAnonymous());
					co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					co.setEndLine(endLine);
					co.setLineNumber(startLine);
					co.setNumberOfCharacters(node.getLength());
					co.setFileName(fileLocation);
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
					co.setSourceCode(classSourceCode.toString());
					co.setStartCharacter(node.getStartPosition());
					co.setEndCharacter(node.getStartPosition() + node.getLength() - 1);
					co.setImportList(importList);
					co.setPackage(packageObject);
					co.setIsAnonymous(true);
				}

				entityStack.push(co);

				return true;
			}

			// TODO
			// nothing changed
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

						if(nodeType.isArrayType()) {
							SuperEntityClass ao = new SuperEntityClass();
							ao.setName(name.toString());
							ao.setFullyQualifiedName(fullyQualifiedName);
							ao.setType(nodeType);
							ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(ao, Entity.EntityType.ARRAY);
							entityStack.peek().addEntity(ao, Entity.EntityType.GLOBAL);
						}
						else if(nodeType.isParameterizedType()) {
							SuperEntityClass go = new SuperEntityClass();
							go.setName(name.toString());
							go.setFullyQualifiedName(fullyQualifiedName);
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(go, Entity.EntityType.GENERICS);
							entityStack.peek().addEntity(go, Entity.EntityType.GLOBAL);
						}
						else if(nodeType.isPrimitiveType()) {
							SuperEntityClass po = new SuperEntityClass();
							po.setName(name.toString());
							po.setFullyQualifiedName(fullyQualifiedName);
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(po, Entity.EntityType.PRIMITIVE);
							entityStack.peek().addEntity(po, Entity.EntityType.GLOBAL);
						}
						else if(nodeType.isSimpleType()) {
							SuperEntityClass so = new SuperEntityClass();
							so.setName(name.toString());
							so.setFullyQualifiedName(fullyQualifiedName);
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(so, Entity.EntityType.SIMPLE);
							entityStack.peek().addEntity(so, Entity.EntityType.GLOBAL);
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

			// TODO
			// nothing has changed
			// actually, should we only be checking the parse boolean when we SAVE the method dec?
			// this is because we always have to visit the method, but we dont necessarily have to save it
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
					entityStack.peek().addEntity(temp, Entity.EntityType.METHOD_DECLARATION);
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

					if(node.getType().isArrayType()) {
						SuperEntityClass ao = new SuperEntityClass();
						ao.setName(name.toString());
						ao.setFullyQualifiedName(fullyQualifiedName);
						ao.setType(node.getType());
						ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addEntity(ao, Entity.EntityType.ARRAY);
					}

					else if(node.getType().isParameterizedType()) {
						SuperEntityClass go = new SuperEntityClass();
						go.setName(name.toString());
						go.setFullyQualifiedName(fullyQualifiedName);
						go.setType(node.getType());
						go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addEntity(go, Entity.EntityType.GENERICS);
					}
					else if(node.getType().isPrimitiveType()) {
						SuperEntityClass po = new SuperEntityClass();
						po.setName(name.toString());
						po.setFullyQualifiedName(fullyQualifiedName);
						po.setType(node.getType());
						po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addEntity(po, Entity.EntityType.PRIMITIVE);
					}
					else if(node.getType().isSimpleType()) {
						SuperEntityClass so = new SuperEntityClass();
						so.setName(name.toString());
						so.setFullyQualifiedName(fullyQualifiedName);
						so.setType(node.getType());
						so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addEntity(so, Entity.EntityType.SIMPLE);
					}
					else if(node.getType().isUnionType()) {
						SuperEntityClass uo = new SuperEntityClass();
						uo.setName(name.toString());
						uo.setFullyQualifiedName(fullyQualifiedName);
						uo.setType(node.getType());
						uo.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						uo.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addEntity(uo, Entity.EntityType.UNION);
					}
					else {
						System.out.println("Something is missing " + node.getType());
					}
				}

				return true;
			}

			// TODO
			public boolean visit(SwitchStatement node) {
				if(inMethod && configProperties.get("SwitchStatement")) {
					SuperEntityClass sso = new SuperEntityClass();
					try {
						sso.setName(node.getExpression().toString());
					} catch (NullPointerException e) {
						sso.setName("");
					}
					sso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					sso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));

					List<SuperEntityClass> switchCaseList = new ArrayList<>();

					for(Object s : node.statements()) {
						if(s instanceof SwitchCase) {
							SuperEntityClass switchCase = new SuperEntityClass();

							String expression;
							try {
								expression = ((SwitchCase) s).getExpression().toString();
							} catch (NullPointerException e) {
								expression = "Default";
							}

							switchCase.setName(expression);
							switchCase.setLineNumber(cu.getLineNumber(((SwitchCase) s).getStartPosition()));
							switchCase.setColumnNumber(cu.getColumnNumber(((SwitchCase)s).getStartPosition()));
							switchCaseList.add(switchCase);
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
					// TODO
					// this seems like it does not even "try" to get the body of a try statement (hehe)

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
						co.setSourceCode(classSourceCode.toString());
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
						entityStack.peek().addEntity(temp, Entity.EntityType.CLASS);

					} catch (EmptyStackException e) {
						containingClass = "";
					}

					fileModel.addJavaClass(temp);
				}

				hasComments = false;
			}

			// TODO
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

						if(nodeType.isArrayType()) {
							SuperEntityClass ao = new SuperEntityClass();
							ao.setName(name.toString());
							ao.setFullyQualifiedName(fullyQualifiedName);
							ao.setType(nodeType);
							ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(ao, Entity.EntityType.ARRAY);
						}
						else if(nodeType.isParameterizedType()) {
							SuperEntityClass go = new SuperEntityClass();
							go.setName(name.toString());
							go.setFullyQualifiedName(fullyQualifiedName);
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(go, Entity.EntityType.GENERICS);
						}
						else if(nodeType.isPrimitiveType()) {
							SuperEntityClass po = new SuperEntityClass();
							po.setName(name.toString());
							po.setFullyQualifiedName(fullyQualifiedName);
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(po, Entity.EntityType.PRIMITIVE);
						}
						else if(nodeType.isSimpleType()) {
							SuperEntityClass so = new SuperEntityClass();
							so.setName(name.toString());
							so.setFullyQualifiedName(fullyQualifiedName);
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(so, Entity.EntityType.SIMPLE);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				}

				return true;
			}

			// TODO
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

						if(nodeType.isArrayType()) {
							SuperEntityClass ao = new SuperEntityClass();
							ao.setName(name.toString());
							ao.setFullyQualifiedName(fullyQualifiedName);
							ao.setType(nodeType);
							ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(ao, Entity.EntityType.ARRAY);
						}
						else if(nodeType.isParameterizedType()) {
							SuperEntityClass go = new SuperEntityClass();
							go.setName(name.toString());
							go.setFullyQualifiedName(fullyQualifiedName);
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(go, Entity.EntityType.GENERICS);
						}
						else if(nodeType.isPrimitiveType()) {
							SuperEntityClass po = new SuperEntityClass();
							po.setName(name.toString());
							po.setFullyQualifiedName(fullyQualifiedName);
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(po, Entity.EntityType.PRIMITIVE);
						}
						else if(nodeType.isSimpleType()) {
							SuperEntityClass so = new SuperEntityClass();
							so.setName(name.toString());
							so.setFullyQualifiedName(fullyQualifiedName);
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(so, Entity.EntityType.SIMPLE);
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

			// TODO
			// unchanged
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


	/**
	 * Test that times how long it takes and how much memory this class uses
	 */
	private static void test1() {
		Runtime runtime = Runtime.getRuntime();
		long start = System.currentTimeMillis();

		// ******************************************

		ASTRefactor a = new ASTRefactor("resources/astconfig.properties");
		a.parseFile("resources/test.java");

//		for(SuperEntityClass obj : a.importList) {
//			System.out.println(obj.getName());
//		}

		// print results?

		// ******************************************

		long end = System.currentTimeMillis();

		int mb = 1024 * 1024;
		System.out.println("----------------------------------");
		System.out.println("*** System statistic estimates ***");
		System.out.println("\tRuntime: \t\t" + (end - start) + " ms");
		System.out.println("\tTotal Memory: \t" + runtime.totalMemory() / mb  + " MB"); // available memory
		System.out.println("\tFree Memory: \t" + runtime.freeMemory() / mb + " MB"); // free memory
		System.out.println("\tUsed Memory: \t" + (runtime.totalMemory() - runtime.freeMemory()) / mb + " MB"); // used memory
		System.out.println("----------------------------------");
	}


	/**
	 * Clearly a main function that does very little
	 */
	public static void main(String[] args) {
//		// time to set up visualvm
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		for(int i = 0; i < 100; i++) {
//			try {
//				Thread.sleep(100);
//				test1();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

		test1();
	}
}
