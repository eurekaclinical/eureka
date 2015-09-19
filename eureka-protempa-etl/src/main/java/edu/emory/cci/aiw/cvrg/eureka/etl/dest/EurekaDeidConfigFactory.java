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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CipherEncryptionAlgorithm;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EncryptionAlgorithm;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.MessageDigestEncryptionAlgorithm;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DeidPerPatientParamDao;

/**
 *
 * @author Andrew Post
 */
public class EurekaDeidConfigFactory {
	private final DestinationEntity destinationEntity;
	private final DeidPerPatientParamDao destinationOffsetDao;
	
	public EurekaDeidConfigFactory(DestinationEntity inDestinationEntity, DeidPerPatientParamDao inDestinationOffsetDao) {
		this.destinationEntity = inDestinationEntity;
		this.destinationOffsetDao = inDestinationOffsetDao;
	}
	
	public EurekaDeidConfig getInstance() {
		EncryptionAlgorithm encryptionAlgorithm = this.destinationEntity.getEncryptionAlgorithm();
		if (encryptionAlgorithm instanceof MessageDigestEncryptionAlgorithm) {
			return new EurekaMessageDigestDeidConfig(this.destinationEntity, (MessageDigestEncryptionAlgorithm) encryptionAlgorithm, this.destinationOffsetDao);
		} else if (encryptionAlgorithm instanceof CipherEncryptionAlgorithm) {
			return new EurekaCipherDeidConfig(this.destinationEntity, (CipherEncryptionAlgorithm) encryptionAlgorithm, this.destinationOffsetDao);
		} else {
			return null;
		}
	}
}
