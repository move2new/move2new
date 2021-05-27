package rnd.tool.code.gen.core;

import java.nio.file.Path;
import java.util.List;

public interface ClassGenerator<T> {

	String getSourceCode();

	Path getPath();

	String getClassName();

	String getClassSuffix();

	void generate(List<T> classes);

}