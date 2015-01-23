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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationDataSpecEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationIntervalSide;
import edu.emory.cci.aiw.i2b2etl.configuration.Data;
import edu.emory.cci.aiw.i2b2etl.configuration.DataSpec;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andrew Post
 */
class I2b2Data implements Data {
	private final Map<String, DataSpec> dataSpecs;

	I2b2Data(List<I2B2DestinationDataSpecEntity> dataSpecs) {
		this.dataSpecs = new HashMap<>();
		for (I2B2DestinationDataSpecEntity dataSpec : dataSpecs) {
			I2B2DestinationIntervalSide start = dataSpec.getStart();
			I2B2DestinationIntervalSide finish = dataSpec.getFinish();
			this.dataSpecs.put(
					dataSpec.getName(), 
					new DataSpec(
							dataSpec.getName(), 
							dataSpec.getReference(), 
							dataSpec.getProperty(), 
							dataSpec.getConceptCodePrefix(), 
							start != null ? start.getName() : null, 
							finish != null ? finish.getName() : null, 
							null));
		}
	}

	@Override
	public DataSpec get(String key) {
		return this.dataSpecs.get(key);
	}

	@Override
	public Collection<DataSpec> getAll() {
		return this.dataSpecs.values();
	}
	
}
