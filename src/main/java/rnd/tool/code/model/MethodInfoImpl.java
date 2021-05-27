package rnd.tool.code.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MethodInfoImpl implements MethodInfo {

	private String type;
	private String name;

	private String exceptions;
	private HttpMethod httpMethod;

	private List<ElementInfo> params = new LinkedList<>();

	private String definition;

	public MethodInfoImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setReturnType(String returnType) {
		this.type = returnType;
	}

	@Override
	public List<ElementInfo> getParams() {
		return Collections.unmodifiableList(params);
	}

	public void setParams(List<ElementInfo> params) {
		this.params.addAll(params);
	}
	
	public void addParam(ElementInfo param) {
		this.params.add(param);
	}

	@Override
	public String getExceptions() {
		return exceptions;
	}

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	@Override
	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	@Override
	public String toString() {
		return type + " " + name + "()";
	}

}
