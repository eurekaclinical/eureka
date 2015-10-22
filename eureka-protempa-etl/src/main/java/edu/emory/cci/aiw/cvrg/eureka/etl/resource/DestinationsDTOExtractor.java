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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ResolvedPermissions;

/**
 *
 * @author Andrew Post
 */
abstract class DestinationsDTOExtractor<E, F extends DestinationEntity> extends ConfigsDTOExtractor<E, F> {
	private final EtlGroupDao groupDao;
	
	public DestinationsDTOExtractor(AuthorizedUserEntity user, EtlGroupDao inGroupDao) {
		super(user);
		this.groupDao = inGroupDao;
	}
	
	@Override
	abstract E extractDTO(Perm perm, F destinationEntity);
	
	@Override
	ResolvedPermissions resolvePermissions(AuthorizedUserEntity owner, DestinationEntity entity) {
		return this.groupDao.resolveDestinationPermissions(owner, entity);
	}
}
