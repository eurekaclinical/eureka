package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.i2b2etl.I2b2Destination;
import org.protempa.KnowledgeSource;
import org.protempa.dest.keyloader.KeyLoaderDestination;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class ProtempaDestinationFactory {
	private final EtlProperties etlProperties;
	private final DestinationDao destinationDao;

	@Inject
	public ProtempaDestinationFactory(DestinationDao inDestinationDao, EtlProperties etlProperties) {
		this.destinationDao = inDestinationDao;
		this.etlProperties = etlProperties;
	}
	
	public org.protempa.dest.Destination getInstance(Long destId, KnowledgeSource knowledgeSource) {
		DestinationEntity dest = this.destinationDao.retrieve(destId);
		if (dest instanceof I2B2DestinationEntity) {
			return new I2b2Destination(this.etlProperties.destinationConfigFile(dest.getName()));
		} else if (dest instanceof CohortDestinationEntity) {
			CohortEntity cohortEntity = ((CohortDestinationEntity) dest).getCohort();
			Cohort cohort = cohortEntity.toCohort();
			return new KeyLoaderDestination(new CohortCriteria(cohort));
		} else {
			throw new AssertionError("Invalid destination entity type " + dest.getClass());
		}
	}

}
