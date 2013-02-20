/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

/**
 * A person related to an encounter; for example, a patient or a provider.
 * 
 * @author hrathod
 * 
 */
abstract class PersonImpl implements Person {
	/**
	 * The unique identifier for the person.
	 */
	private Long id;
	/**
	 * Person's first name.
	 */
	private String firstName;
	/**
	 * Person's last name.
	 */
	private String lastName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#getId()
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#setId(java
	 * .lang.Long)
	 */
	@Override
	public void setId(Long inId) {
		this.id = inId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return this.firstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#setFirstName
	 * (java.lang.String)
	 */
	@Override
	public void setFirstName(String inFirstName) {
		this.firstName = inFirstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#getLastName()
	 */
	@Override
	public String getLastName() {
		return this.lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#setLastName
	 * (java.lang.String)
	 */
	@Override
	public void setLastName(String inLastName) {
		this.lastName = inLastName;
	}
}
