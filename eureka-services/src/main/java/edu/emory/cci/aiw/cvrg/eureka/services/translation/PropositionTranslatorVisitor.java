/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

public final class PropositionTranslatorVisitor implements
        PropositionEntityVisitor {

	private DataElement dataElement;
	private final SystemPropositionFinder finder;
	private final PropositionDao propositionDao;
	private final Long userId;

	public PropositionTranslatorVisitor(Long inUserId, PropositionDao inDao,
	        SystemPropositionFinder inFinder) {
		userId = inUserId;
		propositionDao = inDao;
		finder = inFinder;
	}

	public DataElement getDataElement() {
		return dataElement;
	}

	@Override
	public void visit(SystemProposition proposition) {
		dataElement = new SystemPropositionTranslator(finder)
		        .translateFromProposition(proposition);
	}

	@Override
	public void visit(Categorization categorization) {
		dataElement = new CategorizationTranslator(propositionDao, finder)
		        .translateFromProposition(categorization);
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		dataElement = new SequenceTranslator(userId, propositionDao, finder)
		        .translateFromProposition(highLevelAbstraction);
	}
}
