/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.props;


import com.google.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Miao Ai
 */


@Singleton
public class AppProperties extends AbstractProperties {
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(
			AppProperties.class);
	public Map getProperties(){
		Map<String, Object> prop;
		prop= new HashMap();

		prop.put("ephiProhibited", isEphiProhibited());
		prop.put("demoMode",isDemoMode());
		prop.put("oAuthRegistrationEnabled",isOAuthRegistrationEnabled());
		prop.put("googleOAuthRegistrationEnabled", isGoogleOAuthRegistrationEnabled());
		prop.put("gitHubOAuthRegistrationEnabled", isGitHubOAuthRegistrationEnabled());
		prop.put("twitterOAuthRegistrationEnabled", isTwitterOAuthRegistrationEnabled());
		prop.put("globusOAuthRegistrationEnabled", isGlobusOAuthRegistrationEnabled());
		prop.put("localAccountRegistrationEnabled", isLocalAccountRegistrationEnabled());
		prop.put("registrationEnabled", isRegistrationEnabled());
		prop.put("supportUri", getSupportUri());
		prop.put("aiwSiteUrl", getAiwSiteUrl());
		prop.put("helpSiteUrl", getHelpSiteUrl());
		prop.put("organizationName", getOrganizationName());
		return prop;
	}
        
	/**
	 * Gets the ephiProhibited from application.properties.
	 *
	 * @return ephiProhibited.
	 */           
	public Boolean isEphiProhibited() {
		return Boolean.parseBoolean(this.getValue("eureka.common.ephiProhibited"));
	}
	/**
	 * Gets the demoMode from application.properties.
	 *
	 * @return demoMode.
	 */        
	public Boolean isDemoMode() {
		return Boolean.parseBoolean(this.getValue("eureka.common.demoMode"));
	}
        
	public Map getMode() {
		Map<Object, Object> prop;
		prop = new HashMap();
            
		prop.put("demoMode", isDemoMode());
		prop.put("ephiProhibited", isEphiProhibited());
		return prop;
	}
	/**
	 * Gets the isOAuthRegistrationEnabled from application.properties.
	 *
	 * @return isOAuthRegistrationEnabled.
	 */                            
	public Boolean isOAuthRegistrationEnabled() {
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
	/**
	 * Gets the isOAuthRegistrationEnabled from application.properties.
	 *
	 * @return isLocalAccountRegistrationEnabled.
	 */  
	public Boolean isLocalAccountRegistrationEnabled() {
		return Boolean.parseBoolean(getValue("eureka.webapp.localregistrationenabled"));
	}          
	/**
	 * Gets the registrationEnabled from application.properties.
	 *
	 * @return registrationEnabled.
	 */                   
	public Boolean isRegistrationEnabled() {
		return Boolean.parseBoolean(this.getValue("eureka.common.registrationEnabled"));
	}
	/**
	 * Gets the aiwSiteUrl from application.properties.
	 *
	 * @return aiwSiteUrl.
	 */           
	public String getAiwSiteUrl() {
		return this.getValue("eureka.common.aiwSiteUrl");
	}
	/**
	 * Gets the helpSiteUrl from application.properties.
	 *
	 * @return helpSiteUrl.
	 */           
	public String getHelpSiteUrl() {
		return this.getValue("eureka.common.helpSiteUrl");
	}
	/**
	 * Gets the organizationName from application.properties.
	 *
	 * @return organizationName.
	 */           
	public String getOrganizationName() {
		return this.getValue("eureka.common.organizationName");
	}
	@Override
	public String getProxyCallbackServer() {            
		return this.getValue("eureka.common.callbackserver");
	}        
}



