/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 Emory University
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

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorException;

@Path("/job")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobResource {
	private static final Logger LOGGER = LoggerFactory
	        .getLogger(JobResource.class);
	private final JobDao jobDao;
	private final PropositionValidator propositionValidator;
	private final TaskManager taskManager;
	private final ConfDao confDao;

	@Inject
	public JobResource(JobDao inJobDao, TaskManager inTaskManager,
	        PropositionValidator inValidator, ConfDao inConfDao) {
		this.jobDao = inJobDao;
		this.taskManager = inTaskManager;
		this.propositionValidator = inValidator;
		this.confDao = inConfDao;
	}

	@GET
	@Path("/list")
	public List<Job> getAll() {
		return this.jobDao.getAll();
	}

	@GET
	@Path("/{jobId}")
	public Job getJob(@PathParam("jobId") Long inJobId) {
		return this.jobDao.retrieve(inJobId);
	}

	@POST
	@Path("/submit")
	public Response startJob(JobRequest inJobRequest) {
		Response response;
		Job job = inJobRequest.getJob();
		List<PropositionDefinition> definitions = inJobRequest
		        .getUserPropositions();
		Configuration configuration = this.confDao.getByUserId(job.getUserId());
		propositionValidator.setConfiguration(configuration);
		propositionValidator.setUserPropositions(definitions);
		boolean valid;
		try {
			valid = propositionValidator.validate();
		} catch (PropositionValidatorException e) {
			LOGGER.error(e.getMessage(), e);
			valid = false;
		}

		if (valid) {
			LOGGER.debug("Created {} definitions", definitions.size());
			System.err.println("DEFINITIONS: " + definitions);
			if (LOGGER.isDebugEnabled()) {
				for (PropositionDefinition pd : definitions) {
					LOGGER.debug("PropDef: {}", pd);
				}
			}
			job.setNewState("CREATED", null, null);
			LOGGER.debug("Request to start new Job {}", job.getId());
			this.jobDao.create(job);
			this.taskManager.queueTask(job.getId(), definitions, 
					inJobRequest.getPropositionIdsToShow());
			response = Response.created(URI.create("/" + job.getId())).build();
		} else {
			job.setNewState("FAILED", null, null);
			this.jobDao.create(job);
			for (String message : propositionValidator.getMessages()) {
				LOGGER.error(message);
			}
			response = Response.status(Response.Status.BAD_REQUEST)
			        .entity(propositionValidator.getMessages()).build();
		}
		return response;
	}

	@GET
	@Path("/status")
	public List<Job> getJobStatus(@QueryParam("filter") JobFilter inFilter) {

		LOGGER.debug("Request for job status");
		List<Job> jobs = this.jobDao.getWithFilter(inFilter);
		for (Job job : jobs) {
			this.jobDao.refresh(job);
			LOGGER.debug("Returning job {} with status {}", job.getId(),
			        job.getCurrentState());
		}
		return jobs;
	}
}
