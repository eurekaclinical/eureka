package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;

/**
 *
 * @author Andrew Post
 */
class CohortDestinationsDTOExtractor extends DestinationsDTOExtractor<EtlCohortDestination, CohortDestinationEntity> {

	public CohortDestinationsDTOExtractor(EtlUserEntity user, EtlGroupDao inGroupDao) {
		super(user, inGroupDao);
	}

	@Override
	EtlCohortDestination extractDTO(Perm perm,
			CohortDestinationEntity destinationEntity) {
		EtlCohortDestination cohortDest = new EtlCohortDestination();
		cohortDest.setName(destinationEntity.getName());
		cohortDest.setDescription(destinationEntity.getDescription());
		cohortDest.setCohort(destinationEntity.getCohort().toCohort());
		cohortDest.setId(destinationEntity.getId());
		cohortDest.setRead(perm.read);
		cohortDest.setWrite(perm.write);
		cohortDest.setExecute(perm.execute);
		cohortDest.setOwnerUserId(destinationEntity.getOwner().getId());

		return cohortDest;
	}

}
