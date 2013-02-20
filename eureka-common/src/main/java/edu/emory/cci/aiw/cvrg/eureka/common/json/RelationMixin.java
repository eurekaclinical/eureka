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

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.protempa.proposition.value.Unit;

public abstract class RelationMixin {
    @JsonCreator
    public RelationMixin(
            @JsonProperty("minDistanceBetweenStarts") Integer minDistanceBetweenStarts,
            @JsonProperty("minDistanceBetweenStartsUnits") Unit minDistanceBetweenStartsUnits,
            @JsonProperty("maxDistanceBetweenStarts") Integer maxDistanceBetweenStarts,
            @JsonProperty("maxDistanceBetweenStartsUnits") Unit maxDistanceBetweenStartsUnits,
            @JsonProperty("minSpan") Integer minSpan,
            @JsonProperty("minSpanUnits") Unit minSpanUnits,
            @JsonProperty("maxSpan") Integer maxSpan,
            @JsonProperty("maxSpanUnits") Unit maxSpanUnits,
            @JsonProperty("minDistanceBetween") Integer minDistanceBetween,
            @JsonProperty("minDistanceBetweenUnits") Unit minDistanceBetweenUnits,
            @JsonProperty("maxDistanceBetween") Integer maxDistanceBetween,
            @JsonProperty("maxDistanceBetweenUnits") Unit maxDistanceBetweenUnits,
            @JsonProperty("minDistanceBetweenFinishes") Integer minDistanceBetweenFinishes,
            @JsonProperty("minDistanceBetweenFinishesUnits") Unit minDistanceBetweenFinishesUnits,
            @JsonProperty("maxDistanceBetweenFinishes") Integer maxDistanceBetweenFinishes,
            @JsonProperty("maxDistanceBetweenFinishesUnits") Unit maxDistanceBetweenFinishesUnits) {
    }
}
