package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*-
 * #%L
 * Eureka Protempa ETL
 * %%
 * Copyright (C) 2012 - 2017 Emory University
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
import org.eurekaclinical.protempa.client.comm.EtlCohortDestination;
import org.eurekaclinical.protempa.client.comm.EtlDestination;
import org.eurekaclinical.protempa.client.comm.EtlDestinationVisitor;
import org.eurekaclinical.protempa.client.comm.EtlI2B2Destination;
import org.eurekaclinical.protempa.client.comm.EtlNeo4jDestination;
import org.eurekaclinical.protempa.client.comm.EtlPatientSetExtractorDestination;
import org.eurekaclinical.protempa.client.comm.EtlPatientSetSenderDestination;
import org.eurekaclinical.protempa.client.comm.EtlTabularFileDestination;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.AuthorizedUserDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.CohortEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.etl.entity.NodeToNodeEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EncryptionAlgorithmDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.LinkDao;
import java.util.Date;
import javax.inject.Inject;
import org.eurekaclinical.eureka.client.comm.Cohort;
import org.eurekaclinical.eureka.client.comm.Node;

/**
 *
 * @author Andrew Post
 */
public class EtlDestinationToDestinationEntityVisitor implements EtlDestinationVisitor {

	private DestinationEntity destinationEntity;
	private final EtlGroupDao groupDao;
	private final AuthorizedUserDao authUserDao;
	private final LinkDao linkDao;
	private final EncryptionAlgorithmDao encryptionAlgorithmDao;

	@Inject
	public EtlDestinationToDestinationEntityVisitor(EncryptionAlgorithmDao inEncryptionAlgorithmDao, AuthorizedUserDao inAuthUserDao, EtlGroupDao inGroupDao, LinkDao inLinkDao) {
		this.groupDao = inGroupDao;
		this.authUserDao = inAuthUserDao;
		this.linkDao = inLinkDao;
		this.encryptionAlgorithmDao = inEncryptionAlgorithmDao;
	}

	@Override
	public void visit(EtlCohortDestination etlCohortDestination) {
		CohortDestinationEntity cde = new CohortDestinationEntity();
		visitCommon(etlCohortDestination, cde);
		Cohort cohort = etlCohortDestination.getCohort();
		CohortEntity cohortEntity = new CohortEntity();
		Node node = cohort.getNode();
		NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
		node.accept(v);
		cohortEntity.setNode(v.getNodeEntity());
		cde.setCohort(cohortEntity);
		this.destinationEntity = cde;
	}

	private void visitCommon(EtlDestination etlDestination, DestinationEntity destinationEntity) {
		destinationEntity.setId(etlDestination.getId());
		destinationEntity.setDeidentificationEnabled(false);
		destinationEntity.setDescription(etlDestination.getDescription());
		Long encryptionAlgorithm = null;
		if (encryptionAlgorithm != null) {
			destinationEntity.setEncryptionAlgorithm(this.encryptionAlgorithmDao.retrieve(encryptionAlgorithm));
		}
		//Long group = null;
		//if (group != null) {
		//	destinationEntity.setGroups(this.groupDao.retrieve(group));
		//}
		//destinationEntity.setGroups(this.groupDao);
		//destinationEntity.setLinks(this.linkDao.);
		destinationEntity.setName(etlDestination.getName());
		//destinationEntity.setOffsets(inOffsets);
		//destinationEntity.setOutputName(outputName);
		//destinationEntity.setOutputType(outputType);
		Long ownerUserId = etlDestination.getOwnerUserId();
		if (ownerUserId != null) {
			destinationEntity.setOwner(this.authUserDao.retrieve(ownerUserId));
		}
	}

	@Override
	public void visit(EtlI2B2Destination etlI2B2Destination) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void visit(EtlNeo4jDestination etlNeo4jDestination) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void visit(EtlPatientSetExtractorDestination etlPatientSetExtractorDestination) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void visit(EtlPatientSetSenderDestination etlPatientSetSenderDestination) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void visit(EtlTabularFileDestination etlTabularFileDestination) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public DestinationEntity getDestinationEntity() {
		return this.destinationEntity;
	}

}
