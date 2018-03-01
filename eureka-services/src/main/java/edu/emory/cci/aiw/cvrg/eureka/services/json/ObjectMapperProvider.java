/*
 * #%L
 * Eureka Services
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

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.protempa.Attribute;
import org.protempa.ConstantDefinition;
import org.protempa.DefaultSourceId;
import org.protempa.EventDefinition;
import org.protempa.ExtendedPropositionDefinition;
import org.protempa.GapFunction;
import org.protempa.HighLevelAbstractionDefinition;
import org.protempa.LowLevelAbstractionDefinition;
import org.protempa.NotRecordedSourceId;
import org.protempa.PrimitiveParameterDefinition;
import org.protempa.PropertyDefinition;
import org.protempa.PropositionDefinition;
import org.protempa.ReferenceDefinition;
import org.protempa.SliceDefinition;
import org.protempa.SourceId;
import org.protempa.TemporalExtendedParameterDefinition;
import org.protempa.TemporalExtendedPropositionDefinition;
import org.protempa.proposition.interval.Relation;
import org.protempa.proposition.value.AbsoluteTimeUnit;
import org.protempa.proposition.value.NominalValue;
import org.protempa.proposition.value.NumberValue;
import org.protempa.proposition.value.Unit;
import org.protempa.proposition.value.Value;

import org.protempa.CompoundLowLevelAbstractionDefinition;
import org.protempa.ContextDefinition;
import org.protempa.SequentialTemporalPatternDefinition;
import org.protempa.SequentialTemporalPatternDefinition.SubsequentTemporalExtendedPropositionDefinition;
import org.protempa.proposition.value.BooleanValue;

/**
 * Provides custom JSON serialization/deserialization from proposition
 * definitions and other objects.
 */
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private ObjectMapper mapper;

	public ObjectMapperProvider() {
		this.mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule(getClass().getName(),
		        new Version(1, 0, 0, "eureka")) {
			@Override
			public void setupModule(SetupContext context) {
				super.setupModule(context);
				context.setMixInAnnotations(PropositionDefinition.class,
				        PropositionMixin.class);
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
				context.setMixInAnnotations(NotRecordedSourceId.class,
				        NotRecordedSourceIdMixin.class);
				context.setMixInAnnotations(Unit.class, UnitMixin.class);
				context.setMixInAnnotations(AbsoluteTimeUnit.class,
				        AbsoluteTimeUnitMixin.class);
				context.setMixInAnnotations(Value.class, ValueMixin.class);
				context.setMixInAnnotations(NominalValue.class,
				        NominalValueMixin.class);
				context.setMixInAnnotations(BooleanValue.class,
						BooleanValueMixin.class);
				context.setMixInAnnotations(NumberValue.class,
				        NumberValueMixin.class);
				context.setMixInAnnotations(Relation.class, RelationMixin.class);
				context.setMixInAnnotations(GapFunction.class,
				        GapFunctionMixin.class);
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
				context.setMixInAnnotations(ContextDefinition.class,
						ContextDefinitionMixin.class);
				context.setMixInAnnotations(SubsequentTemporalExtendedPropositionDefinition.class,
						SubsequentTemporalExtendedPropositionDefinitionMixin.class);
				context.setMixInAnnotations(SequentialTemporalPatternDefinition.class,
						SequentialTemporalPatternDefinitionMixin.class);
				context.setMixInAnnotations(Attribute.class, AttributeMixin.class);
			}
		};

		// high-level abstractions require custom serialization
		module.addSerializer(HighLevelAbstractionDefinition.class,
		        new HighLevelAbstractionJsonSerializer());
		module.addDeserializer(HighLevelAbstractionDefinition.class,
		        new HighLevelAbstractionJsonDeserializer());

		// low-level abstractions also require custom serialization
		module.addSerializer(LowLevelAbstractionDefinition.class,
		        new LowLevelAbstractionJsonSerializer());
		module.addDeserializer(LowLevelAbstractionDefinition.class,
		        new LowLevelAbstractionJsonDeserializer());
		
		module.addSerializer(CompoundLowLevelAbstractionDefinition.class,
				new CompoundLowLevelAbstractionJsonSerializer());
		module.addDeserializer(CompoundLowLevelAbstractionDefinition.class,
				new CompoundLowLevelAbstractionJsonDeserializer());

		// slice abstractions also require custom serialization
		module.addSerializer(SliceDefinition.class,
		        new SliceAbstractionJsonSerializer());
		module.addDeserializer(SliceDefinition.class,
		        new SliceAbstractionJsonDeserializer());

		this.mapper.registerModule(module);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return this.mapper;
	}

}
