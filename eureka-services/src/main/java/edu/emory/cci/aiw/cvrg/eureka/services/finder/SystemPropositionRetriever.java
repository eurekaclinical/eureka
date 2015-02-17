/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.services.config.EtlClient;
import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Retrieves proposition definitions from the ETL layer.
 *
 * @author hrathod
 */
public class SystemPropositionRetriever implements
		PropositionRetriever<String> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SystemPropositionRetriever.class);
	private final EtlClient etlClient;

	@Inject
	public SystemPropositionRetriever(EtlClient inEtlClient) {
		this.etlClient = inEtlClient;
	}

	@Override
	public PropositionDefinition retrieve(String sourceConfigId, String inKey)
			throws PropositionFindException {
		PropositionDefinition result;
		try {
			result = etlClient.getPropositionDefinition(sourceConfigId, inKey);
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
			ClientResponse.Status status = e.getResponseStatus();
			if (status == ClientResponse.Status.NOT_FOUND) {
				result = null;
			} else {
				throw new PropositionFindException(
						"Could not retrieve proposition definition " + inKey, e);
			}
		}
		return result;
	}

	/**
	 * Retrieves all of the system elements given by the keys for the given user
	 *
	 * @param sourceConfigId the ID of the source config to use for the look up
	 * @param inKeys the keys of the system elements to retrieve
	 * @param withChildren whether the children of the given elements should be
	 * retrieved as well
	 * @return a {@link List} of {@link SystemElement}s
	 * @throws PropositionFindException
	 */
	public List<PropositionDefinition> retrieveAll(
			String sourceConfigId, List<String> inKeys, Boolean withChildren) throws PropositionFindException {
		List<PropositionDefinition> result = new ArrayList<>();
		try {
			result = etlClient.getPropositionDefinitions(sourceConfigId, inKeys, withChildren);
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
			ClientResponse.Status status = e.getResponseStatus();
			if (status != ClientResponse.Status.NOT_FOUND) {
				throw new PropositionFindException(
						"Could not retrieve proposition definitions " + StringUtils.join(inKeys, ", "), e);
			}
		}
		return result;

	}
}
