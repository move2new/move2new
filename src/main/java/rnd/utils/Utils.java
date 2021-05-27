package rnd.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {

	protected static Logger logger = LogManager.getLogger(Utils.class.getName());

	public static Properties loadProperties(String type) {
		Properties properties = new Properties();
		String config = type + ".properties";
		final InputStream stream = Utils.class.getClassLoader().getResourceAsStream(config);
		try {
			properties.load(stream);
			return properties;
		} catch (Exception e) {
			throw new RuntimeException("Configuration could not be loaded : " + config, e);
		}
	}

	public static Object newInstance(String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}