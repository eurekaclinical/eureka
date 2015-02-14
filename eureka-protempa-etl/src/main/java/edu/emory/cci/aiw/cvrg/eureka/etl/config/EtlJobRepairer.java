package edu.emory.cci.aiw.cvrg.eureka.etl.config;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import javax.persistence.EntityManager;
import org.protempa.DataSource;
import org.protempa.ProtempaException;
import org.protempa.SourceFactory;
import org.protempa.backend.Configurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
class EtlJobRepairer extends JobRepairer {
	private static Logger LOGGER =
			LoggerFactory.getLogger(EtlJobRepairer.class);
	private final EtlProperties etlProperties;

	public EtlJobRepairer(EntityManager inEntityManager, EtlProperties inEtlProperties) {
		super(inEntityManager);
		if (inEtlProperties == null) {
			throw new IllegalArgumentException("inEtlProperties cannot be null");
		}
		this.etlProperties = inEtlProperties;
	}

	@Override
	protected void doRepair(JobEntity job) {
		super.doRepair(job);
		repairData(job);
	}
	
	private void repairData(JobEntity job) {
		try {
			Configurations configurations =
					new EurekaProtempaConfigurations(this.etlProperties);
			SourceFactory sf =
					new SourceFactory(configurations, job.getSourceConfigId());
			try (DataSource dataSource = sf.newDataSourceInstance()) {
				dataSource.failureOccurred(null);
			}
		} catch (ProtempaException ex) {
			LOGGER.error("Data for job {} is in an inconsistent state and could not be repaired", job.getId(), ex);
		}
	}
	
}
