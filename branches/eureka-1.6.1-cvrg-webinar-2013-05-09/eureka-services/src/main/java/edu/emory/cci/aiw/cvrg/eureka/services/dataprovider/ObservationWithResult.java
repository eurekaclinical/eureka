/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
 * Observations for a patient's encounter, including results of the observation.
 * 
 * @author hrathod
 * 
 */
public interface ObservationWithResult extends Observation {

	/**
	 * Get the results as a string.
	 * 
	 * @return the results of the lab test as a string.
	 */
	public abstract String getResultAsStr();

	/**
	 * Set the results as a string.
	 * 
	 * @param inResultAsStr the results, as a string.
	 */
	public abstract void setResultAsStr(String inResultAsStr);

	/**
	 * Get the results as a number.
	 * 
	 * @return The results as a number.
	 */
	public abstract Double getResultAsNum();

	/**
	 * Set the results as a number
	 * 
	 * @param inResultAsNum The results as a number.
	 */
	public abstract void setResultAsNum(Double inResultAsNum);

	/**
	 * Get the units used to measure the results.
	 * 
	 * @return The units units to measure the results.
	 */
	public abstract String getUnits();

	/**
	 * Set the units used to measure the results.
	 * 
	 * @param inUnits The units units to measure the results.
	 */
	public abstract void setUnits(String inUnits);

	/**
	 * Get the flag for the result.
	 * 
	 * @return The flag for the result.
	 */
	public abstract String getFlag();

	/**
	 * Set the flag for the result.
	 * 
	 * @param inFlag The flag for the result.
	 */
	public abstract void setFlag(String inFlag);

}