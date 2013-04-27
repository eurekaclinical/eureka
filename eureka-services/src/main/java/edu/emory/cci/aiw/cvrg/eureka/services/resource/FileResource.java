/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClient;
import java.io.InputStream;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;

/**
 * Operations related to a data file (upload, status, etc)
 *
 * @author hrathod
 *
 */
@Path("/file")
public class FileResource {
	
	private final EtlClient etlClient;

	/**
	 * Create an object with the give data access object.
	 *
	 * @param inFileDao The data access object used to communicate with
	 *            {@link FileDao} objects in the data store.
	 */
	@Inject
	public FileResource(EtlClient inEtlClient) {
		this.etlClient = inEtlClient;
	}

	@POST
	@Path("/upload/{sourceConfigId}/{sourceId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@PathParam("sourceConfigId") String sourceConfigId,
			@PathParam("sourceId") String sourceId,
			@FormDataParam("file") InputStream uploadingInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		try {
			etlClient.upload(fileDetail.getFileName(), sourceConfigId, 
					sourceId, uploadingInputStream);
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
		return Response.status(Status.CREATED).build();
	}
}
