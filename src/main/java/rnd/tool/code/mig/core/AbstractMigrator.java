package rnd.tool.code.mig.core;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rnd.tool.code.gen.core.AbstractClassGenerator;
import rnd.tool.code.parse.core.AbstractClassParser;
import rnd.tool.code.process.core.AbstractClassProcessor;

public class AbstractMigrator<T>
		implements ClassMigrator<AbstractClassParser<T>, AbstractClassProcessor<T>, AbstractClassGenerator<T>> {

	private Logger logger = LogManager.getLogger(getClass().getName());

	protected AbstractClassParser<T> classPar;
	protected AbstractClassProcessor<T> classPro;
	protected AbstractClassGenerator<T> classGen;

	protected AbstractMigrator<T> parentMig;

	protected AbstractMigrator<T> childMig;

	public AbstractMigrator<T> child(AbstractMigrator<T> childMig) {
		this.childMig = childMig.parent(this);
		return this;
	}

	public AbstractMigrator<T> getChild() {
		return childMig;
	}

	public AbstractMigrator<T> parent(AbstractMigrator<T> parentMig) {
		this.parentMig = parentMig;
		return this;
	}

	public AbstractMigrator<T> getParent() {
		return parentMig;
	}

	public AbstractClassParser<T> getClassParser() {
		return classPar;
	}

	public AbstractClassProcessor<T> getClassProcessor() {
		return this.classPro;
	}

	public AbstractClassGenerator<T> getClassGenerator() {
		return classGen;
	}

	@Override
	public void migrate() {

		logger.info("Started migrating : " + getClass().getSimpleName());

		// Parse
		List<T> classes = getClassParser().parse();

		// Process
		AbstractClassProcessor<T> classProcessor = getClassProcessor();
		if (classProcessor != null) {
			classProcessor.process(classes);
		}

		// Generate
		getClassGenerator().generate(classes);

		logger.info("Completed migrating : " + getClass().getSimpleName());

		if (this.childMig != null) {

			logger.info("Started migrating chain : " + this.childMig.getClass().getSimpleName());

			this.childMig.migrate();

			logger.info("Completed migrating chain : " + this.childMig.getClass().getSimpleName());

		}

	}

}
