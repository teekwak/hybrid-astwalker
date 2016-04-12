package tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
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

import entities.JavaClass;

/**
 * Walks Java source code and parses constructs
 *
 * @author Thomas Kwak
 */
public class ASTStandalone {

	public void parseFile(final String fileLocation) throws IOException, CoreException {
			final File file = new File(fileLocation);
			
			String sourceCode = FileUtils.readFileToString(file);
			
			ASTParser parser = ASTParser.newParser(AST.JLS8);
	
			parser.setUnitName(fileLocation);
			parser.setEnvironment(null, null, null, true);
			parser.setSource(sourceCode.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			parser.setStatementsRecovery(true);
	
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			
			// alphabetical order
			cu.accept(new ASTVisitor() {

			public boolean visit(AnonymousClassDeclaration node) {
				System.out.println("Entered anonymous class");				
				return true;
			}
			
			public void endVisit(AnonymousClassDeclaration node) {
				System.out.println("Exited anonymous class");
			}
				
			public boolean visit(CatchClause node) {
				SimpleName name = node.getException().getName();
				//System.out.println("CatchClause of '" + name + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true; // false = do not continue to avoid usage info?
			}

			public boolean visit(ConditionalExpression node){
				//System.out.println("ConditionalExpression of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			public boolean visit(DoStatement node) {
				//System.out.println("DoStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			public boolean visit(EnhancedForStatement node) {
				SimpleName name = node.getParameter().getName();
				//System.out.println("EnhancedForStatement of '" + name  + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(ForStatement node) {
				//System.out.println("ForStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			public boolean visit(IfStatement node) {
				//System.out.println("IfStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			public boolean visit(ImportDeclaration node){
				Name name = node.getName();
				//System.out.println("ImportDeclaration of '" + name  + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(InfixExpression node){
				String name = node.getOperator().toString();
				//System.out.println("InfixExpression of '" + name  + "' at line " + cu.getLineNumber(node.getLeftOperand().getStartPosition()) + " " + (cu.getColumnNumber(node.getLeftOperand().getStartPosition()) + cu.getColumnNumber(node.getRightOperand().getStartPosition())) / 2);
				return true;
			}

			public boolean visit(MethodDeclaration node) {
				SimpleName name = node.getName();
				//System.out.println("MethodDeclaration of '" + name + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(MethodInvocation node) {
				SimpleName name = node.getName();
				//System.out.println("MethodInvocation of '" + name + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(PackageDeclaration node){
				Name name = node.getName();
				//System.out.println("PackageDeclaration of '" + name  + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			// called on parameters of function
			public boolean visit(SingleVariableDeclaration node) {
				SimpleName name = node.getName();
				//System.out.println("SingleVariableDeclaration of '" + name + "' of type '" + node.getType() + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(SwitchCase node) {
				try {
					//System.out.println("SwitchCase of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				} catch (NullPointerException e) {
					// default case
					//System.out.println("SwitchCase of default at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				}
				return true;
			}

			public boolean visit(SwitchStatement node) {
				//System.out.println("SwitchStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}


			public boolean visit(ThrowStatement node) {
				//System.out.println("ThrowStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			public boolean visit(TryStatement node) {
				//System.out.println("TryStatement at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			public boolean visit(TypeDeclaration node) {
				SimpleName name = node.getName();

				StringBuffer s = new StringBuffer();
				s.append("TypeDeclaration of '" + name + "' ");

				if(!node.isInterface()) {
					if(node.getSuperclassType() != null) {
						s.append("extends '" + node.getSuperclassType() + "' ");
					}

					if(node.superInterfaceTypes().size() != 0) {
						for(Object o : node.superInterfaceTypes()) {
							s.append("implements '" + o.toString() + "' ");
						}
					}
				} else {
					s.append(" is an Interface ");
				}

				s.append("at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));

				//System.out.println(s.toString());

				return true;
			}

			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				String[] type = node.getParent().toString().split(" ");
				//System.out.println("VariableDeclarationFragment of '" + name + "' of type '" + type[0] + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));

				return true;
			}

			public boolean visit(WhileStatement node){
				//System.out.println("WhileStatement of '" + node.getExpression() + "' at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}

			// weird case. returns Type
			public boolean visit(WildcardType node) {
				//System.out.println("WildcardType of " + node.getBound() + " at line " + cu.getLineNumber(node.getStartPosition()) + " " + cu.getColumnNumber(node.getStartPosition()));
				return true;
			}
		});
	}

	public static void main(String[] args) throws IOException {
		String fileLocation = "/home/kwak/Desktop/HelloWorldAnonymousClasses.java";
		ASTStandalone astWalkerStandalone = new ASTStandalone();

		try {
			astWalkerStandalone.parseFile(fileLocation);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
