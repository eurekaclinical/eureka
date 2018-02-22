package edu.emory.cci.aiw.cvrg.eureka.webapp.client;

/*-
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2017 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.clients.EtlClient;
import edu.emory.cci.aiw.cvrg.eureka.webapp.comm.clients.ServicesClient;
import javax.inject.Inject;
import org.eurekaclinical.common.comm.clients.Route;
import org.eurekaclinical.common.comm.clients.RouterTable;
import org.eurekaclinical.common.comm.clients.RouterTableLoadException;
import org.eurekaclinical.registry.client.EurekaClinicalRegistryClient;
import org.eurekaclinical.user.client.EurekaClinicalUserClient;

/**
 *
 * @author Andrew Post
 */
public class WebappRouterTable implements RouterTable {
	
	private final EurekaClinicalUserClient userClient;
	private final ServicesClient servicesClient;
	private final EtlClient etlClient;
	private final EurekaClinicalRegistryClient registryClient;

    @Inject
    public WebappRouterTable(ServicesClient inServices, EurekaClinicalUserClient inUserClient, EtlClient inEtlClient, EurekaClinicalRegistryClient inRegistryClient) {
        this.servicesClient = inServices;
		this.userClient = inUserClient;
		this.etlClient = inEtlClient;
		this.registryClient = inRegistryClient;
    }

	@Override
	public Route[] load() throws RouterTableLoadException {
		return new Route[]{
			new Route("/users", "/api/protected/users", this.userClient),
			new Route("/roles", "/api/protected/roles", this.userClient),
			new Route("/appproperties", "/api/appproperties", this.servicesClient),
			new Route("/file", "/api/protected/file", this.etlClient),
			new Route("/output", "/api/protected/output", this.etlClient),
			new Route("/components", "/api/protected/components", this.registryClient),
			new Route("/", "/api/protected/", this.servicesClient)
		};
	}
	
}
