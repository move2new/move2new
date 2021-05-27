package rnd.tool.code.process.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import rnd.utils.Utils;

public class MappingProvider {

	private static Map<String, Properties> MAPPINGS = new LinkedHashMap<String, Properties>();

	public static Properties getMapping(String type) {
		Properties mapping = MAPPINGS.get(type);
		if (mapping == null) {
			mapping = Utils.loadProperties("mapping/" + type);
			MAPPINGS.put(type, mapping);
		}
		return mapping;
	}

}