/*
 * #%L
 * Eureka Common
 * %%
 * Copyright (C) 2012 - 2013 Emory University
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

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.protempa.IntervalSide;

public final class IntervalSideJsonDeserializer extends
        JsonDeserializer<IntervalSide> {

	@Override
	public IntervalSide deserialize(JsonParser parser, DeserializationContext context)
	        throws IOException, JsonProcessingException {
		parser.nextToken();
		parser.nextValue();
		String name = parser.readValueAs(String.class);
		parser.nextToken();
		return IntervalSide.intervalSide(name);
	}

}
