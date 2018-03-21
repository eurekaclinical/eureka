package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

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

import edu.emory.cci.aiw.cvrg.eureka.etl.entity.DeidPerPatientParams;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.MessageDigestEncryptionAlgorithm;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EurekaDeidConfigDao;
import org.protempa.dest.deid.EncryptionInitException;
import org.protempa.dest.deid.MessageDigestDeidConfig;
import org.protempa.dest.deid.MessageDigestEncryption;

/**
 *
 * @author Andrew Post
 */
class EurekaMessageDigestDeidConfig implements EurekaDeidConfig, MessageDigestDeidConfig {
	private final String algorithm;
	private final EurekaDeidConfigDao eurekaDeidConfigDao;
	private final DestinationEntity destination;

	EurekaMessageDigestDeidConfig(DestinationEntity inDestination, EurekaDeidConfigDao inEurekaDeidConfigDao) {
		assert inDestination != null : "inDestination cannot be null";
		assert inEurekaDeidConfigDao != null : "inEurekaDeidConfigDao cannot be null";
		this.destination = inDestination;
		this.algorithm = ((MessageDigestEncryptionAlgorithm) inDestination.getEncryptionAlgorithm()).getAlgorithm();
		this.eurekaDeidConfigDao = inEurekaDeidConfigDao;
	}

	@Override
	public MessageDigestEncryption getEncryptionInstance() throws EncryptionInitException {
		return new MessageDigestEncryption(this);
	}

	@Override
	public String getAlgorithm() {
		return this.algorithm;
	}
	
	@Override
	public byte[] getSalt(String keyId) {
		DeidPerPatientParams deidPerPatientParams = this.eurekaDeidConfigDao.getOrCreatePatientParams(keyId, this.destination);
		byte[] salt = deidPerPatientParams.getSalt();
		if (salt == null) {
			salt = new byte[20];
			this.eurekaDeidConfigDao.getRandom().nextBytes(salt);
			deidPerPatientParams.setSalt(salt);
			this.eurekaDeidConfigDao.update(deidPerPatientParams);
		}
		return salt;
	}

	@Override
	public Integer getOffset(String inKeyId) {
		return this.eurekaDeidConfigDao.getOffset(inKeyId, this.destination);
	}

	@Override
	public void close() throws Exception {
		this.eurekaDeidConfigDao.close();
	}

}
