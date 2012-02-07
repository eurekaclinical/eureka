package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ApplicationProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;
import edu.emory.cci.aiw.cvrg.eureka.services.thread.JobSubmissionThread;

/**
 * REST operations related to jobs submitted by the user.
 * 
 * @author hrathod
 * 
 */
@Path("/job")
public class JobResource {

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
	 * Application's configuration properties holder.
	 */
	private final ApplicationProperties applicationProperties;

	/**
	 * Construct a new job resource with the given job update thread.
	 * 
	 * @param inUserDao The data access object used to fetch information about
	 *            users.
	 * @param inFileDao The data access object used to fetch and store
	 *            information about uploaded files.
	 * @param inApplicationProperties The configuration object holding all the
	 *            appliction's defined properties.
	 */
	@Inject
	public JobResource(UserDao inUserDao, FileDao inFileDao,
			ApplicationProperties inApplicationProperties) {
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
		this.applicationProperties = inApplicationProperties;
	}

	/**
	 * Create a new job (by uploading a new file)
	 * 
	 * @param inFileUpload The file upload to add.
	 * 
	 * @return A {@link Status#OK} if the file is successfully added,
	 *         {@link Status#BAD_REQUEST} if there are errors.
	 * @throws ServletException Thrown when the secure client used to connect to
	 *             the back end services can not be initialized or set up
	 *             properly.
	 */
	@Path("/add")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(FileUpload inFileUpload) throws ServletException {
		String configUrl = this.applicationProperties.getBackendConfigUrl();
		String jobSubmitUrl = this.applicationProperties.getBackendSubmitUrl();

		FileUpload fileUpload = inFileUpload;
		Long userId = inFileUpload.getUser().getId();
		fileUpload.setUser(this.userDao.get(userId));
		this.fileDao.save(fileUpload);

		configUrl += "/" + fileUpload.getUser().getId();

		try {
			JobSubmissionThread jobSubmissionThread = new JobSubmissionThread(
					fileUpload, this.fileDao, configUrl, jobSubmitUrl);
			jobSubmissionThread.start();
		} catch (KeyManagementException e) {
			throw new ServletException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException(e);
		}
		return Response.ok().build();
	}

	/**
	 * Get a list of jobs associated with user referred to by the given unique
	 * identifier.
	 * 
	 * @param inId The unique identifier for the user.
	 * 
	 * @return A list of {@link Job} objects associated with the user.
	 * @throws ServletException Thrown if the user ID is not valid
	 */
	@Path("/list/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Job> getJobsByUser(@PathParam("id") final String inId)
			throws ServletException {
		User user = this.userDao.get(inId);
		List<Job> allJobs = JobCollection.getJobs();
		List<Job> result = new ArrayList<Job>();
		for (Job job : allJobs) {
			if (job.getUserId() == user.getId()) {
				result.add(job);
			}
		}
		return result;
	}

	/**
	 * Get the status of a job process for the given user.s
	 * 
	 * @param inId The unique identifier of the user to query for.
	 * @return A {@link JobInfo} object containing the status information.
	 * @throws ServletException Thrown if there are errors converting the input
	 *             string to a valid user ID.
	 */
	@Path("/status/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public JobInfo getStatus(@PathParam("id") final String inId)
			throws ServletException {

		Job latestJob = null;
		FileUpload latestFileUpload = null;
		Long userId;
		try {
			userId = Long.valueOf(inId);
		} catch (NumberFormatException nfe) {
			throw new ServletException(nfe);
		}

		List<Job> userJobs = this.getJobsByUser(inId);
		if (userJobs.size() > 0) {
			Date latestDate = null;
			for (Job job : userJobs) {
				if (latestJob == null) {
					latestJob = job;
				} else {
					if (job.getTimestamp().after(latestDate)) {
						latestJob = job;
					}
				}
			}
		} else {
			Date latestDate = null;
			List<FileUpload> fileUploads = this.fileDao.getByUserId(userId);
			for (FileUpload fileUpload : fileUploads) {
				if (latestFileUpload == null) {
					latestFileUpload = fileUpload;
					latestDate = fileUpload.getTimestamp();
				} else {
					if (fileUpload.getTimestamp().after(latestDate)) {
						latestFileUpload = fileUpload;
					}
				}
			}
		}

		return new JobInfo(latestFileUpload, latestJob);
	}
}
