package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.LowLevelAbstractionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;

public final class LowLevelAbstractionPackager
        implements
        PropositionDefinitionPackager<LowLevelAbstraction, LowLevelAbstractionDefinition> {

	@Override
	public LowLevelAbstractionDefinition pack(LowLevelAbstraction proposition) {
		LowLevelAbstractionDefinition result = new LowLevelAbstractionDefinition(
		        proposition.getId().toString());

		
		
		return result;
	}

}
