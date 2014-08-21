package edu.emory.cci.aiw.cvrg.eureka.etl.queryresultshandler;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.protempa.dest.keyloader.Criteria;
import org.protempa.proposition.Proposition;

/**
 *
 * @author Andrew Post
 */
class CohortCriteria implements Criteria {
	private Cohort cohort;

	CohortCriteria(Cohort cohort) {
		assert cohort != null : "cohort cannot be null";
		this.cohort = cohort;
	}

	@Override
	public boolean evaluate(List<Proposition> propositions) {
		return this.cohort.evaluate(propositions);
	}

	@Override
	public String[] getPropositionIdsSpecified() {
		return ArrayUtils.EMPTY_STRING_ARRAY;
	}
	
}
