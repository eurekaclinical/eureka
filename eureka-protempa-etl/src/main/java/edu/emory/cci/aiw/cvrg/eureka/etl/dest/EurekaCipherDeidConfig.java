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
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DeidPerPatientParamsDao;

/**
 *
 * @author Andrew Post
 */
public class EurekaCipherDeidConfig extends EurekaDeidConfig implements CipherDeidConfig {

	private static final Base64 BASE64 = new Base64();

	private final String cipherAlgorithm;
	private final String keyAlgorithm;
	private final CipherEncryptionAlgorithm encryptionAlgorithm;
	private KeyGenerator keyGenerator;

	public EurekaCipherDeidConfig(DestinationEntity inDestination, CipherEncryptionAlgorithm encryptionAlgorithm, DeidPerPatientParamsDao inDestinationOffsetDao) {
		super(inDestination, inDestinationOffsetDao);
		this.cipherAlgorithm = encryptionAlgorithm.getCipherAlgorithm();
		this.keyAlgorithm = encryptionAlgorithm.getKeyAlgorithm();
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	@Override
	public CipherEncryption getEncryptionInstance() throws EncryptionInitException {
		return new CipherEncryption(this);
	}

	@Override
	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	@Override
	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	@Override
	public Key getKey(String keyId) throws KeyCreateException {
		if (this.keyAlgorithm == null) {
			return null;
		}

		synchronized (this.encryptionAlgorithm) {
			if (this.keyGenerator == null) {
				try {
					this.keyGenerator = this.encryptionAlgorithm.getKeyGeneratorInstance();
				} catch (NoSuchAlgorithmException ex) {
					throw new KeyCreateException(ex);
				}
			}
		}
		DeidPerPatientParamsDao deidPerPatientParamDao = getDeidPerPatientParamDao();
		DeidPerPatientParams byKeyId = getOrCreatePatientParams(keyId);
		String keyStr = byKeyId.getCipherKey();
		if (keyStr == null) {
			SecretKey generatedKey = this.keyGenerator.generateKey();
			byte[] encoded = generatedKey.getEncoded();
			byKeyId.setCipherKey(BASE64.encodeToString(encoded));
			deidPerPatientParamDao.update(byKeyId);
			return generatedKey;
		} else {
			byte[] encoded = BASE64.decode(keyStr);
			return new SecretKeySpec(encoded, this.keyAlgorithm);
		}
	}

}
