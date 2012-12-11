/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.JobInfo;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.UserRequest;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FileUpload;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Job;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.RelationOperator;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Role;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TimeUnit;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.User;

/**
 * @author hrathod
 */
public class ServicesClient extends AbstractClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServicesClient.class);
	private static final GenericType<List<DataElement>> UserPropositionList = new GenericType<List<DataElement>>() {
	};
	private static final GenericType<List<TimeUnit>> TimeUnitList = new GenericType<List<TimeUnit>>() {
	};
	private static final GenericType<List<RelationOperator>> RelationOperatorList = new GenericType<List<RelationOperator>>() {
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
	private final String servicesUrl;

	public ServicesClient(String inServicesUrl) {
		super();
		this.servicesUrl = inServicesUrl;
	}

	@Override
	String getResourceUrl() {
		return this.servicesUrl;
	}

	public List<User> getUsers() {
		final String path = "/api/user/list";
		return this.getResource().path(path).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(UserList);
	}

	public User getUserByName(String username) {
		final String path = "/api/user/byname/" + username;
		return this.getResource().path(path).accept(MediaType.APPLICATION_JSON).get(User.class);
	}

	public User getUserById(Long inUserId) {
		final String path = "/api/user/byid/" + inUserId;
		return this.getResource().path(path).accept(MediaType.APPLICATION_JSON).get(User.class);
	}

	public void addUser(UserRequest inRequest) throws ClientException {
		final String path = "/api/user";
		ClientResponse response = this.getResource().path(path).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, inRequest);
		if (!response.getClientResponseStatus().equals(ClientResponse.Status.NO_CONTENT)) {
			throw new ClientException(response.getEntity(String.class));
		}
	}

	public void changePassword(Long inUserId, String inOldPass,
			String inNewPass) throws ClientException {
		final String path = "/api/user/passwd/" + inUserId;
		ClientResponse response = this.getResource().path(path).queryParam
				("oldPassword", inOldPass).queryParam("newPassword",
				inNewPass).put(ClientResponse.class);
		if (!response.getClientResponseStatus().equals(ClientResponse.Status.NO_CONTENT)) {
			throw new ClientException(response.getEntity(String.class));
		}
	}

	public void updateUser(User inUser) throws ClientException {
		final String path = "/api/user";
		ClientResponse response = this.getResource().path(path).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, inUser);
		if (response.getClientResponseStatus().equals(ClientResponse.Status.NOT_MODIFIED)) {
			throw new ClientException(response.getEntity(String.class));
		}
	}

	public void verifyUser(String inCode) throws ClientException {
		final String path = "/api/user/verify/" + inCode;
		ClientResponse response = this.getResource().path(path).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);
		if (!response.getClientResponseStatus().equals(ClientResponse.Status.NO_CONTENT)) {
			throw new ClientException(response.getEntity(String.class));
		}
	}

	public List<Role> getRoles() {
		final String path = "/api/role/list";
		return this.getResource().path(path).accept(MediaType.APPLICATION_JSON).get(RoleList);
	}

	public Role getRole(Long inRoleId) {
		final String path = "/api/role/" + inRoleId;
		return this.getResource().path(path).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(Role.class);
	}

	public void addJob(FileUpload inUpload) throws ClientException {
		final String path = "/api/job/add";
		ClientResponse response = this.getResource().type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, inUpload);
		if (!response.getClientResponseStatus().equals(ClientResponse.Status.OK)) {
			throw new ClientException("Job was not added successfully.");
		}
	}

	public List<Job> getJobsByUserId(Long inUserId) {
		final String path = "/api/job/list/" + inUserId;
		return this.getResource().path(path).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(JobList);
	}

	public JobInfo getJobInfo(Long inUserId) {
		final String path = "/api/job/status/" + inUserId;
		return this.getResource().path(path).type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(JobInfo.class);
	}

	public void saveUserElement(DataElement inDataElement)
			throws ClientException {
		final String path = "/api/dataelement";
		ClientResponse response = this.getResource().path(path).type(MediaType.APPLICATION_JSON).accept(
				MediaType.APPLICATION_JSON).post(
				ClientResponse.class, inDataElement);
		if (!response.getClientResponseStatus().equals(
				ClientResponse.Status.NO_CONTENT)) {
			String message = response.getEntity(String.class);
			LOGGER.error("Client error while saving element: {}", message);
			throw new ClientException(message);
		}
	}

	public void updateUserElement(DataElement inDataElement) throws
			ClientException {
		final String path = "/api/dataelement";
		ClientResponse response = this.getResource().path(path).type(MediaType.APPLICATION_JSON).accept(
				MediaType.APPLICATION_JSON).put(
				ClientResponse.class, inDataElement);
		if (!response.getClientResponseStatus().equals(
				ClientResponse.Status.NO_CONTENT)) {
			String message = response.getEntity(String.class);
			LOGGER.error("Client error while updating element: {}", message);
			throw new ClientException(message);
		}
	}

	public List<DataElement> getUserElements(Long inUserId) {
		if (inUserId == null) {
			throw new IllegalArgumentException("inUserId cannot be null");
		}
		final String path = "/api/dataelement/" + inUserId;
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(DataElementList);
	}

	public DataElement getUserElement(Long inUserId, String inKey) {
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
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(DataElement.class);
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
		ClientResponse response = this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		if (response.getClientResponseStatus().getStatusCode()
				!= ClientResponse.Status.NO_CONTENT.getStatusCode()) {
			throw new ClientException(
					"Element " + inKey + "could not be "
					+ "deleted for user " + inUserId);
		}
	}

	public List<SystemElement> getSystemElements(Long inUserId) {
		if (inUserId == null) {
			throw new IllegalArgumentException("inUserId cannot be null");
		}
		final String path = "/api/systemelement/" + inUserId;
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(SystemElementList);
	}

	public SystemElement getSystemElement(Long inUserId, String inKey) {
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
		String path = UriBuilder.fromPath("/api/systemelement/")
				.segment("{arg1}", inKey)
				.build(inUserId).toString();
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(SystemElement.class);
	}

	public List<TimeUnit> getTimeUnits() {
		final String path = "/api/timeunit/list";
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(TimeUnitList);
	}

	public TimeUnit getTimeUnit(Long inId) {
		final String path = "/api/timeunit/" + inId;
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(TimeUnit.class);
	}

	public List<RelationOperator> getRelationOperators() {
		final String path = "/api/relationop/list";
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(RelationOperatorList);
	}

	public RelationOperator getRelationOperator(Long inId) {
		final String path = "/api/relationop/" + inId;
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(RelationOperator.class);
	}

	public RelationOperator getRelationOperatorByName(String inName) {
		final String path = "/api/relationop/byname/" + inName;
		return this.getResource().path(path).accept(
				MediaType.APPLICATION_JSON).get(RelationOperator.class);
	}
}
