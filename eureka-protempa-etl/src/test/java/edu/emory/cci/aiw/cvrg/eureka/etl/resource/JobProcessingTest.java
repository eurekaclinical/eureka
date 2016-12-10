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
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.etl.Constants;
import edu.emory.cci.aiw.cvrg.eureka.etl.dsb.EurekaDataSourceBackend;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.eurekaclinical.eureka.client.comm.FileSourceConfigOption;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.JobSpec;
import org.eurekaclinical.eureka.client.comm.JobStatus;
import org.eurekaclinical.eureka.client.comm.PatientSet;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import org.eurekaclinical.eureka.client.comm.SourceConfig.Section;
import org.eurekaclinical.eureka.client.comm.SourceConfigOption;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Andrew Post
 */
public class JobProcessingTest extends AbstractEtlResourceTest {

	private final FileUploadSupport fileUploadSupport;

	public JobProcessingTest() {
		this.fileUploadSupport = new FileUploadSupport();
	}

	@Before
	public void fileResourceSetUp() throws IOException {
		this.fileUploadSupport.doUploadFile(resource(), "/docs/sample.xlsx", Constants.SOURCECONFIG_NAME, "filename");
	}

	@Test
	public void submitPatientSetExtractorJob() throws IOException {
		JobRequest jobRequest = new JobRequest();
		JobSpec jobSpec = new JobSpec();
		jobSpec.setSourceConfigId(Constants.SOURCECONFIG_NAME);
		jobSpec.setDestinationId(Constants.DESTINATION_NAME);
		SourceConfig prompts = new SourceConfig();
		prompts.setId(Constants.SOURCECONFIG_NAME);
		Section section = new Section();
		section.setId(EurekaDataSourceBackend.class.getName());
		FileSourceConfigOption fsco = new FileSourceConfigOption();
		fsco.setName("filename");
		fsco.setValue("testupload.xlsx");
		section.setOptions(new SourceConfigOption[]{fsco});
		prompts.setDataSourceBackends(new Section[]{section});
		jobSpec.setPrompts(prompts);
		
		jobSpec.setPropositionIds(Arrays.asList(Constants.ALIAS_PROPOSITION_ID));
		jobSpec.setName("test job");
		jobRequest.setJobSpec(jobSpec);

		ClientResponse response = this.resource()
				.path("/api/protected/jobs")
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jobRequest);
		assertEquals(ClientResponse.Status.CREATED, response.getClientResponseStatus());

		URI location = response.getLocation();

		Job job;
		JobStatus jobStatus;
		do {
			try {
				Thread.sleep(5 * 1000L);
			} catch (InterruptedException ex) {}
			job = resource().uri(location).accept(
					MediaType.APPLICATION_JSON).get(new GenericType<Job>() {
					});
			jobStatus = job.getStatus();
		} while (jobStatus != JobStatus.COMPLETED && jobStatus != JobStatus.FAILED);

		assertEquals(JobStatus.COMPLETED, jobStatus);
		
		PatientSet expected;
		try (InputStream expectedIn = getClass().getResourceAsStream("/truth/submitPatientSetSenderJobOutput.json")) {
			expected = new ObjectMapper().readValue(expectedIn, PatientSet.class);
		}
		
		PatientSet actual = this.resource()
				.path("/api/protected/output/" + Constants.DESTINATION_NAME)
				.accept(MediaType.APPLICATION_JSON)
				.get(PatientSet.class);
		
		assertEquals(actual, expected);
	}
}
