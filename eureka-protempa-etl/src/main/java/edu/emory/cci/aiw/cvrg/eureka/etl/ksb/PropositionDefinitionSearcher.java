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

import java.util.*;

import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.PropositionDefinition;
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

import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;

public class PropositionDefinitionSearcher {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PropositionDefinitionSearcher.class);
	private static final Map<String, List<PropositionDefinition>> parentsCache = new HashMap<>();
	private final KnowledgeSource knowledgeSource;
	private final EtlProperties etlProperties;
	private final Set<String> defaultProps;

	public PropositionDefinitionSearcher(String configId,
										 EtlProperties etlProps) throws PropositionFinderException {
		this.etlProperties = etlProps;
		try {
			LOGGER
					.debug("Creating new configurations, source factory, and knowledge source");
			Configurations configurations = new EurekaProtempaConfigurations(
					etlProps);
			SourceFactory sf = new SourceFactory(configurations, configId);
			this.knowledgeSource = sf.newKnowledgeSourceInstance();
			defaultProps = new HashSet<>(this.etlProperties.getDefaultSystemPropositions());
			LOGGER
					.debug("Done: configurations, source factory, and knowledge source created");
		} catch (ConfigurationsNotFoundException | BackendProviderSpecLoaderException | InvalidConfigurationException |
				ConfigurationsLoadException | BackendNewInstanceException | BackendInitializationException ex) {
			throw new PropositionFinderException(ex);
		}
	}

	public List<String> searchPropositions(String inSearchKey) throws PropositionFinderException {
		LinkedHashSet<String> nodesToLoad = new LinkedHashSet<>();
		try {
			List<PropositionDefinition> searchResults = knowledgeSource.getMatchingPropositionDefinitions(inSearchKey);
			for (PropositionDefinition pf : searchResults) {
				if (nodesToLoad.size() > etlProperties.getSearchLimit()) {
					break;
				} else {
					if (pf != null) {
						readParentsForSearchResult(pf, nodesToLoad);
					}
				}
			}
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
		return new ArrayList<>(nodesToLoad);
	}

	private void readParentsForSearchResult(PropositionDefinition pf, LinkedHashSet<String> nodesToLoad) throws PropositionFinderException {
		try {
			Queue<PropositionDefinition> toProcessQueue = new LinkedList<>();
			Stack<String> processedStack = new Stack<>();
			toProcessQueue.add(pf);
			while (!toProcessQueue.isEmpty()) {
				PropositionDefinition currentPropDef = toProcessQueue.remove();
				List<PropositionDefinition> parents = parentsCache.get(currentPropDef.getId());
				if (parents == null) {
					parents = knowledgeSource.readParents(currentPropDef);
					parentsCache.put(currentPropDef.getId(), parents);
				}
				for (PropositionDefinition parent : parents) {
					toProcessQueue.add(parent);
					processedStack.add(parent.getId());
				}
			}
			getNodesToLoad(processedStack, nodesToLoad);
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
	}

	private void getNodesToLoad(Stack<String> processedStack, LinkedHashSet<String> nodesToLoad) {
		while (!processedStack.empty()) {
			String node = processedStack.pop();
			if (!nodesToLoad.contains(node)) {
				if (defaultProps.contains(node)) {
					nodesToLoad.add(node);
				} else {
					List<PropositionDefinition> parents = parentsCache.get(node);
					if (parents != null) {
						for (PropositionDefinition parent : parents) {
							if (nodesToLoad.contains(parent.getId())) {
								nodesToLoad.add(node);
								break;
							}
						}
					}
				}
			}
		}
	}
}