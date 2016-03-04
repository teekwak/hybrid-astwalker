package tools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
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

/**
 * Walks Java source code and parses constructs
 *
 * @author Thomas Kwak
 */
public class ASTWalker {

	public FileModel fileModel;
	public Stack<String> currentClassStack = new Stack<>();
	public Stack<String> currentMethodStack = new Stack<>();
	public boolean inMethod = false;

	/**
	 * Reads code file
	 *
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static char[] readFileToCharArray(String filePath) throws IOException {
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

		return fileData.toString().toCharArray();
	}

	public FileModel parseFile(String fileLocation) throws IOException, CoreException {
		this.fileModel = new FileModel();

		ASTParser parser = ASTParser.newParser(AST.JLS8);

		parser.setUnitName(fileLocation);
		parser.setEnvironment(null, null, null, true);
		parser.setSource(readFileToCharArray(fileLocation));
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// alphabetical order
		cu.accept(new ASTVisitor() {

			// done
			public boolean visit(CatchClause node) {
				if(inMethod) {
					SimpleName name = node.getException().getName();
					fileModel.catchClause__.addCatch(node.getException().getType(), name.toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(ConditionalExpression node){
				if(inMethod) {
					fileModel.conditionalExpression__.addConditionalExpression(node.getExpression().toString(), node.getElseExpression().toString(), node.getThenExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(DoStatement node) {
				if(inMethod) {
					fileModel.doStatement__.addDoStatement(node.getExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(EnhancedForStatement node) {
				if(inMethod) {
					SimpleName name = node.getParameter().getName();
					fileModel.forStatement__.addForStatement(name.toString(), currentClassStack.peek(), currentMethodStack.peek(), true, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}

				return true;
			}

			// done-ish. only returns middle conditional part of for statement
			public boolean visit(ForStatement node) {
				if(inMethod) {
					fileModel.forStatement__.addForStatement(node.getExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), false, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(IfStatement node) {
				if(inMethod) {
					fileModel.ifStatement__.addIfStatement(node.getExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(ImportDeclaration node){
				Name name = node.getName();				
				fileModel.import__.addImport(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			// done
			public boolean visit(InfixExpression node){
				if(inMethod) {
					fileModel.infixExpression__.addInfixExpression(node.getOperator().toString(), node.getLeftOperand().toString(), node.getRightOperand().toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(node.getLeftOperand().getStartPosition()), (cu.getColumnNumber(node.getLeftOperand().getStartPosition())));
				}

				return true;
			}

			// NOT COMPLETE
			public boolean visit(MethodDeclaration node) {
				currentMethodStack.push(node.getName().toString());
				inMethod = true;

				SimpleName name = node.getName();

				IMethodBinding binding = node.resolveBinding();
				ITypeBinding className = binding.getDeclaringClass();

				fileModel.methodDeclaration__.addMethodDeclaration(name.toString(), className.getName(), node.getReturnType2(), node.parameters(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				fileModel.class__.addMethodDeclarationToClass(className.getName(), name.toString());
				return true;
			}

			public void endVisit(MethodDeclaration node) {
				inMethod = false;
				currentMethodStack.pop();
			}

			// NOT COMPLETE
			public boolean visit(MethodInvocation node) {
				if(inMethod) {
					SimpleName name = node.getName();

					IMethodBinding binding = node.resolveMethodBinding();
					
					String parentClass;
					
					try {
						parentClass = binding.getDeclaringClass().getName();
					} catch (NullPointerException e) {
						parentClass = "";
					}
					
					fileModel.methodInvocation__.addMethodInvocation(name.toString(), parentClass, currentClassStack.peek(), node.arguments(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(PackageDeclaration node){
				Name name = node.getName();
				fileModel.package__.addPackage(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			// called on parameters of function
			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(SingleVariableDeclaration node) {				
				SimpleName name = node.getName();
				String currentMethod;
				
				try {
					currentMethod = currentMethodStack.peek();
				} catch (EmptyStackException e) {
					currentMethod = "N/A";
				}
				
				if(node.getType().isArrayType()) {
					fileModel.array__.addArray(name.toString(), currentClassStack.peek(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isParameterizedType()) {
					fileModel.generics__.addGenerics(name.toString(), currentClassStack.peek(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isPrimitiveType()) {
					fileModel.primitive__.addPrimitive(name.toString(), currentClassStack.peek(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isSimpleType()) {
					fileModel.simpleName__.addSimpleName(name.toString(), currentClassStack.peek(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else {
					System.out.println("Something is missing " + node.getType());
				}

				return true;
			}

			// done
			public boolean visit(SwitchStatement node) {
				if(inMethod) {
					Map<String, Map<Integer, Integer>> switchCaseMap = new HashMap<>();

					for(Object s : node.statements()) {
						if(s instanceof SwitchCase) {
							Map<Integer, Integer> position = new HashMap<>();

							String expression = "";
							try {
								expression = ((SwitchCase) s).getExpression().toString();
							} catch (NullPointerException e) {
								expression = "Default";
							}

							position.put(cu.getLineNumber(((SwitchCase) s).getStartPosition()), cu.getColumnNumber(((SwitchCase)s).getStartPosition()));
							switchCaseMap.put(expression, position);
						}
					}

					fileModel.switchStatement__.addSwitchStatement(node.getExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), switchCaseMap, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(ThrowStatement node) {
				if(inMethod) {
					fileModel.throwStatement__.addThrowStatement(node.getExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}
				return true;
			}

			// done
			public boolean visit(TryStatement node) {
				if(inMethod) {
					String tryBody = "";
					String finallyBody = "";

					try {
						tryBody = node.getBody().toString();
					} catch (NullPointerException e1) {
						tryBody = "";
					}

					try {
						finallyBody = node.getFinally().toString();
					} catch (NullPointerException e2) {
						finallyBody = "";
					}

					fileModel.tryStatement__.addTryStatement(tryBody, currentClassStack.peek(), currentMethodStack.peek(), node.catchClauses(), finallyBody, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			// done
			public boolean visit(TypeDeclaration node) {
				currentClassStack.push(node.getName().toString());

				if(!node.isInterface()) {
					fileModel.class__.addClass(node.getName().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));

					if(node.getSuperclassType() != null) {
						fileModel.class__.addExtends(node.getSuperclassType().toString(), cu.getLineNumber(node.getStartPosition()));
					}

					if(node.superInterfaceTypes().size() != 0) {
						for(Object o : node.superInterfaceTypes()) {
							fileModel.interface__.addImplements(o.toString(), cu.getLineNumber(node.getStartPosition()));
						}
					}
				}
				else {
					fileModel.interface__.addInterface(node.getName().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

			public void endVisit(TypeDeclaration node) {
				currentClassStack.pop();
			}

			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				Type nodeType = ((FieldDeclaration) node.getParent()).getType();
				String currentMethod;
				
				try {
					currentMethod = currentMethodStack.peek();
				} catch (EmptyStackException e) {
					currentMethod = "N/A";
				}
				
				if(nodeType.isArrayType()) {
					fileModel.array__.addArray(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(nodeType.isParameterizedType()) {
					fileModel.generics__.addGenerics(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(nodeType.isPrimitiveType()) {
					fileModel.primitive__.addPrimitive(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(nodeType.isSimpleType()) {
					fileModel.simpleName__.addSimpleName(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else {
					System.out.println("Something is missing " + nodeType);
				}
				

				return true;
			}

			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationStatement node) {
			
				Type nodeType = node.getType();

				String currentMethod;
				
				try {
					currentMethod = currentMethodStack.peek();
				} catch (EmptyStackException e) {
					currentMethod = "N/A";
				}
				
				for(Object v : node.fragments()) {
					
					SimpleName name = ((VariableDeclarationFragment) v).getName();
					
					if(nodeType.isArrayType()) {
						fileModel.array__.addArray(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else if(nodeType.isParameterizedType()) {
						fileModel.generics__.addGenerics(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else if(nodeType.isPrimitiveType()) {
						fileModel.primitive__.addPrimitive(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else if(nodeType.isSimpleType()) {
						fileModel.simpleName__.addSimpleName(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else {
						System.out.println("Something is missing " + nodeType);
					}
				}
				

				return false; // does this stop from going to VariableDeclarationFragment?
			}

			// exactly the same as visit(VariableDeclarationStatement node)
			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationExpression node) {
				if(inMethod) {
					Type nodeType = node.getType();

					String currentMethod;
					
					try {
						currentMethod = currentMethodStack.peek();
					} catch (EmptyStackException e) {
						currentMethod = "N/A";
					}
					
					for(Object v : node.fragments()) {
						SimpleName name = ((VariableDeclarationFragment) v).getName();

						if(nodeType.isArrayType()) {
							fileModel.array__.addArray(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else if(nodeType.isParameterizedType()) {
							fileModel.generics__.addGenerics(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else if(nodeType.isPrimitiveType()) {
							fileModel.primitive__.addPrimitive(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else if(nodeType.isSimpleType()) {
							fileModel.simpleName__.addSimpleName(name.toString(), currentClassStack.peek(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				}

				return false; // does this stop from going to VariableDeclarationFragment?
			}

			// done
			public boolean visit(WhileStatement node){
				if(inMethod) {
					fileModel.whileStatement__.addWhileStatement(node.getExpression().toString(), currentClassStack.peek(), currentMethodStack.peek(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}
				return true;
			}

			// done
			public boolean visit(WildcardType node) {
				if(inMethod) {
					fileModel.wildcard__.addWildcard(currentClassStack.peek(), currentMethodStack.peek(), ((ParameterizedType) node.getParent()).getType(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}
				return false;
			}

		});

		return fileModel;

	}

}
