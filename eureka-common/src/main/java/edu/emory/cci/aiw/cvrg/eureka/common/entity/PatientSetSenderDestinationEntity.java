package edu.emory.cci.aiw.cvrg.eureka.common.entity;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "ps_sender_destinations")
public class PatientSetSenderDestinationEntity extends DestinationEntity {
	@Column(nullable = false)
	private String url;
	
	@Column(nullable = false)
	private String aliasPropositionId;
	
	@Column(nullable = false)
	private String aliasFieldNameProperty;
	
	@Column(nullable = false)
	private String aliasFieldName;
	
	@Column(nullable = false)
	private String aliasPatientIdProperty;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAliasPropositionId() {
		return aliasPropositionId;
	}

	public void setAliasPropositionId(String aliasPropositionId) {
		this.aliasPropositionId = aliasPropositionId;
	}

	public String getAliasFieldNameProperty() {
		return aliasFieldNameProperty;
	}

	public void setAliasFieldNameProperty(String aliasFieldNameProperty) {
		this.aliasFieldNameProperty = aliasFieldNameProperty;
	}

	public String getAliasFieldName() {
		return aliasFieldName;
	}

	public void setAliasFieldName(String aliasFieldName) {
		this.aliasFieldName = aliasFieldName;
	}

	public String getAliasPatientIdProperty() {
		return aliasPatientIdProperty;
	}

	public void setAliasPatientIdProperty(String aliasPatientIdProperty) {
		this.aliasPatientIdProperty = aliasPatientIdProperty;
	}

	@Override
	public boolean isGetStatisticsSupported() {
		return false;
	}
	
	@Override
	public void accept(DestinationEntityVisitor visitor) {
		visitor.visit(this);
	}
	
}
