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

import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.GenericDao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload_;

/**
 * An implementation of {@link FileDao} interface using JPA entities.
 *
 * @author hrathod
 */
public class JpaFileDao extends GenericDao<FileUpload, Long>
		implements FileDao {

	/**
	 * Construct an object with the given entity manager.
	 *
	 * @param inEMProvider The entity manager provider used to fetch an entity
	 *                     manager to work with the data store.
	 */
	@Inject
	public JpaFileDao(Provider<EntityManager> inEMProvider) {
		super(FileUpload.class, inEMProvider);
	}

	/**
	 * Gets all the file uploads that belong to the given user.
	 * @param userId The unique identifier for the user.
	 *
	 * @return A list of file uploads belonging to the given user.
	 */
	@Override
	public List<FileUpload> getByUserId(Long userId) {
		return this.getListByAttribute(FileUpload_.userId, userId);
	}
}
