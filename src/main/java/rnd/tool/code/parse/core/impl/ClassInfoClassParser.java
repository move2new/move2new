package rnd.tool.code.parse.core.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import rnd.tool.code.model.ClassInfo;
import rnd.tool.code.model.ClassInfoImpl;
import rnd.tool.code.model.ElementInfoImpl;
import rnd.tool.code.model.MethodInfo;
import rnd.tool.code.model.MethodInfo.HttpMethod;
import rnd.tool.code.model.MethodInfoImpl;
import rnd.tool.code.parse.core.AbstractClassParser;

public class ClassInfoClassParser extends AbstractClassParser<ClassInfo> {

	protected Logger logger = LogManager.getLogger(getClass().getName());

	protected List<ImportDeclaration> sourceImports;
	protected List<FieldDeclaration> sourceFieldMap;
	protected Map<String, MethodDeclaration> sourceMethodMap;

	public ClassInfoClassParser() {
	}

	protected List<ClassInfo> parseSource(List<CompilationUnit> sourceClasses) {

		sourceImports = new LinkedList<>();
		sourceFieldMap = new LinkedList<>();
		sourceMethodMap = new LinkedHashMap<>();

		List<ClassInfo> classDefs = new ArrayList<ClassInfo>(sourceClasses.size());

		for (CompilationUnit sourceClass : sourceClasses) {

			sourceClass.accept(new VoidVisitorAdapter<Object>() {

				@Override
				public void visit(ImportDeclaration impDec, Object arg) {
					sourceImports.add(impDec);
					super.visit(impDec, arg);
				}

				@Override
				public void visit(FieldDeclaration fieldDec, Object arg) {
					sourceFieldMap.add(fieldDec);
					super.visit(fieldDec, arg);
				}

				@Override
				public void visit(MethodDeclaration methDec, Object arg) {
					sourceMethodMap.put(methDec.getNameAsString(), methDec);
					super.visit(methDec, arg);
				}

			}, null);

			ClassOrInterfaceDeclaration sourceTypeDec = (ClassOrInterfaceDeclaration) sourceClass.getTypes().get(0);

			String className = sourceTypeDec.getNameAsString();

			logger.info("Started processing class : " + className);

			ClassInfoImpl classInfo = new ClassInfoImpl();

			parsePackageName(classInfo, sourceClass);
			parseImports(classInfo);
			parseClassName(classInfo, className);
			parseExtensions(classInfo, sourceTypeDec);
			parseFields(classInfo);
			parseMethods(classInfo);

			classDefs.add(classInfo);

			logger.info("Completed processing class : " + className);
		}

		return classDefs;
	}

	protected void parsePackageName(ClassInfoImpl classInfo, CompilationUnit sourceClass) {
		if (sourceClass.getPackageDeclaration().isPresent()) {
			classInfo.setPackageName(sourceClass.getPackageDeclaration().get().getNameAsString());
		}
	}

	protected void parseImports(ClassInfoImpl classInfo) {
		classInfo.setImports(sourceImports.stream().map(imp -> imp.toString().trim()).collect(Collectors.toSet()));
	}

	protected void parseClassName(ClassInfoImpl classInfo, String className) {
		classInfo.setName(getSourceClassName(className));
	}

	public String getSourceClassName(String className) {
		if (getClassSuffix().isEmpty()) {
			return className;
		}
		return className.substring(0, className.length() - getClassSuffix().length());
	}

	protected void parseExtensions(ClassInfoImpl classInfo, ClassOrInterfaceDeclaration sourceTypeDec) {
		sourceTypeDec.getExtendedTypes().forEach(ext -> {
			addExtension(classInfo, ext);
		});
		sourceTypeDec.getImplementedTypes().forEach(imp -> {
			addExtension(classInfo, imp);
		});
	}

	private void addExtension(ClassInfoImpl classInfo, ClassOrInterfaceType ext) {
		classInfo.addExtension(new ClassInfoImpl(ext.getNameAsString()));
	}

	protected void parseFields(ClassInfoImpl classInfo) {
		logger.info("Started default processing fields");
		for (FieldDeclaration fieldDec : sourceFieldMap) {

			VariableDeclarator varDec = fieldDec.getVariables().get(0);

			String type = varDec.getTypeAsString();
			String name = varDec.getNameAsString();

			ElementInfoImpl elem = new ElementInfoImpl(type, name);
			if (varDec.getInitializer().isPresent()) {
				elem.setInit(varDec.getInitializer().get().toString());
			}

			String modifiers = fieldDec.getModifiers().stream().map(Node::toString).collect(Collectors.joining(" "));
			elem.setModifiers(modifiers);

			processField(classInfo, elem);
		}
		logger.info("Completed default processing fields");
	}

	protected void processField(ClassInfoImpl classInfo, ElementInfoImpl elem) {
		classInfo.addField(elem);
	}

	protected void parseMethods(ClassInfoImpl classInfo) {
		sourceMethodMap.entrySet().stream().forEach(method -> processMethod(classInfo, method));
	}

	protected MethodInfo processMethod(ClassInfoImpl classInfo, Entry<String, MethodDeclaration> method) {
		logger.info("Started default processing method : " + classInfo.getName() + "." + method.getKey());
		String name = method.getKey();
		MethodDeclaration methodDec = method.getValue();
		MethodInfoImpl methodInfo = addMethod(classInfo, methodDec.getType().toString(), methodDec, name, null);
		logger.info("Completed default processing method : " + classInfo.getName() + "." + name);
		return methodInfo;
	}

	protected static MethodInfoImpl addMethod(ClassInfoImpl classInfo, String className, MethodDeclaration methodDec,
			String methodName, HttpMethod httpMethod) {

		MethodInfoImpl methodInfo = null;

		if (methodDec != null) {
			methodInfo = new MethodInfoImpl(methodName);
			methodInfo.setHttpMethod(httpMethod);
			methodInfo.setReturnType(className);
			List<Parameter> sourceParams = methodDec.getParameters();
			for (Parameter sp : sourceParams) {
				ElementInfoImpl param = new ElementInfoImpl(sp.getTypeAsString(), sp.getNameAsString());
				methodInfo.addParam(param);
			}
			// Add Indent for method body
			methodInfo.setDefinition(methodDec.getBody().get().toString().replace("\n", "\n\t"));

			classInfo.addMethod(methodInfo);
		}
		return methodInfo;
	}

}
