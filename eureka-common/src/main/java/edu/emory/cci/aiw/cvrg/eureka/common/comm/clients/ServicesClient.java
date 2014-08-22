/*
 * #%L
 * Eureka Common
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
package edu.emory.cci.aiw.cvrg.eureka.common.comm.clients;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.CohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.DestinationType;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.I2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PasswordChangeRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigParams;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.User;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.OAuthProvider;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final GenericType<List<SystemElement>> SystemElementList = new GenericType<List<SystemElement>>() {
	};
	private static final GenericType<List<DataElement>> DataElementList =
			new GenericType<List<DataElement>>() {
			};
	private static final GenericType<List<Role>> RoleList = new GenericType<List<Role>>() {
	};
	private static final GenericType<List<Job>> JobList = new GenericType<List<Job>>() {
	};
	private static final GenericType<List<User>> UserList = new GenericType<List<User>>() {
	};
	private static final GenericType<List<SourceConfig>> SourceConfigList = new GenericType<List<SourceConfig>>() {
	};
	private static final GenericType<List<SourceConfigParams>> SourceConfigParamsList = new GenericType<List<SourceConfigParams>>() {
	};
	private static final GenericType<List<Destination>> DestinationList = new GenericType<List<Destination>>() {
	};
	private static final GenericType<List<CohortDestination>> CohortDestinationListType =
			new GenericType<List<CohortDestination>>() {
			};
	private static final GenericType<List<I2B2Destination>> I2B2DestinationListType =
			new GenericType<List<I2B2Destination>>() {
			};
	private static final GenericType<List<String>> SystemElementSearchResultsList = new GenericType<List<String>>() {
	};
	private final String servicesUrl;

	public ServicesClient(String inServicesUrl) {
		super();
		LOGGER.debug("Using services URL {}", inServicesUrl);
		this.servicesUrl = inServicesUrl;
	}

	@Override
	protected String getResourceUrl() {
		return this.servicesUrl;
	}

	public List<User> getUsers() throws ClientException {
		final String path = "/api/protected/users";
		return doGet(path, UserList);
	}

	public User getMe() throws ClientException {
		String path = "/api/protected/users/me";
		return doGet(path, User.class);
	}

	public User getUserById(Long inUserId) throws ClientException {
		final String path = "/api/protected/users/byid/" + inUserId;
		return doGet(path, User.class);
	}

	public void addUser(UserRequest inRequest) throws ClientException {
		final String path = "/api/userrequest/new";
		doPost(path, inRequest);
	}
	
	public void resetPassword(String username) throws ClientException {
		final String path = "/api/passwordresetrequest/" + username;
		doPost(path);
	}
	
	public void verifyUser(String inCode) throws ClientException {
		final String path = "/api/userrequest/verify/" + inCode;
		doPut(path);
	}

	public void changePassword(String inOldPass, String inNewPass) throws ClientException {
		final String path = "/api/protected/users/passwordchangerequest";
		PasswordChangeRequest passwordChangeRequest =
				new PasswordChangeRequest();
		passwordChangeRequest.setOldPassword(inOldPass);
		passwordChangeRequest.setNewPassword(inNewPass);
		doPost(path, passwordChangeRequest);
	}

	public void updateUser(User inUser) throws ClientException {
		final String path = "/api/protected/users";
		doPut(path, inUser);
	}

	public List<Role> getRoles() throws ClientException {
		final String path = "/api/protected/role/list";
		return doGet(path, RoleList);
	}

	public Role getRole(Long inRoleId) throws ClientException {
		final String path = "/api/protected/role/" + inRoleId;
		return doGet(path, Role.class);
	}

	public Long submitJob(JobSpec inUpload) throws ClientException {
		final String path = "/api/protected/jobs";
		URI jobUrl = doPostCreate(path, inUpload);
		return extractId(jobUrl);
	}

	public void upload(String fileName, String sourceId,
			String fileTypeId, InputStream inputStream)
			throws ClientException {
		String path = UriBuilder
				.fromPath("/api/protected/file/upload/")
				.segment(sourceId)
				.segment(fileTypeId)
				.build().toString();
		doPostMultipart(path, fileName, inputStream);
	}
	
	public Job getJob(Long jobId) throws ClientException {
		final String path = "/api/protected/jobs/" + jobId;
		return doGet(path, Job.class);
	}

	public List<Job> getJobs() throws ClientException {
		final String path = "/api/protected/jobs";
		return doGet(path, JobList);
	}
	
	public List<Job> getJobsDesc() throws ClientException {
		final String path = "/api/protected/jobs";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("order", "desc");
		return doGet(path, JobList, queryParams);
	}

	public void saveUserElement(DataElement inDataElement)
			throws ClientException {
		final String path = "/api/protected/dataelement";
		doPost(path, inDataElement);
	}

	public void updateUserElement(DataElement inDataElement) throws
			ClientException {
		final String path = "/api/protected/dataelement";
		doPut(path, inDataElement);
	}

	public List<DataElement> getUserElements() throws ClientException {
		final String path = "/api/protected/dataelement/";
		return doGet(path, DataElementList);
	}

	public List<DataElement> getSummarizedUserElements() throws ClientException {
		final String path = "/api/protected/dataelement/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("summarize", "true");
		return doGet(path, DataElementList, queryParams);
	}

	public DataElement getUserElement(String inKey) throws ClientException {
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
				.fromPath("/api/protected/dataelement/")
				.segment(inKey)
				.build().toString();
		return doGet(path, DataElement.class);
	}

	public void deleteUserElement(Long inUserId, String inKey) throws
			ClientException {
		if (inUserId == null) {
			throw new IllegalArgumentException("inUserId cannot be null");
		}
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
				.fromPath("/api/protected/dataelement/")
				.segment("{arg1}")
				.segment(inKey)
				.build(inUserId).toString();
		doDelete(path);
	}
	
	public List<SystemElement> getSystemElements() throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/systemelement/").build().toString();
		return doGet(path, SystemElementList);
	}

	public SystemElement getSystemElement(String inKey) throws ClientException {
		if (inKey == null) {
			throw new IllegalArgumentException("inKey cannot be null");
		}
		/*
		 * The inKey parameter may contain spaces, slashes and other 
		 * characters that are not allowed in URLs, so it needs to be
		 * encoded. We use UriBuilder to guarantee a valid URL. The inKey
		 * string can't be templated because the slashes won't be encoded!
		 */
		String path = UriBuilder.fromPath("/api/protected/systemelement/")
				.segment(inKey)
				.build().toString();
		return doGet(path, SystemElement.class);
	}

	public List<TimeUnit> getTimeUnits() throws ClientException {
		final String path = "/api/protected/timeunit/list";
		return doGet(path, TimeUnitList);
	}

	public List<TimeUnit> getTimeUnitsAsc() throws ClientException {
		final String path = "/api/protected/timeunit/listasc";
		return doGet(path, TimeUnitList);
	}

	public TimeUnit getTimeUnit(Long inId) throws ClientException {
		final String path = "/api/protected/timeunit/" + inId;
		return doGet(path, TimeUnit.class);
	}

	public TimeUnit getTimeUnitByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/timeunit/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, TimeUnit.class);
	}

	public TimeUnit getDefaultTimeUnit() throws ClientException {
		String path = "/api/protected/timeunit/default";
		return doGet(path, TimeUnit.class);
	}

	public List<RelationOperator> getRelationOperators() throws ClientException {
		final String path = "/api/protected/relationop/list";
		return doGet(path, RelationOperatorList);
	}

	public List<RelationOperator> getRelationOperatorsAsc() throws ClientException {
		final String path = "/api/protected/relationop/listasc";
		return doGet(path, RelationOperatorList);
	}

	public RelationOperator getRelationOperator(Long inId) throws ClientException {
		final String path = "/api/protected/relationop/" + inId;
		return doGet(path, RelationOperator.class);
	}

	public RelationOperator getRelationOperatorByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/relationop/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, RelationOperator.class);
	}
	
	public OAuthProvider getOAuthProvider(Long inId) throws ClientException {
		final String path = "/api/protected/oauthprovider/" + inId;
		return doGet(path, OAuthProvider.class);
	}

	public OAuthProvider getOAuthProviderByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/oauthprovider/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, OAuthProvider.class);
	}

	public RelationOperator getDefaultRelationOperator() throws ClientException {
		String path = "/api/protected/relationop/default";
		return doGet(path, RelationOperator.class);
	}

	public List<ThresholdsOperator> getThresholdsOperators() throws ClientException {
		final String path = "/api/protected/thresholdsop/list";
		return doGet(path, ThresholdsOperatorList);
	}

	public ThresholdsOperator getThresholdsOperator(Long inId) throws ClientException {
		final String path = "/api/protected/thresholdsop/" + inId;
		return doGet(path, ThresholdsOperator.class);
	}

	public ThresholdsOperator getThresholdsOperatorByName(
			String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/thresholdsop/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, ThresholdsOperator.class);
	}

	public List<ValueComparator> getValueComparators() throws ClientException {
		final String path = "/api/protected/valuecomps/list";
		return doGet(path, ValueComparatorList);
	}

	public List<ValueComparator> getValueComparatorsAsc() throws ClientException {
		final String path = "/api/protected/valuecomps/listasc";
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
		final String path = "/api/protected/frequencytype/listasc";
		return doGet(path, FrequencyTypeList);
	}

	public FrequencyType getDefaultFrequencyType() throws ClientException {
		String path = "/api/protected/frequencytype/default";
		return doGet(path, FrequencyType.class);
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
	
	public void createDestination(Destination destination) throws ClientException {
		String path = "/api/protected/destinations";
		doPost(path, destination);
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
		return doGet(path, CohortDestinationListType, queryParams);
	}
	
	public List<I2B2Destination> getI2B2Destinations() throws
			ClientException {
		final String path = "/api/protected/destinations/";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", DestinationType.I2B2.name());
		return doGet(path, I2B2DestinationListType, queryParams);
	}
	
	public Destination getDestination(String destinationId) throws ClientException {
		String path = UriBuilder.fromPath("/api/protected/destinations/")
				.segment(destinationId)
				.build().toString();
		return doGet(path, Destination.class);
	}
	
	//Search Functionality
	public List<String> getSystemElementSearchResults(String searchKey) throws ClientException {
		final String path = UriBuilder.fromPath("/api/protected/systemelement/search/")
			.segment(searchKey)
			.build().toString();
		return doGet(path, SystemElementSearchResultsList);
	}
	
}
