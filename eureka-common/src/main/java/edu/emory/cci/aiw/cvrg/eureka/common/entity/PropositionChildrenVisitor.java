package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.ArrayList;
import java.util.List;

public final class PropositionChildrenVisitor implements PropositionEntityVisitor {

	private List<Proposition> children;
	
	public List<Proposition> getChildren() {
		return children;
	}
	
	@Override
	public void visit(SystemProposition proposition) {
		this.children = new ArrayList<Proposition>();
	}
	
	@Override
	public void visit(Categorization categorization) {
		this.children = categorization.getInverseIsA();
	}

	@Override
	public void visit(HighLevelAbstraction highLevelAbstraction) {
		this.children = highLevelAbstraction.getAbstractedFrom();
	}

}
