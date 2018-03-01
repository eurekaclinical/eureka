package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import org.eurekaclinical.eureka.client.comm.AbstractDestinationVisitor;
import org.eurekaclinical.eureka.client.comm.Cohort;
import org.eurekaclinical.eureka.client.comm.CohortDestination;
import org.eurekaclinical.eureka.client.comm.PhenotypeField;
import org.eurekaclinical.eureka.client.comm.Destination;
import org.eurekaclinical.eureka.client.comm.I2B2Destination;
import org.eurekaclinical.eureka.client.comm.Neo4jDestination;
import org.eurekaclinical.eureka.client.comm.PatientSetExtractorDestination;
import java.util.ArrayList;
import java.util.List;
import org.eurekaclinical.eureka.client.comm.PatientSetSenderDestination;
import org.eurekaclinical.eureka.client.comm.RelDbDestination;
import org.eurekaclinical.eureka.client.comm.TableColumn;
import org.eurekaclinical.eureka.client.comm.TabularFileDestination;

import org.eurekaclinical.protempa.client.comm.EtlCohortDestination;
import org.eurekaclinical.protempa.client.comm.EtlDestination;
import org.eurekaclinical.protempa.client.comm.EtlI2B2Destination;
import org.eurekaclinical.protempa.client.comm.EtlNeo4jDestination;
import org.eurekaclinical.protempa.client.comm.EtlPatientSetExtractorDestination;
import org.eurekaclinical.protempa.client.comm.EtlPatientSetSenderDestination;
import org.eurekaclinical.protempa.client.comm.EtlTableColumn;
import org.eurekaclinical.protempa.client.comm.EtlTabularFileDestination;

/**
 *
 * @author Andrew Post
 */
public class DestinationToEtlDestinationVisitor extends AbstractDestinationVisitor {

	private EtlDestination etlDestination;
	private final ConversionSupport conversionSupport;

	public DestinationToEtlDestinationVisitor(ConversionSupport inConversionSupport) {
		this.conversionSupport = inConversionSupport;
	}
	
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
		PhenotypeField[] etlDestPhenotypeFields
				= destination.getPhenotypeFields();
		if (etlDestPhenotypeFields != null) {
			etlDestination.setPhenotypeFields(etlDestPhenotypeFields);
		}
		etlDestination.setOwnerUserId(destination.getOwnerUserId());
		etlDestination.setRead(destination.isRead());
		etlDestination.setWrite(destination.isWrite());
		etlDestination.setExecute(destination.isExecute());
		etlDestination.setLinks(destination.getLinks());
		etlDestination.setGetStatisticsSupported(destination.isGetStatisticsSupported());
		List<String> requiredConcepts = destination.getRequiredConcepts();

		if (requiredConcepts != null) {
			List<String> requiredPropIds = new ArrayList<>(requiredConcepts.size());
			for (String requiredConcept : requiredConcepts) {
				requiredPropIds.add(this.conversionSupport.toPropositionId(requiredConcept));
			}
			etlDestination.setRequiredPropositionIds(requiredPropIds);
		}
		etlDestination.setAllowingQueryPropositionIds(destination.isJobConceptListSupported());
	}

	@Override
	public void visit(PatientSetExtractorDestination patientSetExtractorDestination) {
		EtlPatientSetExtractorDestination etlPtSetExtractorDest = new EtlPatientSetExtractorDestination();
		etlPtSetExtractorDest.setAliasPropositionId(patientSetExtractorDestination.getAliasPropositionId());
		etlPtSetExtractorDest.setAliasFieldName(patientSetExtractorDestination.getAliasFieldName());
		etlPtSetExtractorDest.setAliasFieldNameProperty(patientSetExtractorDestination.getAliasFieldNameProperty());
		etlPtSetExtractorDest.setAliasPatientIdProperty(patientSetExtractorDestination.getAliasPatientIdProperty());
		visitCommon(patientSetExtractorDestination, etlPtSetExtractorDest);
		this.etlDestination = etlPtSetExtractorDest;
	}
	
	@Override
	public void visit(PatientSetSenderDestination patientSetSenderDestination) {
		EtlPatientSetSenderDestination etlPtSetSenderDest = new EtlPatientSetSenderDestination();
		etlPtSetSenderDest.setAliasPropositionId(patientSetSenderDestination.getAliasPropositionId());
		etlPtSetSenderDest.setAliasFieldName(patientSetSenderDestination.getAliasFieldName());
		etlPtSetSenderDest.setAliasFieldNameProperty(patientSetSenderDestination.getAliasFieldNameProperty());
		etlPtSetSenderDest.setAliasPatientIdProperty(patientSetSenderDestination.getAliasPatientIdProperty());
		etlPtSetSenderDest.setPatientSetService(patientSetSenderDestination.getPatientSetService());
		visitCommon(patientSetSenderDestination, etlPtSetSenderDest);
		this.etlDestination = etlPtSetSenderDest;
	}

	@Override
	public void visit(TabularFileDestination tabularFileDestination) {
		EtlTabularFileDestination etlTabularFileDest = new EtlTabularFileDestination();
		List<EtlTableColumn> etlTableColumns = new ArrayList<>();
		for (TableColumn tableColumn : tabularFileDestination.getTableColumns()) {
			EtlTableColumn etlTableColumn = new EtlTableColumn();
			etlTableColumn.setTableName(tableColumn.getTableName());
			etlTableColumn.setColumnName(tableColumn.getColumnName());
			etlTableColumn.setPath(tableColumn.getPath());
			etlTableColumn.setFormat(tableColumn.getFormat());
			etlTableColumns.add(etlTableColumn);
		}
		etlTabularFileDest.setTableColumns(etlTableColumns);
		visitCommon(tabularFileDestination, etlTabularFileDest);
		this.etlDestination = etlTabularFileDest;
	}

	@Override
	public void visit(RelDbDestination relDbDestination) {
	}
	
}
