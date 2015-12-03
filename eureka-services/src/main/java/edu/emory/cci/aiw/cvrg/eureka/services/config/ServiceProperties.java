/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.services.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;
import edu.emory.cci.aiw.cvrg.eureka.common.props.PublicUrlGenerator;

/**
 * Looks up the application properties file (application.properties) and
 * presents the values contained in the file to the rest of the application.
 *
 * @author hrathod
 *
 */
@Singleton
public class ServiceProperties extends AbstractProperties {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			ServiceProperties.class);

	/**
	 * Get the URL for the eureka-protempa-etl application.
	 *
	 * @return A string containing the base URL for the ETL layer.
	 */
	public String getEtlUrl() {
		return this.getValue("eureka.etl.url");
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
	 * Get email address in the From header.
	 * 
	 * @return an email address.
	 */
	public String getFromEmailAddress() {
		return this.getValue("eureka.services.email.from");
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
	 * Get the base URL for the services layer for external users.
	 * 
	 * @param request the HTTP request, which will be used to generate a
	 * services layer URL from server information if none of the properties 
	 * files contain an services layer URL property.
	 *
	 * @return The base URL.
	 */
	public String getServicesUrl(HttpServletRequest request) {
		String servicesUrl = this.getValue("eureka.services.url");
		if (servicesUrl == null) {
			servicesUrl = PublicUrlGenerator.generate(request);
			servicesUrl += (servicesUrl.endsWith("/") ? "" : "/") + "eureka-services";
		}
		return servicesUrl;
	}

	/**
	 * Get the verification base URL, to be used in sending a verification email
	 * to the user.
	 * 
	 * @return The verification base URL, as found in the application
	 * configuration file.
	 */
	public String getVerificationUrl() {
		String verUrl = getApplicationUrl();
		return verUrl + (verUrl.endsWith("/") ? "" : "/") + "verify?code=";
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
	@Override
	public List<String> getDefaultSystemPropositions() {
		return this.getStringListValue("eureka.services.defaultprops",
				new ArrayList<String>());
	}

	/**
	 * Gets the i2b2 admin username from application.properties.
	 *
	 * @return admin username.
	 */
	public String getI2b2AdminUser() {
		return this.getValue("eureka.services.i2b2.adminuser");
	}

	/**
	 * Gets the i2b2 admin password from application.properties.
	 *
	 * @return admin password
	 */
	public String getI2b2AdminPassword() {
		return this.getValue("eureka.services.i2b2.adminpassword");
	}

	/**
	 * Gets the i2b2 rest URL from application.properties.
	 *
	 * @return URL.
	 */
	public String getI2b2URL() {
		return this.getValue("eureka.services.i2b2.url");
	}

	/**
	 * Gets the i2b2 domain from application.properties.
	 *
	 * @return domain name.
	 */
	public String getI2b2Domain() {
		return this.getValue("eureka.services.i2b2.domain");
	}

	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eureka.services.callbackserver");
	}
}
