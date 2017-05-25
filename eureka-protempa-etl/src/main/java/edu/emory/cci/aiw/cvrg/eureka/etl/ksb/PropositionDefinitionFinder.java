/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.ksb;

import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.collections4.map.ReferenceMap;
import org.protempa.SourceCloseException;

public class PropositionDefinitionFinder implements AutoCloseable {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropositionDefinitionFinder.class);
	private static final Map<String, List<String>> parentsCache = new ReferenceMap<>();
	private final KnowledgeSource knowledgeSource;
	private final EtlProperties etlProperties;
	private final Set<String> defaultProps;

	public PropositionDefinitionFinder(String configId,
			EtlProperties etlProperties) throws PropositionFinderException {
		this.etlProperties = etlProperties;
		try {
			this.validateConfigDir(etlProperties.getSourceConfigDirectory());
			LOGGER.debug("Creating new configurations, source factory, and knowledge source");
			Configurations configurations
					= new EurekaProtempaConfigurations(etlProperties);
			SourceFactory sf = new SourceFactory(configurations, configId);
			this.knowledgeSource = sf.newKnowledgeSourceInstance();
			this.defaultProps = new HashSet<>(this.etlProperties.getDefaultSystemPropositions());
			LOGGER.debug("Done: configurations, source factory, and knowledge source created");
		} catch (IOException | ConfigurationsNotFoundException | BackendInitializationException | BackendNewInstanceException | ConfigurationsLoadException | InvalidConfigurationException | BackendProviderSpecLoaderException ex) {
			throw new PropositionFinderException(ex);
		}
	}

	public List<PropositionDefinition> findAll(Collection<String> propIds) throws PropositionFinderException {
		try {
			return this.knowledgeSource.readPropositionDefinitions(propIds.toArray(new String[propIds.size()]));
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
	}

	public PropositionDefinition find(String inKey) throws
			PropositionFinderException {
		PropositionDefinition definition = null;
		try {
			definition = this.knowledgeSource.readPropositionDefinition(inKey);
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
		return definition;
	}

	public List<String> getPropIdsBySearchKey(String inSearchKey) throws PropositionFinderException {
		LinkedHashSet<String> nodesToLoad = new LinkedHashSet<>();
		try {
			List<String> searchResults = knowledgeSource.getMatchingPropIds(inSearchKey);
			for (String pf : searchResults) {
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


	public List<PropositionDefinition> getPropositionDefinitionsBySearchKey(String inSearchKey) throws PropositionFinderException {
		List<PropositionDefinition> nodesToLoad = new ArrayList<>();
		List<String> propIds = getPropIdsBySearchKey(inSearchKey);
		
		try {
			nodesToLoad = knowledgeSource.readPropositionDefinitions(propIds.toArray(new String[propIds.size()]));
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
		return nodesToLoad;
	}

	@Override
	public void close() throws PropositionFinderException {
		try {
			this.knowledgeSource.close();
		} catch (SourceCloseException ex) {
			throw new PropositionFinderException(ex);
		}
	}

	private void readParentsForSearchResult(String propId, LinkedHashSet<String> nodesToLoad) throws PropositionFinderException {
		try {
			Queue<String> toProcessQueue = new LinkedList<>();
			Stack<String> processedStack = new Stack<>();
			toProcessQueue.add(propId);
			while (!toProcessQueue.isEmpty()) {
				String currentPropId = toProcessQueue.remove();
				List<String> parents;
				synchronized (parentsCache) {
					parents = parentsCache.get(currentPropId);
					if (parents == null) {
						parents = knowledgeSource.readParentPropIds(currentPropId);
						parentsCache.put(currentPropId, parents);
					}
				}
				for (String parent : parents) {
					toProcessQueue.add(parent);
					processedStack.add(parent);
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
					List<String> parents;
					synchronized (parentsCache) {
						parents = parentsCache.get(node);
					}
					if (parents != null) {
						for (String parent : parents) {
							if (nodesToLoad.contains(parent)) {
								nodesToLoad.add(node);
								break;
							}
						}
					}
				}
			}
		}
	}

	private void validateConfigDir(File inFile) {
		if (LOGGER.isErrorEnabled()) {
			if (!inFile.exists()) {
				LOGGER.error(
						"Configuration directory " + inFile.getAbsolutePath()
						+ " does not exist. Proposition finding will not work"
						+ " without it. Please create it and try again.");
			} else if (!inFile.isDirectory()) {
				LOGGER.error(
						"Path " + inFile.getAbsolutePath() + " is not a "
						+ "directory. Proposition finding requires it to be a"
						+ " directory.");
			}
		}
	}

}
