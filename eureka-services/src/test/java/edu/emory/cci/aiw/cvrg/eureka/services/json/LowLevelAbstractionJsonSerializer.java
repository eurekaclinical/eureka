/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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
package edu.emory.cci.aiw.cvrg.eureka.services.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;

public final class LowLevelAbstractionJsonSerializer extends
        JsonSerializer<LowLevelAbstractionDefinition> {

	@Override
	public void serialize(LowLevelAbstractionDefinition value,
	        JsonGenerator jgen, SerializerProvider provider)
	        throws IOException, JsonProcessingException {
		// start LLA
		jgen.writeStartObject();
		
		// general proposition definition stuff
		provider.defaultSerializeField("@class", value.getClass(), jgen);
		provider.defaultSerializeField("id", value.getId(), jgen);
		provider.defaultSerializeField("displayName", value.getDisplayName(), jgen);
		provider.defaultSerializeField("abbreviatedDisplayName", value.getAbbreviatedDisplayName(), jgen);
		provider.defaultSerializeField("description", value.getDescription(), jgen);
		provider.defaultSerializeField("algorithm", value.getAlgorithmId(), jgen);
		provider.defaultSerializeField("inverseIsA", value.getInverseIsA(), jgen);
		provider.defaultSerializeField("abstractedFrom", value.getAbstractedFrom(), jgen);
		provider.defaultSerializeField("properties", value.getPropertyDefinitions(), jgen);
		provider.defaultSerializeField("references", value.getReferenceDefinitions(), jgen);
		provider.defaultSerializeField("sourceId", value.getSourceId(), jgen);
		provider.defaultSerializeField("concatenable", value.isConcatenable(), jgen);
		provider.defaultSerializeField("inDataSource", value.getInDataSource(), jgen);
		
		provider.defaultSerializeField("gapFunction", value.getGapFunction(), jgen);
		provider.defaultSerializeField("minimumDuration", value.getMinimumDuration(), jgen);
		provider.defaultSerializeField("minimumDurationUnits", value.getMinimumDurationUnits(), jgen);
		provider.defaultSerializeField("maximumDuration", value.getMaximumDuration(), jgen);
		provider.defaultSerializeField("maximumDurationUnits", value.getMaximumDurationUnits(), jgen);
		provider.defaultSerializeField("valueType", value.getValueType(), jgen);
		provider.defaultSerializeField("skipStart", value.getSkipStart(), jgen);
		provider.defaultSerializeField("skipEnd", value.getSkipEnd(), jgen);
		provider.defaultSerializeField("skip", value.getSkip(), jgen);
		provider.defaultSerializeField("maxOverlapping", value.getMaxOverlapping(), jgen);
		provider.defaultSerializeField("slidingWindowWidthMode", value.getSlidingWindowWidthMode(), jgen);
		provider.defaultSerializeField("maximumNumberOfValues", value.getMaximumNumberOfValues(), jgen);
		provider.defaultSerializeField("minimumNumberOfValues", value.getMinimumNumberOfValues(), jgen);
		provider.defaultSerializeField("minGapBetweenValues", value.getMinimumGapBetweenValues(), jgen);
		provider.defaultSerializeField("minGapBetweenValuesUnits", value.getMinimumGapBetweenValuesUnits(), jgen);
		provider.defaultSerializeField("maxGapBetweenValues", value.getMaximumGapBetweenValues(), jgen);
		provider.defaultSerializeField("maxGapBetweenValuesUnits", value.getMaximumGapBetweenValuesUnits(), jgen);
		
		provider.defaultSerializeField("context", value.getContextId(), jgen);
		
		
		// value definitions
		jgen.writeFieldName("values");
		jgen.writeStartObject();
		for (LowLevelAbstractionValueDefinition valDef : value.getValueDefinitions()) {
			provider.defaultSerializeField("id", valDef.getId(), jgen);
			provider.defaultSerializeField("value", valDef.getValue(), jgen);
			jgen.writeArrayFieldStart("params");
			for (String paramName : valDef.getParameters()) {
				jgen.writeStartObject();
				provider.defaultSerializeField("name", paramName, jgen);
				provider.defaultSerializeField("value", valDef.getParameterValue(paramName), jgen);
				provider.defaultSerializeField("comp", valDef.getParameterComp(paramName), jgen);
				jgen.writeEndObject();
			}
			jgen.writeEndArray();
		}
		jgen.writeEndObject();
		
		provider.defaultSerializeField("attributes", value.getAttributes(), jgen);
		
		// end LLA
		jgen.writeEndObject();
	}

}
