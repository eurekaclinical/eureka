package edu.emory.cci.aiw.cvrg.eureka.services.packaging;

import org.protempa.PropositionDefinition;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Categorization;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.HighLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PropositionEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;

public final class PropositionDefinitionPackagerVisitor implements
        PropositionEntityVisitor {

	private PropositionDefinition propositionDefinition;

	public PropositionDefinition getPropositionDefinition() {
		return propositionDefinition;
	}
	
	@Override
	public void visit(SystemProposition proposition) {
		
	}

	@Override
	public void visit(Categorization categorization) {
		this.propositionDefinition = new CategorizationPackager()
		        .pack(categorization);
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		this.propositionDefinition = new HighLevelAbstractionPackager()
		        .pack(highLevelAbstraction);
	}

}
