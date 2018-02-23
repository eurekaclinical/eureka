package edu.emory.cci.aiw.cvrg.eureka.webapp.entity;

/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2014 Emory University
 * %%
 * This program is dual licensed under the Apache 2 and GPLv3 licenses.
 * 
 * Apache License, Version 2.0:
 * 
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
 * 
 * GNU General Public License version 3:
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Andrew Post
 */
@Entity
@Table(name = "i2b2_destinations")
public class I2B2DestinationEntity extends DestinationEntity {

	private String providerFullName;
	private String providerFirstName;
	private String providerMiddleName;
	private String providerLastName;
	private String visitDimension;
	private Boolean skipProviderHierarchy;
	private Boolean skipDemographicsHierarchy;
	private Boolean mergeOnUpdate;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private I2B2DestinationRemoveMethod dataRemoveMethod;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	private I2B2DestinationRemoveMethod metaRemoveMethod;

	private String sourceSystemCode;
	private String patientDimensionMRN;
	private String patientDimensionVital;
	private String patientDimensionReligion;
	private String patientDimensionLanguage;
	private String patientDimensionGender;
	private String patientDimensionBirthdate;
	private String patientDimensionDeathDate;
	private String patientDimensionZipCode;
	private String patientDimensionMaritalStatus;
	private String patientDimensionRace;
	private String rootNodeName;
	private String visitDimensionId;
	private String visitDimensionInOut;
	private String ageConceptCodePrefix;
	private String metaTableName;
	private String metaConnect;
	private String metaUser;
	private String metaPassword;
	private String dataConnect;
	private String dataUser;
	private String dataPassword;

	@Column(nullable = false)
	private Boolean manageCTotalNum = Boolean.FALSE;

	@OneToMany(cascade = CascadeType.ALL, targetEntity = I2B2DestinationDataSpecEntity.class, mappedBy = "destination")
	private List<I2B2DestinationDataSpecEntity> dataSpecs;

	@OneToMany(cascade = CascadeType.ALL, targetEntity = I2B2DestinationConceptSpecEntity.class, mappedBy = "destination")
	private List<I2B2DestinationConceptSpecEntity> conceptSpecs;

	public I2B2DestinationEntity() {
		this.dataSpecs = new ArrayList<>();
		this.conceptSpecs = new ArrayList<>();
	}

	@Override
	public void accept(DestinationEntityVisitor visitor) {
		visitor.visit(this);
	}

	public String getProviderFullName() {
		return this.providerFullName;
	}

	public String getProviderFirstName() {
		return this.providerFirstName;
	}

	public String getProviderMiddleName() {
		return this.providerMiddleName;
	}

	public String getProviderLastName() {
		return this.providerLastName;
	}

	public String getVisitDimension() {
		return this.visitDimension;
	}

	public Boolean getSkipProviderHierarchy() {
		return this.skipProviderHierarchy;
	}

	public Boolean getSkipDemographicsHierarchy() {
		return this.skipDemographicsHierarchy;
	}

	public I2B2DestinationRemoveMethod getDataRemoveMethod() {
		return this.dataRemoveMethod;
	}

	public I2B2DestinationRemoveMethod getMetaRemoveMethod() {
		return this.metaRemoveMethod;
	}

	public String getSourceSystemCode() {
		return this.sourceSystemCode;
	}

	public String getPatientDimensionMRN() {
		return this.patientDimensionMRN;
	}

	public String getPatientDimensionZipCode() {
		return this.patientDimensionZipCode;
	}

	public String getPatientDimensionMaritalStatus() {
		return this.patientDimensionMaritalStatus;
	}

	public String getPatientDimensionRace() {
		return this.patientDimensionRace;
	}

	public String getPatientDimensionBirthdate() {
		return this.patientDimensionBirthdate;
	}

	public String getPatientDimensionDeathDate() {
		return patientDimensionDeathDate;
	}

	public void setPatientDimensionDeathDate(String patientDimensionDeathDate) {
		this.patientDimensionDeathDate = patientDimensionDeathDate;
	}

	public String getPatientDimensionGender() {
		return this.patientDimensionGender;
	}

	public String getPatientDimensionLanguage() {
		return this.patientDimensionLanguage;
	}

	public String getPatientDimensionReligion() {
		return this.patientDimensionReligion;
	}

	public String getPatientDimensionVital() {
		return this.patientDimensionVital;
	}

	public String getRootNodeName() {
		return this.rootNodeName;
	}

	public String getVisitDimensionId() {
		return this.visitDimensionId;
	}

	public String getAgeConceptCodePrefix() {
		return this.ageConceptCodePrefix;
	}

	public String getMetaTableName() {
		return this.metaTableName;
	}

	public void setProviderFullName(String providerFullName) {
		this.providerFullName = providerFullName;
	}

	public void setProviderFirstName(String providerFirstName) {
		this.providerFirstName = providerFirstName;
	}

	public void setProviderMiddleName(String providerMiddleName) {
		this.providerMiddleName = providerMiddleName;
	}

	public void setProviderLastName(String providerLastName) {
		this.providerLastName = providerLastName;
	}

	public void setVisitDimension(String visitDimension) {
		this.visitDimension = visitDimension;
	}

	public void setSkipProviderHierarchy(Boolean skipProviderHierarchy) {
		this.skipProviderHierarchy = skipProviderHierarchy;
	}

	public void setSkipDemographicsHierarchy(Boolean skipDemographicsHierarchy) {
		this.skipDemographicsHierarchy = skipDemographicsHierarchy;
	}

	public void setDataRemoveMethod(I2B2DestinationRemoveMethod dataRemoveMethod) {
		this.dataRemoveMethod = dataRemoveMethod;
	}

