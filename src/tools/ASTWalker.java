package tools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
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

import entities.ClassObject;
import entities.ConditionalExpressionObject;
import entities.MethodDeclarationObject;
import entities.PackageObject;
// import entities.ReturnStatementObject;
import entities.ThrowObject;
import entities.WhileStatementObject;
import entities.CatchClauseObject;
import entities.DoStatementObject;
import entities.Entity;
import entities.ForStatementObject;
import entities.ImportObject;


/**
 * Walks Java source code and parses constructs
 *
 * @author Thomas Kwak
 */
public class ASTWalker {

	public FileModel fileModel;
	public Stack<Entity> entityStack = new Stack<>();
	public PackageObject packageObject = new PackageObject();
	public List<ImportObject> importList = new ArrayList<>();
	public boolean inMethod = false;

	/**
	 * Reads code file
	 *
	 * @param filePath = absolute path to file
	 * @return char[] of file
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

	/**
	 * Actually extracts constructs from code
	 * 
	 * @param fileLocation = absolute path to file
	 * @return FileModel object populated with constructs
	 * @throws IOException, CoreException
	 */
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

			@SuppressWarnings("unchecked")
			public boolean visit(CatchClause node) {
				if(inMethod) {
					SimpleName name = node.getException().getName();
					
					CatchClauseObject cco = new CatchClauseObject();
					cco.setName(name.toString());
					cco.setType(node.getException().getType());
					cco.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					cco.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(cco);
				}

				return true;
			}

			public boolean visit(ConditionalExpression node){
				if(inMethod) {
					ConditionalExpressionObject ceo = new ConditionalExpressionObject();
					ceo.setName(node.getExpression().toString());
					ceo.setElseExpression(node.getElseExpression().toString());
					ceo.setThenExpression(node.getThenExpression().toString());
					ceo.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					ceo.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(ceo);
				}

				return true;
			}

			public boolean visit(DoStatement node) {
				if(inMethod) {					
					DoStatementObject dso = new DoStatementObject();
					dso.setName(node.getExpression().toString());
					dso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					dso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));		
					entityStack.peek().addChild(dso);
				}
				return true;
			}

			public boolean visit(EnhancedForStatement node) {
				if(inMethod) {
					ForStatementObject fso = new ForStatementObject();
					fso.setName(node.getExpression().toString());
					fso.setEnhanced(true);
					fso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					fso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(fso);	
				}

				return true;
			}

			public boolean visit(ForStatement node) {
				if(inMethod) {
					//fileModel.forStatement__.addForStatement(node.getExpression().toString(), currentClassStack.peek().getName().toString(), currentMethodStack.peek().getName().toString(), false, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
					ForStatementObject fso = new ForStatementObject();
					fso.setName(node.getExpression().toString());
					fso.setEnhanced(false);
					fso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					fso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(fso);				
				}

				return true;
			}
/*
			public boolean visit(IfStatement node) {
				if(inMethod) {
					//fileModel.ifStatement__.addIfStatement(node.getExpression().toString(), currentClassStack.peek().getName().toString(), currentMethodStack.peek().getName().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}

*/

			public boolean visit(ImportDeclaration node){
				Name name = node.getName();			
				
				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}
								
				ImportObject io = new ImportObject();
				io.setName(name.toString());
				io.setFullyQualifiedName(fullyQualifiedName);
				io.setLineNumber(cu.getLineNumber(name.getStartPosition()));
				io.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
				importList.add(io);
	
				return true;
			}
