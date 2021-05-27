package rnd.tool.code.parse.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rnd.tool.code.model.ClassInfoImpl;
import rnd.tool.code.model.MethodInfo;
import rnd.tool.code.model.MethodInfo.HttpMethod;
import rnd.tool.code.model.MethodInfoImpl;

public class HttpMethodMapper {

	private static Pattern HTTP_GET_WORDS = Pattern.compile("[Gg]et|[Rr]ead|[Ff]etch|[Ll]ist");
	private static Pattern HTTP_PUT_WORDS = Pattern.compile("[Pp]ut|[Ss]et|[Uu]pd|[Mm]od");
	private static Pattern HTTP_DELETE_WORDS = Pattern.compile("[Dd]el|[Rr]em");
	private static Pattern HTTP_POST_WORDS = Pattern.compile("[Pp]ost|[Aa]dd|[Cc]reate");

	private static Pattern RESOURCE = Pattern.compile("[a-z]+|([A-Z][a-z]+)*");

	// This method will iterate through Class, Method and Parameter definitions
	// and assign respective resources to them
	public static void map(ClassInfoImpl clazz) {

		// Don't break up class name
		if (clazz.getName() != null) {
			for (MethodInfo m : clazz.getMethods()) {
				MethodInfoImpl method = (MethodInfoImpl) m;
				if (method.getName() != null) {
					// Parse the method name
					method.setHttpMethod(map(method.getName()));
				}
			}
		}
	}

	public static HttpMethod map(String name) {

		// Set default HTTP method as GET
		HttpMethod httpMethod = HttpMethod.Get;

		Matcher resourceMatcher = RESOURCE.matcher(name);
		Matcher httpMethodMatcher;
		boolean foundHttpMethod = false;

		while (resourceMatcher.find()) {
			if (!resourceMatcher.group().equals("")) {
				if (!foundHttpMethod) {
					httpMethodMatcher = HTTP_GET_WORDS.matcher(resourceMatcher.group());
					if (httpMethodMatcher.find() && !httpMethodMatcher.group().equals("")) {
						httpMethod = HttpMethod.Get;
						foundHttpMethod = true;
						continue;
					}
					httpMethodMatcher.usePattern(HTTP_PUT_WORDS);
					if (httpMethodMatcher.find() && !httpMethodMatcher.group().equals("")) {
						httpMethod = HttpMethod.Put;
						foundHttpMethod = true;
						continue;
					}
					httpMethodMatcher.usePattern(HTTP_POST_WORDS);
					if (httpMethodMatcher.find() && !httpMethodMatcher.group().equals("")) {
						httpMethod = HttpMethod.Post;
						foundHttpMethod = true;
						continue;
					}
					httpMethodMatcher.usePattern(HTTP_DELETE_WORDS);
					if (httpMethodMatcher.find() && !httpMethodMatcher.group().equals("")) {
						httpMethod = HttpMethod.Delete;
						foundHttpMethod = true;
						continue;
					}
				}
			}
		}

		return httpMethod;
	}

}
