/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import org.protempa.DataSourceFailedDataValidationException;
import org.protempa.PropositionDefinition;
import org.protempa.Protempa;
import org.protempa.ProtempaStartupException;
import org.protempa.SourceFactory;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.ConfigurationsNotFoundException;
import org.protempa.backend.dsb.DataValidationEvent;
import org.protempa.backend.dsb.filter.Filter;
import org.protempa.query.DefaultQueryBuilder;
import org.protempa.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEvent;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobStatus;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dest.ProtempaDestinationFactory;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.Destinations;
import java.io.IOException;
import org.protempa.backend.Configuration;
import org.protempa.backend.InvalidPropertyNameException;
import org.protempa.backend.InvalidPropertyValueException;
import org.protempa.query.QueryMode;

/**
 * This class actually runs Protempa.
 * <p/>
 * There are two configuration files for each Protempa job. One, an INI file,
 * configures Protempa. Another, an XML file, configures the i2b2 query results
 * handler. Each Protempa configuration (an INI file) has associated with it one
 * i2b2 query results handler configuration file (an XML file). They are
 * associated by name. An INI file named my_config.ini has an associated XML
 * file my_config.xml. The INI and XML files go into a directory specified in
 * this class' constructor. The default is <code>/etc/eureka/etlconfig</code>.
 *
 * @author Andrew Post
 */
@Singleton
public class ETL {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETL.class);
	private final EtlProperties etlProperties;
	private final JobDao jobDao;
	private final DestinationDao destinationDao;
	private final ProtempaDestinationFactory protempaDestFactory;
	private final EtlGroupDao groupDao;

	@Inject
	public ETL(EtlProperties inEtlProperties, JobDao inJobDao, DestinationDao inDestinationDao, EtlGroupDao inGroupDao, ProtempaDestinationFactory inProtempaDestFactory) {
		this.etlProperties = inEtlProperties;
		this.jobDao = inJobDao;
		this.destinationDao = inDestinationDao;
		this.protempaDestFactory = inProtempaDestFactory;
		this.groupDao = inGroupDao;
	}

	void run(JobEntity job, PropositionDefinition[] inPropositionDefinitions,
			String[] inPropIdsToShow, Filter filter, boolean updateData,
			Configuration prompts) throws EtlException {
		assert inPropositionDefinitions != null :
				"inPropositionDefinitions cannot be null";
		assert job != null : "job cannot be null";
		try (Protempa protempa = getNewProtempa(job, prompts)) {
			logValidationEvents(job, protempa.validateDataSourceBackendData(), null);

			EtlDestination eurekaDestination
					= new Destinations(this.etlProperties, job.getUser(),
							this.destinationDao, this.groupDao)
					.getOne(job.getDestination().getName());
			org.protempa.dest.Destination protempaDestination
					= this.protempaDestFactory.getInstance(eurekaDestination.getId(), updateData);

			DefaultQueryBuilder q = new DefaultQueryBuilder();
			q.setPropositionDefinitions(inPropositionDefinitions);
			if (!eurekaDestination.isAllowingQueryPropositionIds()) {
				q.setPropositionIds(protempa.getSupportedPropositionIds(protempaDestination));
			} else {
				q.setPropositionIds(inPropIdsToShow);
			}
			q.setName(job.getName());
			q.setUsername(job.getUser().getUsername());
			q.setFilters(filter);
			q.setQueryMode(updateData ? QueryMode.UPDATE : QueryMode.REPLACE);
			LOGGER.trace("Constructed Protempa query {}", q);

			Query query = protempa.buildQuery(q);
			protempa.execute(query, protempaDestination);
		} catch (DataSourceFailedDataValidationException ex) {
			logValidationEvents(job, ex.getValidationEvents(), ex);
			throw new EtlException("ETL failed for job " + job.getId(), ex);
		} catch (Exception ex) {
			throw new EtlException("ETL failed for job " + job.getId(), ex);
		}
	}

	void close() {
	}

	private void logValidationEvents(JobEntity job, DataValidationEvent[] events, DataSourceFailedDataValidationException ex) {
		for (DataValidationEvent event : events) {
			AbstractFileInfo fileInfo;
			JobStatus jobEventType;
			if (event.isFatal()) {
				fileInfo = new FileError();
				jobEventType = JobStatus.ERROR;
			} else {
				fileInfo = new FileWarning();
				jobEventType = JobStatus.WARNING;
			}
			fileInfo.setLineNumber(event.getLine());
			fileInfo.setText(event.getMessage());
			fileInfo.setType(event.getType());
			fileInfo.setURI(event.getURI());
			JobEvent validationJobEvent = new JobEvent();
			validationJobEvent.setJob(job);
			validationJobEvent.setTimeStamp(event.getTimestamp());
			validationJobEvent.setStatus(jobEventType);
			validationJobEvent.setMessage(fileInfo.toUserMessage());
			validationJobEvent.setExceptionStackTrace(collectThrowableMessages(ex));
		}
		this.jobDao.update(job);
	}

	private Protempa getNewProtempa(JobEntity job, Configuration prompts) throws
			NewProtempaException {
		try {
			Configurations configurations = new EurekaProtempaConfigurations(this.etlProperties);
			Configuration configuration = configurations.load(job.getSourceConfigId());
			configuration.merge(prompts);
			SourceFactory sf = new SourceFactory(configuration);
			return Protempa.newInstance(sf);
		} catch (IOException | ConfigurationsLoadException | ProtempaStartupException | ConfigurationsNotFoundException | InvalidPropertyNameException | InvalidPropertyValueException ex) {
			throw new NewProtempaException("Error creating Protempa for sourceconfig " + job.getSourceConfigId() + " for job " + job.getId(), ex);
		}
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
