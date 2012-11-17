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
import org.protempa.PairDefinition;

public final class PairAbstractionJsonSerializer extends
        JsonSerializer<PairDefinition> {

	@Override
	public void serialize(PairDefinition value, JsonGenerator jgen,
	        SerializerProvider provider) throws IOException,
	        JsonProcessingException {
		// start pair
		jgen.writeStartObject();
		
		provider.defaultSerializeField("@class", value.getClass(), jgen);
		provider.defaultSerializeField("id", value.getId(), jgen);
		provider.defaultSerializeField("displayName", value.getDisplayName(), jgen);
		provider.defaultSerializeField("abbreviatedDisplayName", value.getAbbreviatedDisplayName(), jgen);
		provider.defaultSerializeField("description", value.getDescription(), jgen);
		provider.defaultSerializeField("inverseIsA", value.getInverseIsA(), jgen);
		provider.defaultSerializeField("properties", value.getPropertyDefinitions(), jgen);
		provider.defaultSerializeField("references", value.getReferenceDefinitions(), jgen);
		provider.defaultSerializeField("solid", value.isSolid(), jgen);
		provider.defaultSerializeField("concatenable", value.isConcatenable(), jgen);
		provider.defaultSerializeField("sourceId", value.getSourceId(), jgen);
		
		provider.defaultSerializeField("secondRequired", value.isSecondRequired(), jgen);
		provider.defaultSerializeField("temporalOffset", value.getTemporalOffset(), jgen);
		provider.defaultSerializeField("lhs", value.getLeftHandProposition(), jgen);
		provider.defaultSerializeField("rhs", value.getRightHandProposition(), jgen);
		provider.defaultSerializeField("rel", value.getRelation(), jgen);
		
		// end pair
		jgen.writeEndObject();
	}

}
