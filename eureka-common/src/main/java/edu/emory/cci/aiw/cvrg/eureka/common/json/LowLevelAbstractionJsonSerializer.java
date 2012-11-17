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
package edu.emory.cci.aiw.cvrg.eureka.common.json;

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
	        JsonGenerator jgen, SerializerProvider p)
	        throws IOException, JsonProcessingException {
		// start LLA
		jgen.writeStartObject();
		
		// general proposition definition stuff
		p.defaultSerializeField("@class", value.getClass(), jgen);
		p.defaultSerializeField("id", value.getId(), jgen);
		p.defaultSerializeField("displayName", value.getDisplayName(), jgen);
		p.defaultSerializeField("abbreviatedDisplayName", value.getAbbreviatedDisplayName(), jgen);
		p.defaultSerializeField("description", value.getDescription(), jgen);
		p.defaultSerializeField("inverseIsA", value.getInverseIsA(), jgen);
		p.defaultSerializeField("abstractedFrom", value.getAbstractedFrom(), jgen);
		p.defaultSerializeField("sourceId", value.getSourceId(), jgen);
		p.defaultSerializeField("algorithm", value.getAlgorithmId(), jgen);
		
		// value definitions
		jgen.writeFieldName("valueDefinitions");
		jgen.writeStartObject();
		for (LowLevelAbstractionValueDefinition valDef : value.getValueDefinitions()) {
			p.defaultSerializeField("id", valDef.getId(), jgen);
			jgen.writeArrayFieldStart("params");
			for (String paramName : valDef.getParameters()) {
				jgen.writeStartObject();
				p.defaultSerializeField("name", paramName, jgen);
				p.defaultSerializeField("value", valDef.getParameterValue(paramName), jgen);
				p.defaultSerializeField("comp", valDef.getParameterComp(paramName), jgen);
				jgen.writeEndObject();
			}
			jgen.writeEndArray();
		}
		jgen.writeEndObject();
		
		// end LLA
		jgen.writeEndObject();
	}

}
