package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig.Option;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig.Section;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.protempa.backend.Backend;
import org.protempa.backend.BackendInstanceSpec;
import org.protempa.backend.BackendPropertySpec;
import org.protempa.backend.BackendProvider;
import org.protempa.backend.BackendProviderManager;
import org.protempa.backend.BackendProviderSpecLoaderException;
import org.protempa.backend.BackendSpec;
import org.protempa.backend.BackendSpecLoader;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.ConfigurationsNotFoundException;
import org.protempa.backend.InvalidPropertyNameException;
import org.protempa.backend.asb.AlgorithmSourceBackend;
import org.protempa.backend.dsb.DataSourceBackend;
import org.protempa.backend.ksb.KnowledgeSourceBackend;
import org.protempa.backend.tsb.TermSourceBackend;

/**
 *
 * @author Andrew Post
 */
public final class SourceConfigs {

	private final EtlProperties etlProperties;

	public SourceConfigs(EtlProperties inEtlProperties) {
		if (inEtlProperties == null) {
			throw new IllegalArgumentException("inEtlProperties cannot be null");
		}
		this.etlProperties = inEtlProperties;
		if (!this.etlProperties.getSourceConfigDirectory().exists()) {
			try {
				this.etlProperties.getSourceConfigDirectory().mkdir();
			} catch (SecurityException ex) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
						"Could not create source config directory", ex);
			}
		}
	}

	/**
	 * Checks if the Protempa configuration with the specified id exists.
	 *
	 * @param sourceConfigId
	 * @return
	 */
	public boolean sourceConfigExists(String sourceConfigId) {
		if (sourceConfigId == null) {
			throw new IllegalArgumentException("sourceConfigId cannot be null");
		}
		return this.etlProperties.sourceConfigFile(sourceConfigId).exists();
	}

	public List<SourceConfig> getAll() {
		List<SourceConfig> result = new ArrayList<SourceConfig>();
		try {
			File[] files = this.etlProperties
					.getSourceConfigDirectory()
					.listFiles();
			if (files == null) {
				throw new HttpStatusException(
						Response.Status.INTERNAL_SERVER_ERROR,
						"Source config directory " + this.etlProperties.getSourceConfigDirectory().getAbsolutePath() + " does not exist");
			}
			for (File file : files) {
				String filename = file.getName();
				result.add(getSourceConfig(filename));
			}
		} catch (SecurityException ex) {
			throw new HttpStatusException(
					Response.Status.INTERNAL_SERVER_ERROR,
					"Source config directory " + this.etlProperties.getSourceConfigDirectory().getAbsolutePath() + " could not be accessed", ex);
		}
		return result;
	}

	public SourceConfig getSourceConfig(String configId) {
		if (configId == null) {
			throw new IllegalArgumentException("configId cannot be null");
		}
		SourceConfig config = new SourceConfig();
		try {
			EurekaProtempaConfigurations configs =
					new EurekaProtempaConfigurations(this.etlProperties);
			BackendProvider backendProvider =
					BackendProviderManager.getBackendProvider();
			BackendSpecLoader<DataSourceBackend> dataSourceBackendSpecLoader =
					backendProvider.getDataSourceBackendSpecLoader();
			BackendSpecLoader<KnowledgeSourceBackend> knowledgeSourceBackendSpecLoader =
					backendProvider.getKnowledgeSourceBackendSpecLoader();
			BackendSpecLoader<AlgorithmSourceBackend> algorithmSourceBackendSpecLoader =
					backendProvider.getAlgorithmSourceBackendSpecLoader();
			BackendSpecLoader<TermSourceBackend> termSourceBackendSpecLoader =
					backendProvider.getTermSourceBackendSpecLoader();

			List<String> badConfigIds = new ArrayList<String>();
			for (String id : configs.loadConfigurationIds(configId)) {
				if (!dataSourceBackendSpecLoader.hasSpec(id)
						&& !knowledgeSourceBackendSpecLoader.hasSpec(id)
						&& !algorithmSourceBackendSpecLoader.hasSpec(id)
						&& !termSourceBackendSpecLoader.hasSpec(id)) {
					badConfigIds.add(id);
				}
			}
			if (!badConfigIds.isEmpty()) {
				if (badConfigIds.size() == 1) {
					throw new ConfigurationsLoadException("Invalid section id '" + badConfigIds.get(0) + "' in source configuration '" + configId + "'");
				} else {
					throw new ConfigurationsLoadException("Invalid section ids '" + StringUtils.join(badConfigIds.subList(0, badConfigIds.size() - 1), "', '") + "' and '" + badConfigIds.get(badConfigIds.size() - 1) + "' in source configuration '" + configId + "'");
				}
			}

			config.setId(configId);
			config.setPermissions(SourceConfig.Permissions.READ_ONLY);
			List<SourceConfig.Section> dataSourceBackendSections =
					extractConfig(dataSourceBackendSpecLoader, configs, configId);
			config.setDataSourceBackends(
					dataSourceBackendSections.toArray(
					new SourceConfig.Section[dataSourceBackendSections.size()]));


			List<SourceConfig.Section> knowledgeSourceBackendSections =
					extractConfig(knowledgeSourceBackendSpecLoader, configs, configId);
			config.setKnowledgeSourceBackends(
					knowledgeSourceBackendSections.toArray(
					new SourceConfig.Section[knowledgeSourceBackendSections.size()]));


			List<SourceConfig.Section> algorithmSourceBackendSections =
					extractConfig(algorithmSourceBackendSpecLoader, configs, configId);
			config.setAlgorithmSourceBackends(
					algorithmSourceBackendSections.toArray(
					new SourceConfig.Section[algorithmSourceBackendSections.size()]));

			List<SourceConfig.Section> termSourceBackendSections =
					extractConfig(termSourceBackendSpecLoader, configs, configId);
			config.setTermSourceBackends(
					termSourceBackendSections.toArray(
					new SourceConfig.Section[termSourceBackendSections.size()]));
		} catch (ConfigurationsNotFoundException ex) {
			Logger.getLogger(SourceConfigs.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidPropertyNameException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		} catch (BackendProviderSpecLoaderException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		} catch (ConfigurationsLoadException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
		return config;
	}

	private <B extends Backend> List<Section> extractConfig(
			BackendSpecLoader<B> backendSpecLoader,
			EurekaProtempaConfigurations configs, String sourceId)
			throws ConfigurationsNotFoundException,
			ConfigurationsLoadException, InvalidPropertyNameException {
		List<Section> backendSections = new ArrayList<Section>();
		for (BackendSpec<B> bs : backendSpecLoader) {
			List<BackendInstanceSpec<B>> load = configs.load(sourceId, bs);
			for (BackendInstanceSpec<B> bis : load) {
				Section section = new Section();
				section.setId(bs.getId());
				section.setDisplayName(bs.getDisplayName());
				List<BackendPropertySpec> backendPropertySpecs = bis.getBackendPropertySpecs();
				Option[] options = new Option[backendPropertySpecs.size()];
				for (int i = 0; i < options.length; i++) {
					BackendPropertySpec property = backendPropertySpecs.get(i);
					Object value = bis.getProperty(property.getName());
					Option option = new Option();
					option.setKey(property.getName());
					option.setValue(value);
					options[i] = option;
				}
				section.setOptions(options);
				backendSections.add(section);
			}
		}
		return backendSections;
	}
}
