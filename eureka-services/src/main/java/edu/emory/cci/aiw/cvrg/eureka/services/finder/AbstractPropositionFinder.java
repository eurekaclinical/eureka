/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.finder;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPropositionFinder<K> implements PropositionFinder<K> {

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractPropositionFinder.class);
	private final PropositionRetriever<K> retriever;

	protected AbstractPropositionFinder(PropositionRetriever<K> inRetriever) {
		this.retriever = inRetriever;
	}

	/**
	 * Retrieves a proposition definition from the {@link PropositionRetriever}
	 * specified in this object's constructor.
	 *
	 * @param sourceConfigId the source configuration ID
	 * @param inKey a proposition key.
	 * @return the proposition definition, or <code>null</code> if there is no
	 * proposition definition with the specified key for the specified user.
	 *
	 * @throws PropositionFindException if an error occurred looking for the
	 * proposition definition.
	 */
	@Override
	public PropositionDefinition find(String sourceConfigId, K inKey)
			throws PropositionFindException {
		return this.retriever.retrieve(sourceConfigId, inKey);
	}

}
