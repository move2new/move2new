package rnd.tool.code.parse.core;

import java.nio.file.Path;
import java.util.List;

public interface ClassParser<T> {

	String getSourceCode();

	Path getPath();

	String getClassName();

	String getClassSuffix();

	List<T> parse();

}
