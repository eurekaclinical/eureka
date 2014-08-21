package edu.emory.cci.aiw.cvrg.eureka.common.entity;

/*
 * #%L
 * Eureka Common
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UnaryOperator;

/**
 *
 * @author Andrew Post
 */
public class NodeToNodeEntityVisitor extends AbstractNodeVisitor {
	private NodeEntity node;

	@Override
	public void visit(Literal literal) {
		LiteralEntity literalEntity = new LiteralEntity();
		literalEntity.setId(literal.getId());
		literalEntity.setName(literal.getName());
		literalEntity.setStart(literal.getStart());
		literalEntity.setFinish(literal.getFinish());
		this.node = literalEntity;
	}

	@Override
	public void visit(UnaryOperator unaryOperator) {
		UnaryOperatorEntity uo = new UnaryOperatorEntity();
		uo.setOp(unaryOperator.getOp());
		uo.setId(unaryOperator.getId());
		NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
		unaryOperator.getNode().accept(v);
		uo.setNode(v.getNodeEntity());
		this.node = uo;
	}

	@Override
	public void visit(BinaryOperator binaryOperator) {
		BinaryOperatorEntity boe = new BinaryOperatorEntity();
		boe.setOp(binaryOperator.getOp());
		boe.setId(binaryOperator.getId());
		NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
		binaryOperator.getLeftNode().accept(v);
		boe.setLeftNode(v.getNodeEntity());
		binaryOperator.getRightNode().accept(v);
		boe.setRightNode(v.getNodeEntity());
		this.node = boe;
	}
	
	public NodeEntity getNodeEntity() {
		return this.node;
	}
	
}