/*
			public boolean visit(InfixExpression node){
				if(inMethod) {
					//fileModel.infixExpression__.addInfixExpression(node.getOperator().toString(), node.getLeftOperand().toString(), node.getRightOperand().toString(), currentClassStack.peek().getName().toString(), currentMethodStack.peek().getName().toString(), cu.getLineNumber(node.getLeftOperand().getStartPosition()), (cu.getColumnNumber(node.getLeftOperand().getStartPosition())));
					
					if(node.getOperator().toString().equals("&&") || node.getOperator().toString().equals("||")) {
					}
				}

				return true;
			}
*/
			@SuppressWarnings("unchecked")
			public boolean visit(MethodDeclaration node) {
				inMethod = true;

				SimpleName name = node.getName();
				
				IMethodBinding binding = node.resolveBinding();
				ITypeBinding className = binding.getDeclaringClass();

				boolean isStatic = false;
				boolean isAbstract = false;
				
				int mod = node.getModifiers();
				if(Modifier.isAbstract(mod)) {
					isAbstract = true;
				}
				
				if(Modifier.isStatic(mod)) {
					isStatic = true;
				}
				
				MethodDeclarationObject md = new MethodDeclarationObject();
				md.setName(name.toString());
				md.setLineNumber(cu.getLineNumber(name.getStartPosition()));
				md.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
				md.setStatic(isStatic);
				md.setAbstract(isAbstract);
				
				if(node.thrownExceptionTypes().size() > 0) {
					for(Object o : node.thrownExceptionTypes()) {
						md.addThrowsException(o.toString());
					}
				}
				
				entityStack.push(md);
				return true;
			}

			public void endVisit(MethodDeclaration node) {
				inMethod = false;
				MethodDeclarationObject temp = (MethodDeclarationObject) entityStack.pop();
				entityStack.peek().addChild(temp);
			}
/*
			@SuppressWarnings("unchecked")
			public boolean visit(MethodInvocation node) {
				
				if(inMethod) {					
					SimpleName name = node.getName();
					String fullyQualifiedName;
					
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = "";
					}
					
					IMethodBinding binding = node.resolveMethodBinding();
					
					String parentClass;
					
					try {
						parentClass = binding.getDeclaringClass().getName();
					} catch (NullPointerException e) {
						parentClass = "";
					}
					
					//fileModel.methodInvocation__.addMethodInvocation(name.toString(), fullyQualifiedName, parentClass, currentClassStack.peek().getName().toString(), node.arguments(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));	
				}

				return true;
			}
*/
			public boolean visit(PackageDeclaration node){
				Name name = node.getName();
				
				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}
				
				PackageObject po = new PackageObject();
				po.setName(node.getName().toString());
				po.setFullyQualifiedName(fullyQualifiedName);
				po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
				po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
				packageObject = po;
				
				return true;
			}

/*
			public boolean visit(ReturnStatement node) {
				if(inMethod) {
					String expression;
					
					try {
						expression = node.getExpression().toString();
					} catch (NullPointerException e) {
						expression = "";
					}
					
					ReturnStatementObject rso = new ReturnStatementObject();
					rso.setName(expression);
					rso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					rso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(rso);
				}
				return true;
			}
*/

			
/*			
			// called on parameters of function
			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(SingleVariableDeclaration node) {				
				SimpleName name = node.getName();
				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}
				
				String currentMethod;
				
				try {
					//currentMethod = currentMethodStack.peek().getName().toString();
				} catch (EmptyStackException e) {
					currentMethod = "N/A";
				}
				
				if(node.getType().isArrayType()) {
					//fileModel.array__.addArray(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isParameterizedType()) {
					//fileModel.generics__.addGenerics(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isPrimitiveType()) {
					//fileModel.primitive__.addPrimitive(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isSimpleType()) {
					//fileModel.simpleName__.addSimpleName(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, node.getType(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else {
					System.out.println("Something is missing " + node.getType());
				}
				
				return true;
			}

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

					//fileModel.switchStatement__.addSwitchStatement(node.getExpression().toString(), currentClassStack.peek().getName().toString(), currentMethodStack.peek().getName().toString(), switchCaseMap, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}
*/

			public boolean visit(ThrowStatement node) {
				if(inMethod) {
					//fileModel.throwStatement__.addThrowStatement(node.getExpression().toString(), currentClassStack.peek().getName().toString(), currentMethodStack.peek().getName().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
					ThrowObject to = new ThrowObject();
					to.setName(node.getExpression().toString());
					to.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					to.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));	
					entityStack.peek().addChild(to);
				}
				return true;
			}

