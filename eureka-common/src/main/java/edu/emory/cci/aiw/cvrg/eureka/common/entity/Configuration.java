package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The configuration for a Protempa run, including the input data store
 * information, the output i2b2 data store information, ontology information,
 * and other related items.
 * 
 * @author hrathod
 * 
 */
@XmlRootElement
@Entity
@Table(name = "configurations")
public class Configuration {

	/**
	 * The unique identifier for the configuration.
	 */
	@Id
	@SequenceGenerator(name = "CONFIG_SEQ_GENERATOR",
			sequenceName = "CONFIG_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "CONFIG_SEQ_GENERATOR")
	private Long id;
	/**
	 * The unique identifier for the owner of this configuration.
	 */
	private Long userId;
	/**
	 * The ontology to use for this configuration.
	 */
	private String ontology;
	/**
	 * The Protempa input data source host name.
	 */
	private String protempaHost;
	/**
	 * The Protempa input data source port number.
	 */
	private Integer protempaPort;
	/**
	 * The Protempa input data source data base name.
	 */
	private String protempaDatabaseName;
	/**
	 * The Protempa input data source user name.
	 */
	private String protempaSchema;
	/**
	 * The Protempa input data source password.
	 */
	private String protempaPass;
	/**
	 * The i2b2 data source host name.
	 */
	private String i2b2Host;
	/**
	 * The i2b2 data source port number.
	 */
	private Integer i2b2Port;
	/**
	 * The i2b2 meta data schema name.
	 */
	private String i2b2MetaSchema;
	/**
	 * The i2b2 meta data schema password.
	 */
	private String i2b2MetaPass;
	/**
	 * The i2b2 data schema name.
	 */
	private String i2b2DataSchema;
	/**
	 * The i2b2 data schema password.
	 */
	private String i2b2DataPass;

	/**
	 * Get the unique identifier for the configuration.
	 * 
	 * @return The unique identifier for the configuration.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the unique identifier for the configuration.
	 * 
	 * @param inId The unique identifier for the configuration.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the unique identifier for the owner of the configuration.
	 * 
	 * @return The unique identifier for the owner of the configuration.
	 */
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Set the unique identifier for the owner of the configuration.
	 * 
	 * @param inUserId The unique identifier for the owner of the configuration.
	 */
	public void setUserId(Long inUserId) {
		this.userId = inUserId;
	}

	/**
	 * Get the name of the ontology to be used for the configuration.
	 * 
	 * @return The name of the ontology to be used for the configuration.
	 */
	public String getOntology() {
		return this.ontology;
	}

	/**
	 * Set the name of the ontology to be used for the configuration.
	 * 
	 * @param inOntology The name of the ontology to be used for the
	 *            configuration.
	 */
	public void setOntology(String inOntology) {
		this.ontology = inOntology;
	}

	/**
	 * Get the Protempa data source host name.
	 * 
	 * @return The Protempa data source host name.
	 */
	public String getProtempaHost() {
		return this.protempaHost;
	}

	/**
	 * Set the Protempa data source host name.
	 * 
	 * @param inProtempaHost The Protempa data source host name.
	 */
	public void setProtempaHost(String inProtempaHost) {
		this.protempaHost = inProtempaHost;
	}

	/**
	 * Get the Protempa data source port number.
	 * 
	 * @return The Protempa data source port number.
	 */
	public Integer getProtempaPort() {
		return this.protempaPort;
	}

	/**
	 * Set the Protempa data source port number.
	 * 
	 * @param inProtempaPort The Protempa data source port number.
	 */
	public void setProtempaPort(Integer inProtempaPort) {
		this.protempaPort = inProtempaPort;
	}

	/**
	 * @return the protempaDatabaseName
	 */
	public String getProtempaDatabaseName() {
		return this.protempaDatabaseName;
	}

	/**
	 * @param inProtempaDatabaseName the protempaDatabaseName to set
	 */
	public void setProtempaDatabaseName(String inProtempaDatabaseName) {
		this.protempaDatabaseName = inProtempaDatabaseName;
	}

	/**
	 * Get the Protempa data source schema name.
	 * 
	 * @return The Protempa data source schema name.
	 */
	public String getProtempaSchema() {
		return this.protempaSchema;
	}

	/**
	 * Set the Protempa data source schema name.
	 * 
	 * @param inProtempaSchema The Protempa data source schema name.
	 */
	public void setProtempaSchema(String inProtempaSchema) {
		this.protempaSchema = inProtempaSchema;
	}

	/**
	 * Get the Protempa data source password.
	 * 
	 * @return The Protempa data source password.
	 */
	public String getProtempaPass() {
		return this.protempaPass;
	}

	/**
	 * Set the Protempa data source password.
	 * 
	 * @param inProtempaPass The Protempa data source password.
	 */
	public void setProtempaPass(String inProtempaPass) {
		this.protempaPass = inProtempaPass;
	}

	/**
	 * Get the host name of the i2b2 data store.
	 * 
	 * @return The host name of the i2b2 data store.
	 */
	public String getI2b2Host() {
		return this.i2b2Host;
	}

	/**
	 * Set the host name of the i2b2 data store.
	 * 
	 * @param inI2b2Host The host name of the i2b2 data store.
	 */
	public void setI2b2Host(String inI2b2Host) {
		this.i2b2Host = inI2b2Host;
	}

	/**
	 * Get the port number for the i2b2 data source.
	 * 
	 * @return The port number for the i2b2 data source.
	 */
	public Integer getI2b2Port() {
		return this.i2b2Port;
	}

	/**
	 * Set the port number for the i2b2 data source.
	 * 
	 * @param inI2b2Port The port number for the i2b2 data source.
	 */
	public void setI2b2Port(Integer inI2b2Port) {
		this.i2b2Port = inI2b2Port;
	}

	/**
	 * Get the i2b2 meta data schema name.
	 * 
	 * @return The i2b2 meta data schema name.
	 */
	public String getI2b2MetaSchema() {
		return this.i2b2MetaSchema;
	}

	/**
	 * Set the i2b2 meta data schema name.
	 * 
	 * @param inI2b2MetaSchema The i2b2 meta data schema name.
	 */
	public void setI2b2MetaSchema(String inI2b2MetaSchema) {
		this.i2b2MetaSchema = inI2b2MetaSchema;
	}

	/**
	 * Get the i2b2 meta data schema password.
	 * 
	 * @return The i2b2 meta data schema password.
	 */
	public String getI2b2MetaPass() {
		return this.i2b2MetaPass;
	}

	/**
	 * Set the i2b2 meta data schema password.
	 * 
	 * @param inI2b2MetaPass The i2b2 meta data schema password.
	 */
	public void setI2b2MetaPass(String inI2b2MetaPass) {
		this.i2b2MetaPass = inI2b2MetaPass;
	}

	/**
	 * Get the i2b2 data schema name.
	 * 
	 * @return The i2b2 data schema name.
	 */
	public String getI2b2DataSchema() {
		return this.i2b2DataSchema;
	}

	/**
	 * Set the i2b2 data schema name.
	 * 
	 * @param inI2b2DataSchema The i2b2 data schema name.
	 */
	public void setI2b2DataSchema(String inI2b2DataSchema) {
		this.i2b2DataSchema = inI2b2DataSchema;
	}

	/**
	 * Get the i2b2 data schema password.
	 * 
	 * @return The i2b2 data schema password.
	 */
	public String getI2b2DataPass() {
		return this.i2b2DataPass;
	}

	/**
	 * Set the i2b2 data schema password.
	 * 
	 * @param inI2b2DataPass The i2b2 data schema password.
	 */
	public void setI2b2DataPass(String inI2b2DataPass) {
		this.i2b2DataPass = inI2b2DataPass;
	}
}
