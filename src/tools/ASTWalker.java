package tools;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
// import org.eclipse.jdt.core.dom.ReturnStatement;
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
import entities.MethodInvocationObject;
import entities.PackageObject;
import entities.PrimitiveObject;
import entities.SimpleObject;
import entities.SwitchStatementObject;
// import entities.ReturnStatementObject;
import entities.ThrowObject;
import entities.WhileStatementObject;
import entities.WildcardObject;
import entities.TryStatementObject;
import entities.ArrayObject;
import entities.CatchClauseObject;
import entities.DoStatementObject;
import entities.Entity;
import entities.ForStatementObject;
import entities.GenericsObject;
import entities.IfStatementObject;
import entities.ImportObject;
import entities.InfixExpressionObject;


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
	public boolean inInterface = false; // ignoring interfaces

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
					ForStatementObject fso = new ForStatementObject();
					fso.setName(node.getExpression().toString());
					fso.setEnhanced(false);
					fso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					fso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(fso);				
				}

				return true;
			}

			public boolean visit(IfStatement node) {
				if(inMethod) {
					IfStatementObject iso = new IfStatementObject();
					iso.setName(node.getExpression().toString());
					iso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					iso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(iso);
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
								
				ImportObject io = new ImportObject();
				io.setName(name.toString());
				io.setFullyQualifiedName(fullyQualifiedName);
				io.setLineNumber(cu.getLineNumber(name.getStartPosition()));
				io.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
				importList.add(io);
	
				return true;
			}

			public boolean visit(InfixExpression node){
				if(inMethod) {					
					InfixExpressionObject ieo = new InfixExpressionObject();
					ieo.setName(node.getOperator().toString());
					ieo.setOperator(node.getOperator().toString());
					ieo.setLeftOperand(node.getLeftOperand().toString());
					ieo.setRightOperand(node.getRightOperand().toString());
					ieo.setLineNumber(cu.getLineNumber(node.getLeftOperand().getStartPosition()));
					ieo.setColumnNumber(cu.getColumnNumber(node.getLeftOperand().getStartPosition()));		
				}

				return true;
			}
			
			public boolean visit(MethodDeclaration node) {
				if(inInterface == false) {
					inMethod = true;

					SimpleName name = node.getName();
					
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
				}
				return true;
			}

			public void endVisit(MethodDeclaration node) {
				if(inInterface == false) {
					MethodDeclarationObject temp = (MethodDeclarationObject) entityStack.pop();
					entityStack.peek().addChild(temp);					
				}
				
				inMethod = false;
			}

			public boolean visit(MethodInvocation node) {
				
				if(inMethod) {					
					SimpleName name = node.getName();
					String fullyQualifiedName;
					
					try {
						fullyQualifiedName = name.getFullyQualifiedName();
					} catch (NullPointerException e) {
						fullyQualifiedName = "";
					}
									
					MethodInvocationObject mio = new MethodInvocationObject();
					mio.setName(name.toString());
					mio.setFullyQualifiedName(fullyQualifiedName);
					mio.setArguments(node.arguments());
					mio.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					mio.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(mio);
				}

				return true;
			}

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

/* ignoring for now
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
			
			// called on parameters of function
			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(SingleVariableDeclaration node) {			
				if(inInterface == false) {
				SimpleName name = node.getName();
				
				if(node.getType().isArrayType()) {
					ArrayObject ao = new ArrayObject();
					ao.setName(name.toString());
					ao.setType(node.getType());
					ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(ao);
				}
				
				else if(node.getType().isParameterizedType()) {
					GenericsObject go = new GenericsObject();
					go.setName(name.toString());
					go.setType(node.getType());
					go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(go);
				}
				else if(node.getType().isPrimitiveType()) {
					PrimitiveObject po = new PrimitiveObject();
					po.setName(name.toString());
					po.setType(node.getType());
					po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(po);
				}
				else if(node.getType().isSimpleType()) {
					SimpleObject so = new SimpleObject();
					so.setName(name.toString());
					so.setType(node.getType());
					so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(so);
				}
				else {
					System.out.println("Something is missing " + node.getType());
				}
				}
				return true;
			}

			public boolean visit(SwitchStatement node) {

				if(inMethod) {
					SwitchStatementObject sso = new SwitchStatementObject();
					sso.setName(node.getExpression().toString());
					sso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					sso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					
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
					
					sso.setSwitchCaseMap(switchCaseMap);

					entityStack.peek().addChild(sso);
				}

				return true;
			}

			public boolean visit(ThrowStatement node) {
				if(inMethod) {
					ThrowObject to = new ThrowObject();
					to.setName(node.getExpression().toString());
					to.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					to.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));	
					entityStack.peek().addChild(to);
				}
				return true;
			}


			public boolean visit(TryStatement node) {
				if(inMethod) {
					String tryBody = "Try Statement";

					TryStatementObject tso = new TryStatementObject();
					tso.setName(tryBody);
					tso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					tso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(tso);
				}

				return true;
			}

			// NOT DONE
			public boolean visit(TypeDeclaration node) {

				// need to not go into interfaces
				
				if(node.isInterface()) {
					inInterface = true;
				}
				
				else {
					inInterface = false;
					
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
				}
				
				return true;
			}

			public void endVisit(TypeDeclaration node) {				
				if(inInterface == false) {
					fileModel.javaFile.addClass((ClassObject) entityStack.pop());					
				}
				
				inInterface = false;
			}

			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationFragment node) {
				if(inInterface == false) {
				
				SimpleName name = node.getName();
				
				Type nodeType = ((FieldDeclaration) node.getParent()).getType();
				
				if(nodeType.isArrayType()) {
					ArrayObject ao = new ArrayObject();
					ao.setName(name.toString());
					ao.setType(nodeType);
					ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(ao);
				}
				else if(nodeType.isParameterizedType()) {
					GenericsObject go = new GenericsObject();
					go.setName(name.toString());
					go.setType(nodeType);
					go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(go);
				}
				else if(nodeType.isPrimitiveType()) {
					PrimitiveObject po = new PrimitiveObject();
					po.setName(name.toString());
					po.setType(nodeType);
					po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(po);
				}
				else if(nodeType.isSimpleType()) {
					SimpleObject so = new SimpleObject();
					so.setName(name.toString());
					so.setType(nodeType);
					so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
					entityStack.peek().addChild(so);
				}
				else {
					System.out.println("Something is missing " + nodeType);
				}
				}
				return true;
			}

			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationStatement node) {
				if(inInterface == false) {
				Type nodeType = node.getType();
				
				for(Object v : node.fragments()) {
					
					SimpleName name = ((VariableDeclarationFragment) v).getName();
					
					if(nodeType.isArrayType()) {
						ArrayObject ao = new ArrayObject();
						ao.setName(name.toString());
						ao.setType(nodeType);
						ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addChild(ao);
					}
					else if(nodeType.isParameterizedType()) {
						GenericsObject go = new GenericsObject();
						go.setName(name.toString());
						go.setType(nodeType);
						go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addChild(go);
					}
					else if(nodeType.isPrimitiveType()) {
						PrimitiveObject po = new PrimitiveObject();
						po.setName(name.toString());
						po.setType(nodeType);
						po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addChild(po);
					}
					else if(nodeType.isSimpleType()) {
						SimpleObject so = new SimpleObject();
						so.setName(name.toString());
						so.setType(nodeType);
						so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
						so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
						entityStack.peek().addChild(so);
					}
					else {
						System.out.println("Something is missing " + nodeType);
					}
				}
				
				}
				return false; // does this stop from going to VariableDeclarationFragment?
			}

			// done-ish. excluded qualifiedType, unionType, wildcardType
			public boolean visit(VariableDeclarationExpression node) {
				if(inInterface == false) {
					Type nodeType = node.getType();
					
					for(Object v : node.fragments()) {
						SimpleName name = ((VariableDeclarationFragment) v).getName();

						if(nodeType.isArrayType()) {
							ArrayObject ao = new ArrayObject();
							ao.setName(name.toString());
							ao.setType(nodeType);
							ao.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							ao.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addChild(ao);
						}
						else if(nodeType.isParameterizedType()) {
							GenericsObject go = new GenericsObject();
							go.setName(name.toString());
							go.setType(nodeType);
							go.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							go.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addChild(go);
						}
						else if(nodeType.isPrimitiveType()) {
							PrimitiveObject po = new PrimitiveObject();
							po.setName(name.toString());
							po.setType(nodeType);
							po.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							po.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addChild(po);
						}
						else if(nodeType.isSimpleType()) {
							SimpleObject so = new SimpleObject();
							so.setName(name.toString());
							so.setType(nodeType);
							so.setLineNumber(cu.getLineNumber(name.getStartPosition()));
							so.setColumnNumber(cu.getColumnNumber(name.getStartPosition()));
							entityStack.peek().addChild(so);
						}
						else {
							System.out.println("Something is missing " + nodeType);
						}
					}
				}
				return false; // does this stop from going to VariableDeclarationFragment?
			}

			public boolean visit(WhileStatement node){
				if(inMethod) {
					WhileStatementObject wso = new WhileStatementObject();
					wso.setName(node.getExpression().toString());
					wso.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					wso.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));	
					entityStack.peek().addChild(wso);
				}
				return true;
			}

			public boolean visit(WildcardType node) {
				if(inMethod) {					
					WildcardObject wo = new WildcardObject();
					wo.setName("Wildcard");
					wo.setType(((ParameterizedType) node.getParent()).getType());
					wo.setLineNumber(cu.getLineNumber(node.getStartPosition()));
					wo.setColumnNumber(cu.getColumnNumber(node.getStartPosition()));
					entityStack.peek().addChild(wo);					
				}
				
				return false;
			}

		});

		return fileModel;

	}

}
