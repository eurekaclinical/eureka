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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.sun.jersey.api.client.ClientResponse;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.JobFilter;
import org.eurekaclinical.eureka.client.comm.JobSpec;
import org.eurekaclinical.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.UserEntity;

import org.eurekaclinical.protempa.client.EurekaClinicalProtempaClient;
import org.eurekaclinical.protempa.client.comm.JobRequest;

import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionSupport;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionCollector;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PhenotypeEntityDao;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 * REST operations related to jobs submitted by the user.
 *
 * @author hrathod
 *
 */
@Path("/protected/jobs")
@RolesAllowed({"researcher"})
public class JobResource {

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobResource.class);
	/**
	 * The User data access object to retrieve information about the current
	 * user.
	 */
	private final UserDao userDao;
	/**
	 * Used to fetch the user's Propositions, to be sent to the ETL layer when
	 * submitting a new job request.
	 */
	private final PhenotypeEntityDao propositionDao;
	/**
	 * Used for converting the different types of Eureka data entities to
	 * Protempa proposition definitions.
	 */
	private final PropositionDefinitionConverterVisitor converterVisitor;
	private final EurekaClinicalProtempaClient etlClient;
	private final ConversionSupport conversionSupport;
	private final ServiceProperties properties;

	/**
	 * Construct a new job resource with the given job update thread.
	 *
	 * @param inUserDao The data access object used to fetch information about
	 * users.
	 * @param inVisitor The proposition definition converter visitor that will
	 * be used to determine how to convert proposition definitions
	 * @param inPropositionDao The data access object used to fetch information
	 * about propositions.
	 * @param inEtlClient The ETL client to use to perform ETL operations.
	 */
	@Inject
	public JobResource(UserDao inUserDao,
			PropositionDefinitionConverterVisitor inVisitor,
			PhenotypeEntityDao inPropositionDao,
			EurekaClinicalProtempaClient inEtlClient,
			ServiceProperties inProperties) {
		this.userDao = inUserDao;
		this.propositionDao = inPropositionDao;
		this.converterVisitor = inVisitor;
		this.etlClient = inEtlClient;
		this.conversionSupport = new ConversionSupport();
		this.properties = inProperties;
	}

	/**
	 * Create a new job (by uploading a new file).
	 *
	 * @param jobSpec The file upload to add.
	 *
	 * @return A {@link javax.ws.rs.core.Response} indicating the result of the
	 * operation.
	 */
	@Transactional
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public Response submit(@Context HttpServletRequest request, JobSpec jobSpec) {
		LOGGER.debug("Got job submission: {}", jobSpec);
		UserEntity user = this.userDao.getByHttpServletRequest(request);
		JobRequest jobRequest = new JobRequest();
		PropositionDefinitionCollector collector
				= PropositionDefinitionCollector.getInstance(
						this.converterVisitor, this.propositionDao
						.getByUserId(user.getId()));
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Sending {} proposition definitions:", collector.getUserPropDefs().size());
			for (PropositionDefinition pd : collector.getUserPropDefs()) {
				LOGGER.trace("PropDef: {}", pd);
			}
		}

		jobRequest.setJobSpec(jobSpec);
		jobRequest.setUserPropositions(collector.getUserPropDefs());
		List<String> conceptIds = jobSpec.getPropositionIds();
		List<String> propIds = new ArrayList<>(conceptIds != null ? conceptIds.size() : 0);
		if (conceptIds != null) {
			for (String conceptId : conceptIds) {
				propIds.add(this.conversionSupport.toPropositionId(conceptId));
			}
		}
		jobRequest.setPropositionIdsToShow(propIds);

		Long jobId;
		try {
			jobId = this.etlClient.submitJob(jobRequest);
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}

		return Response.created(URI.create("/" + jobId)).build();
	}

	@GET
	@Path("/{jobId}")
	@Produces({MediaType.APPLICATION_JSON})
	public Job getJob(@PathParam("jobId") Long inJobId) {
		try {
			return this.etlClient.getJob(inJobId);
		} catch (ClientException ex) {
			ClientResponse.Status responseStatus = ex.getResponseStatus();
			if (responseStatus == ClientResponse.Status.NOT_FOUND) {
				throw new HttpStatusException(Status.NOT_FOUND);
			} else {
				throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
			}
		}
	}

	@GET
	@Path("/{jobId}/stats/{key}")
	@Produces({MediaType.APPLICATION_JSON})
	public org.eurekaclinical.eureka.client.comm.Statistics getJobStats(@Context HttpServletRequest request,
			@PathParam("jobId") Long inJobId, @PathParam("key") String key) {
		try {
			org.eurekaclinical.eureka.client.comm.Statistics stats = this.etlClient.getJobStats(inJobId, key != null ? this.conversionSupport.toPropositionId(key) : null);
			org.eurekaclinical.eureka.client.comm.Statistics convertedStats = new org.eurekaclinical.eureka.client.comm.Statistics();

			Map<String, String> childrenToParents = stats.getChildrenToParents();
			Map<String, String> convertedChildrenToParents = new HashMap<>();
			for (Map.Entry<String, String> me : childrenToParents.entrySet()) {
				String phenotypeKey = this.conversionSupport.toPhenotypeKey(me.getKey());
				String parentKey = me.getValue();
				if (phenotypeKey != null) {
					convertedChildrenToParents.put(phenotypeKey, parentKey != null ? this.conversionSupport.toPhenotypeKey(parentKey) : null);
				}
			}
			convertedStats.setChildrenToParents(convertedChildrenToParents);

			Map<String, Integer> counts = stats.getCounts();
			Map<String, Integer> convertedCounts = new HashMap<>();
			for (Map.Entry<String, Integer> me : counts.entrySet()) {
				String phenotypeKey = this.conversionSupport.toPhenotypeKey(me.getKey());
				if (phenotypeKey != null) {
					convertedCounts.put(phenotypeKey, me.getValue());
				}
			}
			convertedStats.setCounts(convertedCounts);
			return convertedStats;
		} catch (ClientException ex) {
			ClientResponse.Status responseStatus = ex.getResponseStatus();
			if (responseStatus == ClientResponse.Status.NOT_FOUND) {
				throw new HttpStatusException(Status.NOT_FOUND);
			} else {
				throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
			}
		}
	}

	@GET
	@Path("/{jobId}/stats")
	@Produces({MediaType.APPLICATION_JSON})
	public org.eurekaclinical.eureka.client.comm.Statistics getJobStatsRoot(@Context HttpServletRequest request,
			@PathParam("jobId") Long inJobId) {
		return getJobStats(request, inJobId, null);
	}

	/**
	 * Get a list of jobs associated with user referred to by the given unique
	 * identifier.
	 *
	 * @param order The order in which to get the user's jobs.
	 *
	 * @return A list of {@link Job} objects associated with the user.
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<Job> getJobsByUser(@QueryParam("order") String order) {
		try {
			if (order == null) {
				return this.etlClient.getJobs();
			} else if (order.equals("desc")) {
				return this.etlClient.getJobsDesc();
			} else {
				throw new HttpStatusException(Status.PRECONDITION_FAILED,
						"Invalid value for the order query parameter: " + order);
			}
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	/**
	 * Get the status of the most recent job process for the given user.
	 *
	 * @param inFilter The filter to use when fetching the job statuses.
	 * @return A {@link List} of {@link Job}s containing the status information.
	 */
	@GET
	@Path("/status")
	@RolesAllowed({"admin"})
	@Produces({MediaType.APPLICATION_JSON})
	public List<Job> getStatus(@QueryParam("filter") JobFilter inFilter) {
		try {
			return this.etlClient.getJobStatus(inFilter);
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	@GET
	@Path("/latest")
	@Produces({MediaType.APPLICATION_JSON})
	public List<Job> getLatestJob() {
		try {
			return this.etlClient.getLatestJob();
		} catch (ClientException ex) {
			throw new HttpStatusException(Status.INTERNAL_SERVER_ERROR, ex);
		}
	}
}
