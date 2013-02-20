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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.PropositionDefinition;
import org.protempa.SourceFactory;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendNewInstanceException;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.Configurations;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.InvalidConfigurationException;
import org.protempa.bconfigs.commons.INICommonsConfigurations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;

public class PropositionFinder {

	private static final Logger LOGGER = LoggerFactory.getLogger
		(PropositionFinder.class);
	private static final String CONFIG_DIR = "etlconfig";
	private static final String DEFAULT_CONFIG_DIR = "etlconfigdefaults";
	private static final String CONF_PREFIX = "config";
	private static final String DEFAULT_CONF_FILE = "defaults.ini";
	private final KnowledgeSource knowledgeSource;
	
	public PropositionFinder(Configuration inConfiguration,
		String confDir) throws PropositionFinderException {
		File etlConfDir = new File(confDir + "/" + CONFIG_DIR);
		File defaultEtlConfDir = new File(confDir + "/" + DEFAULT_CONFIG_DIR);
		this.validateConfigDir(etlConfDir);
		Long confId = inConfiguration.getId();
		try {
			String idStr = String.valueOf(confId.longValue());
			String confFileName = CONF_PREFIX + idStr + ".ini";
			LOGGER.debug("Creating new configurations, source factory, and knowledge source");
			Configurations configurations = new INICommonsConfigurations
				(etlConfDir);
			SourceFactory sf = new SourceFactory(
				configurations, confFileName);
			KnowledgeSource ks = sf.newKnowledgeSourceInstance();
			if (ks == null && defaultEtlConfDir.exists()) {
				File defaultConfFile = new File(
					defaultEtlConfDir, DEFAULT_CONF_FILE);
				if (defaultConfFile.exists()) {
					Configurations defaultConfigs = new
						INICommonsConfigurations(defaultConfFile);
					SourceFactory defaultSF = new SourceFactory(
						defaultConfigs, DEFAULT_CONF_FILE);
					ks = defaultSF.newKnowledgeSourceInstance();
				}
			}
			this.knowledgeSource = ks;
			LOGGER.debug("Done: configurations, source factory, and knowledge source created");
		} catch (BackendProviderSpecLoaderException e) {
			throw new PropositionFinderException(e);
		} catch (InvalidConfigurationException e) {
			throw new PropositionFinderException(e);
		} catch (ConfigurationsLoadException e) {
			throw new PropositionFinderException(e);
		} catch (BackendNewInstanceException e) {
			throw new PropositionFinderException(e);
		} catch (BackendInitializationException e) {
			throw new PropositionFinderException(e);
		}
	}

	private void validateConfigDir(File inFile) {
		if (LOGGER.isErrorEnabled()) {
			if (!inFile.exists()) {
				LOGGER.error(
					"Configuration directory " + inFile.getAbsolutePath() +
						" does not exist. Proposition finding will not work" +
						" without it. Please create it and try again.");
			} else if (!inFile.isDirectory()) {
				LOGGER.error(
					"Path " + inFile.getAbsolutePath() + " is not a " +
						"directory. Proposition finding requires it to be a" +
						" directory.");
			}
		}
	}

	public PropositionDefinition find(String inKey) throws
		PropositionFinderException {
		PropositionDefinition definition = null;
		try {
			definition = this.knowledgeSource.readPropositionDefinition
				(inKey);
		} catch (KnowledgeSourceReadException e) {
			throw new PropositionFinderException(e);
		}
		return definition;
	}
}
