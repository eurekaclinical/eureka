package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DeidPerPatientParams;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DeidPerPatientParamDao;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;
import org.protempa.dest.deid.DeidConfig;

/**
 *
 * @author Andrew Post
 */
public abstract class EurekaDeidConfig implements DeidConfig {

	private static final int MAX_OFFSET_SECONDS = 364 * 24 * 60 * 60;

	private final DeidPerPatientParamDao deidPerPatientParamDao;

	private final Random random;
	private final DestinationEntity destination;

	EurekaDeidConfig(DestinationEntity inDestination, DeidPerPatientParamDao inDeidPerPatientParamDao) {
		this.destination = inDestination;
		this.deidPerPatientParamDao = inDeidPerPatientParamDao;
		this.random = new SecureRandom();
		this.random.setSeed(System.currentTimeMillis());
	}

	@Override
	public Integer getOffset(String keyId) {
		DeidPerPatientParams deidPerPatientParams = getOrCreatePatientParams(keyId);
		Integer offset = deidPerPatientParams.getOffset();
		if (offset != null) {
			return offset;
		} else {
			int offsetInSeconds = this.random.nextInt(MAX_OFFSET_SECONDS);
			if (!this.random.nextBoolean()) {
				offsetInSeconds = offsetInSeconds * -1;
			}
			deidPerPatientParams.setOffset(offsetInSeconds);
			this.deidPerPatientParamDao.update(deidPerPatientParams);
			return offsetInSeconds;
		}
	}

	protected DeidPerPatientParams getOrCreatePatientParams(String keyId) {
		synchronized (this.deidPerPatientParamDao) {
			DeidPerPatientParams offset = this.deidPerPatientParamDao.getByKeyId(keyId);
			if (offset == null) {
				offset = new DeidPerPatientParams();
				offset.setKeyId(keyId);
				offset.setDestination(this.destination);
				this.deidPerPatientParamDao.create(offset);
			}
			return offset;
		}
	}
	
	@Override
	public void close() throws IOException {
	}
	
	protected DeidPerPatientParamDao getDeidPerPatientParamDao() {
		return this.deidPerPatientParamDao;
	}
	
	protected Random getRandom() {
		return this.random;
	}

}
