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

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.protempa.Attribute;
import org.protempa.ExtendedPropositionDefinition;
import org.protempa.PropertyDefinition;
import org.protempa.ReferenceDefinition;
import org.protempa.SliceDefinition;
import org.protempa.SourceId;
import org.protempa.TemporalExtendedPropositionDefinition;

public final class SliceAbstractionJsonDeserializer extends
        JsonDeserializer<SliceDefinition> {

	private JsonParser parser;

	@Override
	public SliceDefinition deserialize(JsonParser jp,
	        DeserializationContext ctxt) throws IOException,
	        JsonProcessingException {
		this.parser = jp;

		if (this.parser.getCurrentToken() == JsonToken.START_OBJECT) {
			nextToken(); // should be the id
		}

		checkField("id");
		// now we can construct the slice definition
		SliceDefinition value = new SliceDefinition(
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
		checkField("inverseIsA");
		value.setInverseIsA(this.parser.readValueAs(String[].class));

		nextToken();
		checkField("extendedPropositions");
		nextToken();
		int i = 1;
		Map<Long, ExtendedPropositionDefinition> indices =
				new HashMap<>();
		while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
			checkField("" + i);
			TemporalExtendedPropositionDefinition lhs = this.parser
			        .readValueAs(TemporalExtendedPropositionDefinition.class);
			value.add(lhs);
			indices.put(Long.valueOf(i), lhs);
			nextToken();
			i++;
		}
		
		nextToken();
		checkField("properties");
		value.setPropertyDefinitions(this.parser.readValueAs(PropertyDefinition[].class));
		
		nextToken();
		checkField("references");
		value.setReferenceDefinitions(this.parser.readValueAs(ReferenceDefinition[].class));
		
		nextToken();
		checkField("inDataSource");
		value.setInDataSource(this.parser.getBooleanValue());

		nextToken();
		checkField("sourceId");
		SourceId sourceId = this.parser.readValueAs(SourceId.class);
		value.setSourceId(sourceId);
		
		nextToken();
		checkField("minIndex");
		value.setMinIndex(this.parser.getIntValue());
		
		nextToken();
		checkField("maxIndex");
		value.setMaxIndex(this.parser.getIntValue());
		
		nextToken();
		checkField("mergedInterval");
		value.setMergedInterval(this.parser.getBooleanValue());
		
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
		throw new JsonParseException(property
		        + " not found in expected location",
		        this.parser.getCurrentLocation());
	}
}
