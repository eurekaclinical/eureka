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
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.Neo4jDestinationEntity;
import edu.emory.cci.aiw.neo4jetl.config.Configuration;
import edu.emory.cci.aiw.neo4jetl.config.IndexOnProperty;

/**
 *
 * @author Andrew Post
 */
class EurekaNeo4jConfiguration implements Configuration {

	private final Neo4jDestinationEntity destination;
	private final String[] propIds;
	private final IndexOnProperty[] propertiesToIndex;

	EurekaNeo4jConfiguration(Neo4jDestinationEntity destination) {
		assert destination != null : "destination cannot be null";
		this.destination = destination;
		this.propIds = 
			new String[]{
				"Patient", "PatientDetails", "Encounter", "LAB:LabTest", 
				"ICD9:Diagnoses", "ICD9:Procedures", "MED:medications", 
				"CPTCode", "VitalSign", "NLST"
			};
		this.propertiesToIndex =
			new IndexOnProperty[]{
				new EurekaIndexOnProperty("patientId")
			};
	}

	@Override
	public String getName() {
		return this.destination.getName();
	}
	
	@Override
	public String[] getPropositionIds() {
		return this.propIds.clone();
	}

	@Override
	public String getNeo4jHome() {
		return this.destination.getDbHome();
	}

	/**
	 * Gets the value to set null properties. Must be one of the following
	 * types:
	 * <ul>
	 * <li>boolean or boolean[] byte or byte[]
	 * <li>short or short[]
	 * <li>int or int[]
	 * <li>long or long[]
	 * <li>float or float[]
	 * <li>double or double[]
	 * <li>char or char[]
	 * <li>java.lang.String or String[]
	 * </ul>
	 *
	 * @return an instance of one of the valid types above.
	 */
	@Override
	public Object getNullValue() {
		return "N/A";
	}

	@Override
	public IndexOnProperty[] getPropertiesToIndex() {
		return this.propertiesToIndex.clone();
	}

}
