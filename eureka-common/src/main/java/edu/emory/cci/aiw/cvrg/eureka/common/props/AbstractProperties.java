/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hrathod
 */
public abstract class AbstractProperties {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			AbstractProperties.class);
	/**
	 * Name of the system property containing a pathname to the configuration 
	 * file.
	 */
	private static final String PROPERTY_NAME = "eureka.config.file";
	
	/**
	 * If the configuration file is not specified by the user in the above
	 * system property, search this default location.
	 */
	private static final String DEFAULT_UNIX_LOCATION = "/etc/eureka/";
	private static final String DEFAULT_WIN_LOCATION = "C:\\Program "
			+ "Files\\eureka\\";
	
	/**
	 * The name of the properties file for application configuration.
	 */
	private static final String PROPERTIES_FILE = "application.properties";
	
	/**
	 * Fallback properties file for application configuration as a resource.
	 */
	private static final String FALLBACK_CONFIG_FILE = '/' + PROPERTIES_FILE;
	
	/**
	 * Holds an instance of the properties object which contains all the
	 * application configuration properties.
	 */
	private final Properties properties;
	
	/**
	 * Loads the application configuration.
	 *
	 * There are three potential sources of application configuration. The
	 * fallback configuration should always be there. The default configuration
	 * file is created by the application's administrator and overrides the
	 * fallback configuration for each configuration property that is specified.
	 * It is searched for in the
	 * <code>/etc/eureka</code> directory for Unix/Linux/Mac installations, and
	 * <code>C:\Program Files\eureka</code> for Windows installations. It is
	 * optional, though highly recommended. Finally, the pathname of a
	 * configuration file may be specified in the
	 * <code>eureka.config.file</code> system property, the property values in
	 * which override those in the default configuration above.
	 */
	public AbstractProperties() {
		String userConfig = System.getProperty(PROPERTY_NAME);
		String defaultConfig = getDefaultLocation() + PROPERTIES_FILE;
		
		Properties temp;
		try (InputStream fallbackConfig = this.getFallBackConfig();) {
			temp = this.load(fallbackConfig, null);
			LOGGER.info("Loaded fallback configuration from {}",
					FALLBACK_CONFIG_FILE);
		} catch (IOException ex) {
			throw new AssertionError(
					"Fallback configuration could not be read: "
							+ ex.getMessage());
		}

		LOGGER.info("Trying to load default configuration from {}",
				defaultConfig);
		try {
			temp = this.load(defaultConfig, temp);
			LOGGER.info("Successfully loaded configuration from {}",
					defaultConfig);
		} catch (FileNotFoundException ex) {
			if (userConfig != null) {
				LOGGER.info("No default configuration file found at {}.",
						defaultConfig);
			} else {
				LOGGER.warn("No default configuration file found at {}. "
						+ "Unless you specify a configuration file with the "
						+ "{} property, built-in defaults will be used, some "
						+ "of which are unlikely to be what you want.",
						defaultConfig, PROPERTY_NAME);
			}
		} catch (IOException ioe) {
			LOGGER.warn("Failed to load configuration from file {}: "
					+ "{}", defaultConfig, ioe.getMessage());
		}

		if (userConfig != null) {
			LOGGER.info("Trying to load user configuration from {}",
					userConfig);
			try {
				temp = this.load(userConfig, temp);
			} catch (IOException ex) {
				LOGGER.warn("Failed to load configuration from file {}: "
						+ "{}", userConfig, ex.getMessage());
			}
		} else {
			LOGGER.debug("User configuration property {} not specified",
					PROPERTY_NAME);
		}


		this.properties = temp;
	}
	
	/**
	 * Gets the default location of configuration file, based on the operating
	 * system.
	 *
	 * @return A String containing the default configuration location.
	 */
	private static String getDefaultLocation() {
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
	 * Gets the location of the fallback configuration file, in case the user
	 * specified location and the default location do not contain a
	 * configuration file.
	 *
	 * @return The location of the fallback configuration, guaranteed not null.
	 */
	private InputStream getFallBackConfig() {
		InputStream in = getClass().getResourceAsStream(FALLBACK_CONFIG_FILE);
		if (in == null) {
			throw new AssertionError(
					"Could not locate fallback configuration.");
		}
		return in;
	}

	/**
	 * Loads the application properties from the file named by the given file
	 * name. The filename should be an absolute path to the configuration file.
	 *
	 * @param inFileName The absolute path to the configuration file.
	 * @param defaults the default values for the properties.
	 * @return Properties object containing the application properties.
	 * @throws IOException Thrown if the named filed can not be properly read.
	 */
	private Properties load(String inFileName, Properties defaults) throws IOException {
		return load(new File(inFileName), defaults);
	}

	/**
	 * Loads the application properties from the given File object. The File
	 * object should point to a file with an absolute path.
	 *
	 * @param inFile The File object pointing to a configuration file.
	 * @param defaults the default values for the properties.
	 * @return Properties object containing the application properties. location
	 * that does not exist.
	 * @throws IOException Thrown if the named file can not be properly read.
	 */
	private Properties load(File inFile, Properties defaults) throws IOException {
		Properties props;
		try (InputStream inputStream = new FileInputStream(inFile);) {
			props = load(inputStream, defaults);
		}
		return props;
	}

	/**
	 * Loads the application properties from the given input stream.
	 *
	 * @param inStream InputStream containing the application configuration
	 * data.
	 * @param defaults the default values for the properties.
	 * @return Properties object containing the application properties.
	 * @throws IOException Thrown if the InputStream can not be properly read.
	 */
	private Properties load(InputStream inStream, Properties defaults) throws IOException {
		Properties props = new Properties(defaults);
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
	public final String getConfigDir() {
		return this.getValue("eureka.etl.config.dir", getDefaultLocation());
	}

	/**
	 * Get the base URL for the application front-end for external users.
	 *
	 * @param request the HTTP request, which will be used to generate a URL to
	 * the website if none of the properties files contain an application URL
	 * property.
	 *
	 * @return The base URL.
	 */
	public String getApplicationUrl(HttpServletRequest request) {
		String result = this.getValue("eureka.webapp.url");
		if (result == null) {
			result = PublicUrlGenerator.generate(request);
		}
		return result;
	}

	public abstract String getProxyCallbackServer ();
	
	public String getCasUrl() {
		return this.getValue("cas.url");
	}
	
	public String getCasLoginUrl() {
		UriBuilder builder = UriBuilder.fromUri(getCasUrl());
		builder.path(this.getValue("cas.url.login"));
		return builder.build().toString();
	}

	public String getCasLogoutUrl() {
		UriBuilder builder = UriBuilder.fromUri(getCasUrl());
		builder.path(this.getValue("cas.url.logout"));
		return builder.build().toString();
	}
	
	public String getMajorVersion () {
		return this.getValue("eureka.version.major");
	}

	public String getMinorVersion () {
		return this.getValue("eureka.version.minor");
	}
	public String getIncrementalVersion () {
		return this.getValue("eureka.version.incremental");
	}
	public String getQualifier () {
		return this.getValue("eureka.version.qualifier");
	}
	public String getBuildNumber () {
		return this.getValue("eureka.version.buildNumber");
	}
	
		/**
	 * Get the support email address for the application.
	 *
	 * @return The support email address.
	 */
	public SupportUri getSupportUri() {
		SupportUri supportUri = null;
		try {
			String uriStr = this.getValue("eureka.support.uri");
			String uriName = this.getValue("eureka.support.uri.name");
			if (uriStr != null) {
				supportUri = new SupportUri(new URI(uriStr), uriName);
			}
		} catch (URISyntaxException ex) {
			LOGGER.error("Invalid support URI in application.properties", ex);
		}
		return supportUri;
	}

	
	/**
	 * Returns the String value of the given property name.
	 *
	 * @param propertyName The property name to fetch the value for.
	 * @return A String containing the value of the given property name, or null
	 * if the property name does not exist.
	 */
	protected String getValue(final String propertyName) {
		/*
		 * Don't use this.properties.containsKey(). It doesn't work with the
		 * built-in default value mechanism. Also, the semantics of a
		 * Properties list are such that a null property value is the same
		 * as never specified in the list.
		 */
		return this.properties.getProperty(propertyName);
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
		String value = getValue(propertyName);
		if (value == null) {
			if (defaultValue == null) {
				LOGGER.warn(
						"Property '{}' is not specified in "
						+ getClass().getName()
						+ ", and no default is specified.", propertyName);
			}
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Reads in a property value as a whitespace-delimited list of items.
	 *
	 * @param inPropertyName The name of the property to read.
	 * @return A list containing the items in the value, or <code>null</code> if
	 * the property is not found.
	 */
	protected List<String> getStringListValue(final String inPropertyName) {

		List<String> result = null;
		String value = this.properties.getProperty(inPropertyName);
		if (value != null) {
			String[] temp = value.split("\\s+");
			result = new ArrayList<>(temp.length);
			for (String s : temp) {
				String trimmed = s.trim();
				if (trimmed.length() > 0) {
					result.add(s.trim());
				}
			}
		}
		return result;
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

		List<String> result = getStringListValue(inPropertyName);
		if (result == null) {
			LOGGER.warn("Property not found in configuration: {}",
					inPropertyName);
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
	
	/**
	 * Gets the default list of system propositions for the application.
	 *
	 * @return The default list of system propositions.
	 */
	public List<String> getDefaultSystemPropositions() {
		return this.getStringListValue("eureka.services.defaultprops",
				new ArrayList<String>());
	}
}
