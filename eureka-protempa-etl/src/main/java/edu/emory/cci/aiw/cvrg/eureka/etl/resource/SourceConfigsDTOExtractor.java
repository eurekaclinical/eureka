package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DefaultSourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.FileSourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UriSourceConfigOption;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EurekaProtempaConfigurations;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ResolvedPermissions;
import edu.emory.cci.aiw.cvrg.eureka.etl.dsb.FileBackendPropertyValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.dsb.UriBackendPropertyValidator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.protempa.backend.Backend;
import org.protempa.backend.BackendInstanceSpec;
import org.protempa.backend.BackendPropertySpec;
import org.protempa.backend.BackendPropertyValidator;
import org.protempa.backend.Configuration;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.ConfigurationsNotFoundException;
import org.protempa.backend.InvalidPropertyNameException;
import org.protempa.backend.asb.AlgorithmSourceBackend;
import org.protempa.backend.dsb.DataSourceBackend;
import org.protempa.backend.ksb.KnowledgeSourceBackend;
import org.protempa.backend.tsb.TermSourceBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
class SourceConfigsDTOExtractor extends ConfigsDTOExtractor<SourceConfig, SourceConfigEntity> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SourceConfigsDTOExtractor.class);

	private final EtlGroupDao groupDao;
	private final EtlProperties etlProperties;
	private final EurekaProtempaConfigurations configs;

	SourceConfigsDTOExtractor(AuthorizedUserEntity user, EtlGroupDao inGroupDao, EtlProperties inEtlProperties) throws IOException {
		super(user);
		this.groupDao = inGroupDao;
		this.etlProperties = inEtlProperties;
		this.configs = new EurekaProtempaConfigurations(this.etlProperties);
	}

	@Override
	SourceConfig extractDTO(Perm perm, SourceConfigEntity configEntity) {
		String configId = configEntity.getName();
		try {
			SourceConfig config = new SourceConfig();
			config.setId(configId);
			config.setDisplayName(configId);
			config.setRead(perm.read);
			config.setWrite(perm.write);
			config.setExecute(perm.execute);
			config.setOwnerUsername(perm.owner.getUsername());
			Configuration configuration = configs.load(configId);
			List<SourceConfig.Section> dataSourceBackendSections
					= toSectionsDSB(configuration.getDataSourceBackendSections());
			config.setDataSourceBackends(
					dataSourceBackendSections.toArray(
							new SourceConfig.Section[dataSourceBackendSections.size()]));

			List<SourceConfig.Section> knowledgeSourceBackendSections
					= toSectionsKSB(configuration.getKnowledgeSourceBackendSections());
			config.setKnowledgeSourceBackends(
					knowledgeSourceBackendSections.toArray(
							new SourceConfig.Section[knowledgeSourceBackendSections.size()]));

			List<SourceConfig.Section> algorithmSourceBackendSections
					= toSectionsASB(configuration.getAlgorithmSourceBackendSections());
			config.setAlgorithmSourceBackends(
					algorithmSourceBackendSections.toArray(
							new SourceConfig.Section[algorithmSourceBackendSections.size()]));

			List<SourceConfig.Section> termSourceBackendSections
					= toSectionsTSB(configuration.getTermSourceBackendSections());
			config.setTermSourceBackends(
					termSourceBackendSections.toArray(
							new SourceConfig.Section[termSourceBackendSections.size()]));
			return config;
		} catch (ConfigurationsNotFoundException | ConfigurationsLoadException | InvalidPropertyNameException ex) {
			LOGGER.warn("Error getting INI file for source config {}. This source config will be ignored.", configEntity.getName(), ex);
			return null;
		}
	}

	@Override
	ResolvedPermissions resolvePermissions(AuthorizedUserEntity owner, SourceConfigEntity entity) {
		return this.groupDao.resolveSourceConfigPermissions(owner, entity);
	}

	private List<SourceConfig.Section> toSectionsDSB(List<BackendInstanceSpec<DataSourceBackend>> bises) throws InvalidPropertyNameException {
		List<SourceConfig.Section> result = new ArrayList<>();
		for (BackendInstanceSpec<? extends DataSourceBackend> bis : bises) {
			SourceConfig.Section section = newSection(bis);
			result.add(section);
		}
		return result;
	}
	
	private List<SourceConfig.Section> toSectionsKSB(List<BackendInstanceSpec<KnowledgeSourceBackend>> bises) throws InvalidPropertyNameException {
		List<SourceConfig.Section> result = new ArrayList<>();
		for (BackendInstanceSpec<? extends KnowledgeSourceBackend> bis : bises) {
			SourceConfig.Section section = newSection(bis);
			result.add(section);
		}
		return result;
	}
	
	private List<SourceConfig.Section> toSectionsASB(List<BackendInstanceSpec<AlgorithmSourceBackend>> bises) throws InvalidPropertyNameException {
		List<SourceConfig.Section> result = new ArrayList<>();
		for (BackendInstanceSpec<? extends AlgorithmSourceBackend> bis : bises) {
			SourceConfig.Section section = newSection(bis);
			result.add(section);
		}
		return result;
	}
	
	private List<SourceConfig.Section> toSectionsTSB(List<BackendInstanceSpec<TermSourceBackend>> bises) throws InvalidPropertyNameException {
		List<SourceConfig.Section> result = new ArrayList<>();
		for (BackendInstanceSpec<? extends TermSourceBackend> bis : bises) {
			SourceConfig.Section section = newSection(bis);
			result.add(section);
		}
		return result;
	}

	private SourceConfig.Section newSection(BackendInstanceSpec<? extends Backend> bis) throws InvalidPropertyNameException {
		SourceConfig.Section section = new SourceConfig.Section();
		section.setId(bis.getBackendSpec().getId());
		section.setDisplayName(bis.getBackendSpec().getDisplayName());
		BackendPropertySpec[] backendPropertySpecs = bis.getBackendSpec().getPropertySpecs();
		SourceConfigOption[] options = new SourceConfigOption[backendPropertySpecs.length];
		for (int i = 0; i < options.length; i++) {
			BackendPropertySpec property = backendPropertySpecs[i];
			Object value = bis.getProperty(property.getName());
			SourceConfigOption option;
			BackendPropertyValidator validator = property.getValidator();
			if (validator instanceof FileBackendPropertyValidator) {
				FileSourceConfigOption fileOption = new FileSourceConfigOption();
				FileBackendPropertyValidator fileValidator = (FileBackendPropertyValidator) validator;
				fileOption.setAcceptedMimetypes(fileValidator.getAcceptedMimetypes());
				option = fileOption;
			} else if (validator instanceof UriBackendPropertyValidator) {
				option = new UriSourceConfigOption();
			} else {
				option = new DefaultSourceConfigOption();
			}
			option.setName(property.getName());
			option.setDisplayName(bis.getDisplayName(property.getName()));
			option.setDescription(property.getDescription());
			option.setValue(value);
			option.setRequired(bis.isRequired(property.getName()));
			option.setPropertyType(property.getType());
			option.setPrompt(bis.isRequired(property.getName()) && value == null);
			options[i] = option;
		}
		section.setOptions(options);
		return section;
	}
}
