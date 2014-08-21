package edu.emory.cci.aiw.cvrg.eureka.etl.resource;

/*
 * #%L
 * Eureka Protempa ETL
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
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Cohort;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlCohortDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlDestination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.EtlI2B2Destination;
import edu.emory.cci.aiw.cvrg.eureka.common.comm.Node;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortDestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.CohortEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.DestinationGroupMembership;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlGroup;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.EtlUserEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.I2B2DestinationEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.NodeEntity;
import edu.emory.cci.aiw.cvrg.eureka.common.entity.NodeToNodeEntityVisitor;
import edu.emory.cci.aiw.cvrg.eureka.common.exception.HttpStatusException;
import edu.emory.cci.aiw.cvrg.eureka.etl.config.EtlProperties;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.DestinationDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.EtlGroupDao;
import edu.emory.cci.aiw.cvrg.eureka.etl.dao.ResolvedPermissions;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Post
 */
public final class Destinations extends Configs<EtlDestination, DestinationEntity> {

	private final XPathExpression displayNameExpr;
	private final DocumentBuilderFactory docBuilderFactory;
	private final EtlGroupDao groupDao;
	private final EtlUserEntity etlUser;
	private final DestinationDao destinationDao;

	public Destinations(EtlProperties inEtlProperties, EtlUserEntity inEtlUser, 
			DestinationDao inDestinationDao, EtlGroupDao inGroupDao) {
		super(inEtlProperties, inEtlUser, inDestinationDao);
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			displayNameExpr = xpath.compile("/queryResultsHandler/displayName/text()");
		} catch (XPathExpressionException ex) {
			throw new AssertionError("Invalid xpath expression: " + ex.getMessage());
		}
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		this.docBuilderFactory.setNamespaceAware(true); // never forget this!
		this.groupDao = inGroupDao;
		this.etlUser = inEtlUser;
		this.destinationDao = inDestinationDao;
	}
	
	public void create(EtlDestination etlDestination) {
		if (etlDestination instanceof EtlCohortDestination) {
			CohortDestinationEntity cde = new CohortDestinationEntity();
			cde.setName(etlDestination.getName());
			cde.setDescription(etlDestination.getDescription());
			cde.setOwner(this.etlUser);
			Cohort cohort = ((EtlCohortDestination) etlDestination).getCohort();
			CohortEntity cohortEntity = new CohortEntity();
			Node node = cohort.getNode();
			NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
			node.accept(v);
			cohortEntity.setNode(v.getNodeEntity());
			cde.setCohort(cohortEntity);
			this.destinationDao.create(cde);
		} else if (etlDestination instanceof EtlI2B2Destination) {
			throw new HttpStatusException(Response.Status.BAD_REQUEST, "Can't create i2b2 destinations via web services yet");
		} else {
			throw new AssertionError("Unexpected destination type " + etlDestination.getClass());
		}
	}
	
	public void update(EtlDestination etlDestination) {
		if (etlDestination instanceof EtlCohortDestination) {
			if (!this.etlUser.getId().equals(etlDestination.getOwnerUserId())) {
				throw new HttpStatusException(Response.Status.NOT_FOUND);
			}
			CohortDestinationEntity cde = new CohortDestinationEntity();
			cde.setId(etlDestination.getId());
			cde.setName(etlDestination.getName());
			cde.setDescription(etlDestination.getDescription());
			cde.setOwner(this.etlUser);
			Cohort cohort = ((EtlCohortDestination) etlDestination).getCohort();
			CohortEntity cohortEntity = new CohortEntity();
			cohortEntity.setId(cohort.getId());
			Node node = cohort.getNode();
			NodeToNodeEntityVisitor v = new NodeToNodeEntityVisitor();
			node.accept(v);
			cohortEntity.setNode(v.getNodeEntity());
			this.destinationDao.update(cde);
		} else if (etlDestination instanceof EtlI2B2Destination) {
			throw new HttpStatusException(Response.Status.BAD_REQUEST, "Can't update i2b2 destinations via web services yet");
		} else {
			throw new AssertionError("Unexpected destination type " + etlDestination.getClass());
		}
	}

	@Override
	EtlDestination extractDTO(Perm perm, 
			DestinationEntity destinationEntity) {
		try {
			EtlDestination dest;
			if (destinationEntity instanceof I2B2DestinationEntity) {
				DocumentBuilder builder = this.docBuilderFactory.newDocumentBuilder();
				dest = new EtlI2B2Destination();
				Document doc = builder.parse(getEtlProperties().destinationConfigFile(destinationEntity.getName()));
				String displayName = (String) displayNameExpr.evaluate(doc, XPathConstants.STRING);
				dest.setName(displayName);
			} else if (destinationEntity instanceof CohortDestinationEntity) {
				EtlCohortDestination cohortDest = new EtlCohortDestination();
				cohortDest.setName(destinationEntity.getName());
				cohortDest.setDescription(destinationEntity.getDescription());
				cohortDest.setCohort(((CohortDestinationEntity) destinationEntity).getCohort().toCohort());
				dest = cohortDest;
			} else {
				throw new AssertionError("Unexpected destination type " + destinationEntity.getClass());
			}
			
			dest.setId(destinationEntity.getId());
			
			dest.setRead(perm.read);
			dest.setWrite(perm.write);
			dest.setExecute(perm.execute);
			
			dest.setOwnerUserId(destinationEntity.getOwner().getId());
			
			
			return dest;
		} catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException ex) {
			throw new HttpStatusException(Response.Status.INTERNAL_SERVER_ERROR, ex);
		}
	}

	List<DestinationEntity> configs(EtlUserEntity user) {
		return user.getDestinations();
	}

	List<DestinationGroupMembership> groupConfigs(EtlGroup group) {
		return group.getDestinations();
	}

	String toConfigId(File file) {
		return FromConfigFile.toDestId(file);
	}

	@Override
	ResolvedPermissions resolvePermissions(EtlUserEntity owner, DestinationEntity entity) {
		return this.groupDao.resolveDestinationPermissions(owner, entity);
	}
	
}
