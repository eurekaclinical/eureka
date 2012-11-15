package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

interface PropositionDefinitionPackager<P extends Proposition, Q extends PropositionDefinition> {
	Q pack(P proposition);
}
