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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;

public final class HighLevelAbstractionJsonSerializer extends
        JsonSerializer<HighLevelAbstractionDefinition> {

	@Override
	public void serialize(HighLevelAbstractionDefinition value,
	        JsonGenerator jgen, SerializerProvider provider) throws IOException,
	        JsonProcessingException {

		// start HLA
		jgen.writeStartObject();

		// write out all the gettable fields
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
		provider.defaultSerializeField("inDataSource", value.getInDataSource(), jgen);
		provider.defaultSerializeField("sourceId", value.getSourceId(), jgen);
		
		provider.defaultSerializeField("temporalOffset", value.getTemporalOffset(), jgen);
		provider.defaultSerializeField("gapFunction", value.getGapFunction(), jgen);

		// special case for handling extended propositions and relations, which
		// are not directly gettable
		Map<List<TemporalExtendedPropositionDefinition>, Relation> defPairs = new HashMap<List<TemporalExtendedPropositionDefinition>, Relation>();
		for (List<TemporalExtendedPropositionDefinition> epdPair : value
		        .getTemporalExtendedPropositionDefinitionPairs()) {
			Relation r = value.getRelation(epdPair);
			defPairs.put(epdPair, r);
		}

		// start relation map
		jgen.writeFieldName("defPairs");
		jgen.writeStartObject();
		for (Map.Entry<List<TemporalExtendedPropositionDefinition>, Relation> e : defPairs
		        .entrySet()) {
			jgen.writeFieldName("lhs");
			provider.defaultSerializeValue(e.getKey().get(0), jgen);
			jgen.writeFieldName("rhs");
			provider.defaultSerializeValue(e.getKey().get(1), jgen);
			jgen.writeFieldName("rel");
			provider.defaultSerializeValue(e.getValue(), jgen);
		}
		jgen.writeEndObject();
		// end relation map

		// end HLA
		jgen.writeEndObject();
	}

}
