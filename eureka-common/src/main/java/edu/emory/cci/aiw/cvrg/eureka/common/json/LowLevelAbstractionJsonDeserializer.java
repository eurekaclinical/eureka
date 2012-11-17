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
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.SourceId;
import org.protempa.proposition.value.Value;
import org.protempa.proposition.value.ValueComparator;

public final class LowLevelAbstractionJsonDeserializer extends
        JsonDeserializer<LowLevelAbstractionDefinition> {

	private JsonParser parser;

	@Override
	public LowLevelAbstractionDefinition deserialize(JsonParser jp,
	        DeserializationContext ctxt) throws IOException,
	        JsonProcessingException {
		this.parser = jp;

		if (this.parser.getCurrentToken() == JsonToken.START_OBJECT) {
			nextToken(); // should be the id field
		}
		checkField("id");
		// now we can construct the LLA
		LowLevelAbstractionDefinition value = new LowLevelAbstractionDefinition(
		        this.parser.getText());
		value.setInDataSource(false);

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
		checkField("abstractedFrom");
		Set<String> abstractedFrom = (Set<String>) this.parser.readValueAs(Set.class);
		for (String af : abstractedFrom) {
			value.addPrimitiveParameterId(af);
		}

		nextToken();
		checkField("sourceId");
		SourceId sourceId = this.parser.readValueAs(SourceId.class);
		value.setSourceId(sourceId);
		
		nextToken();
		checkField("algorithm");
		value.setAlgorithmId(this.parser.getText());

		nextToken();
		checkField("valueDefinitions");

		nextToken();
		while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
			checkField("id");
			LowLevelAbstractionValueDefinition valDef = new LowLevelAbstractionValueDefinition(
			        value, this.parser.getText());
			value.addValueDefinition(valDef);

			nextToken();
			checkField("params");

			nextToken();
			while (parser.getCurrentToken() != JsonToken.END_ARRAY) {
				nextToken();
				checkField("name");
				String name = this.parser.getText();

				nextToken();
				checkField("value");
				Value val = this.parser.readValueAs(Value.class);

				nextToken();
				checkField("comp");
				String compStr = this.parser.getText();
				if (compStr != null && !compStr.isEmpty()
				        && !compStr.equals("null")) {
					ValueComparator comp = ValueComparator.valueOf(compStr);

					valDef.setParameterValue(name, val);
					valDef.setParameterComp(name, comp);
				}

				nextToken();
				nextToken();
			}

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
		throw new JsonParseException("\"" + property + "\""
		        + " not found in expected location. Found \""
		        + this.parser.getCurrentToken() + "\" instead",
		        this.parser.getCurrentLocation());
	}

}
