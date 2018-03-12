package edu.emory.cci.aiw.cvrg.eureka.etl.dao;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import com.google.inject.Provider;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.DeidPerPatientParams;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.DestinationEntity;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

/**
 *
 * @author Andrew Post
 */
public class JpaEurekaDeidConfigDao implements EurekaDeidConfigDao {

	private static final int MAX_OFFSET_SECONDS = 364 * 24 * 60 * 60;

	private final Random random;
	private final Provider<EntityManager> entityManagerProvider;
	private EntityTransaction transaction;
	private int count;
	private EntityManager entityManager;
	private final DeidPerPatientParamsDao deidPerPatientParamsDao;

	@Inject
	JpaEurekaDeidConfigDao(Provider<EntityManager> inEntityManagerProvider) {
		this.random = new SecureRandom();
		this.random.setSeed(System.currentTimeMillis());
		this.entityManagerProvider = inEntityManagerProvider;
		this.deidPerPatientParamsDao = new JpaDeidPerPatientParamsDao(this.entityManagerProvider);
	}

	@Override
	public Integer getOffset(String inKeyId, DestinationEntity inDestination) {
		DeidPerPatientParams deidPerPatientParams = getOrCreatePatientParams(inKeyId, inDestination);
		Integer offset = deidPerPatientParams.getOffset();
		if (offset == null) {
			int offsetInSeconds = this.random.nextInt(MAX_OFFSET_SECONDS);
			if (!this.random.nextBoolean()) {
				offsetInSeconds = offsetInSeconds * -1;
			}
			beginTx();
			deidPerPatientParams.setOffset(offsetInSeconds);
			try {
				this.deidPerPatientParamsDao.update(deidPerPatientParams);
				endTx();
			} catch (PersistenceException ex) {
				try {
					rollbackTx();
				} catch (PersistenceException ignored) {
				}
				throw ex;
			}
			offset = offsetInSeconds;
		}

		return offset;
	}

	@Override
	public DeidPerPatientParams getOrCreatePatientParams(String inKeyId, DestinationEntity inDestination) {
		DeidPerPatientParams offset = this.deidPerPatientParamsDao.getByKeyId(inKeyId);
		if (offset == null) {
			beginTx();
			try {
				offset = new DeidPerPatientParams();
				offset.setKeyId(inKeyId);
				offset.setDestination(inDestination);
				this.deidPerPatientParamsDao.create(offset);
				endTx();
			} catch (PersistenceException ex) {
				try {
					rollbackTx();
				} catch (PersistenceException ignored) {
				}
				throw ex;
			}
		}

		return offset;
	}

	@Override
	public void update(DeidPerPatientParams inDeidPerPatientParams) {
		beginTx();
		try {
			this.deidPerPatientParamsDao.update(inDeidPerPatientParams);
			endTx();
		} catch (PersistenceException ex) {
			try {
				rollbackTx();
			} catch (PersistenceException ignored) {
			}
			throw ex;
		}
	}

	@Override
	public Random getRandom() {
		return this.random;
	}

	@Override
	public void close() throws IOException {
		if (this.transaction.isActive()) {
			this.transaction.commit();
		}
		this.entityManager = null;
	}

	private void beginTx() {
		if (this.entityManager == null) {
			this.entityManager = this.entityManagerProvider.get();
			this.transaction = this.entityManager.getTransaction();
		}
		if (!this.transaction.isActive()) {
			++this.count;
			this.transaction.begin();
		}
	}

	private void endTx() {
		if (this.count % 1000 == 0) {
			this.transaction.commit();
		}
	}

	private void rollbackTx() {
		if (this.transaction.isActive()) {
			this.transaction.rollback();
		}
	}

}
