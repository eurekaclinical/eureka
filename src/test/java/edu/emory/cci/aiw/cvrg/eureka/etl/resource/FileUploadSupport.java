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

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Andrew Post
 */
class FileUploadSupport {
	ClientResponse.Status doUploadFile(WebResource webResource, File file, String sourceId, String fileTypeId) throws ClientHandlerException, UniformInterfaceException, IOException {
		try (InputStream is = new FileInputStream(file)) {
			FormDataMultiPart part = 
					new FormDataMultiPart();
			part.bodyPart(
					new FormDataBodyPart(
							FormDataContentDisposition
									.name("file")
									.fileName(file.getName())
									.build(), 
							is, MediaType.APPLICATION_OCTET_STREAM_TYPE));
			ClientResponse response = webResource
					.path("/api/protected/file/upload/" + sourceId + "/" + fileTypeId)
					.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.post(ClientResponse.class, part);
			ClientResponse.Status result = response.getClientResponseStatus();
			return result;
		}
	}
	
	ClientResponse.Status doUploadFile(WebResource webResource, String resourceName, String sourceId, String fileTypeId) throws ClientHandlerException, UniformInterfaceException, IOException {
		try (InputStream is = getClass().getResourceAsStream(resourceName)) {
			FormDataMultiPart part = 
					new FormDataMultiPart();
			part.bodyPart(
					new FormDataBodyPart(
							FormDataContentDisposition
									.name("file")
									.fileName("testupload.xlsx")
									.build(), 
							is, MediaType.APPLICATION_OCTET_STREAM_TYPE));
			ClientResponse response = webResource
					.path("/api/protected/file/upload/" + sourceId + "/" + fileTypeId)
					.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.post(ClientResponse.class, part);
			ClientResponse.Status result = response.getClientResponseStatus();
			return result;
		}
	}
	
}
