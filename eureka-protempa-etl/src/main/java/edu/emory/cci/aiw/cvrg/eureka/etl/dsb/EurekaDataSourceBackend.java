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
package edu.emory.cci.aiw.cvrg.eureka.etl.dsb;

import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet.DataInserter;
import edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet.DataInserterException;
import edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet.DataProvider;
import edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet.DataProviderException;
import edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet.DataValidator;
import edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet.XlsxDataProvider;
import org.arp.javautil.io.FileUtil;
import org.arp.javautil.io.IOUtil;
import org.arp.javautil.sql.InvalidConnectionSpecArguments;
import org.arp.javautil.sql.SQLExecutor;
import org.drools.util.StringUtils;
import org.protempa.BackendCloseException;
import org.protempa.DataSourceBackendCloseException;
import org.protempa.DataSourceReadException;
import org.protempa.DataStreamingEventIterator;
import org.protempa.KnowledgeSource;
import org.protempa.KnowledgeSourceReadException;
import org.protempa.ProtempaException;
import org.protempa.QuerySession;
import org.protempa.backend.BackendInitializationException;
import org.protempa.backend.BackendInstanceSpec;
import org.protempa.backend.DataSourceBackendFailedDataValidationException;
import org.protempa.backend.DataSourceBackendInitializationException;
import org.protempa.backend.annotations.BackendInfo;
import org.protempa.backend.annotations.BackendProperty;
import org.protempa.backend.dsb.DataValidationEvent;
import org.protempa.backend.dsb.filter.Filter;
import org.protempa.backend.dsb.relationaldb.ColumnSpec;
import org.protempa.backend.dsb.relationaldb.EntitySpec;
import org.protempa.backend.dsb.relationaldb.JDBCDateTimeTimestampDateValueFormat;
import org.protempa.backend.dsb.relationaldb.JDBCDateTimeTimestampPositionParser;
import org.protempa.backend.dsb.relationaldb.JDBCPositionFormat;
import org.protempa.backend.dsb.relationaldb.JoinSpec;
import org.protempa.backend.dsb.relationaldb.PropIdToSQLCodeMapper;
import org.protempa.backend.dsb.relationaldb.PropertySpec;
import org.protempa.backend.dsb.relationaldb.ReferenceSpec;
import org.protempa.backend.dsb.relationaldb.RelationalDbDataSourceBackend;
import org.protempa.backend.dsb.relationaldb.StagingSpec;
import org.protempa.proposition.Proposition;
import org.protempa.proposition.value.AbsoluteTimeGranularity;
import org.protempa.proposition.value.AbsoluteTimeGranularityFactory;
import org.protempa.proposition.value.AbsoluteTimeUnitFactory;
import org.protempa.proposition.value.GranularityFactory;
import org.protempa.proposition.value.UnitFactory;
import org.protempa.proposition.value.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Data source backend for Eureka!.
 *
 * @author Andrew Post
 */
@BackendInfo(displayName = "Spreadsheet Data Source Backend")
public final class EurekaDataSourceBackend extends RelationalDbDataSourceBackend {

	private static final Logger LOGGER = LoggerFactory.getLogger(EurekaDataSourceBackend.class);
	private static AbsoluteTimeUnitFactory absTimeUnitFactory =
			new AbsoluteTimeUnitFactory();
	private static AbsoluteTimeGranularityFactory absTimeGranularityFactory =
			new AbsoluteTimeGranularityFactory();
	private static JDBCPositionFormat dtPositionParser =
			new JDBCDateTimeTimestampPositionParser();
	private final PropIdToSQLCodeMapper mapper;
	private XlsxDataProvider[] dataProviders;
	private boolean dataPopulated;
	private String dataFileDirectoryName;
	private String[] mimetypes;
	private String sampleUrl;
	private Boolean required = Boolean.FALSE;
	private String databaseName;
	private boolean exceptionOccurred;

