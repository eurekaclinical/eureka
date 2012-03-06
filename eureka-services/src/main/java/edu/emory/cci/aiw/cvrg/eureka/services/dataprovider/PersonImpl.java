package edu.emory.cci.aiw.cvrg.eureka.services.dataprovider;

/**
 * A person related to an encounter; for example, a patient or a provider.
 * 
 * @author hrathod
 * 
 */
abstract class PersonImpl implements Person {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#getId()
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#setId(java
	 * .lang.Long)
	 */
	@Override
	public void setId(Long inId) {
		this.id = inId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return this.firstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#setFirstName
	 * (java.lang.String)
	 */
	@Override
	public void setFirstName(String inFirstName) {
		this.firstName = inFirstName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#getLastName()
	 */
	@Override
	public String getLastName() {
		return this.lastName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.emory.cci.aiw.cvrg.eureka.services.dataprovider.Person#setLastName
	 * (java.lang.String)
	 */
	@Override
	public void setLastName(String inLastName) {
		this.lastName = inLastName;
	}
}
