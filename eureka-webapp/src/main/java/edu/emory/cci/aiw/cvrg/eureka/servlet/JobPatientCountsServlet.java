package edu.emory.cci.aiw.cvrg.eureka.servlet;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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
import com.google.inject.Inject;
import com.sun.jersey.api.client.ClientResponse.Status;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
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
					List<DataElement> summarizedConcepts = this.servicesClient.getDataElements(keySet.toArray(new String[keySet.size()]), true);
					Map<String, DataElement> keyIdToDE = new HashMap<>();
					for (DataElement de : summarizedConcepts) {
						keyIdToDE.put(de.getKey(), de);
					}

					for (Map.Entry<String, Integer> me : counts.entrySet()) {
						Count count = new Count();
						count.setKey(me.getKey());
						count.setParentKeyId(childrenToParents.get(me.getKey()));
						count.setCount(me.getValue());
						DataElement de = keyIdToDE.get(me.getKey());
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
