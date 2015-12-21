/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
