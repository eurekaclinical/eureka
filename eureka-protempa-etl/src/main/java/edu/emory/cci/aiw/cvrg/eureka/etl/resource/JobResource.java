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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.protempa.PropositionDefinition;
import org.protempa.backend.dsb.filter.DateTimeFilter;
import org.protempa.proposition.value.AbsoluteTimeGranularity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.authentication.EtlAuthenticationSupport;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;

@Path("/protected/jobs")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobResource {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobResource.class);
	private final JobDao jobDao;
	private final EtlUserDao etlUserDao;
	private final TaskManager taskManager;
	private final EtlAuthenticationSupport authenticationSupport;
	private final DestinationDao destinationDao;

	@Inject
	public JobResource(JobDao inJobDao, TaskManager inTaskManager,
			EtlUserDao inEtlUserDao, DestinationDao inDestinationDao) {
		this.jobDao = inJobDao;
		this.taskManager = inTaskManager;
		this.etlUserDao = inEtlUserDao;
		this.authenticationSupport = new EtlAuthenticationSupport(this.etlUserDao);
		this.destinationDao = inDestinationDao;
	}

	@GET
	public List<Job> getAll(@Context HttpServletRequest request,
			@QueryParam("order") String order) {
		JobFilter jobFilter = new JobFilter(null,
				this.authenticationSupport.getEtlUser(request).getId(), null, null, null);
		List<Job> jobs = new ArrayList<>();
		List<JobEntity> jobEntities;
		if (order == null) {
			jobEntities = this.jobDao.getWithFilter(jobFilter);
		} else if (order.equals("desc")) {
			jobEntities = this.jobDao.getWithFilterDesc(jobFilter);
		} else {
			throw new HttpStatusException(Response.Status.PRECONDITION_FAILED, "Invalid value for the order parameter: " + order);
		}
		for (JobEntity jobEntity : jobEntities) {
			jobs.add(jobEntity.toJob());
		}
		return jobs;
	}

	@GET
	@Path("/{jobId}")
	public Job getJob(@Context HttpServletRequest request,
			@PathParam("jobId") Long inJobId) {
		JobFilter jobFilter = new JobFilter(inJobId,
				this.authenticationSupport.getEtlUser(request).getId(), null, null, null);
		List<JobEntity> jobEntities = this.jobDao.getWithFilter(jobFilter);
		if (jobEntities.isEmpty()) {
			throw new HttpStatusException(Status.NOT_FOUND);
		} else if (jobEntities.size() > 1) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, jobEntities.size() + " jobs returned for job id " + inJobId);
		} else {
			JobEntity jobEntity = jobEntities.get(0);
			return jobEntity.toJob();
		}
	}

	@POST
	public Response submit(@Context HttpServletRequest request,
			JobRequest inJobRequest) {
		Long jobId = doCreateJob(inJobRequest, request);
		return Response.created(URI.create("/" + jobId)).build();
	}
	
	private Long doCreateJob(JobRequest inJobRequest, HttpServletRequest request) {
		JobSpec job = inJobRequest.getJobSpec();
		JobEntity jobEntity = 
				newJobEntity(job, 
						this.authenticationSupport.getEtlUser(request));
		this.taskManager.queueTask(jobEntity.getId(), 
				inJobRequest.getUserPropositions(),
				inJobRequest.getPropositionIdsToShow(), 
				new DateTimeFilter(
				new String[]{job.getDateRangeDataElementKey()},
				job.getEarliestDate(), AbsoluteTimeGranularity.DAY,
				job.getLatestDate(), AbsoluteTimeGranularity.DAY,
				job.getEarliestDateSide(), job.getLatestDateSide()),
				job.isAppendData());
		return jobEntity.getId();
	}

	@GET
	@RolesAllowed({"admin"})
	@Path("/status")
	public List<Job> getJobStatus(@QueryParam("filter") JobFilter inFilter) {
		List<Job> jobs = new ArrayList<>();
		for (JobEntity jobEntity : this.jobDao.getWithFilter(inFilter)) {
			jobs.add(jobEntity.toJob());
		}
		return jobs;
	}

	private JobEntity newJobEntity(JobSpec job, EtlUserEntity etlUser) {
		JobEntity jobEntity = new JobEntity();
		jobEntity.setSourceConfigId(job.getSourceConfigId());
		DestinationEntity destination = 
				this.destinationDao.getByName(job.getDestinationId());
		jobEntity.setDestination(destination);
		jobEntity.setCreated(new Date());
		jobEntity.setEtlUser(etlUser);
		this.jobDao.create(jobEntity);
		return jobEntity;
	}

}
