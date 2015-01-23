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
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.i2b2etl.configuration.Concepts;
import edu.emory.cci.aiw.i2b2etl.configuration.Configuration;
import edu.emory.cci.aiw.i2b2etl.configuration.ConfigurationReadException;
import edu.emory.cci.aiw.i2b2etl.configuration.Data;
import edu.emory.cci.aiw.i2b2etl.configuration.Database;
import edu.emory.cci.aiw.i2b2etl.configuration.Settings;
import edu.emory.cci.aiw.i2b2etl.configuration.XmlFileConfiguration;
import java.io.File;

/**
 *
 * @author Andrew Post
 */
class EurekaConfiguration implements Configuration {
	private boolean useOld;
	private XmlFileConfiguration oldConfiguration;
	private final I2B2DestinationEntity i2B2DestinationEntity;
	private final EtlProperties etlProperties;

	EurekaConfiguration(I2B2DestinationEntity inI2B2DestinationEntity, EtlProperties inEtlProperties) {
		this.i2B2DestinationEntity = inI2B2DestinationEntity;
		this.etlProperties = inEtlProperties;
	}
	
	@Override
	public void init() throws ConfigurationReadException {
		File destinationConfigFile = this.etlProperties.destinationConfigFile(this.i2B2DestinationEntity.getName());
		this.oldConfiguration = new XmlFileConfiguration(destinationConfigFile);
		this.oldConfiguration.init();
		this.useOld = true;
	}

	@Override
	public Concepts getConcepts() {
		if (this.useOld) {
			return this.oldConfiguration.getConcepts();
		} else {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public Data getData() {
		if (this.useOld) {
			return this.oldConfiguration.getData();
		} else {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public Database getDatabase() {
		if (this.useOld) {
			return this.oldConfiguration.getDatabase();
		} else {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public Settings getSettings() {
		if (this.useOld) {
			return this.oldConfiguration.getSettings();
		} else {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public String getName() {
		if (this.useOld) {
			return this.oldConfiguration.getName();
		} else {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
	
}
