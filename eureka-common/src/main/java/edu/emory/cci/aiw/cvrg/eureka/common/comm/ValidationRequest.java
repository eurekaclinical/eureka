package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

public class ValidationRequest {
	private Long userId;
	private PropositionWrapper targetProposition;
	private List<PropositionWrapper> propositions;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long inUserId) {
		userId = inUserId;
	}

	public PropositionWrapper getTargetProposition() {
		return targetProposition;
	}

	public void setTargetProposition(PropositionWrapper inTargetProposition) {
		targetProposition = inTargetProposition;
	}

	public List<PropositionWrapper> getPropositions() {
		return propositions;
	}

	public void setPropositions(List<PropositionWrapper> inPropositions) {
		propositions = inPropositions;
	}
}
