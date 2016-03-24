package tools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

import entities.JavaClass;
import entities.MethodDeclarationObject;
import entities.MethodInvocationObject;
import entities.SuperEntityClass;
import entities.Entity;
import entities.Entity.EntityType;

/**
 * Walks Java source code and parses constructs
 *
 * @author Thomas Kwak
 */
public class ASTWalker {

	public FileModel fileModel;
	public Stack<Entity> entityStack = new Stack<>();
	public SuperEntityClass packageObject = new SuperEntityClass();
	public List<SuperEntityClass> importList = new ArrayList<>();
	public boolean inMethod = false;
	public boolean inInterface = false; // ignoring interfaces
	public boolean hasComments = false;

	// find comments
	class CommentVisitor extends ASTVisitor {
		CompilationUnit cu;
		String source;
	 
		public CommentVisitor(CompilationUnit cu, String source) {
			super();
			this.cu = cu;
			this.source = source;
		}
	 
		public boolean visit(LineComment node) {
			hasComments = true;
			return true;
		}
	 
		public boolean visit(BlockComment node) {
			hasComments = true;
			return true;
		}
	}
	
	/**
	 * Reads code file
	 *
	 * @param filePath = absolute path to file
	 * @return char[] of file
	 * @throws IOException
	 */
	public static String readFileToCharArray(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	/**
	 * Actually extracts constructs from code
	 * 
	 * @param fileLocation = absolute path to file
	 * @return FileModel object populated with constructs
	 * @throws IOException, CoreException
	 */
	@SuppressWarnings("unchecked")
	public FileModel parseFile(String fileLocation) throws IOException, CoreException {
		this.fileModel = new FileModel();

		String sourceCode = readFileToCharArray(fileLocation);
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		parser.setUnitName(fileLocation);
		parser.setEnvironment(null, null, null, true);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		for (Comment comment : (List<Comment>) cu.getCommentList()) {
		    comment.accept(new CommentVisitor(cu, sourceCode));
		}
		
		// alphabetical order
		cu.accept(new ASTVisitor() {

			public boolean visit(CatchClause node) {
				if(inMethod) {
					SimpleName name = node.getException().getName();
					
					SuperEntityClass cco = new SuperEntityClass();
					cco.setName(name.toString());
					cco.setType(node.getException().getType());
					cco.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					cco.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addEntity(cco, EntityType.CATCH_CLAUSE);
				}

				return true;
			}
		
			public boolean visit(ConditionalExpression node){
				if(inMethod) {
					SuperEntityClass ceo = new SuperEntityClass();
					ceo.setName(node.getExpression().toString());
					ceo.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					ceo.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addEntity(ceo, EntityType.CONDITIONAL_EXPRESSION);
				}

				return true;
			}

			public boolean visit(DoStatement node) {
				if(inMethod) {					
					SuperEntityClass dso = new SuperEntityClass();
					dso.setName(node.getExpression().toString());
					dso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					dso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));		
					entityStack.peek().addEntity(dso, EntityType.DO_STATEMENT);
				}
				return true;
			}

			public boolean visit(EnhancedForStatement node) {
				if(inMethod) {
					SuperEntityClass fso = new SuperEntityClass();
					fso.setName(node.getExpression().toString());
					fso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					fso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addEntity(fso, EntityType.FOR_STATEMENT);	
				}

				return true;
			}

			public boolean visit(FieldDeclaration node) {
				if(inInterface == false) {
					
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
							entityStack.peek().addEntity(ao, EntityType.ARRAY);
							entityStack.peek().addEntity(ao, EntityType.GLOBAL);
						}
						else if(nodeType.isParameterizedType()) {
							SuperEntityClass go = new SuperEntityClass();
							go.setName(name.toString());
							go.setFullyQualifiedName(fullyQualifiedName);
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(go, EntityType.GENERICS);
							entityStack.peek().addEntity(go, EntityType.GLOBAL);
						}
						else if(nodeType.isPrimitiveType()) {
							SuperEntityClass po = new SuperEntityClass();
							po.setName(name.toString());
							po.setFullyQualifiedName(fullyQualifiedName);
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(po, EntityType.PRIMITIVE);
							entityStack.peek().addEntity(po, EntityType.GLOBAL);
						}
						else if(nodeType.isSimpleType()) {
							SuperEntityClass so = new SuperEntityClass();
							so.setName(name.toString());
							so.setFullyQualifiedName(fullyQualifiedName);
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(so, EntityType.SIMPLE);
							entityStack.peek().addEntity(so, EntityType.GLOBAL);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				}
				
				return true;
			}			
			
			public boolean visit(ForStatement node) {
				if(inMethod) {
					SuperEntityClass fso = new SuperEntityClass();
					fso.setName(node.getExpression().toString());
					fso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					fso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addEntity(fso, EntityType.FOR_STATEMENT);				
				}

				return true;
			}

			public boolean visit(IfStatement node) {
				if(inMethod) {
					SuperEntityClass iso = new SuperEntityClass();
					iso.setName(node.getExpression().toString());
					iso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					iso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addEntity(iso, EntityType.IF_STATEMENT);
				}

				return true;
			}

			public boolean visit(ImportDeclaration node){
				Name name = node.getName();			
				
				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}
								
				SuperEntityClass io = new SuperEntityClass();
				io.setName(name.toString());
				io.setFullyQualifiedName(fullyQualifiedName);
				io.setLineNumber(cu.getLineNumber(name.getStartPosition()));
				io.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
				importList.add(io);
	
				return true;
			}

			public boolean visit(InfixExpression node){
				if(inMethod) {					
					SuperEntityClass ieo = new SuperEntityClass();
					ieo.setName(node.getOperator().toString());
					ieo.setLineNumber(cu.getLineNumber(node.getLeftOperand().getStartPosition()));
					ieo.setColumnNumber(cu.getColumnNumber(node.getLeftOperand().getStartPosition()));		
					entityStack.peek().addEntity(ieo, EntityType.INFIX_EXPRESSION);
				}

				return true;
			}
		
			public boolean visit(MethodDeclaration node) {
				if(inInterface == false) {
					inMethod = true;

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
					
					// get generic parameters
					List<String> genericParametersList = new ArrayList<>();
					if(binding.isGenericMethod()) {
						for(Object o : binding.getTypeParameters()) {
							genericParametersList.add(o.toString());
						}
					}
					
					MethodDeclarationObject md = new MethodDeclarationObject();
					md.setAbstract(isAbstract);
					md.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					md.setDeclaringClass(binding.getDeclaringClass().getQualifiedName());
					md.setConstructor(node.isConstructor());
					md.setEndLine(cu.getLineNumber(node.getStartPosition() + node.getLength()));
					md.setFullyQualifiedName(fullyQualifiedName);
					md.setGenericParametersList(genericParametersList);
					md.setIsGenericType(binding.isGenericMethod());
					md.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					md.setName(name.toString());
					md.setNumberOfCharacters(node.getLength());
					md.setParametersList(node.parameters());
					md.setParameterTypesList(parameterTypes);
					md.setReturnType(binding.getReturnType().getQualifiedName());
					md.setStatic(isStatic);
					md.setVarargs(node.isVarargs());

					if(node.thrownExceptionTypes().size() > 0) {
						for(Object o : node.thrownExceptionTypes()) {
							md.addThrowsException(o.toString());
						}
					}
					
					entityStack.push(md);			
				}
				return true;
			}

			public void endVisit(MethodDeclaration node) {
				if(inInterface == false) {
					MethodDeclarationObject temp = (MethodDeclarationObject) entityStack.pop();
					temp.setCyclomaticComplexity();
					entityStack.peek().addEntity(temp, EntityType.METHOD_DECLARATION);					
				}
				
				inMethod = false;
			}

			public boolean visit(MethodInvocation node) {
				
				if(inMethod) {					
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
					mio.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addEntity(mio, EntityType.METHOD_INVOCATION);
				}

				return true;
			}

			public boolean visit(PackageDeclaration node){
				Name name = node.getName();
				
				// get fully qualified name
				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}
				
				SuperEntityClass po = new SuperEntityClass();
				po.setName(node.getName().toString());
				po.setFullyQualifiedName(fullyQualifiedName);
				po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
				po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
				packageObject = po;
				
				return true;
			}
			
