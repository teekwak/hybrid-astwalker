import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
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

			// not done, does not get type of exception
			public boolean visit(CatchClause node) {
				SimpleName name = node.getException().getName();
				fileModel.catchClauseNames.addCatch(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return true; // false = do not continue to avoid usage info?				
			}
			
			public boolean visit(ConditionalExpression node){
				System.out.println("ConditionalExpression of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;		
			}
			
			public boolean visit(DoStatement node) {
				System.out.println("DoStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;				
			}
			
			public boolean visit(EnhancedForStatement node) {
				SimpleName name = node.getParameter().getName();
				System.out.println("EnhancedForStatement of '" + name  + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;				
			}
			
			public boolean visit(ForStatement node) {
				System.out.println("ForStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;				
			}
			
			public boolean visit(IfStatement node) {
				System.out.println("IfStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}
			
			// done
			public boolean visit(ImportDeclaration node){
				Name name = node.getName();
				fileModel.importNames.addImport(name.toString(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));				
				return true;
			}
			
			public boolean visit(InfixExpression node){
				String name = node.getOperator().toString();
				System.out.println("InfixExpression of '" + name  + "' at line " + cu.getLineNumber(node.getLeftOperand().getStartPosition()) + " " + (cu.getColumnNumber(node.getLeftOperand().getStartPosition()) + cu.getColumnNumber(node.getRightOperand().getStartPosition())) / 2);
				return true;
			}
			
			
			public boolean visit(MethodDeclaration node) {
				SimpleName name = node.getName();
				fileModel.methodNames.addMethod(name.toString(), node.getReturnType2(), node.parameters(), cu.getLineNumber(name.getStartPosition()), cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			
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
				System.out.println("SingleVariableDeclaration of '" + name + "' of type '" + node.getType() + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(SwitchCase node) {
				try {
					System.out.println("SwitchCase of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				} catch (NullPointerException e) {
					// default case
					System.out.println("SwitchCase of default at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				}
				return true;				
			}
			
			public boolean visit(SwitchStatement node) {
				System.out.println("SwitchStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}
			
			
			public boolean visit(ThrowStatement node) {
				System.out.println("ThrowStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;	
			}

			public boolean visit(TryStatement node) {
				System.out.println("TryStatement at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
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
				fileModel.add(node, cu.getLineNumber(node.getName().getStartPosition()), cu.getColumnNumber(node.getName().getStartPosition()));
				return true;
			}
			*/
 
			public boolean visit(WhileStatement node){				
				System.out.println("WhileStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;	
			}	
			
			// weird case. returns Type
			public boolean visit(WildcardType node) {				
				System.out.println("WildcardType of " + node.getBound() + " at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}
			
		});		
	
		return fileModel;
		
	}
	
}
