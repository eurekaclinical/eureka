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

import org.protempa.backend.dsb.filter.DateTimeFilter;
import org.protempa.proposition.value.AbsoluteTimeGranularity;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobFilter;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.authentication.EtlAuthenticationSupport;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.job.TaskManager;
import org.protempa.backend.BackendInstanceSpec;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.BackendSpecNotFoundException;
import org.protempa.backend.Configuration;
import org.protempa.backend.InvalidPropertyNameException;
import org.protempa.backend.InvalidPropertyValueException;
import org.protempa.backend.dsb.DataSourceBackend;

@Path("/protected/jobs")
@RolesAllowed({"researcher"})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobResource {

	private final JobDao jobDao;
	private final EtlUserDao etlUserDao;
	private final TaskManager taskManager;
	private final EtlAuthenticationSupport authenticationSupport;
	private final DestinationDao destinationDao;
	private final EurekaProtempaConfigurations configurations;

	@Inject
	public JobResource(JobDao inJobDao, TaskManager inTaskManager,
			EtlUserDao inEtlUserDao, DestinationDao inDestinationDao,
			EurekaProtempaConfigurations configurations) {
		this.jobDao = inJobDao;
		this.taskManager = inTaskManager;
		this.etlUserDao = inEtlUserDao;
		this.authenticationSupport = new EtlAuthenticationSupport(this.etlUserDao);
		this.destinationDao = inDestinationDao;
		this.configurations = configurations;
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
		JobSpec jobSpec = inJobRequest.getJobSpec();
		Configuration prompts = toConfiguration(jobSpec.getPrompts());
		JobEntity jobEntity
				= newJobEntity(jobSpec,
						this.authenticationSupport.getEtlUser(request));
		DateTimeFilter dateTimeFilter;
		String dateRangeDataElementKey = jobSpec.getDateRangeDataElementKey();
		if (dateRangeDataElementKey != null) {
			dateTimeFilter = new DateTimeFilter(
						new String[]{dateRangeDataElementKey},
						jobSpec.getEarliestDate(), AbsoluteTimeGranularity.DAY,
						jobSpec.getLatestDate(), AbsoluteTimeGranularity.DAY,
						jobSpec.getEarliestDateSide(), jobSpec.getLatestDateSide());
		} else {
			dateTimeFilter = null;
		}
		this.taskManager.queueTask(jobEntity.getId(),
				inJobRequest.getUserPropositions(),
				inJobRequest.getPropositionIdsToShow(),
				dateTimeFilter,
				jobSpec.isUpdateData(),
				prompts);
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
		DestinationEntity destination
				= this.destinationDao.getByName(job.getDestinationId());
		jobEntity.setDestination(destination);
		jobEntity.setCreated(new Date());
		jobEntity.setEtlUser(etlUser);
		this.jobDao.create(jobEntity);
		return jobEntity;
	}

	private Configuration toConfiguration(SourceConfig prompts) {
		if (prompts != null) {
			Configuration result = new Configuration();
			SourceConfig.Section[] dsbSections = prompts.getDataSourceBackends();
			List<BackendInstanceSpec<DataSourceBackend>> sections = new ArrayList<>();
			for (int i = 0; i < dsbSections.length; i++) {
				SourceConfig.Section section = dsbSections[i];
				try {
					BackendInstanceSpec<DataSourceBackend> bis = this.configurations.newDataSourceBackendSection(section.getId());
					SourceConfigOption[] options = section.getOptions();
					for (SourceConfigOption option : options) {
						bis.setProperty(option.getName(), option.getValue());
					}
					sections.add(bis);
				} catch (BackendSpecNotFoundException | BackendProviderSpecLoaderException | InvalidPropertyNameException | InvalidPropertyValueException ex) {
					throw new HttpStatusException(Status.BAD_REQUEST, ex);
				}
			}
			result.setDataSourceBackendSections(sections);
			return result;
		} else {
			return null;
		}
	}

}
