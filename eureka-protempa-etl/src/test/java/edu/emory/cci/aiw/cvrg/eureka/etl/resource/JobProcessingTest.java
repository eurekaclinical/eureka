package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.FileSourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PatientSet;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig.Section;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus;
import edu.emory.cci.aiw.cvrg.eureka.etl.Constants;
import edu.emory.cci.aiw.cvrg.eureka.etl.dsb.EurekaDataSourceBackend;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
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
	public void submitPatientSetSenderJob() throws IOException {
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
