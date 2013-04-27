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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import com.sun.jersey.api.client.ClientHandlerException;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.ws.rs.core.MediaType;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;

/**
 * Tests related to the file upload resource.
 *
 * @author hrathod
 *
 */
public class FileResourceTest extends AbstractEtlResourceTest {

	private File tmpFile;
	
	@Before
	public void fileResourceSetUp() throws IOException {
		this.tmpFile = File.createTempFile("mockdatafile", null);
	}

	@After
	public void fileResourceTearDown() {
		this.tmpFile.delete();
		this.tmpFile = null;
	}

	@Test
	public final void testFileUploadPreconditionFailed() throws IOException {
		Assert.assertEquals(Status.PRECONDITION_FAILED, doUpload("baz", "oof"));
	}

	@Test
	public final void testFileUploadCreated() throws IOException {
		doUpload("foo", "bar");
		EtlProperties etlProperties = new EtlProperties();
		File dir = etlProperties.uploadedDirectory("foo", "bar");
		File[] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File directory, String filename) {
				return filename.endsWith(".uploaded");
			}
		});
		Assert.assertEquals(1, files.length);
	}

	private Status doUpload(String sourceId, String fileTypeId) throws UniformInterfaceException, 
			ClientHandlerException, IOException {
		FileInputStream is = new FileInputStream(tmpFile);
		try {
			FormDataMultiPart part = 
					new FormDataMultiPart();
			part.bodyPart(new FormDataBodyPart(FormDataContentDisposition.name("file").fileName(tmpFile.getName()).build(), is, MediaType.APPLICATION_OCTET_STREAM_TYPE));
			ClientResponse response = resource()
					.path("/api/file/upload/" + sourceId + "/" + fileTypeId)
					.type(MediaType.MULTIPART_FORM_DATA_TYPE)
					.post(ClientResponse.class, part);
			Status result = response.getClientResponseStatus();
			is.close();
			is = null;
			return result;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
			}
		}

		
	}
}
