package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
import org.eurekaclinical.eureka.client.comm.DefaultSourceConfigOption;
import org.eurekaclinical.eureka.client.comm.FileSourceConfigOption;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import org.eurekaclinical.eureka.client.comm.SourceConfigOption;
import org.eurekaclinical.eureka.client.comm.UriSourceConfigOption;
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
import org.protempa.backend.BackendPropertyType;
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
			option.setPropertyType(toEurekaBackendPropertyType(property.getType()));
			option.setPrompt(bis.isRequired(property.getName()) && value == null);
			options[i] = option;
		}
		section.setOptions(options);
		return section;
	}

	private SourceConfigOption.BackendPropertyType toEurekaBackendPropertyType(BackendPropertyType type) {
		switch (type) {
			case STRING:
				return SourceConfigOption.BackendPropertyType.STRING;
			case BOOLEAN:
				return SourceConfigOption.BackendPropertyType.BOOLEAN;
			case INTEGER:
				return SourceConfigOption.BackendPropertyType.INTEGER;
			case LONG:
				return SourceConfigOption.BackendPropertyType.LONG;
			case FLOAT:
				return SourceConfigOption.BackendPropertyType.FLOAT;
			case DOUBLE:
				return SourceConfigOption.BackendPropertyType.DOUBLE;
			case CHARACTER:
				return SourceConfigOption.BackendPropertyType.CHARACTER;
			case STRING_ARRAY:
				return SourceConfigOption.BackendPropertyType.STRING_ARRAY;
			case DOUBLE_ARRAY:
				return SourceConfigOption.BackendPropertyType.DOUBLE_ARRAY;
			case FLOAT_ARRAY:
				return SourceConfigOption.BackendPropertyType.FLOAT_ARRAY;
			case INTEGER_ARRAY:
				return SourceConfigOption.BackendPropertyType.INTEGER_ARRAY;
			case LONG_ARRAY:
				return SourceConfigOption.BackendPropertyType.LONG_ARRAY;
			case BOOLEAN_ARRAY:
				return SourceConfigOption.BackendPropertyType.BOOLEAN_ARRAY;
			case CHARACTER_ARRAY:
				return SourceConfigOption.BackendPropertyType.CHARACTER_ARRAY;
			default:
				throw new AssertionError("unexepcted backend property type " + type);
		}
	}
}
