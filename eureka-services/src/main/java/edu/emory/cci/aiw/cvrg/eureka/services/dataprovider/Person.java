package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

/**
 * A person related to an encounter; for example, a patient or a provider.
 * 
 * @author hrathod
 * 
 */
public abstract class Person {
	/**
	 * The unique identifier for the person.
	 */
	private Long id;
	/**
	 * Person's first name.
	 */
	private String firstName;
	/**
	 * Person's last name.
	 */
	private String lastName;

	/**
	 * Get the person's unique identifier.
	 * 
	 * @return The person's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the person's unique identifier.
	 * 
	 * @param inId The person's unique identifier.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the person's first name.
	 * 
	 * @return The person's first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set the person's first name.
	 * 
	 * @param inFirstName The person's first name.
	 */
	public void setFirstName(String inFirstName) {
		this.firstName = inFirstName;
	}

	/**
	 * Get the person's last name.
	 * 
	 * @return The person's last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set the person's last name.
	 * 
	 * @param inLastName The person's last name.
	 */
	public void setLastName(String inLastName) {
		this.lastName = inLastName;
	}
}
