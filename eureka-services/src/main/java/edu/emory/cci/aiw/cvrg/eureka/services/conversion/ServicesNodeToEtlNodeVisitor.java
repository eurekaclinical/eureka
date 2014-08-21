package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Literal;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Node;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UnaryOperator;

/**
 *
 * @author Andrew Post
 */
public class ServicesNodeToEtlNodeVisitor extends AbstractNodeVisitor {
	private ConversionSupport conversionSupport;
	private Node node;

	public ServicesNodeToEtlNodeVisitor() {
		this.conversionSupport = new ConversionSupport();
	}
	
	@Override
	public void visit(Literal literal) {
		Literal etlLiteral = new Literal();
		etlLiteral.setId(literal.getId());
		etlLiteral.setName(
				this.conversionSupport.toPropositionId(literal.getName()));
		etlLiteral.setStart(literal.getStart());
		etlLiteral.setFinish(literal.getFinish());
		this.node = etlLiteral;
	}

	@Override
	public void visit(UnaryOperator unaryOperator) {
		UnaryOperator etlUO = new UnaryOperator();
		etlUO.setId(unaryOperator.getId());
		ServicesNodeToEtlNodeVisitor v =
				new ServicesNodeToEtlNodeVisitor();
		unaryOperator.getNode().accept(v);
		etlUO.setNode(v.getNode());
		etlUO.setOp(unaryOperator.getOp());
		this.node = etlUO;
	}

	@Override
	public void visit(BinaryOperator binaryOperator) {
		BinaryOperator etlBO = new BinaryOperator();
		etlBO.setId(binaryOperator.getId());
		etlBO.setOp(binaryOperator.getOp());
		ServicesNodeToEtlNodeVisitor v =
				new ServicesNodeToEtlNodeVisitor();
		binaryOperator.getLeftNode().accept(v);
		etlBO.setLeftNode(v.getNode());
		binaryOperator.getRightNode().accept(v);
		etlBO.setRightNode(v.getNode());
		this.node = etlBO;
	}

	public Node getNode() {
		return this.node;
	}
	
}
