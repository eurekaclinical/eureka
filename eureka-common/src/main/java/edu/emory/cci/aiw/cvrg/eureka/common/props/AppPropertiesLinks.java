/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.props;


/**
 *
 * @author Miao Ai
 */
public class AppPropertiesLinks {
        private SupportUri supportUri;
        private String aiwSiteUrl;
        private String organizationName;
        private String helpSiteUrl;
        /**
	 * Constructor.
	 */
        public AppPropertiesLinks(){
                AppProperties appProperties = AppProperties.getInstance();
                supportUri = appProperties.getSupportUri();
                aiwSiteUrl = appProperties.getValue("aiw.site.url");
                organizationName = appProperties.getValue("project.organization.name");
                helpSiteUrl = appProperties.getValue("aiw.help.url");
        }
        /**
	 * Gets the supportUri .
	 *
	 * @return supportUri.
	 */
        public SupportUri getSupportUri() {
                return this.supportUri;
        }
        /**
	 * Sets the supportUri.
	 * 
	 * @param inSupportUri
	 */
        public void setSupportUri(SupportUri inSupportUri) {
                this.supportUri = inSupportUri;    
        }
        /**
	 * Gets the aiwSiteUrl .
	 *
	 * @return aiwSiteUrl.
	 */     
        public String getAiwUrl() {		
                return this.aiwSiteUrl;
        }
        /**
	 * Sets the aiwSiteUrl.
	 * 
	 * @param inAiwUrl
	 */
        public void setAiwUrl(String inAiwUrl) {		
                this.aiwSiteUrl= inAiwUrl;
        }
        /**
	 * Gets the organizationName .
	 *
	 * @return organizationName.
	 */           
        public String getOrganizationName() {		
                return this.organizationName;
        }
        /**
	 * Sets the organizationName.
	 * 
	 * @param inOrganizationName
	 */
        public void setOrganizationName(String inOrganizationName) {		
                this.organizationName = inOrganizationName;
        }          
        /**
	 * Gets the helpSiteUrl .
	 *
	 * @return helpSiteUrl.
	 */            
        public String getHelpSiteUrl() {		
                return this.helpSiteUrl;
        }
        /**
	 * Sets the helpSiteUrl.
	 * 
	 * @param inHelpSiteUrl
	 */
        public void setHelpSiteUrl(String inHelpSiteUrl) {		
                this.helpSiteUrl = inHelpSiteUrl;
        }          
}
