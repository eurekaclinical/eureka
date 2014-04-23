/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity.CategoryType;
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

/**
 * Contains attributes which describe a Protempa high level abstraction.
 *
 * @author hrathod
 */
@Entity
@Table(name = "sequences")
public class SequenceEntity extends DataElementEntity {

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="primaryextendeddataelement_id")
	private ExtendedDataElement primaryExtendedDataElement;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "sequence_id")
	private List<Relation> relations;
	
	public SequenceEntity() {
		super(CategoryType.HIGH_LEVEL_ABSTRACTION);
	}

	public ExtendedDataElement getPrimaryExtendedDataElement() {
		return primaryExtendedDataElement;
	}

	public void setPrimaryExtendedDataElement(ExtendedDataElement inExtendedDataElement) {
		primaryExtendedDataElement = inExtendedDataElement;
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
	public List<DataElementEntity> getAbstractedFrom() {
		Map<Long, DataElementEntity> entities = 
				new HashMap<>();
		for (Relation relation : this.relations) {
			DataElementEntity lhs = 
					relation.getLhsExtendedDataElement()
					.getDataElementEntity();
			DataElementEntity rhs = 
					relation.getRhsExtendedDataElement()
					.getDataElementEntity();
			entities.put(lhs.getId(), lhs);
			entities.put(rhs.getId(), rhs);
		}
		return new ArrayList<>(entities.values());
	}

	@Override
	public void accept(DataElementEntityVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}
}
