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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.protempa.GapFunction;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.Offsets;
import org.protempa.PropertyDefinition;
import org.protempa.ReferenceDefinition;
import org.protempa.SimpleGapFunction;
import org.protempa.SourceId;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;

public final class HighLevelAbstractionJsonDeserializer extends
        JsonDeserializer<HighLevelAbstractionDefinition> {

	private JsonParser parser;

	@Override
	public HighLevelAbstractionDefinition deserialize(JsonParser jp,
	        DeserializationContext ctxt) throws IOException,
	        JsonProcessingException {
		this.parser = jp;

		if (this.parser.getCurrentToken() == JsonToken.START_OBJECT) {
			nextToken(); // should be the id field
		}

		checkField("id");
		// now we can construct the HLA
		HighLevelAbstractionDefinition value = new HighLevelAbstractionDefinition(
		        this.parser.getText());
		value.setInDataSource(false);
		value.setSolid(true);
		value.setConcatenable(true);
		value.setGapFunction(new SimpleGapFunction());

		nextToken();
		checkField("displayName");
		value.setDisplayName(this.parser.getText());

		nextToken();
		checkField("abbreviatedDisplayName");
		value.setAbbreviatedDisplayName(this.parser.getText());
		
		nextToken();
		checkField("description");
		value.setDescription(this.parser.getText());
		
		nextToken();
		checkField("inverseIsA");
		value.setInverseIsA(this.parser.readValueAs(String[].class));
		
		nextToken();
		checkField("properties");
		value.setPropertyDefinitions(this.parser.readValueAs(PropertyDefinition[].class));
		
		nextToken();
		checkField("references");
		value.setReferenceDefinitions(this.parser.readValueAs(ReferenceDefinition[].class));
		
		nextToken();
		checkField("solid");
		value.setSolid(this.parser.getBooleanValue());
		
		nextToken();
		checkField("concatenable");
		value.setConcatenable(this.parser.getBooleanValue());
		
		nextToken();
		checkField("inDataSource");
		value.setInDataSource(this.parser.getBooleanValue());
		
		nextToken();
		checkField("sourceId");
		SourceId sourceId = this.parser.readValueAs(SourceId.class);
		value.setSourceId(sourceId);

		nextToken();
		checkField("temporalOffset");
		value.setTemporalOffset(this.parser.readValueAs(Offsets.class));
		
		nextToken();
		checkField("gapFunction");
		value.setGapFunction(this.parser.readValueAs(GapFunction.class));
		
		nextToken();
		checkField("defPairs");

		nextToken();
		while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
			checkField("lhs");
			TemporalExtendedPropositionDefinition lhs = this.parser
			        .readValueAs(TemporalExtendedPropositionDefinition.class);

			nextToken();
			checkField("rhs");
			TemporalExtendedPropositionDefinition rhs = this.parser
			        .readValueAs(TemporalExtendedPropositionDefinition.class);

			nextToken();
			checkField("rel");
			Relation rel = this.parser.readValueAs(Relation.class);

			value.add(lhs);
			value.add(rhs);
			value.setRelation(lhs, rhs, rel);

			nextToken();
		}

		return value;
	}

	private void checkField(String field) throws JsonParseException,
	        IOException {
		if (!(this.parser.getCurrentToken() == JsonToken.FIELD_NAME && field
		        .equals(this.parser.getCurrentName()))) {
			fail(field);
		} else {
			nextValue();
		}
	}

	private void nextToken() throws JsonParseException, IOException {
		this.parser.nextToken();
	}

	private void nextValue() throws JsonParseException, IOException {
		this.parser.nextValue();
	}

	private void fail(String property) throws JsonProcessingException {
		throw new JsonParseException(property
		        + " not found in expected location",
		        this.parser.getCurrentLocation());
	}
}
