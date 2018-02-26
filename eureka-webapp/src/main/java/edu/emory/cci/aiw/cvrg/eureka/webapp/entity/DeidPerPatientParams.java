package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

/*
 * #%L
 * Eureka Common
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

import java.io.UnsupportedEncodingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "DEID_PER_PATIENT_PARAMS")
public class DeidPerPatientParams {
	
	@Id
	@SequenceGenerator(name = "DEID_PER_PT_PARAMS_SEQ_GEN", sequenceName = "DEID_PER_PT_PARAMS_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "DEID_PER_PT_PARAMS_SEQ_GEN")
	private Long id;
	
	@Column(nullable = false)
	private String keyId;
	
	@Column(name = "DTOFFSET")
	private Integer offset;
	
	private String cipherKey;
	
	private String salt;
	
	@ManyToOne
	@JoinColumn(name="DESTINATIONS_ID")
	private DestinationEntity destination;

	public DeidPerPatientParams() {
	}

	public DeidPerPatientParams(DestinationEntity destination, String keyId, int offset) {
		this.destination = destination;
		this.keyId = keyId;
		this.offset = offset;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DestinationEntity getDestination() {
		return destination;
	}

	public void setDestination(DestinationEntity destination) {
		this.destination = destination;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getCipherKey() {
		return cipherKey;
	}

	public void setCipherKey(String cipherKey) {
		this.cipherKey = cipherKey;
	}
	
	public void setSalt(byte[] salt) {
		if (salt != null) {
			try {
				this.salt = new String(salt, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new AssertionError("UTF-8 character set not supported");
			}
		} else {
			this.salt = null;
		}
	}
	
	public byte[] getSalt() {
		if (this.salt == null) {
			return null;
		} else {
			try {
				return this.salt.getBytes("UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new AssertionError("UTF-8 character set not supported");
			}
		}
	}
	
}
