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
package edu.emory.cci.aiw.cvrg.eureka.common.comm;

import java.io.IOException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Andrew Post
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortDataElementField {

	public enum Type {

		CATEGORIZATION, SEQUENCE, FREQUENCY, VALUE_THRESHOLD, SYSTEM
	}
	
	private String dataElementKey;
	private String dataElementAbbrevDisplayName;
	private String dataElementDisplayName;
	
	private Type type;

	public String getDataElementKey() {
		return dataElementKey;
	}

	public void setDataElementKey(String dataElement) {
		this.dataElementKey = dataElement;
	}

	public String getDataElementAbbrevDisplayName() {
		return dataElementAbbrevDisplayName;
	}

	public void setDataElementAbbrevDisplayName(String inDataElementAbbrevDisplayName) {
		dataElementAbbrevDisplayName = inDataElementAbbrevDisplayName;
	}

	public String getDataElementDisplayName() {
		return dataElementDisplayName;
	}

	public void setDataElementDisplayName(String inDataElementDisplayName) {
		dataElementDisplayName = inDataElementDisplayName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isInSystem() {
		return this.type == Type.SYSTEM;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
