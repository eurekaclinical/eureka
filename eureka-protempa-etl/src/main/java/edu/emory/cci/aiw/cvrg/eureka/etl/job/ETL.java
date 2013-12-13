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
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventType;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler.EurekaQueryResultsHandlerFactory;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.Destinations;
import org.protempa.CloseException;
import org.protempa.DataSourceFailedDataValidationException;
import org.protempa.DataSourceValidationIncompleteException;
import org.protempa.FinderException;
import org.protempa.PropositionDefinition;
import org.protempa.Protempa;
import org.protempa.ProtempaStartupException;
import org.protempa.SourceFactory;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.ConfigurationsNotFoundException;
import org.protempa.backend.InvalidConfigurationException;
import org.protempa.backend.dsb.DataValidationEvent;
import org.protempa.backend.dsb.filter.Filter;
import org.protempa.query.DefaultQueryBuilder;
import org.protempa.query.Query;
import org.protempa.query.QueryBuildException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.protempa.query.handler.QueryResultsHandlerFactory;

/**
 * This class actually runs Protempa.
 * <p/>
 * There are two configuration files for each Protempa job. One, an INI file,
 * configures Protempa. Another, an XML file, configures the i2b2 query results
 * handler. Each Protempa configuration (an INI file) has associated with it one
 * i2b2 query results handler configuration file (an XML file). They are
 * associated by name. An INI file named my_config.ini has an associated XML
 * file my_config.xml. The INI and XML files go into a directory specified in
 * this class' constructor. The default is
 * <code>/etc/eureka/etlconfig</code>.
 *
 * @author Andrew Post
 */
public class ETL {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETL.class);
	private final EtlProperties etlProperties;
	private PreventCloseKnowledgeSource knowledgeSource;
	private PreventCloseDataSource dataSource;
	private PreventCloseAlgorithmSource algorithmSource;
	private PreventCloseTermSource termSource;
	private final JobDao jobDao;
	private final DestinationDao destinationDao;

	@Inject
	public ETL(EtlProperties inEtlProperties, JobDao inJobDao, DestinationDao inDestinationDao) {
		this.etlProperties = inEtlProperties;
		this.jobDao = inJobDao;
		this.destinationDao = inDestinationDao;
	}

	void run(JobEntity job, PropositionDefinition[] inPropositionDefinitions,
			String[] inPropIdsToShow, Filter filter) throws EtlException {
		assert inPropositionDefinitions != null :
				"inPropositionDefinitions cannot be null";
		assert job != null : "job cannot be null";
		Protempa protempa = null;
		try {
			protempa = getNewProtempa(job);
			logValidationEvents(job, protempa.validateDataSourceBackendData(), null);
			DefaultQueryBuilder q = new DefaultQueryBuilder();
			q.setPropositionDefinitions(inPropositionDefinitions);
			q.setPropositionIds(inPropIdsToShow);
			q.setId(job.getId().toString());
			q.setFilters(filter);
			LOGGER.debug("Constructed Protempa query " + q);
			Query query = protempa.buildQuery(q);
			File i2b2Config = 
					this.etlProperties.destinationConfigFile(
					job.getDestinationId());
			Destination dest = 
					new Destinations(this.etlProperties, job.getEtlUser(), this.destinationDao).getOne(job.getDestinationId());
			QueryResultsHandlerFactory qrh = 
					new EurekaQueryResultsHandlerFactory()
					.getInstance(dest.getType(), i2b2Config);
			protempa.execute(query, qrh);
			protempa.close();
			protempa = null;
		} catch (CloseException | BackendNewInstanceException | BackendInitializationException | ConfigurationsLoadException | BackendProviderSpecLoaderException | QueryBuildException | InvalidConfigurationException | ConfigurationsNotFoundException | DataSourceValidationIncompleteException ex) {
			throw new EtlException(ex);
		} catch (DataSourceFailedDataValidationException ex) {
			logValidationEvents(job, ex.getValidationEvents(), ex);
			throw new EtlException(ex);
		} catch (FinderException e) {
			String msg = collectThrowableMessages(e.getCause());
			throw new EtlException(msg, e);
		} catch (ProtempaStartupException e) {
			Throwable cause = e.getCause();
			String msg = collectThrowableMessages(cause != null ? cause : e);
			throw new EtlException(msg, e);
		} finally {
			if (protempa != null) {
				try {
					protempa.close();
				} catch (CloseException ignore) {
				}
			}
		}
	}

	void close() {
		if (this.knowledgeSource != null) {
			this.knowledgeSource.reallyClose();
		}
		if (this.dataSource != null) {
			this.dataSource.reallyClose();
		}
		if (this.algorithmSource != null) {
			this.algorithmSource.reallyClose();
		}
		if (this.termSource != null) {
			this.termSource.reallyClose();
		}
	}

	private void logValidationEvents(JobEntity job, DataValidationEvent[] events, DataSourceFailedDataValidationException ex) {
		// if the validation caused any errors/warnings, we insert them into
		// our file upload object, and amend our response.
		List<JobEvent> jobEvents = new ArrayList<>();
		for (DataValidationEvent event : events) {
			JobEvent jobEvent = new JobEvent();
			jobEvent.setJob(job);
			jobEvent.setTimeStamp(event.getTimestamp());
			AbstractFileInfo fileInfo;
			if (event.isFatal()) {
				fileInfo = new FileError();
				jobEvent.setState(JobEventType.ERROR);
			} else {
				fileInfo = new FileWarning();
				jobEvent.setState(JobEventType.WARNING);
			}
			fileInfo.setLineNumber(event.getLine());
			fileInfo.setText(event.getMessage());
			fileInfo.setType(event.getType());
			fileInfo.setURI(event.getURI());
			jobEvent.setMessage(fileInfo.toUserMessage());
			jobEvent.setExceptionStackTrace(collectThrowableMessages(ex));
		}
		job.setJobEvents(jobEvents);
		this.jobDao.update(job);
	}

	private Protempa getNewProtempa(JobEntity job) throws
			ConfigurationsLoadException, BackendProviderSpecLoaderException,
			InvalidConfigurationException, ProtempaStartupException,
			BackendInitializationException, BackendNewInstanceException, ConfigurationsNotFoundException {
		Configurations configurations =
				new EurekaProtempaConfigurations(this.etlProperties);
		SourceFactory sf =
				new SourceFactory(configurations, job.getSourceConfigId());
		return Protempa.newInstance(sf);
	}

	private static String collectThrowableMessages(Throwable throwable) {
		String msg = throwable.getMessage();
		Throwable cause = throwable.getCause();
		if (cause != null) {
			msg += ": " + cause.getMessage();
		}
		return msg;
	}
}
