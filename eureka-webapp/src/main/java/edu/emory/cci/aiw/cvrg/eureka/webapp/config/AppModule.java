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

import com.google.inject.AbstractModule;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ProxyClientProvider;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ScribeExtGitHubProvider;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ScribeExtGlobusProvider;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ScribeExtGoogleProvider;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ScribeExtTwitterProvider;
import edu.emory.cci.aiw.cvrg.eureka.webapp.provider.ServicesClientProvider;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ProxyClient;
import org.eurekaclinical.scribeupext.provider.GitHubProvider;
import org.eurekaclinical.scribeupext.provider.GlobusProvider;
import org.eurekaclinical.scribeupext.provider.Google2Provider;
import org.eurekaclinical.scribeupext.provider.SSLTwitterProvider;
import org.eurekaclinical.standardapis.props.EurekaClinicalProperties;

/**
 *
 * @author hrathod
 */
class AppModule extends AbstractModule {
	private final WebappProperties webappProperties;

	AppModule(WebappProperties webappProperties) {
		assert webappProperties != null : "webappProperties cannot be null";
		this.webappProperties = webappProperties;
	}
	
	@Override
	protected void configure() {
		bind(WebappProperties.class).toInstance(this.webappProperties);
		bind(EurekaClinicalProperties.class).toInstance(this.webappProperties);
		bind(ServicesClient.class).toProvider(ServicesClientProvider.class);
                bind(ProxyClient.class).toProvider(ProxyClientProvider.class);
		bind(GitHubProvider.class).toProvider(ScribeExtGitHubProvider.class);
		bind(GlobusProvider.class).toProvider(ScribeExtGlobusProvider.class);
		bind(Google2Provider.class).toProvider(ScribeExtGoogleProvider.class);
		bind(SSLTwitterProvider.class).toProvider(ScribeExtTwitterProvider.class);
	}
	
}
