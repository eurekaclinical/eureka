package edu.emory.cci.aiw.cvrg.eureka.services.transformation;

import org.protempa.MinMaxGapFunction;
import org.protempa.SliceDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;

import static edu.emory.cci.aiw.cvrg.eureka.services.transformation.PropositionDefinitionPackagerUtil.unit;

public final class FrequencySliceAbstractionPackager implements
        PropositionDefinitionPackager<FrequencyEntity, SliceDefinition> {

	@Override
	public SliceDefinition pack(FrequencyEntity entity) {
		SliceDefinition result = new SliceDefinition(entity.getKey());

		result.setMinIndex(entity.getAtLeastCount());
		result.addAbstractedFrom(entity.getAbstractedFrom().getKey());
		result.setGapFunction(new MinMaxGapFunction(entity.getWithinAtLeast(),
		        unit(entity.getWithinAtLeastUnits()), entity.getWithinAtMost(),
		        unit(entity.getWithinAtMostUnits())));

		return result;
	}
}
