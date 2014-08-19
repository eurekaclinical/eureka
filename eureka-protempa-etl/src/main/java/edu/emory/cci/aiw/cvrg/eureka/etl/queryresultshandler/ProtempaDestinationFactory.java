package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DestinationType;
import edu.emory.cci.aiw.i2b2etl.I2b2Destination;
import java.io.File;
import org.protempa.dest.keyloader.KeyLoaderDestination;

/**
 *
 * @author Andrew Post
 */
public class ProtempaDestinationFactory {
	
	public org.protempa.dest.Destination getInstance(DestinationType type, File config) {
		switch(type) {
			case I2B2:
				return new I2b2Destination(config);
			case COHORT:
				return new KeyLoaderDestination(new CohortCriteria(config));
			default:
				throw new AssertionError("Unexpected destination type " + type);
		}
	}

}
