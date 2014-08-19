package edu.emory.cci.aiw.cvrg.eureka.common.comm;

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

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.protempa.proposition.Proposition;

/**
 *
 * @author Andrew Post
 */
public class BinaryOperator extends Node {

	public static enum Op {
		AND,
		OR
	}
	
	private Op op;
	
	@JsonProperty("left_node")
	private Node leftNode;
	
	@JsonProperty("right_node")
	private Node rightNode;

	public Op getOp() {
		return op;
	}

	public void setOp(Op op) {
		this.op = op;
	}

	public Node getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}

	public Node getRightNode() {
		return rightNode;
	}

	public void setRightNode(Node rightNode) {
		this.rightNode = rightNode;
	}
	
	@Override
	boolean evaluate(Map<String, List<Proposition>> propMap) {
		switch (this.op) {
			case AND:
				return this.leftNode.evaluate(propMap) && this.rightNode.evaluate(propMap);
			case OR:
				return this.leftNode.evaluate(propMap) || this.rightNode.evaluate(propMap);
			default:
				throw new AssertionError("Invalid op " + this.op);
		}
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}
}
