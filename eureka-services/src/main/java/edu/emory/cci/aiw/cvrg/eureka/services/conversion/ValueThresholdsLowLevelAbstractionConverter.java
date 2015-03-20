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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PropositionDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.protempa.ContextDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.SlidingWindowWidthMode;
import static edu.emory.cci.aiw.cvrg.eureka.services.conversion.ConversionUtil.extractContextDefinition;

public final class ValueThresholdsLowLevelAbstractionConverter 
		extends AbstractValueThresholdGroupEntityConverter implements
		PropositionDefinitionConverter<ValueThresholdGroupEntity, LowLevelAbstractionDefinition> {
	
	private PropositionDefinitionConverterVisitor converterVisitor;
	private LowLevelAbstractionDefinition primary;
	private String primaryPropId;

	@Override
	public LowLevelAbstractionDefinition getPrimaryPropositionDefinition() {
		return primary;
	}

	@Override
	public String getPrimaryPropositionId() {
		return primaryPropId;
	}

	public void setConverterVisitor(
			PropositionDefinitionConverterVisitor inVisitor) {
		converterVisitor = inVisitor;
	}

	@Override
	public List<PropositionDefinition> convert(
			ValueThresholdGroupEntity entity) {
		if (entity.getValueThresholds().size() > 1) {
			throw new IllegalArgumentException(
					"Low-level abstraction definitions may be created only "
					+ "from singleton value thresholds.");
		}
		List<PropositionDefinition> result =
				new ArrayList<>();
		String propId = toPropositionId(entity);
		if (this.converterVisitor.addPropositionId(propId)) {
			LowLevelAbstractionDefinition wrapped =
					new LowLevelAbstractionDefinition(propId);
			wrapped.setDisplayName(entity.getDisplayName());
			wrapped.setDescription(entity.getDescription());
			wrapped.setAlgorithmId("stateDetector");

			// low-level abstractions can be created only from singleton value
			// thresholds
			if (entity.getValueThresholds() != null
					&& entity.getValueThresholds().size() == 1) {
				ValueThresholdEntity threshold =
						entity.getValueThresholds().get(0);
				threshold.getAbstractedFrom().accept(converterVisitor);
				Collection<PropositionDefinition> abstractedFrom =
						converterVisitor.getPropositionDefinitions();

				wrapped.addPrimitiveParameterId(
						converterVisitor.getPrimaryPropositionId());
				thresholdToValueDefinitions(entity, threshold, wrapped);
				List<ExtendedDataElement> extendedDataElements = threshold.getExtendedDataElements();
				if (extendedDataElements != null && !extendedDataElements.isEmpty()) {
					ContextDefinition contextDefinition = extractContextDefinition(entity,
							threshold.getExtendedDataElements(), threshold);
					result.add(contextDefinition);
					wrapped.setContextId(contextDefinition.getId());
				}
				result.addAll(abstractedFrom);
			}
			wrapped.setSlidingWindowWidthMode(SlidingWindowWidthMode.DEFAULT);
			wrapped.setGapFunction(new SimpleGapFunction(0, null));
			wrapped.setSourceId(sourceId(entity));
			result.add(wrapped);
			this.primary = wrapped;
			this.primaryPropId = wrapped.getPropositionId();
		}

		return result;
	}

}