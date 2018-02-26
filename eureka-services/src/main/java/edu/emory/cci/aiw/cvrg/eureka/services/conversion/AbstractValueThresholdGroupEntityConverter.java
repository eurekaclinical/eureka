package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
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

import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdGroupEntity;
import java.util.HashMap;
import java.util.Map;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.NominalValue;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

/**
 *
 * @author Andrew Post
 */
public class AbstractValueThresholdGroupEntityConverter extends AbstractConverter {
	
	private static final Map<String, ValueComparator> VC_MAP =
			new HashMap<>();
	static {
		VC_MAP.put(">", ValueComparator.GREATER_THAN);
		VC_MAP.put(">=", ValueComparator.GREATER_THAN_OR_EQUAL_TO);
		VC_MAP.put("=", ValueComparator.EQUAL_TO);
		VC_MAP.put("not=", ValueComparator.NOT_EQUAL_TO);
		VC_MAP.put("<=", ValueComparator.LESS_THAN_OR_EQUAL_TO);
		VC_MAP.put("<", ValueComparator.LESS_THAN);
	}

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
				toPropositionIdWrapped(valueThresholdGroup));
		
		tepd.setValue(asValue(valueThresholdGroup));
		wrapper.add(tepd);
		Relation relation = new Relation();
		wrapper.setRelation(tepd, tepd, relation);
		wrapper.setConcatenable(false);
		wrapper.setGapFunction(new SimpleGapFunction(0, null));
		wrapper.setSolid(false);
		wrapper.setSourceId(sourceId(valueThresholdGroup));
		return wrapper;
	}
	
	void thresholdToValueDefinitions(ValueThresholdGroupEntity entity,
			ValueThresholdEntity threshold,
			LowLevelAbstractionDefinition def) {
		LowLevelAbstractionValueDefinition valueDef =
				new LowLevelAbstractionValueDefinition(
				def, asValueString(entity));
		valueDef.setValue(NominalValue.getInstance(asValueString(entity)));
		if (threshold.getMinValueThreshold() != null
				&& threshold.getMinValueComp() != null) {
			valueDef.setParameterValue("minThreshold", ValueType.VALUE
					.parse(threshold.getMinValueThreshold().toString()));
			valueDef.setParameterComp("minThreshold", 
					VC_MAP.get(threshold.getMinValueComp().getName()));
		}
		if (threshold.getMaxValueThreshold() != null
				&& threshold.getMaxValueComp() != null) {
			valueDef.setParameterValue("maxThreshold", ValueType.VALUE
					.parse(threshold.getMaxValueThreshold().toString()));
			valueDef.setParameterComp("maxThreshold", 
					VC_MAP.get(threshold.getMaxValueComp().getName()));
		}
		if (threshold.getMinValueThreshold() != null
				&& threshold.getMinValueComp() != null
				&& threshold.getMaxValueThreshold() != null
				&& threshold.getMaxValueComp() != null) {
			LowLevelAbstractionValueDefinition comp1ValueDef =
				new LowLevelAbstractionValueDefinition(def, asValueCompString(entity));
			comp1ValueDef.setValue(NominalValue.getInstance(asValueCompString(entity)));
			comp1ValueDef.setParameterValue("maxThreshold", ValueType.VALUE
					.parse(threshold.getMinValueThreshold().toString()));
			comp1ValueDef.setParameterComp("maxThreshold",
					VC_MAP.get(threshold.getMinValueComp()
					.getComplement().getName()));
			
			LowLevelAbstractionValueDefinition comp2ValueDef =
				new LowLevelAbstractionValueDefinition(def, asValueCompString(entity));
			comp2ValueDef.setValue(NominalValue.getInstance(asValueCompString(entity)));
			comp2ValueDef.setParameterValue("minThreshold", ValueType.VALUE
					.parse(threshold.getMaxValueThreshold().toString()));
			comp2ValueDef.setParameterComp("minThreshold",
					VC_MAP.get(
					threshold.getMaxValueComp().getComplement().getName()));
		} else if (threshold.getMinValueThreshold() != null
				&& threshold.getMinValueComp() != null) {
			LowLevelAbstractionValueDefinition compValueDef =
				new LowLevelAbstractionValueDefinition(def, asValueCompString(entity));
			compValueDef.setValue(NominalValue.getInstance(asValueCompString(entity)));
			compValueDef.setParameterValue("maxThreshold", ValueType.VALUE
					.parse(threshold.getMinValueThreshold().toString()));
			compValueDef.setParameterComp("maxThreshold",
					VC_MAP.get(threshold.getMinValueComp()
					.getComplement().getName()));
		} else if (threshold.getMaxValueThreshold() != null
				&& threshold.getMaxValueComp() != null) {
			LowLevelAbstractionValueDefinition compValueDef =
				new LowLevelAbstractionValueDefinition(def, asValueCompString(entity));
			compValueDef.setValue(NominalValue.getInstance(asValueCompString(entity)));
			compValueDef.setParameterValue("minThreshold", ValueType.VALUE
					.parse(threshold.getMaxValueThreshold().toString()));
			compValueDef.setParameterComp("minThreshold",
					VC_MAP.get(
					threshold.getMaxValueComp().getComplement().getName()));
		}
	}
	
}
