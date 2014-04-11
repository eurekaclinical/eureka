package edu.emory.cci.aiw.cvrg.eureka.common.config;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
			.getLogger(AbstractJerseyServletModule.class);
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

	Map<String, String> getCasProxyFilterInitParams() {
		Map<String, String> params = new HashMap<>();
		params.put("acceptAnyProxy", "true");
		params.put("proxyCallbackUrl", this.getProxyCallbackUrl());
		params.put("proxyReceptorUrl", this.getProxyReceptorUrl());
		params.put("casServerUrlPrefix", this.properties.getCasUrl());
		params.put("serverName", this.properties.getProxyCallbackServer());
		params.put("redirectAfterValidation", "false");
		if (LOGGER.isDebugEnabled()) {
			this.printParams(params);
		}
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
