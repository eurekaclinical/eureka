package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
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
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import org.protempa.proposition.value.NominalValue;

/**
 *
 * @author Andrew Post
 */
public class DataElementConversionSupport {

	public DataElementConversionSupport() {
	}

	public String toPropositionIdWrapped(String dataElementKey) {
		return dataElementKey + ConversionUtil.PROP_ID_WRAPPED_SUFFIX;
	}

	public String toPropositionIdWrapped(DataElementEntity dataElement) {
		return toPropositionIdWrapped(dataElement.getKey());
	}

	public String toPropositionId(String dataElementKey) {
		return dataElementKey + ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
	}

	public String toPropositionId(DataElementEntity dataElement) {
		return toPropositionId(dataElement.getKey());
	}

	public String toDataElementKey(String propId) {
		int lastIndexOf
				= propId.lastIndexOf(ConversionUtil.PRIMARY_PROP_ID_SUFFIX);
		return propId.substring(0, lastIndexOf);
	}

	public String asValueString(String dataElementKey) {
		return dataElementKey + ConversionUtil.VALUE_SUFFIX;
	}

	public String asValueString(DataElementEntity dataElement) {
		return asValueString(dataElement.getKey());
	}

	public NominalValue asValue(DataElementEntity dataElement) {
		return NominalValue.getInstance(asValueString(dataElement));
	}

	public NominalValue asValue(String dataElementKey) {
		return NominalValue.getInstance(asValueString(dataElementKey));
	}

}
