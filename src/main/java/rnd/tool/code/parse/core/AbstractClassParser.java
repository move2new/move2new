package rnd.tool.code.parse.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public abstract class AbstractClassParser<T> implements ClassParser<T> {

	protected Logger logger = LogManager.getLogger(getClass().getName());

	private String sourceCode;

	private Path path;
	private String className;

	private String classSuffix = "";

	private List<CompilationUnit> sourceClasses;

	public AbstractClassParser() {
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassSuffix(String classSuffix) {
		this.classSuffix = classSuffix;
	}

	@Override
	public String getClassSuffix() {
		return this.classSuffix;
	}

	@Override
	public final List<T> parse() {

		this.sourceClasses = new LinkedList<>();

		if (this.path != null) {
			logger.debug("Started parsing source path");
			parseSourcePath();
		} else if (this.sourceCode != null) {
			logger.debug("Started parsing source code");
			parseSourceCode();
		} else {
			logger.warn("Skipping parsing : No source available");
		}

		List<T> classes = parseSource(this.sourceClasses);
		
		
		
		return classes;
	}

	protected void parseSourcePath() {

		File sourcePath = this.path.toFile();

		// Single Source File
		boolean isFile = sourcePath.isFile();
		logger.debug("Validating source file : " + isFile);
		if (isFile) {
			parseSourceFile(sourcePath);
			return;
		}

		// Single Source File in package
		if (className != null && !className.isEmpty()) {

			File sourceFile = this.path.resolve(className.replace('.', File.separatorChar) + ".java").toFile();
			boolean exists = sourceFile.exists();
			logger.debug("Validating source class : " + exists);
			if (exists) {
				parseSourceFile(sourceFile);
			} else {
				logger.error("Invalid source file : " + sourceFile.getAbsolutePath());
			}
			return;
		}

		// All Files in package
		boolean exists = sourcePath.exists();
		logger.info("Validating source directory : " + exists);
		if (exists) {
			File[] sourceFiles = sourcePath.listFiles();
			for (File sourceFile : sourceFiles) {
				if (sourceFile.isFile()) {
					parseSourceFile(sourceFile);
				}
			}
		} else {
			logger.error("Invalid source file : " + sourcePath.getAbsolutePath());
		}

	}

	protected void parseSourceFile(File sourceFile) {
		logger.info("Started parsing source file : " + sourceFile.getAbsolutePath());
		try (InputStream in = new FileInputStream(sourceFile)) {
			parseSource(new InputStreamReader(in));
		} catch (IOException ex) {
			logger.error(ex);
			throw new IllegalArgumentException(ex);
		}
		logger.info("Completed parsing source file");
	}

	protected void parseSourceCode() {
		logger.info("Started parsing source code");
		parseSource(new StringReader(this.sourceCode));
	}

	protected void parseSource(Reader reader) {
		this.sourceClasses.add(StaticJavaParser.parse(reader));
	}

	protected abstract List<T> parseSource(List<CompilationUnit> sourceClasses);

}
