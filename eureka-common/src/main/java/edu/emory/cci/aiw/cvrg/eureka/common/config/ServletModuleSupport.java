package edu.emory.cci.aiw.cvrg.eureka.common.config;

/*
 * #%L
 * Eureka Common
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

import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew Post
 */
 class ServletModuleSupport {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractJerseyServletModuleWithPersist.class);
	private static final String CAS_CALLBACK_PATH = "/proxyCallback";
	private static final String ROLES_SQL = "select a.name as role from roles a, user_role b, users c where a.id=b.role_id and b.user_id=c.id and c.username=?";
	private static final String ROLE_COLUMN = "role";
	private static final String SERVICES_JNDI_NAME = "java:comp/env/jdbc/EurekaService";
	private final AbstractProperties properties;
	private final String contextPath;
	
	ServletModuleSupport(String contextPath,
			AbstractProperties inProperties) {
		this.properties = inProperties;
		this.contextPath = contextPath;
	}
	
	protected void printParams(Map<String, String> inParams) {
		for (Map.Entry<String, String> entry : inParams.entrySet()) {
			LOGGER.debug(entry.getKey() + " -> " + entry.getValue());
		}
	}
	
	Map<String, String> getRolesFilterInitParams() {
		Map<String, String> rolesFilterInitParams = new HashMap<>();
		rolesFilterInitParams.put("datasource", SERVICES_JNDI_NAME);
		rolesFilterInitParams.put("sql", ROLES_SQL);
		rolesFilterInitParams.put("rolecolumn", ROLE_COLUMN);
		if (LOGGER.isDebugEnabled()) {
			printParams(rolesFilterInitParams);
		}
		return rolesFilterInitParams;
	}

	Map<String, String> getCasProxyFilterInitParamsForWebApp() {
		Map<String, String> params = getCasProxyFilterInitParams();
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		return params;
	}
	
	Map<String, String> getCasProxyFilterInitParamsForWebService() {
		Map<String, String> params = getCasProxyFilterInitParams();
		params.put("redirectAfterValidation", "false");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		return params;
	}
	
	private Map<String, String> getCasProxyFilterInitParams() {
		Map<String, String> params = new HashMap<>();
		params.put("acceptAnyProxy", "true");
		params.put("proxyCallbackUrl", this.getProxyCallbackUrl());
		params.put("proxyReceptorUrl", this.getProxyReceptorUrl());
		params.put("casServerUrlPrefix", this.properties.getCasUrl());
		params.put("serverName", this.properties.getProxyCallbackServer());
		return params;
	}

	Map<String, String> getCasAuthenticationFilterInitParams() {
		Map<String, String> params = new HashMap<>();
		params.put("casServerLoginUrl", this.getCasLoginUrl());
		params.put("serverName", this.properties.getProxyCallbackServer());
		params.put("renew", "false");
		params.put("gateway", "false");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		return params;
	}

	Map<String, String> getServletRequestWrapperFilterInitParams() {
		Map<String, String> params = new HashMap<>();
		params.put("roleAttribute", "authorities");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
		return params;
	}

	private String getProxyCallbackUrl() {
		return this.properties.getProxyCallbackServer() + this.contextPath
				+ CAS_CALLBACK_PATH;
	}

	private String getProxyReceptorUrl() {
		return CAS_CALLBACK_PATH;
	}

	protected String getCasLoginUrl() {
		return this.properties.getCasLoginUrl();
	}
}
