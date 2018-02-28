/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import com.sun.jersey.api.client.ClientResponse;

import org.eurekaclinical.eureka.client.comm.SystemPhenotype;
import org.eurekaclinical.protempa.client.EurekaClinicalProtempaClient;
import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.eurekaclinical.common.comm.clients.ClientException;

/**
 * Retrieves proposition definitions from the ETL layer.
 *
 * @author hrathod
 */
public class SystemPropositionRetriever implements
		PropositionRetriever<String> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SystemPropositionRetriever.class);
	private final EurekaClinicalProtempaClient etlClient;

	@Inject
	public SystemPropositionRetriever(EurekaClinicalProtempaClient inEtlClient) {
		this.etlClient = inEtlClient;
	}

	@Override
	public PropositionDefinition retrieve(String sourceConfigId, String inKey)
			throws PropositionFindException {
		PropositionDefinition result;
		try {
			result = this.etlClient.getPropositionDefinition(sourceConfigId, inKey);
		} catch (ClientException e) {
			ClientResponse.Status status = e.getResponseStatus();
			if (status == ClientResponse.Status.NOT_FOUND) {
				result = null;
			} else {
				LOGGER.error(e.getMessage(), e);
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
	 * @return a {@link List} of {@link SystemPhenotype}s
	 * @throws PropositionFindException
	 */
	public List<PropositionDefinition> retrieveAll(
			String sourceConfigId, List<String> inKeys, Boolean withChildren) throws PropositionFindException {
		List<PropositionDefinition> result = new ArrayList<>();
		try {
			result = this.etlClient.getPropositionDefinitions(sourceConfigId, inKeys, withChildren);
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
