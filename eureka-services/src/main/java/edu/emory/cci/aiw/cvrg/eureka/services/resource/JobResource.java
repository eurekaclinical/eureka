/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionCollector;
import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.services.conversion.PropositionDefinitionConverterVisitor;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;
import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobExecutor;
import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * REST operations related to jobs submitted by the user.
 *
 * @author hrathod
 *
 */
@Path("/job")
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
	 * The data access object used to work with file upload objects in the data
	 * store.
	 */
	private final FileDao fileDao;
	/**
	 * Used to fetch the user's Propositions, to be sent to the ETL layer when
	 * submitting a new job request.
	 */
	private final PropositionDao propositionDao;
	/**
	 * Used for converting the different types of Eureka data entities to
	 * Protempa proposition definitions.
	 */
	private final PropositionDefinitionConverterVisitor converterVisitor;
	/**
	 * The runnable used to run the data processing and job submission tasks.
	 */
	private final JobTask jobTask;
	/**
	 * The executor service used to run the job tasks.
	 */
	private final JobExecutor jobExecutor;

	/**
	 * Construct a new job resource with the given job update thread.
	 *
	 * @param inUserDao The data access object used to fetch information about
	 * users.
	 * @param inFileDao The data access object used to fetch and store
	 * information about uploaded files.
	 * @param inJobTask The job submission runnable to be used to process
	 * incoming data and submit jobs to the ETL layer.
	 * @param inJobExecutor The executor service used to run the job tasks.
	 */
	@Inject
	public JobResource(UserDao inUserDao, FileDao inFileDao,
			PropositionDefinitionConverterVisitor inVisitor,
			PropositionDao inPropositionDao,
			JobTask inJobTask, JobExecutor inJobExecutor) {
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
		this.propositionDao = inPropositionDao;
		this.converterVisitor = inVisitor;
		this.jobTask = inJobTask;
		this.jobExecutor = inJobExecutor;
	}

	/**
	 * Create a new job (by uploading a new file).
	 *
	 * @param inFileUpload The file upload to add.
	 *
	 * @return A {@link Response.Status#OK} if the file is successfully added,
	 * {@link Response.Status#BAD_REQUEST} if there are errors.
	 */
	@Path("/add")
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response uploadFile(
			edu.emory.cci.aiw.cvrg.eureka.common.comm.FileUpload inFileUpload) {
		LOGGER.debug("Got file upload: {}", inFileUpload);
		FileUpload fileUpload = new FileUpload();
		fileUpload.setTimestamp(new Date());
		fileUpload.setUserId(inFileUpload.getUserId());
		fileUpload.setLocation(inFileUpload.getLocation());
		this.fileDao.create(fileUpload);
		this.jobTask.setFileUploadId(fileUpload.getId());
		this.converterVisitor.setUserId(fileUpload.getUserId());
		PropositionDefinitionCollector collector =
				PropositionDefinitionCollector.getInstance(
				this.converterVisitor, this.propositionDao
				.getByUserId(fileUpload.getUserId()));
		this.jobTask.setUserPropositions(collector.getUserPropDefs());
		this.jobTask.setNonHelperPropositionIds(collector.getToShowPropDefs());
		this.jobExecutor.queueJob(this.jobTask);

		return Response.ok().build();
	}

	/**
	 * Get a list of jobs associated with user referred to by the given unique
	 * identifier.
	 *
	 * @param userId The unique identifier for the user.
	 *
	 * @return A list of {@link Job} objects associated with the user.
	 */
	@Path("/list/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Job> getJobsByUser(@PathParam("id") final Long userId) {
		User user = this.userDao.retrieve(userId);
		List<Job> allJobs = JobCollection.getJobs();
		List<Job> result = new ArrayList<Job>();
		for (Job job : allJobs) {
			if (job.getUserId().equals(user.getId())) {
				result.add(job);
			}
		}
		return result;
	}

	/**
	 * Get the status of the most recent job process for the given user.
	 *
	 * @param userId The unique identifier of the user to query for.
	 * @return A {@link JobInfo} object containing the status information.
	 */
	@Path("/status/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public JobInfo getStatus(@PathParam("id") final Long userId) {
		FileUpload latestFileUpload = null;

		List<FileUpload> fileUploads = this.fileDao.getByUserId(userId);
		for (FileUpload fileUpload : fileUploads) {
			this.fileDao.refresh(fileUpload);
			if (latestFileUpload == null) {
				latestFileUpload = fileUpload;
			} else {
				if (fileUpload.getTimestamp().after(
						latestFileUpload.getTimestamp())) {
					latestFileUpload = fileUpload;
				}
			}
		}

		if (latestFileUpload == null) {
			throw new HttpStatusException(Response.Status.NOT_FOUND,
					"No files uploaded by user " + userId);
		}

		JobInfo jobInfo = new JobInfo();
		jobInfo.setFileUpload(latestFileUpload);

		if (latestFileUpload.isCompleted()) {
			Job latestJob = null;
			List<Job> userJobs = this.getJobsByUser(userId);
			for (Job job : userJobs) {
				if (latestJob == null) {
					latestJob = job;
				} else {
					if (job.getTimestamp().after(latestJob.getTimestamp())) {
						latestJob = job;
					}
				}
			}
			jobInfo.setJob(latestJob);
		}
		LOGGER.debug(
				"Returning job status for user id {}: {}/{}",
				new Object[]{userId,
					Integer.valueOf(jobInfo.getCurrentStep()),
					Integer.valueOf(jobInfo.getTotalSteps())});
		return jobInfo;
	}
}
