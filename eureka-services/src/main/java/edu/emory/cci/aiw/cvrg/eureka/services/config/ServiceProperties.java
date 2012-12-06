/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.props.ApplicationProperties;

/**
 * Looks up the application properties file (eureka-services.properties) and
 * presents the values contained in the file to the rest of the application.
 *
 * @author hrathod
 *
 */
@Singleton
public class ServiceProperties extends ApplicationProperties {

	/**
	 * Get the URL to access the back-end's configuration information for a
	 * user.
	 *
	 * @return A string containing the back-end's configuration end-point URL.
	 */
	public String getEtlConfGetUrl() {
		return this.getValue("eureka.services.etl.conf.get.url");
	}

	/**
	 * Get the URL for the eureka-protempa-etl application.
	 *
	 * @return A string containing the base URL for the ETL layer.
	 */
	public String getEtlUrl () {
		return this.getValue("eureka.etl.url");
	}

	/**
	 * Get the URL to submit a new configuration item to the ETL backend
	 * service.
	 *
	 * @return A string containing the back-end's configuration submission URL.
	 */
	public String getEtlConfSubmitUrl() {
		return this.getValue("eureka.services.etl.conf.submit.url");
	}

	/**
	 * Get the URL to access the back-end's job submission end-point.
	 *
	 * @return A string containing the back-end's job submission end-point.
	 */
	public String getEtlJobSubmitUrl() {
		return this.getValue("eureka.services.etl.job.submit.url");
	}

	/**
	 * Get the URL to access the back-end's job status update information
	 * end-point.
	 *
	 * @return A string containing the back-end's job status update information
	 * end-point.
	 */
	public String getEtlJobUpdateUrl() {
		return this.getValue("eureka.services.etl.job.update.url");
	}

	/**
	 * Gets the URL to access the back-end's proposition retrieval end-point.
	 *
	 * @return A string containing the back-end's proposition retrieval
	 * end-point.
	 */
	public String getEtlPropositionGetUrl() {
		return this.getValue("eureka.services.etl.proposition.get.url");
	}

	/**
	 * Gets the URL to access the validation end-point for the ETL layer.
	 *
	 * @return A string representing the URL of the validation end-point.
	 */
	public String getEtlPropositionValidationUrl() {
		return this.getValue("eureka.services.etl.proposition.validation.url");
	}

	/**
	 * Get the size of the job executor thread pool.
	 *
	 * @return The size of the job executor thread pool from the configuration
	 * file, or 5 as the default if no value can be determined.
	 */
	public int getJobPoolSize() {
		return this.getIntValue("eureka.services.jobpool.size", 5);
	}

	/**
	 * Get the number of hours to keep a user registration without verification,
	 * before deleting it from the database.
	 *
	 * @return The number of hours listed in the configuration, and 24 if the
	 * configuration is not found.
	 */
	public int getRegistrationTimeout() {
		return this.getIntValue("eureka.services.registration.timeout", 24);
	}

	/**
	 * Get the verification base URL, to be used in sending a verification email
	 * to the user.
	 *
	 * @return The verification base URL, as found in the application
	 * configuration file.
	 */
	public String getVerificationUrl() {
		return this.getValue("eureka.services.email.verify.url");
	}

	/**
	 * Get the verification email subject line.
	 *
	 * @return The subject for the verification email.
	 */
	public String getVerificationEmailSubject() {
		return this.getValue(
				"eureka.services.email.verify.subject");
	}

	/**
	 * Get the activation email subject line.
	 *
	 * @return The subject for the activation email.
	 */
	public String getActivationEmailSubject() {
		return this.getValue(
				"eureka.services.email.activation.subject");
	}

	/**
	 * Get the base URL for the application front-end.
	 *
	 * @return The base URL.
	 */
	public String getApplicationUrl() {
		return this.getValue("eureka.services.url");
	}

	/**
	 * Get the support email address for the application.
	 *
	 * @return The support email address.
	 */
	public String getSupportEmail() {
		return this.getValue("eureka.services.support.email");
	}

	/**
	 * Get the password change email subject line.
	 *
	 * @return The email subject line.
	 */
	public String getPasswordChangeEmailSubject() {
		return this.getValue(
				"eureka.services.email.password.subject");
	}

	/**
	 * Gets the subject line for a password reset email.
	 *
	 * @return The subject line.
	 */
	public String getPasswordResetEmailSubject() {
		return this.getValue("eureka.services.email.reset.subject");
	}

	/**
	 * Gets the default list of system propositions for the application.
	 *
	 * @return The default list of system propositions.
	 */
	public List<String> getDefaultSystemPropositions () {
		return this.getStringListValue("eureka.services.defaultprops",
			new ArrayList<String>());
	}
}
