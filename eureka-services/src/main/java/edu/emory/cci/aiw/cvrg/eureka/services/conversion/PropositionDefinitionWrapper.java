/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.AbstractDataElementEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Andrew Post
 */
final class PropositionDefinitionWrapper extends AbstractDataElementEntityVisitor {
	private List<PropositionDefinition> propDefs;
	private List<String> toShowPropIds;

	PropositionDefinitionWrapper() {
		this.propDefs = new ArrayList<PropositionDefinition>();
		this.toShowPropIds = new ArrayList<String>();
	}

	Collection<PropositionDefinition> getUserPropositionDefinitions() {
		return this.propDefs;
	}

	Collection<String> getToShowPropositionIds() {
		return this.toShowPropIds;
	}

	@Override
	public void visit(SystemProposition proposition) {
		throw new UnsupportedOperationException(
				"We're only processing user data elements here");
	}

	@Override
	public void visit(CategoryEntity category) {
		this.toShowPropIds.add(category.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX);
	}

	@Override
	public void visit(SequenceEntity sequenceEntity) {
		this.toShowPropIds.add(sequenceEntity.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX);
	}

	@Override
	public void visit(FrequencyEntity frequencyEntity) {
		this.toShowPropIds.add(frequencyEntity.getKey() + ConversionUtil.PRIMARY_PROP_ID_SUFFIX);
	}

	@Override
	public void visit(ValueThresholdGroupEntity valueThresholdGroup) {
		HighLevelAbstractionDefinition wrapper = 
				new HighLevelAbstractionDefinition(
				valueThresholdGroup.getKey() + ConversionUtil
						.PRIMARY_PROP_ID_SUFFIX + "_WRAPPER");
		wrapper.setDisplayName(valueThresholdGroup.getDisplayName());
		wrapper.setDescription(valueThresholdGroup.getDescription());
		TemporalExtendedParameterDefinition tepd = 
				new TemporalExtendedParameterDefinition(
				valueThresholdGroup.getKey() + 
				ConversionUtil.PRIMARY_PROP_ID_SUFFIX);
		
		tepd.setValue(NominalValue.getInstance(
				valueThresholdGroup.getKey() + "_VALUE"));
		wrapper.add(tepd);
		Relation relation = new Relation();
		wrapper.setRelation(tepd, tepd, relation);
		wrapper.setConcatenable(false);
		wrapper.setGapFunction(new SimpleGapFunction(0, null));
		wrapper.setSolid(false);
		this.propDefs.add(wrapper);
		this.toShowPropIds.add(wrapper.getId());
	}
    
}
