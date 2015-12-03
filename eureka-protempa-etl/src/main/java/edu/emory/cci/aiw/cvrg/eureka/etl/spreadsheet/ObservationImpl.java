/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet;

import java.util.Date;

/**
 * Abstract class used as the super-type for various types of observations
 * related to an encounter.
 * 
 * @author hrathod
 * 
 */
abstract class ObservationImpl implements Observation {
	/**
	 * The unique identifier for the observation.
	 */
	private String id;
	/**
	 * The unique identifier for the encounter to which this observation is
	 * associated.
	 */
	private Long encounterId;
	/**
	 * The date of the observation.
	 */
	private Date timestamp;
	/**
	 * The unique identifier for the entity associated with this observation.
	 */
	private String entityId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#setId
	 * (java.lang.String)
	 */
	@Override
	public void setId(String inId) {
		this.id = inId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#
	 * getEncounterId()
	 */
	@Override
	public Long getEncounterId() {
		return this.encounterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#
	 * setEncounterId(java.lang.Long)
	 */
	@Override
	public void setEncounterId(Long inEncounterId) {
		this.encounterId = inEncounterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#getTimestamp
	 * ()
	 */
	@Override
	public Date getTimestamp() {
		return this.timestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#setTimestamp
	 * (java.util.Date)
	 */
	@Override
	public void setTimestamp(Date inTimestamp) {
		this.timestamp = inTimestamp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#getEntityId
	 * ()
	 */
	@Override
	public String getEntityId() {
		return this.entityId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation#setEntityId
	 * (java.lang.String)
	 */
	@Override
	public void setEntityId(String inEntityId) {
		this.entityId = inEntityId;
	}
}
