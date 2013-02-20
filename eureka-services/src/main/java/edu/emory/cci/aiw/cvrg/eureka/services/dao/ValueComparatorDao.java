/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.dao;

import edu.emory.cci.aiw.cvrg.eureka.common.dao.Dao;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import java.util.List;

public interface ValueComparatorDao extends Dao<ValueComparator, Long> {
	/**
	 * Gets a value comparator based on the name attribute.
	 * @param inName the name to search for in the database
	 * @return a {@link ValueComparator} with the given name if found, null otherwise
	 */
	public ValueComparator getByName(String inName);

	public List<ValueComparator> getAllAsc();
}