			public boolean visit(SingleVariableDeclaration node) {			
				if(inInterface == false) {
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
					entityStack.peek().addEntity(ao, EntityType.ARRAY);
				}
				
				else if(node.getType().isParameterizedType()) {
					SuperEntityClass go = new SuperEntityClass();
					go.setName(name.toString());
					go.setFullyQualifiedName(fullyQualifiedName);
					go.setType(node.getType());
					go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addEntity(go, EntityType.GENERICS);
				}
				else if(node.getType().isPrimitiveType()) {
					SuperEntityClass po = new SuperEntityClass();
					po.setName(name.toString());
					po.setFullyQualifiedName(fullyQualifiedName);
					po.setType(node.getType());
					po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addEntity(po, EntityType.PRIMITIVE);
				}
				else if(node.getType().isSimpleType()) {
					SuperEntityClass so = new SuperEntityClass();
					so.setName(name.toString());
					so.setFullyQualifiedName(fullyQualifiedName);
					so.setType(node.getType());
					so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addEntity(so, EntityType.SIMPLE);
				}
				else {
					System.out.println("Something is missing " + node.getType());
				}
				}
				return true;
			}

			public boolean visit(SwitchStatement node) {

				if(inMethod) {
					SuperEntityClass sso = new SuperEntityClass();
					sso.setName(node.getExpression().toString());
					sso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					sso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					
					List<SuperEntityClass> switchCaseList = new ArrayList<>();
					
					for(Object s : node.statements()) {
						if(s instanceof SwitchCase) {
							SuperEntityClass switchCase = new SuperEntityClass();
							
							String expression = "";
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
										
					sso.addEntities(switchCaseList, EntityType.SWITCH_CASE);
					entityStack.peek().addEntity(sso, EntityType.SWITCH_STATEMENT);
				}

				return true;
			}

			public boolean visit(ThrowStatement node) {
				if(inMethod) {
					SuperEntityClass to = new SuperEntityClass();
					to.setName(node.getExpression().toString());
					to.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					to.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));	
					entityStack.peek().addEntity(to, EntityType.THROW_STATEMENT);
				}
				return true;
			}

			public boolean visit(TryStatement node) {
				if(inMethod) {
					String tryBody = "Try Statement";

					SuperEntityClass tso = new SuperEntityClass();
					tso.setName(tryBody);
					tso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					tso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addEntity(tso, EntityType.TRY_STATEMENT);
				}

				return true;
			}

			public boolean visit(TypeDeclaration node) {

				if(node.isInterface()) {
					inInterface = true;
				}
				
				else {
					inInterface = false;
					
					int startLine = cu.getLineNumber(node.getStartPosition());
					int endLine = cu.getLineNumber(node.getStartPosition() + node.getLength());
					
					// get source code
					StringBuilder classSourceCode = new StringBuilder();
					
					Scanner scanner = new Scanner(sourceCode);
						for(int i = 0; i < startLine - 1; i++) {
							scanner.nextLine();
						}
					
						for(int j = startLine - 1; j < endLine - 1; j++) {
							classSourceCode.append(scanner.nextLine());
							classSourceCode.append(System.getProperty("line.separator"));
						}
						
						classSourceCode.append(scanner.nextLine());
					scanner.close();
					
					ITypeBinding binding = node.resolveBinding();					
					
					// get fully qualified name
					String fullyQualifiedName;
					try {
						fullyQualifiedName = node.getName().getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = node.getName().toString();
					}
									
					// get generic parameters
					List<String> genericParametersList = new ArrayList<>();
					if(binding.isGenericType()) {
						for(Object o : binding.getTypeParameters()) {
							genericParametersList.add(o.toString());
						}
					}
					
					JavaClass co = new JavaClass();
					co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					co.setEndLine(endLine);
					co.setLineNumber(startLine);
					co.setName(node.getName().toString());
					co.setNumberOfCharacters(node.getLength());
					co.setFileName(fileLocation);
					co.setFullyQualifiedName(fullyQualifiedName);
					co.setGenericParametersList(genericParametersList);
					co.setHasComments(hasComments);
					co.setImportList(importList);
					co.setIsGenericType(binding.isGenericType());
					co.setPackage(packageObject);
					co.setSourceCode(classSourceCode.toString());
					
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
				if(inInterface == false) {
					JavaClass temp = (JavaClass) entityStack.pop();
										
					// check if inner class
					boolean isInnerClass = true;
					try {
						entityStack.peek();
					} catch (EmptyStackException e) {
						isInnerClass = false;
					}					
					temp.setInnerClass(isInnerClass);
					
					temp.setCyclomaticComplexity();
					fileModel.addJavaClass(temp);					
				}
				
				hasComments = false;
				inInterface = false;
			}
			
			public boolean visit(VariableDeclarationStatement node) {
				if(inInterface == false) {
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
							entityStack.peek().addEntity(ao, EntityType.ARRAY);
						}
						else if(nodeType.isParameterizedType()) {
							SuperEntityClass go = new SuperEntityClass();
							go.setName(name.toString());
							go.setFullyQualifiedName(fullyQualifiedName);
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(go, EntityType.GENERICS);
						}
						else if(nodeType.isPrimitiveType()) {
							SuperEntityClass po = new SuperEntityClass();
							po.setName(name.toString());
							po.setFullyQualifiedName(fullyQualifiedName);
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(po, EntityType.PRIMITIVE);
						}
						else if(nodeType.isSimpleType()) {
							SuperEntityClass so = new SuperEntityClass();
							so.setName(name.toString());
							so.setFullyQualifiedName(fullyQualifiedName);
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(so, EntityType.SIMPLE);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				
				}
				return true;
			}

			public boolean visit(VariableDeclarationExpression node) {
				if(inInterface == false) {
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
							entityStack.peek().addEntity(ao, EntityType.ARRAY);
						}
						else if(nodeType.isParameterizedType()) {
							SuperEntityClass go = new SuperEntityClass();
							go.setName(name.toString());
							go.setFullyQualifiedName(fullyQualifiedName);
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(go, EntityType.GENERICS);
						}
						else if(nodeType.isPrimitiveType()) {
							SuperEntityClass po = new SuperEntityClass();
							po.setName(name.toString());
							po.setFullyQualifiedName(fullyQualifiedName);
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(po, EntityType.PRIMITIVE);
						}
						else if(nodeType.isSimpleType()) {
							SuperEntityClass so = new SuperEntityClass();
							so.setName(name.toString());
							so.setFullyQualifiedName(fullyQualifiedName);
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addEntity(so, EntityType.SIMPLE);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				}
				return true;
			}

			public boolean visit(WhileStatement node){
				if(inMethod) {
					SuperEntityClass wso = new SuperEntityClass();
					wso.setName(node.getExpression().toString());
					wso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					wso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));	
					entityStack.peek().addEntity(wso, EntityType.WHILE_STATEMENT);
				}
				return true;
			}

			public boolean visit(WildcardType node) {
				if(inMethod) {					
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
					entityStack.peek().addEntity(wo, EntityType.WILDCARD);
				}
				
				return false;
			}

		});

		return fileModel;

	}

}
