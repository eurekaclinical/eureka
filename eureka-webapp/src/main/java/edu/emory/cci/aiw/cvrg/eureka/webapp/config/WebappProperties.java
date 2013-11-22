package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

/*
 * #%L
 * Eureka WebApp
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

import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;

/**
 *
 * @author Andrew Post
 */
public class WebappProperties extends AbstractProperties {

	public boolean isEphiProhibited() {
		return Boolean.parseBoolean(getValue("eureka.webapp.ephiprohibited"));
	}
	public boolean isDemoMode() {
		return Boolean.parseBoolean(getValue("eureka.webapp.demomode"));
	}

	public String getUploadDir() {
		return this.getValue("eureka.webapp.uploaddir");
	}

	public String getServiceUrl() {
		return this.getValue("eureka.services.url");
	}

	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eureka.webapp.callbackserver");
	}

}
