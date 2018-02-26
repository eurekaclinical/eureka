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
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.protempa.ExtendedPropositionDefinition;
import org.protempa.SliceDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;

public final class SliceAbstractionJsonSerializer extends
        JsonSerializer<SliceDefinition> {

	@Override
	public void serialize(SliceDefinition value, JsonGenerator jgen,
	        SerializerProvider provider) throws IOException,
	        JsonProcessingException {
		// start slice
		jgen.writeStartObject();

		provider.defaultSerializeField("@class", value.getClass(), jgen);
		provider.defaultSerializeField("id", value.getId(), jgen);
		provider.defaultSerializeField("displayName", value.getDisplayName(),
		        jgen);
		provider.defaultSerializeField("abbreviatedDisplayName",
		        value.getAbbreviatedDisplayName(), jgen);
		provider.defaultSerializeField("description", value.getDescription(),
		        jgen);
		provider.defaultSerializeField("inverseIsA", value.getInverseIsA(),
		        jgen);
		
		jgen.writeFieldName("extendedPropositions");
		jgen.writeStartObject();
		Map<ExtendedPropositionDefinition, Long> indices =
				new HashMap<>();
		int i = 1;
		for (TemporalExtendedPropositionDefinition epd : value.getTemporalExtendedPropositionDefinitions()) {
			indices.put(epd, Long.valueOf(i));
			jgen.writeFieldName("" + (i++));
			provider.defaultSerializeValue(epd, jgen);
		}
		jgen.writeEndObject();
		
		provider.defaultSerializeField("properties",
		        value.getPropertyDefinitions(), jgen);
		provider.defaultSerializeField("references",
		        value.getReferenceDefinitions(), jgen);
		provider.defaultSerializeField("inDataSource", value.getInDataSource(), jgen);
		provider.defaultSerializeField("sourceId", value.getSourceId(), jgen);

		provider.defaultSerializeField("minIndex", value.getMinIndex(), jgen);
		provider.defaultSerializeField("maxIndex", value.getMaxIndex(), jgen);
		provider.defaultSerializeField("mergedInterval",
		        value.isMergedInterval(), jgen);
		
		provider.defaultSerializeField("attributes", value.getAttributes(), jgen);

		// end slice
		jgen.writeEndObject();
	}

}
