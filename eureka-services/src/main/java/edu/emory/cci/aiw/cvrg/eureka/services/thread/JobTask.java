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
package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import java.util.Date;
import java.util.List;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.EtlClient;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileError;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileWarning;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.FileDao;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.DataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.DataProviderException;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.XlsxDataProvider;
import edu.emory.cci.aiw.cvrg.eureka.services.datavalidator.DataValidator;
import edu.emory.cci.aiw.cvrg.eureka.services.datavalidator.ValidationEvent;
import edu.emory.cci.aiw.cvrg.eureka.services.jdbc.DataInserter;
import edu.emory.cci.aiw.cvrg.eureka.services.jdbc.DataInserterException;

/**
 * Create a new task to perform validation on an uploaded file, and submit a job
 * to the ETL layer.
 *
 * @author hrathod
 *
 */
public class JobTask implements Runnable {

	/**
	 * The data access object used to update status information about the job.
	 */
	private final FileDao fileDao;
	/**
	 * The application level configuration.
	 */
	private final ServiceProperties serviceProperties;
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
	 * The propositions belonging to the user.
	 */
	private List<PropositionDefinition> propositions;
	/**
	 * The user-created propositions.
	 */
	private List<PropositionDefinition> userPropositions;
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JobTask.class);

	/**
	 * Construct an instance with the given file upload, DAO, and related
	 * information.
	 *
	 * @param inFileDao               The DAO used to update status information
	 *                                   about the file upload.
	 * @param inServiceProperties Application level configuration object,
	 *                                   used to fetch relevant URLs needed to
	 *                                   communicate with the ETL layer.
	 */
	@Inject
	public JobTask(FileDao inFileDao,
			ServiceProperties inServiceProperties) {
		super();
		this.fileDao = inFileDao;
		this.serviceProperties = inServiceProperties;
	}

	/**
	 * @param inFileUploadId the fileUploadId to set
	 */
	public void setFileUploadId(Long inFileUploadId) {
		this.fileUploadId = inFileUploadId;
	}

	/**
	 * Sets the propositions to add to the task.
	 * @param inPropositions The list of propositions to add to the task.
	 */
	public void setPropositions (List<PropositionDefinition> inPropositions) {
		this.propositions = inPropositions;
	}

	/**
	 * Sets the user-created propositions to add to the task.
	 * @param inPropositions The list of user-created propositions to add to the task
	 */
	public void setUserPropositions(List<PropositionDefinition> inPropositions) {
		this.userPropositions = inPropositions;
	}

	/*
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		this.fileUpload = this.fileDao.retrieve(this.fileUploadId);
		if (this.fileUpload != null) {
			try {
				// first we make sure that the file is validated
				DataProvider dataProvider = new XlsxDataProvider(
						this.fileUpload);
				this.validateUpload(dataProvider);
				LOGGER.debug("Data file validated: {}",
						this.fileUpload.getLocation());
				this.fileUpload.setValidated(true);
				this.fileUpload.setTimestamp(new Date());
				this.fileDao.update(this.fileUpload);

				// then we make sure the file is processed
				this.processUpload(dataProvider);
				LOGGER.debug("Data file processed");
				this.fileUpload.setProcessed(true);
				this.fileUpload.setTimestamp(new Date());
				this.fileDao.update(this.fileUpload);

				// finally, we submit the job
				this.submitJob();
				LOGGER.debug("Job submitted");
				this.fileUpload.setCompleted(true);
				this.fileUpload.setTimestamp(new Date());
				this.fileDao.update(this.fileUpload);

			} catch (DataProviderException e) {
				LOGGER.error(e.getMessage(), e);
				this.setCompleteWithError(e.getMessage());
			} catch (TaskException e) {
				LOGGER.error(e.getMessage(), e);
				this.setCompleteWithError(e.getMessage());
			}
		} else {
			LOGGER.error("run() called before setting a file upload");
		}
	}

	/**
	 * Validate the data provided by the given data provider.
	 *
	 * @param dataProvider Used to fetch the data to validate.
	 * @throws TaskException Thrown if there are any errors while validating the
	 *                          data.
	 */
	private void validateUpload(DataProvider dataProvider) throws TaskException {
		DataValidator dataValidator = new DataValidator();
		try {
			dataValidator.setPatients(dataProvider.getPatients()).setEncounters(dataProvider.
					getEncounters()).setProviders(dataProvider.getProviders()).
					setCpts(dataProvider.getCptCodes()).setIcd9Procedures(dataProvider.
					getIcd9Procedures()).setIcd9Diagnoses(dataProvider.
					getIcd9Diagnoses()).setMedications(dataProvider.getMedications()).
					setLabs(dataProvider.getLabs()).setVitals(dataProvider.getVitals()).
					validate();
		} catch (DataProviderException e) {
			throw new TaskException(e);
		}
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
			throw new TaskException("Invalid data file.");
		}
	}

	/**
	 * Process the uploaded file into the data base, to be used by Protempa.
	 *
	 * @param dataProvider The data provider to get the data from.
	 * @throws TaskException Thrown if there are errors while fetching the user
	 *                          configuration, or while inserting data into the
	 *                          data source.
	 */
	private void processUpload(DataProvider dataProvider) throws TaskException {
		Configuration conf = this.getUserConfiguration();
		if (conf != null) {
			try {
				DataInserter dataInserter = new DataInserter(conf);
				dataInserter.insertPatients(dataProvider.getPatients());
				dataInserter.insertEncounters(dataProvider.getEncounters());
				dataInserter.insertProviders(dataProvider.getProviders());
				dataInserter.insertCptCodes(dataProvider.getCptCodes());
				dataInserter.insertIcd9Diagnoses(dataProvider.getIcd9Diagnoses());
				dataInserter.insertIcd9Procedures(dataProvider.getIcd9Procedures());
				dataInserter.insertLabs(dataProvider.getLabs());
				dataInserter.insertMedications(dataProvider.getMedications());
				dataInserter.insertVitals(dataProvider.getVitals());
			} catch (DataInserterException e) {
				throw new TaskException(e);
			} catch (DataProviderException e) {
				throw new TaskException(e);
			}
		} else {
			throw new TaskException("Received null configuration!");
		}
	}

	/**
	 * Submit a new job to the back-end after validating and processing the file
	 * upload.
	 *
	 * @throws TaskException Thrown if there are any errors in submitting the
	 *                          job to the ETL layer.
	 *
	 */
	private void submitJob() throws TaskException {

		Configuration conf = this.getUserConfiguration();
		JobRequest jobRequest = new JobRequest();
		Job job = new Job();
		job.setConfigurationId(conf.getId());
		job.setUserId(this.fileUpload.getUserId());
		jobRequest.setJob(job);
		jobRequest.setPropositions(this.propositions);
		jobRequest.setUserPropositions(this.userPropositions);

		EtlClient etlClient = new EtlClient(this.serviceProperties.getEtlUrl
				());
		try {
			etlClient.submitJob(jobRequest);
			LOGGER.info("Job successfully submitted to ETL layer.");
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Get the configuration associated with the current user.
	 *
	 * @return The user configuration.
	 *
	 */
	private Configuration getUserConfiguration() {
		if (this.configuration == null) {
			Long userId = this.fileUpload.getUserId();
			String url = this.serviceProperties.getEtlUrl();
			EtlClient etlClient = new EtlClient(url);
			try {
				this.configuration = etlClient.getConfiguration(userId);
			} catch (ClientException e) {
				LOGGER.error(e.getMessage(), e);
			}
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
		this.fileUpload.setTimestamp(new Date());

		this.fileDao.update(this.fileUpload);
	}
}
