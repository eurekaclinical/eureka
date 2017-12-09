package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

import java.util.List;
import org.eurekaclinical.standardapis.props.CasEurekaClinicalProperties;

/*
 * #%L
 * Eureka WebApp
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
/**
 *
 * @author Andrew Post
 */
public class WebappProperties extends CasEurekaClinicalProperties {

	public WebappProperties() {
		super("/etc/eureka");
	}

	public String getMajorVersion() {
		return this.getValue("eureka.webapp.version.major");
	}

	public String getMinorVersion() {
		return this.getValue("eureka.webapp.version.minor");
	}

	public String getIncrementalVersion() {
		return this.getValue("eureka.webapp.version.incremental");
	}

	public String getQualifier() {
		return this.getValue("eureka.webapp.version.qualifier");
	}

	public String getBuildNumber() {
		return this.getValue("eureka.webapp.version.buildNumber");
	}

	@Override
	public String getUrl() {
		return this.getValue("eureka.webapp.url");
	}

	public String getUserWebappUrl() {
		return this.getValue("eurekaclinical.userwebapp.url");
	}

	public String getUserServiceUrl() {
		return this.getValue("eurekaclinical.userservice.url");
	}

	public String getRegistryServiceUrl() {
		return getValue("eurekaclinical.registryservice.url");
	}

	public boolean isEphiProhibited() {
		return Boolean.parseBoolean(getValue("eureka.webapp.ephiprohibited"));
	}

	public boolean isDemoMode() {
		return Boolean.parseBoolean(getValue("eureka.webapp.demomode"));
	}

	public String getServiceUrl() {
		return this.getValue("eureka.services.url");
	}

	/**
	 * Get the URL for the eureka-protempa-etl application.
	 *
	 * @return A string containing the base URL for the ETL layer.
	 */
	public String getEtlUrl() {
		return this.getValue("eureka.etl.url");
	}

	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eureka.webapp.callbackserver");
	}

	public String getContactEmail() {
		return this.getValue("eureka.webapp.contactemail");
	}

	@Override
	public List<String> getAllowedWebClientUrls() {
		return getStringListValue("eureka.webapp.allowedwebclients");
	}
	
}
