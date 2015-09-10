package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import java.io.UnsupportedEncodingException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "DEID_PER_PATIENT_PARAMS")
public class DeidPerPatientParams {
	
	@Id
	@SequenceGenerator(name = "DEID_PER_PT_PARAMS_SEQ_GEN", sequenceName = "DEID_PER_PT_PARAMS_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "DEID_PER_PT_PARAMS_SEQ_GEN")
	private Long id;
	
	@Column(nullable = false)
	private String keyId;
	
	@Column(name = "DTOFFSET")
	private Integer offset;
	
	private String cipherKey;
	
	private String salt;
	
	@ManyToOne
	@JoinColumn(name="DESTINATIONS_ID")
	private DestinationEntity destination;

	public DeidPerPatientParams() {
	}

	public DeidPerPatientParams(DestinationEntity destination, String keyId, int offset) {
		this.destination = destination;
		this.keyId = keyId;
		this.offset = offset;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DestinationEntity getDestination() {
		return destination;
	}

	public void setDestination(DestinationEntity destination) {
		this.destination = destination;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getCipherKey() {
		return cipherKey;
	}

	public void setCipherKey(String cipherKey) {
		this.cipherKey = cipherKey;
	}
	
	public void setSalt(byte[] salt) {
		if (salt != null) {
			try {
				this.salt = new String(salt, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new AssertionError("UTF-8 character set not supported");
			}
		} else {
			this.salt = null;
		}
	}
	
	public byte[] getSalt() {
		if (this.salt == null) {
			return null;
		} else {
			try {
				return this.salt.getBytes("UTF-8");
			} catch (UnsupportedEncodingException ex) {
				throw new AssertionError("UTF-8 character set not supported");
			}
		}
	}
	
}
