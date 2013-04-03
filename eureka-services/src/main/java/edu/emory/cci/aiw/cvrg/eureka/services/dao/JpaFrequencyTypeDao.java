/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType_;
import java.util.List;

import javax.persistence.EntityManager;

/**
 *
 */
public class JpaFrequencyTypeDao extends
		GenericDao<FrequencyType, Long> implements FrequencyTypeDao {

	@Inject
	protected JpaFrequencyTypeDao(Provider<EntityManager> inManagerProvider) {
		super(FrequencyType.class, inManagerProvider);
	}

	@Override
	public FrequencyType getByName(String inName) {
		return this.getUniqueByAttribute(FrequencyType_.name, inName);
	}
	
	@Override
	public FrequencyType getDefault() {
		return getUniqueByAttribute(FrequencyType_.isDefault, true);
	}

	@Override
	public List<FrequencyType> getAllAsc() {
		return getListAsc(FrequencyType_.rank);
	}
}
