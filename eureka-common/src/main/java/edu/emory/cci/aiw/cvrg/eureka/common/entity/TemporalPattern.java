package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.bind.CycleRecoverable;

/**
 * Holds information about a temporal pattern, attached to a Proposition.
 *
 * @author hrathod
 */
@XmlRootElement
@Entity
@Table(name = "temporal_pattern")
public class TemporalPattern implements CycleRecoverable, Serializable {

	/**
	 * Needed for serialization.
	 */
	private static final long serialVersionUID = 4169533799638095483L;
	/**
	 * The unique identifier for the temporal pattern.
	 */
	@Id
	@SequenceGenerator(name = "TEMP_PAT_GEN", sequenceName = "temp_pat_seq",
					   allocationSize = 1)
	@GeneratedValue(generator = "TEMP_PAT_GEN",
					strategy = GenerationType.SEQUENCE)
	private Long id;
	/**
	 * Is the temporal pattern "solid".
	 */
	private boolean solid;
	/**
	 * Is the temporal pattern "concatenable".
	 */
	private boolean concatenable;

	/**
	 * Gets the concatenable property of the temporal pattern.
	 *
	 * @return The concatenable property of the temporal pattern.
	 */
	public boolean isConcatenable() {
		return concatenable;
	}

	/**
	 * Sets the concatenable property of the temporal pattern.
	 *
	 * @param concatenable The concatenable property of the temporal pattern.
	 */
	public void setConcatenable(boolean concatenable) {
		this.concatenable = concatenable;
	}

	/**
	 * Gets the solid property of the temporal pattern.
	 *
	 * @return The solid property of the temporal pattern.
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Sets the solid property of the temporal pattern.
	 *
	 * @param solid The solid property of the temporal pattern.
	 */
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	/**
	 * Gets the unique identifier for the temporal pattern.
	 *
	 * @return The unique identifier for the temporal pattern.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the unique identifier for the temporal pattern.
	 *
	 * @param id The unique identifier for the temporal pattern.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Object onCycleDetected(Context context) {
		return null;
	}
}
