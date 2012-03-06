package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

/**
 * A person related to an encounter, such as a provider or a patient.
 * 
 * @author hrathod
 * 
 */
public interface Person {

	/**
	 * Get the person's unique identifier.
	 * 
	 * @return The person's unique identifier.
	 */
	public abstract Long getId();

	/**
	 * Set the person's unique identifier.
	 * 
	 * @param inId The person's unique identifier.
	 */
	public abstract void setId(Long inId);

	/**
	 * Get the person's first name.
	 * 
	 * @return The person's first name.
	 */
	public abstract String getFirstName();

	/**
	 * Set the person's first name.
	 * 
	 * @param inFirstName The person's first name.
	 */
	public abstract void setFirstName(String inFirstName);

	/**
	 * Get the person's last name.
	 * 
	 * @return The person's last name.
	 */
	public abstract String getLastName();

	/**
	 * Set the person's last name.
	 * 
	 * @param inLastName The person's last name.
	 */
	public abstract void setLastName(String inLastName);

}