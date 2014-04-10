package edu.emory.cci.aiw.cvrg.eureka.etl.ksb;

/*
 * #%L
 * Eureka Protempa ETL
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.SourceFactory;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.ConfigurationsNotFoundException;
import org.protempa.backend.InvalidConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.filter.SearchFilter;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;

public class PropositionDefinitionSearcher {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionDefinitionSearcher.class);
	private final KnowledgeSource knowledgeSource;
    private final EtlProperties etlProperties;
	public PropositionDefinitionSearcher(String configId,
			EtlProperties etlProps) throws PropositionFinderException {
		File etlConfDir = etlProps.getSourceConfigDirectory();
        this.etlProperties = etlProps;
		try {
			LOGGER
					.debug("Creating new configurations, source factory, and knowledge source");
			Configurations configurations = new EurekaProtempaConfigurations(
					etlProps);
			SourceFactory sf = new SourceFactory(configurations, configId);
			this.knowledgeSource = sf.newKnowledgeSourceInstance();
			LOGGER
					.debug("Done: configurations, source factory, and knowledge source created");
		} catch (ConfigurationsNotFoundException | BackendProviderSpecLoaderException | InvalidConfigurationException |
				ConfigurationsLoadException | BackendNewInstanceException| BackendInitializationException ex) {
			throw new PropositionFinderException(ex);
		}
	}

	public List<String> searchPropositions(String inSearchKey,
										   SearchFilter searchFilter)
			throws PropositionFinderException {
		try {
			List<String> filteredSearchResults = new ArrayList<String>();
			int searchLimit = etlProperties.getSearchLimit();
			List<List<String>> searchResults = knowledgeSource
					.getMatchingPropositionDefinitions(inSearchKey);
			for (int indexForList = 0; indexForList < searchResults.size(); indexForList++) {
				List<String> currentSearchResult = searchResults
						.get(indexForList);

				for (int indexForCurrentSearch = currentSearchResult.size() - 1; indexForCurrentSearch >= 0; indexForCurrentSearch--) {
					if (searchFilter.filter(currentSearchResult.get(indexForCurrentSearch)) && filteredSearchResults.size() < searchLimit) {
						for (int secondIndexForCurrentSearch = indexForCurrentSearch; secondIndexForCurrentSearch >= 0; secondIndexForCurrentSearch--) {
							String nodeName = currentSearchResult.get(secondIndexForCurrentSearch);
							if (!filteredSearchResults.contains(nodeName)) {
								filteredSearchResults.add(nodeName);
							}
						}
						break;
					}
				}
				if (filteredSearchResults.size() >= searchLimit) {
					break;
				}
			}
			return filteredSearchResults;
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}

	}
}