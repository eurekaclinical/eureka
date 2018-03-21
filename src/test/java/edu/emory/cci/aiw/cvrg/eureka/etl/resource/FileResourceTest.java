/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.UniformInterfaceException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;

/**
 * Tests related to the file upload resource.
 *
 * @author hrathod
 *
 */
public class FileResourceTest extends AbstractEtlResourceTest {

	private File tmpFile;
	private static EtlProperties etlProperties;
	private final FileUploadSupport fileUploadSupport;

	public FileResourceTest() {
		this.fileUploadSupport = new FileUploadSupport();
	}

	@BeforeClass
	public static void setupClass() throws IOException {
		etlProperties = new EtlProperties();
		FileUtils.deleteDirectory(etlProperties.getUploadedDirectory());
	}

	@Before
	public void fileResourceSetUp() throws IOException {
		this.tmpFile = File.createTempFile("mockdatafile", null);
	}

	@After
	public void fileResourceTearDown() throws IOException {
		if (this.tmpFile != null) {
			this.tmpFile.delete();
		}
	}
	
	@After
	public void uploadedDirectoryTearDown() throws IOException {
		if (etlProperties != null) {
			FileUtils.deleteDirectory(etlProperties.getUploadedDirectory());
		}
	}

	@Test
	public final void testFileUploadCreated1() throws IOException {
		assertEquals(Status.CREATED, doUpload("foo", "oof"));
	}

	@Test
	public final void testFileUploadNotFound() throws IOException {
		assertEquals(Status.NOT_FOUND, doUpload("baz", "oof"));
	}

	@Test
	public final void testFileUploadCreated2() throws IOException {
		doUpload("foo", "bar");
		File dir = etlProperties.uploadedDirectory("foo", "bar");
		File[] files = dir.listFiles();
		assertEquals(1, files == null ? 0 : files.length);
	}

	private Status doUpload(String sourceId, String fileTypeId) throws UniformInterfaceException,
			ClientHandlerException, IOException {
		return this.fileUploadSupport.doUploadFile(resource(), this.tmpFile, sourceId, fileTypeId);
	}

}
