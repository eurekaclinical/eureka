package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CommUtils;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileWarning;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ApplicationProperties;
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
	 * The data access object used to update status information about the job.
	 */
	private final FileDao fileDao;
	/**
	 * The application level configuration.
	 */
	private final ApplicationProperties applicationProperties;
	/**
	 * The unique identifier for the file upload to process.
	 */
	private Long fileUploadId;
	/**
	 * The file upload to process.
	 */
	private FileUpload fileUpload;
	/**
	 * The current user's configuration.
	 */
	private Configuration configuration;

	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobSubmissionThread.class);

	/**
	 * Construct an instance with the given file upload, DAO, and related
	 * information.
	 * 
	 * @param inFileDao The DAO used to update status information about the file
	 *            upload.
	 * @param inApplicationProperties Application level configuration object,
	 *            used to fetch relevant URLs needed to communicate with the ETL
	 *            layer.
	 * @throws NoSuchAlgorithmException Thrown by the secure restful client.
	 * @throws KeyManagementException Thrown by the secure restful client.
	 */
	@Inject
	public JobSubmissionThread(FileDao inFileDao,
			ApplicationProperties inApplicationProperties)
			throws KeyManagementException, NoSuchAlgorithmException {
		super();
		this.fileDao = inFileDao;
		this.applicationProperties = inApplicationProperties;
	}

	/**
	 * @param inFileUploadId the fileUploadId to set
	 */
	public void setFileUploadId(Long inFileUploadId) {
		this.fileUploadId = inFileUploadId;
	}

	/*
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		this.fileUpload = this.fileDao.get(this.fileUploadId);
		if (this.fileUpload != null) {
			try {
				// first we make sure that the file is validated
				DataProvider dataProvider = new XlsxDataProvider(
						this.fileUpload);
				if (this.validateUpload(dataProvider)) {
					LOGGER.debug("Data file validated");
					this.fileUpload.setValidated(true);
					this.fileDao.save(this.fileUpload);
					// then we make sure the file is processed
					if (this.processUpload(dataProvider)) {
						LOGGER.debug("Data file processed");
						this.fileUpload.setProcessed(true);
						this.fileDao.save(this.fileUpload);
						// finally, we submit the job
						if (this.submitJob()) {
							LOGGER.debug("Job submitted");
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
				LOGGER.error(e.getMessage(), e);
				this.setCompleteWithError(e.getMessage());
			} catch (KeyManagementException e) {
				LOGGER.error(e.getMessage(), e);
				this.setCompleteWithError(e.getMessage());
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error(e.getMessage(), e);
				this.setCompleteWithError(e.getMessage());
			} catch (SQLException e) {
				LOGGER.error(e.getMessage(), e);
				this.setCompleteWithError(e.getMessage());
			}
		} else {
			LOGGER.error("run() called before setting a file upload");
			throw new IllegalStateException(
					"run() called before setting a file upload");
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
			for (ValidationEvent event : events) {
				if (event.isFatal()) {
					FileError error = new FileError();
					error.setLineNumber(event.getLine());
					error.setText(event.getMessage());
					error.setType(event.getType());
					error.setFileUpload(this.fileUpload);
					this.fileUpload.addError(error);
				} else {
					FileWarning warning = new FileWarning();
					warning.setLineNumber(event.getLine());
					warning.setText(event.getMessage());
					warning.setType(event.getType());
					warning.setFileUpload(this.fileUpload);
					this.fileUpload.addWarning(warning);
				}
			}
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
		DataInserter dataInserter = new DataInserter(
				this.getUserConfiguration());
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
		WebResource resource = client.resource(this.applicationProperties
				.getEtlJobSubmitUrl());
		Job job = new Job();
		job.setConfigurationId(this.getUserConfiguration().getId());
		job.setUserId(this.fileUpload.getUser().getId());
		Job resultJob = resource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).post(Job.class, job);
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
	 * @return The user configuration.
	 * 
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	private Configuration getUserConfiguration() throws KeyManagementException,
			NoSuchAlgorithmException {
		if (this.configuration == null) {
			Client client = CommUtils.getClient();
			WebResource resource = client.resource(this.applicationProperties
					.getEtlConfGetUrl()
					+ "/"
					+ this.fileUpload.getUser().getId());
			this.configuration = resource.accept(MediaType.APPLICATION_JSON)
					.get(Configuration.class);
		}
		return this.configuration;
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
