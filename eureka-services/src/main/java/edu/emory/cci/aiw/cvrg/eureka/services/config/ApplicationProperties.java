package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

/**
 * Looks up the application properties file (eureka-services.properties) and
 * presents the values contained in the file to the rest of the application.
 * 
 * @author hrathod
 * 
 */
@Singleton
public class ApplicationProperties {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ApplicationProperties.class);
	/**
	 * Name of the properties file that is required for application
	 * configuration.
	 */
	private static final String PROPERTIES_FILE = "/eureka-services.properties";
	/**
	 * Holds an instance of the properties object which contains all the
	 * application configuration properties.
	 */
	private final Properties properties;

	/**
	 * No-arg constructor, looks for the application configuration file and
	 * loads up the properties.
	 */
	public ApplicationProperties() {
		InputStream inputStream = this.getClass().getResourceAsStream(
				PROPERTIES_FILE);

		if (inputStream == null) {
			throw new RuntimeException(
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
			// do nothing
		}
	}

	/**
	 * Get the URL to access the back-end's configuration information for a
	 * user.
	 * 
	 * @return A string containing the back-end's configuration end-point URL.
	 */
	public String getEtlConfGetUrl() {
		return this.properties.getProperty("eureka.services.etl.conf.get.url");
	}

	/**
	 * Get the URL to submit a new configuration item to the ETL backend
	 * service.
	 * 
	 * @return A string containing the back-end's configuration submission URL.
	 */
	public String getEtlConfSubmitUrl() {
		return this.properties
				.getProperty("eureka.services.etl.conf.submit.url");
	}

	/**
	 * Get the URL to access the back-end's job submission end-point.
	 * 
	 * @return A string containing the back-end's job submission end-point.
	 */
	public String getEtlJobSubmitUrl() {
		return this.properties
				.getProperty("eureka.services.etl.job.submit.url");
	}

	/**
	 * Get the URL to access the back-end's job status update information
	 * end-point.
	 * 
	 * @return A string containing the back-end's job status update information
	 *         end-point.
	 */
	public String getEtlJobUpdateUrl() {
		return this.properties
				.getProperty("eureka.services.etl.job.update.url");
	}

	/**
	 * Get the size of the job executor thread pool.
	 * 
	 * @return The size of the job executor thread pool from the configuration
	 *         file, or 5 as the default if no value can be determined.
	 */
	public int getJobPoolSize() {
		return this.getIntValue("eureka.services.jobpool.size", 5);
	}

	/**
	 * Get the number of hours to keep a user registration without verification,
	 * before deleting it from the database.
	 * 
	 * @return The number of hours listed in the configuration, and 24 if the
	 *         configuration is not found.
	 */
	public int getRegistrationTimeout() {
		return this.getIntValue("eureka.services.registration.timeout", 24);
	}

	/**
	 * Get the verification base URL, to be used in sending a verification email
	 * to the user.
	 * 
	 * @return The verification base URL, as found in the application
	 *         configuration file.
	 */
	public String getVerificationUrl() {
		return this.properties.getProperty("eureka.services.email.verify.url");
	}

	/**
	 * Get the verification email subject line.
	 * 
	 * @return The subject for the verification email.
	 */
	public String getVerificationEmailSubject() {
		return this.properties
				.getProperty("eureka.services.email.verify.subject");
	}

	/**
	 * Get the activation email subject line.
	 * 
	 * @return The subject for the activation email.
	 */
	public String getActivationEmailSubject() {
		return this.properties
				.getProperty("eureka.services.email.activation.subject");
	}

	/**
	 * Get the base URL for the application front-end.
	 * 
	 * @return The base URL.
	 */
	public String getApplicationUrl() {
		return this.properties.getProperty("eureka.services.url");
	}

	/**
	 * Get the support email address for the application.
	 * 
	 * @return The support email address.
	 */
	public String getSupportEmail() {
		return this.properties.getProperty("eureka.services.support.email");
	}

	/**
	 * Get the password change email subject line.
	 * 
	 * @return The email subject line.
	 */
	public String getPasswordChangeEmailSubject() {
		return this.properties
				.getProperty("eureka.services.email.password.subject");
	}

	/**
	 * Utility method to get an int from the properties file.
	 * 
	 * @param propertyName The name of the property.
	 * @param defaultValue The default value to return, if the property is not
	 *            found, or is malformed.
	 * @return The property value, as an int.
	 */
	public int getIntValue(final String propertyName, int defaultValue) {
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