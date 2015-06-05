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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Link;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LinkEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Post
 */
class I2B2DestinationsDTOExtractor extends DestinationsDTOExtractor<EtlI2B2Destination, I2B2DestinationEntity> {

	public I2B2DestinationsDTOExtractor(EtlProperties inEtlProperties, EtlUserEntity user, EtlGroupDao inGroupDao) {
		super(user, inGroupDao);
	}

	@Override
	EtlI2B2Destination extractDTO(Perm perm,
			I2B2DestinationEntity destinationEntity) {
		EtlI2B2Destination dest = new EtlI2B2Destination();
		dest.setName(destinationEntity.getName());

		dest.setId(destinationEntity.getId());

		dest.setRead(perm.read);
		dest.setWrite(perm.write);
		dest.setExecute(perm.execute);

		dest.setOwnerUserId(destinationEntity.getOwner().getId());

		dest.setCreatedAt(destinationEntity.getCreatedAt());
		dest.setUpdatedAt(destinationEntity.getEffectiveAt());
		dest.setGetStatisticsSupported(destinationEntity.isGetStatisticsSupported());
		
		List<LinkEntity> linkEntities = destinationEntity.getLinks();
		if (linkEntities != null) {
			List<Link> links = new ArrayList<>(linkEntities.size());
			for (LinkEntity le : linkEntities) {
				links.add(le.toLink());
			}
			dest.setLinks(links);
		}

		return dest;
	}
}
