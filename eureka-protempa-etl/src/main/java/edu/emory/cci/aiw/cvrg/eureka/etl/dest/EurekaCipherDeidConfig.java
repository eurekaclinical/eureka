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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DeidPerPatientParams;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DeidPerPatientParamDao;
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
public class EurekaCipherDeidConfig extends EurekaDeidConfig implements CipherDeidConfig {

	private static final Base64 BASE64 = new Base64();

	private final String cipherAlgorithm;
	private final String keyAlgorithm;
	private final CipherEncryptionAlgorithm encryptionAlgorithm;
	private KeyGenerator keyGenerator;

	public EurekaCipherDeidConfig(DestinationEntity inDestination, CipherEncryptionAlgorithm encryptionAlgorithm, DeidPerPatientParamDao inDestinationOffsetDao) {
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
		DeidPerPatientParamDao deidPerPatientParamDao = getDeidPerPatientParamDao();
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
