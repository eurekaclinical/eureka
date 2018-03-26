package edu.emory.cci.aiw.cvrg.eureka.etl.dest;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.etl.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.I2B2DestinationRemoveMethod;
import edu.emory.cci.aiw.i2b2etl.dest.RemoveMethod;
import edu.emory.cci.aiw.i2b2etl.dest.config.AbstractSettings;

/**
 *
 * @author Andrew Post
 */
class I2b2Settings extends AbstractSettings {
	private final I2B2DestinationEntity entity;

	I2b2Settings(I2B2DestinationEntity entity) {
		this.entity = entity;
	}

	@Override
	public String getProviderFullName() {
		return entity.getProviderFullName();
	}

	@Override
	public String getProviderFirstName() {
		return entity.getProviderFirstName();
	}

	@Override
	public String getProviderMiddleName() {
		return entity.getProviderMiddleName();
	}

	@Override
	public String getProviderLastName() {
		return entity.getProviderLastName();
	}

	@Override
	public String getVisitDimension() {
		return entity.getVisitDimension();
	}

	@Override
	public boolean getMergeOnUpdate() {
		Boolean mergeOnUpdate = entity.getMergeOnUpdate();
        if (mergeOnUpdate != null) {
            return mergeOnUpdate;
        } else {
            return true;
        }
	}

	@Override
	public boolean getSkipProviderHierarchy() {
		Boolean skipProviderHierarchy1 = entity.getSkipProviderHierarchy();
		if (skipProviderHierarchy1 != null) {
			return skipProviderHierarchy1;
		} else {
			return false;
		}
	}

	@Override
	public boolean getSkipDemographicsHierarchy() {
		Boolean skipDemographicsHierarchy1 = entity.getSkipDemographicsHierarchy();
		if (skipDemographicsHierarchy1 != null) {
			return skipDemographicsHierarchy1;
		} else {
			return false;
		}
	}

	@Override
	public RemoveMethod getDataRemoveMethod() {
		I2B2DestinationRemoveMethod dataRemoveMethod1 = entity.getDataRemoveMethod();
		if (dataRemoveMethod1 != null) {
			return RemoveMethod.valueOf(dataRemoveMethod1.getName());
		} else {
			return null;
		}
	}

	@Override
	public RemoveMethod getMetaRemoveMethod() {
		I2B2DestinationRemoveMethod metaRemoveMethod1 = entity.getMetaRemoveMethod();
		if (metaRemoveMethod1 != null) {
			return RemoveMethod.valueOf(metaRemoveMethod1.getName());
		} else {
			return null;
		}
	}

	@Override
	public String getSourceSystemCode() {
		return entity.getSourceSystemCode();
	}

	@Override
	public String getPatientDimensionMRN() {
		return entity.getPatientDimensionMRN();
	}

	@Override
	public String getPatientDimensionZipCode() {
		return entity.getPatientDimensionZipCode();
	}

	@Override
	public String getPatientDimensionMaritalStatus() {
		return entity.getPatientDimensionMaritalStatus();
	}

	@Override
	public String getPatientDimensionRace() {
		return entity.getPatientDimensionRace();
	}

	@Override
	public String getPatientDimensionBirthdate() {
		return entity.getPatientDimensionBirthdate();
	}

	@Override
	public String getPatientDimensionGender() {
		return entity.getPatientDimensionGender();
	}

	@Override
	public String getPatientDimensionLanguage() {
		return entity.getPatientDimensionLanguage();
	}

	@Override
	public String getPatientDimensionReligion() {
		return entity.getPatientDimensionReligion();
	}

	@Override
	public String getPatientDimensionVital() {
		return entity.getPatientDimensionVital();
	}

	@Override
	public String getRootNodeName() {
		return entity.getRootNodeName();
	}

	@Override
	public String getVisitDimensionId() {
		return entity.getVisitDimensionId();
	}

	@Override
	public String getVisitDimensionInOut() {
		return entity.getVisitDimensionInOut();
	}
	
	@Override
	public String getAgeConceptCodePrefix() {
		return entity.getAgeConceptCodePrefix();
	}

	@Override
	public String getMetaTableName() {
		return entity.getMetaTableName();
	}

	@Override
	public String getPatientDimensionDeathDate() {
		return entity.getPatientDimensionDeathDate();
	}

	@Override
	public boolean getManageCTotalNum() {
		return entity.getManageCTotalNum();
	}

}
