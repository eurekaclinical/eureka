/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.ExtendedPhenotype;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdGroupEntity;
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
				List<ExtendedPhenotype> extendedPhenotypes = threshold.getExtendedPhenotypes();
				if (extendedPhenotypes != null && !extendedPhenotypes.isEmpty()) {
					ContextDefinition contextDefinition = extractContextDefinition(entity,
							threshold.getExtendedPhenotypes(), threshold);
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
