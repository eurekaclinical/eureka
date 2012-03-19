package edu.emory.cci.aiw.cvrg.eureka.services.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.entity.Configuration;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.CPT;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Encounter;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Icd9Diagnosis;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Icd9Procedure;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Lab;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Medication;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Observation;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.ObservationWithResult;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Patient;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Provider;
import edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Vital;

/**
 * Inserts data into a Protempa database.
 * 
 * @author hrathod
 * 
 */
public class DataInserter {

	/**
	 * The user's configuration, used to get database connection information.
	 */
	private final Configuration configuration;

	/**
	 * Build a new object with the given configuration.
	 * 
	 * @param inConfiguration The configuration to use for database connection
	 *            information.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public DataInserter(Configuration inConfiguration) throws SQLException {
		super();
		this.configuration = inConfiguration;
		this.truncateTables();
	}

	/**
	 * Get a connection to the target database using the current user's
	 * configuration.
	 * 
	 * @return A connection to the target database.
	 * 
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	private Connection getConnection() throws SQLException {
		StringBuilder connectString = new StringBuilder();
		connectString.append("jdbc:oracle:thin:@")
				.append(this.configuration.getProtempaHost()).append(":")
				.append(this.configuration.getProtempaPort()).append("/")
				.append(this.configuration.getProtempaDatabaseName());
		return DriverManager.getConnection(connectString.toString(),
				this.configuration.getProtempaSchema(),
				this.configuration.getProtempaPass());
	}

	/**
	 * Truncate all the tables that we will be inserting into later.
	 * 
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	private void truncateTables() throws SQLException {
		final Connection connection = this.getConnection();
		final List<String> sqlStatements = new ArrayList<String>();
		sqlStatements.add("truncate table patient");
		sqlStatements.add("truncate table encounter");
		sqlStatements.add("truncate table provider");
		sqlStatements.add("truncate table cpt_event");
		sqlStatements.add("truncate table icd9d_event");
		sqlStatements.add("truncate table icd9p_event");
		sqlStatements.add("truncate table labs_event");
		sqlStatements.add("truncate table meds_event");
		sqlStatements.add("truncate table vitals_event");

		for (String sql : sqlStatements) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.close();
		}
		connection.commit();
		connection.close();
	}

	/**
	 * Insert a list of patients to the data base using the given connection.
	 * 
	 * @param patients The list of patients to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertPatients(List<Patient> patients) throws SQLException {
		int counter = 0;
		final Connection connection = this.getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into patient values (?,?,?,?,?,?,?,?)");
		for (Patient patient : patients) {
			Date dateOfBirth;
			if (patient.getDateOfBirth() == null) {
				dateOfBirth = null;
			} else {
				dateOfBirth = new Date(patient.getDateOfBirth().getTime());
			}
			preparedStatement.setLong(1, patient.getId().longValue());
			preparedStatement.setString(2, patient.getFirstName());
			preparedStatement.setString(3, patient.getLastName());
			preparedStatement.setDate(4, dateOfBirth);
			preparedStatement.setString(5, patient.getLanguage());
			preparedStatement.setString(6, patient.getMaritalStatus());
			preparedStatement.setString(7, patient.getRace());
			preparedStatement.setString(8, patient.getGender());
			preparedStatement.addBatch();

			counter++;
			if (counter >= 128) {
				preparedStatement.executeBatch();
				connection.commit();
				preparedStatement.clearBatch();
				counter = 0;
			}
		}
		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.clearBatch();
		preparedStatement.close();
		connection.close();
	}

	/**
	 * Insert the given list of encounters to a target database using the given
	 * connection.
	 * 
	 * @param encounters The list of encounters to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertEncounters(List<Encounter> encounters)
			throws SQLException {
		int counter = 0;
		final Connection connection = this.getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into encounter values (?,?,?,?,?,?,?)");
		for (Encounter encounter : encounters) {
			preparedStatement.setLong(1, encounter.getId().longValue());
			preparedStatement.setLong(2, encounter.getPatientId().longValue());
			preparedStatement.setLong(3, encounter.getProviderId().longValue());
			preparedStatement.setTimestamp(4, new Timestamp(encounter
					.getStart().getTime()));
			preparedStatement.setTimestamp(5, new Timestamp(encounter.getEnd()
					.getTime()));
			preparedStatement.setString(6, encounter.getType());
			preparedStatement.setString(7, encounter.getDischargeDisposition());
			preparedStatement.addBatch();

			counter++;
			if (counter >= 128) {
				preparedStatement.executeBatch();
				connection.commit();
				preparedStatement.clearBatch();
				counter = 0;
			}
		}
		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.clearBatch();
		preparedStatement.close();
		connection.close();
	}

	/**
	 * Insert the given list of providers to a target database using the given
	 * connection.
	 * 
	 * @param providers The list of providers to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertProviders(List<Provider> providers) throws SQLException {
		int counter = 0;
		final Connection connection = this.getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into provider values (?,?,?)");
		for (Provider provider : providers) {
			preparedStatement.setLong(1, provider.getId().longValue());
			preparedStatement.setString(2, provider.getFirstName());
			preparedStatement.setString(3, provider.getLastName());
			preparedStatement.addBatch();

			counter++;
			if (counter >= 128) {
				preparedStatement.executeBatch();
				connection.commit();
				preparedStatement.clearBatch();
				counter = 0;
			}
		}
		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.clearBatch();
		preparedStatement.close();
		connection.close();
	}

	/**
	 * Insert the given list of CPT codes to a target database using the given
	 * connection.
	 * 
	 * @param cptCodes The list of CPT codes to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertCptCodes(List<CPT> cptCodes) throws SQLException {
		this.insertObservations(cptCodes, "cpt_event");
	}

	/**
	 * Insert the given list of ICD9 diagnosis codes to a target database using
	 * the given connection.
	 * 
	 * @param diagnoses The list of diagnosis codes to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertIcd9Diagnoses(List<Icd9Diagnosis> diagnoses)
			throws SQLException {
		this.insertObservations(diagnoses, "icd9d_event");
	}

	/**
	 * Insert the given list of ICD9 procedure codes to a target database using
	 * the given connection.
	 * 
	 * @param procedures The list of procedure codes to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertIcd9Procedures(List<Icd9Procedure> procedures)
			throws SQLException {
		this.insertObservations(procedures, "icd9p_event");
	}

	/**
	 * Insert the given list of medications to a target database using the given
	 * connection.
	 * 
	 * @param medications The list of medications to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertMedications(List<Medication> medications)
			throws SQLException {
		this.insertObservations(medications, "meds_event");
	}

	/**
	 * Insert the given list of lab results to a target database using the given
	 * connection.
	 * 
	 * @param labs The list of lab results to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertLabs(List<Lab> labs) throws SQLException {
		this.insertObservationsWithResult(labs, "labs_event");
	}

	/**
	 * Insert the given list of vital signs to a target database using the given
	 * connection.
	 * 
	 * @param vitals The list of vitals to insert.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	public void insertVitals(List<Vital> vitals) throws SQLException {
		this.insertObservationsWithResult(vitals, "vitals_event");
	}

	/**
	 * Add the given list of observation objects to a target database using the
	 * given connection.
	 * 
	 * @param observations The list of observations to insert.
	 * @param table The table in which the observations should be inserted.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	private void insertObservations(List<? extends Observation> observations,
			String table) throws SQLException {
		int counter = 0;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into ").append(table)
				.append(" values (?,?,?,?)");
		final Connection connection = this.getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement(sqlBuilder.toString());
		for (Observation observation : observations) {
			preparedStatement.setString(1, observation.getId());
			preparedStatement.setLong(2, observation.getEncounterId()
					.longValue());
			preparedStatement.setTimestamp(3, new Timestamp(observation
					.getTimestamp().getTime()));
			preparedStatement.setString(4, observation.getEntityId());
			preparedStatement.addBatch();

			counter++;
			if (counter >= 128) {
				preparedStatement.executeBatch();
				connection.commit();
				preparedStatement.clearBatch();
				counter = 0;
			}
		}
		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.clearBatch();
		preparedStatement.close();
		connection.close();
	}

	/**
	 * Insert a list of observations and their related results to a target
	 * database using the given connection.
	 * 
	 * @param observations The observations to insert.
	 * @param table The table in which the observations should be inserted.
	 * @throws SQLException Thrown if there are any JDBC errors.
	 */
	private void insertObservationsWithResult(
			List<? extends ObservationWithResult> observations, String table)
			throws SQLException {
		int counter = 0;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("insert into ").append(table)
				.append(" values (?,?,?,?,?,?,?,?)");
		final Connection connection = this.getConnection();
		PreparedStatement preparedStatement = connection
				.prepareStatement(sqlBuilder.toString());
		for (ObservationWithResult observation : observations) {
			preparedStatement.setString(1, observation.getId());
			preparedStatement.setLong(2, observation.getEncounterId()
					.longValue());
			preparedStatement.setTimestamp(3, new Timestamp(observation
					.getTimestamp().getTime()));
			preparedStatement.setString(4, observation.getEntityId());
			preparedStatement.setString(5, observation.getResultAsStr());
			preparedStatement.setDouble(6, observation.getResultAsNum()
					.doubleValue());
			preparedStatement.setString(7, observation.getUnits());
			preparedStatement.setString(8, observation.getFlag());
			preparedStatement.addBatch();

			counter++;
			if (counter >= 128) {
				preparedStatement.executeBatch();
				connection.commit();
				preparedStatement.clearBatch();
				counter = 0;
			}
		}
		preparedStatement.executeBatch();
		connection.commit();
		preparedStatement.clearBatch();
		preparedStatement.close();
		connection.close();
	}

}
