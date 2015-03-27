package edu.emory.cci.aiw.cvrg.eureka.etl.dsb;

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

import java.net.URI;
import java.net.URISyntaxException;
import org.protempa.backend.BackendPropertyValidator;
import org.protempa.backend.InvalidPropertyValueException;

/**
 *
 * @author Andrew Post
 */
public class UriBackendPropertyValidator implements BackendPropertyValidator {

	@Override
	public void validate(String name, Object value) throws InvalidPropertyValueException {
		try {
			new URI((String) value);
		} catch (URISyntaxException ex) {
			throw new InvalidPropertyValueException(name, value, ex);
		}
	}

}
