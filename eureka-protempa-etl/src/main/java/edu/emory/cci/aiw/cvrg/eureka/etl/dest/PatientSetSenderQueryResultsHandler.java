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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PatientSet;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetSenderDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.protempa.PropositionDefinition;
import org.protempa.dest.AbstractQueryResultsHandler;
import org.protempa.dest.QueryResultsHandlerCloseException;
import org.protempa.dest.QueryResultsHandlerProcessingException;
import org.protempa.dest.QueryResultsHandlerValidationFailedException;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.UniqueId;
import org.protempa.proposition.value.Value;
import org.protempa.query.Query;

/**
 *
 * @author Andrew Post
 */
public class PatientSetSenderQueryResultsHandler extends AbstractQueryResultsHandler {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final String name;
	private final String url;
	private final String aliasPropId;
	private final String aliasFieldNameProperty;
	private final String aliasFieldName;
	private final String aliasPatientIdPropertyName;
	private final EtlProperties etlProperties;
	private final String queryId;
	private final String username;
	private final String outputName;

	PatientSetSenderQueryResultsHandler(Query query, PatientSetSenderDestinationEntity inPatientSetSenderDestinationEntity) {
		assert inPatientSetSenderDestinationEntity != null : "inPatientSetSenderDestinationEntity cannot be null";
		this.name = inPatientSetSenderDestinationEntity.getName();

		this.url = inPatientSetSenderDestinationEntity.getUrl();
		this.aliasPropId = inPatientSetSenderDestinationEntity.getAliasPropositionId();
		this.aliasFieldNameProperty = inPatientSetSenderDestinationEntity.getAliasFieldNameProperty();
		this.aliasFieldName = inPatientSetSenderDestinationEntity.getAliasFieldName();
		this.aliasPatientIdPropertyName = inPatientSetSenderDestinationEntity.getAliasPatientIdProperty();

		assert url != null : "url cannot be null";
		assert aliasPropId != null : "aliasPropId cannot be null";
		assert aliasFieldNameProperty != null : "aliasFieldNameProperty cannot be null";
		assert aliasFieldName != null : "aliasFieldName cannot be null";
		assert aliasPatientIdPropertyName != null : "aliasPatientIdPropertyName cannot be null";

		this.outputName = new PatientSetSenderSupport().getOutputName(inPatientSetSenderDestinationEntity);

		this.queryId = query.getId();
		this.username = query.getUsername();

		this.etlProperties = new EtlProperties();

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
	}

	@Override
	public void handleQueryResult(String keyId, List<Proposition> propositions, Map<Proposition, List<Proposition>> forwardDerivations, Map<Proposition, List<Proposition>> backwardDerivations, Map<UniqueId, Proposition> references) throws QueryResultsHandlerProcessingException {
		File outputFile = new File(etlProperties.outputFileDirectory(this.name), this.outputName);
		PatientSet patientSet = new PatientSet();
		patientSet.setName(this.queryId);
		patientSet.setUsername(this.username);
		List<String> keyIds = new ArrayList<>();
		try {
			for (Proposition proposition : propositions) {
				String propId = proposition.getId();
				if (propId.equals(this.aliasPropId)) {
					Value aliasFieldNameVal = proposition.getProperty(this.aliasFieldNameProperty);
					if (aliasFieldNameVal != null && aliasFieldNameVal.getFormatted().equals(this.aliasFieldName)) {
						Value val = proposition.getProperty(this.aliasPatientIdPropertyName);
						if (val != null) {
							keyIds.add(val.getFormatted());
						}
					}
				}
			}
			patientSet.setPatients(keyIds);
			MAPPER.writeValue(outputFile, patientSet);
		} catch (IOException ex) {
			throw new QueryResultsHandlerProcessingException(ex);
		}
	}

	@Override
	public void finish() throws QueryResultsHandlerProcessingException {
	}

	@Override
	public void close() throws QueryResultsHandlerCloseException {
	}

}
