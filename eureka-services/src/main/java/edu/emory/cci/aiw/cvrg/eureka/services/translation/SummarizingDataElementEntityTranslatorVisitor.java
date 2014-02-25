/*
 * #%L
 * Eureka Services
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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Category;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.DataElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Frequency;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Sequence;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.SystemElement;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CategoryEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.FrequencyEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DataElementEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SequenceEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SystemProposition;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.ValueThresholdGroupEntity;

public final class SummarizingDataElementEntityTranslatorVisitor implements
		DataElementEntityVisitor {

	private DataElement dataElement;

	public SummarizingDataElementEntityTranslatorVisitor() {
	}

	public DataElement getDataElement() {
		return dataElement;
	}

	@Override
	public void visit(SystemProposition entity) {
		this.dataElement = new SystemElement();
		populate(entity);
	}

	@Override
	public void visit(CategoryEntity entity) {
		this.dataElement = new Category();
		populate(entity);
	}

	@Override
	public void visit(SequenceEntity entity) {
		this.dataElement = new Sequence();
		populate(entity);
	}

	@Override
	public void visit(FrequencyEntity entity) {
		this.dataElement = new Frequency();
		populate(entity);
	}

	@Override
	public void visit(ValueThresholdGroupEntity entity) {
		this.dataElement = new ValueThresholds();
		populate(entity);
	}

	private void populate(DataElementEntity dataElementEntity) {
		this.dataElement.setSummarized(true);
		PropositionTranslatorUtil.populateCommonDataElementFields(dataElement, 
				dataElementEntity);
	}
	
}
