package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

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
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.SourceConfigEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.SourceConfigDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 *
 * @author Andrew Post
 */
final class SourceConfigs {

	private final EtlGroupDao groupDao;
	private final EtlProperties etlProperties;
	private final AuthorizedUserEntity etlUser;
	private final SourceConfigDao sourceConfigDao;
	private final SourceConfigsDTOExtractor extractor;

	SourceConfigs(EtlProperties inEtlProperties, AuthorizedUserEntity inEtlUser, SourceConfigDao inSourceConfigDao, EtlGroupDao inGroupDao) {
		try {
			inEtlProperties.getSourceConfigDirectory();
			this.groupDao = inGroupDao;
			this.etlProperties = inEtlProperties;
			this.sourceConfigDao = inSourceConfigDao;
			this.etlUser = inEtlUser;
			this.extractor = new SourceConfigsDTOExtractor(this.etlUser, this.groupDao, this.etlProperties);
		} catch (IOException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR,
					"Could not create source config directory", ex);
		}
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

		return extractor.extractDTO(this.sourceConfigDao.getByName(configId));
	}

	/**
	 * Gets all configs for which the current user has read permissions.
	 *
	 * @return a {@link List} of configs.
	 */
	public final List<SourceConfig> getAll() {
		List<SourceConfig> result = new ArrayList<>();
		for (SourceConfigEntity configEntity : this.sourceConfigDao.getAll()) {
			SourceConfig dto = extractor.extractDTO(configEntity);
			if (dto != null) {
				result.add(dto);
			}
		}
		return result;
	}

}
