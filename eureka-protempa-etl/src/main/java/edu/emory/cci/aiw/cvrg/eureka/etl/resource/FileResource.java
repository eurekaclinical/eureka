package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

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
import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.authentication.EtlAuthenticationSupport;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.FileUtils;

/**
 * 
 * @author Andrew Post
 */
@Path("/protected/file")
@RolesAllowed({"researcher"})
public class FileResource {

	private final EtlProperties etlProperties;
	private final EtlUserDao userDao;
	private final SourceConfigDao sourceConfigDao;
	private final EtlAuthenticationSupport authenticationSupport;
	private final EtlGroupDao groupDao;

	@Inject
	public FileResource(EtlProperties inEtlProperties, EtlUserDao inUserDao, SourceConfigDao inSourceConfigDao, EtlGroupDao inGroupDao) {
		this.etlProperties = inEtlProperties;
		this.userDao = inUserDao;
		this.sourceConfigDao = inSourceConfigDao;
		this.authenticationSupport = new EtlAuthenticationSupport(this.userDao);
		this.groupDao = inGroupDao;
	}
	
	@POST
	@Path("/upload/{sourceConfigId}/{sourceId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(
			@Context HttpServletRequest req,
			@PathParam("sourceConfigId") String sourceConfigId,
			@PathParam("sourceId") String sourceId,
			@FormDataParam("file") InputStream uploadingInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		EtlUserEntity user = this.authenticationSupport.getEtlUser(req);
		SourceConfigs sources = new SourceConfigs(this.etlProperties, user, this.sourceConfigDao, this.groupDao);

		try {
			SourceConfig sourceConfig = sources.getOne(sourceConfigId);
			if (sourceConfig == null || !sourceConfig.isExecute()) {
				throw new HttpStatusException(Status.NOT_FOUND);
			}

			File uploadedDir = this.etlProperties.uploadedDirectory(sourceConfigId, sourceId);
			try {
				File uploadingFile = new File(uploadedDir, fileDetail.getFileName());
				FileUtils.copyInputStreamToFile(uploadingInputStream, 
						uploadingFile);
			} catch (IOException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR, 
						"Uploading file '" + fileDetail.getFileName() + "' failed", 
						ex);
			}
		} catch (SecurityException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR, 
					"Uploading file '" + fileDetail.getFileName() + "' failed", 
					ex);
		}

		return Response.status(Status.CREATED).build();
	}

	
}
