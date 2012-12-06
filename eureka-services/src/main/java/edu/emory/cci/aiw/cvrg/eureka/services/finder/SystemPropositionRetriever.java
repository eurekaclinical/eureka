/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.EtlClient;
import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;

public class SystemPropositionRetriever implements PropositionRetriever<Long,
		String> {

	private static final Logger LOGGER = LoggerFactory.getLogger
			(SystemPropositionRetriever.class);
	private final ServiceProperties applicationProperties;

	@Inject
	public SystemPropositionRetriever(ServiceProperties inProperties) {
		this.applicationProperties = inProperties;
	}

	@Override
	public PropositionDefinition retrieve(Long inUserId, String inKey) {
		PropositionDefinition propDef = null;

		EtlClient etlClient = new EtlClient(
				this.applicationProperties.getEtlUrl());
		PropositionDefinition result;
		try {;
			result = etlClient.getPropositionDefinition(inUserId, inKey);
		} catch (ClientException e) {
			LOGGER.error(e.getMessage(), e);
			result = null;
		}
		return result;
	}
}
