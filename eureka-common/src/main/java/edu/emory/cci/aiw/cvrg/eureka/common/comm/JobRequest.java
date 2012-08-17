package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;

public class JobRequest {

	private Job job;
	private List<PropositionWrapper> propositionWrappers;

	public Job getJob() {
		return job;
	}

	public void setJob(Job inJob) {
		job = inJob;
	}

	public List<PropositionWrapper> getPropositionWrappers() {
		return propositionWrappers;
	}

	public void setPropositionWrappers(List<PropositionWrapper>
			inPropositionWrappers) {
		propositionWrappers = inPropositionWrappers;
	}
}
