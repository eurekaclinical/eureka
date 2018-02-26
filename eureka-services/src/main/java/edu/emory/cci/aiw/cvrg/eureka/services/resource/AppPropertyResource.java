package edu.emory.cci.aiw.cvrg.eureka.services.resource;

/*
 * #%L
 * Eureka Services
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

import com.google.inject.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppPropertiesLinks;
import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppPropertiesModes;
import edu.emory.cci.aiw.cvrg.eureka.services.comm.AppPropertiesRegistration;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
/**
 *
 * @author Miao Ai
 */

@Path("/appproperties")
@Produces(MediaType.APPLICATION_JSON)
public class AppPropertyResource {
	private final ServiceProperties serviceProperties;     
        
	@Inject
	public AppPropertyResource(ServiceProperties inServiceProperties){
		this.serviceProperties= inServiceProperties;
	}

	@GET
	public AppProperties getAppProperties() {
		return this.serviceProperties.getAppProperties();
	}        
        
	@GET
	@Path("/modes")
	public AppPropertiesModes getAppPropertiesModes() {
		return this.serviceProperties.getAppPropertiesModes();
	}
    
	@GET
	@Path("/links")
	public AppPropertiesLinks getAppPropertiesLinks() {
		return this.serviceProperties.getAppPropertiesLinks();
	}

	@GET
	@Path("/registration")
	public AppPropertiesRegistration getAppPropertiesRegistration() {
		return this.serviceProperties.getAppPropertiesRegistration();
	}
}


