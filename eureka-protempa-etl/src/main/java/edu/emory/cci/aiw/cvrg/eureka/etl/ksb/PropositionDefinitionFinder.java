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

public class PropositionDefinitionFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropositionDefinitionFinder.class);
	private final KnowledgeSource knowledgeSource;

	public PropositionDefinitionFinder(String configId,
			EtlProperties etlProperties) throws PropositionFinderException {
		File etlConfDir = etlProperties.getSourceConfigDirectory();
		this.validateConfigDir(etlConfDir);
		try {
			LOGGER.debug("Creating new configurations, source factory, and knowledge source");
			Configurations configurations = 
					new EurekaProtempaConfigurations(etlProperties);
			SourceFactory sf = new SourceFactory(configurations, configId);
			this.knowledgeSource = sf.newKnowledgeSourceInstance();
			LOGGER.debug("Done: configurations, source factory, and knowledge source created");
		} catch (ConfigurationsNotFoundException | BackendInitializationException | BackendNewInstanceException | ConfigurationsLoadException | InvalidConfigurationException | BackendProviderSpecLoaderException ex) {
			throw new PropositionFinderException(ex);
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
}
