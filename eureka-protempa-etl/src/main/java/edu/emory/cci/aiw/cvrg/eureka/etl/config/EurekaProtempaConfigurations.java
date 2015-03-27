package edu.emory.cci.aiw.cvrg.eureka.etl.config;

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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.etl.resource.ToConfigFile;
import org.protempa.backend.Configuration;
import org.protempa.backend.ConfigurationsLoadException;
import org.protempa.backend.ConfigurationsNotFoundException;
import org.protempa.bconfigs.ini4j.INIConfigurations;

/**
 *
 * @author Andrew Post
 */
public class EurekaProtempaConfigurations extends INIConfigurations {

	@Inject
	public EurekaProtempaConfigurations(EtlProperties etlProperties) {
		super(etlProperties.getSourceConfigDirectory());
	}

	@Override
	public Configuration load(
			String configId) 
			throws ConfigurationsLoadException, 
			ConfigurationsNotFoundException {
		return super.load(ToConfigFile.fromSourceConfigId(configId));
	}
}
