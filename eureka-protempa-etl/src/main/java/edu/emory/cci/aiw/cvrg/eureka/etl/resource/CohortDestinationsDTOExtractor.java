package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Andrew Post
 */
class CohortDestinationsDTOExtractor extends DestinationsDTOExtractor<EtlCohortDestination, CohortDestinationEntity> {

	public CohortDestinationsDTOExtractor(AuthorizedUserEntity user, EtlGroupDao inGroupDao) {
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
		cohortDest.setCreatedAt(destinationEntity.getCreatedAt());
		cohortDest.setUpdatedAt(destinationEntity.getEffectiveAt());
		cohortDest.setGetStatisticsSupported(destinationEntity.isGetStatisticsSupported());
		cohortDest.setAllowingQueryPropositionIds(destinationEntity.isAllowingQueryPropositionIds());
		cohortDest.setRequiredPropositionIds(new ArrayList<>(0));

		return cohortDest;
	}

}