	@Override
	public void initialize(BackendInstanceSpec config) throws BackendInitializationException {
		super.initialize(config);
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException ex) {
			throw new DataSourceBackendInitializationException("The H2 database driver is not registered", ex);
		}
		String databaseName = getDatabaseId();
		if (databaseName == null) {
			throw new DataSourceBackendInitializationException("No database name specified for data source backend '" + nameForErrors() + "'");
		}
		File schemaFile;
		try {
			schemaFile = IOUtil.resourceToFile("/eureka-dsb-schema.sql", "eureka-dsb-schema", ".sql");
		} catch (IOException ex) {
			throw new DataSourceBackendInitializationException("Unable to create data schema (data source backend '" + nameForErrors() + "')");
		}
		super.setDatabaseId("jdbc:h2:mem:" + databaseName + ";INIT=RUNSCRIPT FROM '" + schemaFile + "';DB_CLOSE_DELAY=-1");
		File dataFileDirectory = new EtlProperties().uploadedDirectory(getConfigurationsId(), this.dataFileDirectoryName);
		File[] dataFiles = dataFileDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String string) {
				return string.endsWith(".uploaded");
			}
		});
		if (dataFiles != null) {
			this.dataProviders = new XlsxDataProvider[dataFiles.length];
			for (int i = 0; i < dataFiles.length; i++) {
				try {
					this.dataProviders[i] = new XlsxDataProvider(dataFiles[i], null);
				} catch (DataProviderException ex) {
					dataFiles[i].renameTo(FileUtil.replaceExtension
							(dataFiles[i], ".failed"));
					throw new DataSourceBackendInitializationException("Error initializing data source backend " + this.nameForErrors(), ex);
				}
			}
		}
	}

	@Override
	public DataValidationEvent[] validateData(KnowledgeSource knowledgeSource) throws DataSourceBackendFailedDataValidationException, KnowledgeSourceReadException {
		List<DataValidationEvent> events = new ArrayList<DataValidationEvent>();
		boolean failedValidation = false;
		if (this.dataProviders == null) {
			throw new DataSourceBackendFailedDataValidationException("No spreadsheets found by data source backend " + nameForErrors(), null);
		}
		for (XlsxDataProvider dataProvider : this.dataProviders) {
			DataValidator dataValidator = new DataValidator(dataProvider.getDataFile());
			try {
				dataValidator.setPatients(dataProvider.getPatients())
						.setEncounters(dataProvider.getEncounters())
						.setProviders(dataProvider.getProviders())
						.setCpts(dataProvider.getCptCodes())
						.setIcd9Procedures(dataProvider.getIcd9Procedures())
						.setIcd9Diagnoses(dataProvider.getIcd9Diagnoses())
						.setMedications(dataProvider.getMedications())
						.setLabs(dataProvider.getLabs())
						.setVitals(dataProvider.getVitals()).validate();
			} catch (DataProviderException e) {
				throw new DataSourceBackendFailedDataValidationException(e, null);
			}
			events.addAll(dataValidator.getValidationEvents());

			if (dataValidator.isFailed()) {
				failedValidation = true;
			}
		}
		DataValidationEvent[] validationEvents = events.toArray(new DataValidationEvent[events.size()]);
		if (failedValidation) {
			throw new DataSourceBackendFailedDataValidationException("Invalid spreadsheet " + this.dataFileDirectoryName + " in data source backend " + nameForErrors(), validationEvents);
		} else {
			return validationEvents;
		}
	}

	private void populateDatabase() throws DataSourceReadException {
		Connection dataInserterConnection = null;
		try {
			dataInserterConnection =
					getConnectionSpecInstance().getOrCreate();
			for (DataProvider dataProvider : this.dataProviders) {
				DataInserter dataInserter =
						new DataInserter(dataInserterConnection);
				dataInserter.insertPatients(dataProvider.getPatients());
				dataInserter.insertEncounters(dataProvider.getEncounters());
				dataInserter.insertProviders(dataProvider.getProviders());
				dataInserter.insertCptCodes(dataProvider.getCptCodes());
				dataInserter.insertIcd9Diagnoses(dataProvider
						.getIcd9Diagnoses());
				dataInserter.insertIcd9Procedures(dataProvider
						.getIcd9Procedures());
				dataInserter.insertLabs(dataProvider.getLabs());
				dataInserter.insertMedications(dataProvider.getMedications());
				dataInserter.insertVitals(dataProvider.getVitals());
			}
			this.dataPopulated = true;
			dataInserterConnection.close();
			dataInserterConnection = null;
		} catch (SQLException sqle) {
			throw new DataSourceReadException("Error reading spreadsheets in " + this.dataFileDirectoryName + " in data source backend " + nameForErrors(), sqle);
		} catch (InvalidConnectionSpecArguments ex) {
			throw new DataSourceReadException("Error reading spreadsheets in " + this.dataFileDirectoryName + " in data source backend " + nameForErrors(), ex);
		} catch (DataInserterException e) {
			throw new DataSourceReadException("Error reading spreadsheets in " + this.dataFileDirectoryName + " in data source backend " + nameForErrors(), e);
		} catch (DataProviderException e) {
			throw new DataSourceReadException("Error reading spreadsheets in " + this.dataFileDirectoryName + " in data source backend " + nameForErrors(), e);
		} finally {
			if (dataInserterConnection != null) {
				try {
					dataInserterConnection.close();
				} catch (SQLException ignore) {
				}
			}
		}
	}

	public EurekaDataSourceBackend() {
		this.mapper = new PropIdToSQLCodeMapper("/mappings/",
				getClass());
	}

	@Override
	public DataStreamingEventIterator<Proposition> readPropositions(Set<String> keyIds, Set<String> propIds, Filter filters, QuerySession qs) throws DataSourceReadException {
		if (!dataPopulated) {
			populateDatabase();
		}
		return super.readPropositions(keyIds, propIds, filters, qs);
	}

	@Override
	protected StagingSpec[] stagedSpecs() throws IOException {
		return null;
	}

	@Override
	public String getSchemaName() {
		return "EUREKA";
	}

	@Override
	public String getKeyIdTable() {
		return "PATIENT";
	}

	@Override
	public String getKeyIdColumn() {
		return "PATIENT_KEY";
	}

	@Override
	public String getKeyIdJoinKey() {
		return "PATIENT_KEY";
	}

	@Override
	public String getKeyType() {
		return "Patient";
	}

	@Override
	public String getKeyTypeDisplayName() {
		return "patient";
	}

	public String getSampleUrl() {
		return sampleUrl;
	}

	@Override
	public String getDatabaseId() {
		return this.databaseName;
	}

	@Override
	@BackendProperty(propertyName = "databaseName")
	public void setDatabaseId(String databaseId) {
		this.databaseName = databaseId;
	}

	@BackendProperty
	public void setSampleUrl(String sampleUrl) {
		this.sampleUrl = sampleUrl;
	}

	public String[] getMimetypes() {
		return mimetypes.clone();
	}

	public void setMimetypes(String[] mimetypes) {
		if (mimetypes == null) {
			this.mimetypes = StringUtils.EMPTY_STRING_ARRAY;
		} else {
			this.mimetypes = mimetypes.clone();
		}
	}

	@BackendProperty(propertyName = "mimetypes")
	public void parseMimetypes(String mimetypesString) {
		if (mimetypesString == null) {
			setMimetypes(null);
		} else {
			setMimetypes(mimetypesString.split("\\|"));
		}
	}

	public String getDataFileDirectoryName() {
		return dataFileDirectoryName;
	}

	@BackendProperty
	public void setDataFileDirectoryName(String dataFileDirectoryName) {
		this.dataFileDirectoryName = dataFileDirectoryName;
	}

	public Boolean isRequired() {
		return required;
	}

	@BackendProperty
	public void setRequired(Boolean required) {
		if (required == null) {
			this.required = Boolean.FALSE;
		} else {
			this.required = required;
		}
	}

	@Override
	protected EntitySpec[] constantSpecs() throws IOException {
		String schemaName = getSchemaName();
		EntitySpec[] constantSpecs = new EntitySpec[]{
			new EntitySpec("Patients", null, new String[]{"PatientAll"},
			false, new ColumnSpec(getKeyIdSchema(),
			getKeyIdTable(),
			getKeyIdColumn()), new ColumnSpec[]{
				new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
				getKeyIdColumn())}, null, null,
			new PropertySpec[]{
				new PropertySpec("patientId", null,
				new ColumnSpec(getKeyIdSchema(),
				getKeyIdTable(),
				"PATIENT_KEY"),
				ValueType.NOMINALVALUE)},
			new ReferenceSpec[]{
				new ReferenceSpec("encounters", "Encounters",
				new ColumnSpec[]{
					new ColumnSpec(getKeyIdSchema(),
					getKeyIdTable(),
					new JoinSpec(
					"PATIENT_KEY",
					"PATIENT_KEY",
					new ColumnSpec(
					schemaName,
					"ENCOUNTER",
					"ENCOUNTER_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("patientDetails",
				"Patient Details", new ColumnSpec[]{
					new ColumnSpec(getKeyIdSchema(),
					getKeyIdTable(),
					"PATIENT_KEY")},
				ReferenceSpec.Type.MANY)}, null, null,
			null, null, null, null, null, null),
			new EntitySpec("Patient Details", null, new String[]{"Patient"},
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn()), new ColumnSpec[]{
				new ColumnSpec(schemaName, getKeyIdTable(),
				"PATIENT_KEY")}, null, null, new PropertySpec[]{
				new PropertySpec("dateOfBirth", null,
				new ColumnSpec(getKeyIdSchema(),
				getKeyIdTable(), "DOB"),
				ValueType.DATEVALUE,
				new JDBCDateTimeTimestampDateValueFormat()),
				new PropertySpec("patientId", null,
				new ColumnSpec(getKeyIdSchema(),
				getKeyIdTable(), "PATIENT_KEY"),
				ValueType.NOMINALVALUE),
				new PropertySpec("firstName", null,
				new ColumnSpec(schemaName, "PATIENT",
				"FIRST_NAME"), ValueType.NOMINALVALUE),
				new PropertySpec("lastName", null,
				new ColumnSpec(schemaName, "PATIENT",
				"LAST_NAME"), ValueType.NOMINALVALUE),
				new PropertySpec("gender", null,
				new ColumnSpec(schemaName, "PATIENT", "GENDER",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"gender_08172011.txt"),
				true), ValueType.NOMINALVALUE),
				new PropertySpec(
				"maritalStatus",
				null,
				new ColumnSpec(
				schemaName,
				"PATIENT",
				"MARITAL_STATUS",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray("marital_status_08172011.txt"),
				true), ValueType.NOMINALVALUE),
				new PropertySpec(
				"language",
				null,
				new ColumnSpec(
				schemaName,
				"PATIENT",
				"LANGUAGE",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray("language_08152012.txt"),
				true), ValueType.NOMINALVALUE),
				new PropertySpec("race", null,
				new ColumnSpec(schemaName, "PATIENT", "RACE",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"race_08172011.txt"),
				true), ValueType.NOMINALVALUE),
				new PropertySpec("ethnicity", null,
				new ColumnSpec(schemaName, "PATIENT", "RACE",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"ethnicity_08172011.txt"),
				true), ValueType.NOMINALVALUE)},
			new ReferenceSpec[]{
				new ReferenceSpec("encounters", "Encounters",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"PATIENT", new JoinSpec(
					"PATIENT_KEY",
					"PATIENT_KEY",
					new ColumnSpec(
					schemaName,
					"ENCOUNTER",
					"ENCOUNTER_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("patient", "Patients",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"PATIENT",
					"PATIENT_KEY")},
				ReferenceSpec.Type.ONE)}, null, null,
			null, null, null, null, null, null),
			new EntitySpec("Providers", null,
			new String[]{"AttendingPhysician"}, false,
			new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("PROVIDER_KEY",
			"PROVIDER_KEY",
			new ColumnSpec(
			schemaName,
			"PROVIDER"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "PROVIDER",
				"PROVIDER_KEY")}, null, null,
			new PropertySpec[]{
				new PropertySpec("firstName", null,
				new ColumnSpec(schemaName, "PROVIDER",
				"FIRST_NAME"),
				ValueType.NOMINALVALUE),
				new PropertySpec("lastName", null,
				new ColumnSpec(schemaName, "PROVIDER",
				"LAST_NAME"),
				ValueType.NOMINALVALUE)}, null, null,
			null, null, null, null, null, null, null),};
		return constantSpecs;
	}

	@Override
	protected EntitySpec[] eventSpecs() throws IOException {
		String schemaName = getSchemaName();
		EntitySpec[] eventSpecs = new EntitySpec[]{
			new EntitySpec("Encounters", null, new String[]{"Encounter"},
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER"))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "ENCOUNTER",
				"ENCOUNTER_KEY")},
			new ColumnSpec(schemaName, "ENCOUNTER", "TS_START"),
			new ColumnSpec(schemaName, "ENCOUNTER", "TS_END"),
			new PropertySpec[]{
				new PropertySpec("encounterId", null,
				new ColumnSpec(schemaName, "ENCOUNTER",
				"ENCOUNTER_KEY"),
				ValueType.NOMINALVALUE),
				new PropertySpec("type", null,
				new ColumnSpec(schemaName, "ENCOUNTER",
				"ENCOUNTER_TYPE",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"type_encounter_08172011.txt"),
				true), ValueType.NOMINALVALUE),
				/*new PropertySpec("healthcareEntity", null, new ColumnSpec(schemaName, "ENCOUNTER", "UNIVCODE", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("entity_healthcare_07182011.txt"), true), ValueType.NOMINALVALUE), */
				new PropertySpec("dischargeDisposition", null,
				new ColumnSpec(schemaName, "ENCOUNTER",
				"DISCHARGE_DISP",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"disposition_discharge_08172011.txt"),
				true), ValueType.NOMINALVALUE), /*new PropertySpec("aprdrgRiskMortalityValue", null, new ColumnSpec(schemaName, "ENCOUNTER", "APRRISKOFMORTALITY"), ValueType.NUMERICALVALUE), new PropertySpec("aprdrgSeverityValue", null, new ColumnSpec(schemaName, "ENCOUNTER", "APRSEVERITYOFILLNESS"), ValueType.NOMINALVALUE), new PropertySpec("insuranceType", null, new ColumnSpec(schemaName, "ENCOUNTER", "HOSPITALPRIMARYPAYER", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("insurance_types_07182011.txt")), ValueType.NOMINALVALUE)*/},
			new ReferenceSpec[]{
				new ReferenceSpec("patient", "Patients",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"ENCOUNTER",
					"PATIENT_KEY")},
				ReferenceSpec.Type.ONE),
				new ReferenceSpec("labs", "Labs",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"ENCOUNTER",
					new JoinSpec(
					"ENCOUNTER_KEY",
					"ENCOUNTER_KEY",
					new ColumnSpec(
					schemaName,
					"LABS_EVENT",
					"EVENT_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("vitals", "Vitals",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"ENCOUNTER",
					new JoinSpec(
					"ENCOUNTER_KEY",
					"ENCOUNTER_KEY",
					new ColumnSpec(
					schemaName,
					"VITALS_EVENT",
					"EVENT_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("diagnosisCodes",
				"Diagnosis Codes", new ColumnSpec[]{
					new ColumnSpec(schemaName, "ENCOUNTER",
					new JoinSpec("ENCOUNTER_KEY",
					"ENCOUNTER_KEY",
					new ColumnSpec(
					schemaName,
					"ICD9D_EVENT",
					"EVENT_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("medications",
				"Medication Orders", new ColumnSpec[]{
					new ColumnSpec(schemaName, "ENCOUNTER",
					new JoinSpec("ENCOUNTER_KEY",
					"ENCOUNTER_KEY",
					new ColumnSpec(
					schemaName,
					"MEDS_EVENT",
					"EVENT_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("procedures",
				"ICD9 Procedure Codes",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"ENCOUNTER",
					new JoinSpec(
					"ENCOUNTER_KEY",
					"ENCOUNTER_KEY",
					new ColumnSpec(
					schemaName,
					"ICD9P_EVENT",
					"EVENT_KEY")))},
				ReferenceSpec.Type.MANY),
				new ReferenceSpec("procedures",
				"CPT Procedure Codes", new ColumnSpec[]{
					new ColumnSpec(schemaName, "ENCOUNTER",
					new JoinSpec("ENCOUNTER_KEY",
					"ENCOUNTER_KEY",
					new ColumnSpec(
					schemaName,
					"CPT_EVENT",
					"EVENT_KEY")))},
				ReferenceSpec.Type.MANY),
				/*new ReferenceSpec("msdrg", "MSDRG Codes", new ColumnSpec[]{new ColumnSpec(schemaName, "ENCOUNTER", "RECORD_ID")}, ReferenceSpec.Type.ONE), new ReferenceSpec("aprdrg", "APR DRG Codes", new ColumnSpec[]{new ColumnSpec(schemaName, "ENCOUNTER", "RECORD_ID")}, ReferenceSpec.Type.ONE), */
				new ReferenceSpec("provider", "Providers",
				new ColumnSpec[]{
					new ColumnSpec(schemaName,
					"ENCOUNTER",
					"PROVIDER_KEY")},
				ReferenceSpec.Type.ONE),
				/*new ReferenceSpec("attendingPhysician", "AttendingPhysicians", new ColumnSpec[]{new ColumnSpec(schemaName, "ENCOUNTER", "DISCHARGEPHYSICIAN")}, ReferenceSpec.Type.ONE), */
				new ReferenceSpec("patientDetails",
				"Patient Details", new ColumnSpec[]{
					new ColumnSpec(schemaName, "ENCOUNTER",
					"PATIENT_KEY")},
				ReferenceSpec.Type.MANY), /*new ReferenceSpec("chargeAmount", "Hospital Charge Amount", new ColumnSpec[]{new ColumnSpec(schemaName, "ENCOUNTER", "RECORD_ID")}, ReferenceSpec.Type.ONE)*/},
			null, null, null, null, null,
			AbsoluteTimeGranularity.DAY, dtPositionParser, null),
			new EntitySpec("Diagnosis Codes", null, this.mapper
			.readCodes("icd9_diagnosis_08172011.txt", "\t", 0),
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("ENCOUNTER_KEY",
			"ENCOUNTER_KEY",
			new ColumnSpec(schemaName,
			"ICD9D_EVENT"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "ICD9D_EVENT",
				"EVENT_KEY")},
			new ColumnSpec(schemaName, "ICD9D_EVENT", "TS_OBX"),
			null, new PropertySpec[]{
				new PropertySpec("code", null,
				new ColumnSpec(schemaName, "ICD9D_EVENT",
				"ENTITY_ID",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"icd9_diagnosis_08172011.txt")),
				ValueType.NOMINALVALUE), /*new PropertySpec("position", null, new ColumnSpec(schemaName, "ENCOUNTER", new JoinSpec("RECORD_ID", "RECORD_ID", new ColumnSpec(schemaName, "DIAGNOSIS", "SEQ_NBR", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("icd9_diagnosis_position_07182011.txt")))), ValueType.NOMINALVALUE)*/},
			null, null,
			new ColumnSpec(schemaName, "ICD9D_EVENT", "ENTITY_ID",
			ColumnSpec.Constraint.EQUAL_TO, this.mapper
			.propertyNameOrPropIdToSqlCodeArray(
			"icd9_diagnosis_08172011.txt"), true),
			null, null, null, AbsoluteTimeGranularity.MINUTE,
			dtPositionParser, null),
			new EntitySpec("ICD9 Procedure Codes", null, this.mapper
			.readCodes("icd9_procedure_08172011.txt", "\t", 0),
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("ENCOUNTER_KEY",
			"ENCOUNTER_KEY",
			new ColumnSpec(schemaName,
			"ICD9P_EVENT"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "ICD9P_EVENT",
				"EVENT_KEY")},
			new ColumnSpec(schemaName, "ICD9P_EVENT", "TS_OBX"),
			null, new PropertySpec[]{
				new PropertySpec("code", null,
				new ColumnSpec(schemaName, "ICD9P_EVENT",
				"ENTITY_ID",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"icd9_procedure_08172011.txt")),
				ValueType.NOMINALVALUE)}, null, null,
			new ColumnSpec(schemaName, "ICD9P_EVENT", "ENTITY_ID",
			ColumnSpec.Constraint.EQUAL_TO, this.mapper
			.propertyNameOrPropIdToSqlCodeArray(
			"icd9_procedure_08172011.txt"), true),
			null, null, null, AbsoluteTimeGranularity.MINUTE,
			dtPositionParser, null),
			new EntitySpec("CPT Procedure Codes", null, this.mapper
			.readCodes("cpt_procedure_08172011.txt", "\t", 0), true,
			new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("ENCOUNTER_KEY",
			"ENCOUNTER_KEY",
			new ColumnSpec(
			schemaName,
			"CPT_EVENT"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "CPT_EVENT",
				"EVENT_KEY")},
			new ColumnSpec(schemaName, "CPT_EVENT", "TS_OBX"), null,
			new PropertySpec[]{
				new PropertySpec("code", null,
				new ColumnSpec(schemaName, "CPT_EVENT",
				"ENTITY_ID",
				ColumnSpec.Constraint.EQUAL_TO,
				this.mapper
				.propertyNameOrPropIdToSqlCodeArray(
				"cpt_procedure_08172011.txt")),
				ValueType.NOMINALVALUE)}, null, null,
			new ColumnSpec(schemaName, "CPT_EVENT", "ENTITY_ID",
			ColumnSpec.Constraint.EQUAL_TO, this.mapper
			.propertyNameOrPropIdToSqlCodeArray(
			"cpt_procedure_08172011.txt"), true),
			null, null, null, AbsoluteTimeGranularity.MINUTE,
			dtPositionParser, null),
			new EntitySpec("Medication Orders", null,
			this.mapper.readCodes("meds_08182011.txt", "\t", 0),
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("ENCOUNTER_KEY",
			"ENCOUNTER_KEY",
			new ColumnSpec(schemaName,
			"MEDS_EVENT"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "MEDS_EVENT",
				"EVENT_KEY")},
			new ColumnSpec(schemaName, "MEDS_EVENT", "TS_OBX"),
			null, new PropertySpec[]{ /*new PropertySpec("orderDescription", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("synonym_order_key", "synonym_order_key", new ColumnSpec(schemaName, "lkp_synonym_order", "synonym_order_desc"))), ValueType.NOMINALVALUE), new PropertySpec("orderContext", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("context_medication_key", "context_medication_key", new ColumnSpec(schemaName, "lkp_context_medication", "context_medication_id", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("order_context_03292011.txt"), true))), ValueType.NOMINALVALUE), new PropertySpec("continuingOrder", null, new ColumnSpec(schemaName, "fact_history_medication", "order_continuing_ind"), ValueType.BOOLEANVALUE), new PropertySpec("orderStatus", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("order_status_key", "order_status_key", new ColumnSpec(schemaName, "lkp_order_status", "order_status_id", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("order_status_03292011.txt"), true))), ValueType.NOMINALVALUE), new PropertySpec("orderAction", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("action_order_key", "action_order_key", new ColumnSpec(schemaName, "lkp_action_order", "action_order_id", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("order_action_03292011.txt"), false))), ValueType.NOMINALVALUE)*/},
			null, null,
			new ColumnSpec(schemaName, "MEDS_EVENT", "ENTITY_ID",
			ColumnSpec.Constraint.EQUAL_TO, this.mapper
			.propertyNameOrPropIdToSqlCodeArray(
			"meds_08182011.txt"), true), null, null,
			null, AbsoluteTimeGranularity.MINUTE, dtPositionParser,
			null),};
		return eventSpecs;
	}

	@Override
	protected EntitySpec[] primitiveParameterSpecs() throws IOException {
		String schemaName = getSchemaName();
		EntitySpec[] primitiveParameterSpecs = new EntitySpec[]{
			new EntitySpec("Labs", null,
			this.mapper.readCodes("labs_08172011.txt", "\t", 0),
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(),
			new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("ENCOUNTER_KEY",
			"ENCOUNTER_KEY",
			new ColumnSpec(schemaName,
			"LABS_EVENT"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "LABS_EVENT",
				"EVENT_KEY")},
			new ColumnSpec(schemaName, "LABS_EVENT", "TS_OBX"),
			null, new PropertySpec[]{
				new PropertySpec("unitOfMeasure", null,
				new ColumnSpec(schemaName, "LABS_EVENT",
				"UNITS"), ValueType.NOMINALVALUE),
				/*new PropertySpec("referenceRangeLow", null, new ColumnSpec(schemaName, "fact_result_lab", "reference_range_low_val"), ValueType.NUMBERVALUE), new PropertySpec("referenceRangeHigh", null, new ColumnSpec(schemaName, "fact_result_lab", "reference_range_high_val"), ValueType.NUMBERVALUE), */
				new PropertySpec("interpretation", null,
				new ColumnSpec(schemaName, "LABS_EVENT",
				"FLAG"), ValueType.NOMINALVALUE)}, null,
			null,
			new ColumnSpec(schemaName, "LABS_EVENT", "ENTITY_ID",
			ColumnSpec.Constraint.EQUAL_TO, this.mapper
			.propertyNameOrPropIdToSqlCodeArray(
			"labs_08172011.txt"), true), null,
			new ColumnSpec(schemaName, "LABS_EVENT", "RESULT_STR"),
			ValueType.VALUE, AbsoluteTimeGranularity.MINUTE,
			dtPositionParser, null), new EntitySpec("Vitals", null,
			this.mapper
			.readCodes("vitals_result_types_08172011.txt", "\t", 0),
			true, new ColumnSpec(getKeyIdSchema(), getKeyIdTable(),
			getKeyIdColumn(), new JoinSpec("PATIENT_KEY", "PATIENT_KEY",
			new ColumnSpec(schemaName, "ENCOUNTER",
			new JoinSpec("ENCOUNTER_KEY", "ENCOUNTER_KEY",
			new ColumnSpec(schemaName, "VITALS_EVENT"))))),
			new ColumnSpec[]{
				new ColumnSpec(schemaName, "VITALS_EVENT",
				"EVENT_KEY")},
			new ColumnSpec(schemaName, "VITALS_EVENT", "TS_OBX"), null,
			new PropertySpec[]{
				new PropertySpec("unitOfMeasure", null,
				new ColumnSpec(schemaName, "VITALS_EVENT",
				"UNITS"), ValueType.NOMINALVALUE),
				new PropertySpec("interpretation", null,
				new ColumnSpec(schemaName, "VITALS_EVENT",
				"FLAG"), ValueType.NOMINALVALUE)}, null,
			null, new ColumnSpec(schemaName, "VITALS_EVENT", "ENTITY_ID",
			ColumnSpec.Constraint.EQUAL_TO, this.mapper
			.propertyNameOrPropIdToSqlCodeArray(
			"vitals_result_types_08172011.txt"), true), null,
			new ColumnSpec(schemaName, "VITALS_EVENT", "RESULT_STR"),
			ValueType.VALUE, AbsoluteTimeGranularity.MINUTE,
			dtPositionParser, null),};
		return primitiveParameterSpecs;
	}

	@Override
	public GranularityFactory getGranularityFactory() {
		return absTimeGranularityFactory;
	}

	@Override
	public UnitFactory getUnitFactory() {
		return absTimeUnitFactory;
	}

	@Override
	public void exceptionOccurred(ProtempaException protempaException) {
		this.exceptionOccurred = true;
	}

	@Override
	public void close() throws BackendCloseException {
		super.close();
		BackendCloseException exceptionToThrow = null;
		try {
			SQLExecutor.executeSQL(getConnectionSpecInstance(), "DROP ALL OBJECTS", null);
		} catch (SQLException ex) {
			this.exceptionOccurred = true;
			exceptionToThrow = new DataSourceBackendCloseException("Error in data source backend " + nameForErrors() + ": could not drop the database", ex);
		} catch (InvalidConnectionSpecArguments ex) {
			this.exceptionOccurred = true;
			exceptionToThrow = new DataSourceBackendCloseException("Error in data source backend " + nameForErrors() + ": could not drop the database", ex);
		}
		if (!this.exceptionOccurred) {
			for (XlsxDataProvider dataProvider : dataProviders) {
				File dataFile = dataProvider.getDataFile();
				try {
					if (!dataFile.renameTo(FileUtil.replaceExtension(dataFile, ".processed"))) {
						throw new DataSourceBackendCloseException("Error in data source backend " + nameForErrors() + ": failed to mark data file " + dataFile.getAbsolutePath() + " as processed");
					}
				} catch (SecurityException se) {
					throw new DataSourceBackendCloseException("Error in data source backend " + nameForErrors() + ": failed to mark data file " + dataFile.getAbsolutePath() + " as processed", se);
				}
			}
		} else {
			for (XlsxDataProvider dataProvider : dataProviders) {
				File dataFile = dataProvider.getDataFile();
				try {
					if (!dataFile.renameTo(FileUtil.replaceExtension(dataFile, ".failed"))) {
						if (exceptionToThrow == null) {
							throw new DataSourceBackendCloseException("Error in data source backend " + nameForErrors() + ": could not mark data file " + dataFile.getAbsolutePath() + " as failed");
						}
					}
				} catch (SecurityException se) {
					if (exceptionToThrow == null) {
						throw new DataSourceBackendCloseException("Error in data source backend " + nameForErrors() + ": could not mark data file " + dataFile.getAbsolutePath() + " as failed", se);
					}
				}
			}
		}
		if (exceptionToThrow != null) {
			throw exceptionToThrow;
		}
	}
}
