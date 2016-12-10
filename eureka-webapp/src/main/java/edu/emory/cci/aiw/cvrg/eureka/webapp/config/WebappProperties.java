package edu.emory.cci.aiw.cvrg.eureka.webapp.config;

import edu.emory.cci.aiw.cvrg.eureka.common.props.AbstractProperties;

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
public class WebappProperties extends AbstractProperties {

	@Override
	public String getUrl() {
		return this.getValue("eureka.webapp.url");
	}
	
	public String getUserWebappUrl() {
		return this.getValue("user.webapp.url");
	}
	public String getUserServiceUrl() {
		return this.getValue("user.service.url");
	}         
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
	
	public String getCohortsUrl() {
		return this.getValue("eureka.cohorts.url");
	}

	@Override
	public String getProxyCallbackServer() {
		return this.getValue("eureka.webapp.callbackserver");
	}
	
	public String getContactEmail() {
		return this.getValue("eureka.webapp.contactemail");
	}

	public boolean isOAuthRegistrationEnabled() {
		return isGoogleOAuthRegistrationEnabled() || isGitHubOAuthRegistrationEnabled() || isTwitterOAuthRegistrationEnabled() || isGlobusOAuthRegistrationEnabled();
	}

	public boolean isGoogleOAuthRegistrationEnabled() {
		return getGoogleOAuthKey() != null && getGoogleOAuthSecret() != null;
	}

	public boolean isGitHubOAuthRegistrationEnabled() {
		return getGitHubOAuthKey() != null && getGitHubOAuthSecret() != null;
	}

	public boolean isTwitterOAuthRegistrationEnabled() {
		return getTwitterOAuthKey() != null && getTwitterOAuthSecret() != null;
	}

	public boolean isGlobusOAuthRegistrationEnabled() {
		return getGlobusOAuthKey() != null && getGlobusOAuthSecret() != null;
	}

	public boolean isLocalAccountRegistrationEnabled() {
		return Boolean.parseBoolean(getValue("eureka.webapp.localregistrationenabled"));
	}
	
	public boolean isRegistrationEnabled() {
		return isLocalAccountRegistrationEnabled() || isOAuthRegistrationEnabled();
	}

	public String getGoogleOAuthKey() {
		return getValue("eureka.webapp.googleoauthkey");
	}

	public String getGoogleOAuthSecret() {
		return getValue("eureka.webapp.googleoauthsecret");
	}

	public String getGitHubOAuthKey() {
		return getValue("eureka.webapp.githuboauthkey");
	}

	public String getGitHubOAuthSecret() {
		return getValue("eureka.webapp.githuboauthsecret");
	}

	public String getTwitterOAuthKey() {
		return getValue("eureka.webapp.twitteroauthkey");
	}

	public String getTwitterOAuthSecret() {
		return getValue("eureka.webapp.twitteroauthsecret");
	}

	public String getGlobusOAuthKey() {
		return getValue("eureka.webapp.globusoauthkey");
	}

	public String getGlobusOAuthSecret() {
		return getValue("eureka.webapp.globusoauthsecret");
	}

}
