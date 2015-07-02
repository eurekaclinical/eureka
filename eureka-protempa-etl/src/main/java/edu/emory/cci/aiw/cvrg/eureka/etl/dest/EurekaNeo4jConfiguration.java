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

/**
 *
 * @author Andrew Post
 */
public class EurekaNeo4jConfiguration implements Configuration {
	private final Neo4jDestinationEntity destination;

	EurekaNeo4jConfiguration(Neo4jDestinationEntity destination) {
		this.destination = destination;
	}
	
	@Override
	public String[] getPropositionIds() {
		return new String[]{"Patient", "PatientDetails", "Encounter", "LAB:LabTest", "ICD9:Diagnoses", "ICD9:Procedures", "MED:medications", "CPTCode", "VitalSign"};
	}

	@Override
	public String getNeo4jHome() {
		return this.destination.getDbHome();
	}

}
