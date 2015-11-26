package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

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
import org.protempa.dest.keyloader.KeyLoaderDestination;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Neo4jDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetExtractorDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DeidPerPatientParamDao;
import edu.emory.cci.aiw.i2b2etl.dest.I2b2Destination;
import edu.emory.cci.aiw.i2b2etl.dest.config.ConfigurationInitException;
import edu.emory.cci.aiw.neo4jetl.Neo4jDestination;
import org.protempa.dest.DestinationInitException;
import org.protempa.dest.deid.DeidentifiedDestination;

/**
 *
 * @author Andrew Post
 */
@Singleton
public class ProtempaDestinationFactory {

	private final EtlProperties etlProperties;
	private final DestinationDao destinationDao;
	private final DeidPerPatientParamDao destinationOffsetDao;

	@Inject
	public ProtempaDestinationFactory(DestinationDao inDestinationDao, DeidPerPatientParamDao inDestinationOffsetDao, EtlProperties etlProperties) {
		this.destinationDao = inDestinationDao;
		this.destinationOffsetDao = inDestinationOffsetDao;
		this.etlProperties = etlProperties;
	}

	public org.protempa.dest.Destination getInstance(Long destId, boolean updateData) throws DestinationInitException {
		DestinationEntity dest = this.destinationDao.retrieve(destId);
		return getInstance(dest, updateData);
	}

	public org.protempa.dest.Destination getInstance(DestinationEntity dest, boolean updateData) throws DestinationInitException {
		org.protempa.dest.Destination result;
		try {
			if (dest instanceof I2B2DestinationEntity) {
				result = new I2b2Destination(new EurekaI2b2Configuration((I2B2DestinationEntity) dest, this.etlProperties));
			} else if (dest instanceof CohortDestinationEntity) {
				CohortEntity cohortEntity = ((CohortDestinationEntity) dest).getCohort();
				Cohort cohort = cohortEntity.toCohort();
				result = new KeyLoaderDestination(dest.getName(), new CohortCriteria(cohort));
			} else if (dest instanceof Neo4jDestinationEntity) {
				result = new Neo4jDestination(new EurekaNeo4jConfiguration((Neo4jDestinationEntity) dest));
			} else if (dest instanceof PatientSetExtractorDestinationEntity) {
				result = new PatientSetExtractorDestination((PatientSetExtractorDestinationEntity) dest);
			} else {
				throw new AssertionError("Invalid destination entity type " + dest.getClass());
			}

			if (dest.isDeidentificationEnabled()) {
				if (updateData) {
					this.destinationOffsetDao.deleteAll(dest);
				}
				EurekaDeidConfigFactory deidConfigFactory = new EurekaDeidConfigFactory(dest, this.destinationOffsetDao);
				EurekaDeidConfig deidConfig = deidConfigFactory.getInstance();
				return new DeidentifiedDestination(result, deidConfig);
			} else {
				return result;
			}
		} catch (ConfigurationInitException ex) {
			throw new DestinationInitException(ex);
		}
	}

}
