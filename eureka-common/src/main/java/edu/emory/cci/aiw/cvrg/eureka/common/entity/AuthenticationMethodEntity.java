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

import edu.emory.cci.aiw.cvrg.eureka.common.authentication.AuthenticationMethod;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
*
*/
@Entity
@Table(name = "authentication_methods")
public class AuthenticationMethodEntity {

	@Id
	@SequenceGenerator(name = "AUTHENTICATION_METHODS_SEQ_GEN",
			sequenceName =  "AUTHENTICATION_METHODS_SEQ", allocationSize = 1,
			initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "AUTHENTICATION_METHODS_SEQ_GEN")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private AuthenticationMethod name;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AuthenticationMethod getName() {
		return name;
	}

	public void setName(AuthenticationMethod name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public edu.emory.cci.aiw.cvrg.eureka.common.comm.AuthenticationMethod toAuthenticationMethod() {
		edu.emory.cci.aiw.cvrg.eureka.common.comm.AuthenticationMethod authenticationMethod = new edu.emory.cci.aiw.cvrg.eureka.common.comm.AuthenticationMethod();
		authenticationMethod.setId(this.id);
		authenticationMethod.setName(this.name);
		authenticationMethod.setDescription(this.description);
		return authenticationMethod;
	}
}
