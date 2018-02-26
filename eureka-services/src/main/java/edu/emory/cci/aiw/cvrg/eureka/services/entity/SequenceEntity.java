/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.entity;

//import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity.CategoryType;

/**
 * Contains attributes which describe a Protempa high level abstraction.
 *
 * @author hrathod
 */
@Entity
@Table(name = "sequences")
public class SequenceEntity extends PhenotypeEntity {
	private static final Logger LOGGER
			= LoggerFactory.getLogger(SequenceEntity.class);

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="primaryextendedphenotype_id")
	private ExtendedPhenotype primaryExtendedPhenotype;
        
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "sequence_id")
	private List<Relation> relations;
	
	public SequenceEntity() {
		super(CategoryType.HIGH_LEVEL_ABSTRACTION);
	}

	public ExtendedPhenotype getPrimaryExtendedPhenotype() {
		return primaryExtendedPhenotype;
	}

	public void setPrimaryExtendedPhenotype(ExtendedPhenotype inExtendedPhenotype) {
		primaryExtendedPhenotype = inExtendedPhenotype;
	}
        
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
	 *         from.
	 */
	public List<PhenotypeEntity> getAbstractedFrom() {    
		Map<Long, PhenotypeEntity> entities = 
				new HashMap<>();
		for (Relation relation : this.relations) {
			PhenotypeEntity lhs = 
					relation.getLhsExtendedPhenotype()
					.getPhenotypeEntity();
			PhenotypeEntity rhs = 
					relation.getRhsExtendedPhenotype()
					.getPhenotypeEntity();
                        
			entities.put(lhs.getId(), lhs);
			entities.put(rhs.getId(), rhs);
		}
             
		return new ArrayList<>(entities.values());
	}

	@Override
	public void accept(PhenotypeEntityVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}
}
