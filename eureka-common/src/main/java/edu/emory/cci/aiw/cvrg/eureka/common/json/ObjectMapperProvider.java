/*
 * #%L
 * Eureka Services
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

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.protempa.ConstantDefinition;
import org.protempa.DefaultSourceId;
import org.protempa.EventDefinition;
import org.protempa.ExtendedPropositionDefinition;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropertyDefinition;
import org.protempa.ReferenceDefinition;
import org.protempa.SourceId;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.AbsoluteTimeUnit;
import org.protempa.proposition.value.NominalValue;
import org.protempa.proposition.value.Unit;
import org.protempa.proposition.value.Value;


/**
 * Provides custom JSON serialization/deserialization from proposition
 * definitions.
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    private ObjectMapper mapper;

    public ObjectMapperProvider() {
        this.mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule(getClass().getName(),
                new Version(1, 0, 0, "")) {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.setMixInAnnotations(
                        ExtendedPropositionDefinition.class,
                        ExtendedPropositionMixin.class);
                context.setMixInAnnotations(
                        TemporalExtendedPropositionDefinition.class,
                        TemporalExtendedPropositionMixin.class);
                context.setMixInAnnotations(
                        TemporalExtendedParameterDefinition.class,
                        TemporalExtendedParameterMixin.class);
                context.setMixInAnnotations(SourceId.class, SourceIdMixin.class);
                context.setMixInAnnotations(DefaultSourceId.class,
                        DefaultSourceIdMixin.class);
                context.setMixInAnnotations(Unit.class, UnitMixin.class);
                context.setMixInAnnotations(AbsoluteTimeUnit.class,
                        AbsoluteTimeUnitMixin.class);
                context.setMixInAnnotations(Value.class, ValueMixin.class);
                context.setMixInAnnotations(NominalValue.class,
                        NominalValueMixin.class);
                context.setMixInAnnotations(Relation.class, RelationMixin.class);
                context.setMixInAnnotations(ConstantDefinition.class,
                        ConstantMixin.class);
                context.setMixInAnnotations(EventDefinition.class,
                        EventMixin.class);
                context.setMixInAnnotations(PrimitiveParameterDefinition.class,
                        PrimitiveParameterMixin.class);
                context.setMixInAnnotations(PropertyDefinition.class,
                        PropertyMixin.class);
                context.setMixInAnnotations(ReferenceDefinition.class,
                        ReferenceMixin.class);
            }
        };

        module.addSerializer(HighLevelAbstractionDefinition.class,
                new HighLevelAbstractionJsonSerializer());
        module.addDeserializer(HighLevelAbstractionDefinition.class,
                new HighLevelAbstractionJsonDeserializer());
        this.mapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return this.mapper;
    }

}
