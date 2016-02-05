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
import edu.emory.cci.aiw.cvrg.eureka.common.props.AppPropertiesMode;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AppPropertiesRegistration;
import edu.emory.cci.aiw.cvrg.eureka.common.props.AppPropertiesLinks;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Miao Ai
 */

@Path("/appproperties")
@Produces(MediaType.APPLICATION_JSON)
public class AppPropertyResource {
    
	private final AppPropertiesMode appPropertiesMode;
	private final AppPropertiesLinks appPropertiesLinks;
	private final AppPropertiesRegistration appPropertiesRegistration;
        
	@Inject
	public AppPropertyResource( AppPropertiesMode inAppPropertiesMode, 
                                                    AppPropertiesLinks inAppPropertiesLinks, 
                                                    AppPropertiesRegistration inAppPropertiesRegistration){
		this.appPropertiesMode = inAppPropertiesMode;
		this.appPropertiesLinks = inAppPropertiesLinks;
		this.appPropertiesRegistration = inAppPropertiesRegistration;
	}
        
	@GET
	@Path("/mode")
	public AppPropertiesMode getAppPropertiesMode() {
		return this.appPropertiesMode;
	}
    
	@GET
	@Path("/links")
	public AppPropertiesLinks getAppPropertiesLinks() {
		return this.appPropertiesLinks;
	}

	@GET
	@Path("/registration")
	public AppPropertiesRegistration getAppPropertiesRegistration() {  
		return this.appPropertiesRegistration;
	}        
}


