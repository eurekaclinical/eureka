package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dest.PatientSetSenderSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;

/**
 * Operations related to a data file (doPost, status, etc)
 *
 * @author Andrew Post
 *
 */
@Path("/protected")
@RolesAllowed({"researcher"})
public class OutputResource {

	private final EtlProperties etlProperties;
	private final DestinationDao destinationDao;
	private final PatientSetSenderSupport patientSetSenderSupport;

	/**
	 * Create an object with the give data access object.
	 *
	 */
	@Inject
	public OutputResource(EtlProperties inEtlProperties, DestinationDao inDestinationDao, PatientSetSenderSupport inPatientSetSenderSupport) {
		this.etlProperties = inEtlProperties;
		this.destinationDao = inDestinationDao;
		this.patientSetSenderSupport = inPatientSetSenderSupport;
	}

	@GET
	@Path("/output/{destinationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response doGet(@PathParam("destinationId") String inId) {
		DestinationEntity dest = this.destinationDao.getByName(inId);
		if (dest == null) {
			throw new HttpStatusException(Status.PRECONDITION_FAILED);
		}
		final File outputFile;
		try {
			outputFile = new File(
					this.etlProperties.outputFileDirectory(inId),
					this.patientSetSenderSupport.getOutputName(inId));
		} catch (IOException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
		if (!outputFile.exists()) {
			throw new HttpStatusException(Status.NOT_FOUND);
		}
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream os) throws IOException {
				try (InputStream inputStream = new FileInputStream(outputFile)) {
					IOUtils.copy(inputStream, os);
				}
				os.flush();
			}
		};

		String outputType = dest.getOutputType();
		String outputName = dest.getOutputName();
		if (outputName == null) {
			outputName = inId + "_out";
		}
		return Response.ok(stream, outputType).header("content-disposition", "attachment; filename = " + outputName).build();
	}

	@DELETE
	@Path("/output/{destinationId}")
	public Response doDelete(@PathParam("destinationId") String inId) {
		try {
			final File outputFile = new File(
					this.etlProperties.outputFileDirectory(inId),
					this.patientSetSenderSupport.getOutputName(inId));
			if (!outputFile.exists()) {
				throw new HttpStatusException(Status.NOT_FOUND);
			}
			Files.delete(outputFile.toPath());
			return Response.noContent().build();
		} catch (IOException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

}
