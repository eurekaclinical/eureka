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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CipherEncryptionAlgorithm;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DeidPerPatientParams;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EurekaDeidConfigDao;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.protempa.dest.deid.CipherDeidConfig;
import org.protempa.dest.deid.CipherEncryption;
import org.protempa.dest.deid.EncryptionInitException;
import org.protempa.dest.deid.KeyCreateException;

/**
 *
 * @author Andrew Post
 */
class EurekaCipherDeidConfig implements EurekaDeidConfig, CipherDeidConfig {

	private static final Base64 BASE64 = new Base64();

	private final CipherEncryptionAlgorithm encryptionAlgorithm;
	private KeyGenerator keyGenerator;
	private final EurekaDeidConfigDao eurekaDeidConfigDao;
	private final DestinationEntity destination;

	EurekaCipherDeidConfig(DestinationEntity inDestination, EurekaDeidConfigDao inEurekaDeidConfigDao) {
		assert inDestination != null : "inDestination cannot be null";
		assert inEurekaDeidConfigDao != null : "inEurekaDeidConfigDao cannot be null";
		this.destination = inDestination;
		this.encryptionAlgorithm = (CipherEncryptionAlgorithm) inDestination.getEncryptionAlgorithm();
		this.eurekaDeidConfigDao = inEurekaDeidConfigDao;
	}

	@Override
	public CipherEncryption getEncryptionInstance() throws EncryptionInitException {
		return new CipherEncryption(this);
	}

	@Override
	public String getCipherAlgorithm() {
		return this.encryptionAlgorithm.getCipherAlgorithm();
	}

	@Override
	public String getKeyAlgorithm() {
		return this.encryptionAlgorithm.getKeyAlgorithm();
	}

	@Override
	public Key getKey(String keyId) throws KeyCreateException {
		synchronized (this.encryptionAlgorithm) {
			if (this.keyGenerator == null) {
				try {
					this.keyGenerator = this.encryptionAlgorithm.getKeyGeneratorInstance();
				} catch (NoSuchAlgorithmException ex) {
					throw new KeyCreateException(ex);
				}
			}
		}
		DeidPerPatientParams byKeyId = this.eurekaDeidConfigDao.getOrCreatePatientParams(keyId, this.destination);
		String keyStr = byKeyId.getCipherKey();
		if (keyStr == null) {
			SecretKey generatedKey = this.keyGenerator.generateKey();
			byte[] encoded = generatedKey.getEncoded();
			byKeyId.setCipherKey(BASE64.encodeToString(encoded));
			this.eurekaDeidConfigDao.update(byKeyId);
			return generatedKey;
		} else {
			byte[] encoded = BASE64.decode(keyStr);
			return new SecretKeySpec(encoded, this.encryptionAlgorithm.getKeyAlgorithm());
		}
	}

	@Override
	public Integer getOffset(String keyId) {
		return this.eurekaDeidConfigDao.getOffset(keyId, this.destination);
	}

	@Override
	public void close() throws Exception {
		this.eurekaDeidConfigDao.close();
	}

}
