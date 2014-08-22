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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

/**
 *
 * @author Andrew Post
 */
final class SourceConfigs {
	private final EtlGroupDao groupDao;
	private final EtlProperties etlProperties;
	private final EtlUserEntity etlUser;
	private final SourceConfigDao sourceConfigDao;

	SourceConfigs(EtlProperties inEtlProperties, EtlUserEntity inEtlUser, SourceConfigDao inSourceConfigDao, EtlGroupDao inGroupDao) {
		File inConfigDir = inEtlProperties.getSourceConfigDirectory();
		if (!inConfigDir.exists()) {
			try {
				inConfigDir.mkdir();
			} catch (SecurityException ex) {
				throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
						"Could not create source config directory", ex);
			}
		}
		this.groupDao = inGroupDao;
		this.etlProperties = inEtlProperties;
		this.sourceConfigDao = inSourceConfigDao;
		this.etlUser = inEtlUser;
	}

	/**
	 * Gets the specified source extractDTO. If it does not exist or the current
	 * user lacks read permissions for it, this method returns
	 * <code>null</code>.
	 *
	 * @return a extractDTO.
	 */
	public final SourceConfig getOne(String configId) {
		if (configId == null) {
			throw new IllegalArgumentException("configId cannot be null");
		}
		SourceConfigsDTOExtractor extractor = new SourceConfigsDTOExtractor(this.etlUser, this.groupDao, this.etlProperties);
		return extractor.extractDTO(this.sourceConfigDao.getByName(configId));
	}

	/**
	 * Gets all configs for which the current user has read permissions.
	 *
	 * @return a {@link List} of configs.
	 */
	public final List<SourceConfig> getAll() {
		List<SourceConfig> result = new ArrayList<>();
		SourceConfigsDTOExtractor extractor = new SourceConfigsDTOExtractor(this.etlUser, this.groupDao, this.etlProperties);
		for (SourceConfigEntity configEntity : this.sourceConfigDao.getAll()) {
			SourceConfig dto = extractor.extractDTO(configEntity);
			if (dto != null) {
				result.add(dto);
			}
		}
		return result;
	}
	
}
