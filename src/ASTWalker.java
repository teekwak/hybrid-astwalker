import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
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

import entities.ClassNames;
import entities.InterfaceNames;
import entities.PackageNames;
import entities.PrimitiveNames;

/**
 * Walks Java source code and parses constructs
 * 
 * @author Thomas Kwak
 */
public class ASTWalker {
	
	public FileModel fileModel;
	
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
	
	public FileModel parseFile(String fileLocation) throws IOException {
		this.fileModel = new FileModel();
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);

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
				SimpleName name = node.getException().getName();
				fileModel.catchClauseNames.addCatch(node.getException().getType(), name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return false;			
			}
			
			// done
			public boolean visit(ConditionalExpression node){
				fileModel.conditionalExpressionExpressions.addConditionalExpression(node.getExpression().toString(), node.getElseExpression().toString(), node.getThenExpression().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				return true;		
			}
			
			// done
			public boolean visit(DoStatement node) {
				fileModel.doStatementExpressions.addDoStatement(node.getExpression().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));				
				return true;				
			}
			
			// done
			public boolean visit(EnhancedForStatement node) {
				SimpleName name = node.getParameter().getName();			
				fileModel.forStatementExpressions.addForStatement(name.toString(), true, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return true;				
			}
			
			// done - ish. only returns middle conditional part of for statement
			public boolean visit(ForStatement node) {
				fileModel.forStatementExpressions.addForStatement(node.getExpression().toString(), false, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()) );				
				return true;				
			}
			
			// done
			public boolean visit(IfStatement node) {
				fileModel.ifStatementExpressions.addIfStatement(node.getExpression().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));				
				return true;
			}
			
			// done
			public boolean visit(ImportDeclaration node){
				Name name = node.getName();
				fileModel.importNames.addImport(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));				
				return true;
			}
			
			// done
			public boolean visit(InfixExpression node){
				fileModel.infixExpressionExpressions.addInfixExpression(node.getOperator().toString(), node.getLeftOperand().toString(), node.getRightOperand().toString(), cu.getLineNumber(node.getLeftOperand().getStartPosition()), (cu.getColumnNumber(node.getLeftOperand().getStartPosition()) + cu.getColumnNumber(node.getRightOperand().getStartPosition())) / 2);
				return true;
			}
			
			// NOT COMPLETE
			public boolean visit(MethodDeclaration node) {
				SimpleName name = node.getName();
				fileModel.methodNames.addMethod(name.toString(), node.getReturnType2(), node.parameters(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			// NOT COMPLETE
			public boolean visit(MethodInvocation node) {
				SimpleName name = node.getName();
				fileModel.methodNames.addMethodInvocation(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));				
				return true;					
			}
			
			// done
			public boolean visit(PackageDeclaration node){
				Name name = node.getName();
				fileModel.packageNames.addPackage(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));				
				return true;
			}			
			
			// called on parameters of function
			public boolean visit(SingleVariableDeclaration node) {
				SimpleName name = node.getName();
								
				
				System.out.println("SVD: " + name.toString() + ": " + node.getType());
				
				
				if(node.getType().isPrimitiveType()) {
					fileModel.primitiveNames.addPrimitive(name.toString(), node.getType().toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if(node.getType().isParameterizedType()) {
					fileModel.genericsNames.addGenerics(name.toString(), node.getType().toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else if (node.getType().isArrayType()) {					
					fileModel.arrayNames.addArray(name.toString(), node.getType().toString().replace("[]", ""), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				}
				else {
					// this does get run!
					System.out.println("Something is missing " + node.getType().toString());
				}
				
				return true;
			}
			
			// done
			public boolean visit(SwitchStatement node) {				
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
				
				fileModel.switchStatementNames.addSwitchStatement(node.getExpression().toString(), switchCaseMap, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				return true;
			}
			
			// done
			public boolean visit(ThrowStatement node) {
				fileModel.throwStatementNames.addThrowStatement(node.getExpression().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));				
				return true;	
			}

			// done
			public boolean visit(TryStatement node) {
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
				
				fileModel.tryStatementNames.addTryStatement(tryBody, node.catchClauses(), finallyBody, cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				return true;				
			}
			
			// done
			public boolean visit(TypeDeclaration node) {
				if(!node.isInterface()) {
					fileModel.classNames.addClass(node.getName().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
					
					if(node.getSuperclassType() != null) {
						fileModel.classNames.addExtends(node.getSuperclassType().toString());
					}
					
					if(node.superInterfaceTypes().size() != 0) {
						for(Object o : node.superInterfaceTypes()) {
							fileModel.interfaceNames.addImplements(o.toString());
						}
					}
				} 
				else {
					fileModel.interfaceNames.addInterface(node.getName().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				}
				
				return true;				
			}
			

			/*
			// depends on type
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				
				System.out.print("(Fragment: " + node.getName().toString() + ")");
				
				if(node.getInitializer() instanceof ClassInstanceCreation) {
					ClassInstanceCreation instanceCreation = (ClassInstanceCreation)node.getInitializer();
					if(instanceCreation.getType() instanceof SimpleType) {
						SimpleType simpleType = (SimpleType)instanceCreation.getType();
						System.out.println(simpleType.getName().getFullyQualifiedName());
					}
				}
				
				else {
					Type varType = null;
					try {
						varType = ((FieldDeclaration)node.getParent()).getType();
					}
					catch(ClassCastException e) {
						System.out.println("Error: " + node.getName().toString() + " " + cu.getLineNumber(name.getStartPosition()));
						e.printStackTrace();
						System.exit(-1);
					}
					
					System.out.println(varType + " WORKS FINE");
					
					if(varType.isPrimitiveType()) {
						fileModel.primitiveNames.addPrimitive(name.toString(), varType, cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
					}
					
					if(varType.isParameterizedType()) {
						System.out.println("PARAMETERIZED");
					}
					
					//fileModel.add(node, cu.getLineNumber(node.getName().getStartPosition()), cu.getColumnNumber(node.getName().getStartPosition()));

				}
				
				return true;
			}
			*/
			
			// NOT COMPLETE
			public boolean visit(VariableDeclarationStatement node) {
				System.out.println("VDS " + node.getType());
				return false; // does this stop from going to VariableDeclarationFragment?
			}
			
			// NOT COMPLETE
			public boolean visit(VariableDeclarationExpression node) {
				System.out.println("VDE");
				return false; // does this stop from going to VariableDeclarationFragment?
			}
			
			// done
			public boolean visit(WhileStatement node){		
				fileModel.whileStatementExpressions.addWhileStatement(node.getExpression().toString(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));
				return true;	
			}	
			
			// done
			public boolean visit(WildcardType node) {	
				fileModel.wildCardNames.addWildCard(((ParameterizedType) node.getParent()).getType(), cu.getLineNumber(node.getStartPosition()), cu.getColumnNumber(node.getStartPosition()));				
				return true;
			}
			
		});		
	
		return fileModel;
		
	}
	
}
