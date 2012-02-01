package edu.emory.cci.aiw.cvrg.eureka.services.resource;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileWarning;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.UserDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.DataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.DataProviderException;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.XlsxDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.datavalidator.DataValidator;
import edu.emory.cci.aiw.cvrg.eureka.services.datavalidator.ValidationEvent;
import edu.emory.cci.aiw.cvrg.eureka.services.job.JobCollection;

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
	 * Construct a new job resource with the given job update thread.
	 * 
	 * @param inUserDao The data access object used to fetch information about
	 *            users.
	 * @param inFileDao The data access object used to fetch and store
	 *            information about uploaded files.
	 */
	@Inject
	public JobResource(UserDao inUserDao, FileDao inFileDao) {
		this.userDao = inUserDao;
		this.fileDao = inFileDao;
	}

	/**
	 * Create a new job (by uploading a new file)
	 * 
	 * @param fileUpload The file upload to add.
	 * @return A {@link Status#CREATED} if the file is successfully added,
	 *         {@link Status#BAD_REQUEST} if there are errors.
	 */
	@Path("/upload")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces(MediaType.TEXT_PLAIN)
	public Response uploadFile(FileUpload fileUpload) {
		// start with the assumption that the resource will be created.
		Response response = Response.created(
				URI.create("/" + fileUpload.getId())).build();

		try {
			// first we validate the file upload.
			DataProvider dataProvider = new XlsxDataProvider(fileUpload);
			DataValidator dataValidator = new DataValidator();
			dataValidator.setPatients(dataProvider.getPatients())
					.setEncounters(dataProvider.getEncounters())
					.setProviders(dataProvider.getProviders())
					.setCpts(dataProvider.getCpts())
					.setIcd9Procedures(dataProvider.getIcd9Procedures())
					.setIcd9Diagnoses(dataProvider.getIcd9Diagnoses())
					.setMedications(dataProvider.getMedications())
					.setLabs(dataProvider.getLabs())
					.setVitals(dataProvider.getVitals()).validate();
			List<ValidationEvent> events = dataValidator.getValidationEvents();

			// if the validation caused any errors/warnings, we insert them into
			// our file upload object, and amend our response.
			if (events.size() > 0) {
				List<FileError> errors = new ArrayList<FileError>();
				List<FileWarning> warnings = new ArrayList<FileWarning>();
				for (ValidationEvent event : events) {
					if (event.isFatal()) {
						FileError error = new FileError();
						error.setLineNumber(event.getLine());
						error.setText(event.getMessage());
						error.setType(event.getType());
						error.setFileUpload(fileUpload);
						errors.add(error);
					} else {
						FileWarning warning = new FileWarning();
						warning.setLineNumber(event.getLine());
						warning.setText(event.getMessage());
						warning.setType(event.getType());
						warning.setFileUpload(fileUpload);
						warnings.add(warning);
					}
					fileUpload.setErrors(errors);
					fileUpload.setWarnings(warnings);
				}
				response = Response.status(Status.BAD_REQUEST).entity(events)
						.build();
			} else {
				// there are no errors, we send the job to the back end.
				// TODO: SEND THE JOB TO THE BACK END!
			}
		} catch (DataProviderException e) {
			response = Response.status(Status.BAD_REQUEST)
					.entity(e.getMessage()).build();
		}

		// now we save the file upload off to the data base, so we can fetch it
		// again if we need to process the data.
		User user = this.userDao.get(fileUpload.getUser().getId());
		fileUpload.setUser(user);
		this.fileDao.save(fileUpload);

		return response;
	}

	/**
	 * Get a list of jobs associated with the logged-in user.
	 * 
	 * @param securityContext The context that provides the user principal
	 *            currently logged in.
	 * 
	 * @return A list of {@link Job} objects associated with the user.
	 * @throws ServletException Thrown if the user ID is not valid
	 */
	@Path("/list")
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Job> getJobsByUser(@Context SecurityContext securityContext)
			throws ServletException {

		Principal userPrincipal = securityContext.getUserPrincipal();
		if (userPrincipal == null) {
			throw new ServletException("Bad User ID");
		}

		String name = userPrincipal.getName();
		User user = this.userDao.get(name);
		List<Job> allJobs = JobCollection.getJobs();
		List<Job> result = new ArrayList<Job>();
		for (Job job : allJobs) {
			if (job.getUserId() == user.getId()) {
				result.add(job);
			}
		}

		return result;
	}
}
