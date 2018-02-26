/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppPropertiesLinks;
import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppPropertiesModes;
import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppPropertiesRegistration;
import edu.emory.cci.aiw.cvrg.eureka.services.props.PublicUrlGenerator;
import edu.emory.cci.aiw.cvrg.eureka.services.props.SupportUri;

import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Singleton;
import org.eurekaclinical.standardapis.props.CasJerseyEurekaClinicalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Looks up the application properties file (application.properties) and
 * presents the values contained in the file to the rest of the application.
 *
 * @author hrathod
 *
 */
@Singleton
public class ServiceProperties extends CasJerseyEurekaClinicalProperties {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProperties.class);

	private AppPropertiesModes appPropertiesModes;
	private AppPropertiesLinks appPropertiesLinks;
	private AppPropertiesRegistration appPropertiesRegistration;
	private AppProperties appProperties;
	
	public ServiceProperties() {
		super("/etc/eureka");
	}
        
	/**
	 * Gets the appPropertiesModes.
	 *
	 * @return appPropertiesModes.
	 */
	public AppPropertiesModes getAppPropertiesModes() {
		this.appPropertiesModes = new AppPropertiesModes();
		this.appPropertiesModes.setDemoMode(Boolean.parseBoolean(this.getValue("eureka.common.demomode")));
		this.appPropertiesModes.setEphiProhibited(Boolean.parseBoolean(this.getValue("eureka.common.ephiprohibited")));
		return this.appPropertiesModes;
	}

	/**
	 * Gets the appPropertiesLinks.
	 *
	 * @return appPropertiesLinks.
	 */
	public AppPropertiesLinks getAppPropertiesLinks() {
		this.appPropertiesLinks = new AppPropertiesLinks();
		this.appPropertiesLinks.setSupportUri(this.getSupportUri());
		this.appPropertiesLinks.setOrganizationName(this.getValue("project.organization.name"));
		return this.appPropertiesLinks;
	}

	/**
	 * Gets the appPropertiesRegistration.
	 *
	 * @return appPropertiesRegistration.
	 */
	public AppPropertiesRegistration getAppPropertiesRegistration() {
		this.appPropertiesRegistration = new AppPropertiesRegistration();
		this.appPropertiesRegistration.setRegistrationEnabled(Boolean.parseBoolean(this.getValue("eureka.webapp.registrationenabled")));
		return this.appPropertiesRegistration;
	}

	/**
	 * Gets the appProperties.
	 *
	 * @return appProperties.
	 */
	public AppProperties getAppProperties() {
		this.appProperties = new AppProperties();
		this.appProperties.setAppPropertiesModes(this.getAppPropertiesModes());
		this.appProperties.setAppPropertiesLinks(this.getAppPropertiesLinks());
		this.appProperties.setAppPropertiesRegistration(this.getAppPropertiesRegistration());                
		return this.appProperties;
	}
	
	/**
     * Get the base URL for the application for external users. Always
     * ends with a slash ("/").
     *
     * @return The base URL.
     */
	public String getApplicationUrl() {
		String result = this.getValue("eureka.webapp.url");
		if (result.endsWith("/")) {
			return result;
		} else {
			return result + "/";
		}
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
	 * Gets the default list of system propositions for the application.
	 *
	 * @return The default list of system propositions.
	 */
	public List<String> getDefaultSystemPropositions() {
		return this.getStringListValue("eureka.services.defaultprops",
				new ArrayList<>());
	}
        
	public String getGoogleOAuthKey() {
		return this.getValue("eureka.webapp.googleoauthkey");
	}

	public String getGoogleOAuthSecret() {
		return this.getValue("eureka.webapp.googleoauthsecret");
	}

	public String getGitHubOAuthKey() {
		return this.getValue("eureka.webapp.githuboauthkey");
	}

	public String getGitHubOAuthSecret() {
		return this.getValue("eureka.webapp.githuboauthsecret");
	}

	public String getTwitterOAuthKey() {
		return this.getValue("eureka.webapp.twitteroauthkey");
	}

	public String getTwitterOAuthSecret() {
		return this.getValue("eureka.webapp.twitteroauthsecret");
	}

	public String getGlobusOAuthKey() {
		return this.getValue("eureka.webapp.globusoauthkey");
	}

	public String getGlobusOAuthSecret() {
		return this.getValue("eureka.webapp.globusoauthsecret");
	}

	/**
	 * Sets the appPropertiesModes.
	 *
	 * @param inAppPropertiesModes.
	 */
	public void setAppPropertiesModes(AppPropertiesModes inAppPropertiesModes) {
		this.appPropertiesModes = inAppPropertiesModes;
	}

	/**
	 * Sets the appPropertiesLinks.
	 *
	 * @param inAppPropertiesLinks.
	 */
	public void setAppPropertiesLinks(AppPropertiesLinks inAppPropertiesLinks) {
		this.appPropertiesLinks = inAppPropertiesLinks;
	}

	/**
	 * Sets the appPropertiesRegistration.
	 *
	 * @param inAppPropertiesRegistration.
	 */
	public void setAppPropertiesRegistration(AppPropertiesRegistration inAppPropertiesRegistration) {
		this.appPropertiesRegistration = inAppPropertiesRegistration;
	}

	/**
	 * Get the URL for the eureka-protempa-etl application.
	 *
	 * @return A string containing the base URL for the ETL layer.
	 */
	public String getEtlUrl() {
		return this.getValue("eureka.etl.url");
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
	 * Gets the i2b2 admin username from application.properties.
	 *
	 * @return admin username.
	 */
	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eureka.services.callbackserver");
	}   
	
	@Override
	public String getUrl() {
		return this.getValue("eureka.services.url");
	}

}
