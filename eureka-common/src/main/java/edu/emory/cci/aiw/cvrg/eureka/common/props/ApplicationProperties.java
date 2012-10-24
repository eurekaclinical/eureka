/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.common.props;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
	private static final String PROPERTY_NAME = "eureka.config.file";
	/**
	 * If the configuration file is not specified by the user, search
	 * this default location.
	 */
	private static final String DEFAULT_UNIX_LOCATION = "/etc/eureka";
	private static final String DEFAULT_WIN_LOCATION = "C:\\Program " +
		"Files\\eureka";
	/**
	 * Name of the properties file that is required for application
	 * configuration.
	 */
	private static final String PROPERTIES_FILE = "/application.properties";
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
		String userConfig = System.getProperty(PROPERTY_NAME);
		String defaultConfig = this.getDefaultLocation() + PROPERTIES_FILE;
		String fallbackConfig = this.getFallBackConfig();
		Properties temp = null;
		String[] files = {userConfig, defaultConfig, fallbackConfig};

		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			if (file != null) {
				LOGGER.info("Trying to load configuration from {}", file);
				try {
					temp = this.load(file);
					LOGGER.info("Successfully loaded configuration from {}",
						file);
				} catch (IOException e) {
					LOGGER.warn("Failed to load configuration from file {}",
						file, e);
				}
			}
			if(temp != null) {
				break;
			}
		}

		if (temp == null) {
			throw new RuntimeException("No application configuration found.");
		}
		this.properties = temp;
	}

	/**
	 * Gets the default location of configuration file,
	 * based on the operating system.
	 *
	 * @return A String containing the default configuration location.
	 */
	private String getDefaultLocation () {
		String os = System.getProperty("os.name");
		String path;
		if (os.toLowerCase().contains("windows")) {
			path = DEFAULT_WIN_LOCATION;
		} else {
			path = DEFAULT_UNIX_LOCATION;
		}
		return path;
	}

	/**
	 * Gets the location of the fallback configuration file,
	 * in case the user specified location and the default location do not
	 * contain a configuration file.
	 *
	 * @return The location of the fallback configuration,
	 * or null if the fallback configuration file can  not be found.
	 */
	private String getFallBackConfig () {
		String path = null;
		try {
			URL fileUrl = this.getClass().getResource(PROPERTIES_FILE);
			if (fileUrl != null) {
				URI fileUri = fileUrl.toURI();
				path = fileUri.getPath();
			}
		} catch (URISyntaxException e) {
			LOGGER.error("Could not location fallback configuration.", e);
		}
		return path;
	}

	/**
	 * Loads the application properties from the file named by the given file
	 * name. The filename should be an absolute path to the configuration file.
	 *
	 * @param inFileName The absolute path to the configuration file.
	 * @return Properties object containing the application properties.
	 * @throws IOException Thrown if the named filed can not be properly read.
	 */
	private Properties load(String inFileName) throws IOException {
		return load(new File(inFileName));
	}

	/**
	 * Loads the application properties from the given File object. The File
	 * object should point to a file with an absolute path.
	 *
	 * @param inFile The File object pointing to a configuration file.
	 * @return Properties object containing the application properties.
	 * location that does not exist.
	 * @throws IOException Thrown if the named file can not be properly read.
	 */
	private Properties load(File inFile) throws IOException {
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
	 * Gets the user-configured directory where the INI configuration files for
	 * Protempa are located.
	 *
	 * @return A string containing the path to the directory containing Protempa
	 * INI configuration files.
	 */
	public String getConfigDir() {
		return this.getValue("eureka.etl.config.dir");
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
	 * Reads in a property value as a whitespace-delimited list of items.
	 *
	 * @param inPropertyName The name of the property to read.
	 * @param defaultValue The value to return if the property is not found.
	 * @return A list containing the items in the value.
	 */
	protected List<String> getStringListValue(final String inPropertyName,
		List<String> defaultValue) {

		List<String> result;
		if (this.properties.containsKey(inPropertyName)) {
			String value = this.properties.getProperty(inPropertyName);
			String[] temp = value.split("\\s+");
			result = new ArrayList<String>(temp.length);
			for (String s : temp) {
				String trimmed = s.trim();
				if (trimmed.length() > 0) {
					result.add(s.trim());
				}
			}
		} else {
			LOGGER.warn("Property not found in configuration: {}", inPropertyName);
			result = defaultValue;
		}
		return result;
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
