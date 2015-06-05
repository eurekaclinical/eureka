package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.AbstractEtlDestinationVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementField;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlNeo4jDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlPatientSetSenderDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.I2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Neo4jDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PatientSetSenderDestination;

/**
 *
 * @author Andrew Post
 */
public class EtlDestinationToDestinationVisitor extends AbstractEtlDestinationVisitor {
	private Destination destination;

	@Override
	public void visit(EtlCohortDestination etlCohortDestination) {
		CohortDestination cohortDestination = new CohortDestination();
		visitCommon(etlCohortDestination, cohortDestination);
		EtlNodeToServicesNodeVisitor v = new EtlNodeToServicesNodeVisitor();
		Cohort etlCohort = etlCohortDestination.getCohort();
		Cohort servicesCohort = new Cohort();
		etlCohort.getNode().accept(v);
		servicesCohort.setNode(v.getNode());
		cohortDestination.setCohort(servicesCohort);
		this.destination = cohortDestination;
	}

	@Override
	public void visit(EtlI2B2Destination etlI2B2Destination) {
		I2B2Destination i2b2Destination = new I2B2Destination();
		visitCommon(etlI2B2Destination, i2b2Destination);
		this.destination = i2b2Destination;
	}

	@Override
	public void visit(EtlNeo4jDestination etlNeo4jDestination) {
		Neo4jDestination neo4jDestination = new Neo4jDestination();
		visitCommon(etlNeo4jDestination, neo4jDestination);
		neo4jDestination.setDbPath(etlNeo4jDestination.getDbPath());
		this.destination = neo4jDestination;
	}
	
	public Destination getDestination() {
		return this.destination;
	}
	
	private void visitCommon(EtlDestination etlDestination, Destination destination) {
		destination.setId(etlDestination.getId());
		destination.setName(etlDestination.getName());
		destination.setDescription(etlDestination.getDescription());
		DataElementField[] etlDestDataElementFields
				= etlDestination.getDataElementFields();
		if (etlDestDataElementFields != null) {
			destination.setDataElementFields(etlDestDataElementFields);
		}
		destination.setOwnerUserId(etlDestination.getOwnerUserId());
		destination.setRead(etlDestination.isRead());
		destination.setWrite(etlDestination.isWrite());
		destination.setExecute(etlDestination.isExecute());
		destination.setCreatedAt(etlDestination.getCreatedAt());
		destination.setUpdatedAt(etlDestination.getUpdatedAt());
		destination.setLinks(etlDestination.getLinks());
		destination.setGetStatisticsSupported(etlDestination.isGetStatisticsSupported());
	}

	@Override
	public void visit(EtlPatientSetSenderDestination etlPatientSetSenderDestination) {
		PatientSetSenderDestination ptSetSenderDest = new PatientSetSenderDestination();
		visitCommon(etlPatientSetSenderDestination, ptSetSenderDest);
		ptSetSenderDest.setUrl(etlPatientSetSenderDestination.getUrl());
		this.destination = ptSetSenderDest;
	}
	
}
