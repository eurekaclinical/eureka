package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import edu.emory.cci.aiw.cvrg.eureka.etl.authentication.AuthorizedUserSupport;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.AuthorizedUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.FileUtils;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 *
 * @author Andrew Post
 */
@Path("/protected/file")
@RolesAllowed({"researcher"})
public class FileResource {

	private final EtlProperties etlProperties;
	private final AuthorizedUserDao userDao;
	private final SourceConfigDao sourceConfigDao;
	private final AuthorizedUserSupport authenticationSupport;
	private final EtlGroupDao groupDao;

	@Inject
	public FileResource(EtlProperties inEtlProperties, AuthorizedUserDao inUserDao, SourceConfigDao inSourceConfigDao, EtlGroupDao inGroupDao) {
		this.etlProperties = inEtlProperties;
		this.userDao = inUserDao;
		this.sourceConfigDao = inSourceConfigDao;
		this.authenticationSupport = new AuthorizedUserSupport(this.userDao);
		this.groupDao = inGroupDao;
	}

	@POST
	@Path("/upload/{sourceConfigId}/{sourceId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(
			@Context HttpServletRequest req,
			@PathParam("sourceConfigId") String sourceConfigId,
			@PathParam("sourceId") String sourceId,
			@FormDataParam("file") InputStream inUploadingInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@DefaultValue("1") @FormDataParam("flowChunkNumber") int chunkNumber,
			@DefaultValue("1") @FormDataParam("flowTotalChunks") int totalChunks) {
		AuthorizedUserEntity user = this.authenticationSupport.getUser(req);
		SourceConfigs sources = new SourceConfigs(this.etlProperties, user, this.sourceConfigDao, this.groupDao);
		try {
			SourceConfig sourceConfig = sources.getOne(sourceConfigId);
			if (sourceConfig == null || !sourceConfig.isExecute()) {
				throw new HttpStatusException(Status.NOT_FOUND);
			}
			File uploadedDir = this.etlProperties.uploadedDirectory(sourceConfigId, sourceId);
			try {
				File tempDir = this.etlProperties.tempUploadedDirectory(sourceConfigId, sourceId);
				File partialFile = new File(tempDir, String.format(fileDetail.getFileName() + "%05d", chunkNumber));
				FileUtils.copyInputStreamToFile(inUploadingInputStream, partialFile);
				File[] partialFiles = tempDir.listFiles();
				if (partialFiles.length == totalChunks) {
					Arrays.sort(partialFiles, fileComparator);
					try (OutputStream out = new FileOutputStream(new File(uploadedDir, fileDetail.getFileName()))) {
						for (int i = 0; i < totalChunks; i++) {
							FileUtils.copyFile(partialFiles[i], out);
						}
					}
					FileUtils.deleteDirectory(tempDir);
				};
			} catch (IOException ex) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"Uploading file '" + fileDetail.getFileName() + "' failed",
						ex);
			}
		} catch (IOException | SecurityException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"Uploading file '" + fileDetail.getFileName() + "' failed",
					ex);
		}

		return Response.status(Status.CREATED).build();
	}

	private static final Comparator<File> fileComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};

}
