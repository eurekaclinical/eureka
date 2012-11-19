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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.CategoricalElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElementVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;
import edu.emory.cci.aiw.cvrg.eureka.services.dao.PropositionDao;
import edu.emory.cci.aiw.cvrg.eureka.services.finder.SystemPropositionFinder;

public final class DataElementTranslatorVisitor implements DataElementVisitor {

	private Proposition proposition;
	private final Long userId;
	private final PropositionDao propositionDao;
	private final SystemPropositionFinder finder;

	public DataElementTranslatorVisitor(Long inUserId,
	        PropositionDao inPropositionDao, SystemPropositionFinder inFinder) {
		userId = inUserId;
		propositionDao = inPropositionDao;
		finder = inFinder;
	}

	public Proposition getProposition() {
		return proposition;
	}

	@Override
	public void visit(SystemElement systemElement) {
		proposition = new SystemPropositionTranslator(finder)
		        .translateFromElement(systemElement);
	}

	@Override
	public void visit(CategoricalElement categoricalElement) {
		proposition = new CategorizationTranslator(propositionDao, finder)
		        .translateFromElement(categoricalElement);
	}

	@Override
	public void visit(Sequence sequence) {
		proposition = new SequenceTranslator(userId, propositionDao, finder)
		        .translateFromElement(sequence);
	}

}
