package tools;

import entities.Entity;
import entities.JavaClass;
import entities.SuperEntityClass;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.*;

/**
 * @author Thomas Kwak
 *
 * Table of Contents (out of date)
 *
 * 1. Class variables...........31 x
 * 2. Constructor...............39 x
 *
 * 3. validateProperties()......45 x
 * 4. parseFile()...............63 x
 * 5. test1()...................96 x
 * 6. main()....................121 x
 *
 */
public class ASTRefactor {
	private Properties configProperties;
	private String containingClass;
	private Stack<Entity> entityStack;
	private FileModel fileModel;
	private boolean hasComments;
	private List<SuperEntityClass> importList;
	private boolean inMethod;
	private SuperEntityClass packageObject;


	/**
	 * Constructor for class. Creates a FileModel object and takes in a config file and adds into a Properties object
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

		configProperties = new Properties();
		try(InputStream input = new FileInputStream(configFilePath)) {
			configProperties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Validates the properties from the config file
	 */
	private void validateProperties() {
		StringBuilder errors = new StringBuilder();

		for(Map.Entry entry : configProperties.entrySet()) {
			String entryValue = entry.getValue().toString();

			if(entryValue == null || entryValue.isEmpty()) {
				errors.append("\n");
				errors.append(entry.getKey());
				errors.append(" is null!");
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
//			AnonymousClassDeclaration
//			CatchClause
//			ConditionalExpression
//			DoStatement
//			EnhancedForStatement
//			FieldDeclaration
//			ForStatement
//			IfStatement
//			ImportDeclaration x
//			InfixExpression x
//			MethodDeclaration
//			MethodInvocation
//			PackageDeclaration
//			SingleVariableDeclaration
//			SwitchStatement
//			ThrowStatement
//			TryStatement
//			TypeDeclaration
//			VariableDeclarationStatement
//			VariableDeclarationExpression
//			WhileStatement
//			WildcardType




			public boolean visit(ImportDeclaration node) {
				if(Boolean.parseBoolean(configProperties.getProperty("ImportDeclaration"))) {
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
				if(inMethod) {
					SuperEntityClass ieo = new SuperEntityClass();
					ieo.setName(node.getOperator().toString());
					ieo.setLineNumber(cu.getLineNumber(node.getLeftOperand().getStartPosition()));
					ieo.setColumnNumber(cu.getColumnNumber(node.getLeftOperand().getStartPosition()));
					entityStack.peek().addEntity(ieo, Entity.EntityType.INFIX_EXPRESSION);
				}

				return true;
			}

			public boolean visit(TypeDeclaration node) {

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

					JavaClass co = new JavaClass();

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

					entityStack.push(co);
				}

				return true;
			}

			public void endVisit(TypeDeclaration node) {
				if(!node.isInterface()) {
					JavaClass temp = (JavaClass) entityStack.pop();

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
		a.validateProperties();
		a.parseFile("resources/test.java");

		for(SuperEntityClass obj : a.importList) {
			System.out.println(obj.getName());
		}

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
		test1();
	}
}
