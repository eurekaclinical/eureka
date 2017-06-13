package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

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
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.servlet.*;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.UserFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.MessagesFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.filter.RolesFilter;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.*;
import java.util.HashMap;
import java.util.Map;
import org.eurekaclinical.common.config.AbstractServletModule;
import org.eurekaclinical.common.servlet.DestroySessionServlet;
import org.eurekaclinical.common.servlet.LogoutServlet;
import org.eurekaclinical.common.servlet.ProxyServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hrathod
 */
class ServletModule extends AbstractServletModule {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServletModule.class);
	private static final String FILTER_PATH = "^/(?!(assets|bower_components)).*"; 
	private static final String LOGOUT_PATH = "/logout";

	private final WebappProperties properties;

	public ServletModule(WebappProperties inProperties) {
		super(inProperties);
		this.properties = inProperties;
	}

	private void setupMessageFilter() {
		filterRegex(FILTER_PATH).through(MessagesFilter.class);
	}

	private void setupUserFilter() {
		filterRegex(FILTER_PATH).through(UserFilter.class);
	}
	
	private void setupRolesFilter() {
		filterRegex(FILTER_PATH).through(RolesFilter.class);
	}

	@Override
	protected void setupFilters() {
		this.setupMessageFilter();
		this.setupUserFilter();
		this.setupRolesFilter();
	}

	@Override
	protected void setupServlets() {

		serve("/proxy-resource/*").with(ProxyServlet.class);
		
		bind(SessionPropertiesServlet.class).in(Singleton.class);
		serve("/protected/get-session-properties").with(SessionPropertiesServlet.class);
		
		serve("/destroy-session").with(DestroySessionServlet.class);  

		serve(LOGOUT_PATH).with(LogoutServlet.class);

		serve("/protected/login").with(LoginServlet.class);

		bind(JobStatsServlet.class).in(Singleton.class);
		serve("/protected/jobstats").with(JobStatsServlet.class);

		bind(JobPatientCountsServlet.class).in(Singleton.class);
		serve("/protected/jobpatcounts").with(JobPatientCountsServlet.class);

		bind(EditorHomeServlet.class).in(Singleton.class);
		serve("/protected/editorhome").with(EditorHomeServlet.class);

		bind(SystemPropositionListServlet.class).in(Singleton.class);
		serve("/protected/systemlist").with(
				SystemPropositionListServlet.class);

		bind(SavePropositionServlet.class).in(Singleton.class);
		serve("/protected/saveprop").with(SavePropositionServlet.class);

		bind(DeletePropositionServlet.class).in(Singleton.class);
		serve("/protected/deleteprop").with(DeletePropositionServlet.class);

		bind(UserPropositionListServlet.class).in(Singleton.class);
		serve("/protected/userproplist").with(
				UserPropositionListServlet.class);

		bind(ListUserDefinedPropositionChildrenServlet.class).in(
				Singleton.class);
		serve("/protected/userpropchildren").with(
				ListUserDefinedPropositionChildrenServlet.class);

		bind(EditPropositionServlet.class).in(Singleton.class);
		serve("/protected/editprop").with(EditPropositionServlet.class);

		bind(DateRangePhenotypeServlet.class).in(Singleton.class);
		serve("/protected/destinationphenotypes").with(
				DateRangePhenotypeServlet.class);

		bind(SearchSystemPropositionJSTreeV3Servlet.class).in(Singleton.class);
		serve("/protected/jstree3_searchsystemlist").with(
				SearchSystemPropositionJSTreeV3Servlet.class);

		bind(SearchSystemPropositionJSTreeV1Servlet.class).in(Singleton.class);
		serve("/protected/searchsystemlist").with(
				SearchSystemPropositionJSTreeV1Servlet.class);

	}
	
	@Override
	protected Map<String, String> getCasValidationFilterInitParams() {
		Map<String, String> params = new HashMap<>();
		params.put("casServerUrlPrefix", this.properties.getCasUrl());
		params.put("serverName", this.properties.getProxyCallbackServer());
		params.put("proxyCallbackUrl", getCasProxyCallbackUrl());
		params.put("proxyReceptorUrl", getCasProxyCallbackPath());
		return params;
	}

}
