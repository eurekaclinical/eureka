/*
 * #%L
 * Eureka Protempa ETL
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
package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ConfDao;
import edu.emory.cci.aiw.i2b2etl.configuration.ConfigurationReadException;
import edu.emory.cci.aiw.i2b2etl.configuration.ConfigurationReader;
import edu.emory.cci.aiw.i2b2etl.configuration.DatabaseSection;

/**
 * @author hrathod
 */
@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PingResource {

	private final ConfDao confDao;
	private final EtlProperties etlProperties;

	@Inject
	public PingResource(ConfDao inConfDao, EtlProperties inEtlProperties) {
		this.confDao = inConfDao;
		this.etlProperties = inEtlProperties;
	}

	@GET
	@Path("/{id}")
	public String ping(@PathParam("id") Long inUserId) {
		String response = "Ping successful";
		Configuration configuration = this.confDao.getByUserId(inUserId);
		if (configuration == null) {
			throw new HttpStatusException(
					Response.Status.NOT_FOUND,
					"No configuration found for user " + inUserId);
		} else {
			try {
				testProtempaDatabase(configuration);
			} catch (SQLException e) {
				throw new HttpStatusException(
						Response.Status.PRECONDITION_FAILED,
						"Error connecting to Protempa " +
								"database for user " + inUserId);
			}
			try {
				testI2b2Database(configuration);
			} catch (SQLException e) {
				throw new HttpStatusException(
						Response.Status.PRECONDITION_FAILED,
						"Error connecting to i2b2 database for user " + inUserId);
			} catch (ConfigurationReadException e) {
				throw new HttpStatusException(
						Response.Status.PRECONDITION_FAILED,
						"Error reading i2b2 configuration for user " + inUserId);
			}
		}
		return response;
	}

	private void testProtempaDatabase(Configuration inConfiguration)
			throws SQLException {
		String url = inConfiguration.getProtempaJdbcUrl();
		Connection connection = DriverManager.getConnection(
				url, inConfiguration.getProtempaSchema(),
				inConfiguration.getProtempaPassword());
		connection.close();
	}

	private void testI2b2Database(Configuration inConfiguration)
			throws ConfigurationReadException, SQLException {
		File configDirectory = new File(
				this.etlProperties.getConfigDir(), "etlconfig");
		File confFile = new File(
				configDirectory, "config" + inConfiguration.getId() + ".xml");
		ConfigurationReader configurationReader = new ConfigurationReader(
				confFile);
		configurationReader.read();
		DatabaseSection databaseSection = configurationReader
				.getDatabaseSection();

		DatabaseSection.DatabaseSpec dataschema = databaseSection
				.get("dataschema");
		Connection dataConnection = DriverManager.getConnection(
				dataschema.connect, dataschema.user, dataschema.passwd);
		dataConnection.close();

		DatabaseSection.DatabaseSpec metaschema = databaseSection
				.get("metaschema");
		Connection metaConnection = DriverManager.getConnection(
				metaschema.connect, metaschema.user, metaschema.passwd);
		metaConnection.close();
	}
}
