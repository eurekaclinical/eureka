package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.i2b2etl.dest.config.Concepts;
import edu.emory.cci.aiw.i2b2etl.dest.config.Configuration;
import edu.emory.cci.aiw.i2b2etl.dest.config.ConfigurationInitException;
import edu.emory.cci.aiw.i2b2etl.dest.config.Data;
import edu.emory.cci.aiw.i2b2etl.dest.config.Database;
import edu.emory.cci.aiw.i2b2etl.dest.config.Settings;
import edu.emory.cci.aiw.i2b2etl.dest.config.xml.XmlFileConfiguration;
import java.io.File;
import java.io.IOException;

/**
 * Database-based i2b2 loader configuration.
 *
 * @author Andrew Post
 */
class EurekaI2b2Configuration implements Configuration {

	private XmlFileConfiguration oldConfiguration;
	private final I2B2DestinationEntity i2B2DestinationEntity;
	private final EtlProperties etlProperties;
	private I2b2Settings i2b2Settings;
	private I2b2Database i2b2Database;
	private I2b2Data i2b2Data;
	private I2b2Concepts i2b2Concepts;

	EurekaI2b2Configuration(I2B2DestinationEntity inI2B2DestinationEntity, EtlProperties inEtlProperties) throws ConfigurationInitException {
		try {
			this.i2B2DestinationEntity = inI2B2DestinationEntity;
			this.etlProperties = inEtlProperties;
			File destinationConfigFile = this.etlProperties.destinationConfigFile(this.i2B2DestinationEntity.getName());
			if (this.i2B2DestinationEntity.getDataConnect() == null && this.i2B2DestinationEntity.getMetaConnect() == null) {
				this.oldConfiguration = new XmlFileConfiguration(destinationConfigFile);
			} else {
				this.i2b2Settings = new I2b2Settings(this.i2B2DestinationEntity);
				this.i2b2Database = new I2b2Database(this.i2B2DestinationEntity);
			}
		} catch (IOException ex) {
			throw new ConfigurationInitException("Error initializing configuration", ex);
		}
	}

	@Override
	public Concepts getConcepts() {
		if (this.oldConfiguration != null) {
			return this.oldConfiguration.getConcepts();
		} else {
			if (this.i2b2Concepts == null) {
				this.i2b2Concepts = new I2b2Concepts(this.i2B2DestinationEntity.getConceptSpecs());
			}
			return this.i2b2Concepts;
		}
	}

	@Override
	public Data getData() {
		if (this.oldConfiguration != null) {
			return this.oldConfiguration.getData();
		} else {
			if (this.i2b2Data == null) {
				this.i2b2Data = new I2b2Data(this.i2B2DestinationEntity.getDataSpecs());
			}
			return this.i2b2Data;
		}
	}

	@Override
	public Database getDatabase() {
		if (this.oldConfiguration != null) {
			return this.oldConfiguration.getDatabase();
		} else {
			return this.i2b2Database;
		}
	}

	@Override
	public Settings getSettings() {
		if (this.oldConfiguration != null) {
			return this.oldConfiguration.getSettings();
		} else {
			return this.i2b2Settings;
		}
	}

	@Override
	public String getName() {
		if (this.oldConfiguration != null) {
			return this.oldConfiguration.getName();
		} else {
			return this.i2B2DestinationEntity.getName();
		}
	}

}
