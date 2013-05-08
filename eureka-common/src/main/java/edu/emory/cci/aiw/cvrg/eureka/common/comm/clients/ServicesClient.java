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

import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobSpec;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.PasswordChangeRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfig;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SourceConfigParams;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyType;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueComparator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ThresholdsOperator;
import java.io.InputStream;
import java.net.URI;
import javax.ws.rs.core.MultivaluedMap;

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
	private static final GenericType<List<UserInfo>> UserList = new GenericType<List<UserInfo>>() {
	};
	private static final GenericType<List<SourceConfig>> SourceConfigList = new GenericType<List<SourceConfig>>() {
	};
	private static final GenericType<List<SourceConfigParams>> SourceConfigParamsList = new GenericType<List<SourceConfigParams>>() {
	};
	private static final GenericType<List<Destination>> DestinationList = new GenericType<List<Destination>>() {
	};
	private final String servicesUrl;

	public ServicesClient(String inServicesUrl) {
		super();
		this.servicesUrl = inServicesUrl;
	}

	@Override
	protected String getResourceUrl() {
		return this.servicesUrl;
	}

	public List<UserInfo> getUsers() throws ClientException {
		final String path = "/api/user/list";
		return doGet(path, UserList);
	}

	public UserInfo getUserByName(String username) throws ClientException {
		String path = UriBuilder.fromPath("/api/user/byname/")
				.segment("{arg1}")
				.build(username)
				.toString();
		return doGet(path, UserInfo.class);
	}

	public UserInfo getUserById(Long inUserId) throws ClientException {
		final String path = "/api/user/byid/" + inUserId;
		return doGet(path, UserInfo.class);
	}

	public void addUser(UserRequest inRequest) throws ClientException {
		final String path = "/api/user";
		doPost(path, inRequest);
	}

	public void changePassword(String inOldPass, String inNewPass) throws ClientException {
		final String path = "/api/user/passwordchangerequest";
		PasswordChangeRequest passwordChangeRequest =
				new PasswordChangeRequest();
		passwordChangeRequest.setOldPassword(inOldPass);
		passwordChangeRequest.setNewPassword(inNewPass);
		doPost(path, passwordChangeRequest);
	}

	public void resetPassword(String email) throws ClientException {
		final String path = "/api/user/pwreset/" + email;
		doPut(path);
	}

	public void updateUser(UserInfo inUser) throws ClientException {
		final String path = "/api/user";
		doPut(path, inUser);
	}

	public void verifyUser(String inCode) throws ClientException {
		final String path = "/api/user/verify/" + inCode;
		doPut(path);
	}

	public List<Role> getRoles() throws ClientException {
		final String path = "/api/role/list";
		return doGet(path, RoleList);
	}

	public Role getRole(Long inRoleId) throws ClientException {
		final String path = "/api/role/" + inRoleId;
		return doGet(path, Role.class);
	}

	public Long submitJob(JobSpec inUpload) throws ClientException {
		final String path = "/api/jobs";
		URI jobUrl = doPostCreate(path, inUpload);
		return extractId(jobUrl);
	}

	public void upload(String fileName, String sourceId,
			String fileTypeId, InputStream inputStream)
			throws ClientException {
		String path = UriBuilder
				.fromPath("/api/file/upload/")
				.segment(sourceId)
				.segment(fileTypeId)
				.build().toString();
		doPostMultipart(path, fileName, inputStream);
	}
	
	public Job getJob(Long jobId) throws ClientException {
		final String path = "/api/jobs/" + jobId;
		return doGet(path, Job.class);
	}

	public List<Job> getJobs() throws ClientException {
		final String path = "/api/jobs";
		return doGet(path, JobList);
	}
	
	public List<Job> getJobsDesc() throws ClientException {
		final String path = "/api/jobs";
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("order", "desc");
		return doGet(path, JobList, queryParams);
	}

	public void saveUserElement(DataElement inDataElement)
			throws ClientException {
		final String path = "/api/dataelement";
		doPost(path, inDataElement);
	}

	public void updateUserElement(DataElement inDataElement) throws
			ClientException {
		final String path = "/api/dataelement";
		doPut(path, inDataElement);
	}

	public List<DataElement> getUserElements() throws ClientException {
		final String path = "/api/dataelement/";
		return doGet(path, DataElementList);
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
				.fromPath("/api/dataelement/")
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
				.fromPath("/api/dataelement/")
				.segment("{arg1}")
				.segment(inKey)
				.build(inUserId).toString();
		doDelete(path);
	}
	
	public List<SystemElement> getSystemElements() throws ClientException {
		final String path = UriBuilder.fromPath("/api/systemelement/").build().toString();
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
		String path = UriBuilder.fromPath("/api/systemelement/")
				.segment(inKey)
				.build().toString();
		return doGet(path, SystemElement.class);
	}

	public List<TimeUnit> getTimeUnits() throws ClientException {
		final String path = "/api/timeunit/list";
		return doGet(path, TimeUnitList);
	}

	public List<TimeUnit> getTimeUnitsAsc() throws ClientException {
		final String path = "/api/timeunit/listasc";
		return doGet(path, TimeUnitList);
	}

	public TimeUnit getTimeUnit(Long inId) throws ClientException {
		final String path = "/api/timeunit/" + inId;
		return doGet(path, TimeUnit.class);
	}

	public TimeUnit getTimeUnitByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/timeunit/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, TimeUnit.class);
	}

	public TimeUnit getDefaultTimeUnit() throws ClientException {
		String path = "/api/timeunit/default";
		return doGet(path, TimeUnit.class);
	}

	public List<RelationOperator> getRelationOperators() throws ClientException {
		final String path = "/api/relationop/list";
		return doGet(path, RelationOperatorList);
	}

	public List<RelationOperator> getRelationOperatorsAsc() throws ClientException {
		final String path = "/api/relationop/listasc";
		return doGet(path, RelationOperatorList);
	}

	public RelationOperator getRelationOperator(Long inId) throws ClientException {
		final String path = "/api/relationop/" + inId;
		return doGet(path, RelationOperator.class);
	}

	public RelationOperator getRelationOperatorByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/relationop/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, RelationOperator.class);
	}

	public RelationOperator getDefaultRelationOperator() throws ClientException {
		String path = "/api/relationop/default";
		return doGet(path, RelationOperator.class);
	}

	public List<ThresholdsOperator> getThresholdsOperators() throws ClientException {
		final String path = "/api/thresholdsop/list";
		return doGet(path, ThresholdsOperatorList);
	}

	public ThresholdsOperator getThresholdsOperator(Long inId) throws ClientException {
		final String path = "/api/thresholdsop/" + inId;
		return doGet(path, ThresholdsOperator.class);
	}

	public ThresholdsOperator getThresholdsOperatorByName(
			String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/thresholdsop/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, ThresholdsOperator.class);
	}

	public List<ValueComparator> getValueComparators() throws ClientException {
		final String path = "/api/valuecomps/list";
		return doGet(path, ValueComparatorList);
	}

	public List<ValueComparator> getValueComparatorsAsc() throws ClientException {
		final String path = "/api/valuecomps/listasc";
		return doGet(path, ValueComparatorList);
	}

	public ValueComparator getValueComparator(Long inId) throws ClientException {
		final String path = "/api/valuecomps/" + inId;
		return doGet(path, ValueComparator.class);
	}

	public ValueComparator getValueComparatorByName(String inName) throws ClientException {
		final String path = UriBuilder.fromPath("/api/valuecomps/byname/")
				.segment(inName)
				.build().toString();
		return doGet(path, ValueComparator.class);
	}

	public void pingAccount(Long inUserId) throws ClientException {
		final String path = "/api/ping/testuser/" + inUserId;
		doGet(path, ClientResponse.class);
	}

	public List<FrequencyType> getFrequencyTypesAsc() throws ClientException {
		final String path = "/api/frequencytype/listasc";
		return doGet(path, FrequencyTypeList);
	}

	public FrequencyType getDefaultFrequencyType() throws ClientException {
		String path = "/api/frequencytype/default";
		return doGet(path, FrequencyType.class);
	}

	public List<SourceConfig> getSourceConfigs() throws ClientException {
		String path = "/api/sourceconfig/list";
		return doGet(path, SourceConfigList);
	}

	public List<SourceConfigParams> getSourceConfigParams() throws ClientException {
		String path = "/api/sourceconfig/parameters/list";
		return doGet(path, SourceConfigParamsList);
	}

	public List<Destination> getDestinations() throws ClientException {
		String path = "/api/destination/list";
		return doGet(path, DestinationList);
	}
	
	public Destination getDestination(String destinationId) throws ClientException {
		String path = UriBuilder.fromPath("/api/destination/")
				.segment(destinationId)
				.build().toString();
		return doGet(path, Destination.class);
	}
	
}
