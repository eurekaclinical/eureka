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

/**
 * Holds information about an observation during a patient's visit, including
 * results of that observation.
 * 
 * @author hrathod
 * 
 */
abstract class ObservationWithResultImpl extends ObservationImpl implements
		ObservationWithResult {
	/**
	 * The results of the observation, held as a String.
	 */
	private String resultAsStr;
	/**
	 * The results of the observation, held as a number.
	 */
	private Double resultAsNum;
	/**
	 * The units used to measure the results.
	 */
	private String units;
	/**
	 * Any flags resulting from the results.
	 */
	private String flag;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #getResultAsStr()
	 */
	@Override
	public String getResultAsStr() {
		return this.resultAsStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #setResultAsStr(java.lang.String)
	 */
	@Override
	public void setResultAsStr(String inResultAsStr) {
		this.resultAsStr = inResultAsStr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #getResultAsNum()
	 */
	@Override
	public Double getResultAsNum() {
		return this.resultAsNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #setResultAsNum(java.lang.Double)
	 */
	@Override
	public void setResultAsNum(Double inResultAsNum) {
		this.resultAsNum = inResultAsNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #getUnits()
	 */
	@Override
	public String getUnits() {
		return this.units;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #setUnits(java.lang.String)
	 */
	@Override
	public void setUnits(String inUnits) {
		this.units = inUnits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #getFlag()
	 */
	@Override
	public String getFlag() {
		return this.flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult
	 * #setFlag(java.lang.String)
	 */
	@Override
	public void setFlag(String inFlag) {
		this.flag = inFlag;
	}
}
