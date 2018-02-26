/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.webapp.comm.clients;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.eurekaclinical.eureka.client.comm.CohortDestination;
import org.eurekaclinical.eureka.client.comm.Destination;
import org.eurekaclinical.eureka.client.comm.DestinationType;
import org.eurekaclinical.eureka.client.comm.I2B2Destination;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.JobSpec;
import org.eurekaclinical.eureka.client.comm.Phenotype;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import org.eurekaclinical.eureka.client.comm.SourceConfigParams;
import org.eurekaclinical.eureka.client.comm.Statistics;
import org.eurekaclinical.eureka.client.comm.SystemPhenotype;
import edu.emory.cci.aiw.cvrg.eureka.webapp.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.webapp.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.webapp.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.webapp.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.webapp.entity.ValueComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.Response;
import org.eurekaclinical.common.comm.Role;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.standardapis.exception.HttpStatusException;

/**
 * @author hrathod
 */
public class ServicesClient extends EurekaClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesClient.class);
	private static final GenericType<List<TimeUnit>> TimeUnitList = new GenericType<List<TimeUnit>>() {
	};
	private static final GenericType<List<FrequencyType>> FrequencyTypeList = new GenericType<List<FrequencyType>>() {
	};
	private static final GenericType<List<RelationOperator>> RelationOperatorList = new GenericType<List<RelationOperator>>() {
	};
	private static final GenericType<List<ThresholdsOperator>> ThresholdsOperatorList = new GenericType<List<ThresholdsOperator>>() {
	};
	private static final GenericType<List<ValueComparator>> ValueComparatorList = new GenericType<List<ValueComparator>>() {
	};
	private static final GenericType<List<SystemPhenotype>> SystemPhenotypeList = new GenericType<List<SystemPhenotype>>() {
	};
	private static final GenericType<List<Phenotype>> PhenotypeList
			= new GenericType<List<Phenotype>>() {
	};
	private static final GenericType<List<Role>> RoleList = new GenericType<List<Role>>() {
	};
	private static final GenericType<List<Job>> JobList = new GenericType<List<Job>>() {
	};
	private static final GenericType<List<SourceConfig>> SourceConfigList = new GenericType<List<SourceConfig>>() {
	};
	private static final GenericType<List<SourceConfigParams>> SourceConfigParamsList = new GenericType<List<SourceConfigParams>>() {
	};
	private static final GenericType<List<Destination>> DestinationList = new GenericType<List<Destination>>() {
	};
	private static final GenericType<List<CohortDestination>> CohortDestinationListType
			= new GenericType<List<CohortDestination>>() {
	};
	private static final GenericType<List<I2B2Destination>> I2B2DestinationListType
			= new GenericType<List<I2B2Destination>>() {
	};
	private static final GenericType<List<String>> SystemPhenotypeSearchResultsList = new GenericType<List<String>>() {
	};


	private final URI servicesUrl;

	public ServicesClient(String inServicesUrl) {
		super();
		LOGGER.info("Using eureka-services URL {}", inServicesUrl);
		this.servicesUrl = URI.create(inServicesUrl);
	}

	@Override
	protected URI getResourceUrl() {
		return this.servicesUrl;
	}

	public void verifyUser(String inCode) throws ClientException {
		final String path = "/api/userrequests/verify/" + inCode;
		doPut(path);
	}

	public List<Role> getRoles() throws ClientException {
		final String path = "/api/protected/roles";
		return doGet(path, RoleList);
	}

	public Role getRole(Long inRoleId) throws ClientException {
		final String path = "/api/protected/roles/" + inRoleId;
		return doGet(path, Role.class);
	}
	
	public Role getRoleByName(String name) throws ClientException {
		return doGet("/api/protected/roles/byname/" + name, Role.class);
	}

	public URI submitJob(JobSpec inUpload) throws ClientException {
		final String path = "/api/protected/jobs";
		URI jobUrl = doPostCreate(path, inUpload);
		return jobUrl;
	}

	public Job getJob(Long jobId) throws ClientException {
		final String path = "/api/protected/jobs/" + jobId;
		return doGet(path, Job.class);
	}

	public Statistics getJobStats(Long jobId, String propId) throws ClientException {
		if (jobId == null) {
			throw new IllegalArgumentException("jobId cannot be null");
		}
		UriBuilder uriBuilder = UriBuilder.fromPath("/api/protected/jobs/{arg1}/stats/");
		if (propId != null) {
			uriBuilder = uriBuilder.segment(propId);
		}

		return doGet(uriBuilder.build(jobId).toString(), Statistics.class);
	}

	public List<Job> getJobs() throws ClientException {
		final String path = "/api/protected/jobs";
		return doGet(path, JobList);
	}

	public List<Job> getJobsDesc() throws ClientException {
		final String path = "/api/protected/jobs";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("order", "desc");
		return doGet(path, queryParams, JobList);
	}

	public List<Job> getLatestJob() throws ClientException {
		final String path = "/api/protected/jobs/latest";
		return doGet(path, JobList);
	}

	public List<Phenotype> getPhenotypes(String[] inKeys, boolean summarized) throws ClientException {
		List<Phenotype> result = new ArrayList<>();
		if (inKeys != null) {
			List<String> userPhenotypes = new ArrayList<>();
			List<String> systemPhenotypes = new ArrayList<>();
			for (String key : inKeys) {
				if (key.startsWith("USER:")) {
					userPhenotypes.add(key);
				} else {
					systemPhenotypes.add(key);
				}
			}
			if (!userPhenotypes.isEmpty()) {
				for (String userPhenotype : userPhenotypes) {
					result.add(getUserPhenotype(userPhenotype, summarized));
				}
			}
			if (!systemPhenotypes.isEmpty()) {
				result.addAll(getSystemPhenotypes(systemPhenotypes, summarized));
			}
		}
		return result;
	}

	public List<Phenotype> getUserPhenotypes(boolean summarized) throws ClientException {
		final String path = "/api/protected/phenotypes";
		if (summarized) {
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("summarize", "true");
			return doGet(path, queryParams, PhenotypeList);
		} else {
			return doGet(path, PhenotypeList);
		}
	}

	public Phenotype getUserPhenotype(String inKey, boolean summarized) throws ClientException {
		if (inKey == null) {
			throw new IllegalArgumentException("inKey cannot be null");
		}
		/*
		 * The inKey parameter may contain spaces, slashes and other 
		 * characters that are not allowed in URLs, so it needs to be
		 * encoded. We use UriBuilder to guarantee a valid URL. The inKey
		 * string can't be templated because the slashes won't be encoded!
		 */
		String path = UriBuilder
				.fromPath("/api/protected/phenotypes/")
				.segment(inKey)
				.build().toString();

		if (summarized) {
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			queryParams.add("summarize", "true");
			return doGet(path, queryParams, Phenotype.class);
		} else {
			return doGet(path, Phenotype.class);
		}
	}
        
	public URI saveUserPhenotype(Phenotype inPhenotype) throws ClientException {
		final String path = "/api/protected/phenotypes";
		URI phenotypeURI = doPostCreate(path, inPhenotype);
		return phenotypeURI;
	}         
        
	public void updateUserPhenotype(Long inId, Phenotype inPhenotype) throws
			ClientException {
		if (inId == null) {
			throw new IllegalArgumentException("inId cannot be null");
		}                
		final String path = "/api/protected/phenotypes/"+ inId;
		doPut(path, inPhenotype);
	}        

	public void deleteUserPhenotype(Long inId) throws
			ClientException {
		if (inId == null) {
			throw new IllegalArgumentException("inId cannot be null");
		}
		/*
		 * The inKey parameter may contain spaces, slashes and other 
		 * characters that are not allowed in URLs, so it needs to be
		 * encoded. We use UriBuilder to guarantee a valid URL. The inKey
		 * string can't be templated because the slashes won't be encoded!
		 */
		final String path = "/api/protected/phenotypes/"+ inId;
		doDelete(path);
	}

	public List<SystemPhenotype> getSystemPhenotypes() throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/concepts/").build().toString();
		return doGet(path, SystemPhenotypeList);
	}

	public List<SystemPhenotype> getSystemPhenotypes(List<String> inKeys, boolean summarize) throws ClientException {
		if (inKeys == null) {
			throw new IllegalArgumentException("inKeys cannot be null");
		}
		MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
		for (String key : inKeys) {
			formParams.add("key", key);
		}
		formParams.add("summarize", Boolean.toString(summarize));
		String path = UriBuilder.fromPath("/api/protected/concepts/")
				.build().toString();
		return doPost(path, formParams, SystemPhenotypeList);
	}

	public SystemPhenotype getSystemPhenotype(String inKey, boolean summarize) throws ClientException {
		List<SystemPhenotype> result = getSystemPhenotypes(Collections.singletonList(inKey), summarize);
		if (result.isEmpty()) {
			throw new HttpStatusException(Response.Status.NOT_FOUND);
		} else {
			return result.get(0);
		}
	}

	public List<TimeUnit> getTimeUnitsAsc() throws ClientException {
		final String path = "/api/protected/timeunits";
		return doGet(path, TimeUnitList);
	}

	public TimeUnit getTimeUnit(Long inId) throws ClientException {
		final String path = "/api/protected/timeunits/" + inId;
		return doGet(path, TimeUnit.class);
	}

	public TimeUnit getTimeUnitByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/timeunits/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, TimeUnit.class);
	}

	public List<RelationOperator> getRelationOperatorsAsc() throws ClientException {
		final String path = "/api/protected/relationops";
		return doGet(path, RelationOperatorList);
	}

	public RelationOperator getRelationOperator(Long inId) throws ClientException {
		final String path = "/api/protected/relationops/" + inId;
		return doGet(path, RelationOperator.class);
	}

	public RelationOperator getRelationOperatorByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/relationops/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, RelationOperator.class);
	}

	public List<ThresholdsOperator> getThresholdsOperators() throws ClientException {
		final String path = "/api/protected/thresholdsops/";
		return doGet(path, ThresholdsOperatorList);
	}

	public ThresholdsOperator getThresholdsOperator(Long inId) throws ClientException {
		final String path = "/api/protected/thresholdsops/" + inId;
		return doGet(path, ThresholdsOperator.class);
	}

	public ThresholdsOperator getThresholdsOperatorByName(
			String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/thresholdsops/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, ThresholdsOperator.class);
	}

	public List<ValueComparator> getValueComparatorsAsc() throws ClientException {
		final String path = "/api/protected/valuecomps";
		return doGet(path, ValueComparatorList);
	}

	public ValueComparator getValueComparator(Long inId) throws ClientException {
		final String path = "/api/protected/valuecomps/" + inId;
		return doGet(path, ValueComparator.class);
	}

	public ValueComparator getValueComparatorByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/valuecomps/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, ValueComparator.class);
	}

	public List<FrequencyType> getFrequencyTypesAsc() throws ClientException {
		final String path = "/api/protected/frequencytypes";
		return doGet(path, FrequencyTypeList);
	}

	public List<SourceConfig> getSourceConfigs() throws ClientException {
		String path = "/api/protected/sourceconfig";
		return doGet(path, SourceConfigList);
	}

	public SourceConfig getSourceConfig(String sourceConfigId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/sourceconfig/")
				.segment(sourceConfigId)
				.build().toString();
		return doGet(path, SourceConfig.class);
	}

	public List<SourceConfigParams> getSourceConfigParams() throws ClientException {
		String path = "/api/protected/sourceconfig/parameters/list";
		return doGet(path, SourceConfigParamsList);
	}

	public URI createDestination(Destination destination) throws ClientException {
		String path = "/api/protected/destinations";
                URI destURI = doPostCreate(path, destination);
                return destURI;
	}

	public void updateDestination(Destination destination) throws ClientException {
		String path = "/api/protected/destinations";
		doPut(path, destination);
	}

	public List<Destination> getDestinations() throws ClientException {
		String path = "/api/protected/destinations";
		return doGet(path, DestinationList);
	}

	public List<CohortDestination> getCohortDestinations() throws
			ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.COHORT.name());
		return doGet(path, queryParams, CohortDestinationListType);
	}

	public List<I2B2Destination> getI2B2Destinations() throws
			ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.I2B2.name());
		return doGet(path, queryParams, I2B2DestinationListType);
	}

	public Destination getDestination(String destinationId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/destinations/")
				.segment(destinationId)
				.build().toString();
		return doGet(path, Destination.class);
	}

	public void deleteDestination(String destinationId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/destinations/")
				.segment(destinationId)
				.build().toString();
		doDelete(path);
	}

	//Search Functionality
	public List<String> getSystemPhenotypeSearchResults(String searchKey) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/concepts/search/")
				.segment(searchKey)
				.build().toString();
		return doGet(path, SystemPhenotypeSearchResultsList);
	}

	//Search Functionality
	public List<SystemPhenotype> getSystemPhenotypeSearchResultsBySearchKey(String searchKey) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/concepts/propsearch/")
				.segment(searchKey)
				.build().toString();
		return doGet(path, SystemPhenotypeList);
	}
	
}
