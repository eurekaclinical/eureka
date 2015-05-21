/*
 * #%L
 * Eureka WebApp
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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class JobListServlet extends HttpServlet {

	private final ServicesClient servicesClient;

	@Inject
	public JobListServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			List<SourceConfig> sourceConfigs = this.servicesClient.getSourceConfigs();
			req.setAttribute("sources", sourceConfigs);

			List<Destination> destinations = this.servicesClient.getDestinations();
			req.setAttribute("destinations", destinations);
			req.setAttribute("dateRangeSides", DateRangeSide.values());
			
			String jobIdStr = req.getParameter("jobId");
			Job job = null;
			if (jobIdStr != null) {
				Long jobId;
				try {
					jobId = Long.valueOf(jobIdStr);
				} catch (NumberFormatException ex) {
					throw new ServletException("Query parameter jobId must be a long, was " + jobIdStr);
				}
				job = this.servicesClient.getJob(jobId);
			} else {
				List<Job> jobs = this.servicesClient.getJobsDesc();
				if (!jobs.isEmpty()) {
					job = jobs.get(0);
				}
			}
			if (job != null) {
				req.setAttribute("jobStatus", job.toJobListRow());
				String destName = job.getDestinationId();
				req.setAttribute("destination", destName);
				String sourceConfigName = job.getSourceConfigId();
				req.setAttribute("sourceConfig", sourceConfigName);
			}

			req.getRequestDispatcher("/protected/job_submission.jsp").forward(req, resp);
		} catch (ClientException ex) {
			throw new ServletException("Error getting job list", ex);
		}

	}
}
