package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

import java.util.List;

/**
 * An interface to provide functionality needed to provide data for upload to a
 * PROTEMPA schema.
 * 
 * @author hrathod
 * 
 */
public interface DataProvider {
	/**
	 * Get a list of patients from the data.
	 * 
	 * @return A list of {@link Patient} objects.
	 */
	public List<Patient> getPatients();

	/**
	 * Get a list of providers from the data.
	 * 
	 * @return A list of {@link Provider} objects.
	 */
	public List<Provider> getProviders();

	/**
	 * Get a list of encounters from the data.
	 * 
	 * @return A list of {@link Encounter} objects.
	 */
	public List<Encounter> getEncounters();

	/**
	 * Get a list of CPT codes from the data.
	 * 
	 * @return A list of {@link CPT} objects.
	 */
	public List<CPT> getCptCodes();

	/**
	 * Get a list of ICD9 Diagnostic codes from the data.
	 * 
	 * @return A list of {@link Icd9Diagnosis} objects.
	 */
	public List<Icd9Diagnosis> getIcd9Diagnoses();

	/**
	 * Get a list of ICD9 Procedure codes from the data.
	 * 
	 * @return A list of {@link Icd9Procedure} objects.
	 */
	public List<Icd9Procedure> getIcd9Procedures();

	/**
	 * Get a list of medication from the data.
	 * 
	 * @return A list of {@link Medication} objects.
	 */
	public List<Medication> getMedications();

	/**
	 * Get a list of lab results from the data.
	 * 
	 * @return A list of {@link Lab} objects.
	 */
	public List<Lab> getLabs();

	/**
	 * Get a list of vitals from the data.
	 * 
	 * @return A list of {@link Vital} objects.
	 */
	public List<Vital> getVitals();
}
