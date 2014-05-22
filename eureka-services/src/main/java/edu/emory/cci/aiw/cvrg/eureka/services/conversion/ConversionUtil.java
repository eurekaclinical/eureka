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

import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ExtendedDataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
import java.util.Date;
import java.util.List;
import org.protempa.ContextDefinition;
import org.protempa.ContextOffset;
import org.protempa.proposition.interval.Interval.Side;
import org.protempa.PropertyConstraint;
import org.protempa.SimpleGapFunction;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.AbsoluteTimeGranularityUtil;
import org.protempa.proposition.value.AbsoluteTimeUnit;
import org.protempa.proposition.value.NominalValue;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

/**
 *
 * @author Andrew Post
 */
class ConversionUtil {

	static final String PRIMARY_PROP_ID_SUFFIX = "_PRIMARY";

	static AbsoluteTimeUnit unit(TimeUnit unit) {
		return unit != null ? AbsoluteTimeUnit.nameToUnit(unit.getName())
				: null;
	}

	static TemporalExtendedPropositionDefinition buildExtendedPropositionDefinition(ExtendedDataElement ep) {
		DataElementEntity dataElementEntity = ep.getDataElementEntity();
		TemporalExtendedPropositionDefinition tepd =
				buildExtendedPropositionDefinition(dataElementEntity);
		if (ep.getPropertyConstraint() != null) {
			PropertyConstraint pc = new PropertyConstraint();
			pc.setPropertyName(
					ep.getPropertyConstraint().getPropertyName());
			pc.setValue(ValueType.VALUE.parse(ep.getPropertyConstraint()
					.getValue()));
			pc.setValueComp(ValueComparator.EQUAL_TO);

			tepd.setPropertyConstraints(new PropertyConstraint[] {pc});
		}
		tepd.setMinLength(ep.getMinDuration());
		tepd.setMinLengthUnit(unit(ep.getMinDurationTimeUnit()));
		tepd.setMaxLength(ep.getMaxDuration());
		tepd.setMaxLengthUnit(unit(ep.getMaxDurationTimeUnit()));
		return tepd;
	}

	static ContextDefinition extractContextDefinition(
			ValueThresholdGroupEntity entity,
			List<ExtendedDataElement> extendedDataElements,
			ValueThresholdEntity v) {
		ContextDefinition cd = new ContextDefinition(
				entity.getKey() + "_SUB_CONTEXT");
		cd.setGapFunction(new SimpleGapFunction(
				Integer.valueOf(0), null));
		TemporalExtendedPropositionDefinition[] tepds =
				new TemporalExtendedPropositionDefinition[extendedDataElements.size()];
		int i = 0;
		for (ExtendedDataElement ede : extendedDataElements) {
			DataElementEntity dee = ede.getDataElementEntity();
			TemporalExtendedPropositionDefinition tepd;
			String tepdId = dee.getKey();
			if (!dee.isInSystem()) {
				tepdId += PRIMARY_PROP_ID_SUFFIX;
			}
			if (dee instanceof ValueThresholdGroupEntity) {
				TemporalExtendedParameterDefinition teParamD =
						new TemporalExtendedParameterDefinition(tepdId);
				teParamD.setValue(
						NominalValue.getInstance(
						dee.getKey() + "_VALUE"));
				tepd = teParamD;
			} else {
				tepd = new TemporalExtendedPropositionDefinition(tepdId);
			}
			tepd.setDisplayName(dee.getDisplayName());
			tepd.setMaxLength(ede.getMaxDuration());
			tepd.setMaxLengthUnit(
					unit(ede.getMaxDurationTimeUnit()));
			tepd.setMinLength(ede.getMinDuration());
			tepd.setMinLengthUnit(
					unit(ede.getMinDurationTimeUnit()));
			tepds[i++] = tepd;
		}
		cd.setInducedBy(tepds);
		ContextOffset offset = new ContextOffset();
		RelationOperator relOp = v.getRelationOperator();
		Integer withinAtLeast = v.getWithinAtLeast();
		Integer withinAtMost = v.getWithinAtMost();
		String relOpName = relOp.getName();
		if ("before".equals(relOpName)) {
			offset.setStartIntervalSide(Side.FINISH);
			offset.setFinishIntervalSide(Side.FINISH);
			if (withinAtLeast != null) {
				offset.setStartOffset(withinAtLeast);
			}
			offset.setStartOffsetUnits(unit(v.getWithinAtLeastUnits()));
			offset.setFinishOffset(withinAtMost);
			offset.setFinishOffsetUnits(unit(v.getWithinAtMostUnits()));
		} else if ("after".equals(relOpName)) {
			offset.setStartIntervalSide(Side.START);
			offset.setFinishIntervalSide(Side.START);
			offset.setStartOffset(withinAtMost != null ? -withinAtMost : null);
			offset.setStartOffsetUnits(unit(v.getWithinAtMostUnits()));
			if (withinAtLeast != null) {
				offset.setFinishOffset(-withinAtLeast);
			}
			offset.setFinishOffsetUnits(unit(v.getWithinAtLeastUnits()));
		} else if ("around".equals(relOpName)) {
			offset.setStartIntervalSide(Side.START);
			offset.setFinishIntervalSide(Side.FINISH);
			offset.setStartOffset(withinAtLeast != null ? -withinAtLeast : null);
			offset.setStartOffsetUnits(unit(v.getWithinAtLeastUnits()));
			offset.setFinishOffset(withinAtMost);
			offset.setFinishOffsetUnits(unit(v.getWithinAtMostUnits()));
		}
		cd.setOffset(offset);
		return cd;
	}

	private static TemporalExtendedPropositionDefinition buildExtendedPropositionDefinition(String propId,
			DataElementEntity entity) {
		TemporalExtendedPropositionDefinition tepd;
		if (entity instanceof ValueThresholdGroupEntity) {
			TemporalExtendedParameterDefinition tepvDef =
					new TemporalExtendedParameterDefinition(propId);
			tepvDef.setValue(
					NominalValue.getInstance(entity.getKey() + "_VALUE"));
			tepd = tepvDef;
		} else {
			tepd = new TemporalExtendedPropositionDefinition(propId);
		}
		return tepd;
	}

	private static TemporalExtendedPropositionDefinition buildExtendedPropositionDefinition(
			DataElementEntity dataElementEntity) {
		String propId;
		if (dataElementEntity.isInSystem()) {
			propId = dataElementEntity.getKey();
		} else {
			propId = dataElementEntity.getKey()
					+ ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		}
		return buildExtendedPropositionDefinition(propId, dataElementEntity);
	}
}
