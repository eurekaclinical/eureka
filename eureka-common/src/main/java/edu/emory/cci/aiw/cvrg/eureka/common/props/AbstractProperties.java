/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.props;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eurekaclinical.standardapis.props.EurekaClinicalProperties;

/**
 *
 * @author hrathod
 */
public abstract class AbstractProperties extends EurekaClinicalProperties {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			AbstractProperties.class);

	/**
	 * Loads the application configuration.
	 *
	 * There are two potential sources of application configuration. The
	 * fallback configuration should always be there. The default configuration
	 * directory, <code>/etc/eureka</code>, may optionally have an
	 * application.properties file within it that overrides the fallback
	 * configuration for each configuration property that is specified. The
	 * <code>eureka.config.dir</code> system property allows specifying an
	 * alternative configuration directory.
	 */
	public AbstractProperties() {
		super("/etc/eureka");
	}


	/**
	 * Get the base URL for the application front-end for external users. Always
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

	public String getMajorVersion() {
		return this.getValue("eureka.version.major");
	}

	public String getMinorVersion() {
		return this.getValue("eureka.version.minor");
	}

	public String getIncrementalVersion() {
		return this.getValue("eureka.version.incremental");
	}

	public String getQualifier() {
		return this.getValue("eureka.version.qualifier");
	}

	public String getBuildNumber() {
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
	 * Gets the default list of system propositions for the application.
	 *
	 * @return The default list of system propositions.
	 */
	public List<String> getDefaultSystemPropositions() {
		return this.getStringListValue("eureka.services.defaultprops",
				new ArrayList<>());
	}

}

