/*
 * #%L
 * Eureka WebApp
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
package edu.emory.cci.aiw.cvrg.eureka.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import org.eurekaclinical.eureka.client.comm.Destination;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import org.eurekaclinical.common.comm.clients.ClientException;

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
				List<Job> jobs = this.servicesClient.getLatestJob();
				if(!jobs.isEmpty()){
					job = jobs.get(0);
				}
			}
			if (job != null) {
				req.setAttribute("jobId", job.getId());

				String sourceConfigName = job.getSourceConfigId();
				req.setAttribute("sourceConfig", sourceConfigName);

				String destName = job.getDestinationId();
				Destination destination = null;
				for (Destination candidateDest : destinations) {
					if (candidateDest.getName().equals(destName)) {
						destination = candidateDest;
						break;
					}
				}

				if (destination != null) {
					req.setAttribute("destination", destination);
					job.setGetStatisticsSupported(destination.isGetStatisticsSupported());
					req.setAttribute("jobStatus", job.toJobListRow());
				} else {
					throw new ServletException("Can't find destination " + destName);
				}
			}

			req.getRequestDispatcher("/protected/job_submission.jsp").forward(req, resp);
		} catch (ClientException ex) {
			throw new ServletException("Error getting job list", ex);
		}

	}
}
