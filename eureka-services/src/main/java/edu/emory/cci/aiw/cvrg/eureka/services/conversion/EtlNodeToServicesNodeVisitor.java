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
class EtlNodeToServicesNodeVisitor extends AbstractNodeVisitor {
	private Node node;
	private final CohortConversionSupport conversionSupport;

	public EtlNodeToServicesNodeVisitor() {
		this.conversionSupport = new CohortConversionSupport();
	}
	
	@Override
	public void visit(Literal literal) {
		Literal servicesLiteral = new Literal();
		servicesLiteral.setId(literal.getId());
		servicesLiteral.setName(
				this.conversionSupport.toDataElementKey(literal.getName()));
		servicesLiteral.setStart(literal.getStart());
		servicesLiteral.setFinish(literal.getFinish());
		this.node = servicesLiteral;
	}

	@Override
	public void visit(UnaryOperator unaryOperator) {
		UnaryOperator servicesUO = new UnaryOperator();
		servicesUO.setId(unaryOperator.getId());
		EtlNodeToServicesNodeVisitor v = new EtlNodeToServicesNodeVisitor();
		unaryOperator.getNode().accept(v);
		servicesUO.setNode(v.getNode());
		servicesUO.setOp(unaryOperator.getOp());
		this.node = servicesUO;
	}

	@Override
	public void visit(BinaryOperator binaryOperator) {
		BinaryOperator servicesBO = new BinaryOperator();
		servicesBO.setId(binaryOperator.getId());
		servicesBO.setOp(binaryOperator.getOp());
		EtlNodeToServicesNodeVisitor v = new EtlNodeToServicesNodeVisitor();
		binaryOperator.getLeftNode().accept(v);
		servicesBO.setLeftNode(v.getNode());
		binaryOperator.getRightNode().accept(v);
		servicesBO.setRightNode(v.getNode());
		this.node = servicesBO;
	}
	
	public Node getNode() {
		return this.node;
	}
	
}
