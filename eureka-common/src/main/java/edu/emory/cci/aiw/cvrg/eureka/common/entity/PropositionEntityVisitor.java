package edu.emory.cci.aiw.cvrg.eureka.common.entity;

public interface PropositionEntityVisitor {
	
	public void visit(SystemProposition proposition);
	
	public void visit(Categorization categorization);
	
	public void visit(HighLevelAbstraction highLevelAbstraction);
}
