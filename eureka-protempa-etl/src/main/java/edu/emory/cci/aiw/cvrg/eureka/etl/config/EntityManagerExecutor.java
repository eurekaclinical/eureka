package edu.emory.cci.aiw.cvrg.eureka.etl.config;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Andrew Post
 */
public abstract class EntityManagerExecutor implements AutoCloseable {
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	
	
	public EntityManagerExecutor(String jpaUnit) {
		this.factory = Persistence.createEntityManagerFactory(jpaUnit);
	}
	
	public final void execute() {
		this.entityManager = factory.createEntityManager();
		doExecute(this.entityManager);
	}
	
	protected abstract void doExecute(EntityManager entityManager);

	@Override
	public void close() {
		this.entityManager.close();
		this.entityManager = null;
		this.factory.close();
		this.factory = null;
	}
	
}
