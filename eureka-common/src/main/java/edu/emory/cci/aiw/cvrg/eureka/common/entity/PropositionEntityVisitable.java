package edu.emory.cci.aiw.cvrg.eureka.common.entity;

public interface PropositionEntityVisitable {
	public void accept(PropositionEntityVisitor visitor);
}
