package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.protempa.PropositionDefinition;
import org.protempa.dest.AbstractQueryResultsHandler;
import org.protempa.dest.QueryResultsHandlerCloseException;
import org.protempa.dest.QueryResultsHandlerProcessingException;
import org.protempa.dest.QueryResultsHandlerValidationFailedException;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.UniqueId;
import org.protempa.proposition.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public class PatientSetSenderQueryResultsHandler extends AbstractQueryResultsHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientSetSenderQueryResultsHandler.class);
	private final String url;
	private final String[] propIdsNeeded;
	private final String aliasPropId;
	private final String aliasFieldNameProperty;
	private final String aliasFieldName;
	private final String aliasPatientIdPropertyName;

	PatientSetSenderQueryResultsHandler(String url, String aliasPropId, String aliasFieldNameProperty, String aliasFieldName, String aliasPatientIdPropertyName) {
		assert url != null : "url cannot be null";
		assert aliasPropId != null : "aliasPropId cannot be null";
		assert aliasFieldNameProperty != null : "aliasFieldNameProperty cannot be null";
		assert aliasFieldName != null : "aliasFieldName cannot be null";
		assert aliasPatientIdPropertyName != null : "aliasPatientIdPropertyName cannot be null";
		
		this.url = url;
		this.propIdsNeeded = new String[]{aliasPropId};
		this.aliasPropId = aliasPropId;
		this.aliasFieldNameProperty = aliasFieldNameProperty;
		this.aliasFieldName = aliasFieldName;
		this.aliasPatientIdPropertyName = aliasPatientIdPropertyName;
	}

	@Override
	public void validate() throws QueryResultsHandlerValidationFailedException {
		try {
			new URL(this.url);
		} catch (MalformedURLException ex) {
			throw new QueryResultsHandlerValidationFailedException(ex);
		}
	}

	@Override
	public void start(Collection<PropositionDefinition> cache) throws QueryResultsHandlerProcessingException {
		LOGGER.error("got url {}", this.url);
	}

	@Override
	public void handleQueryResult(String keyId, List<Proposition> propositions, Map<Proposition, List<Proposition>> forwardDerivations, Map<Proposition, List<Proposition>> backwardDerivations, Map<UniqueId, Proposition> references) throws QueryResultsHandlerProcessingException {
		LOGGER.error("got key id " + keyId);
		for (Proposition proposition : propositions) {
			String propId = proposition.getId();
			if (propId.equals(this.aliasPropId)) {
				Value aliasFieldNameVal = proposition.getProperty(this.aliasFieldNameProperty);
				if (aliasFieldNameVal != null && aliasFieldNameVal.getFormatted().equals(this.aliasFieldName)) {
					LOGGER.error("got patient id " + proposition.getProperty(this.aliasPatientIdPropertyName));
				}
			}
		}
	}

	@Override
	public void finish() throws QueryResultsHandlerProcessingException {
		LOGGER.error("all done!");
	}

	@Override
	public void close() throws QueryResultsHandlerCloseException {
		LOGGER.error("closing");
	}

	@Override
	public String[] getPropositionIdsNeeded() throws QueryResultsHandlerProcessingException {
		return this.propIdsNeeded.clone();
	}

}
