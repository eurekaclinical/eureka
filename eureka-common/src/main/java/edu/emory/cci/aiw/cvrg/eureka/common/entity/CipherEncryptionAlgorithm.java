/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "ea_ciphers")
public class CipherEncryptionAlgorithm extends EncryptionAlgorithm {

	@Column(nullable = false)
	private String keyAlgorithm;

	@Column(nullable = false)
	private String cipherAlgorithm;
	

	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public void setKeyAlgorithm(String keyAlgorithm) throws NoSuchAlgorithmException {
		this.keyAlgorithm = keyAlgorithm;
	}

	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	public void setCipherAlgorithm(String cipherAlgorithm) {
		this.cipherAlgorithm = cipherAlgorithm;
	}

	public KeyGenerator getKeyGeneratorInstance() throws NoSuchAlgorithmException {
		if (this.keyAlgorithm != null) {
			return KeyGenerator.getInstance(this.keyAlgorithm);
		} else {
			return null;
		}
	}

	public boolean isReversible() {
		return true;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
