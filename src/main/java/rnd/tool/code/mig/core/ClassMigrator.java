package rnd.tool.code.mig.core;

import rnd.tool.code.gen.core.ClassGenerator;
import rnd.tool.code.parse.core.ClassParser;
import rnd.tool.code.process.core.ClassProcessor;

public interface ClassMigrator<P extends ClassParser<?>, PR extends ClassProcessor<?>, G extends ClassGenerator<?>> {

	ClassMigrator<?, ?, ?> getChild();

	ClassMigrator<?, ?, ?> getParent();

	P getClassParser();
	
	PR getClassProcessor();

	G getClassGenerator();

	void migrate();

}
