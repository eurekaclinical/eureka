package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.ConstantDefinition;
import org.protempa.EventDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.SliceDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;

public final class SystemPropositionPackager implements
        PropositionDefinitionPackager<SystemProposition, PropositionDefinition> {

	@Override
	public PropositionDefinition pack(SystemProposition proposition) {
		switch (proposition.getSystemType()) {
			case EVENT:
				return new EventDefinition(proposition.getKey());
			case CONSTANT:
				return new ConstantDefinition(proposition.getKey());
			case PRIMITIVE_PARAMETER:
				return new PrimitiveParameterDefinition(proposition.getKey());
			case HIGH_LEVEL_ABSTRACTION:
				return new HighLevelAbstractionDefinition(proposition.getKey());
			case LOW_LEVEL_ABSTRACTION:
				return new LowLevelAbstractionDefinition(proposition.getKey());
			case SLICE_ABSTRACTION:
				return new SliceDefinition(proposition.getKey());
			default:
				return null;
		}
	}

}
