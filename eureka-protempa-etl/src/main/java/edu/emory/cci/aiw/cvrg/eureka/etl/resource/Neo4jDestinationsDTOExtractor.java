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
