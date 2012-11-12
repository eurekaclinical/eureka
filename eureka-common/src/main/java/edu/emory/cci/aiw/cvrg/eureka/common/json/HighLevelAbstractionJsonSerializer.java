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
            JsonGenerator g, SerializerProvider p) throws IOException,
            JsonProcessingException {
        
        // start HLA
        g.writeStartObject();

        // write out all the gettable fields
        p.defaultSerializeField("id", value.getId(), g);
        p.defaultSerializeField("displayName", value.getDisplayName(), g);
        p.defaultSerializeField("description", value.getDescription(), g);
        p.defaultSerializeField("sourceId", value.getSourceId(), g);

        // special case for handling extended propositions and relations, which
        // are not directly gettable
        Map<List<TemporalExtendedPropositionDefinition>, Relation> defPairs = new HashMap<List<TemporalExtendedPropositionDefinition>, Relation>();
        for (List<TemporalExtendedPropositionDefinition> epdPair : value
                .getTemporalExtendedPropositionDefinitionPairs()) {
            Relation r = value.getRelation(epdPair);
            defPairs.put(epdPair, r);
        }
        
        // start relation map
        g.writeFieldName("defPairs");
        g.writeStartObject();
        for (Map.Entry<List<TemporalExtendedPropositionDefinition>, Relation> e : defPairs.entrySet()) {
            g.writeFieldName("lhs");
            p.defaultSerializeValue(e.getKey().get(0), g);
            g.writeFieldName("rhs");
            p.defaultSerializeValue(e.getKey().get(1), g);
            g.writeFieldName("rel");
            p.defaultSerializeValue(e.getValue(), g);
        }
        g.writeEndObject();
        // end relation map

        // end HLA
        g.writeEndObject();
    }

}
