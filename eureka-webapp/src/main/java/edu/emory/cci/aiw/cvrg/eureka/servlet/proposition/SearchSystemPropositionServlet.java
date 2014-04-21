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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sleepycat.je.tree.SearchResult;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ClientException;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.clients.ServicesClient;

public class SearchSystemPropositionServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SearchSystemPropositionServlet.class);
	private final ServicesClient servicesClient;

	@Inject
	public SearchSystemPropositionServlet(ServicesClient inClient) {
		this.servicesClient = inClient;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String eurekaServicesUrl = req.getSession().getServletContext()
				.getInitParameter("eureka-services-url");


		String searchKey = req.getParameter("searchKey");
		List<String> searchResult = null;
		List<String> processedSearchResult = null;
		if (searchKey == null) {
			throw new ServletException("Invalid parameter id: " + searchKey);
		}
		try {
			searchResult = servicesClient
					.getSystemElementSearchResults(searchKey);
			processedSearchResult = convertResultsForJstreeRequirement(searchResult);
		} catch (ClientException e) {
			e.printStackTrace();
		}

		LOGGER.debug("executed resource ");

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		mapper.writeValue(out, processedSearchResult);
	}

	private List<String> convertResultsForJstreeRequirement(
			List<String> searchResult) {

		List<String> newResultSet = new ArrayList<String>();
		newResultSet.add("#root");
		for(String currentSearchResult : searchResult){
			newResultSet.add("#"+currentSearchResult.replaceAll("[^a-zA-Z0-9]", "\\\\$0"));
		}
		return newResultSet;
	}

}
