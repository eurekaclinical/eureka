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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ResolvedPermissions;

/**
 *
 * @author Andrew Post
 */
abstract class ConfigsDTOExtractor<E, F extends ConfigEntity>  {
	private final EtlUserEntity user;
	
	protected ConfigsDTOExtractor(EtlUserEntity user) {
		this.user = user;
	}
	protected E extractDTO(F configEntity) {
		Perm perm = perm(configEntity);
		if (perm != null && perm.read) {
			return extractDTO(perm, configEntity);
		} else {
			return null;
		}
	}
	
	private Perm perm(F configEntity) {
		if (configEntity != null) {
			EtlUserEntity owner = configEntity.getOwner();
			if (this.user.equals(owner)) {
				return new Perm(owner, true, true, true);
			}

			ResolvedPermissions resolvedPermissions = resolvePermissions(this.user, configEntity);
			return new Perm(this.user, resolvedPermissions.isGroupRead(), resolvedPermissions.isGroupWrite(), resolvedPermissions.isGroupExecute());
		} else {
			return null;
		}
	}
	
	abstract E extractDTO(Perm perm, F configEntity);
	
	abstract ResolvedPermissions resolvePermissions(EtlUserEntity owner, F entity);
}
