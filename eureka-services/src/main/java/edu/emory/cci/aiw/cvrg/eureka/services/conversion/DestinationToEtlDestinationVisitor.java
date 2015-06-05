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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.AbstractDestinationVisitor;
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
public class DestinationToEtlDestinationVisitor extends AbstractDestinationVisitor {

	private EtlDestination etlDestination;

	@Override
	public void visit(CohortDestination cohortDestination) {
		EtlCohortDestination etlCohortDestination = new EtlCohortDestination();
		visitCommon(cohortDestination, etlCohortDestination);
		ServicesNodeToEtlNodeVisitor v = new ServicesNodeToEtlNodeVisitor();
		Cohort servicesCohort = cohortDestination.getCohort();
		Cohort etlCohort = new Cohort();
		servicesCohort.getNode().accept(v);
		etlCohort.setNode(v.getNode());
		etlCohortDestination.setCohort(etlCohort);
		this.etlDestination = etlCohortDestination;
	}

	@Override
	public void visit(I2B2Destination i2b2Destination) {
		EtlI2B2Destination etlI2B2Destination = new EtlI2B2Destination();
		visitCommon(i2b2Destination, etlI2B2Destination);
		this.etlDestination = etlI2B2Destination;
	}

	@Override
	public void visit(Neo4jDestination neo4jDestination) {
		EtlNeo4jDestination etlNeo4jDestination = new EtlNeo4jDestination();
		visitCommon(neo4jDestination, etlNeo4jDestination);
		etlNeo4jDestination.setDbPath(neo4jDestination.getDbPath());
		this.etlDestination = etlNeo4jDestination;
	}
	
	public EtlDestination getEtlDestination() {
		return this.etlDestination;
	}

	private void visitCommon(Destination destination, EtlDestination etlDestination) {
		etlDestination.setId(destination.getId());
		etlDestination.setName(destination.getName());
		etlDestination.setDescription(destination.getDescription());
		DataElementField[] etlDestDataElementFields
				= destination.getDataElementFields();
		if (etlDestDataElementFields != null) {
			etlDestination.setDataElementFields(etlDestDataElementFields);
		}
		etlDestination.setOwnerUserId(destination.getOwnerUserId());
		etlDestination.setRead(destination.isRead());
		etlDestination.setWrite(destination.isWrite());
		etlDestination.setExecute(destination.isExecute());
		etlDestination.setLinks(destination.getLinks());
		etlDestination.setGetStatisticsSupported(destination.isGetStatisticsSupported());
	}

	@Override
	public void visit(PatientSetSenderDestination patientSetSenderDestination) {
		EtlPatientSetSenderDestination etlPtSetSenderDest = new EtlPatientSetSenderDestination();
		etlPtSetSenderDest.setUrl(patientSetSenderDestination.getUrl());
		visitCommon(patientSetSenderDestination, etlPtSetSenderDest);
		this.etlDestination = etlPtSetSenderDest;
	}

}
