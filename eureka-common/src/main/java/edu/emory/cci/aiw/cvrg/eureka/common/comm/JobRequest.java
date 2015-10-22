/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.util.List;

import org.protempa.PropositionDefinition;

public class JobRequest {

	private JobSpec job;
	private List<PropositionDefinition> userPropositions;
	private List<String> propIdsToShow;

	public JobRequest() {
		this.job = new JobSpec();
	}

	public JobSpec getJobSpec() {
		return job;
	}

	public void setJobSpec(JobSpec inJob) {
		if (inJob == null) {
			this.job = new JobSpec();
		} else {
			this.job = inJob;
		}
	}

	public List<PropositionDefinition> getUserPropositions() {
		return userPropositions;
	}

	public void setUserPropositions(
			List<PropositionDefinition> inUserPropositions) {
		this.userPropositions = inUserPropositions;
	}

	public List<String> getPropositionIdsToShow() {
		return propIdsToShow;
	}

	public void setPropositionIdsToShow(List<String> inPropositionIds) {
		this.propIdsToShow = inPropositionIds;
	}
}
