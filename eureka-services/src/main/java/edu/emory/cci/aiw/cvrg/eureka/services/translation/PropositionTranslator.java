package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Proposition;

interface PropositionTranslator<P extends Proposition, Q> {
	P translate(Q element);
}
