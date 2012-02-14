package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileWarning;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.DataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.DataProviderException;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.XlsxDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.datavalidator.DataValidator;
import edu.emory.cci.aiw.cvrg.eureka.services.datavalidator.ValidationEvent;
import edu.emory.cci.aiw.cvrg.eureka.services.jdbc.DataInserter;

/**
 * A thread to validate an uploaded file, and send a job request to the backend
 * when done.
 * 
 * @author hrathod
 * 
 */
public class JobSubmissionThread extends Thread {

	/**
	 * The file upload to process.
	 */
	private final FileUpload fileUpload;
	/**
	 * The data access object used to update status information about the job.
	 */
	private final FileDao fileDao;
	/**
	 * The URL used to get the user's configuration (used for data base login
	 * information).
	 */
	private final String configurationUrl;
	/**
	 * The URL used to submit the job.
	 */
	private final String jobUrl;
	/**
	 * The current user
	 */
	private final User user;
	/**
	 * The current user's configuration.
	 */
	private Configuration configuration;

	/**
	 * Construct an instance with the given file upload, DAO, and related
	 * information.
	 * 
	 * @param inFileUpload The file to process.
	 * @param inFileDao The DAO used to update status information about the file
	 *            upload.
	 * @param inConfUrl The URL used to find the user's configuration
	 *            information.
	 * @param inJobUrl The URL used to submit a new job to the back end.
	 * @throws NoSuchAlgorithmException Thrown by the secure restful client.
	 * @throws KeyManagementException Thrown by the secure restful client.
	 */
	public JobSubmissionThread(FileUpload inFileUpload, FileDao inFileDao,
			String inConfUrl, String inJobUrl) throws KeyManagementException,
			NoSuchAlgorithmException {
		super();
		this.fileUpload = inFileUpload;
		this.fileDao = inFileDao;
		this.configurationUrl = inConfUrl;
		this.jobUrl = inJobUrl;
		this.user = this.fileUpload.getUser();
		this.getUserConfiguration();
	}

	/*
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			// first we make sure that the file is validated
			DataProvider dataProvider = new XlsxDataProvider(this.fileUpload);
			if (this.validateUpload(dataProvider)) {
				this.fileUpload.setValidated(true);
				this.fileDao.save(this.fileUpload);
				// then we make sure the file is processed
				if (this.processUpload(dataProvider)) {
					this.fileUpload.setProcessed(true);
					this.fileDao.save(this.fileUpload);
					// finally, we submit the job
					if (this.submitJob()) {
						this.fileUpload.setCompleted(true);
						this.fileDao.save(this.fileUpload);
					} else {
						this.setCompleteWithError("Job could not be submitted properly");
					}
				} else {
					this.setCompleteWithError("Data could not be processed");
				}
			} else {
				this.setCompleteWithError("Invalid data file");
			}
		} catch (DataProviderException e) {
			e.printStackTrace();
			this.setCompleteWithError(e.getMessage());
		} catch (KeyManagementException e) {
			e.printStackTrace();
			this.setCompleteWithError(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			this.setCompleteWithError(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			this.setCompleteWithError(e.getMessage());
		}
	}

	/**
	 * Validate the data provided by the given data provider.
	 * 
	 * @param dataProvider Used to fetch the data to validate.
	 * @return True if the data is valid, false otherwise.
	 * @throws DataProviderException Thrown by the data provider if the data can
	 *             not be fetched properly.
	 */
	private boolean validateUpload(DataProvider dataProvider)
			throws DataProviderException {
		boolean result = true;
		DataValidator dataValidator = new DataValidator();
		dataValidator.setPatients(dataProvider.getPatients())
				.setEncounters(dataProvider.getEncounters())
				.setProviders(dataProvider.getProviders())
				.setCpts(dataProvider.getCptCodes())
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
					error.setFileUpload(this.fileUpload);
					errors.add(error);
				} else {
					FileWarning warning = new FileWarning();
					warning.setLineNumber(event.getLine());
					warning.setText(event.getMessage());
					warning.setType(event.getType());
					warning.setFileUpload(this.fileUpload);
					warnings.add(warning);
				}
			}
			this.fileUpload.setErrors(errors);
			this.fileUpload.setWarnings(warnings);
		}
		if (this.fileUpload.containsErrors()) {
			result = false;
		}
		return result;
	}

	/**
	 * Process the uploaded file into the data base, to be used by Protempa.
	 * 
	 * @param dataProvider The data provider to get the data from.
	 * @return True if the data is successfully processed into the database,
	 *         false otherwise.
	 * @throws KeyManagementException thrown by the secure restful client.
	 * @throws NoSuchAlgorithmException thrown by the secure restful client
	 * @throws SQLException Thrown by JDBC if any of the insert queries can not
	 *             be completed successfully.
	 */
	private boolean processUpload(DataProvider dataProvider)
			throws KeyManagementException, NoSuchAlgorithmException,
			SQLException {
		boolean result = true;
		DataInserter dataInserter = new DataInserter(this.configuration);
		dataInserter.insertPatients(dataProvider.getPatients());
		dataInserter.insertEncounters(dataProvider.getEncounters());
		dataInserter.insertProviders(dataProvider.getProviders());
		dataInserter.insertCptCodes(dataProvider.getCptCodes());
		dataInserter.insertIcd9Diagnoses(dataProvider.getIcd9Diagnoses());
		dataInserter.insertIcd9Procedures(dataProvider.getIcd9Procedures());
		dataInserter.insertLabs(dataProvider.getLabs());
		dataInserter.insertMedications(dataProvider.getMedications());
		dataInserter.insertVitals(dataProvider.getVitals());
		return result;
	}

	/**
	 * Submit a new job to the back-end after validating and processing the file
	 * upload.
	 * 
	 * @return True if the job is submitted successfully, false otherwise.
	 * @throws KeyManagementException Thrown by the secure client.
	 * @throws NoSuchAlgorithmException Thrown by the secure client.
	 */
	private boolean submitJob() throws KeyManagementException,
			NoSuchAlgorithmException {
		boolean result;
		Client client = CommUtils.getClient();
		WebResource resource = client.resource(this.jobUrl);
		Job job = new Job();
		job.setConfigurationId(this.configuration.getId());
		job.setUserId(this.user.getId());
		Job resultJob = resource.accept(MediaType.APPLICATION_JSON).post(
				Job.class, job);
		if (resultJob.getCurrentState().equals("CREATED")) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Get the configuration associated with the current user.
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	private void getUserConfiguration() throws KeyManagementException,
			NoSuchAlgorithmException {
		Client client = CommUtils.getClient();
		WebResource resource = client.resource(this.configurationUrl);
		this.configuration = resource.accept(MediaType.APPLICATION_JSON).get(
				Configuration.class);
	}

	/**
	 * Set the completed flag on the current file upload, with the given error
	 * message.
	 * 
	 * @param message The error message.
	 */
	private void setCompleteWithError(String message) {
		FileError error = new FileError();
		error.setType("job processing");
		error.setText(message);
		error.setLineNumber(Long.valueOf(0));
		error.setFileUpload(this.fileUpload);

		this.fileUpload.addError(error);
		this.fileUpload.setCompleted(true);
		this.fileDao.save(this.fileUpload);
	}

}
