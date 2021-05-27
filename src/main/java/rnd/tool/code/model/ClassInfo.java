package rnd.tool.code.model;

import java.util.List;
import java.util.Set;

public interface ClassInfo {

	public static enum Type {
		Class, Interface
	};

	String getPackageName();

	Set<String> getImports();

	List<ClassInfo> getExtensions();

	String getName();

	Type getType();

	List<ElementInfo> getFields();

	ElementInfo getField(String name);

	List<MethodInfo> getMethods();

	MethodInfo getMethod(String name);
}
