package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
 * %%
 * Copyright (C) 2012 - 2015 Emory University
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

/**
 *
 * @author Andrew Post
 */
public class ConversionSupport {

	public ConversionSupport() {
	}

	public String toPropositionIdWrapped(String dataElementKey) {
		if (dataElementKey == null) {
			return null;
		} else {
			return dataElementKey + ConversionUtil.PROP_ID_WRAPPED_SUFFIX;
		}
	}

	public String toPropositionIdWrapped(DataElementEntity dataElement) {
		return toPropositionIdWrapped(dataElement.getKey());
	}

	public String toPropositionId(String dataElementKey) {
		if (dataElementKey == null || !dataElementKey.startsWith(ConversionUtil.USER_KEY_PREFIX)) {
			return dataElementKey;
		} else {
			return dataElementKey + ConversionUtil.PRIMARY_PROP_ID_SUFFIX;
		}
	}

	public String toPropositionId(DataElementEntity dataElement) {
		return toPropositionId(dataElement.getKey());
	}

	public String toDataElementKey(String propId) {
		if (propId != null && propId.startsWith(ConversionUtil.USER_KEY_PREFIX)) {
			int lastIndexOf
					= propId.lastIndexOf(ConversionUtil.PRIMARY_PROP_ID_SUFFIX);
			if (lastIndexOf > -1) {
				return propId.substring(0, lastIndexOf);
			} else {
				return null;
			}
		} else {
			return propId;
		}
	}
}
