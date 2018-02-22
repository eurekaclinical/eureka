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
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.PatientSetExtractorDestinationEntity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.eurekaclinical.patientset.client.PatientSetJsonWriter;
import org.protempa.PropositionDefinition;
import org.protempa.dest.QueryResultsHandlerCloseException;
import org.protempa.dest.QueryResultsHandlerProcessingException;
import org.protempa.dest.QueryResultsHandlerValidationFailedException;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.UniqueId;
import org.protempa.query.Query;

/**
 *
 * @author Andrew Post
 */
public class PatientSetExtractorQueryResultsHandler extends AbstractFileQueryResultsHandler {

	private final String queryId;
	private final String username;
	private final PatientIdExtractor patientIdExtractor;
	private PatientSetJsonWriter jsonGenerator;

	PatientSetExtractorQueryResultsHandler(Query query, PatientSetExtractorDestinationEntity inPatientSetSenderDestinationEntity, EtlProperties inEtlProperties) {
		super(inPatientSetSenderDestinationEntity, inEtlProperties);
		assert inPatientSetSenderDestinationEntity != null : "inPatientSetSenderDestinationEntity cannot be null";
		
		this.patientIdExtractor = new PatientIdExtractor(inPatientSetSenderDestinationEntity);

		this.queryId = query.getName();
		this.username = query.getUsername();
	}

	@Override
	public void validate() throws QueryResultsHandlerValidationFailedException {
	}

	@Override
	public void start(OutputStream outputFileOutputStream, Collection<PropositionDefinition> cache) throws QueryResultsHandlerProcessingException {
		try {
			this.jsonGenerator = new PatientSetJsonWriter(outputFileOutputStream, this.queryId, this.username);
		} catch (IOException ex) {
			throw new QueryResultsHandlerProcessingException("Error starting output", ex);
		}
	}

	@Override
	public void handleQueryResult(String keyId, List<Proposition> propositions, Map<Proposition, List<Proposition>> forwardDerivations, Map<Proposition, List<Proposition>> backwardDerivations, Map<UniqueId, Proposition> references) throws QueryResultsHandlerProcessingException {
		try {
			this.jsonGenerator.writePatient(this.patientIdExtractor.extract(keyId, propositions));
		} catch (IOException ex) {
			throw new QueryResultsHandlerProcessingException("Error writing output", ex);
		}
	}

	@Override
	public void finish() throws QueryResultsHandlerProcessingException {
		try {
			this.jsonGenerator.finish();
		} catch (IOException ex) {
			throw new QueryResultsHandlerProcessingException(ex);
		}
	}

	@Override
	public void cleanup() throws QueryResultsHandlerCloseException {
		if (this.jsonGenerator != null) {
			try {
				this.jsonGenerator.close();
			} catch (IOException ex) {
				throw new QueryResultsHandlerCloseException("Error closing", ex);
			}
		}
	}

}
