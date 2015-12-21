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
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobListRow;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class JobPollServlet extends HttpServlet {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private final ServicesClient servicesClient;

	@Inject
	public JobPollServlet (ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/json");

		String jobIdStr = req.getParameter("jobId");
		Long jobId = null;
		if (jobIdStr != null) {
			try {
				jobId = Long.valueOf(jobIdStr);
			} catch (NumberFormatException nfe) {
				throw new ServletException("jobId parameter must be a long, was " + jobIdStr);
			}
		}

		Job job = null;
		try {
			if (jobId != null) {
				job = this.servicesClient.getJob(jobId);
			} else {
				List<Job> jobs = this.servicesClient.getJobsDesc();
				if (!jobs.isEmpty()) {
					job = jobs.get(0);
				}
			}
		} catch (ClientException ex) {
			throw new ServletException("Error polling job list", ex);
		}

		JobListRow jobListRow = null;
		if (job != null) {
			jobListRow = job.toJobListRow();
		}
		String value = MAPPER.writeValueAsString(jobListRow);
		resp.setContentLength(value.length());
		PrintWriter out = resp.getWriter();
		out.println(value);

	}
}
