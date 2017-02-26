/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.etl.job;

import java.util.List;

import org.protempa.PropositionDefinition;
import org.protempa.backend.dsb.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.JobEventEntity;
import org.eurekaclinical.eureka.client.comm.JobStatus;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.JobDao;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import org.protempa.backend.Configuration;
import java.util.Date;
import javax.persistence.EntityManager;

public final class Task implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
	private final JobDao jobDao;
	private final ETL etl;
	private Long jobId;
	private List<PropositionDefinition> propositionDefinitions;
	private List<String> propIdsToShow;
	private Filter filter;
	private boolean updateData;
	private Configuration prompts;
	private final Provider<EntityManager> entityManagerProvider;

	@Inject
	Task(JobDao inJobDao, ETL inEtl, Provider<EntityManager> inEntityManagerProvider) {
		this.jobDao = inJobDao;
		this.etl = inEtl;
		this.propIdsToShow = Collections.emptyList();
		this.propositionDefinitions = Collections.emptyList();
		this.entityManagerProvider = inEntityManagerProvider;
	}

	Long getJobId() {
		return jobId;
	}

	void setJobId(Long inJobId) {
		jobId = inJobId;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	List<String> getPropositionIdsToShow() {
		return new ArrayList<>(this.propIdsToShow);
	}

	void setPropositionIdsToShow(List<String> propIdsToShow) {
		if (propIdsToShow == null) {
			this.propIdsToShow = Collections.emptyList();
		} else {
			this.propIdsToShow = new ArrayList<>(propIdsToShow);
		}
	}

	List<PropositionDefinition> getPropositionDefinitions() {
		return propositionDefinitions;
	}

	void setPropositionDefinitions(List<PropositionDefinition> inPropositionDefinitions) {
		if (inPropositionDefinitions != null) {
			this.propositionDefinitions = inPropositionDefinitions;
		} else {
			propositionDefinitions = Collections.emptyList();
		}
	}

	public boolean isUpdateData() {
		return updateData;
	}

	public void setUpdateData(boolean updateData) {
		this.updateData = updateData;
	}

	public Configuration getPrompts() {
		return prompts;
	}

	public void setPrompts(Configuration prompts) {
		this.prompts = prompts;
	}

	@Override
	public void run() {
		JobEntity myJob = null;
		EntityManager entityManager = this.entityManagerProvider.get();
		try {
			entityManager.getTransaction().begin();
			myJob = this.jobDao.retrieve(this.jobId);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Just got job {} from user {}",
						new Object[]{myJob.getId(),
							myJob.getUser().getUsername()});
			}
			JobEventEntity startedJobEvent = new JobEventEntity();
			startedJobEvent.setJob(myJob);
			startedJobEvent.setTimeStamp(new Date());
			startedJobEvent.setStatus(JobStatus.STARTED);
			startedJobEvent.setMessage("Processing started");
			this.jobDao.update(myJob);
			entityManager.getTransaction().commit();

			PropositionDefinition[] propDefArray
					= new PropositionDefinition[this.getPropositionDefinitions()
					.size()];
			this.propositionDefinitions.toArray(propDefArray);

			String[] propIdsToShowArray
					= this.propIdsToShow.toArray(
							new String[this.propIdsToShow.size()]);

			entityManager.getTransaction().begin();
			this.etl.run(myJob, propDefArray, propIdsToShowArray, this.filter, this.updateData, this.prompts);
			this.jobDao.update(myJob);
			entityManager.getTransaction().commit();
			this.etl.close();
			JobEventEntity completedJobEvent = new JobEventEntity();
			Date jobFinishedDate = new Date();
			myJob.setFinished(jobFinishedDate);
			completedJobEvent.setJob(myJob);
			completedJobEvent.setTimeStamp(jobFinishedDate);
			completedJobEvent.setStatus(JobStatus.COMPLETED);
			completedJobEvent.setMessage("Processing completed without error");
			entityManager.getTransaction().begin();
			this.jobDao.update(myJob);
			entityManager.getTransaction().commit();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Completed job {} for user {} without errors.",
						new Object[]{
							myJob.getId(), myJob.getUser().getUsername()});
			}
			myJob = null;
		} catch (EtlException | Error | RuntimeException e) {
			handleError(myJob, e);
		} finally {
			if (myJob != null) {
				if (entityManager.getTransaction().isActive()) {
					entityManager.getTransaction().rollback();
				}
				try {
					Date jobFinishedDate = new Date();
					myJob.setFinished(jobFinishedDate);
					JobEventEntity failedJobEvent = new JobEventEntity();
					failedJobEvent.setJob(myJob);
					failedJobEvent.setTimeStamp(new Date());
					failedJobEvent.setStatus(JobStatus.FAILED);
					failedJobEvent.setMessage("Processing failed");
					LOGGER.error("Finished job {} for user {} with errors.",
							new Object[]{
								myJob.getId(), myJob.getUser().getUsername()});
					entityManager.getTransaction().begin();
					this.jobDao.update(myJob);
					entityManager.getTransaction().commit();
				} catch (Throwable ignore) {
					if (entityManager.getTransaction().isActive()) {
						entityManager.getTransaction().rollback();
					}
				}
			}
			if (this.etl != null) {
				try {
					this.etl.close();
				} catch (Throwable ignore) {
				}
			}
		}

	}

	private void handleError(JobEntity job, Throwable e) {
		if (job != null) {
			LOGGER.error("Job " + job.getId() + " for user "
					+ job.getUser().getUsername() + " failed: " + e.getMessage(), e);

			StringWriter sw = new StringWriter();
			try (PrintWriter ps = new PrintWriter(sw)) {
				e.printStackTrace(ps);
			}
			String msg = e.getMessage();
			if (msg == null) {
				msg = e.getClass().getName();
			}
			JobEventEntity errorJobEvent = new JobEventEntity();
			errorJobEvent.setJob(job);
			errorJobEvent.setTimeStamp(new Date());
			errorJobEvent.setStatus(JobStatus.ERROR);
			errorJobEvent.setMessage(msg);
			errorJobEvent.setExceptionStackTrace(sw.toString());
			this.jobDao.update(job);
		} else {
			LOGGER.error("Could not create job: " + e.getMessage(), e);
		}

	}
}
