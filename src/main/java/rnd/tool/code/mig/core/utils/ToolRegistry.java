package rnd.tool.code.mig.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import rnd.tool.code.mig.core.AbstractMigrator;
import rnd.utils.Utils;

public class ToolRegistry {

	private static Map<String, AbstractMigrator<?>> TOOLS = new HashMap<>();

	public static void loadTools(String type) {
		Properties tools = Utils.loadProperties(type);

		// Register Tools
		tools.forEach((toolName, toolClass) -> {
			TOOLS.put(toolName.toString(), (AbstractMigrator<?>) Utils.newInstance(toolClass.toString()));
		});
	}

	public static AbstractMigrator<?> getMigrator(String migName) {
		return TOOLS.get(migName);
	}

}