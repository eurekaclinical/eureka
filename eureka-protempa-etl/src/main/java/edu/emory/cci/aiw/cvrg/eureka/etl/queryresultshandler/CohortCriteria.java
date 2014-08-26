package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.AbstractNodeVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.BinaryOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Literal;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UnaryOperator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.arp.javautil.collections.Collections;
import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.dest.keyloader.Criteria;
import org.protempa.dest.keyloader.CriteriaEvaluateException;
import org.protempa.dest.keyloader.CriteriaInitException;
import org.protempa.proposition.Proposition;

/**
 *
 * @author Andrew Post
 */
class CohortCriteria implements Criteria {

	private final Cohort cohort;
	private final Map<String, List<String>> cache;
	private final String[] propIdsSpecified;

	CohortCriteria(Cohort cohort) {
		assert cohort != null : "cohort cannot be null";
		this.cohort = cohort;
		this.cache = new HashMap<>();
		CollectLiteralNamesNodeVisitor v = new CollectLiteralNamesNodeVisitor();
		this.cohort.getNode().accept(v);
		Set<String> literalNames = v.getLiteralNames();
		this.propIdsSpecified = literalNames.toArray(new String[literalNames.size()]);
	}

	@Override
	public void init(final KnowledgeSource knowledgeSource) throws CriteriaInitException {
		GetChildrenNodeVisitor nodeVisitorImpl = new GetChildrenNodeVisitor(knowledgeSource);
		this.cohort.getNode().accept(nodeVisitorImpl);
		if (nodeVisitorImpl.exception != null) {
			throw new CriteriaInitException(nodeVisitorImpl.exception);
		}
	}

	@Override
	public boolean evaluate(List<Proposition> propositions) throws CriteriaEvaluateException {
		Map<String, List<Proposition>> propMap = new HashMap<>();
		for (Proposition prop : propositions) {
			List<String> get = this.cache.get(prop.getId());
			if (get != null) {
				for (String literalName : get) {
					Collections.putList(propMap, literalName, prop);
				}
			}
		}
		System.out.println("propMap: " + propMap);
		return this.cohort.evaluate(propMap);
	}

	@Override
	public String[] getPropositionIdsSpecified() {
		return this.propIdsSpecified;
	}

	private class GetChildrenNodeVisitor extends AbstractNodeVisitor {

		private final KnowledgeSource knowledgeSource;
		private KnowledgeSourceReadException exception;

		public GetChildrenNodeVisitor(KnowledgeSource knowledgeSource) {
			this.knowledgeSource = knowledgeSource;
		}

		@Override
		public void visit(Literal literal) {
			try {
				Set<String> propIds = this.knowledgeSource.inDataSourcePropositionIds(literal.getName());
				for (String propId : propIds) {
					Collections.putList(cache, propId, literal.getName());
				}
			} catch (KnowledgeSourceReadException ex) {
				this.exception = ex;
			}
		}

		@Override
		public void visit(UnaryOperator unaryOperator) {
			if (this.exception == null) {
				GetChildrenNodeVisitor v = new GetChildrenNodeVisitor(this.knowledgeSource);
				unaryOperator.getNode().accept(v);
			}
		}

		@Override
		public void visit(BinaryOperator binaryOperator) {
			if (this.exception == null) {
				GetChildrenNodeVisitor v = new GetChildrenNodeVisitor(this.knowledgeSource);
				binaryOperator.getLeftNode().accept(v);
				binaryOperator.getRightNode().accept(v);
			}
		}

	}

	private class CollectLiteralNamesNodeVisitor extends AbstractNodeVisitor {

		private final Set<String> literalNames;

		public CollectLiteralNamesNodeVisitor() {
			this.literalNames = new HashSet<String>();
		}

		@Override
		public void visit(Literal literal) {
			this.literalNames.add(literal.getName());
		}

		@Override
		public void visit(UnaryOperator unaryOperator) {
			CollectLiteralNamesNodeVisitor v = new CollectLiteralNamesNodeVisitor();
			unaryOperator.getNode().accept(v);
			this.literalNames.addAll(v.getLiteralNames());
		}

		@Override
		public void visit(BinaryOperator binaryOperator) {
			CollectLiteralNamesNodeVisitor v = new CollectLiteralNamesNodeVisitor();
			binaryOperator.getLeftNode().accept(v);
			this.literalNames.addAll(v.getLiteralNames());
			v = new CollectLiteralNamesNodeVisitor();
			binaryOperator.getRightNode().accept(v);
			this.literalNames.addAll(v.getLiteralNames());
		}

		public Set<String> getLiteralNames() {
			return this.literalNames;
		}

	}

}
