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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobState;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.validator.PropositionValidatorException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;

@Path("/jobs")
@RolesAllowed({"researcher", "admin"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobResource.class);
	private final JobDao jobDao;
	private final EtlUserDao etlUserDao;
	private final PropositionValidator propositionValidator;
	private final TaskManager taskManager;

	@Inject
	public JobResource(JobDao inJobDao, TaskManager inTaskManager,
			PropositionValidator inValidator, EtlUserDao inEtlUserDao) {
		this.jobDao = inJobDao;
		this.taskManager = inTaskManager;
		this.propositionValidator = inValidator;
		this.etlUserDao = inEtlUserDao;
	}

	@GET
	public List<Job> getAll(@Context HttpServletRequest request,
			@QueryParam("order") String order) {
		JobFilter jobFilter = new JobFilter(null, 
				request.getUserPrincipal().getName(), null, null, null);
		List<Job> jobs = new ArrayList<Job>();
		List<JobEntity> jobEntities;
		if (order == null) {
			jobEntities = this.jobDao.getWithFilter(jobFilter);
		} else if (order.equals("desc")) {
			jobEntities = this.jobDao.getWithFilterDesc(jobFilter);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED, "Invalid value for the order parameter: " + order);
		}
		for (JobEntity jobEntity : jobEntities) {
			Job job = new Job();
			job.setDestinationId(jobEntity.getDestinationId());
			job.setSourceConfigId(jobEntity.getSourceConfigId());
			job.setTimestamp(jobEntity.getCreated());
			job.setId(jobEntity.getId());
			job.setUsername(jobEntity.getEtlUser().getUsername());
			job.setState(jobEntity.getCurrentState());
			job.setJobEvents(jobEntity.getJobEvents());
			jobs.add(job);
		}
		return jobs;
	}

	@GET
	@Path("/{jobId}")
	public Job getJob(@Context HttpServletRequest request,
			@PathParam("jobId") Long inJobId) {
		JobFilter jobFilter = new JobFilter(inJobId, 
				request.getUserPrincipal().getName(), null, null, null);
		List<JobEntity> jobEntities = this.jobDao.getWithFilter(jobFilter);
		if (jobEntities.isEmpty()) {
			throw new HttpStatusException(Status.NOT_FOUND);
		} else if (jobEntities.size() > 1) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, jobEntities.size() + " jobs returned for job id " + inJobId);
		} else {
			JobEntity jobEntity = jobEntities.get(0);
			Job job = new Job();
			job.setDestinationId(jobEntity.getDestinationId());
			job.setSourceConfigId(jobEntity.getSourceConfigId());
			job.setTimestamp(jobEntity.getCreated());
			job.setId(jobEntity.getId());
			job.setUsername(jobEntity.getEtlUser().getUsername());
			job.setState(jobEntity.getCurrentState());
			job.setJobEvents(jobEntity.getJobEvents());
			return job;
		}
	}

	@POST
	public Response submit(@Context HttpServletRequest request,
			JobRequest inJobRequest) {
		JobSpec job = inJobRequest.getJobSpec();
		JobEntity jobEntity = new JobEntity();
		jobEntity.setSourceConfigId(job.getSourceConfigId());
		jobEntity.setDestinationId(job.getDestinationId());
		jobEntity.setNewState(JobState.CREATED, null, null);
		jobEntity.setCreated(new Date());
		jobEntity.setEtlUser(toEtlUser(request.getUserPrincipal()));
		this.jobDao.create(jobEntity);

		List<PropositionDefinition> definitions = inJobRequest
				.getUserPropositions();
		propositionValidator.setConfigId(job.getSourceConfigId());
		propositionValidator.setUserPropositions(definitions);
		boolean valid;
		try {
			LOGGER.debug("Created {} definitions", definitions.size());
			if (LOGGER.isDebugEnabled()) {
				for (PropositionDefinition pd : definitions) {
					LOGGER.debug("PropDef: {}", pd);
				}
			}
			valid = propositionValidator.validate();
		} catch (PropositionValidatorException e) {
			LOGGER.error(e.getMessage(), e);
			valid = false;
		}
		if (valid) {
			jobEntity.setNewState(JobState.VALIDATED, null, null);
			this.jobDao.update(jobEntity);
		} else {
			return failed(jobEntity);
		}

		LOGGER.debug("Request to start new Job {}", jobEntity.getId());
		this.taskManager.queueTask(jobEntity.getId(), definitions,
				inJobRequest.getPropositionIdsToShow());
		return Response.created(URI.create("/" + jobEntity.getId())).build();
	}

	private Response failed(JobEntity job) {
		return failed(job, null, null);
	}

	private Response failed(JobEntity job, String message, Exception ex) {
		StackTraceElement[] stes = ex.getStackTrace();
		String[] stackTrace = new String[stes.length];
		for (int i = 0; i < stackTrace.length; i++) {
			stackTrace[i] = stes[i].toString();
		}
		job.setNewState(JobState.FAILED, message, stackTrace);
		this.jobDao.update(job);
		for (String msg : propositionValidator.getMessages()) {
			LOGGER.error(msg);
		}
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(propositionValidator.getMessages()).build();
	}

	@GET
	@Path("/status")
	public List<Job> getJobStatus(@QueryParam("filter") JobFilter inFilter) {
		List<Job> jobs = new ArrayList<Job>();
		for (JobEntity jobEntity : this.jobDao.getWithFilter(inFilter)) {
			Job job = new Job();
			job.setDestinationId(jobEntity.getDestinationId());
			job.setSourceConfigId(jobEntity.getSourceConfigId());
			job.setTimestamp(jobEntity.getCreated());
			job.setId(jobEntity.getId());
			job.setUsername(jobEntity.getEtlUser().getUsername());
			job.setState(jobEntity.getCurrentState());
			job.setJobEvents(jobEntity.getJobEvents());
			jobs.add(job);
		}
		return jobs;
	}
	
	private EtlUser toEtlUser(Principal userPrincipal) {
		String username = userPrincipal.getName();
		EtlUser etlUser = this.etlUserDao.getByUsername(username);
		if (etlUser == null) {
			etlUser = new EtlUser();
			etlUser.setUsername(username);
			this.etlUserDao.create(etlUser);
		}
		return etlUser;
	}
}
