package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients;

/*
 * #%L
 * Eureka Common
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

import com.sun.jersey.api.client.GenericType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import java.util.List;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
public class CohortsClient extends EurekaClient {
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(CohortsClient.class);
	private static final GenericType<List<Cohort>> CohortList = 
			new GenericType<List<Cohort>>() {};
	
	private final String cohortsUrl;

	public CohortsClient(String inCohortsUrl) {
		if (inCohortsUrl == null) {
			throw new IllegalArgumentException("inCohortsUrl cannot be null");
		}
		LOGGER.debug("Using cohorts URL {}", inCohortsUrl);
		this.cohortsUrl = inCohortsUrl;
	}

	@Override
	protected String getResourceUrl() {
		return this.cohortsUrl;
	}
	
	public List<Cohort> getAll(String username) throws ClientException {
		String path = UriBuilder
				.fromPath("/users/")
				.segment(username)
				.segment("cohorts")
				.build().toString();
		return doGet(path, CohortList);
	}
	
	public Cohort get(String username, Long id) throws ClientException {
		String path = UriBuilder
				.fromPath("/users/")
				.segment(username)
				.segment("cohorts/")
				.segment("{arg1}")
				.build(id).toString();
		return doGet(path, Cohort.class);
	}
	
	public void create(Cohort cohort) throws ClientException {
		String path = UriBuilder
				.fromPath("/users/")
				.segment(cohort.getUsername())
				.segment("cohorts")
				.build().toString();
		doPost(path, cohort);
	}
	
	public void update(Cohort cohort) throws ClientException {
		String path = UriBuilder
				.fromPath("/users/")
				.segment(cohort.getUsername())
				.segment("cohorts/")
				.segment("{arg1}")
				.build(cohort.getId()).toString();
		doPut(path, cohort);
	}
	
	public void delete(Cohort cohort) throws ClientException {
		String path = UriBuilder
				.fromPath("/users/")
				.segment(cohort.getUsername())
				.segment("cohorts/")
				.segment("{arg1}")
				.build(cohort.getId()).toString();
		doDelete(path);
	}
	
}
