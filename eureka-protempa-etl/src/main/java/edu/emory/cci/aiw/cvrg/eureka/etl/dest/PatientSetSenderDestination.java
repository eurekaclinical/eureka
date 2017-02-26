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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetSenderDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.util.List;
import org.protempa.DataSource;
import org.protempa.KnowledgeSource;
import org.protempa.ProtempaEventListener;
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
	private final EtlProperties etlProperties;

	PatientSetSenderDestination(EtlProperties inEtlProperties, PatientSetSenderDestinationEntity inPatientSetSenderDestinationEntity) {
		assert inPatientSetSenderDestinationEntity != null : "inPatientSetSenderDestinationEntity cannot be null";
		this.patientSetSenderDestinationEntity = inPatientSetSenderDestinationEntity;
		this.propIdsSupported = new String[] {this.patientSetSenderDestinationEntity.getAliasPropositionId()};
		this.etlProperties = inEtlProperties;
	}

	@Override
	public QueryResultsHandler getQueryResultsHandler(Query query, DataSource dataSource, KnowledgeSource knowledgeSource, List<? extends ProtempaEventListener> eventListeners) throws QueryResultsHandlerInitException {
		if (query.getQueryMode() == QueryMode.UPDATE) {
			throw new QueryResultsHandlerInitException("Update mode not supported");
		}
		return new PatientSetSenderQueryResultsHandler(query, this.patientSetSenderDestinationEntity, this.etlProperties);
	}
	
	@Override
	public String[] getSupportedPropositionIds(DataSource dataSource, KnowledgeSource knowledgeSource) {
		return this.propIdsSupported.clone();
	}

}
