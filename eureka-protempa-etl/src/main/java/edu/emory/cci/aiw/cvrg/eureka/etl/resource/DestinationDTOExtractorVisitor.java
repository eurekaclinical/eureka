package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2014 Emory University
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

import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.AuthorizedUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.Neo4jDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetExtractorDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.PatientSetSenderDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.TabularFileDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;

/**
 *
 * @author Andrew Post
 */
public class DestinationDTOExtractorVisitor implements ConfigDTOExtractorVisitor, DestinationEntityVisitor {
	private final CohortDestinationsDTOExtractor cohortExtractor;
	private final I2B2DestinationsDTOExtractor i2b2Extractor;
	private final Neo4jDestinationsDTOExtractor neo4jExtractor;
	private final PatientSetExtractorDestinationsDTOExtractor patientSetExtractorExtractor;
	private final PatientSetSenderDestinationsDTOExtractor patientSetSenderExtractor;
	private final TabularFileDestinationsDTOExtractor tabularFileExtractor;
	private EtlDestination destDTO;

	public DestinationDTOExtractorVisitor(EtlProperties inEtlProperties, AuthorizedUserEntity user, EtlGroupDao inGroupDao) {
		this.cohortExtractor = new CohortDestinationsDTOExtractor(user, inGroupDao);
		this.i2b2Extractor = new I2B2DestinationsDTOExtractor(inEtlProperties, user, inGroupDao);
		this.neo4jExtractor = new Neo4jDestinationsDTOExtractor(inEtlProperties, user, inGroupDao);
		this.patientSetExtractorExtractor = new PatientSetExtractorDestinationsDTOExtractor(user, inGroupDao);
		this.patientSetSenderExtractor = new PatientSetSenderDestinationsDTOExtractor(user, inGroupDao);
		this.tabularFileExtractor = new TabularFileDestinationsDTOExtractor(user, inGroupDao);
	}
	
	@Override
	public void visit(CohortDestinationEntity cohortDestination) {
		this.destDTO = cohortExtractor.extractDTO(cohortDestination);
	}

	@Override
	public void visit(I2B2DestinationEntity i2b2Destination) {
		this.destDTO = i2b2Extractor.extractDTO(i2b2Destination);
	}

	@Override
	public void visit(Neo4jDestinationEntity neo4jDestination) {
		this.destDTO = this.neo4jExtractor.extractDTO(neo4jDestination);
	}
	
	@Override
	public void visit(PatientSetExtractorDestinationEntity patientSetExtractorDestination) {
		this.destDTO = this.patientSetExtractorExtractor.extractDTO(patientSetExtractorDestination);
	}
	
	@Override
	public void visit(PatientSetSenderDestinationEntity patientSetSenderDestination) {
		this.destDTO = this.patientSetSenderExtractor.extractDTO(patientSetSenderDestination);
	}
	
	@Override
	public void visit(TabularFileDestinationEntity tabularFileDestination) {
		this.destDTO = this.tabularFileExtractor.extractDTO(tabularFileDestination);
	}
	
	public EtlDestination getEtlDestination() {
		return this.destDTO;
	}

}
