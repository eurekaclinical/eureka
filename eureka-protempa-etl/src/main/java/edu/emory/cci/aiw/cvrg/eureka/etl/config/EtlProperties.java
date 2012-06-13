package edu.emory.cci.aiw.cvrg.eureka.etl.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains methods to fetch configuration information for the application.
 *
 * @author hrathod
 *
 */
public class EtlProperties {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EtlProperties.class);
	/**
	 * Name of the properties file that is required for application
	 * configuration.
	 */
	private static final String PROPERTIES_FILE = "/eureka-protempa-etl.properties";
	/**
	 * Holds an instance of the properties object which contains all the
	 * application configuration properties.
	 */
	private final Properties properties;

	/**
	 * No-arg constructor, looks for the application configuration file and
	 * loads up the properties.
	 */
	public EtlProperties() {
		InputStream inputStream = this.getClass().getResourceAsStream(
				PROPERTIES_FILE);

		if (inputStream == null) {
			throw new ConfigurationError(
					"Missing application configuration file: "
					+ PROPERTIES_FILE);
		}

		this.properties = new Properties();
		try {
			this.properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			inputStream.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the user-configured directory where the INI configuration files for
	 * Protempa are located.
	 *
	 * @return A string containing the path to the directory containing Protempa
	 * INI configuration files.
	 */
	public String getProtempaConfigDir() {
		return this.properties.getProperty("eureka.etl.protempa.config.dir");
	}

	/**
	 * Gets the user-configured directory where the i2b2 loader configuration
	 * XML files are located.
	 *
	 * @return A string containing the path to the directory containing i2b2
	 * loader XML configuration files.
	 */
	public String getI2b2ConfigDir() {
		return this.properties.getProperty("eureka.etl.i2b2.config.dir");
	}

	/**
	 * Gets the size of the thread pool created to run Protempa tasks.
	 *
	 * @return The size of the thread pool.
	 */
	public int getTaskThreadPoolSize() {
		return this.getIntValue("eureka.etl.threadpool.size", 4);
	}

	/**
	 * Utility method to get an int from the properties file.
	 *
	 * @param propertyName The name of the property.
	 * @param defaultValue The default value to return, if the property is not
	 * found, or is malformed.
	 * @return The property value, as an int.
	 */
	private int getIntValue(final String propertyName, int defaultValue) {
		int result;
		String property = this.properties.getProperty(propertyName);
		try {
			result = Integer.parseInt(property);
		} catch (NumberFormatException e) {
			LOGGER.warn("Invalid integer property in configuration: {}",
					propertyName);
			result = defaultValue;
		}
		return result;
	}
}
