/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import java.util.List;

import org.protempa.PropositionDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;

public final class Task implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
	private final JobDao jobDao;
	private final ETL etl;
	private Long jobId;
	private List<PropositionDefinition> propositionDefinitions;
	private List<String> propIdsToShow;

	@Inject
	Task(JobDao inJobDao, ETL inEtl) {
		this.jobDao = inJobDao;
		this.etl = inEtl;
	}

	Long getJobId() {
		return jobId;
	}

	void setJobId(Long inJobId) {
		jobId = inJobId;
	}

	List<String> getPropositionIdsToShow() {
		return propIdsToShow;
	}

	void setPropositionIdsToShow(List<String> propIdsToShow) {
		this.propIdsToShow = propIdsToShow;
	}

	List<PropositionDefinition> getPropositionDefinitions() {
		return propositionDefinitions;
	}

	void setPropositionDefinitions(List<PropositionDefinition>
		inPropositionDefinitions) {
		propositionDefinitions = inPropositionDefinitions;
	}

	@Override
	public void run() {
		Job myJob = this.jobDao.retrieve(this.jobId);
		LOGGER.info("{} just got a job from user {}, id={}",
				myJob.getUserId(), new Object[]{
				Thread.currentThread().getName(), myJob.toString()});
		myJob.setNewState("PROCESSING", null, null);
		LOGGER.debug("About to save job: {}", myJob.toString());
		this.jobDao.update(myJob);

		Long configId = myJob.getConfigurationId();

		try {
			PropositionDefinition[] propDefArray =
				new PropositionDefinition[this.getPropositionDefinitions()
					.size()];
			this.propositionDefinitions.toArray(propDefArray);
			
			String[] propIdsToShowArray = 
					this.propIdsToShow.toArray(
					new String[this.propIdsToShow.size()]);
			
			this.etl.run("config" + configId, propDefArray, 
					propIdsToShowArray, myJob.getId());
			this.etl.close();
		} catch (EtlException e) {
			handleError(myJob, e);
		} catch (RuntimeException e) {
			handleError(myJob, e);
		} catch (Error e) {
			handleError(myJob, e);
		}

		myJob.setNewState("DONE", null, null);
		LOGGER.info("{} completed job {} for user {} without errors.",
				Thread.currentThread().getName(), 
				new Object[]{myJob.getId(), myJob.getUserId()});
		this.jobDao.update(myJob);
	}

	private void handleError(Job job, Throwable e) {
		LOGGER.error("Job " + job.getId() + " for user " 
				+ job.getUserId() + " failed: " + e.getMessage(), e);
		StackTraceElement[] ste = e.getStackTrace();
		String[] st = new String[ste.length];
		for (int i = 0; i < ste.length; i++) {
			st[i] = ste[i].toString();
		}
		String msg = e.getMessage();
		if (msg == null) {
			msg = e.getClass().getName();
		}
		job.setNewState("EXCEPTION", msg, st);
		this.jobDao.update(job);
	}
}
