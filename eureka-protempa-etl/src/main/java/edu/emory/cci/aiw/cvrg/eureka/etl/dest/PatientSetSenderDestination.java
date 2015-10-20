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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetSenderDestinationEntity;
import org.protempa.DataSource;
import org.protempa.KnowledgeSource;
import org.protempa.dest.AbstractDestination;
import org.protempa.dest.QueryResultsHandler;
import org.protempa.dest.QueryResultsHandlerInitException;
import org.protempa.query.Query;
import org.protempa.query.QueryMode;

/**
 *
 * @author Andrew Post
 */
public class PatientSetSenderDestination extends AbstractDestination {
	private final PatientSetSenderDestinationEntity patientSetSenderDestinationEntity;
	private final String[] propIdsSupported;

	PatientSetSenderDestination(PatientSetSenderDestinationEntity inPatientSetSenderDestinationEntity) {
		assert inPatientSetSenderDestinationEntity != null : "inPatientSetSenderDestinationEntity cannot be null";
		this.patientSetSenderDestinationEntity = inPatientSetSenderDestinationEntity;
		this.propIdsSupported = new String[] {this.patientSetSenderDestinationEntity.getAliasPropositionId()};
	}

	@Override
	public QueryResultsHandler getQueryResultsHandler(Query query, DataSource dataSource, KnowledgeSource knowledgeSource) throws QueryResultsHandlerInitException {
		if (query.getQueryMode() == QueryMode.UPDATE) {
			throw new QueryResultsHandlerInitException("Update mode not supported");
		}
		return new PatientSetSenderQueryResultsHandler(query, this.patientSetSenderDestinationEntity);
	}
	
	@Override
	public String[] getSupportedPropositionIds(DataSource dataSource, KnowledgeSource knowledgeSource) {
		return this.propIdsSupported.clone();
	}

}
