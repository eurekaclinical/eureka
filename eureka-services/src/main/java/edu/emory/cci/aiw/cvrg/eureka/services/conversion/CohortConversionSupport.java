package edu.emory.cci.aiw.cvrg.eureka.services.conversion;

/*
 * #%L
 * Eureka Services
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

/**
 *
 * @author Andrew Post
 */
class CohortConversionSupport extends ConversionSupport {
	
	CohortConversionSupport() {
	}
	
	Cohort etlCohortToServicesCohort(Cohort etlCohort) {
		Cohort cohort = new Cohort();
		cohort.setId(etlCohort.getId());
		EtlNodeToServicesNodeVisitor v = 
				new EtlNodeToServicesNodeVisitor();
		etlCohort.getNode().accept(v);
		cohort.setNode(v.getNode());
		
		return cohort;
	}
	
	Cohort servicesCohortToEtlCohort(Cohort servicesCohort) {
		Cohort cohort = new Cohort();
		cohort.setId(servicesCohort.getId());
		ServicesNodeToEtlNodeVisitor v =
				new ServicesNodeToEtlNodeVisitor();
		servicesCohort.getNode().accept(v);
		cohort.setNode(v.getNode());
		
		return cohort;
	}

}
