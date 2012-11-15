/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Contains attributes which describe a Protempa high level abstraction.
 *
 * @author hrathod
 */
@Entity
@Table(name = "high_level_abstractions")
public class HighLevelAbstraction extends Proposition {

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "highLevelAbstractionId")
	private List<Relation> relations;

	/**
	 * The propositions that the current proposition is abstracted from.
	 */
	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH,
		CascadeType.PERSIST})
	@JoinTable(name = "abstracted_from", joinColumns = {
		@JoinColumn(name = "target_proposition_id")})
	private List<Proposition> abstractedFrom;


	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> inRelations) {
		relations = inRelations;
	}

	/**
	 * Gets the list of propositions the current proposition is abstracted from.
	 *
	 * @return The list of propositions the current proposition is abstracted
	 * from.
	 */
	public List<Proposition> getAbstractedFrom() {
		return abstractedFrom;
	}

	/**
	 * Sets the list of propositions the current proposition is abstracted from.
	 *
	 * @param abstractedFrom The list of propositions the current proposition is
	 * abstracted from.
	 */
	public void setAbstractedFrom(List<Proposition> abstractedFrom) {
		this.abstractedFrom = abstractedFrom;
	}

	@Override
    public void accept(PropositionEntityVisitor visitor) {
		visitor.visit(this);
    }
}
