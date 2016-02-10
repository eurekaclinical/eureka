package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2016 Emory University
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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miao Ai
 */
public class AppPropertiesRegistration {
	private boolean localAccountRegistrationEnabled = false;
	private boolean registrationEnabled = false;
	private String googleOAuthKey;
	private String googleOAuthSecret;
	private String gitHubOAuthKey;
	private String gitHubOAuthSecret;
	private String twitterOAuthKey;
	private String twitterOAuthSecret;
	private String globusOAuthKey;
	private String globusOAuthSecret;        
	/**
	 * Constructor.
	 *
	 */         
	public AppPropertiesRegistration(){        
	}
        
	public boolean isRegistrationEnabled() {
		return isLocalAccountRegistrationEnabled() || !getOAuthRegistrationProviders().isEmpty();
	}   
	/**
	 * Gets the localAccountRegistrationEnabled.
	 *
	 * @return localAccountRegistrationEnabled.
	 */        
	public boolean isLocalAccountRegistrationEnabled() {
		return localAccountRegistrationEnabled;
	}        
	/**
	 * Sets the localAccountRegistrationEnabled status.
	 *
	 * @param inLocalAccountRegistrationEnabled.
	 */  
	public void setLocalAccountRegistrationEnabled(boolean inLocalAccountRegistrationEnabled) {
                this.localAccountRegistrationEnabled = inLocalAccountRegistrationEnabled;
	}   
        
	public enum OAuths{
            GOOGLE, GITHUB, TWITTER, GLOBUS
	} 
        
	public List<OAuths> getOAuthRegistrationProviders(){
		List<OAuths> providers;
		providers = new ArrayList<>();
		if(getGoogleOAuthKey() != null && getGoogleOAuthSecret() != null){
			providers.add(OAuths.GOOGLE);
		}
		if(getGitHubOAuthKey() != null && getGitHubOAuthSecret() != null){
			providers.add(OAuths.GITHUB);
		} 
		if(getTwitterOAuthKey() != null && getTwitterOAuthSecret() != null){
			providers.add(OAuths.TWITTER);
		}
		if(getGlobusOAuthKey() != null && getGlobusOAuthSecret() != null){
			providers.add(OAuths.GLOBUS);
		}             
 		return providers;
	}
        
	protected String getGoogleOAuthKey() {
		return googleOAuthKey;
	}

	public void setGoogleOAuthKey(String inGoogleOAuthKey) {
		this.googleOAuthKey = inGoogleOAuthKey;
	}        
        
	protected String getGoogleOAuthSecret() {
		return googleOAuthSecret;
	}

	public void setGoogleOAuthSecret(String inGoogleOAuthSecret) {
		this.googleOAuthSecret = inGoogleOAuthSecret;
	}          
        
	protected String getGitHubOAuthKey() {
		return gitHubOAuthKey;
	}

	public void setGitHubOAuthKey(String inGitHubOAuthKey) {
		this.gitHubOAuthKey = inGitHubOAuthKey;
	}          
        
	protected String getGitHubOAuthSecret() {
		return gitHubOAuthSecret;
	}

	public void setGitHubOAuthSecret(String inGitHubOAuthSecret) {
		this.gitHubOAuthSecret = inGitHubOAuthSecret;
	}          
        
	protected String getTwitterOAuthKey() {
		return twitterOAuthKey;
	}

	public void setTwitterOAuthKey(String inTwitterOAuthKey) {
		this.twitterOAuthKey = inTwitterOAuthKey;
	}         
        
	protected String getTwitterOAuthSecret() {
		return twitterOAuthSecret;
	}

	public void setTwitterOAuthSecret(String inTwitterOAuthSecret) {
		this.twitterOAuthSecret = inTwitterOAuthSecret;
	}           
        
	protected String getGlobusOAuthKey() {
		return globusOAuthKey;
	}

	public void setGlobusOAuthKey(String inGlobusOAuthKey) {
		this.globusOAuthKey = inGlobusOAuthKey;
	}           
        
	protected String getGlobusOAuthSecret() {
		return globusOAuthSecret;
	}
        
	public void setGlobusOAuthSecret(String inGlobusOAuthSecret) {
		this.globusOAuthSecret = inGlobusOAuthSecret;
	}           
}
