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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Node;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UnaryOperator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "unary_operators")
public class UnaryOperatorEntity extends NodeEntity {
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UnaryOperator.Op op;
	
	@OneToOne(cascade = CascadeType.ALL)
	@Column(nullable = false)
	private NodeEntity node;

	public UnaryOperator.Op getOp() {
		return op;
	}

	public void setOp(UnaryOperator.Op op) {
		this.op = op;
	}

	public NodeEntity getNode() {
		return node;
	}

	public void setNode(NodeEntity node) {
		this.node = node;
	}

	@Override
	public Node toNode() {
		UnaryOperator uo = new UnaryOperator();
		uo.setOp(this.op);
		uo.setNode(this.node.toNode());
		return uo;
	}
	
}
