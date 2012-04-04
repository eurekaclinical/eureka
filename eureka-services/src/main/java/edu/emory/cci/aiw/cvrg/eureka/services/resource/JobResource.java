package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;
import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobExecutor;
import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobTask;

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
	 *            users.
	 * @param inFileDao The data access object used to fetch and store
	 *            information about uploaded files.
	 * @param inJobTask The job submission runnable to be used to process
	 *            incoming data and submit jobs to the ETL layer.
	 * @param inJobExecutor The executor service used to run the job tasks.
	 */
	@Inject
	public JobResource(UserDao inUserDao, FileDao inFileDao, JobTask inJobTask,
			JobExecutor inJobExecutor) {
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
		this.jobTask = inJobTask;
		this.jobExecutor = inJobExecutor;
	}

	/**
	 * Create a new job (by uploading a new file)
	 * 
	 * @param inFileUpload The file upload to add.
	 * 
	 * @return A {@link Status#OK} if the file is successfully added,
	 *         {@link Status#BAD_REQUEST} if there are errors.
	 */
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response uploadFile(FileUpload inFileUpload) {
		LOGGER.debug("Got file upload: {}", inFileUpload);
		FileUpload fileUpload = inFileUpload;
		Long userId = inFileUpload.getUser().getId();
		fileUpload.setUser(this.userDao.getById(userId));
		fileUpload.setTimestamp(new Date());
		this.fileDao.save(fileUpload);
		this.jobTask.setFileUploadId(fileUpload.getId());
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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Job> getJobsByUser(@PathParam("id") final Long userId) {
		User user = this.userDao.getById(userId);
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
	 * Get the status of a job process for the given user.
	 * 
	 * @param userId The unique identifier of the user to query for.
	 * @return A {@link JobInfo} object containing the status information.
	 */
	@Path("/status/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public JobInfo getStatus(@PathParam("id") final Long userId) {

		Job latestJob = null;
		FileUpload latestFileUpload = null;

		List<Job> userJobs = this.getJobsByUser(userId);
		if (userJobs.size() > 0) {
			for (Job job : userJobs) {
				if (latestJob == null) {
					latestJob = job;
				} else {
					if (job.getTimestamp().after(latestJob.getTimestamp())) {
						latestJob = job;
					}
				}
			}
		}
		List<FileUpload> fileUploads = this.fileDao.getByUserId(userId);
		if (fileUploads.size() > 0) {
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
		}

		JobInfo jobInfo = new JobInfo();
		jobInfo.setFileUpload(latestFileUpload);
		jobInfo.setJob(latestJob);
		LOGGER.debug(
				"Returning job status for user id {}: {}/{}",
				new Object[] { userId,
						Integer.valueOf(jobInfo.getCurrentStep()),
						Integer.valueOf(jobInfo.getTotalSteps()) });
		return jobInfo;
	}
}
