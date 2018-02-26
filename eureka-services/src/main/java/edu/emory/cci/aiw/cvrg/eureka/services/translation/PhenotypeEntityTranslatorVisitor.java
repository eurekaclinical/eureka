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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import com.google.inject.Inject;

import edu.emory.cci.aiw.cvrg.eureka.services.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.ValueThresholdGroupEntity;
import edu.emory.cci.aiw.cvrg.eureka.services.entity.PhenotypeEntityVisitor;

import org.eurekaclinical.eureka.client.comm.Phenotype;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;
//import edu.emory.cci.aiw.cvrg.eureka.common.entity.PhenotypeEntityVisitor;

public final class PhenotypeEntityTranslatorVisitor implements
		PhenotypeEntityVisitor {

	private final SystemPropositionTranslator systemPropositionTranslator;
	private final SequenceTranslator sequenceTranslator;
	private final CategorizationTranslator categorizationTranslator;
	private final FrequencyTranslator frequencyTranslator;
	private final ValueThresholdsTranslator valueThresholdsTranslator;
	
	private Phenotype phenotype;

	@Inject
	public PhenotypeEntityTranslatorVisitor(
			SystemPropositionTranslator inSystemPropositionTranslator,
			SequenceTranslator inSequenceTranslator,
			CategorizationTranslator inCategorizationTranslator,
			FrequencyTranslator inFrequencyTranslator,
			ValueThresholdsTranslator inValueThresholdsTranslator) {
		this.systemPropositionTranslator = inSystemPropositionTranslator;
		this.categorizationTranslator = inCategorizationTranslator;
		this.sequenceTranslator = inSequenceTranslator;
		this.frequencyTranslator = inFrequencyTranslator;
		this.valueThresholdsTranslator = inValueThresholdsTranslator;
	}

	public Phenotype getPhenotype() {
		return phenotype;
	}

	@Override
	public void visit(SystemProposition entity) {
		phenotype = this.systemPropositionTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(CategoryEntity entity) {
		phenotype = this.categorizationTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(SequenceEntity entity) {
		phenotype = this.sequenceTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(FrequencyEntity entity) {
		phenotype = this.frequencyTranslator
				.translateFromProposition(entity);
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		phenotype = this.valueThresholdsTranslator
				.translateFromProposition(entity);
	}
}
