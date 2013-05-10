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
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.FileUtils;
import org.arp.javautil.io.FileUtil;

/**
 * 
 * @author Andrew Post
 */
@Path("/file")
@RolesAllowed({"researcher"})
public class FileResource {

	private final EtlProperties etlProperties;

	@Inject
	public FileResource(EtlProperties inEtlProperties) {
		this.etlProperties = inEtlProperties;
	}
	
	@POST
	@Path("/upload/{sourceConfigId}/{sourceId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(
			@PathParam("sourceConfigId") String sourceConfigId,
			@PathParam("sourceId") String sourceId,
			@FormDataParam("file") InputStream uploadingInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		SourceConfigs sources = new SourceConfigs(this.etlProperties);

		try {
			if (!sources.sourceConfigExists(sourceConfigId)) {
				throw new HttpStatusException(Status.PRECONDITION_FAILED,
						"Source id '" + sourceConfigId + "' not found");
			}

			File uploadedDir = this.etlProperties.uploadedDirectory(sourceConfigId, sourceId);
			uploadedDir.mkdirs();
			try {
				File uploadingFile = File.createTempFile(
						"eurekabackend", ".uploading", uploadedDir);
				FileUtils.copyInputStreamToFile(uploadingInputStream, 
						uploadingFile);
				File renamedFile = FileUtil.replaceExtension(uploadingFile, ".uploaded");
				if (!uploadingFile.renameTo(renamedFile)) {
					throw new HttpStatusException(
							Response.Status.INTERNAL_SERVER_ERROR, 
							"Uploading file '" + fileDetail.getFileName() + "' failed: could not mark the file as uploaded");
				}
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
