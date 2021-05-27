package rnd.tool.code.process.core.impl;

import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.visitor.ModifierVisitor;

import rnd.tool.code.process.core.AbstractClassProcessor;

public class CompilationUnitClassProcessor extends AbstractClassProcessor<CompilationUnit> {

	private Logger logger = LogManager.getLogger(getClass().getName());
	
	private Properties mapping;

	public CompilationUnitClassProcessor() {
	}

	public CompilationUnitClassProcessor(Properties mapping) {
		this.mapping = mapping;
	}

	public Properties getMapping() {
		return mapping;
	}

	public void setMapping(Properties mapping) {
		this.mapping = mapping;
	}

	@Override
	public final void process(List<CompilationUnit> sourceClasses) {
		
		if (sourceClasses == null || sourceClasses.isEmpty()) {
			logger.warn("Skipping processing : No Source Class available");
			return;
		}

		for (CompilationUnit sourceClass : sourceClasses) {

			sourceClass.accept(new ModifierVisitor<Object>() {

				@Override
				public Name visit(Name n, Object arg) {
					if (mapping != null) {
						String targetType = mapping.getProperty(n.toString());
						if (targetType != null) {
							return StaticJavaParser.parseName(targetType);
						}
					}
					return n;
				}

				@Override
				public SimpleName visit(SimpleName n, Object arg) {
					if (mapping != null) {
						String targetType = mapping.getProperty(n.toString());
						if (targetType != null) {
							return new SimpleName(targetType);
						}
					}
					return n;

				}

			}, null);

			ClassOrInterfaceDeclaration sourceTypeDec = (ClassOrInterfaceDeclaration) sourceClass.getTypes().get(0);

			String className = sourceTypeDec.getNameAsString();

			logger.info("Started processing class : " + className);

			processClass(sourceClass, sourceTypeDec);

			logger.info("Completed processing class : " + className);
		}

	}

	protected void processClass(CompilationUnit sourceClass, ClassOrInterfaceDeclaration sourceTypeDec) {
	}

	protected static boolean removeImportAndExtension(CompilationUnit clazz, ClassOrInterfaceDeclaration typeDec,
			String name) {
		clazz.getImports().removeIf(imp -> imp.getNameAsString().equals(name));
		String extClassName = name.substring(name.lastIndexOf('.') + 1);
		boolean changed = typeDec.getExtendedTypes().removeIf(ext -> ext.getNameAsString().equals(extClassName));
		return changed;
	}

}
