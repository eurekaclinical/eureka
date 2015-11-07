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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Neo4jDestinationEntity;
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
