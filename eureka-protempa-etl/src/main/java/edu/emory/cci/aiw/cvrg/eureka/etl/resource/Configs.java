/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfigDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ResolvedPermissions;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andrew Post
 */
public abstract class Configs<E, F extends ConfigEntity> {

	private final EtlUserEntity user;
	private final EtlProperties etlProperties;
	private final ConfigDao<F> configDao;

	Configs(EtlProperties inEtlProperties, EtlUserEntity inEtlUser, ConfigDao<F> inConfigDao) {
		assert inEtlProperties != null : "inEtlProperties cannot be null";
		assert inEtlUser != null : "inEtlUser cannot be null";
		assert inConfigDao != null : "inConfigDao cannot be null";
		this.user = inEtlUser;
		this.etlProperties = inEtlProperties;
		this.configDao = inConfigDao;
	}

	/**
	 * Gets the specified source extractDTO. If it does not exist or the current
	 * user lacks read permissions for it, this method returns
	 * <code>null</code>.
	 *
	 * @return a extractDTO.
	 */
	public final E getOne(String configId) {
		if (configId == null) {
			throw new IllegalArgumentException("configId cannot be null");
		}
		return Configs.this.extractDTO(this.configDao.getByName(configId));
	}

	private E extractDTO(F configEntity) {
		Perm perm = perm(configEntity);
		if (perm != null && perm.read) {
			return extractDTO(perm, configEntity);
		} else {
			return null;
		}
	}

	/**
	 * Gets all configs for which the current user has read permissions.
	 *
	 * @return a {@link List} of configs.
	 */
	public final List<E> getAll() {
		List<E> result = new ArrayList<>();
		for (F configEntity : this.configDao.getAll()) {
			E dto = extractDTO(configEntity);
			if (dto != null) {
				result.add(dto);
			}
		}
		return result;
	}

	private Perm perm(F configEntity) {
		if (configEntity != null) {
			EtlUserEntity owner = configEntity.getOwner();
			if (this.user.equals(owner)) {
				return new Perm(owner, true, true, true);
			}

			ResolvedPermissions resolvedPermissions = resolvePermissions(owner, configEntity);
			return new Perm(owner, resolvedPermissions.isGroupRead(), resolvedPermissions.isGroupWrite(), resolvedPermissions.isGroupExecute());
		} else {
			return null;
		}
	}

	EtlProperties getEtlProperties() {
		return etlProperties;
	}

	abstract E extractDTO(Perm perm, F configEntity);

	abstract ResolvedPermissions resolvePermissions(EtlUserEntity owner, F entity);
}
