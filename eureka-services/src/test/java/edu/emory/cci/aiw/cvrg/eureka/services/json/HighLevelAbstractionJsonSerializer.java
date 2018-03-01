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
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.protempa.ExtendedPropositionDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.TemporalPatternOffset;
import org.protempa.proposition.interval.Relation;

public final class HighLevelAbstractionJsonSerializer extends JsonSerializer<HighLevelAbstractionDefinition> {

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
		provider.defaultSerializeField("gapFunction", value.getGapFunction(), jgen);

		jgen.writeFieldName("extendedPropositions");
		jgen.writeStartObject();
		Map<ExtendedPropositionDefinition, Long> indices =
				new HashMap<>();
		int i = 1;
		for (ExtendedPropositionDefinition epd : value.getExtendedPropositionDefinitions()) {
			indices.put(epd, Long.valueOf(i));
			jgen.writeFieldName("" + (i++));
			provider.defaultSerializeValue(epd, jgen);
		}
		jgen.writeEndObject();

		// special case for handling extended propositions and relations, which
		// are not directly gettable
		Map<List<TemporalExtendedPropositionDefinition>, Relation> defPairs =
				new HashMap<>();
		for (List<TemporalExtendedPropositionDefinition> epdPair : value
				.getTemporalExtendedPropositionDefinitionPairs()) {
			Relation r = value.getRelation(epdPair);
			defPairs.put(epdPair, r);
		}

		// start relation map
		jgen.writeFieldName("relations");
		jgen.writeStartObject();
		for (Map.Entry<List<TemporalExtendedPropositionDefinition>, Relation> e : defPairs
				.entrySet()) {
			jgen.writeFieldName("lhs");
			provider.defaultSerializeValue(indices.get(e.getKey().get(0)), jgen);
			jgen.writeFieldName("rhs");
			provider.defaultSerializeValue(indices.get(e.getKey().get(1)), jgen);
			jgen.writeFieldName("relation");
			provider.defaultSerializeValue(e.getValue(), jgen);
		}
		jgen.writeEndObject();
		// end relation map

		jgen.writeFieldName("temporalOffset");
		jgen.writeStartObject();
		TemporalPatternOffset offsets = value.getTemporalOffset();
		if (offsets != null) {
			TemporalExtendedPropositionDefinition start = offsets.getStartTemporalExtendedPropositionDefinition();
			if (start != null) {
				provider.defaultSerializeField("startExtendedProposition", indices.get(start), jgen);
			} else {
				provider.defaultSerializeField("startExtendedProposition", null, jgen);
			}
			provider.defaultSerializeField("startValue", offsets.getStartAbstractParamValue(), jgen);
			provider.defaultSerializeField("startSide", offsets.getStartIntervalSide(), jgen);
			provider.defaultSerializeField("startOffset", offsets.getStartOffset(), jgen);
			provider.defaultSerializeField("startOffsetUnits", offsets.getStartOffsetUnits(), jgen);
			
			TemporalExtendedPropositionDefinition finish = offsets.getFinishTemporalExtendedPropositionDefinition();
			if (finish != null) {
				provider.defaultSerializeField("finishExtendedProposition", indices.get(finish), jgen);
			} else {
				provider.defaultSerializeField("finishExtendedProposition", null, jgen);
			}
			provider.defaultSerializeField("finishValue", offsets.getFinishAbstractParamValue(), jgen);
			provider.defaultSerializeField("finishSide", offsets.getFinishIntervalSide(), jgen);
			provider.defaultSerializeField("finishOffset", offsets.getFinishOffset(), jgen);
			provider.defaultSerializeField("finishOffsetUnits", offsets.getFinishOffsetUnits(), jgen);
		}
		jgen.writeEndObject();
		
		provider.defaultSerializeField("attributes", value.getAttributes(), jgen);
		
		jgen.writeEndObject();

	}
}
