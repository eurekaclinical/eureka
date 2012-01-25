package edu.emory.cci.aiw.cvrg.eureka.services.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.bind.CycleRecoverable;

/**
 * A bean class to hold information about users in the system.
 * 
 * @author hrathod
 * 
 */
@Entity
@XmlRootElement
@Table(name = "users")
public class User implements CycleRecoverable {

	/**
	 * The user's unique identifier.
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
	 * The user's organization.
	 */
	private String organization;
	/**
	 * The user's password.
	 */
	private String password;
	/**
	 * A list of roles assigned to the user.
	 */
	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Role.class)
	@JoinTable(name = "user_role",
			joinColumns = { @JoinColumn(name = "userId") },
			inverseJoinColumns = { @JoinColumn(name = "roleId") })
	private List<Role> roles;

	/**
	 * Create an empty User object.
	 */
	public User() {
		super();
	}

	/**
	 * Get the user's unique identifier.
	 * 
	 * @return A {@link Long} representing the user's unique identifier.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the uesr's unique identifier.
	 * 
	 * @param inId A {@link Long} representing the user's unique identifier.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the user's first name.
	 * 
	 * @return A String containing the user's first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set the user's first name.
	 * 
	 * @param inFirstName A String containing the user's first name.
	 */
	public void setFirstName(String inFirstName) {
		this.firstName = inFirstName;
	}

	/**
	 * Get the user's last name.
	 * 
	 * @return A String containing the user's last name.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set the user's last name.
	 * 
	 * @param inLastName A String containing the user's last name.
	 */
	public void setLastName(String inLastName) {
		this.lastName = inLastName;
	}

	/**
	 * Get the user's email address.
	 * 
	 * @return A String containing the user's email address.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the user's email address.
	 * 
	 * @param inEmail A String containing the user's email address.
	 */
	public void setEmail(String inEmail) {
		this.email = inEmail;
	}

	/**
	 * Get the user's organization.
	 * 
	 * @return A String containing the user's organization.
	 */
	public String getOrganization() {
		return this.organization;
	}

	/**
	 * Set the user's organization.
	 * 
	 * @param inOrganization A String containing the user's organization.
	 */
	public void setOrganization(String inOrganization) {
		this.organization = inOrganization;
	}

	/**
	 * Get the user's password
	 * 
	 * @return A String containing the user's password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Set the user's password.
	 * 
	 * @param inPassword A String containing the user's password.
	 */
	public void setPassword(String inPassword) {
		this.password = inPassword;
	}

	/**
	 * Get all the roles assigned to the user.
	 * 
	 * @return A list of {@link Role} objects.
	 */
	public List<Role> getRoles() {
		return this.roles;
	}

	/**
	 * Set the roles assigned to the current user.
	 * 
	 * @param inRoles A Set of roles to be assigned to the user.
	 */
	public void setRoles(List<Role> inRoles) {
		this.roles = inRoles;
	}

	@Override
	public Object onCycleDetected(Context context) {
		return null;
	}

}
