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

import org.apache.commons.lang3.ArrayUtils;
import org.protempa.backend.BackendPropertyValidator;
import org.protempa.backend.InvalidPropertyValueException;

/**
 *
 * @author Andrew Post
 */
public class FileBackendPropertyValidator implements BackendPropertyValidator {

	private String[] acceptedMimetypes;

	public FileBackendPropertyValidator() {
		this.acceptedMimetypes = ArrayUtils.EMPTY_STRING_ARRAY;
	}
	
	public String[] getAcceptedMimetypes() {
		return acceptedMimetypes.clone();
	}

	public void setAcceptedMimetypes(String[] acceptedMimetypes) {
		if (acceptedMimetypes != null) {
			this.acceptedMimetypes = acceptedMimetypes;
		} else {
			this.acceptedMimetypes = ArrayUtils.EMPTY_STRING_ARRAY;
		}
	}

	@Override
	public void validate(String name, Object value) throws InvalidPropertyValueException {
		// Do nothing
	}

}
