package rnd.tool.code.gen.core.impl;

import rnd.tool.code.model.MethodInfo;
import rnd.tool.code.model.MethodInfo.HttpMethod;

public class ClassInfoRestGenerator extends ClassInfoClassGenerator {

	protected boolean hasParam(MethodInfo methodInfo) {
		HttpMethod httpMethod = methodInfo.getHttpMethod();
		boolean pathParam = HttpMethod.Get == httpMethod || HttpMethod.Delete == httpMethod;
		return pathParam;
	}

	protected boolean hasBody(MethodInfo methodInfo) {
		HttpMethod httpMethod = methodInfo.getHttpMethod();
		boolean pathParam = HttpMethod.Put == httpMethod || HttpMethod.Post == httpMethod;
		return pathParam;
	}

}