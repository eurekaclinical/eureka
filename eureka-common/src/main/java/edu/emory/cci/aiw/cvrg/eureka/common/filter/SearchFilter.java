package edu.emory.cci.aiw.cvrg.eureka.common.filter;

/*
 * #%L
 * Eureka Common
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

import java.util.List;

public class SearchFilter {

	List<String> rootIDS;

	public SearchFilter(List<String> rootIDs) {
		// TODO Auto-generated constructor stub
		this.rootIDS = rootIDs;
	}

	public Boolean filter(String key) {

		return this.rootIDS.contains(key);

	}
}
