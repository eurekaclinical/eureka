package edu.emory.cci.aiw.cvrg.eureka.etl.dsb;

import java.io.IOException;

import org.protempa.backend.annotations.BackendInfo;
import org.protempa.backend.dsb.relationaldb.ColumnSpec;
import org.protempa.backend.dsb.relationaldb.EntitySpec;
import org.protempa.backend.dsb.relationaldb
		.JDBCDateTimeTimestampDateValueFormat;
import org.protempa.backend.dsb.relationaldb
		.JDBCDateTimeTimestampPositionParser;
import org.protempa.backend.dsb.relationaldb.JDBCPositionFormat;
import org.protempa.backend.dsb.relationaldb.JoinSpec;
import org.protempa.backend.dsb.relationaldb.PropIdToSQLCodeMapper;
import org.protempa.backend.dsb.relationaldb.PropertySpec;
import org.protempa.backend.dsb.relationaldb.ReferenceSpec;
import org.protempa.backend.dsb.relationaldb.RelationalDbDataSourceBackend;
import org.protempa.backend.dsb.relationaldb.StagingSpec;
import org.protempa.proposition.value.AbsoluteTimeGranularity;
import org.protempa.proposition.value.AbsoluteTimeGranularityFactory;
import org.protempa.proposition.value.AbsoluteTimeUnitFactory;
import org.protempa.proposition.value.GranularityFactory;
import org.protempa.proposition.value.UnitFactory;
import org.protempa.proposition.value.ValueType;

/**
 * Data source backend for Eureka!.
 *
 * @author Andrew Post
 */
@BackendInfo(displayName = "Eureka Data Source Backend")
public class EurekaDataSourceBackend extends RelationalDbDataSourceBackend {

	private static AbsoluteTimeUnitFactory absTimeUnitFactory =
			new AbsoluteTimeUnitFactory();
	private static AbsoluteTimeGranularityFactory absTimeGranularityFactory =
			new AbsoluteTimeGranularityFactory();
	private static JDBCPositionFormat dtPositionParser =
			new JDBCDateTimeTimestampPositionParser();

	private final PropIdToSQLCodeMapper mapper;

	public EurekaDataSourceBackend() {
		this.mapper = new PropIdToSQLCodeMapper("/mappings/",
				getClass());
	}

	protected StagingSpec[] stagedSpecs() throws IOException {

		return null;
	}

	@Override
	public String getSchemaName() {
		return super.username;
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
												new ColumnSpec
														(getKeyIdSchema(),
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
												true), ValueType.NOMINALVALUE),
								/*new PropertySpec("aprdrgRiskMortalityValue", null, new ColumnSpec(schemaName, "ENCOUNTER", "APRRISKOFMORTALITY"), ValueType.NUMERICALVALUE), new PropertySpec("aprdrgSeverityValue", null, new ColumnSpec(schemaName, "ENCOUNTER", "APRSEVERITYOFILLNESS"), ValueType.NOMINALVALUE), new PropertySpec("insuranceType", null, new ColumnSpec(schemaName, "ENCOUNTER", "HOSPITALPRIMARYPAYER", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("insurance_types_07182011.txt")), ValueType.NOMINALVALUE)*/},
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
										ReferenceSpec.Type.MANY),
								/*new ReferenceSpec("chargeAmount", "Hospital Charge Amount", new ColumnSpec[]{new ColumnSpec(schemaName, "ENCOUNTER", "RECORD_ID")}, ReferenceSpec.Type.ONE)*/},
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
								ValueType.NOMINALVALUE),
						/*new PropertySpec("position", null, new ColumnSpec(schemaName, "ENCOUNTER", new JoinSpec("RECORD_ID", "RECORD_ID", new ColumnSpec(schemaName, "DIAGNOSIS", "SEQ_NBR", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("icd9_diagnosis_position_07182011.txt")))), ValueType.NOMINALVALUE)*/},
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
						null, new PropertySpec[]{
						/*new PropertySpec("orderDescription", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("synonym_order_key", "synonym_order_key", new ColumnSpec(schemaName, "lkp_synonym_order", "synonym_order_desc"))), ValueType.NOMINALVALUE), new PropertySpec("orderContext", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("context_medication_key", "context_medication_key", new ColumnSpec(schemaName, "lkp_context_medication", "context_medication_id", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("order_context_03292011.txt"), true))), ValueType.NOMINALVALUE), new PropertySpec("continuingOrder", null, new ColumnSpec(schemaName, "fact_history_medication", "order_continuing_ind"), ValueType.BOOLEANVALUE), new PropertySpec("orderStatus", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("order_status_key", "order_status_key", new ColumnSpec(schemaName, "lkp_order_status", "order_status_id", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("order_status_03292011.txt"), true))), ValueType.NOMINALVALUE), new PropertySpec("orderAction", null, new ColumnSpec(schemaName, "fact_history_medication", new JoinSpec("action_order_key", "action_order_key", new ColumnSpec(schemaName, "lkp_action_order", "action_order_id", ColumnSpec.Constraint.EQUAL_TO, this.mapper.propertyNameOrPropIdToSqlCodeArray("order_action_03292011.txt"), false))), ValueType.NOMINALVALUE)*/},
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
				dtPositionParser, null),

		};
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

}
