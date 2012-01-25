package edu.emory.cci.aiw.cvrg.eureka.services.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A bean holding information about a user's registration request.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "user_requests")
public class UserRequest {
	/**
	 * The request's unique identifier.
	 */
	@Id
	@GeneratedValue
	private Long id;
	/**
	 * The user's first name.
	 */
	private String firstName;
	/**
	 * The user's last name.
	 */
	private String lastName;
	/**
	 * The user's email address.
	 */
	private String email;
	/**
	 * The user's email address verification.
	 */
	private String verifyEmail;
	/**
	 * The user's organization.
	 */
	private String organization;
	/**
	 * The user's password.
	 */
	private String password;
	/**
	 * The user's password verification.
	 */
	private String verifyPassword;

	/**
	 * Get the request's unique identifier.
	 * 
	 * @return The request's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the user's unique identifier.
	 * 
	 * @param inId The unique identifier for the request.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the user's first name.
	 * 
	 * @return user's first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set the user's first name.
	 * 
	 * @param inFirstName the user's first name.
	 */
	public void setFirstName(String inFirstName) {
		this.firstName = inFirstName;
	}

	/**
	 * Get the user's last name.
	 * 
	 * @return The user's last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set the user's last name.
	 * 
	 * @param inLastName The user's last name.
	 */
	public void setLastName(String inLastName) {
		this.lastName = inLastName;
	}

	/**
	 * Get the user's email address.
	 * 
	 * @return The user's email address.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the user's email address.
	 * 
	 * @param inEmail The user's email address.
	 */
	public void setEmail(String inEmail) {
		this.email = inEmail;
	}

	/**
	 * Set the user's email address verification.
	 * 
	 * @return The user's email address verification.
	 */
	public String getVerifyEmail() {
		return this.verifyEmail;
	}

	/**
	 * Set the user's email address verification.
	 * 
	 * @param inVerifyEmail The user's email address verification.
	 */
	public void setVerifyEmail(String inVerifyEmail) {
		this.verifyEmail = inVerifyEmail;
	}

	/**
	 * Get the user's organization.
	 * 
	 * @return The user's organization.
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * Set the user's organization.
	 * 
	 * @param inOrganization The user's organization.
	 */
	public void setOrganization(String inOrganization) {
		this.organization = inOrganization;
	}

	/**
	 * Get the user's password.
	 * 
	 * @return The user's password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the user's password.
	 * 
	 * @param inPassword The user's password.
	 */
	public void setPassword(String inPassword) {
		this.password = inPassword;
	}

	/**
	 * Get the password verification.
	 * 
	 * @return The password verification.
	 */
	public String getVerifyPassword() {
		return this.verifyPassword;
	}

	/**
	 * Set the password verification.
	 * 
	 * @param inVerifyPassword The password verification.
	 */
	public void setVerifyPassword(String inVerifyPassword) {
		this.verifyPassword = inVerifyPassword;
	}
}
