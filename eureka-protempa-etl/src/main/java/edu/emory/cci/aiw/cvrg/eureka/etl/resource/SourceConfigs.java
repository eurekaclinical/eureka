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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUser;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
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
final class SourceConfigs extends Configs<SourceConfig, SourceConfigEntity> {

	SourceConfigs(EtlProperties inEtlProperties, EtlUser inEtlUser, SourceConfigDao inSourceConfigDao) {
		super("source", inEtlProperties, inEtlUser, inEtlProperties.getSourceConfigDirectory(), inSourceConfigDao);
	}

	@Override
	SourceConfig config(String configId, Perm perm) {
		SourceConfig config = new SourceConfig();
		try {
			EurekaProtempaConfigurations configs =
					new EurekaProtempaConfigurations(getEtlProperties());
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

			List<String> badConfigIds = new ArrayList<>();
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

			config.setRead(perm.read);
			config.setWrite(perm.write);
			config.setExecute(perm.execute);
			config.setOwnerUsername(perm.owner.getUsername());

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
		} catch (ConfigurationsNotFoundException | ConfigurationsLoadException | BackendProviderSpecLoaderException | InvalidPropertyNameException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
		return config;
	}

	private <B extends Backend> List<Section> extractConfig(
			BackendSpecLoader<B> backendSpecLoader,
			EurekaProtempaConfigurations configs, String sourceId)
			throws ConfigurationsNotFoundException,
			ConfigurationsLoadException, InvalidPropertyNameException {
		List<Section> backendSections = new ArrayList<>();
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

	@Override
	List<SourceConfigEntity> configs(EtlUser user) {
		return user.getSourceConfigs();
	}

	@Override
	List<SourceConfigGroupMembership> groupConfigs(EtlGroup group) {
		return group.getSourceConfigs();
	}

	@Override
	String toConfigId(File file) {
		return FromConfigFile.toSourceConfigId(file);
	}
	
	
}
