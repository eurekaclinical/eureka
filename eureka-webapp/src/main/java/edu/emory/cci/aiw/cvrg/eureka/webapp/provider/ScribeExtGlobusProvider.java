package edu.emory.cci.aiw.cvrg.eureka.webapp.provider;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.webapp.config.WebappProperties;
import org.eurekaclinical.scribeupext.provider.GlobusProvider;

/**
 *
 * @author Andrew Post
 */
public class ScribeExtGlobusProvider implements Provider<GlobusProvider> {
	private final GlobusProvider globusProvider;
	
	@Inject
	public ScribeExtGlobusProvider(WebappProperties inProperties) {
		this.globusProvider = new GlobusProvider();
		this.globusProvider.setKey(inProperties.getGlobusOAuthKey());
		this.globusProvider.setSecret(inProperties.getGlobusOAuthSecret());
		this.globusProvider.setCallbackUrl(inProperties.getApplicationUrl() + "registrationoauthglobuscallback");
	}

	@Override
	public GlobusProvider get() {
		return this.globusProvider;
	}
	
}
