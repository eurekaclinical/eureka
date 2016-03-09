package edu.emory.cci.aiw.cvrg.eureka.servlet;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Phenotype;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Statistics;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

/**
 *
 * @author Andrew Post
 */
public class JobPatientCountsServlet extends HttpServlet {

	private final ServicesClient servicesClient;
	private final ObjectWriter writer;

	@Inject
	public JobPatientCountsServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
		this.writer = new ObjectMapper().writer();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String jobIdStr = req.getParameter("jobId");
		String key = req.getParameter("propId");
		Long jobId = null;
		if (StringUtils.isNotEmpty(jobIdStr)) {
			try {
				jobId = Long.valueOf(jobIdStr);
				try {
					Statistics jobStats = this.servicesClient.getJobStats(jobId, key);
					Map<String, String> childrenToParents = jobStats.getChildrenToParents();
					Map<String, Integer> counts = jobStats.getCounts();

					Counts results = new Counts();
					List<Count> countResults = new ArrayList<>();

					Set<String> keySet = counts.keySet();
					List<Phenotype> summarizedConcepts = this.servicesClient.getPhenotypes(keySet.toArray(new String[keySet.size()]), true);
					Map<String, Phenotype> keyIdToDE = new HashMap<>();
					for (Phenotype de : summarizedConcepts) {
						keyIdToDE.put(de.getKey(), de);
					}

					for (Map.Entry<String, Integer> me : counts.entrySet()) {
						Count count = new Count();
						count.setKey(me.getKey());
						count.setParentKeyId(childrenToParents.get(me.getKey()));
						count.setCount(me.getValue());
						Phenotype de = keyIdToDE.get(me.getKey());
						if (de != null) {
							count.setDisplayName(de.getDisplayName());
						}
						countResults.add(count);
					}

					results.setCounts(countResults);

					this.writer.writeValue(resp.getOutputStream(), results);
				} catch (ClientException ex) {
					if (ex.getResponseStatus() == Status.NOT_FOUND) {
						resp.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "No job with jobId " + jobIdStr);
					} else {
						throw new ServletException("Error getting patient counts for job " + jobId, ex);
					}
				}
			} catch (NumberFormatException nfe) {
				resp.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "jobId parameter must be a long, was " + jobIdStr);
			}
		} else {
			resp.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "jobId parameter is required");
		}
	}

}
