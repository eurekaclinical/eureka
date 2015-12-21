package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/*
 * #%L
 * Eureka Common
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
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
public class Literal extends Node {

	private String name;

	private Date start;

	private Date finish;

	private Interval interval;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getFinish() {
		return finish;
	}

	public void setFinish(Date finish) {
		this.finish = finish;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}

	@Override
	boolean evaluate(Map<String, List<Proposition>> propMap) {
		List<Proposition> props = propMap.get(this.name);
		if (props != null && !props.isEmpty()) {
			if (this.interval == null && (this.start != null || this.finish != null)) {
				this.interval
						= new AbsoluteTimeIntervalFactory().getInstance(
								start, null,
								finish, null);
			}
			if (this.interval != null) {
				LiteralEvaluatePropositionVisitor v
						= new LiteralEvaluatePropositionVisitor();
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

	@Override
	public void accept(NodeVisitor nodeVisitor) {
		nodeVisitor.visit(this);
	}

	private class LiteralEvaluatePropositionVisitor extends AbstractPropositionVisitor {

		private boolean result;

		LiteralEvaluatePropositionVisitor() {
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
}
