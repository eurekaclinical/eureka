package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.i2b2etl.dest.config.Database;
import edu.emory.cci.aiw.i2b2etl.dest.config.DatabaseSpec;

/**
 *
 * @author Andrew Post
 */
class I2b2Database implements Database {
	private final DatabaseSpec metaSpec;
	private final DatabaseSpec dataSpec;

	I2b2Database(I2B2DestinationEntity entity) {
		String metaConnect = entity.getMetaConnect();
		String metaUser = entity.getMetaUser();
		String metaPassword = entity.getMetaPassword();
		this.metaSpec = new DatabaseSpec("metaschema", metaUser, metaPassword, metaConnect);
		
		String dataConnect = entity.getDataConnect();
		String dataUser = entity.getDataUser();
		String dataPassword = entity.getDataPassword();
		this.dataSpec = new DatabaseSpec("dataschema", dataUser, dataPassword, dataConnect);
	}
	
	@Override
	public DatabaseSpec getMetadataSpec() {
		return this.metaSpec;
	}

	@Override
	public DatabaseSpec getDataSpec() {
		return this.dataSpec;
	}
	
}
