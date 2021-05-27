package rnd.tool.code.gen.core.impl;

import java.io.Writer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import rnd.tool.code.gen.core.AbstractClassGenerator;

public class CompilationUnitClassGenerator extends AbstractClassGenerator<CompilationUnit> {

	@Override
	public String getClassSuffix() {
		return "";
	}

	protected String getSourcePackageName(CompilationUnit sourceClass) {
		String packageName = "";
		if (sourceClass.getPackageDeclaration().isPresent()) {
			packageName = sourceClass.getPackageDeclaration().get().getNameAsString();
		}
		return packageName;
	}

	protected String getSourceClassName(CompilationUnit sourceClass) {
		ClassOrInterfaceDeclaration sourceTypeDec = (ClassOrInterfaceDeclaration) sourceClass.getTypes().get(0);
		String className = sourceTypeDec.getNameAsString();
		return className;
	}

	protected void generateClass(Writer writer, CompilationUnit sourceClass) {
		write(writer, sourceClass.toString());
	}

}