package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.PropositionWrapper;

public final class PropositionTypeVisitor implements PropositionEntityVisitor {

	private PropositionWrapper.Type type;
	
	public PropositionWrapper.Type getType() {
		return type;
	}
	
	@Override
    public void visit(SystemProposition proposition) {
		this.type = PropositionWrapper.Type.SYSTEM;
    }

	@Override
	public void visit(Categorization categorization) {
		this.type = PropositionWrapper.Type.CATEGORIZATION;
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		this.type = PropositionWrapper.Type.SEQUENCE;
	}

}
