package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;

/**
 *
 * @author Andrew Post
 */
public class AbstractValueThresholdGroupEntityConverter extends AbstractConverter {

	AbstractValueThresholdGroupEntityConverter() {
	}
	
	HighLevelAbstractionDefinition wrap(ValueThresholdGroupEntity valueThresholdGroup) {
		HighLevelAbstractionDefinition wrapper = 
				new HighLevelAbstractionDefinition(
						toPropositionId(valueThresholdGroup));
		wrapper.setDisplayName(valueThresholdGroup.getDisplayName());
		wrapper.setDescription(valueThresholdGroup.getDescription());
		TemporalExtendedParameterDefinition tepd = 
				new TemporalExtendedParameterDefinition(
				toPropositionId(valueThresholdGroup));
		
		tepd.setValue(asValue(valueThresholdGroup));
		wrapper.add(tepd);
		Relation relation = new Relation();
		wrapper.setRelation(tepd, tepd, relation);
		wrapper.setConcatenable(false);
		wrapper.setGapFunction(new SimpleGapFunction(0, null));
		wrapper.setSolid(false);
		return wrapper;
	}
	
}
