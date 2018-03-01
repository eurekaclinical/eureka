package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*-
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2018 Emory University
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
import org.eurekaclinical.eureka.client.comm.TableColumn;
import org.eurekaclinical.eureka.client.comm.TabularFileDestination;

import org.eurekaclinical.protempa.client.comm.AbstractEtlDestinationVisitor;
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
public class EtlDestinationToDestinationVisitor extends AbstractEtlDestinationVisitor {

	private Destination destination;
	private final ConversionSupport conversionSupport;

	public EtlDestinationToDestinationVisitor(ConversionSupport conversionSupport) {
		this.conversionSupport = conversionSupport;
	}

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
		PhenotypeField[] etlDestPhenotypeFields
				= etlDestination.getPhenotypeFields();
		if (etlDestPhenotypeFields != null) {
			destination.setPhenotypeFields(etlDestPhenotypeFields);
		}
		destination.setOwnerUserId(etlDestination.getOwnerUserId());
		destination.setRead(etlDestination.isRead());
		destination.setWrite(etlDestination.isWrite());
		destination.setExecute(etlDestination.isExecute());
		destination.setCreatedAt(etlDestination.getCreatedAt());
		destination.setUpdatedAt(etlDestination.getUpdatedAt());
		destination.setLinks(etlDestination.getLinks());
		destination.setGetStatisticsSupported(etlDestination.isGetStatisticsSupported());
		destination.setJobConceptListSupported(etlDestination.isAllowingQueryPropositionIds());
		List<String> requiredPropIds = etlDestination.getRequiredPropositionIds();
		List<String> requiredConcepts = new ArrayList<>(requiredPropIds != null ? requiredPropIds.size() : 0);
		if (requiredPropIds != null) {
			for (String requiredPropId : requiredPropIds) {
				if (requiredPropId != null) {
					requiredConcepts.add(this.conversionSupport.toPhenotypeKey(requiredPropId));
				}
			}
		}
		destination.setRequiredConcepts(requiredConcepts);
	}

	@Override
	public void visit(EtlPatientSetExtractorDestination etlPatientSetExtractorDestination) {
		PatientSetExtractorDestination ptSetExtractorDest = new PatientSetExtractorDestination();
		visitCommon(etlPatientSetExtractorDestination, ptSetExtractorDest);
		ptSetExtractorDest.setAliasPropositionId(etlPatientSetExtractorDestination.getAliasPropositionId());
		ptSetExtractorDest.setAliasFieldName(etlPatientSetExtractorDestination.getAliasFieldName());
		ptSetExtractorDest.setAliasFieldNameProperty(etlPatientSetExtractorDestination.getAliasFieldNameProperty());
		ptSetExtractorDest.setAliasPatientIdProperty(etlPatientSetExtractorDestination.getAliasPatientIdProperty());
		this.destination = ptSetExtractorDest;
	}
	
	@Override
	public void visit(EtlPatientSetSenderDestination etlPatientSetSenderDestination) {
		PatientSetSenderDestination ptSetExtractorDest = new PatientSetSenderDestination();
		visitCommon(etlPatientSetSenderDestination, ptSetExtractorDest);
		ptSetExtractorDest.setAliasPropositionId(etlPatientSetSenderDestination.getAliasPropositionId());
		ptSetExtractorDest.setAliasFieldName(etlPatientSetSenderDestination.getAliasFieldName());
		ptSetExtractorDest.setAliasFieldNameProperty(etlPatientSetSenderDestination.getAliasFieldNameProperty());
		ptSetExtractorDest.setAliasPatientIdProperty(etlPatientSetSenderDestination.getAliasPatientIdProperty());
		ptSetExtractorDest.setPatientSetService(etlPatientSetSenderDestination.getPatientSetService());
		this.destination = ptSetExtractorDest;
	}
	
	@Override
	public void visit(EtlTabularFileDestination etlTabularFileDestination) {
		TabularFileDestination tabularFileDest = new TabularFileDestination();
		visitCommon(etlTabularFileDestination, tabularFileDest);
		List<TableColumn> tableColumns = new ArrayList<>();
		for (EtlTableColumn etlTableColumn : etlTabularFileDestination.getTableColumns()) {
			TableColumn tableColumn = new TableColumn();
			tableColumn.setTableName(etlTableColumn.getTableName());
			tableColumn.setColumnName(etlTableColumn.getColumnName());
			tableColumn.setPath(etlTableColumn.getPath());
			tableColumn.setFormat(etlTableColumn.getFormat());
			tableColumns.add(tableColumn);
		}
		tabularFileDest.setTableColumns(tableColumns);
		this.destination = tabularFileDest;
	}

}
