package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;

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
	 * Is the user activate?
	 */
	private boolean active;
	/**
	 * Is the user verified?
	 */
	private boolean verified;
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
	 * The last log-in date for the user.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	/**
	 * A list of roles assigned to the user.
	 */
	@ManyToMany(cascade = CascadeType.ALL, targetEntity = Role.class)
	@JoinTable(name = "user_role",
			joinColumns = { @JoinColumn(name = "userId") },
			inverseJoinColumns = { @JoinColumn(name = "roleId") })
	private List<Role> roles = new ArrayList<Role>();
	/**
	 * The file uploads belonging to the user.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = FileUpload.class,
			mappedBy = "user")
	private List<FileUpload> fileUploads = new ArrayList<FileUpload>();

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
	public void setId(final Long inId) {
		this.id = inId;
	}

	/**
	 * Get whether the user is active or not.
	 * 
	 * @return True if the user is active, false otherwise.
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * Set whether the user is active or not.
	 * 
	 * @param inActive True if the user should be active, false otherwise.
	 */
	public void setActive(final boolean inActive) {
		this.active = inActive;
	}

	/**
	 * Get whether the user has been verified.
	 * 
	 * @return True if the user has been verified, false otherwise.
	 */
	public boolean isVerified() {
		return this.verified;
	}

	/**
	 * Set whether the user has been verified.
	 * 
	 * @param inVerified True if the user has been verified, false otherwise.
	 */
	public void setVerified(final boolean inVerified) {
		this.verified = inVerified;
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
	public void setFirstName(final String inFirstName) {
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
	public void setLastName(final String inLastName) {
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
	public void setEmail(final String inEmail) {
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
	public void setOrganization(final String inOrganization) {
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
	public void setPassword(final String inPassword) {
		this.password = inPassword;
	}

	/**
	 * Get the last log-in date for the user.
	 * 
	 * @return The user's last log-in date.
	 */
	public Date getLastLogin() {
		return this.lastLogin;
	}

	/**
	 * Set the user's last log-in date.
	 * 
	 * @param inLastLogin The last log-in date for the user.
	 */
	public void setLastLogin(final Date inLastLogin) {
		this.lastLogin = inLastLogin;
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
	public void setRoles(final List<Role> inRoles) {
		this.roles = inRoles;
	}

	/**
	 * Get the list of files uploaded by the user.
	 * 
	 * @return The list of files uploaded by the user.
	 */
	@JsonBackReference("user-fileuploads")
	public List<FileUpload> getFileUploads() {
		return this.fileUploads;
	}

	/**
	 * Set the list of files uploaded by the user.
	 * 
	 * @param inFileUploads The list of files uploaded by the user.
	 */
	public void setFileUploads(List<FileUpload> inFileUploads) {
		this.fileUploads = inFileUploads;
	}

	/**
	 * Add a new file upload for the user.
	 * 
	 * @param fileUpload The file upload to add to the list of file uploads for
	 *            the user.
	 */
	public void addFileUpload(FileUpload fileUpload) {
		User user = fileUpload.getUser();
		if (user == null || user.getId() != this.getId()) {
			fileUpload.setUser(this);
		}
		this.fileUploads.add(fileUpload);
	}

	@Override
	public Object onCycleDetected(final Context context) {
		return null;
	}

}
