/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.common.dao;

import java.util.List;

/**
 * Provides an interface for the common CRUD operations for an entity.
 *
 * @param <T>  The type of the entity.
 * @param <PK> The type of the primary key of the entity.
 * @author hrathod
 */
public interface Dao<T, PK> {

	/**
	 * Creates (persists) the given entity in the data store.
	 *
	 * @param entity The entity to create (persist).
	 * @return The persisted entity.  Useful for chained method calls,
	 * if needed.
	 */
	public T create(T entity);

	/**
	 * Retrieves the entity referred to by the unique identifier from the data
	 * store.
	 *
	 * @param uniqueId The unique identifier for the entity to retrieve.
	 * @return The retrieved entity, or null if it can not be found.
	 */
	public T retrieve(PK uniqueId);

	/**
	 * Updates the data store using the given entity.
	 *
	 * @param entity The entity to use for updates to the data store.
	 * @return The given entity.  Useful for chained method calls, if needed.
	 */
	public T update(T entity);

	/**
	 * Removes the given entity from the data store.
	 *
	 * @param entity The entity to remove from the data store.
	 * @return The removed entity.
	 */
	public T remove(T entity);

	/**
	 * Refreshes the given entity instance with latest information from the
	 * data store.
	 *
	 * @param entity The entity to refresh from the data store.
	 * @return The refreshed entity.
	 */
	public T refresh(T entity);

	/**
	 * Retrieves a list of all the entities of the given type in the data
	 * store.
	 *
	 * @return A list of all entities in the data store.
	 */
	public List<T> getAll();
}
