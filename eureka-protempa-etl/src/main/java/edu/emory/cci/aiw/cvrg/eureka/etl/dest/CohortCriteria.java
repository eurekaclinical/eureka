package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.etl.util.AbstractNodeVisitor;
import java.util.Date;
import org.eurekaclinical.eureka.client.comm.BinaryOperator;
import org.eurekaclinical.eureka.client.comm.Cohort;
import org.eurekaclinical.eureka.client.comm.Literal;
import org.eurekaclinical.eureka.client.comm.UnaryOperator;
import org.eurekaclinical.eureka.client.comm.Node;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.arp.javautil.collections.Collections;
import org.eurekaclinical.eureka.client.comm.BinaryOperator.Op;
import static org.neo4j.ext.udc.UdcSettings.interval;
import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.dest.keyloader.Criteria;
import org.protempa.dest.keyloader.CriteriaEvaluateException;
import org.protempa.dest.keyloader.CriteriaInitException;
import org.protempa.proposition.AbstractParameter;
import org.protempa.proposition.Constant;
import org.protempa.proposition.Context;
import org.protempa.proposition.Event;
import org.protempa.proposition.PrimitiveParameter;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.TemporalProposition;
import org.protempa.proposition.interval.AbsoluteTimeIntervalFactory;
import org.protempa.proposition.interval.Interval;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.visitor.AbstractPropositionVisitor;

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
		return evaluate(this.cohort.getNode(), propMap);
	}
	
	private boolean evaluate(Node node, Map<String, List<Proposition>> propMap) {
		if (node instanceof UnaryOperator) {
			return evaluateUnaryOperator((UnaryOperator) node, propMap);
		} else if (node instanceof BinaryOperator) {
			return evaluateBinaryOperator((BinaryOperator) node, propMap);
		} else if (node instanceof Literal) {
			return evaluateLiteral((Literal) node, propMap);
		} else {
			throw new AssertionError("Unexpected node type " + node.getClass().getName());
		}
    }
	
    private boolean evaluateUnaryOperator(UnaryOperator unaryOperator, Map<String, List<Proposition>> propMap) {
		UnaryOperator.Op op = unaryOperator.getOp();
        switch (op) {
            case NOT:
                return !evaluate(unaryOperator.getNode(), propMap);
            default:
                throw new AssertionError("Invalid op " + op);
        }
    }
	
    private boolean evaluateBinaryOperator(BinaryOperator binaryOperator, Map<String, List<Proposition>> propMap) {
		Node leftNode = binaryOperator.getLeftNode();
		Node rightNode = binaryOperator.getRightNode();
		Op op = binaryOperator.getOp();
        switch (op) {
            case AND:
                return evaluate(leftNode, propMap) && evaluate(rightNode, propMap);
            case OR:
                return evaluate(leftNode, propMap) || evaluate(rightNode, propMap);
            default:
                throw new AssertionError("Invalid op " + op);
        }
    }
	
	private boolean evaluateLiteral(Literal literal, Map<String, List<Proposition>> propMap) {
        List<Proposition> props = propMap.get(literal.getName());
		Date start = literal.getStart();
		Date finish = literal.getFinish();
		Interval interval = null;
        if (props != null && !props.isEmpty()) {
            if ((start != null || finish != null)) {
                interval
                        = new AbsoluteTimeIntervalFactory().getInstance(
                                start, null,
                                finish, null);
            }
            if (interval != null) {
                LiteralEvaluatePropositionVisitor v
                        = new LiteralEvaluatePropositionVisitor(interval);
                for (Proposition prop : props) {
                    prop.accept(v);
                    if (v.evaluate()) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    private class LiteralEvaluatePropositionVisitor extends AbstractPropositionVisitor {

        private boolean result;
		private final Interval interval;

        LiteralEvaluatePropositionVisitor(Interval interval) {
			this.interval = interval;
        }

        boolean evaluate() {
            return result;
        }

        @Override
        public void visit(Context context) {
            handleTemporalProposition(context);
        }

        @Override
        public void visit(Constant constant) {
            this.result = true;
        }

        @Override
        public void visit(PrimitiveParameter primitiveParameter) {
            handleTemporalProposition(primitiveParameter);
        }

        @Override
        public void visit(Event event) {
            handleTemporalProposition(event);
        }

        @Override
        public void visit(AbstractParameter abstractParameter) {
            handleTemporalProposition(abstractParameter);
        }

        private void handleTemporalProposition(TemporalProposition tempProp) {
            Interval tempPropIval = tempProp.getInterval();
            this.result = Relation.CONTAINS_OR_EQUALS.hasRelation(interval, tempPropIval);
        }

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
				Set<String> propIds = this.knowledgeSource.collectPropIdDescendantsUsingAllNarrower(false, literal.getName());
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
