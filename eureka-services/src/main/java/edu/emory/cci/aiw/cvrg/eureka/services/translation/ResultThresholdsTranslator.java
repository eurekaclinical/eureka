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
package edu.emory.cci.aiw.cvrg.eureka.services.translation;

import java.util.ArrayList;
import java.util.List;

import edu.emory.cci.aiw.cvrg.eureka.common.comm.ResultThresholds;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.ValueThreshold;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.LowLevelAbstraction;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.SimpleParameterConstraint;

public final class ResultThresholdsTranslator implements
        PropositionTranslator<ResultThresholds, LowLevelAbstraction> {

	@Override
	public LowLevelAbstraction translateFromElement(ResultThresholds element) {
		LowLevelAbstraction result = new LowLevelAbstraction();
		
		PropositionTranslatorUtil.populateCommonPropositionFields(result, element);
		List<SimpleParameterConstraint> spcs = new ArrayList<SimpleParameterConstraint>();
		for (ValueThreshold vt : element.getValueThresholds()) {
			spcs.add(buildSimpleParameterConstraint(vt));
		}
		result.setSimpleParameterConstraints(spcs);
		
		return result;
	}
	
	private SimpleParameterConstraint buildSimpleParameterConstraint(ValueThreshold threshold) {
		SimpleParameterConstraint result = new SimpleParameterConstraint();
		
		return result;
	}

	@Override
	public ResultThresholds translateFromProposition(
	        LowLevelAbstraction proposition) {
		// TODO Auto-generated method stub
		return null;
	}

}
