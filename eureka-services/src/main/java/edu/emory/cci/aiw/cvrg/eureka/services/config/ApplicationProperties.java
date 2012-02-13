package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
}
