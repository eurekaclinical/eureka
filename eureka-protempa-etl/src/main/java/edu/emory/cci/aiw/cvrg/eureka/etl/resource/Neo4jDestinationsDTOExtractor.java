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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlNeo4jDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Link;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LinkEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Neo4jDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Post
 */
class Neo4jDestinationsDTOExtractor extends DestinationsDTOExtractor<EtlNeo4jDestination, Neo4jDestinationEntity> {

	public Neo4jDestinationsDTOExtractor(EtlProperties etlProperties, AuthorizedUserEntity user, EtlGroupDao inGroupDao) {
		super(user, inGroupDao);
	}

	@Override
	EtlNeo4jDestination extractDTO(Perm perm,
			Neo4jDestinationEntity destinationEntity) {
		EtlNeo4jDestination neo4jDestination = new EtlNeo4jDestination();
		neo4jDestination.setName(destinationEntity.getName());
		neo4jDestination.setDescription(destinationEntity.getDescription());
		neo4jDestination.setId(destinationEntity.getId());
		neo4jDestination.setRead(perm.read);
		neo4jDestination.setWrite(perm.write);
		neo4jDestination.setExecute(perm.execute);
		neo4jDestination.setOwnerUserId(destinationEntity.getOwner().getId());
		neo4jDestination.setCreatedAt(destinationEntity.getCreatedAt());
		neo4jDestination.setUpdatedAt(destinationEntity.getEffectiveAt());
		neo4jDestination.setDbPath(destinationEntity.getDbHome());
		neo4jDestination.setGetStatisticsSupported(destinationEntity.isGetStatisticsSupported());
		neo4jDestination.setAllowingQueryPropositionIds(destinationEntity.isAllowingQueryPropositionIds());
		neo4jDestination.setRequiredPropositionIds(new ArrayList<String>(0));
		List<LinkEntity> linkEntities = destinationEntity.getLinks();
		if (linkEntities != null) {
			List<Link> links = new ArrayList<>(linkEntities.size());
			for (LinkEntity le : linkEntities) {
				links.add(le.toLink());
			}
			neo4jDestination.setLinks(links);
		}

		return neo4jDestination;
	}

}
