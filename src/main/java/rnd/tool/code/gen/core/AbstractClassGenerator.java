package rnd.tool.code.gen.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractClassGenerator<T> implements ClassGenerator<T> {

	private Logger logger = LogManager.getLogger(getClass().getName());

	private String sourceCode;

	private Path path;
	private String className;

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getSourceCode() {
		return this.sourceCode;
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

	@Override
	public void generate(List<T> classes) {

		if (classes == null || classes.isEmpty()) {
			logger.warn("Skipping generation : No Source Class available");
			return;
		}

		createPath();

		for (T classInfo : classes) {

			try {

				String packageName = getSourcePackageName(classInfo);
				String className = getSourceClassName(classInfo);

				logger.info("Started class generation : " + className);

				Writer writer = new StringWriter();
				// Writer writer = new OutputStreamWriter(System.out);
				if (getPath() != null) {

					File dir;
					String targetClassName = getTargetClassName(className);

					if (packageName.isEmpty()) {
						dir = this.path.toFile();
						setClassName(targetClassName);
					} else {
						packageName = packageName.replace('.', File.separatorChar);
						dir = this.path.resolve(packageName).toFile();
						dir.mkdirs();
						logger.info("Created package : " + packageName);
						setClassName(packageName + '.' + targetClassName);
					}

					File clazzFile = new File(dir, targetClassName + ".java");
					logger.info("Creating java file : " + clazzFile.getName());

					writer = new PrintWriter(new BufferedWriter(new FileWriter(clazzFile)));
				}

				generateClass(writer, classInfo);

				if (getPath() == null) {
					setSourceCode(writer.toString());
				}

				writer.close();
				
				logger.info("Completed class generation : " + className);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void createPath() {
		if (this.path != null && !this.path.toFile().exists()) {
			this.path.toFile().mkdirs();
			logger.info("Created out path : " + this.path);
		}
	}

	protected abstract String getSourcePackageName(T sourceClass);

	protected abstract String getSourceClassName(T sourceClass);

	protected abstract void generateClass(Writer writer, T classInfo);

	protected String getTargetClassName(String className) {
		return className + getClassSuffix();
	}

	protected void writeln(Writer writer, String str) {
		write(writer, str);
		writeln(writer);
	}

	protected void writeln(Writer writer) {
		write(writer, System.lineSeparator());
	}

	protected void write(Writer writer, String str) {
		try {
			writer.write(str);
		} catch (IOException e) {
			logger.trace(e);
		}
	}

}