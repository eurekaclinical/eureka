/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthorizedUserSupport;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.AuthorizedUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

@Path("/protected/sourceconfigs")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SourceConfigResource {
	private final EtlProperties etlProperties;
	private final AuthorizedUserDao userDao;
	private final SourceConfigDao sourceConfigDao;
	private final AuthorizedUserSupport authenticationSupport;
	private final EtlGroupDao groupDao;

	@Inject
	public SourceConfigResource(EtlProperties inEtlProperties, AuthorizedUserDao inUserDao, SourceConfigDao inSourceConfigDao, EtlGroupDao inGroupDao) {
		this.etlProperties = inEtlProperties;
		this.userDao = inUserDao;
		this.sourceConfigDao = inSourceConfigDao;
		this.authenticationSupport = new AuthorizedUserSupport(this.userDao);
		this.groupDao = inGroupDao;
	}

	@GET
	@Path("/{sourceConfigId}")
	public SourceConfig getSource(@Context HttpServletRequest req,
			@PathParam("sourceConfigId") String sourceConfigId) {
		AuthorizedUserEntity user = this.authenticationSupport.getUser(req);
		SourceConfigs sourceConfigs = new SourceConfigs(this.etlProperties, user, this.sourceConfigDao, this.groupDao);
		SourceConfig sourceConfig = sourceConfigs.getOne(sourceConfigId);
		if (sourceConfig != null) {
			return sourceConfig;
		} else {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
	}

	@GET
	public List<SourceConfig> getAll(@Context HttpServletRequest req) {
		AuthorizedUserEntity user = this.authenticationSupport.getUser(req);
		SourceConfigs sourceConfigs = new SourceConfigs(this.etlProperties, user, this.sourceConfigDao, this.groupDao);
		return sourceConfigs.getAll();
	}
}
