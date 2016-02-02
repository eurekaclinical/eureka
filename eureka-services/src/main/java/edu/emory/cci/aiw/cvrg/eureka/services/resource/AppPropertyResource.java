/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.google.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AppProperties;
import edu.emory.cci.aiw.cvrg.eureka.common.props.SupportUri;
import java.util.HashMap;


import java.util.Map;
/**
 *
 * @author Miao Ai
 */

@Path("/appproperties")
@Produces(MediaType.APPLICATION_JSON)
public class AppPropertyResource {
    
	private final AppProperties appProperties;
	@Inject
	public AppPropertyResource( AppProperties inAppProperties){
		this.appProperties = inAppProperties;
	}
        
	@GET
	public Map getAllAppProperties() {
		return this.appProperties.getProperties();
	}
        
	@GET
	@Path("/registration")
	public Boolean getAppPropertiesIsRegistrationEnabled() {  
		return this.appProperties.isRegistrationEnabled();
	}
    
	@GET
	@Path("/support")
	public SupportUri getAppPropertiesSupportUri() {
		return this.appProperties.getSupportUri();
	}
        
	@GET
	@Path("/mode")
	public Map getAppPropertiesMode() {
		return this.appProperties.getMode();
	}
}


