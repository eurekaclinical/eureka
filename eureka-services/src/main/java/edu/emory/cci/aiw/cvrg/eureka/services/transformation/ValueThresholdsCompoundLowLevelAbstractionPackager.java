package edu.emory.cci.aiw.cvrg.eureka.services.transformation;

import java.util.ArrayList;
import java.util.List;

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;

public final class ValueThresholdsCompoundLowLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<ValueThresholdGroupEntity, CompoundLowLevelAbstractionDefinition> {

	@Override
	public CompoundLowLevelAbstractionDefinition pack(
	        ValueThresholdGroupEntity entity) {
		CompoundLowLevelAbstractionDefinition result = new CompoundLowLevelAbstractionDefinition(
		        entity.getKey());

		if (entity.getThresholdsOperator().getName().equalsIgnoreCase("any")) {
			result.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ANY);
		} else if (entity.getThresholdsOperator().getName()
		        .equalsIgnoreCase("all")) {
			result.setValueDefinitionMatchOperator(CompoundLowLevelAbstractionDefinition.ValueDefinitionMatchOperator.ALL);
		} else {
			throw new IllegalStateException("valueDefinitionMatchOperator"
			        + " can only be ANY or ALL");
		}

		List<LowLevelAbstractionDefinition> llas = new ArrayList<LowLevelAbstractionDefinition>();
		for (ValueThresholdEntity v : entity.getValueThresholds()) {
			LowLevelAbstractionDefinition def = new LowLevelAbstractionDefinition(
			        v.getAbstractedFrom().getKey() + "_CLASSIFICATION");
			def.addPrimitiveParameterId(v.getAbstractedFrom().getKey());
			def.setMinimumNumberOfValues(1);
			ValueThresholdsLowLevelAbstractionPackager
			        .thresholdToValueDefinitions(def.getId(), v, def);
			llas.add(def);
		}

		for (LowLevelAbstractionDefinition def : llas) {
			result.addValueClassification(entity.getKey() + "_VALUE",
			        def.getId(), def.getValueDefinitions().get(0).getId());
			result.addValueClassification(entity.getKey() + "_VALUE_COMP",
			        def.getId(), def.getValueDefinitions().get(1).getId());
		}

		return result;
	}
}
