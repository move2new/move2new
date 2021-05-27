package rnd.tool.code.parse.core.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import rnd.tool.code.parse.core.AbstractClassParser;

public class CompilationUnitClassParser extends AbstractClassParser<CompilationUnit> {

	@Override
	protected List<CompilationUnit> parseSource(List<CompilationUnit> sourceClasses) {
		List<CompilationUnit> newSourceClasses = new ArrayList<>();

		for (CompilationUnit sourceClass : sourceClasses) {
			ClassOrInterfaceDeclaration sourceTypeDec = (ClassOrInterfaceDeclaration) sourceClass.getTypes().get(0);
			boolean applicable = checkClass(sourceClass, sourceTypeDec);
			if (applicable) {
				newSourceClasses.add(sourceClass);
			}
		}

		return newSourceClasses;
	}

	protected boolean checkClass(CompilationUnit sourceClass, ClassOrInterfaceDeclaration sourceTypeDec) {
		return true;
	}
	
	protected static boolean checkExtension(ClassOrInterfaceDeclaration sourceTypeDec, String extClassName) {
		return sourceTypeDec.getExtendedTypes().stream().anyMatch(ext -> ext.getNameAsString().equals(extClassName));
	}
}
