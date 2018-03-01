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
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.protempa.Attribute;
import org.protempa.GapFunction;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionValueDefinition;
import org.protempa.PropertyDefinition;
import org.protempa.ReferenceDefinition;
import org.protempa.SlidingWindowWidthMode;
import org.protempa.SourceId;
import org.protempa.proposition.value.Unit;
import org.protempa.proposition.value.Value;
import org.protempa.proposition.value.ValueComparator;
import org.protempa.proposition.value.ValueType;

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
		        this.parser.readValueAs(String.class));
		value.setInDataSource(false);

		nextToken();
		checkField("displayName");
		value.setDisplayName(this.parser.readValueAs(String.class));

		nextToken();
		checkField("abbreviatedDisplayName");
		value.setAbbreviatedDisplayName(this.parser.readValueAs(String.class));

		nextToken();
		checkField("description");
		value.setDescription(this.parser.readValueAs(String.class));
		
		nextToken();
		checkField("algorithm");
		value.setAlgorithmId(this.parser.readValueAs(String.class));

		nextToken();
		checkField("inverseIsA");
		value.setInverseIsA(this.parser.readValueAs(String[].class));

		nextToken();
		checkField("abstractedFrom");
		@SuppressWarnings("unchecked")
		// abstractedFrom is always a set of strings
		Set<String> abstractedFrom = (Set<String>) this.parser
		        .readValueAs(Set.class);
		for (String af : abstractedFrom) {
			value.addPrimitiveParameterId(af);
		}

		nextToken();
		checkField("properties");
		value.setPropertyDefinitions(this.parser
		        .readValueAs(PropertyDefinition[].class));

		nextToken();
		checkField("references");
		value.setReferenceDefinitions(this.parser
		        .readValueAs(ReferenceDefinition[].class));

		nextToken();
		checkField("sourceId");
		SourceId sourceId = this.parser.readValueAs(SourceId.class);
		value.setSourceId(sourceId);

		nextToken();
		checkField("concatenable");
		value.setConcatenable(this.parser.getBooleanValue());

		nextToken();
		checkField("inDataSource");
		value.setInDataSource(this.parser.getBooleanValue());

		nextToken();
		checkField("gapFunction");
		value.setGapFunction(this.parser.readValueAs(GapFunction.class));
		
		nextToken();
		checkField("minimumDuration");
		if (this.parser.getCurrentToken() != JsonToken.VALUE_NULL) {
			value.setMinimumDuration(this.parser.getIntValue());
		}
		
		nextToken();
		checkField("minimumDurationUnits");
		value.setMinimumDurationUnits(this.parser.readValueAs(Unit.class));
		
		nextToken();
		checkField("maximumDuration");
		if (this.parser.getCurrentToken() != JsonToken.VALUE_NULL) {
			value.setMaximumDuration(this.parser.getIntValue());
		}
		
		nextToken();
		checkField("maximumDurationUnits");
		value.setMaximumDurationUnits(this.parser.readValueAs(Unit.class));
		
		nextToken();
		checkField("valueType");
		value.setValueType(this.parser.readValueAs(ValueType.class));
		
		nextToken();
		checkField("skipStart");
		value.setSkipStart(this.parser.getIntValue());
		
		nextToken();
		checkField("skipEnd");
		value.setSkipEnd(this.parser.getIntValue());
		
		nextToken();
		checkField("skip");
		value.setSkip(this.parser.getIntValue());
		
		nextToken();
		checkField("maxOverlapping");
		value.setMaxOverlapping(this.parser.getIntValue());
		
		nextToken();
		checkField("slidingWindowWidthMode");
		value.setSlidingWindowWidthMode(this.parser.readValueAs(SlidingWindowWidthMode.class));
		
		nextToken();
		checkField("maximumNumberOfValues");
		value.setMaximumNumberOfValues(this.parser.getIntValue());
		
		nextToken();
		checkField("minimumNumberOfValues");
		value.setMinimumNumberOfValues(this.parser.getIntValue());
		
		nextToken();
		checkField("minGapBetweenValues");
		value.setMinimumGapBetweenValues(this.parser.getIntValue());
		
		nextToken();
		checkField("minGapBetweenValuesUnits");
		value.setMinimumGapBetweenValuesUnits(this.parser.readValueAs(Unit.class));
		
		nextToken();
		checkField("maxGapBetweenValues");
		if (this.parser.getCurrentToken() != JsonToken.VALUE_NULL) {
			value.setMaximumGapBetweenValues(this.parser.getIntValue());
		}
		
		nextToken();
		checkField("maxGapBetweenValuesUnits");
		value.setMaximumGapBetweenValuesUnits(this.parser.readValueAs(Unit.class));
		
		nextToken();
		checkField("context");
		value.setContextId(this.parser.readValueAs(String.class));

		nextToken();
		checkField("values");

		nextToken();
		while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
			checkField("id");
			LowLevelAbstractionValueDefinition valDef = new LowLevelAbstractionValueDefinition(
			        value, this.parser.readValueAs(String.class));
			
			nextToken();
			checkField("value");
			valDef.setValue(this.parser.readValueAs(Value.class));

			nextToken();
			checkField("params");

			nextToken();
			while (parser.getCurrentToken() != JsonToken.END_ARRAY) {
				nextToken();
				checkField("name");
				String name = this.parser.readValueAs(String.class);

				nextToken();
				checkField("value");
				Value val = this.parser.readValueAs(Value.class);

				nextToken();
				checkField("comp");
				String compStr = this.parser.readValueAs(String.class);
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
		nextToken();
		
		checkField("attributes");
		value.setAttributes(this.parser.readValueAs(Attribute[].class));
			
		nextToken();

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
