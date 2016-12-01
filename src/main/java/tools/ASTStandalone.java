package tools;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.*;

/**
 * Walks Java source code and parses constructs
 * THIS NEEDS TO BE UPDATED!!!
 *
 * @author Thomas Kwak
 */
public class ASTStandalone {
	private void parseFile(final String fileLocation) throws IOException, CoreException {
			final File file = new File(fileLocation);
			
			String sourceCode = FileUtils.readFileToString(file, "ISO-8859-1");
			
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
				System.out.println("CatchClause of '" + name + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
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

			public boolean visit(ImportDeclaration node){
				Name name = node.getName();
				System.out.println("ImportDeclaration of '" + name  + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(InfixExpression node){
				String name = node.getOperator().toString();
				System.out.println("InfixExpression of '" + name  + "' at line " + cu.getLineNumber(node.getLeftOperand().getStartPosition()) + " " + (cu.getColumnNumber(node.getLeftOperand().getStartPosition()) + cu.getColumnNumber(node.getRightOperand().getStartPosition())) / 2);
				return true;
			}

			public boolean visit(MethodDeclaration node) {
				SimpleName name = node.getName();
				System.out.println("MethodDeclaration of '" + name + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public void endVisit(MethodDeclaration node) {
				System.out.println("Exited MethodDeclaration of " + node.getName());
			}
			
			public boolean visit(MethodInvocation node) {
				SimpleName name = node.getName();
				System.out.println("MethodInvocation of '" + name + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
				return true;
			}

			public boolean visit(PackageDeclaration node){
				Name name = node.getName();
				System.out.println("PackageDeclaration of '" + name  + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));
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

			public boolean visit(TypeDeclaration node) {
				SimpleName name = node.getName();

				StringBuilder s = new StringBuilder();
				s.append("TypeDeclaration of '");
				s.append(name);
				s.append("' ");

				if(!node.isInterface()) {
					if(node.getSuperclassType() != null) {
						s.append("extends '");
						s.append(node.getSuperclassType());
						s.append("' ");
					}

					if(node.superInterfaceTypes().size() != 0) {
						for(Object o : node.superInterfaceTypes()) {
							s.append("implements '");
							s.append(o.toString());
							s.append("' ");
						}
					}
				} else {
					s.append(" is an Interface ");
				}

				s.append("at line ");
				s.append(cu.getLineNumber(name.getStartPosition()));
				s.append(" ");
				s.append(cu.getColumnNumber(name.getStartPosition()));

				System.out.println(s.toString());

				return true;
			}

			public void endVisit(TypeDeclaration node) {
				System.out.println("Exited class of " + node.getName());
			}
			
			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				String[] type = node.getParent().toString().split(" ");
				System.out.println("VariableDeclarationFragment of '" + name + "' of type '" + type[0] + "' at line " + cu.getLineNumber(name.getStartPosition()) + " " + cu.getColumnNumber(name.getStartPosition()));

				return true;
			}

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
	}

	private static void test1() {
		Runtime runtime = Runtime.getRuntime();
		long start = System.currentTimeMillis();

		// ******************************************

		String fileLocation = "resources/test.java";
		ASTStandalone astWalkerStandalone = new ASTStandalone();

		try {
			astWalkerStandalone.parseFile(fileLocation);
		} catch (IOException|CoreException e) {
			e.printStackTrace();
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

	public static void main(String[] args) throws IOException {
		test1();
	}
}
