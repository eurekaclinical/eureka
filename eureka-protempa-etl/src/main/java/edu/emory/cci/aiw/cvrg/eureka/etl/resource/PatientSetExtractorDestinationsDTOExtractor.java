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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlPatientSetExtractorDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetExtractorDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import java.util.ArrayList;

/**
 *
 * @author Andrew Post
 */
class PatientSetExtractorDestinationsDTOExtractor extends DestinationsDTOExtractor<EtlPatientSetExtractorDestination, PatientSetExtractorDestinationEntity> {

	PatientSetExtractorDestinationsDTOExtractor(AuthorizedUserEntity user, EtlGroupDao inGroupDao) {
		super(user, inGroupDao);
	}

	@Override
	EtlPatientSetExtractorDestination extractDTO(Perm perm,
			PatientSetExtractorDestinationEntity destinationEntity) {
		EtlPatientSetExtractorDestination dest = new EtlPatientSetExtractorDestination();
		dest.setName(destinationEntity.getName());
		dest.setDescription(destinationEntity.getDescription());
		dest.setId(destinationEntity.getId());
		dest.setRead(perm.read);
		dest.setWrite(perm.write);
		dest.setExecute(perm.execute);
		dest.setOwnerUserId(destinationEntity.getOwner().getId());
		dest.setCreatedAt(destinationEntity.getCreatedAt());
		dest.setUpdatedAt(destinationEntity.getEffectiveAt());
		dest.setGetStatisticsSupported(destinationEntity.isGetStatisticsSupported());
		dest.setAllowingQueryPropositionIds(destinationEntity.isAllowingQueryPropositionIds());
		dest.setRequiredPropositionIds(new ArrayList<String>(0));
		dest.setAliasPropositionId(destinationEntity.getAliasPropositionId());
		return dest;
	}

}
