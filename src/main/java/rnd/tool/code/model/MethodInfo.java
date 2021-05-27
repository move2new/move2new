package rnd.tool.code.model;

import java.util.List;

public interface MethodInfo {

	public static enum HttpMethod {
		Get, Put, Post, Delete
	}

	String getName();

	String getType();

	List<ElementInfo> getParams();

	String getExceptions();

	HttpMethod getHttpMethod();

	String getDefinition();
}
