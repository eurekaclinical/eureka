/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.etl.spreadsheet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;

/**
 * An implementation of the {@link DataProvider} interface, using an Excel
 * workbook as the data source.
 *
 * @author hrathod
 *
 */
public class XlsxDataProvider implements DataProvider {

	/**
	 * The standard date format for data held in the workbook.
	 */
	private static ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		}
	};
	/**
	 * The class level logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(XlsxDataProvider.class);
	/**
	 * Log message template for a missing required worksheet. Takes one
	 * parameter, the worksheet name.
	 */
	private final MessageFormat logErrorMessage;
	/**
	 * Exception message for a missing required worksheet. Takes one parameter,
	 * the worksheet name.
	 */
	private final MessageFormat exceptionErrorMessage;
	/**
	 * A handle to the input stream so we can close it.
	 */
	private final OPCPackage opcPackage;
	/**
	 * Holds the workbook associated with the data file.
	 */
	private final XSSFWorkbook workbook;
	/**
	 * The list of patients parsed from the data file.
	 */
	private List<Patient> patients;
	/**
	 * The list of providers parsed from the data file.
	 */
	private List<Provider> providers;
	/**
	 * The list of encounters parsed from the data file.
	 */
	private List<Encounter> encounters;
	/**
	 * The list of CPT codes parsed from the data file.
	 */
	private List<CPT> cpts;
	/**
	 * The list of ICD9 Diagnostic codes parsed from the data file.
	 */
	private List<Icd9Diagnosis> icd9Diagnoses;
	/**
	 * The list of ICD9 Procedure codes parsed from the data file.
	 */
	private List<Icd9Procedure> icd9Procedures;
	/**
	 * The list of medications parsed from the data file.
	 */
	private List<Medication> medications;
	/**
	 * The list of labs parsed from the data file.
	 */
	private List<Lab> labs;
	/**
	 * The list of vitals parsed from the data file.
	 */
	private List<Vital> vitals;
	private final ResourceBundle messages;
	
	private final File dataFile;

	/**
	 * Create the data provider from the given data file.
	 *
	 * @param inDataFile The Excel workbook file to use as the data store.
	 * @throws DataProviderException Thrown when the workbook can not be
	 * accessed, or parsed correctly.
	 */
	public XlsxDataProvider(File inDataFile, Locale locale) throws DataProviderException {
		if (inDataFile == null) {
			throw new IllegalArgumentException("inDataFile cannot be null");
		}

		this.logErrorMessage = new MessageFormat(MessageFormat.format(
				"Spreadsheet {0} is missing required sheet",
				new Object[]{inDataFile.getAbsolutePath()})
				+ " {0}");
		this.exceptionErrorMessage = new MessageFormat(
				"Required worksheet {0} is missing");

		try {
			LOGGER.debug("Creating workbook from {}", inDataFile.getAbsolutePath());
			this.opcPackage = OPCPackage.open(inDataFile.getAbsolutePath());
			this.workbook = new XSSFWorkbook(this.opcPackage);
			this.validateWorksheets();
		} catch (InvalidFormatException ex) {
			throw new DataProviderException("Invalid XLSX file", ex);
		} catch (IOException ioe) {
			throw new DataProviderException("Error reading XLSX file", ioe);
		} catch (InvalidOperationException ioe) {
			throw new DataProviderException("Invalid XLSX file", ioe);
		}
		this.messages = 
				locale != null 
				? ResourceBundle.getBundle("Messages", locale) 
				: ResourceBundle.getBundle("Messages");
		this.dataFile = inDataFile;
	}

	/**
	 * Make sure that all the necessary worksheets are present in the given
	 * workbook.
	 *
	 * @throws DataProviderException Thrown if there are missing worksheets in
	 * the given workbook.
	 */
	private void validateWorksheets() throws DataProviderException {
		String[] requiredSheetNames = new String[]{"patient", "provider",
			"encounter"};
		for (String sheetName : requiredSheetNames) {
			XSSFSheet sheet = this.workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new DataProviderException(
						this.exceptionErrorMessage.format(sheetName));
			}
		}
	}

	public File getDataFile() {
		return dataFile;
	}
	
	

	@Override
	public List<Patient> getPatients() throws DataProviderException {
		if (this.patients == null) {
			this.patients = this.readPatients();
		}
		return this.patients;
	}

	@Override
	public List<Provider> getProviders() throws DataProviderException {
		if (this.providers == null) {
			this.providers = this.readProviders();
		}
		return this.providers;
	}

	@Override
	public List<Encounter> getEncounters() throws DataProviderException {
		if (this.encounters == null) {
			this.encounters = this.readEncounters();
		}
		return this.encounters;
	}

	@Override
	public List<CPT> getCptCodes() throws DataProviderException {
		if (this.cpts == null) {
			this.cpts = this.readCpts();
		}
		return this.cpts;
	}

	@Override
	public List<Icd9Diagnosis> getIcd9Diagnoses() throws DataProviderException {
		if (this.icd9Diagnoses == null) {
			this.icd9Diagnoses = this.readIcd9Diagnoses();
		}
		return this.icd9Diagnoses;
	}

	@Override
	public List<Icd9Procedure> getIcd9Procedures() throws DataProviderException {
		if (this.icd9Procedures == null) {
			this.icd9Procedures = this.readIcd9Procedures();
		}
		return this.icd9Procedures;
	}

	@Override
	public List<Medication> getMedications() throws DataProviderException {
		if (this.medications == null) {
			this.medications = this.readMedications();
		}
		return this.medications;
	}

	@Override
	public List<Lab> getLabs() throws DataProviderException {
		if (this.labs == null) {
			this.labs = this.readLabs();
		}
		return this.labs;
	}

	@Override
	public List<Vital> getVitals() throws DataProviderException {
		if (this.vitals == null) {
			this.vitals = this.readVitals();
		}
		return this.vitals;
	}

	@Override
	public void close() throws IOException {
		if (this.opcPackage != null) {
			this.opcPackage.close();
		}
	}

	/**
	 * Convenience method to read a required sheet and throw an exception if the
	 * sheet is not present.
	 *
	 * @param sheetName the sheet's name.
	 * @return the sheet, guaranteed not <code>null</code>.
	 * @throws DataProviderException if the sheet is not present.
	 */
	private XSSFSheet readRequiredSheet(String sheetName)
			throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet(sheetName);
		if (sheet == null) {
			LOGGER.error(this.logErrorMessage.format(sheetName));
			throw new DataProviderException(
					this.exceptionErrorMessage.format(sheetName));
		}
		return sheet;
	}

	/**
	 * Parse the list of patients from the workbook.
	 *
	 * @return A list of {@link Patient} objects.
	 */
	private List<Patient> readPatients() throws DataProviderException {
		XSSFSheet sheet = readRequiredSheet("patient");
		String sheetName = sheet.getSheetName();
		List<Patient> result = new ArrayList<Patient>();
		Iterator<Row> rows = sheet.rowIterator();
		rows.next(); // skip header row
		while (rows.hasNext()) {
			Row row = rows.next();
			Patient patient = new Patient();
			patient.setId(readLongValue(sheetName, row.getCell(0)));
			patient.setFirstName(readStringValue(sheetName, row.getCell(1)));
			patient.setLastName(readStringValue(sheetName, row.getCell(2)));
			patient.setDateOfBirth(readDateValue(sheetName, row.getCell(3)));
			patient.setLanguage(readStringValue(sheetName, row.getCell(4)));
			patient.setMaritalStatus(readStringValue(sheetName, row.getCell(5)));
			patient.setRace(readStringValue(sheetName, row.getCell(6)));
			patient.setGender(readStringValue(sheetName, row.getCell(7)));
			result.add(patient);
		}
		return result;
	}

	/**
	 * Parse the list of providers in the workbook.
	 *
	 * @return A list of {@link Provider} objects.
	 */
	private List<Provider> readProviders() throws DataProviderException {
		XSSFSheet sheet = readRequiredSheet("provider");
		String sheetName = sheet.getSheetName();
		List<Provider> result = new ArrayList<Provider>();
		Iterator<Row> rows = sheet.rowIterator();
		rows.next(); // skip header row
		while (rows.hasNext()) {
			Row row = rows.next();
			Provider provider = new Provider();
			provider.setId(readLongValue(sheetName, row.getCell(0)));
			provider.setFirstName(readStringValue(sheetName, row.getCell(1)));
			provider.setLastName(readStringValue(sheetName, row.getCell(2)));
			result.add(provider);
		}
		return result;
	}

	/**
	 * Parse the list of encounters in the workbook.
	 *
	 * @return A list of {@link Encounter} objects.
	 */
	private List<Encounter> readEncounters() throws DataProviderException {
		XSSFSheet sheet = readRequiredSheet("encounter");
		String sheetName = sheet.getSheetName();
		List<Encounter> result = new ArrayList<Encounter>();
		Iterator<Row> rows = sheet.rowIterator();
		LOGGER.debug("Encounter iterator: {}", Integer.valueOf(rows.hashCode()));
		rows.next(); // skip header row
		while (rows.hasNext()) {
			Row row = rows.next();
			Encounter encounter = new Encounter();
			encounter.setId(readLongValue(sheetName, row.getCell(0)));
			encounter.setPatientId(readLongValue(sheetName, row.getCell(1)));
			encounter.setProviderId(readLongValue(sheetName, row.getCell(2)));
			encounter.setStart(readDateValue(sheetName, row.getCell(3)));
			encounter.setEnd(readDateValue(sheetName, row.getCell(4)));
			encounter.setType(readStringValue(sheetName, row.getCell(5)));
			encounter.setDischargeDisposition(readStringValue(sheetName, row.getCell(6)));
			result.add(encounter);
		}
		return result;
	}

	/**
	 * Parse the list of CPT codes in the workbook.
	 *
	 * @return A list of {@link CPT} objects.
	 */
	private List<CPT> readCpts() throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet("eCPT");
		List<CPT> result = new ArrayList<CPT>();
		if (sheet != null) {
			String sheetName = sheet.getSheetName();
			Iterator<Row> rows = sheet.rowIterator();
			rows.next(); // skip header row
			while (rows.hasNext()) {
				Row row = rows.next();
				CPT cpt = new CPT();
				cpt.setId(readStringValue(sheetName, row.getCell(0)));
				cpt.setEncounterId(readLongValue(sheetName, row.getCell(1)));
				cpt.setTimestamp(readDateValue(sheetName, row.getCell(2)));
				cpt.setEntityId(readStringValue(sheetName, row.getCell(3)));
				result.add(cpt);
			}
		}
		return result;
	}

	/**
	 * Parse the list of ICD9 Diagnostic codes present in the workbook.
	 *
	 * @return A list of {@link Icd9Diagnosis} objects.
	 */
	private List<Icd9Diagnosis> readIcd9Diagnoses() throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet("eICD9D");
		List<Icd9Diagnosis> result = new ArrayList<Icd9Diagnosis>();
		if (sheet != null) {
			String sheetName = sheet.getSheetName();
			Iterator<Row> rows = sheet.rowIterator();
			rows.next(); // skip header row
			while (rows.hasNext()) {
				Row row = rows.next();
				Icd9Diagnosis diagnosis = new Icd9Diagnosis();
				diagnosis.setId(readStringValue(sheetName, row.getCell(0)));
				diagnosis.setEncounterId(readLongValue(sheetName, row.getCell(1)));
				diagnosis.setTimestamp(readDateValue(sheetName, row
						.getCell(2)));
				diagnosis.setEntityId(readStringValue(sheetName, row
						.getCell(3)));
				result.add(diagnosis);
			}
		}
		return result;
	}

	/**
	 * Parse the list of ICD9 Procedure codes present in the workbook.
	 *
	 * @return A list of {@link Icd9Procedure} objects.
	 */
	private List<Icd9Procedure> readIcd9Procedures() throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet("eICD9P");
		List<Icd9Procedure> result = new ArrayList<Icd9Procedure>();
		if (sheet != null) {
			String sheetName = sheet.getSheetName();
			Iterator<Row> rows = sheet.rowIterator();
			rows.next(); // skip header row
			while (rows.hasNext()) {
				Row row = rows.next();
				Icd9Procedure procedure = new Icd9Procedure();
				procedure.setId(readStringValue(sheetName, row.getCell(0)));
				procedure.setEncounterId(readLongValue(sheetName, row.getCell(1)));
				procedure.setTimestamp(readDateValue(sheetName, row
						.getCell(2)));
				procedure.setEntityId(readStringValue(sheetName, row
						.getCell(3)));
				result.add(procedure);
			}
		}
		return result;
	}

	/**
	 * Parse the list of medications present in the workbook.
	 *
	 * @return A list of {@link Medication} objects.
	 */
	private List<Medication> readMedications() throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet("eMEDS");
		List<Medication> result = new ArrayList<Medication>();
		if (sheet != null) {
			String sheetName = sheet.getSheetName();
			Iterator<Row> rows = sheet.rowIterator();
			rows.next(); // skip header row
			while (rows.hasNext()) {
				Row row = rows.next();
				Medication medication = new Medication();
				medication.setId(readStringValue(sheetName, row.getCell(0)));
				medication.setEncounterId(readLongValue(sheetName, row
						.getCell(1)));
				medication.setTimestamp(readDateValue(sheetName, row
						.getCell(2)));
				medication.setEntityId(readStringValue(sheetName, row
						.getCell(3)));
				result.add(medication);
			}
		}
		return result;
	}

	/**
	 * Parse the list of labs present in the workbook's "eLABS" worksheet.
	 *
	 * @return A list of {@link Lab} objects.
	 */
	private List<Lab> readLabs() throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet("eLABS");
		List<Lab> result = new ArrayList<Lab>();
		if (sheet != null) {
			String sheetName = sheet.getSheetName();
			Iterator<Row> rows = sheet.rowIterator();
			rows.next(); // skip header row
			while (rows.hasNext()) {
				Row row = rows.next();
				Lab lab = new Lab();
				lab.setId(readStringValue(sheetName, row.getCell(0)));
				lab.setEncounterId(readLongValue(sheetName, row.getCell(1)));
				lab.setTimestamp(readDateValue(sheetName, row.getCell(2)));
				lab.setEntityId(readStringValue(sheetName, row.getCell(3)));
				lab.setResultAsStr(readStringValue(sheetName, row.getCell(4)));
				lab.setResultAsNum(readDoubleValue(sheetName, row.getCell(5)));
				lab.setUnits(readStringValue(sheetName, row.getCell(6)));
				lab.setFlag(readStringValue(sheetName, row.getCell(7)));
				result.add(lab);
			}
		}
		return result;
	}

	/**
	 * Parse the list of vitals present in the workbook's "eVITALS" worksheet.
	 *
	 * @return A list of {@link Vital} objects.
	 */
	private List<Vital> readVitals() throws DataProviderException {
		XSSFSheet sheet = this.workbook.getSheet("eVITALS");
		List<Vital> result = new ArrayList<Vital>();
		if (sheet != null) {
			String sheetName = sheet.getSheetName();
			Iterator<Row> rows = sheet.rowIterator();
			rows.next(); // skip header row
			while (rows.hasNext()) {
				Row row = rows.next();
				Vital vital = new Vital();
				vital.setId(readStringValue(sheetName, row.getCell(0)));
				vital.setEncounterId(readLongValue(sheetName, row.getCell(1)));
				vital.setTimestamp(readDateValue(sheetName, row.getCell(2)));
				vital.setEntityId(readStringValue(sheetName, row.getCell(3)));
				vital.setResultAsStr(readStringValue(sheetName, row.getCell(4)));
				vital.setResultAsNum(readDoubleValue(sheetName, row.getCell(5)));
				vital.setUnits(readStringValue(sheetName, row.getCell(6)));
				vital.setFlag(readStringValue(sheetName, row.getCell(7)));
				result.add(vital);
			}
		}
		return result;
	}

	/**
	 * Read a date value from the given spreadsheet cell.
	 *
	 * @param cell The cell to read the value from.
	 * @return The date in the cell, if valid, null otherwise.
	 */
	private Date readDateValue(String sheetName, Cell cell) 
			throws DataProviderException {
		Date result = null;
		String value = readStringValue(sheetName, cell);
		if (value != null) {
			try {
				result = dateFormat.get().parse(value);
			} catch (ParseException e) {
				throwException(sheetName, cell, e.getMessage(), e);
			}
		}
		return result;
	}

	/**
	 * Read a string value from the given cell.
	 *
	 * @param cell The cell to read value from.
	 * @return A String containing the cell value, if valid, null otherwise.
	 */
	private String readStringValue(String sheetName, Cell cell)
			throws DataProviderException {
		String result = null;
		if (cell != null) {
			try {
				int cellType = cell.getCellType();
				if (cellType == Cell.CELL_TYPE_STRING) {
					result = cell.getStringCellValue();
				} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
					result = Double.toString(cell.getNumericCellValue());
				} else if (cellType == Cell.CELL_TYPE_BLANK) {
					result = null;
				} else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
					result = Boolean.toString(cell.getBooleanCellValue());
				} else {
					throwException(sheetName, cell,
							"Cell type must be a number, string, boolean or blank");
				}
			} catch (Exception e) {
				throwException(sheetName, cell, e.getMessage(), e);
			}
		}
		return result;
	}

	private void throwException(String sheetName, Cell cell, 
			String problemDescription) throws DataProviderException {
		throwException(sheetName, cell, problemDescription, null);
	}

	private void throwException(String sheetName, Cell cell, 
			String problemDescription, Exception cause) 
			throws DataProviderException {
		String msgTemplate =
				messages.getString("xlsxDataProvider.error.parsing");
		CellReference cellRef =
				new CellReference(cell.getRowIndex(),
				cell.getColumnIndex());
		String msg = MessageFormat.format(msgTemplate, sheetName, 
				cellRef.formatAsString(), problemDescription);
		throw new DataProviderException(msg, cause);
	}

	/**
	 * Read a numerical value as a Long type from the given cell.
	 *
	 * @param cell The cell to read the value from.
	 * @return A Long containing the cell's value, if valid, null otherwise.
	 */
	private Long readLongValue(String sheetName, Cell cell) 
			throws DataProviderException {
		Long result = null;
		if (cell != null) {
			try {
				Double value = new Double(cell.getNumericCellValue());
				result = Long.valueOf(value.longValue());
			} catch (Exception e) {
				throwException(sheetName, cell, e.getMessage(), e);
			}
		}
		return result;
	}

	/**
	 * Read the give cell's value as a Double type.
	 *
	 * @param cell The cell to read the value from.
	 * @return A Double containing the cell value, if valid, null otherwise.
	 */
	private Double readDoubleValue(String sheetName, Cell cell) 
			throws DataProviderException {
		Double result = null;
		if (cell != null) {
			try {
				double value = cell.getNumericCellValue();
				result = Double.valueOf(value);
			} catch (Exception e) {
				throwException(sheetName, cell, e.getMessage(), e);
			}
		}
		return result;
	}
}
