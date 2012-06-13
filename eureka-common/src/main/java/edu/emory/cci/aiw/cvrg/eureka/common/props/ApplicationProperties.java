package edu.emory.cci.aiw.cvrg.eureka.common.props;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hrathod
 */
public abstract class ApplicationProperties {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			ApplicationProperties.class);
	/**
	 * Name of the system property that points to the configuration file.
	 */
	private static final String PROPERTY_NAME = "eureka.config";
	/**
	 * Name of the properties file that is required for application
	 * configuration.
	 */
	private static final String PROPERTIES_FILE = "/eureka.properties";
	/**
	 * Holds an instance of the properties object which contains all the
	 * application configuration properties.
	 */
	private final Properties properties;

	/**
	 * Loads the application configuration. First test to see if the system
	 * property for a configuration file is set, and use the value of that
	 * property. If the system property is not null and the named file can not
	 * be loaded, reports and error. If the system property is null, attempts to
	 * load a default configuration from the class-path.
	 */
	public ApplicationProperties() {
		String configFile = System.getProperty(PROPERTY_NAME);
		Properties temp = null;
		try {
			if (configFile != null) {
				temp = this.load(configFile);
			} else {
				try {
					temp = this.loadDefault();
				} catch (URISyntaxException ex) {
					LOGGER.error(ex.getMessage(), ex);
					throw new RuntimeException(
							"Could not load default application configuration file");
				}
				if (temp == null) {
					throw new RuntimeException(
							"Could not load default application configuration file");
				}
			}
		} catch (FileNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new RuntimeException(
					"Could not load configuration file " + configFile, ex);
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new RuntimeException(
					"Could not load configuration file " + configFile, ex);
		}
		this.properties = temp;
	}

	/**
	 * Loads the application configuration file from the default location.
	 *
	 * @return Properties object containing application properties.
	 * @throws IOException Thrown when the file can not be properly read.
	 * @throws URISyntaxException Thrown if the URL returned by the
	 * getClass().getResource() call can not be properly converted to a URI.F
	 */
	private Properties loadDefault() throws IOException, URISyntaxException {
		URL fileUrl = this.getClass().getResource(PROPERTIES_FILE);
		URI fileUri = fileUrl.toURI();
		return this.load(fileUri.getPath());
	}

	/**
	 * Loads the application properties from the file named by the given file
	 * name. The filename should be an absolute path to the configuration file.
	 *
	 * @param inFileName The absolute path to the configuration file.
	 * @return Properties object containing the application properties.
	 * @throws FileNotFoundException Thrown if the named file can not be found.
	 * @throws IOException Thrown if the named filed can not be properly read.
	 */
	private Properties load(String inFileName) throws FileNotFoundException,
			IOException {
		return load(new File(inFileName));
	}

	/**
	 * Loads the application properties from the given File object. The File
	 * object should point to a file with an absolute path.
	 *
	 * @param inFile The File object pointing to a configuration file.
	 * @return Properties object containing the application properties.
	 * @throws FileNotFoundException Thrown if the given File points to a
	 * location that does not exist.
	 * @throws IOException Thrown if the named file can not be properly read.
	 */
	private Properties load(File inFile) throws FileNotFoundException,
			IOException {
		InputStream inputStream = new FileInputStream(inFile);
		Properties props;
		try {
			props = load(inputStream);
		} finally {
			try {
				inputStream.close();
			} catch (IOException ioe) {
				// do nothing here
			}
		}
		return props;
	}

	/**
	 * Loads the application properties from the given input stream.
	 *
	 * @param inStream InputStream containing the application configuration
	 * data.
	 * @return Properties object containing the application properties.
	 * @throws IOException Thrown if the InputStream can not be properly read.
	 */
	private Properties load(InputStream inStream) throws IOException {
		Properties props = new Properties();
		props.load(inStream);
		return props;
	}

	/**
	 * Returns the String value of the given property name.
	 *
	 * @param propertyName The property name to fetch the value for.
	 * @return A String containing the value of the given property name, or null
	 * if the property name does not exist.
	 */
	protected String getValue(final String propertyName) {
		return this.getValue(propertyName, null);
	}

	/**
	 * Returns the String value of the given property name, or the given default
	 * if the given property name does not exist.
	 *
	 * @param propertyName The name of the property to fetch a value for.
	 * @param defaultValue The default value to return if the property name does
	 * not exist.
	 * @return A string containing either the value of the property, or the
	 * default value.
	 */
	protected String getValue(final String propertyName, String defaultValue) {
		String value;
		if (this.properties.containsKey(propertyName)) {
			value = this.properties.getProperty(propertyName);
		} else {
			LOGGER.warn("Property not found in configuration: {}", propertyName);
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Utility method to get an int from the properties file.
	 *
	 * @param propertyName The name of the property.
	 * @param defaultValue The default value to return, if the property is not
	 * found, or is malformed.
	 * @return The property value, as an int.
	 */
	protected int getIntValue(final String propertyName, int defaultValue) {
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
