package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonBackReference;

import com.sun.xml.bind.CycleRecoverable;

/**
 * A bean class to hold information related to roles in the system.
 * 
 * @author hrathod
 * 
 */
@Entity
@XmlRootElement
@Table(name = "roles")
public class Role implements CycleRecoverable {

	/**
	 * The role's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "ROLE_SEQ_GENERATOR", sequenceName = "ROLE_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "ROLE_SEQ_GENERATOR")
	private Long id;
	/**
	 * The role's name.
	 */
	private String name;
	/**
	 * A set of users to have this role assigned.
	 */
	@ManyToMany(cascade = CascadeType.ALL, targetEntity = User.class)
	@JoinTable(name = "user_role",
			joinColumns = { @JoinColumn(name = "roleId") },
			inverseJoinColumns = { @JoinColumn(name = "userId") })
	private List<User> users;
	/**
	 * Is this role a default role? Default roles are assigned to all new users.
	 */
	private Boolean defaultRole;

	/**
	 * Create an empty role.
	 */
	public Role() {
		super();
	}

	/**
	 * Get the role's identification number.
	 * 
	 * @return A {@link Long} representing the role's id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the role's identification number.
	 * 
	 * @param inId The number representing the role's id.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the role's name.
	 * 
	 * @return A String containing the role's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the role's name.
	 * 
	 * @param inName A string containing the role's name.
	 */
	public void setName(String inName) {
		this.name = inName;
	}

	/**
	 * Is this role a default role?
	 * 
	 * @return True if the role is a default role, false otherwise.
	 */
	public Boolean isDefaultRole() {
		return this.defaultRole;
	}

	/**
	 * Set the role's default flag.
	 * 
	 * @param inDefaultRole True or False, True indicating a default role, False
	 *            indicating a non-default role.
	 */
	public void setDefaultRole(Boolean inDefaultRole) {
		this.defaultRole = inDefaultRole;
	}

	/**
	 * Get all the users who have the current role assigned.
	 * 
	 * @return A Set of {@link User} objects.
	 */
	@JsonBackReference("users-roles")
	public List<User> getUsers() {
		return this.users;
	}

	/**
	 * Set the users who have the current role assigned to them.
	 * 
	 * @param inUsers A Set of {@link User} objects.
	 */
	public void setUsers(List<User> inUsers) {
		this.users = inUsers;
	}

	@Override
	public Object onCycleDetected(Context context) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Role [id=").append(this.id).append(", name=")
				.append(this.name).append(", defaultRole=")
				.append(this.defaultRole).append("]");
		return builder.toString();
	}
}
