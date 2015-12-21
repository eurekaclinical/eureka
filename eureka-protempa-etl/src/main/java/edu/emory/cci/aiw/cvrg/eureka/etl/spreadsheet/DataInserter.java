/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet;

import java.sql.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Inserts data into a Protempa database.
 *
 * @author hrathod
 */
public class DataInserter {

	private static int BATCH_SIZE = 128;
	/**
	 * Class level logger.
	 */
	private static final Logger LOGGER =
			LoggerFactory.getLogger(DataInserter.class);
	private final Connection connection;

	/**
	 * Build a new object with the given configuration.
	 *
	 * @param connection The database connection information.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public DataInserter(Connection connection) throws
			DataInserterException {
		if (connection == null) {
			throw new IllegalArgumentException("connection cannot be null");
		}
		this.connection = connection;
	}

	private abstract class DatabaseExecutor {

		private final String statement;
		private PreparedStatement preparedStatement;

		DatabaseExecutor(String statement) {
			this.statement = statement;
		}
		private int counter = 0;

		void execute() throws DataInserterException {
			try {
				this.preparedStatement = connection.prepareStatement(
						this.statement);
				try {
					doExecute(preparedStatement);
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
					preparedStatement.close();
				} finally {
					if (preparedStatement != null) {
						try {
							preparedStatement.close();
						} catch (SQLException ignore) {
						}
					}
				}
			} catch (SQLException ex) {
				LOGGER.error(ex.getMessage(), ex);
				throw new DataInserterException(ex);
			}
		}

		void addBatch() throws SQLException {
			preparedStatement.addBatch();
			counter++;
			if (counter >= BATCH_SIZE) {
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
				counter = 0;
			}
		}

		abstract void doExecute(PreparedStatement preparedStatement) throws SQLException;
	}

	/**
	 * Insert a list of patients to the data base using the given connection.
	 *
	 * @param patients The list of patients to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertPatients(final List<Patient> patients) throws
			DataInserterException {
		new DatabaseExecutor("insert into " + "patient values (?,?,?,?,?,?,?,?)") {
			@Override
			void doExecute(PreparedStatement preparedStatement) throws SQLException {
				for (Patient patient : patients) {
					Date dateOfBirth = null;
					if (patient.getDateOfBirth() != null) {
						dateOfBirth = new Date(
								patient.getDateOfBirth().getTime());
					}
					preparedStatement.setLong(1, patient.getId().longValue());
					preparedStatement.setString(2, patient.getFirstName());
					preparedStatement.setString(3, patient.getLastName());
					preparedStatement.setDate(4, dateOfBirth);
					preparedStatement.setString(5, patient.getLanguage());
					preparedStatement.setString(6, patient.getMaritalStatus());
					preparedStatement.setString(7, patient.getRace());
					preparedStatement.setString(8, patient.getGender());

					addBatch();
				}
			}
		}.execute();
	}

	/**
	 * Insert the given list of encounters to a target database using the given
	 * connection.
	 *
	 * @param encounters The list of encounters to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertEncounters(final List<Encounter> encounters) throws
			DataInserterException {
		new DatabaseExecutor("insert into " + "encounter values (?,?,?,?,?,?,?)") {
			@Override
			void doExecute(PreparedStatement preparedStatement) throws SQLException {
				for (Encounter encounter : encounters) {
					preparedStatement.setLong(1, encounter.getId().longValue());
					preparedStatement.setLong(
							2, encounter.getPatientId().longValue());
					preparedStatement.setLong(
							3, encounter.getProviderId().longValue());
					preparedStatement.setTimestamp(
							4, new Timestamp(
							encounter.
							getStart().
							getTime()));
					preparedStatement.setTimestamp(
							5, new Timestamp(
							encounter.getEnd().
							getTime()));
					preparedStatement.setString(6, encounter.getType());
					preparedStatement.setString(
							7, encounter.getDischargeDisposition());

					addBatch();
				}
			}
		}.execute();
	}

	/**
	 * Insert the given list of providers to a target database using the given
	 * connection.
	 *
	 * @param providers The list of providers to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertProviders(final List<Provider> providers) throws
			DataInserterException {
		new DatabaseExecutor("insert into " + "provider values (?,?,?)") {
			@Override
			void doExecute(PreparedStatement preparedStatement) throws SQLException {
				for (Provider provider : providers) {
					preparedStatement.setLong(1, provider.getId().longValue());
					preparedStatement.setString(2, provider.getFirstName());
					preparedStatement.setString(3, provider.getLastName());

					addBatch();
				}
			}
		}.execute();
	}

	/**
	 * Insert the given list of CPT codes to a target database using the given
	 * connection.
	 *
	 * @param cptCodes The list of CPT codes to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertCptCodes(List<CPT> cptCodes) throws
			DataInserterException {
		this.insertObservations(cptCodes, "cpt_event");
	}

	/**
	 * Insert the given list of ICD9 diagnosis codes to a target database using
	 * the given connection.
	 *
	 * @param diagnoses The list of diagnosis codes to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertIcd9Diagnoses(List<Icd9Diagnosis> diagnoses) throws
			DataInserterException {
		this.insertDiagnoses(diagnoses);
	}

	/**
	 * Insert the given list of ICD9 procedure codes to a target database using
	 * the given connection.
	 *
	 * @param procedures The list of procedure codes to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertIcd9Procedures(List<Icd9Procedure> procedures) throws
			DataInserterException {
		this.insertObservations(procedures, "icd9p_event");
	}

	/**
	 * Insert the given list of medications to a target database using the given
	 * connection.
	 *
	 * @param medications The list of medications to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertMedications(List<Medication> medications) throws
			DataInserterException {
		this.insertObservations(medications, "meds_event");
	}

	/**
	 * Insert the given list of lab results to a target database using the given
	 * connection.
	 *
	 * @param labs The list of lab results to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertLabs(List<Lab> labs) throws DataInserterException {
		this.insertObservationsWithResult(labs, "labs_event");
	}

	/**
	 * Insert the given list of vital signs to a target database using the given
	 * connection.
	 *
	 * @param vitals The list of vitals to insert.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	public void insertVitals(List<Vital> vitals) throws
			DataInserterException {
		this.insertObservationsWithResult(vitals, "vitals_event");
	}

	/**
	 * Add the given list of observation objects to a target database using the
	 * given connection.
	 *
	 * @param observations The list of observations to insert.
	 * @param table The table in which the observations should be inserted.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	private void insertObservations(final List<? extends Observation> observations, String table) throws DataInserterException {
		new DatabaseExecutor("insert into " + table + " values (?,?,?,?)") {
			@Override
			void doExecute(PreparedStatement preparedStatement) throws SQLException {
				for (Observation observation : observations) {
					preparedStatement.setString(1, observation.getId());
					preparedStatement.setLong(
							2, observation.getEncounterId().
							longValue());
					preparedStatement.setTimestamp(
							3, new Timestamp(
							observation.
							getTimestamp().getTime()));
					preparedStatement.setString(4, observation.getEntityId());

					addBatch();
				}
			}
		}.execute();
	}
	
	private void insertDiagnoses(final List<Icd9Diagnosis> observations) throws DataInserterException {
		new DatabaseExecutor("insert into icd9d_event values (?,?,?,?,?)") {
			@Override
			void doExecute(PreparedStatement preparedStatement) throws SQLException {
				for (Icd9Diagnosis observation : observations) {
					preparedStatement.setString(1, observation.getId());
					preparedStatement.setLong(
							2, observation.getEncounterId().
							longValue());
					preparedStatement.setTimestamp(
							3, new Timestamp(
							observation.
							getTimestamp().getTime()));
					preparedStatement.setString(4, observation.getEntityId());
					preparedStatement.setInt(5, observation.getRank());
					
					addBatch();
				}
			}
		}.execute();
	}

	/**
	 * Insert a list of observations and their related results to a target
	 * database using the given connection.
	 *
	 * @param observations The observations to insert.
	 * @param table The table in which the observations should be inserted.
	 * @throws DataInserterException Thrown if there are any JDBC errors.
	 */
	private void insertObservationsWithResult(final List<? extends ObservationWithResult> observations, String table) throws
			DataInserterException {
		new DatabaseExecutor("insert into " + table + " values (?,?,?,?,?,?,?,?)") {
			@Override
			void doExecute(PreparedStatement preparedStatement) throws SQLException {
				for (ObservationWithResult observation : observations) {
					preparedStatement.setString(1, observation.getId());
					preparedStatement.setLong(
							2, observation.getEncounterId().
							longValue());
					preparedStatement.setTimestamp(
							3, new Timestamp(
							observation.
							getTimestamp().getTime()));
					preparedStatement.setString(4, observation.getEntityId());
					preparedStatement.setString(5, observation.getResultAsStr());
					preparedStatement.setDouble(
							6, observation.getResultAsNum().
							doubleValue());
					preparedStatement.setString(7, observation.getUnits());
					preparedStatement.setString(8, observation.getFlag());

					addBatch();
				}
			}
		}.execute();
	}
}
