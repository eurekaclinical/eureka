/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.props;


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
	AppPropertiesRegistration(){
                AppProperties appProperties = AppProperties.getInstance();
                localAccountRegistrationEnabled = (Boolean.parseBoolean(appProperties.getValue("eureka.webapp.localregistrationenabled")));
                googleOAuthKey = appProperties.getValue("eureka.webapp.googleoauthkey");
                googleOAuthSecret = appProperties.getValue("eureka.webapp.googleoauthsecret");
                gitHubOAuthKey = appProperties.getValue("eureka.webapp.githuboauthkey");
                gitHubOAuthSecret = appProperties.getValue("eureka.webapp.githuboauthsecret");
                twitterOAuthKey = appProperties.getValue("eureka.webapp.twitteroauthkey");
                twitterOAuthSecret = appProperties.getValue("eureka.webapp.twitteroauthsecret");
                globusOAuthKey = appProperties.getValue("eureka.webapp.globusoauthkey");
                globusOAuthSecret = appProperties.getValue("eureka.webapp.globusoauthsecret");                
                
        } 
        
	public boolean isRegistrationEnabled() {
		return isLocalAccountRegistrationEnabled() || isOAuthRegistrationEnabled();
	}   
	/**
	 * Gets the localAccountRegistrationEnabled .
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
        
 
	public List<String> getOAuthRegistrationProviders(){
		List<String> providers;
		providers = new ArrayList();
		if(isGoogleOAuthRegistrationEnabled()){
			providers.add("GoogleOAuth");
		}
		if(isGitHubOAuthRegistrationEnabled()){
			providers.add("GitHubOAuth");
 		}
		if(isTwitterOAuthRegistrationEnabled()){
			providers.add("TwitterOAuth");
		}
		if(isGlobusOAuthRegistrationEnabled()){
			providers.add("GlobusOAuth");
		}    
            
 		return providers;
	}
        
	public boolean isOAuthRegistrationEnabled() {
		return isGoogleOAuthRegistrationEnabled() || isGitHubOAuthRegistrationEnabled() || isTwitterOAuthRegistrationEnabled() || isGlobusOAuthRegistrationEnabled();
	}

	protected boolean isGoogleOAuthRegistrationEnabled() {
		return getGoogleOAuthKey() != null && getGoogleOAuthSecret() != null;
	}

	protected boolean isGitHubOAuthRegistrationEnabled() {
		return getGitHubOAuthKey() != null && getGitHubOAuthSecret() != null;
	}

	protected boolean isTwitterOAuthRegistrationEnabled() {
		return getTwitterOAuthKey() != null && getTwitterOAuthSecret() != null;
	}

	protected boolean isGlobusOAuthRegistrationEnabled() {
		return getGlobusOAuthKey() != null && getGlobusOAuthSecret() != null;
	}

	protected String getGoogleOAuthKey() {
		return googleOAuthKey;
	}

	protected void setGoogleOAuthKey(String inGoogleOAuthKey) {
		this.googleOAuthKey = inGoogleOAuthKey;
	}        
        
	protected String getGoogleOAuthSecret() {
		return googleOAuthSecret;
	}

	protected void setGoogleOAuthSecret(String inGoogleOAuthSecret) {
		this.googleOAuthSecret = inGoogleOAuthSecret;
	}          
        
	protected String getGitHubOAuthKey() {
		return gitHubOAuthKey;
	}

	protected void setGitHubOAuthKey(String inGitHubOAuthKey) {
		this.gitHubOAuthKey = inGitHubOAuthKey;
	}          
        
	protected String getGitHubOAuthSecret() {
		return gitHubOAuthSecret;
	}

	protected void setGitHubOAuthSecret(String inGitHubOAuthSecret) {
		this.gitHubOAuthSecret = inGitHubOAuthSecret;
	}          
        
	protected String getTwitterOAuthKey() {
		return twitterOAuthKey;
	}

	protected void setTwitterOAuthKey(String inTwitterOAuthKey) {
		this.twitterOAuthKey = inTwitterOAuthKey;
	}         
        
	protected String getTwitterOAuthSecret() {
		return twitterOAuthSecret;
	}

	protected void setTwitterOAuthSecret(String inTwitterOAuthSecret) {
		this.twitterOAuthSecret = inTwitterOAuthSecret;
	}           
        
	protected String getGlobusOAuthKey() {
		return globusOAuthKey;
	}

	protected void setGlobusOAuthKey(String inGlobusOAuthKey) {
		this.globusOAuthKey = inGlobusOAuthKey;
	}           
        
	protected String getGlobusOAuthSecret() {
		return globusOAuthSecret;
	}
        
	protected void setGlobusOAuthSecret(String inGlobusOAuthSecret) {
		this.globusOAuthSecret = inGlobusOAuthSecret;
	}           
}
