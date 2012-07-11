/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.bind.CycleRecoverable;

/**
 * Holds information about a user-defined ontological concept.
 *
 * @author hrathod
 */
@XmlRootElement
@Entity
@Table(name = "propositions")
public class Proposition implements CycleRecoverable, Serializable {

	/**
	 * Needed for the Serializable implementation.
	 */
	private static final long serialVersionUID = -4972778121056149836L;
	/**
	 * The unique identifier for the Proposition.
	 */
	@Id
	@SequenceGenerator(name = "PROP_SEQ_GENERATOR", sequenceName = "PROP_SEQ",
					   allocationSize = 1)
	@GeneratedValue(generator = "PROP_SEQ_GENERATOR")
	private Long id;
	/**
	 * The user to which this proposition belongs.
	 */
	@ManyToOne(
	cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
			   targetEntity = User.class)
	@JoinColumn(name = "user_id")
	private User user;
	/**
	 * The propositions that the current proposition is abstracted from.
	 */
	@ManyToMany(
	cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
	@JoinTable(name = "abstracted_from", joinColumns = {
		@JoinColumn(name = "target_proposition_id")})
	private List<Proposition> abstractedFrom;
	/**
	 * The "children" of of this proposition.
	 */
	@ManyToMany(
	cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
	@JoinTable(name = "inverse_is_a", joinColumns = {
		@JoinColumn(name = "target_proposition_id")})
	private List<Proposition> inverseIsA;
	/**
	 * The temporal pattern related to the proposition.
	 */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "proposition_id")
	private TemporalPattern temporalPattern;
	/**
	 * The display name for the proposition.
	 */
	private String displayName;
	/**
	 * The abbreviated display name for the proposition.
	 */
	private String abbrevDisplayName;
	/**
	 * The proposition type.
	 */
	private String type;

	/**
	 * Gets the abbreviated display name of the proposition.
	 *
	 * @return The abbreviated display name of the proposition.
	 */
	public String getAbbrevDisplayName() {
		return abbrevDisplayName;
	}

	/**
	 * Sets the abbreviated display name.
	 *
	 * @param inAbbrevDisplayName The abbreviated display name to set.
	 */
	public void setAbbrevDisplayName(String inAbbrevDisplayName) {
		this.abbrevDisplayName = inAbbrevDisplayName;
	}

	/**
	 * Gets the display name of the proposition.
	 *
	 * @return The display name of the proposition.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of the proposition.
	 *
	 * @param inDisplayName The display name of the proposition.
	 */
	public void setDisplayName(String inDisplayName) {
		this.displayName = inDisplayName;
	}

	/**
	 * Gets the proposition type.
	 *
	 * @return The proposition type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the proposition type.
	 *
	 * @param inType The proposition type.
	 */
	public void setType(String inType) {
		this.type = inType;
	}

	/**
	 * Gets the user to which this proposition belongs.
	 *
	 * @return The user to which this proposition belongs.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user to which this proposition belongs.
	 *
	 * @param inUser The user to which this proposition belongs.
	 */
	public void setUser(User inUser) {
		this.user = inUser;
	}

	/**
	 * Gets the list of propositions the current proposition is abstracted from.
	 *
	 * @return The list of propositions the current proposition is abstracted
	 * from.
	 */
	public List<Proposition> getAbstractedFrom() {
		return abstractedFrom;
	}

	/**
	 * Sets the list of propositions the current proposition is abstracted from.
	 *
	 * @param abstractedFrom The list of propositions the current proposition is
	 * abstracted from.
	 */
	public void setAbstractedFrom(List<Proposition> abstractedFrom) {
		this.abstractedFrom = abstractedFrom;
	}

	/**
	 * Gets the list of
	 * @return
	 */
	public List<Proposition> getInverseIsA() {
		return inverseIsA;
	}

	public void setInverseIsA(List<Proposition> inverseIsA) {
		this.inverseIsA = inverseIsA;
	}

	public TemporalPattern getTemporalPattern() {
		return temporalPattern;
	}

	public void setTemporalPattern(TemporalPattern temporalPattern) {
		this.temporalPattern = temporalPattern;
	}

	/**
	 * Gets the unique identifier for the proposition.
	 *
	 * @return The unique identifier for the proposition.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the proposition.
	 *
	 * @param inId The unique identifier for the proposition.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	@Override
	public Object onCycleDetected(Context context) {
		return null;
	}
}
