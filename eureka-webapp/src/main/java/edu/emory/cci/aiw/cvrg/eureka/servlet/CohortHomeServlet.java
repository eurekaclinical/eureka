package edu.emory.cci.aiw.cvrg.eureka.servlet;

/*
 * #%L
 * Eureka WebApp
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import edu.emory.cci.aiw.cvrg.eureka.servlet.proposition.JsonTreeData;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by akshatha on 7/23/14.
 */
public class CohortHomeServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger
			(CohortHomeServlet.class);

	private final ServicesClient servicesClient;

	@Inject
	public CohortHomeServlet(ServicesClient cohortsClient) {
		this.servicesClient = cohortsClient;
	}

	private JsonTreeData createData(String key, String data) {
		JsonTreeData d = new JsonTreeData();
		d.setData(data);
		d.setKeyVal("key", key);

		return d;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<JsonTreeData> cohortList = new ArrayList<>();
		try {
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
			List<CohortDestination> destinations = servicesClient.getCohortDestinations();
			for (Destination dest : destinations) {
				JsonTreeData cohortJson = createData(dest.getName(), dest.getName());
				cohortJson.setKeyVal("displayName", dest.getName());
				cohortJson.setKeyVal("description", dest.getDescription());
				Date createdAt = dest.getCreatedAt();
				cohortJson.setKeyVal("createdDate", createdAt != null ? df.format(createdAt) : null);
				Date updatedAt = dest.getUpdatedAt();
				cohortJson.setKeyVal("lastModifiedDate", updatedAt != null ? df.format(updatedAt) : null);
				cohortList.add(cohortJson);
			}
		} catch (ClientException ex) {
			throw new ServletException(ex);
		}
		/*JsonTreeData cohort = createData("lymphoma-iCD9","lymphoma-iCD9");
		cohort.setKeyVal("displayName","lymphoma-iCD9");
		cohort.setKeyVal("description","The cohort defined for lymphoma based on ICD9 codes");
		cohort.setKeyVal("createdDate","07/04/2014");
		cohort.setKeyVal("lastModifiedDate","07/04/2014");
		cohort.setKeyVal("currentlyActive","");
		cohortList.add(cohort);
		cohort=createData("lymphoma-labs","lymphoma-labs");
		cohort.setKeyVal("displayName","lymphoma-labs");
		cohort.setKeyVal("description","The cohort defined for lymphoma based on labs performed");
		cohort.setKeyVal("createdDate","07/02/2014");
		cohort.setKeyVal("lastModifiedDate","07/05/2014");
		cohort.setKeyVal("currentlyActive","Yes");
		cohortList.add(cohort);
		cohort=createData("lung-codes","lung-codes");
		cohort.setKeyVal("displayName","lung-codes");
		cohort.setKeyVal("description","The cohort defined for lung based on procedure and icd9 codes");
		cohort.setKeyVal("createdDate","07/03/2014");
		cohort.setKeyVal("lastModifiedDate","07/15/2014");
		cohort.setKeyVal("currentlyActive","");
		cohortList.add(cohort);*/
		req.setAttribute("cohorts", cohortList);
		req.getRequestDispatcher("/protected/cohort_home.jsp").forward(
				req, resp);
	}
}