/*
			@SuppressWarnings("unchecked")
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

					//fileModel.tryStatement__.addTryStatement(tryBody, currentClassStack.peek().getName().toString(), currentMethodStack.peek().getName().toString(), node.catchClauses(), finallyBody, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}

				return true;
			}
*/
			// NOT DONE
			public boolean visit(TypeDeclaration node) {

				// need to not go into interfaces
				
				ClassObject co = new ClassObject();
				co.setName(node.getName().toString());
				co.setLineNumber(cu.getLineNumber(node.getStartPosition()));
				co.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
				co.setPackageObject(packageObject);
				co.setImportList(importList);
				
				if(node.getSuperclassType() != null) {
					co.setSuperClass(node.getSuperclassType().toString());
				}
				
				if(node.superInterfaceTypes().size() > 0) {
					for(Object o : node.superInterfaceTypes()) {
						co.addImplementsInterface(o.toString());	
					}
				}
				
				entityStack.push(co);
				
				return true;
			}

			public void endVisit(TypeDeclaration node) {
				//currentClassStack.pop();
				// add class object to class__ and pop entityStack at the same time
				fileModel.javaFile.addClass((ClassObject) entityStack.pop());
				
			}
/*
			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				String fullyQualifiedName;
				try {
					fullyQualifiedName = name.getFullyQualifiedName();
				} catch (NullPointerException e) {
					fullyQualifiedName = "";
				}
				
				Type nodeType = ((FieldDeclaration) node.getParent()).getType();
				String currentMethod;
				
				try {
					//currentMethod = currentMethodStack.peek().getName().toString();
				} catch (EmptyStackException e) {
					currentMethod = "N/A";
				}
				
				if(nodeType.isArrayType()) {
					//fileModel.array__.addArray(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(nodeType.isParameterizedType()) {
					//fileModel.generics__.addGenerics(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(nodeType.isPrimitiveType()) {
					//fileModel.primitive__.addPrimitive(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(nodeType.isSimpleType()) {
					//fileModel.simpleName__.addSimpleName(name.toString(), fullyQualifiedName, currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
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
					//currentMethod = currentMethodStack.peek().getName().toString();
				} catch (EmptyStackException e) {
					currentMethod = "N/A";
				}
				
				for(Object v : node.fragments()) {
					
					SimpleName name = ((VariableDeclarationFragment) v).getName();
					
					if(nodeType.isArrayType()) {
						//fileModel.array__.addArray(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else if(nodeType.isParameterizedType()) {
						//fileModel.generics__.addGenerics(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else if(nodeType.isPrimitiveType()) {
						//fileModel.primitive__.addPrimitive(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					else if(nodeType.isSimpleType()) {
						//fileModel.simpleName__.addSimpleName(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
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
					Type nodeType = node.getType();

					String currentMethod;
					
					try {
						//currentMethod = currentMethodStack.peek().getName().toString();
					} catch (EmptyStackException e) {
						currentMethod = "N/A";
					}
					
					for(Object v : node.fragments()) {
						SimpleName name = ((VariableDeclarationFragment) v).getName();

						if(nodeType.isArrayType()) {
							//fileModel.array__.addArray(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else if(nodeType.isParameterizedType()) {
							//fileModel.generics__.addGenerics(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else if(nodeType.isPrimitiveType()) {
							//fileModel.primitive__.addPrimitive(name.toString(), "", currentClassStack.peek().getName().toString(), currentMethod, nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else if(nodeType.isSimpleType()) {
							//fileModel.simpleName__.addSimpleName(name.toString(), "", nodeType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				

				return false; // does this stop from going to VariableDeclarationFragment?
			}
*/
			public boolean visit(WhileStatement node){
				if(inMethod) {
					WhileStatementObject wo = new WhileStatementObject();
					wo.setName(node.getExpression().toString());
					wo.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					wo.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));	

					entityStack.peek().addChild(wo);
				}
				return true;
			}
/*
			public boolean visit(WildcardType node) {
				if(inMethod) {
					//fileModel.wildcard__.addWildcard(((ParameterizedType) node.getParent()).getType(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}
				return false;
			}
*/
		});

		return fileModel;

	}

}
