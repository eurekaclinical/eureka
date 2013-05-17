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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobStatus;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import java.util.List;

public class JobPollServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");

		resp.setContentType("application/json");

		String jobIdStr = req.getParameter("jobId");
		Long jobId;
		if (jobIdStr != null) {
			try {
				jobId = Long.valueOf(jobIdStr);
			} catch (NumberFormatException nfe) {
				throw new ServletException("jobId parameter must be a long, was " + jobIdStr);
			}
		} else {
			jobId = null;
		}

		ServicesClient servicesClient = new ServicesClient(eurekaServicesUrl);
		Job job;
		try {
			if (jobId != null) {
				job = servicesClient.getJob(jobId);
			} else {
				List<Job> jobs = servicesClient.getJobsDesc();
				if (!jobs.isEmpty()) {
					job = jobs.get(0);
				} else {
					job = null;
				}
			}
		} catch (ClientException ex) {
			throw new ServletException("Error polling job list", ex);
		}

		JobStatus jobStatus;
		if (job != null) {
			jobStatus = job.toJobStatus();
		} else {
			jobStatus = null;
		}
		ObjectMapper mapper = new ObjectMapper();
		resp.setContentLength(mapper.writeValueAsString(jobStatus)
				.length());
		PrintWriter out = resp.getWriter();
		out.println(mapper.writeValueAsString(jobStatus));

	}
}
