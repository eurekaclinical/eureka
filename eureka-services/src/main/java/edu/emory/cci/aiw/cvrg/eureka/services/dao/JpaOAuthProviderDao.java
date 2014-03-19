/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.emory.cci.aiw.cvrg.eureka.services.dao;

/*
 * #%L
 * Eureka Services
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

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthProvider;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthProvider_;
import javax.persistence.EntityManager;

/**
 *
 * @author Andrew Post
 */
public class JpaOAuthProviderDao extends GenericDao<OAuthProvider, Long> implements OAuthProviderDao{

	@Inject
	protected JpaOAuthProviderDao(Provider<EntityManager> inManagerProvider) {
		super(OAuthProvider.class, inManagerProvider);
	}

	@Override
	public OAuthProvider getByName(String inName) {
		return this.getUniqueByAttribute(OAuthProvider_.name, inName);
	}
	
}
