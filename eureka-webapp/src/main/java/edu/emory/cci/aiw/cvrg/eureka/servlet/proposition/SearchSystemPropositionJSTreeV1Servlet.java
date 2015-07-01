package edu.emory.cci.aiw.cvrg.eureka.servlet.proposition;

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

import com.google.inject.Inject;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchSystemPropositionJSTreeV1Servlet extends HttpServlet {
	private final ServicesClient servicesClient;

	@Inject
	public SearchSystemPropositionJSTreeV1Servlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	private JsonTreeData createData(SystemElement element) {
		JsonTreeData d = new JsonTreeData();
		d.setState("closed");
		d.setId(element.getKey());
		d.setData(this.getDisplayName(element));
		d.setText(this.getDisplayName(element));
		d.setKeyVal("id", element.getKey());

		String properties = StringUtils.join(element.getProperties(), ",");
		d.setKeyVal("data-properties", properties);
		d.setKeyVal("data-key", element.getKey());
		d.setKeyVal("data-space", "system");
		d.setKeyVal("data-type", element.getSystemType().toString());
		d.setKeyVal("data-proposition", element.getKey());
		d.setChildren(false);

		return d;
	}

	private String getDisplayName(SystemElement p) {
		String displayName = "";

		if (p.getDisplayName() != null && !p.getDisplayName().equals("")) {
			displayName = p.getDisplayName() + " (" + p.getKey() + ")";

		} else {

			displayName = p.getKey();

		}

		return displayName;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String searchKey = req.getParameter("str");

		List<JsonTreeData> l = new ArrayList<>();
		if (searchKey == null) {
			throw new ServletException("Search key is null");
		}

		try {
			List<String> processedSearchResult = null;
			List<String> searchResult = null;
			searchResult = servicesClient
					.getSystemElementSearchResults(searchKey);
			processedSearchResult = convertResultsForJstreeRequirement(searchResult);

			ObjectMapper mapper = new ObjectMapper();
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			mapper.writeValue(out, processedSearchResult);



		} catch (ClientException e) {
			throw new ServletException("Error getting search results", e);
		}


	}

	private List<String> convertResultsForJstreeRequirement(
			List<String> searchResult) {
		List<String> newResultSet = new ArrayList<>();
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");

		newResultSet.add("#root");
		for (String currentSearchResult : searchResult) {
			newResultSet.add("#" + pattern.matcher(currentSearchResult).replaceAll("\\\\$0"));
		}
		return newResultSet;
	}

}
