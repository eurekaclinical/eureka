/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.cvrg.eureka.common.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;

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
			sequenceName = "CONFIG_SEQ", allocationSize = 1)
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
	 * The JDBC connect string for the Protempa database.
	 */
	private String protempaJdbcUrl;
	/**
	 * The schema to insert Protempa source data into.
	 */
	private String protempaSchema;
	/**
	 * The password to log into the Protempa source data schema.
	 */
	private String protempaPassword;
	/**
	 * The JDBC connect string for the i2b2 database.
	 */
	private String i2b2JdbcUrl;
	/**
	 * The i2b2 meta data schema name.
	 */
	private String i2b2MetaSchema;
	/**
	 * The i2b2 meta data schema password.
	 */
	private String i2b2MetaPassword;
	/**
	 * The i2b2 data schema name.
	 */
	private String i2b2DataSchema;
	/**
	 * The i2b2 data schema password.
	 */
	private String i2b2DataPassword;

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
	 * Gets the Protempa data source JDBC connect string.
	 * @return The connect string for Protempa source data.
	 */
	public String getProtempaJdbcUrl() {
		return protempaJdbcUrl;
	}

	/**
	 * Sets the Protempa source data JDBC connect string.
	 * @param inProtempaJdbcUrl The connect string.
	 */
	public void setProtempaJdbcUrl(String inProtempaJdbcUrl) {
		protempaJdbcUrl = inProtempaJdbcUrl;
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
	public String getProtempaPassword() {
		return this.protempaPassword;
	}

	/**
	 * Set the Protempa data source password.
	 *
	 * @param inProtempaPassword The Protempa data source password.
	 */
	public void setProtempaPassword(String inProtempaPassword) {
		this.protempaPassword = inProtempaPassword;
	}

	/**
	 * Gets the i2b2 database connect string.
	 * @return The connect string.
	 */
	public String getI2b2JdbcUrl() {
		return i2b2JdbcUrl;
	}

	/**
	 * Sets the i2b2 database connect string.
	 * @param inI2b2JdbcUrl The database connect string.
	 */
	public void setI2b2JdbcUrl(String inI2b2JdbcUrl) {
		i2b2JdbcUrl = inI2b2JdbcUrl;
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
	public String getI2b2MetaPassword() {
		return this.i2b2MetaPassword;
	}

	/**
	 * Set the i2b2 meta data schema password.
	 *
	 * @param inI2b2MetaPassword The i2b2 meta data schema password.
	 */
	public void setI2b2MetaPassword(String inI2b2MetaPassword) {
		this.i2b2MetaPassword = inI2b2MetaPassword;
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
	public String getI2b2DataPassword() {
		return this.i2b2DataPassword;
	}

	/**
	 * Set the i2b2 data schema password.
	 *
	 * @param inI2b2DataPassword The i2b2 data schema password.
	 */
	public void setI2b2DataPassword(String inI2b2DataPassword) {
		this.i2b2DataPassword = inI2b2DataPassword;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
