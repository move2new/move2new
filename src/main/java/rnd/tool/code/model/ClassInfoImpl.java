package rnd.tool.code.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInfoImpl implements ClassInfo {

	public ClassInfoImpl() {
	}

	public ClassInfoImpl(String name) {
		this.name = name;
	}

	public ClassInfoImpl(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	private String packageName;

	private Set<String> imports = new HashSet<>();

	private Type type = Type.Class;

	private String name;

	private List<ClassInfo> extensions = new LinkedList<>();

	private Map<String, ElementInfo> fields = new LinkedHashMap<>();

	private Map<String, MethodInfo> methods = new LinkedHashMap<>();

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public Set<String> getImports() {
		return this.imports;
	}

	public void setImports(Set<String> imports) {
		this.imports = imports;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String className) {
		this.name = className;
	}

	@Override
	public List<ClassInfo> getExtensions() {
		return this.extensions;
	}

	public void setExtensions(List<ClassInfo> extensions) {
		this.extensions = extensions;
	}

	public void addExtension(ClassInfo extension) {
		this.extensions.add(extension);
	}

	@Override
	public List<ElementInfo> getFields() {
		List<ElementInfo> result = new ArrayList<>(fields.values());
		return Collections.unmodifiableList(result);
	}

	@Override
	public ElementInfo getField(String name) {
		return fields.get(name);
	}

	public void addField(ElementInfo field) {
		fields.put(field.getName(), field);
	}

	@Override
	public List<MethodInfo> getMethods() {
		List<MethodInfo> result = new ArrayList<>(methods.values());
		return Collections.unmodifiableList(result);
	}

	@Override
	public MethodInfo getMethod(String name) {
		return methods.get(name);
	}

	public void addMethod(MethodInfo methodInfo) {
		methods.put(methodInfo.getName(), methodInfo);
	}

	public String getFQN() {
		return packageName + "." + name;
	}

	@Override
	public String toString() {
		return getFQN();
	}
}