	public void setMetaRemoveMethod(I2B2DestinationRemoveMethod metaRemoveMethod) {
		this.metaRemoveMethod = metaRemoveMethod;
	}

	public void setSourceSystemCode(String sourceSystemCode) {
		this.sourceSystemCode = sourceSystemCode;
	}

	public void setPatientDimensionMRN(String patientDimensionMRN) {
		this.patientDimensionMRN = patientDimensionMRN;
	}

	public void setPatientDimensionVital(String patientDimensionVital) {
		this.patientDimensionVital = patientDimensionVital;
	}

	public void setPatientDimensionReligion(String patientDimensionReligion) {
		this.patientDimensionReligion = patientDimensionReligion;
	}

	public void setPatientDimensionLanguage(String patientDimensionLanguage) {
		this.patientDimensionLanguage = patientDimensionLanguage;
	}

	public void setPatientDimensionGender(String patientDimensionGender) {
		this.patientDimensionGender = patientDimensionGender;
	}

	public void setPatientDimensionBirthdate(String patientDimensionBirthdate) {
		this.patientDimensionBirthdate = patientDimensionBirthdate;
	}

	public void setPatientDimensionZipCode(String patientDimensionZipCode) {
		this.patientDimensionZipCode = patientDimensionZipCode;
	}

	public void setPatientDimensionMaritalStatus(String patientDimensionMaritalStatus) {
		this.patientDimensionMaritalStatus = patientDimensionMaritalStatus;
	}

	public void setPatientDimensionRace(String patientDimensionRace) {
		this.patientDimensionRace = patientDimensionRace;
	}

	public void setRootNodeName(String rootNodeName) {
		this.rootNodeName = rootNodeName;
	}

	public void setVisitDimensionId(String visitDimensionId) {
		this.visitDimensionId = visitDimensionId;
	}

	public void setAgeConceptCodePrefix(String ageConceptCodePrefix) {
		this.ageConceptCodePrefix = ageConceptCodePrefix;
	}

	public void setMetaTableName(String metaTableName) {
		this.metaTableName = metaTableName;
	}

	public List<I2B2DestinationDataSpecEntity> getDataSpecs() {
		return new ArrayList<>(this.dataSpecs);
	}

	public void setDataSpecs(List<I2B2DestinationDataSpecEntity> inDataSpecs) {
		if (inDataSpecs == null) {
			this.dataSpecs = new ArrayList<>();
		} else {
			this.dataSpecs = new ArrayList<>(inDataSpecs);
		}
	}

	public void addDataSpec(I2B2DestinationDataSpecEntity inDataSpec) {
		if (!this.dataSpecs.contains(inDataSpec)) {
			this.dataSpecs.add(inDataSpec);
			inDataSpec.setDestination(this);
		}
	}

	public void removeDataSpec(I2B2DestinationDataSpecEntity inDataSpec) {
		if (this.dataSpecs.remove(inDataSpec)) {
			inDataSpec.setDestination(null);
		}
	}

	public List<I2B2DestinationConceptSpecEntity> getConceptSpecs() {
		return new ArrayList<>(this.conceptSpecs);
	}

	public void setConceptSpecs(List<I2B2DestinationConceptSpecEntity> inConceptSpecs) {
		if (inConceptSpecs == null) {
			this.conceptSpecs = new ArrayList<>();
		} else {
			this.conceptSpecs = new ArrayList<>(inConceptSpecs);
		}
	}
	
	public void addConceptSpec(I2B2DestinationConceptSpecEntity inConceptSpec) {
		if (!this.conceptSpecs.contains(inConceptSpec)) {
			this.conceptSpecs.add(inConceptSpec);
			inConceptSpec.setDestination(this);
		}
	}
	
	public void removeConceptSpec(I2B2DestinationConceptSpecEntity inConceptSpec) {
		if (this.conceptSpecs.remove(inConceptSpec)) {
			inConceptSpec.setDestination(null);
		}
	}

	public String getMetaConnect() {
		return metaConnect;
	}

	public void setMetaConnect(String metaConnect) {
		this.metaConnect = metaConnect;
	}

	public String getMetaUser() {
		return metaUser;
	}

	public void setMetaUser(String metaUser) {
		this.metaUser = metaUser;
	}

	public String getMetaPassword() {
		return metaPassword;
	}

	public void setMetaPassword(String metaPassword) {
		this.metaPassword = metaPassword;
	}

	public String getDataConnect() {
		return dataConnect;
	}

	public void setDataConnect(String dataConnect) {
		this.dataConnect = dataConnect;
	}

	public String getDataUser() {
		return dataUser;
	}

	public void setDataUser(String dataUser) {
		this.dataUser = dataUser;
	}

	public String getDataPassword() {
		return dataPassword;
	}

	public void setDataPassword(String dataPassword) {
		this.dataPassword = dataPassword;
	}

	public String getVisitDimensionInOut() {
		return this.visitDimensionInOut;
	}

	public void setVisitDimensionInOut(String visitDimensionInOut) {
		this.visitDimensionInOut = visitDimensionInOut;
	}

	public Boolean getMergeOnUpdate() {
		return mergeOnUpdate;
	}

	public void setMergeOnUpdate(Boolean mergeOnUpdate) {
		this.mergeOnUpdate = mergeOnUpdate;
	}

	public Boolean getManageCTotalNum() {
		return manageCTotalNum;
	}

	public void setManageCTotalNum(Boolean manageCTotalNum) {
		if (manageCTotalNum == null) {
			this.manageCTotalNum = Boolean.FALSE;
		} else {
			this.manageCTotalNum = manageCTotalNum;
		}
	}

	@Override
	public boolean isGetStatisticsSupported() {
		return true;
	}

	@Override
	public boolean isAllowingQueryPropositionIds() {
		return true;
	}

}
